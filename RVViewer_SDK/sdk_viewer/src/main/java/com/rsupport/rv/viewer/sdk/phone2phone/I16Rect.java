package com.rsupport.rv.viewer.sdk.phone2phone;


import com.rsupport.rv.viewer.sdk.decorder.Converter;

public class I16Rect {
    public final static int SIZE = 2 * 4;
    public int left,top,right,bottom;

    public I16Rect() {}

    public I16Rect(int l, int t, int r, int b) {
        left = l; top = t; right = r; bottom = b;
    }

    public static I16Rect[] fromStream(byte[] data, int startIndex, int count) {
        I16Rect[] rc = new I16Rect[count];
        int index = startIndex;
        for (int i = 0; i < count; i++) {
            int left = Converter.readShortLittleEndian(data, index);
            index+=2;
            int top = Converter.readShortLittleEndian(data, index);
            index+=2;
            int right = Converter.readShortLittleEndian(data, index);
            index+=2;
            int bottom = Converter.readShortLittleEndian(data, index);
            index+=2;
            rc[i] = new I16Rect(left, top, right, bottom);
        }
        return rc;
    }

    public int getWidth() {
        return right - left;
    }

    public int getHeight() {
        return bottom - top;
    }

    // getWH()
    public int getRectSize() {
        return getWidth() * getHeight();
    }
}
