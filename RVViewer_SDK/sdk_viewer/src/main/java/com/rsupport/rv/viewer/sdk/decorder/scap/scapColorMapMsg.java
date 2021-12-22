package com.rsupport.rv.viewer.sdk.decorder.scap;


import com.rsupport.rv.viewer.sdk.decorder.Converter;
import com.rsupport.rv.viewer.sdk.decorder.model.IModel;
import com.rsupport.rv.viewer.sdk.decorder.model.RGBQUAD;

public class scapColorMapMsg implements IModel {

    public short type;
    public short mode;
    public int count;
    public RGBQUAD ptDelta = new RGBQUAD();

    public void save(byte[] szBuffer, int nStart) {
        int nIndex = nStart;

        type = (short) ((int) szBuffer[nIndex] & 0xff);
        nIndex++;
        mode = (short) ((int) szBuffer[nIndex] & 0xff);
        nIndex++;
        count = ((int) Converter.readShortLittleEndian(szBuffer, nIndex) & 0xffff);
        nIndex += 2;

        ptDelta.save(szBuffer, nIndex);
    }

    public void push(byte[] szBuffer, int nStart) {
        int nIndex = nStart;

        // 패킷 만들기
        szBuffer[nIndex] = (byte) type;
        nIndex++;
        szBuffer[nIndex] = (byte) mode;
        nIndex++;
        System.arraycopy(Converter.getBytesFromShortLE((short) count), 0, szBuffer, nIndex, 2);
        nIndex += 2;
        ptDelta.push(szBuffer, nIndex);

    }

    public int size() {
        return 4 + ptDelta.size();
    }

    public void save2(byte[] szBuffer, int start, int dstOffset, int dstLen) {
        // TODO Auto-generated method stub
    }

}
