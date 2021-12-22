package com.rsupport.rv.viewer.sdk.decorder.scapDec;


import com.rsupport.rv.viewer.sdk.common.log.RLog;
import com.rsupport.rv.viewer.sdk.decorder.cache.TileCache;
import com.rsupport.rv.viewer.sdk.decorder.model.IModel;
import com.rsupport.rv.viewer.sdk.decorder.model.POINTS;
import com.rsupport.rv.viewer.sdk.decorder.model.RECT;
import com.rsupport.rv.viewer.sdk.decorder.model.RECTS;
import com.rsupport.rv.viewer.sdk.decorder.model.RGBQUAD;
import com.rsupport.rv.viewer.sdk.decorder.model.SCAP_ENCODER_INFORMATION;
import com.rsupport.rv.viewer.sdk.decorder.scap.scapColorMapMsg;
import com.rsupport.rv.viewer.sdk.decorder.scap.scapEncMsg;
import com.rsupport.rv.viewer.sdk.decorder.scap.scapOptEncMsg;
import com.rsupport.rv.viewer.sdk.decorder.scap.scapSrnToSrnMsg;
import com.rsupport.rv.viewer.sdk.decorder.scap.scapTileCacheMsg;

import com.rsupport.rv.viewer.sdk.decorder.viewer.channel.CRCChannel;

public class Decoder {

//	public static final int MAX_TXT_PALETTE = 32;
//	public static final int MAX_JPG_PALETTE = 6;
//	public static final int MAX_COLOR_COUNT = 256;
//	public static final int TWIDTH = 32;
//
//	public static final int ZIPENC_MAX_RECT = 1024*16;
////	public static final int ZIPENC_MAX_RECT = 2000*1600;
//	public static final int ZIPENC_MAX_ZIPBUF = 1024*32;
//	public static final int HIF_MAX_RECT = 512*512;

    public final int MAX_TXT_PALETTE = 32;
    public final int MAX_JPG_PALETTE = 6;
    public final int MAX_COLOR_COUNT = 256;
    public final int TWIDTH = 32;

    public final int ZIPENC_MAX_RECT = 1024*16;
    //	public final int ZIPENC_MAX_RECT = 2000*1600;
    public final int ZIPENC_MAX_ZIPBUF = 1024*32;
    public final int HIF_MAX_RECT = 512*512;

    Jex24Dec m_jpp;
    public TileCache m_tileCache;
    public CanvasHandler m_canvasHandler;

    public icomp_stream	m_uncomp;

    public byte[] m_pNetBuf;
    public int m_pNetBufIndex;
    public int m_nNetBuf;

    //	public JNonBufferingNet m_socket;
    public CRCChannel m_channel;
    public int m_encType;

    byte[] bReceive = new byte[5000];
    byte[] bSend = new byte[500];

    public Decoder() {
        m_pNetBuf = null;
        m_nNetBuf = 0;
        // m_txtZipper = null;

        m_encType = scapOptEncMsg.scapEncodingNull;

        m_nNetBuf = 500;
        m_pNetBuf = new byte[m_nNetBuf];
        m_pNetBufIndex = 0;
    }

    private Decoder m_decoder = null;

    public void setDecoder(Decoder decoder) {
        m_decoder = decoder;
    }

    public boolean Initialize(SCAP_ENCODER_INFORMATION di) {
        if (m_canvasHandler == null)
            return false;

        return true;
    }

    private int m_lowQuality = 10;
    private int m_highQuality = 70;

    public void setLowQuality(int val) {
        m_lowQuality = val;
    }
    public void setHighQuality(int val) {
        m_highQuality = val;
    }
    public void setQuality(int low, int high) {
        Jex24Dec j24Dec = (Jex24Dec)m_decoder;
        j24Dec.m_jppdec.setQuality(low, high);
    }
    public int getLowQuality() {
        return m_lowQuality;
    }
    public int getHighQuality() {
        return m_highQuality;
    }

    private int m_hostBpp = 0;
    public void setHostBpp(int bit) {
        m_hostBpp = bit;
    }
    public int getHostBpp() {
        return m_hostBpp;
    }

    public boolean Read(int nBuf) {
        if (m_nNetBuf < nBuf) {
            m_pNetBuf = new byte[nBuf];
            m_nNetBuf = nBuf;
            m_pNetBufIndex = 0;
        }
        return Read(m_pNetBuf, m_pNetBufIndex, nBuf);
    }

    public boolean Read(byte[] pBuf, int offset, int len) {
        if (!m_channel.readExact(pBuf, offset, len)) {
            RLog.e("Failed to ReadExact");
            return false;
        }
        return true;
    }

    public boolean Read(IModel obj) {
        if (obj.size() > bReceive.length) {
            RLog.e("Failed to ReadExact");
            return false;
        }
        if (Read(bReceive, 0, obj.size())) {
            obj.save(bReceive, 0);
            return true;
        }
        return false;
    }

