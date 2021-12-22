package com.rsupport.rv.viewer.sdk.data.decoder;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.media.MediaCodec;
import android.media.MediaCodec.BufferInfo;
import android.media.MediaFormat;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.util.Size;
import android.view.Surface;


import com.rsupport.rscommon.define.RSErrorCode.Decoder;
import com.rsupport.rscommon.exception.RSException;
import com.rsupport.rv.viewer.sdk.common.log.RLog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * <pre>
 * Android MediaCodec 을 사용한 H264 Decoder
 * {@link MediaCodec}이 추가된 API Level 16 부터 사용이 가능함.
 * </pre>
 */

public abstract class H264Decoder {
    private static final String MIME_AVC = "video/avc";
    protected static final int INPUT_WAIT_US = 50000;
    protected static final int OUTPUT_WAIT_US = 50000;

    protected enum State {
        CREATED,
        STARTED,
        STOPPED
    }

    protected MediaCodec decoder = null;
    protected State currentState = State.CREATED;
    protected int configWidth = 0;
    protected int configHeight = 0;
    protected Surface targetSurface = null;
    private boolean isReceivedKeyFrame = false;

    private CaptureDecoder captureDecoderThread = null;

    protected abstract void onDecoderStarted();
    protected abstract ByteBuffer getInputBuffer(int inputBufferIndex);
    protected abstract ByteBuffer getOutputBuffer(int outputBufferIndex);
    protected abstract void onOutputBufferChanged();
    protected abstract String findDecoderForName(MediaFormat format);

    public static H264Decoder getDecoder() throws RSException {
        if (VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN) {
            if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
                return new H264Decoder21();
            } else {
                return new H264Decoder16();
            }
        }

        RLog.w("Cannot use MediaCodec: Current SDK=" + VERSION.SDK_INT);

