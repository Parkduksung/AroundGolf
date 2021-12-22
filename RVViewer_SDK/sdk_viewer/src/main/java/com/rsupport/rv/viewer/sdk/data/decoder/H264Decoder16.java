package com.rsupport.rv.viewer.sdk.data.decoder;

import android.annotation.TargetApi;
import android.media.MediaCodecInfo;
import android.media.MediaCodecList;
import android.media.MediaFormat;
import android.os.Build.VERSION_CODES;

import java.nio.ByteBuffer;

/**
 * Created by hyosang on 2017. 10. 11..
 */

@TargetApi(VERSION_CODES.JELLY_BEAN)
public class H264Decoder16 extends H264Decoder {
    private ByteBuffer[] inputBuffers = null;
    private ByteBuffer[] outputBuffers = null;

    @Override
    protected void onDecoderStarted() {
        inputBuffers = decoder.getInputBuffers();
        outputBuffers = decoder.getOutputBuffers();
    }

    @Override
    protected ByteBuffer getInputBuffer(int inputBufferIndex) {
        return inputBuffers[inputBufferIndex];
    }

    @Override
    protected ByteBuffer getOutputBuffer(int outputBufferIndex) {
        return outputBuffers[outputBufferIndex];
    }

    @Override
    protected void onOutputBufferChanged() {
        outputBuffers = decoder.getOutputBuffers();
    }

    @Override
    protected String findDecoderForName(MediaFormat format) {
        int count = MediaCodecList.getCodecCount();
        String findMime = format.getString(MediaFormat.KEY_MIME);

        for(int i=0;i<count;i++) {
            MediaCodecInfo codecInfo = MediaCodecList.getCodecInfoAt(i);

            if(!codecInfo.isEncoder()) {
                String[] types = codecInfo.getSupportedTypes();
                for (String type : types) {
                    if (type.equalsIgnoreCase(findMime)) {
                        return codecInfo.getName();
                    }
                }
            }
        }

        return null;
    }
}
