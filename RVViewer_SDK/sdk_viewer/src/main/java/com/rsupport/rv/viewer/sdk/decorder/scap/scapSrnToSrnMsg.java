package com.rsupport.rv.viewer.sdk.decorder.scap;

import com.rsupport.rv.viewer.sdk.decorder.Converter;
import com.rsupport.rv.viewer.sdk.decorder.model.IModel;
import com.rsupport.rv.viewer.sdk.decorder.model.POINTS;

public class scapSrnToSrnMsg implements IModel {

    public short type;
    public short Pad;
    public int RectCount;
    public POINTS ptDelta = new POINTS();

    public void save(byte[] szBuffer, int nStart) {
        int nIndex = nStart;

        type = (short) ((int) szBuffer[nIndex] & 0xff);
        nIndex++;
        Pad = (short) ((int) szBuffer[nIndex] & 0xff);
        nIndex++;
        RectCount = ((int) Converter.readShortLittleEndian(szBuffer, nIndex) & 0xffff);
        nIndex += 2;

        // ptDelta 부분
        ptDelta.x = Converter.readShortLittleEndian(szBuffer, nIndex);
        nIndex += 2;
        ptDelta.y = Converter.readShortLittleEndian(szBuffer, nIndex);
    }

    public void push(byte[] szBuffer, int start) {
    }

    public int size() {
        return 4 + ptDelta.size();
    }

    public void save2(byte[] szBuffer, int nStart, int dstOffset, int dstLen) {
        int nIndex = nStart;
        int st = 0;

        if (st >= dstOffset) {
            type = (short) ((int) szBuffer[nIndex] & 0xff);
            nIndex++;
        }
        st++;
        if (nIndex - nStart >= dstLen) return;

        if (st >= dstOffset) {
            Pad = (short) ((int) szBuffer[nIndex] & 0xff);
            nIndex++;
        }
        st++;
        if (nIndex - nStart >= dstLen) return;

        if (st >= dstOffset) {
            RectCount = ((int) Converter.readShortLittleEndian(szBuffer, nIndex) & 0xffff);
            nIndex += 2;
        }
        st += 2;
        if (nIndex - nStart >= dstLen) return;

        if (st >= dstOffset) {
            ptDelta.x = Converter.readShortLittleEndian(szBuffer, nIndex);
            nIndex += 2;

            ptDelta.y = Converter.readShortLittleEndian(szBuffer, nIndex);
        }
    }

}
