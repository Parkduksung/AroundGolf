package com.rsupport.rv.viewer.sdk.data.decoder;

import android.media.MediaCodecInfo;
import android.media.MediaCodecInfo.CodecCapabilities;
import android.media.MediaCodecList;

import com.rsupport.rscommon.define.RSErrorCode.Decoder;
import com.rsupport.rscommon.exception.RSException;
import com.rsupport.rv.viewer.sdk.common.log.RLog;

import java.nio.ByteBuffer;
import java.util.HashSet;
import java.util.Set;

/**
 * Converts YUV color format to RGB
 * MediaCodec decoder 에서 나온 outputBuffer의 데이터를 처리하는데 사용한다.
 * Created by hyosang on 2017. 10. 24..
 */

public class YUV2RGB {
    public static final int OMX_QCOM_COLOR_Format_YUV420PackedSemiPlanar32m = 0x7FA30C04;

    public static void convert(ByteBuffer rgbBuffer, byte[] yuv, int width, int height, int colorFormat) throws RSException {
        switch(colorFormat) {
            case CodecCapabilities.COLOR_FormatYUV420SemiPlanar:
            case OMX_QCOM_COLOR_Format_YUV420PackedSemiPlanar32m:
                convertYUV420SP(rgbBuffer, yuv, width, height);
                break;

            case CodecCapabilities.COLOR_FormatYUV420Planar:
                convertYUV420P(rgbBuffer, yuv, width, height);
                break;

            default:
                throw new RSException(Decoder.UNSUPPORT_COLOR_FORMAT);
        }

        rgbBuffer.rewind();
    }

    public static int getRawByteLen(int colorFormat, int width, int height) throws RSException {
        switch(colorFormat) {
            case CodecCapabilities.COLOR_FormatYUV420SemiPlanar:
            case CodecCapabilities.COLOR_FormatYUV420Planar:
            case OMX_QCOM_COLOR_Format_YUV420PackedSemiPlanar32m:
                //YUV420: 6 bytes per 4 pixels
                return (int)(Math.ceil((double)(width * height) / 4f)) * 6;

            default:
                throw new RSException(Decoder.UNSUPPORT_COLOR_FORMAT);
        }
    }

    private static void convertYUV420SP(ByteBuffer rgbBuffer, byte[] yuv420sp, int width, int height) {
        if(yuv420sp == null) return;

        int frameSize = width * height;
        int rgbPixelIndex = 0;
        int [] rgb = new int[3];
        for (int j = 0, yp = 0; j < height; j++) {
            int uvp = frameSize + (j >> 1) * width, u = 0, v = 0;
            for (int i = 0; i < width; i++, yp++) {
                int y = (0xff & ((int) yuv420sp[yp])) - 16;
                if (y < 0)
                    y = 0;
                if ((i & 1) == 0) {
                    v = (0xff & yuv420sp[uvp++]) - 128;
                    u = (0xff & yuv420sp[uvp++]) - 128;
                }

                convertYUVtoRGB(y, u, v, rgb);

                rgbBuffer.put(rgbPixelIndex++, (byte)((rgb[2] >> 10) & 0xFF));
                rgbBuffer.put(rgbPixelIndex++, (byte)((rgb[1] >> 10) & 0xFF));
                rgbBuffer.put(rgbPixelIndex++, (byte)((rgb[0] >> 10) & 0xFF));
                rgbBuffer.put(rgbPixelIndex++, (byte) 0xFF);
            }
        }
    }

    private static void convertYUV420P(ByteBuffer rgbBuffer, byte[] yuv420p, int width, int height) {
        if(yuv420p == null) return;

        int frameSize = width * height;
        int yp = 0;
        int up, vp;
        int y = 0, v = 0, u = 0;
        int uvDist = (height >> 2) * width;
        int rgbPixelIndex = 0;
        int[] rgb = new int[3];

        for(int j=0;j<height;j++) {
            up = frameSize + (j >> 2) * width;
            vp = up + uvDist;

            for(int i=0;i<width;i++, yp++) {
                y = Math.max(0, (0xFF & ((int) yuv420p[yp])) - 16);

                if((i & 1) == 0) {
                    v = (0xFF & yuv420p[up++]) - 128;
                    u = (0xFF & yuv420p[vp++]) - 128;
                }

                convertYUVtoRGB(y, u, v, rgb);

                rgbBuffer.put(rgbPixelIndex++, (byte)((rgb[2] >> 10) & 0xFF));
                rgbBuffer.put(rgbPixelIndex++, (byte)((rgb[1] >> 10) & 0xFF));
                rgbBuffer.put(rgbPixelIndex++, (byte)((rgb[0] >> 10) & 0xFF));
                rgbBuffer.put(rgbPixelIndex++, (byte) 0xFF);
            }
        }
    }

    private static void convertYUVtoRGB(int y, int u, int v, int[] rgb) {
        y = y * 1192;
        rgb[0] = Math.min(0x0003FFFF, Math.max(0, (y + 1634 * v)));
        rgb[1] = Math.min(0x0003FFFF, Math.max(0, (y - 833 * v - 400 * u)));
        rgb[2] = Math.min(0x0003FFFF, Math.max(0, (y + 2066 * u)));
    }

    public static int decideColorFormat() throws RSException {
        MediaCodecList codecList = new MediaCodecList(MediaCodecList.REGULAR_CODECS);
        MediaCodecInfo[] infos = codecList.getCodecInfos();

        Set<Integer> colorFormatSet = new HashSet<Integer>();

        for (MediaCodecInfo info : infos) {
            try {
                CodecCapabilities capabilities = info.getCapabilitiesForType("video/avc");
                for (int format : capabilities.colorFormats) {
                    colorFormatSet.add(format);
                }
            } catch (IllegalArgumentException e) {
                //코덱이 해당 MIME을 지원하지 않으면 발생함. 무시.
            }
        }

        if (colorFormatSet.contains(OMX_QCOM_COLOR_Format_YUV420PackedSemiPlanar32m)) {
            RLog.i("Capture color format: OMX_QCOM_COLOR_FormatYUV420PackedSemiPlanar32m");
            return OMX_QCOM_COLOR_Format_YUV420PackedSemiPlanar32m;
        } else if (colorFormatSet.contains(CodecCapabilities.COLOR_FormatYUV420Planar)) {
            RLog.i("Capture color format: YUV420 Planar");
            return CodecCapabilities.COLOR_FormatYUV420Planar;
        } else if (colorFormatSet.contains(CodecCapabilities.COLOR_FormatYUV420SemiPlanar)) {
            RLog.i("Capture color format: YUV420 Semi Planar");
            return CodecCapabilities.COLOR_FormatYUV420SemiPlanar;
        }

        throw new RSException(Decoder.UNSUPPORT_COLOR_FORMAT);
    }

}
