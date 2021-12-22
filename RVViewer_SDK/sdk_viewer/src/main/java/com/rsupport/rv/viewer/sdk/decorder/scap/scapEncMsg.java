package com.rsupport.rv.viewer.sdk.decorder.scap;


import com.rsupport.rv.viewer.sdk.decorder.Converter;
import com.rsupport.rv.viewer.sdk.decorder.model.IModel;

public class scapEncMsg implements IModel {

    public short type;
    public short pad;
    public int RectCount;


    public void save(byte[] szBuffer, int nStart) {
        int nIndex = nStart;

        type = (short) ((int) szBuffer[nIndex] & 0xff);
        nIndex++;
        pad = (short) ((int) szBuffer[nIndex] & 0xff);
        nIndex++;
        RectCount = ((int) Converter.readShortLittleEndian(szBuffer, nIndex) & 0xffff);
    }

    public void push(byte[] szBuffer, int start) {
    }

    public int size() {
        return 4;
    }

    public void save2(byte[] szBuffer, int start, int dstOffset, int dstLen) {
        // TODO Auto-generated method stub
    }
}
