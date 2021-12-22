package com.rsupport.rv.viewer.sdk.decorder.model;


import com.rsupport.rv.viewer.sdk.decorder.Converter;

public class SCAP_CLIENT_INFORMATION implements IModel {

    public long id;
    public SCAP_ENCODER_INFORMATION encoder = new SCAP_ENCODER_INFORMATION();
    public SCAP_DESK_INFORMATION desk = new SCAP_DESK_INFORMATION();

    public void save(byte[] szBuffer, int nStart) {
        int nIndex = nStart;

        id = Converter.readIntLittleEndian(szBuffer, nIndex) & 0xffffffffL;
        nIndex += 4;
        encoder.save(szBuffer, nIndex);
        nIndex += encoder.size();
        desk.save(szBuffer, nIndex);
    }

    public int size() {
        return 4 + encoder.size() + desk.size();
    }

    public void push(byte[] szBuffer, int start) {
        int nIndex = start;

        System.arraycopy(Converter.getBytesFromIntLE((int) id), 0, szBuffer, nIndex, 4);
        nIndex += 4;
        encoder.push(szBuffer, nIndex);
        nIndex += encoder.size();
        desk.push(szBuffer, nIndex);
    }

    public void save2(byte[] szBuffer, int start, int dstOffset, int dstLen) {
    }
}
