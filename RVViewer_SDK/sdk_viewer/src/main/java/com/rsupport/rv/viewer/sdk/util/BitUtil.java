package com.rsupport.rv.viewer.sdk.util;

/**
 * Created by hyosang on 2016. 8. 22..
 */
public class BitUtil {
    /**
     * Single byte to unsigned int
     * @param b a byte
     * @return Unsigned int value
     */
    public static int byteToUInt(byte b) {
        return (b & 0x0000_00FF);
    }

    public static int byte4ToIntLE(byte [] b, int offset) {
        return (b[offset] & 0xFF) |
                ((b[offset+1] & 0xFF) << 8) |
                ((b[offset+2] & 0xFF) << 16) |
                ((b[offset+3] & 0xFF) << 24);
    }

    public static int byte2ToIntLE(byte [] b, int offset) {
        int v = 0;
        v |= ((b[offset] & 0xFF));
        v |= ((b[offset+1] & 0xFF) << 8);

        return v;
    }

    public static short byte2ToShortLE(byte [] b, int offset) {
        return (short)(b[offset] | b[offset+1] << 8);
    }

    public static int intToByte2LE(int val, byte [] b, int offset) {
        b[offset] = (byte)(0x000000FF & val);
        b[offset+1] = (byte)((0x0000FF00 & val) >> 8);

        return offset + 2;
    }

    public static int intToByte4LE(int val, byte [] b, int offset) {
        b[offset] = (byte)(0x000000FF & val);
        b[offset+1] = (byte)((0x0000FF00 & val) >> 8);
        b[offset+2] = (byte)((0x00FF0000 & val) >> 16);
        b[offset+3] = (byte)((0xFF000000 & val) >> 24);

        return offset + 4;
    }

    public static int shortToByte2LE(short val, byte [] b, int offset) {
        b[offset] = (byte)(0x00FF & val);
        b[offset+1] = (byte)((0xFF00 & val) >> 8);

        return offset + 2;
    }
}