    public boolean Read(IModel obj, int offset, int size, byte[] tbuf, int tbuf_idx, int tbuf_size) {
        if (obj.size() > bReceive.length) {
            RLog.e("Failed to Decoder.ReadExact");
            return false;
        }
        int st = tbuf_idx;
        int i =0;
        for( i=0; i<tbuf_size; i++, st++) {
            bReceive[i] = tbuf[st];
        }

        if (Read(bReceive, i, size)) {
            obj.save2(bReceive, 0, 0, tbuf_size + size);
            return true;
        }
        return false;
    }

    public boolean WriteExact(IModel obj) {
        obj.push(bSend, 0);
        boolean bRes = m_channel.writeExact(bSend, 0, obj.size());
        if (!bRes) {
            RLog.e("Failed to Decoder.WriteExact");
        }
        return bRes;
    }

    public boolean WriteExact(byte[] sendBuf, int offset, int nSize) {
        boolean bRes = m_channel.writeExact(sendBuf, offset, nSize);
        if (!bRes) {
            RLog.e("Failed to Decoder.WriteExact");
        }
        return bRes;
    }

    byte[] tosendBuf = new byte[1024*3];
    int tosendBufIndex = 0;
    public void WriteExactToBuffer(byte[] sendBuf, int offset, int nSize) {
        System.arraycopy(sendBuf, offset, tosendBuf, tosendBufIndex, nSize);
        tosendBufIndex += nSize;
    }

    public void flushBufferToServer() {
        if(tosendBufIndex == 0)
            return;
        int nSize = tosendBufIndex;
        boolean bRes = m_channel.writeExact(tosendBuf, 0, nSize);
        tosendBufIndex -= nSize;
        if (!bRes) {
            RLog.e("Failed to Decoder.flushBufferToServer");
            System.exit(0);
        }
    }

//	public static void main(String[] arg) {
//		RLog.i (Integer.toBinaryString(4));
//	}

    public void Decode(scapEncMsg pMsg) {
//		RLog.i("decode enc raw");
        int nCount = pMsg.RectCount;
        RECTS src = new RECTS();
        RECT rc = new RECT();

        if (nCount <= 0) {
//			RLog.i("decode enc rect count : " + 0);
            return;
        }

        while (nCount-- > 0) {
            Read(src);

            int revsize = src.getWH()* m_canvasHandler.ByteCount();
            if (m_pNetBuf.length < revsize) {
                m_pNetBuf = new byte[revsize];
            }

            rc.left = src.left;
            rc.right = src.right;
            int nUnitLine = (ZIPENC_MAX_RECT / rc.getWidth());
            for (rc.top = src.top; rc.top < src.bottom; rc.top += nUnitLine) {
                rc.bottom = Math.min(rc.top + nUnitLine, src.bottom);

                rc.bottom = Math.min(rc.top + nUnitLine, rc.bottom);
                int nBits = rc.getWH()* m_canvasHandler.ByteCount();
                m_uncomp.read_lzo(m_pNetBuf,  0, nBits);
                // RLog.i("top : " + rc.top);
                // m_canvas->Update(m_uncomp->read_lzo(), &rc);
                m_canvasHandler.Update(m_pNetBuf, 0, rc);
            }
//			m_canvasHandler.viewer.canvas.repaintRect(src);
        }
//		int trash = this.m_socket.sslNet.getSSLWanted();
//		RLog.i("decode enc raw trash : " + trash);
//		if (trash == 0) return;
        byte[] bytes = new byte[2];
        m_uncomp.read(bytes, 2);
    }


