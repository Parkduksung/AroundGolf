package com.rsupport.rv.viewer.sdk.decorder.scap;


import com.rsupport.rv.viewer.sdk.decorder.Converter;
import com.rsupport.rv.viewer.sdk.decorder.model.IModel;

public class scapNotifyMsg implements IModel {

    public short type;
    public short code;
    public int length;

    public Memory memory = new Memory();
    public Invalid invalid = new Invalid();
    public Loading loading = new Loading();
    public InputDesktop inputDesktop = new InputDesktop();

    public void save(byte[] szBuffer, int nStart) {
        int nIndex = nStart;

        type = (short) ((int) szBuffer[nIndex] & 0xff);
        nIndex++;
        code = (short) ((int) szBuffer[nIndex] & 0xff);
        nIndex++;
        length = ((int) Converter.readShortLittleEndian(szBuffer, nIndex) & 0xffff);
        nIndex += 2;

        memory.save(szBuffer, nIndex);
        invalid.save(szBuffer, nIndex);
        loading.save(szBuffer, nIndex);
        inputDesktop.save(szBuffer, nIndex);
    }

    public void push(byte[] szBuffer, int start) {
    }

    public int size() {
        return 4 + memory.size();
    }

    static class Memory implements IModel {
        long Full;
        long Used;

        public void save(byte[] szBuffer, int nStart) {
            int nIndex = nStart;

            Full = Converter.readIntLittleEndian(szBuffer, nIndex) & 0xffffffffL;
            nIndex += 4;
            Used = Converter.readIntLittleEndian(szBuffer, nIndex) & 0xffffffffL;
        }

        public void push(byte[] szBuffer, int start) {
        }

        public int size() {
            return 8;
        }

        public void save2(byte[] szBuffer, int start, int dstOffset, int dstLen) {
            // TODO Auto-generated method stub

        }
    }

    static class Invalid implements IModel {
        long Full;
        long Used;

        public void save(byte[] szBuffer, int nStart) {
            int nIndex = nStart;

            Full = Converter.readIntLittleEndian(szBuffer, nIndex) & 0xffffffffL;
            nIndex += 4;
            Used = Converter.readIntLittleEndian(szBuffer, nIndex) & 0xffffffffL;
        }

        public void push(byte[] szBuffer, int start) {
        }

        public int size() {
            return 8;
        }

        public void save2(byte[] szBuffer, int start, int dstOffset, int dstLen) {
            // TODO Auto-generated method stub

        }
    }

    static class Loading implements IModel {
        long ErrorCode;

        public void save(byte[] szBuffer, int nStart) {
            ErrorCode = Converter.readIntLittleEndian(szBuffer, nStart) & 0xffffffffL;
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

    static class InputDesktop implements IModel {
        long ErrorCode;

        public void save(byte[] szBuffer, int nStart) {

            ErrorCode = Converter.readIntLittleEndian(szBuffer, nStart) & 0xffffffffL;
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

    public void save2(byte[] szBuffer, int nStart, int dstOffset, int dstLen) {
        int nIndex = nStart;
        int st = 0;

        if (st >= dstOffset) {
            type = (short) ((int) szBuffer[nIndex] & 0xff);
            nIndex++;
        }
        st++;
        if (nIndex - nStart >= dstLen)
            return;

        if (st >= dstOffset) {
            code = (short) ((int) szBuffer[nIndex] & 0xff);
            nIndex++;
        }
        st++;
        if (nIndex - nStart >= dstLen)
            return;

        if (st >= dstOffset) {
            length = ((int) Converter.readShortLittleEndian(szBuffer, nIndex) & 0xffff);
        }
    }
}