        throw new RSException(Decoder.HARDWARE_DECODER_UNSUPPORT);
    }

    public void setSize(int width, int height) throws RSException {
        configWidth = width;
        configHeight = height;

        initDecoder();
    }

    public void setSurface(Surface surface) throws RSException {
        targetSurface = surface;

        initDecoder();
    }

    protected void initDecoder() throws RSException {
        if (isConfigured() && isCreatedState()) {
            try {
                synchronized (this) {
                    decoder = MediaCodec.createDecoderByType(MIME_AVC);

                    if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
                        SupportedCodecSize codecSize = new SupportedCodecSize(decoder, MIME_AVC, new Size(configWidth, configHeight));
                        configWidth = codecSize.getWidth();
                        configHeight = codecSize.getHeight();
                    }

                    MediaFormat format = MediaFormat.createVideoFormat(MIME_AVC, configWidth, configHeight);

                    RLog.i("Config width = " + configWidth + " height = " + configHeight);

                    //check codec supports
                    String codecName = findDecoderForName(format);
                    RLog.i("DECODER: " + codecName);

                    try {
                        if (isCreatedState()) {
                            decoder.configure(format, targetSurface, null, 0);
                            decoder.start();

                            currentState = State.STARTED;

                            onDecoderStarted();
                        }
                    } catch (Exception e) {
                        RLog.w(e);
                        throw new RSException(Decoder.CANNOT_FOUND_DECODER);
                    }
                }

            } catch (IOException e) {
                throw new RSException(Decoder.DECODER_CREATE_FAILED, e);
            }
        }
    }

    protected boolean isConfigured() {
        if ((configWidth > 0) && (configHeight > 0)) {
            if (targetSurface != null) {
                return true;
            }
        }

        return false;
    }

    protected boolean isCreatedState() {
        return (currentState == State.CREATED);
    }

    protected synchronized boolean isRunningState() {
        return (currentState == State.STARTED);
    }

    public boolean inputBufferData(byte[] data, int len) {
        if(!waitForDecoderReady()) return false;

        if(!isReceivedKeyFrame){
            if(new H264Frame(data).isKeyFrame()){
                isReceivedKeyFrame = true;
                RLog.i("received keyFrame");
            }else {
                RLog.w("drop frame");
                return true;
            }
        }

        if(captureDecoderThread != null) {
            if(captureDecoderThread.isNeedData()) {
                captureDecoderThread.inputData(data, len);
            }
        }

        try {
            int inputBufferIndex = decoder.dequeueInputBuffer(INPUT_WAIT_US);

            if (inputBufferIndex >= 0) {
                ByteBuffer inputBuffer = getInputBuffer(inputBufferIndex);
                inputBuffer.clear();
                inputBuffer.put(data, 0, len);

                decoder.queueInputBuffer(inputBufferIndex, 0, len, 0, 0);//System.currentTimeMillis(), 0);

                return true;
            } else {
                switch (inputBufferIndex) {
                    case MediaCodec.INFO_TRY_AGAIN_LATER: {
                        RLog.d("Decoder inputbuffer not available now. Skipped.");
                    }
                    break;

                    default: {
                        RLog.d("Decoder getInputBuffer timed out: " + inputBufferIndex);
                    }
                    break;
                }
            }
        } catch (IllegalStateException e) {
            if (isRunningState()) {
                RLog.w(e);
            }
        }

        return false;
    }

    private boolean waitForDecoderReady() {
        long startTime = System.currentTimeMillis();
        while (!isRunningState()){
            if(System.currentTimeMillis() - startTime > 3000){
                RLog.e("waitForDecoderRunning timeout.");
                return false;
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                RLog.e("stop interrupt");
            }
        }
        return true;
    }

    private boolean isDropFameBeforeIntoKeyFrame = false;

    /**
     * 새로운 key frame 이 output 될때까지 rendering 하지 않는다.
     */
    public void setDropFrameBeforeIntoKeyFrame(){
        isDropFameBeforeIntoKeyFrame = true;
    }

    public synchronized boolean renderOutput() {
        if (!isRunningState()) return false;

        try {
            BufferInfo bufferInfo = new BufferInfo();
            int outputBufferIndex = decoder.dequeueOutputBuffer(bufferInfo, OUTPUT_WAIT_US);

            if (outputBufferIndex >= 0) {
                if(isDropFameBeforeIntoKeyFrame && bufferInfo.flags != MediaCodec.BUFFER_FLAG_KEY_FRAME){
                    decoder.releaseOutputBuffer(outputBufferIndex, false);
                }else {
                    isDropFameBeforeIntoKeyFrame = false;
                    decoder.releaseOutputBuffer(outputBufferIndex, true);
                }
                return true;
            } else {
                switch (outputBufferIndex) {
                    case MediaCodec.INFO_TRY_AGAIN_LATER: {

                    }
                    break;

                    case MediaCodec.INFO_OUTPUT_FORMAT_CHANGED: {
                        MediaFormat bufferFormat = decoder.getOutputFormat();
                        RLog.i("Decoder output format changed: " + bufferFormat);
                    }
                    break;

                    case MediaCodec.INFO_OUTPUT_BUFFERS_CHANGED: {
                        onOutputBufferChanged();
                        RLog.d("outputbuffer changed");
                    }
                    break;

                    default: {
                        RLog.d("Decoder getOutputBuffer returns " + outputBufferIndex);
                    }
                    break;
                }
            }
        } catch (IllegalStateException e) {
            if (isRunningState()) {
                //실행중 상태인데 IllegalStateException. 일단 로그만 찍음.
                RLog.w(e);
            }
        }

        return false;
    }

    public void captureNextFrame(String savePath, ICaptureListener listener) {
        if(captureDecoderThread == null) {
            captureDecoderThread = new CaptureDecoder(savePath, listener);
            captureDecoderThread.start();
        }
    }

    public synchronized void release() {
        currentState = State.STOPPED;

        synchronized(this) {
            //release() 를 동시에 여러곳에서 호출하기 때문에 동기화 처리.
            //최초 NullPointerException 만 방지처리를 하였으나, MediaCodec.stop() 이 재호출되면 hang 현상 발생하여 동기화로 변경함.
            if (decoder != null) {
                try {
                    decoder.stop();
                    decoder.release();
                } catch (IllegalStateException e) {
                    //해재작업중 발생하는 illegalstate 는 무시..
                    RLog.w(e);
                } catch (NullPointerException e) {
                    RLog.w(e);
                }

                decoder = null;
            }
        }

        if((captureDecoderThread != null) && (captureDecoderThread.isAlive())) {
            captureDecoderThread.interrupt();
            captureDecoderThread = null;
        }
    }

    /**
     * <pre>
     * Surface가 연결된 MediaCodec 디코더에서는 output buffer 에 대한 접근이 불가하여,
     * 캡춰를 위한 디코더를 따로 둠.
     * 정상 프레임이 출력되면 파일 저장 후 바로 종료함.
     * </pre>
     */
    private class CaptureDecoder extends Thread {
        private MediaCodec captureDecoder = null;
        private boolean needData = false;

        private String savePath;
        private ICaptureListener listener = null;

        public CaptureDecoder(String savePath, ICaptureListener listener) {
            this.savePath = savePath;
            this.listener = listener;
        }

        @Override
        public void run() {
            RLog.i("[Capture] Capture start");
            try {
                //decide color format
                int colorFormat = YUV2RGB.decideColorFormat();

                if (colorFormat != 0) {
                    int width = 0;
                    int height = 0;
                    byte[] rawBuffer = null;
                    ByteBuffer rgbBuffer = null;

                    //init decoder
                    MediaFormat format = MediaFormat.createVideoFormat(MIME_AVC, 0, 0);
                    format.setInteger(MediaFormat.KEY_COLOR_FORMAT, colorFormat);

                    try {
                        captureDecoder = MediaCodec.createDecoderByType(MIME_AVC);
                        captureDecoder.configure(format, null, null, 0);
                        captureDecoder.start();

                        needData = true;

                        int bufferIndex, decoderColorFormat;
                        BufferInfo bufferInfo = new BufferInfo();
                        int maxLoop = 100;
                        ByteBuffer outputBuffer;
                        while (maxLoop-- > 0) {
                            bufferIndex = captureDecoder.dequeueOutputBuffer(bufferInfo, -1);
                            if (bufferIndex == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED) {
                                format = captureDecoder.getOutputFormat();
                                width = format.getInteger(MediaFormat.KEY_WIDTH);
                                height = format.getInteger(MediaFormat.KEY_HEIGHT);
                                decoderColorFormat = format.getInteger(MediaFormat.KEY_COLOR_FORMAT);

                                RLog.i(String.format("[Capture] format changed: 0x%08X / %dx%d", decoderColorFormat, width, height));

                                if (decoderColorFormat != colorFormat) {
                                    RLog.w("Decoder color format changed to " + decoderColorFormat);
                                }
                            } else if (bufferIndex >= 0) {
                                //frame output.
                                if ((width > 0) && (height > 0)) {
                                    int rawLength = YUV2RGB.getRawByteLen(colorFormat, width, height);

                                    if (rawLength <= bufferInfo.size) {
                                        rawBuffer = new byte[rawLength];

                                        outputBuffer = captureDecoder.getOutputBuffer(bufferIndex);
                                        outputBuffer.get(rawBuffer, 0, rawLength);
                                        captureDecoder.releaseOutputBuffer(bufferIndex, false);

                                        needData = false;

                                        //frame grabbed. stop decoding.
                                        break;
                                    }
                                }
                            }
                        }
                    } catch (IOException e) {
                        RLog.w("[Capture] decoder error");
                        RLog.w(e);

                        throw new RSException(Decoder.CAPTURE_DECODER_CREATE_FAILED, e);
                    }

                    try {
                        //convert YUV to RGB
                        RLog.i("[Capture] Convert YUV to RGB");
                        if (rawBuffer != null) {
                            rgbBuffer = ByteBuffer.allocateDirect(width * height * 4).order(ByteOrder.nativeOrder());

                            YUV2RGB.convert(rgbBuffer, rawBuffer, width, height, colorFormat);

                            RLog.i("[Capture] Creating bitmap...");
                            Bitmap bitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
                            bitmap.copyPixelsFromBuffer(rgbBuffer);

                            RLog.i("[Capture] Save to file: " + savePath);
                            FileOutputStream fos = new FileOutputStream(new File(savePath));
                            bitmap.compress(CompressFormat.PNG, 100, fos);
                            fos.close();

                            RLog.i("[Capture] completed.");

                            if (listener != null) {
                                listener.onCaptured(savePath);
                            }
                        }
                    } catch (IOException e) {
                        RLog.w("[Capture] Save file error");
                        RLog.w(e);

                        throw new RSException(Decoder.CAPTURE_IO_EXCEPTION, e);
                    }
                }
            }catch(RSException e) {
                if(listener != null) {
                    listener.onError(e);
                }
            }finally {
                if (captureDecoder != null) {
                    needData = false;
                    captureDecoder.stop();
                }
            }

            captureDecoder = null;
            captureDecoderThread = null;
        }



        public boolean isNeedData() {
            return needData;
        }

        public void inputData(byte[] data, int len) {
            int bufferIndex = captureDecoder.dequeueInputBuffer(10000);
            if(bufferIndex >= 0) {
                ByteBuffer inputBuffer = captureDecoder.getInputBuffer(bufferIndex);
                inputBuffer.put(data, 0, len);
                captureDecoder.queueInputBuffer(bufferIndex, 0, len, 0, 0);
            }
        }
    }

    public interface ICaptureListener {
        void onCaptured(String path);
        void onError(RSException exception);
    }
}
