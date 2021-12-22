package com.rsupport.rv.viewer.sdk.data.decoder;

import android.annotation.TargetApi;
import android.media.MediaCodecList;
import android.media.MediaFormat;
import android.os.Build.VERSION_CODES;

import java.nio.ByteBuffer;

/**
 * Created by hyosang on 2017. 10. 11..
 */

@TargetApi(VERSION_CODES.LOLLIPOP)
public class H264Decoder21 extends H264Decoder {
    @Override
    protected void onDecoderStarted() {

    }

    @Override
    protected ByteBuffer getInputBuffer(int inputBufferIndex) {
        return decoder.getInputBuffer(inputBufferIndex);
    }

    @Override
    protected ByteBuffer getOutputBuffer(int outputBufferIndex) {
        return decoder.getOutputBuffer(outputBufferIndex);
    }

    @Override
    protected void onOutputBufferChanged() {
        //do nothing on API 21 or higher
    }

    @Override
    protected String findDecoderForName(MediaFormat format) {
        MediaCodecList mediaCodecList = new MediaCodecList(MediaCodecList.ALL_CODECS);
        return mediaCodecList.findDecoderForFormat(format);

    }
}
