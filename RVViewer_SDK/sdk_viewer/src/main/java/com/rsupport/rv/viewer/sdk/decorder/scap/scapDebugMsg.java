package com.rsupport.rv.viewer.sdk.decorder.scap;

import com.rsupport.rv.viewer.sdk.decorder.model.IModel;


public class scapDebugMsg implements IModel {
    short type;
    short[] subtype = new short[3];

    public void save(byte[] szBuffer, int nStart) {
        // TODO Auto-generated method stub
        int nIndex = nStart;

        type = (short) ((int) szBuffer[nIndex] & 0xff);
        nIndex++;
        for (int i = 0; i < subtype.length; i++) {
            subtype[i] = (short) ((int) szBuffer[nIndex] & 0xff);
            nIndex++;
        }
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
