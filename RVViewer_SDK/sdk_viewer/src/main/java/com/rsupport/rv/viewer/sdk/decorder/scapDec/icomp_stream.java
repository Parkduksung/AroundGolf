package com.rsupport.rv.viewer.sdk.decorder.scapDec;

import com.rsupport.rv.viewer.sdk.decorder.model.IModel;
import com.rsupport.rv.viewer.sdk.decorder.viewer.channel.CRCChannel;

public class icomp_stream {
    public static final int Z_INBUF = 64 * 1024 - 1;
    public static final int Z_OUTBUF = 128 * 1024;
    public static final int LZO_MAXBUF = 64 * 1024;

    byte[] m_rawbuf = new byte[Z_OUTBUF + Z_INBUF];
    byte[] m_unlzobuf = new byte[m_rawbuf.length - LZO_MAXBUF + 1];

    int z_rawendIndex;
    int z_rawpopIndex;
    int m_zipbufIndex;

    CRCChannel m_channel;

    unzip m_unzip;
    unlzo m_unlzo;
    boolean m_bMore;

    byte[] bRead = new byte[500];

    public icomp_stream(CRCChannel channel) {
        m_channel = channel;
        init();
    }

    public void init() {
        m_zipbufIndex = Z_OUTBUF;

        z_rawpopIndex = 0;
        z_rawendIndex = 0;

        m_unzip = new unzip();
        m_unlzo = new unlzo();
        m_bMore = false;
    }

    public void read_zip() {
        if (m_bMore) {
            read_from_zip(m_rawbuf, 0, Z_OUTBUF);
        }
    }

    public void read_zip(IModel obj) {
        if (read_zip(bRead, 0, obj.size())) {
            obj.save(bRead, 0);
        }
    }

    public boolean read_zip(byte[] pDst, int offset, int nMaxRead) {
        int nData = get_data_size();

        if (nData >= nMaxRead) {
            System.arraycopy(m_rawbuf, z_rawpopIndex, pDst, offset, nMaxRead);
            z_rawpopIndex += nMaxRead;
            return true;
        }

        System.arraycopy(m_rawbuf, z_rawpopIndex, pDst, offset, nData);
        z_rawendIndex = z_rawpopIndex;
        nMaxRead -= nData;
        offset += nData;

        while (nMaxRead > 0) {
            nData = read_from_zip(m_rawbuf, 0, Math.max(nMaxRead, Z_OUTBUF));
            if (nData < 0) return true;
            z_rawendIndex = nData;
            z_rawpopIndex = 0;

            nData = Math.min(nMaxRead, nData);
            System.arraycopy(m_rawbuf, z_rawpopIndex, pDst, offset, nData);
            z_rawpopIndex += nData;
            nMaxRead -= nData;
            offset += nData;
        }
        return true;
    }

    public int read_zipbyte() {
        read_zip(bRead, 0, 1);
        int c = 0;
        c |= bRead[0] & 0xff;

        return c;
    }

    public byte read_zipbyte2() {
        read_zip(bRead, 0, 1);
        return bRead[0];
    }

    public void read_zipshort(byte[] bytes) {
        read_zip(bytes, 0, 2);
    }

    public void read_lzo(byte[] pDst, int offset, int nMaxRead) {
        int nData = get_data_size();

        if (nData >= nMaxRead) {
            System.arraycopy(m_rawbuf, z_rawpopIndex, pDst, offset, nMaxRead);
            z_rawpopIndex += nMaxRead;
            return;
        }

        System.arraycopy(m_rawbuf, z_rawpopIndex, pDst, offset, nData);
        z_rawendIndex = z_rawpopIndex;
        nMaxRead -= nData;
        offset += nData;

        if (nMaxRead >= Z_OUTBUF) {
            while (nMaxRead > 0) {
                nData = read_from_lzo(pDst, offset);
                offset += nData;
                nMaxRead -= nData;
            }
            return;
        }

        while (nMaxRead > 0) {
            nData = read_from_lzo(pDst, offset);
            if (nData < 0) return;
            z_rawendIndex = nData;
            z_rawpopIndex = 0;

            nData = Math.min(nMaxRead, nData);
            z_rawpopIndex += nData;
            nMaxRead -= nData;
            offset += nData;
        }
    }

    public boolean read(IModel obj) {
        if (read(bRead, obj.size())) {
            obj.save(bRead, 0);
            return true;
        }
        return false;
    }

    public boolean read(byte[] b, int len) {
        return m_channel.readExact(b, 0, len);
    }

    public int read_from_lzo(byte[] buf, int offset) {
        if (m_channel.readExact(bRead, 0, 2)) {
            int packetlen = (bRead[1] & 0xff) << 8 | (bRead[0] & 0xff);
            while (true) {
                if (m_channel.readExact(m_rawbuf, 0, packetlen)) {
                    int decomplen = m_unlzo.decompress(m_unlzobuf, m_rawbuf, packetlen);
                    System.arraycopy(m_unlzobuf, 0, buf, offset, decomplen);
                    return decomplen;
                }
            }
        }
        return (-1);
    }

    public int get_data_size() {
        return z_rawendIndex - z_rawpopIndex;
    }

    public int read_from_zip(byte[] buf, int offset, int len) {
        m_unzip.set_outbuf(buf, offset, len);

        if (m_unzip.get_inbuf_size() == 0) {
            int avail_in = read_from_network();
            m_unzip.set_inbuf(m_rawbuf, m_zipbufIndex, avail_in);
        }

        return m_unzip.decompress();
    }

    private byte[] readLen = new byte[2];

    public int read_from_network() {
        if (m_channel.readExact(readLen, 0, 2)) {
            int len = (readLen[1] & 0xff) << 8 | (readLen[0] & 0xff);

            if (m_channel.readExact(m_rawbuf, m_zipbufIndex, len)) {
                m_bMore = (len == Z_INBUF);
                return len;
            }
        }
        return -1;
    }
}