    public void Decode(scapSrnToSrnMsg pMsg) {

        int nRectCount = pMsg.RectCount;
        // int nData = nRectCount*sizeof(RECTS) + sizeof(POINTS);
        int nData = nRectCount*8 + 4;
        if(!Read(nData)) {
            return;
        }

        POINTS ptSrc = new POINTS();
        POINTS ptDelta = new POINTS();
        int prcDstIndex = m_pNetBufIndex;
        ptDelta.save(m_pNetBuf, prcDstIndex);
        prcDstIndex += ptDelta.size();

        RECTS[] prcDst = new RECTS[nRectCount];
        int index = 0;
        prcDst[index] = new RECTS();
        prcDst[index].save(m_pNetBuf, prcDstIndex);

        RECTS rcInv = new RECTS((short) 0x0FFF, (short) 0x0FFF, (short) 0, (short) 0);
        if (ptDelta.x >= 0 && ptDelta.y >= 0)
        {
            prcDstIndex += (nRectCount - 1)* prcDst[index].size();
            index = nRectCount-1;
            do
            {
                prcDst[index] = new RECTS();
                prcDst[index].save(m_pNetBuf, prcDstIndex);

                ptSrc.x = (short) (prcDst[index].left - ptDelta.x);
                ptSrc.y = (short) (prcDst[index].top  - ptDelta.y);

                m_canvasHandler.Srn2Srn(prcDst[index], ptSrc);

                rcInv.left		= (short) Math.min(prcDst[index].left, rcInv.left);
                rcInv.top		= (short) Math.min(prcDst[index].top, rcInv.top);
                rcInv.right		= (short) Math.max(prcDst[index].right, rcInv.right);
                rcInv.bottom	= (short) Math.max(prcDst[index].bottom, rcInv.bottom);

                prcDstIndex -= prcDst[index].size();
                index--;
            } while(--nRectCount > 0);
        }
        else if (ptDelta.x <= 0 && ptDelta.y <= 0)
        {
            ptSrc = new POINTS();
            do
            {
                prcDst[index] = new RECTS();
                prcDst[index].save(m_pNetBuf, prcDstIndex);

                ptSrc.x = (short)(prcDst[index].left - ptDelta.x);
                ptSrc.y = (short)(prcDst[index].top  - ptDelta.y);

                m_canvasHandler.Srn2Srn(prcDst[index], ptSrc);

                prcDstIndex += prcDst[index].size();
                index++;

            } while(--nRectCount > 0);
        }
        else
        {
            if (ptDelta.x > 0)
            {
                RectS_ToTopRight(prcDst, nRectCount);
            }
            else
            {
                RectS_ToBottomLeft(prcDst, nRectCount);
            }

            ptSrc = new POINTS();
            do
            {
                if (prcDst[index] == null || ptDelta == null) continue;
                ptSrc.x = (short) (prcDst[index].left - ptDelta.x);
                ptSrc.y = (short) (prcDst[index].top  - ptDelta.y);
                m_canvasHandler.Srn2Srn(prcDst[index], ptSrc);

                rcInv.left		= (short) Math.min(prcDst[index].left, rcInv.left);
                rcInv.top		= (short) Math.min(prcDst[index].top, rcInv.top);
                rcInv.right		= (short) Math.max(prcDst[index].right, rcInv.right);
                rcInv.bottom	= (short) Math.max(prcDst[index].bottom, rcInv.bottom);

                prcDstIndex += prcDst[index].size();
                index++;
            }
            while(--nRectCount > 0);
        }
    }

    public void Decode(scapTileCacheMsg pMsg) {
    }

    public void Decode(scapColorMapMsg pMsg) {
        int nClrCount = pMsg.count;
        RGBQUAD[] ptDelta = new RGBQUAD[nClrCount];
        for(int i = 0 ; i < ptDelta.length ; i++) {
            ptDelta[i] = new RGBQUAD();
            Read(ptDelta[i]);
        }
        m_canvasHandler.Update(ptDelta, nClrCount);
    }

    public boolean CreateBuffer(int nBitDepth) {
        return true;
    }

    public void setType(int type) {
        m_encType = type;
    }

    public int getType() {
        return m_encType;
    }

    public void SetChannel(CRCChannel channel) { m_channel = channel; }
    public void SetCanvas(CanvasHandler pCanvas)  { m_canvasHandler = pCanvas;}
    public void SetTileCache(TileCache cache)  { m_tileCache = cache;}

    public void SetUncompressor(Jex24Dec obj1, icomp_stream hencBufUnzipper) {
        m_jpp = obj1;
        m_uncomp = hencBufUnzipper;
    }

    public void RectS_ToBottomLeft(RECTS[] prc, int nCount) {
        int nMax;
        RECTS rcTmp = new RECTS();
        for (int i = 0; i < nCount - 1; i++) {
            nMax = i;
            for (int j = i + 1; j < nCount; j++) {
                if (prc[j] == null || prc[nMax] == null) continue;
                if (prc[j].top > prc[nMax].top
                        || (prc[j].top == prc[nMax].top && prc[j].left < prc[nMax].left)) {
                    nMax = j;
                }
            }
            if (nMax != i) {
                rcTmp = prc[nMax];
                prc[nMax] = prc[i];
                prc[i] = rcTmp;
            }
        }
    }

    public void RectS_ToTopRight(RECTS[] prc, int nCount) {
        int nMax;
        RECTS rcTmp = new RECTS();
        for (int i = 0; i < nCount - 1; i++) {
            nMax = i;
            for (int j = i + 1; j < nCount; j++) {
                if (prc[j] == null || prc[nMax] == null) continue;
                if (prc[j].top < prc[nMax].top
                        || (prc[j].top == prc[nMax].top && prc[j].left > prc[nMax].left)) {
                    nMax = j;
                }
            }

            if (nMax != i) {
                rcTmp = prc[nMax];
                prc[nMax] = prc[i];
                prc[i] = rcTmp;
            }
        }
    }

    public void destoryDecorder() { }

}
