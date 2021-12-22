package com.rsupport.rv.viewer.sdk.decorder.scap;

import com.rsupport.rv.viewer.sdk.decorder.Converter;
import com.rsupport.rv.viewer.sdk.decorder.model.IModel;

public class scapTileCacheMsg implements IModel {

    public short type;
    public short action;
    public int count;

    public void save(byte[] szBuffer, int nStart) {
        int nIndex = nStart;

        type = (short) ((int) szBuffer[nIndex] & 0xff);
        nIndex++;
        action = (short) ((int) szBuffer[nIndex] & 0xff);
        nIndex++;
        count = ((int) Converter.readShortLittleEndian(szBuffer, nIndex) & 0xffff);
    }

    public void push(byte[] szBuffer, int start) {
    }

    public int size() {
        // TODO Auto-generated method stub
        return 4;
    }

    public void save2(byte[] szBuffer, int start, int dstOffset, int dstLen) {
        // TODO Auto-generated method stub

    }

}
