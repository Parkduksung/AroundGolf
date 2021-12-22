package com.rsupport.rv.viewer.sdk.decorder.scapDec;

//import javax.swing.JPanel;


import com.rsupport.rv.viewer.sdk.common.log.RLog;
import com.rsupport.rv.viewer.sdk.decorder.Converter;
import com.rsupport.rv.viewer.sdk.decorder.cache.TileCache;
import com.rsupport.rv.viewer.sdk.decorder.model.RECT;
import com.rsupport.rv.viewer.sdk.decorder.model.RGBQUAD;
import com.rsupport.rv.viewer.sdk.decorder.model.SCAP_CLIENT_INFORMATION;
import com.rsupport.rv.viewer.sdk.decorder.model.SCAP_ENCODER_INFORMATION;
import com.rsupport.rv.viewer.sdk.decorder.model.SCAP_STRETCH_INFORMATION;
import com.rsupport.rv.viewer.sdk.decorder.model.SIZE;
import com.rsupport.rv.viewer.sdk.decorder.scap.ScapContants;
import com.rsupport.rv.viewer.sdk.decorder.scap.etc;
import com.rsupport.rv.viewer.sdk.decorder.scap.scapColorMapMsg;
import com.rsupport.rv.viewer.sdk.decorder.scap.scapCursorMsg;
import com.rsupport.rv.viewer.sdk.decorder.scap.scapCursorPosMsg;
import com.rsupport.rv.viewer.sdk.decorder.scap.scapDebugMsg;
import com.rsupport.rv.viewer.sdk.decorder.scap.scapEncMsg;
import com.rsupport.rv.viewer.sdk.decorder.scap.scapGraphyCacheMsg;
import com.rsupport.rv.viewer.sdk.decorder.scap.scapMsg;
import com.rsupport.rv.viewer.sdk.decorder.scap.scapOptEncMsg;
import com.rsupport.rv.viewer.sdk.decorder.scap.scapSeamlessMsg;
import com.rsupport.rv.viewer.sdk.decorder.scap.scapSrnToSrnMsg;
import com.rsupport.rv.viewer.sdk.decorder.scap.scapTileCacheMsg;
import com.rsupport.rv.viewer.sdk.decorder.scap.scapTileCacheUnit;
import com.rsupport.rv.viewer.sdk.setting.GlobalStatic;

public class DecorderThread {
    Jex24Dec		m_jpp;
    icomp_stream	m_uncomp;

    public Decoder m_decoder;
    public CanvasHandler m_canvas;
    public TileCache m_tileCache;
    public SCAP_CLIENT_INFORMATION m_opts;
    long m_nLastPacket;
    private int nCanvasWidth = 0;
    private int nCanvasHeight = 0;

    public DecorderThread() {
        m_nLastPacket = 0;
        m_opts = new SCAP_CLIENT_INFORMATION();
    }

    public void setFreeCanvasHandler() {
        m_canvas = null;
    }

    static int CalculateRatioLength(int nOrgLength, SIZE ratio)	{
        if (ratio.cx == 0 || ratio.cy == 0)
            return nOrgLength;

        int nGcm = etc.gcm(ratio.cx, ratio.cy);

        ratio.cx /= nGcm;
        ratio.cy /= nGcm;

        if (ratio.cx == ratio.cy)
            return nOrgLength;

        return (nOrgLength+ratio.cy-1) * ratio.cx / ratio.cy;
    }

    public boolean init_objects(SCAP_ENCODER_INFORMATION encOpts, RECT rcSrn) {
        if ((encOpts.flags & SCAP_ENCODER_INFORMATION.eof_sock) != 0)
//			m_opts.encoder.encClientSocket = encOpts.encClientSocket;
            m_opts.encoder.encClientChannel = encOpts.encClientChannel;

        RLog.d("RCLog : init_objects");
        RLog.i("init_objects CanvasHandler Create  : " + rcSrn.right + ", " + rcSrn.bottom);

        if(m_uncomp == null) {
            m_uncomp = new icomp_stream(m_opts.encoder.encClientChannel);
        }
        RLog.d("RCLog : m_uncomp != null");
        if ( (encOpts.flags & SCAP_ENCODER_INFORMATION.eof_jpgcomp) != 0) {}
        if(m_jpp != null) {}

        m_opts.encoder.encJpgLowQuality = encOpts.encJpgLowQuality;
        m_opts.encoder.encJpgHighQuality= encOpts.encJpgHighQuality;

        boolean bHostBppChanged = true;
        boolean bViewBppChanged = true;
        boolean bResChanged = true;
        if (m_canvas != null) {
            bHostBppChanged = m_opts.encoder.encHostBitsPerPixel != encOpts.encHostBitsPerPixel;
            bViewBppChanged =  (m_canvas.BitCount() != encOpts.encViewerBitsPerPixel);
        }
        RLog.d("RCLog : bViewBppChanged || bResChanged : " + (bViewBppChanged || bResChanged));
        if (bViewBppChanged || bResChanged) {
            SIZE xRatio = new SIZE(1, 1);
            SIZE yRatio = new SIZE(1, 1);

            RLog.d("RCLog : m_canvas == null : " + (m_canvas == null));
            if (m_canvas == null) {
                m_canvas = new CanvasHandler();
            } else {
                xRatio = m_canvas.m_xRatio;
                yRatio = m_canvas.m_yRatio;
                m_canvas.reInitialize();
            }

            if (m_canvas == null) return false;

            nCanvasWidth = rcSrn.getWidth();
            nCanvasHeight = rcSrn.getHeight();

            SCAP_STRETCH_INFORMATION stretch = encOpts.stretch;

            RLog.d("RCLog : stretch.fixedWidth > 0 && stretch.fixedHeight > 0 : " + (stretch.fixedWidth > 0 && stretch.fixedHeight > 0));
            if (stretch.fixedWidth > 0 && stretch.fixedHeight > 0) {
                nCanvasWidth = (int) stretch.fixedWidth;
                nCanvasHeight = (int) stretch.fixedHeight;
            } else {
                nCanvasWidth  = CalculateRatioLength(nCanvasWidth, stretch.ratioWidth);
                nCanvasHeight = CalculateRatioLength(nCanvasHeight, stretch.ratioHeight);
            }

            int nViewBitsPerPixel = encOpts.encViewerBitsPerPixel;

            if(!m_canvas.Create(nCanvasWidth, nCanvasHeight, nViewBitsPerPixel))
                return false;
        }
        RLog.d("RCLog : (encOpts.flags & SCAP_ENCODER_INFORMATION.eof_cache) != 0 : " + ((encOpts.flags & SCAP_ENCODER_INFORMATION.eof_cache) != 0));
        if ((encOpts.flags & SCAP_ENCODER_INFORMATION.eof_cache) != 0) {
            m_opts.encoder.encMaxTileCache = 0;
            RLog.d("RCLog : encOpts.encMaxTileCache > 0 : " + (encOpts.encMaxTileCache > 0));
            if (encOpts.encMaxTileCache > 0) {
                m_tileCache = new TileCache(encOpts.encMaxTileCache, m_canvas.ByteCount());
                RLog.d("RCLog : new TileCache OK" );
                m_opts.encoder.encMaxTileCache = encOpts.encMaxTileCache;
                m_opts.encoder.encHostBitsPerPixel = encOpts.encHostBitsPerPixel;
            }
        }

        if (bHostBppChanged && encOpts.encHostBitsPerPixel > 8 && encOpts.encViewerBitsPerPixel <= 8) {
            int nViewBpp = encOpts.encViewerBitsPerPixel;
        }
        m_opts.encoder.encHostBitsPerPixel = encOpts.encHostBitsPerPixel;

        if (m_opts.encoder.encType != encOpts.encType) m_decoder = null;

        RLog.d("RCLog : (encOpts.flags & SCAP_ENCODER_INFORMATION.eof_type) != 0 && m_decoder == null) " + (m_opts.encoder.encType != encOpts.encType) +" ,  " + ((encOpts.flags & SCAP_ENCODER_INFORMATION.eof_type) != 0 && m_decoder == null));
        if ((encOpts.flags & SCAP_ENCODER_INFORMATION.eof_type) != 0 && m_decoder == null) {
//			m_decoder = Decoder.Create(encOpts);
            m_decoder = createDecoderObject(encOpts);
            m_decoder.setDecoder(m_decoder);
            m_decoder.setType(encOpts.encType);
            m_decoder.CreateBuffer(encOpts.encViewerBitsPerPixel);
            if ( m_decoder == null) return false;
            m_opts.encoder.encType = encOpts.encType;
        }
        RLog.d("RCLog : next");
        if (bViewBppChanged) {
            m_decoder.CreateBuffer(encOpts.encViewerBitsPerPixel);
        }

        if (m_decoder.getType() == scapOptEncMsg.scapEncodingHif) {
            if (m_jpp == null || m_uncomp == null) return false;
        } else {
        }
        RLog.d("RCLog : end");
        return true;
    }

    private Decoder createDecoderObject(SCAP_ENCODER_INFORMATION encInfo) {
        Decoder pDecoder = null;
        switch(encInfo.encType)
        {
            case SCAP_ENCODER_INFORMATION.encoderRaw:
                pDecoder = new Decoder();
                break;

            case SCAP_ENCODER_INFORMATION.encoderZip:
                pDecoder = new ZipDec();
                break;

            case SCAP_ENCODER_INFORMATION.encoderJex:
                pDecoder = new Jex24Dec();
                break;
            default:
                System.err.println("not support Encoder Type");
                break;
        }
        return pDecoder;
    }

//	private static Decoder m_decoder = null;
//	public static Decoder Create(SCAP_ENCODER_INFORMATION encInfo) {
//		Decoder pDecoder = null;
//		switch(encInfo.encType)
//		{
//		case SCAP_ENCODER_INFORMATION.encoderRaw:
//			m_decoder = new Decoder();
//			break;
//
//		case SCAP_ENCODER_INFORMATION.encoderZip:
//			m_decoder = new ZipDec();
//			break;
//
//		case SCAP_ENCODER_INFORMATION.encoderJex:
//			m_decoder = new Jex24Dec();
//			break;
//		default:
//			System.err.println("not support Encoder Type");
//			break;
//		}
//		return m_decoder;
//	}

    public boolean SetOption(SCAP_CLIENT_INFORMATION newOpts) {

        if (!init_objects(newOpts.encoder, newOpts.desk.rcSrn))
            return false;

        m_decoder.SetUncompressor(m_jpp, m_uncomp);
        m_decoder.SetChannel(m_opts.encoder.encClientChannel);
        m_decoder.SetCanvas(m_canvas);
        m_decoder.SetTileCache(m_tileCache);
        m_decoder.setHostBpp(m_opts.encoder.encHostBitsPerPixel);

        if (newOpts.encoder.encType == 'J') {
            RLog.i("DecorderThread encJpgLowQuality : "+ newOpts.encoder.encJpgLowQuality + "/" + newOpts.encoder.encJpgHighQuality);
            m_decoder.setLowQuality(newOpts.encoder.encJpgLowQuality);
            m_decoder.setHighQuality(newOpts.encoder.encJpgHighQuality);
            m_decoder.setQuality(newOpts.encoder.encJpgLowQuality, newOpts.encoder.encJpgHighQuality);
        }

        return true;
    }

    public int Decode(scapEncMsg msg, byte[] buf, int offset) {
        m_nLastPacket = msg.type;
        m_decoder.Decode(msg);
        return 0;
    }

    public void Decode(scapSrnToSrnMsg msg, byte[] buf, int offset) {
        m_nLastPacket = ScapContants.scapSrnToSrn;
        m_decoder.Decode(msg);
    }

    public void Decode(scapGraphyCacheMsg msg) {}

    public void Decode(scapCursorMsg msg, byte[] buf, int offset) {
        int nIndex = offset;
        int type = (short) ((int)buf[nIndex] & 0xff);
        m_nLastPacket = msg.type;

        nIndex++;

        if (type == ScapContants.scapCursorCached) {
            m_decoder.Read(msg, 0, msg.sz_scapCursorCachedMsg-4, buf, offset, 4);
//			RLog.i("msg ime : " + msg.ime);
            m_canvas.CursorShapeUpdate(msg.idx);
            m_canvas.setIMECursor(msg.ime);
            return;
        }

        m_decoder.Read(msg, 0, scapCursorMsg.sz_scapCursorNewMsg-4, buf, offset, 4);

        int cjBits = msg.cjBits;
        byte[] pBits = new byte[cjBits];

//		RLog.i("cursor scapCursorNew : " + msg.idx);
        m_decoder.Read(pBits, 0, msg.cjBits);
        m_canvas.CursorShapeUpdate(msg, pBits);
        m_canvas.setIMECursor(msg.ime);

        return;
    }

    public void Decode(scapCursorPosMsg msg) {
        if (GlobalStatic.MouseMode == GlobalStatic.MOUSE_TRACE) {
            m_canvas.softCursorMove(msg.ptCur.x, msg.ptCur.y);
            m_canvas.softCursorRepaint();
        }
        m_canvas.setIMECursor(msg.ime);
    }

    public void Decode(scapTileCacheMsg msg, byte[] buf, int offset) {
        m_nLastPacket = ScapContants.scapTileCache;

        int nCount = msg.count;
        scapTileCacheUnit tu = new scapTileCacheUnit();
        int bitsIndex = 0;

        if (msg.action == ScapContants.tc_hit) {
            while (nCount-- > 0) {
                m_decoder.Read(tu);
                if (m_tileCache == null) return;
                bitsIndex = m_tileCache.GetTile(tu.bitIndex);
                if (bitsIndex != -1) {
                    RECT rc = new RECT();
                    rc.left = tu.hit.x;
                    rc.top  = tu.hit.y;
                    rc.right= rc.left + tu.hit.w;
                    rc.bottom= rc.top + tu.hit.h;

                    int xoffset = (tu.hit.w < TileCache.WIDTH) ? (rc.left & TileCache.MASK) : 0;
                    int yoffset = (tu.hit.h < TileCache.WIDTH) ? (rc.top & TileCache.MASK) : 0;

                    bitsIndex += (xoffset + yoffset*TileCache.WIDTH)*m_canvas.ByteCount();

                    m_canvas.Update(m_tileCache.getBits(), bitsIndex,
                            rc.left, rc.top, tu.hit.w, tu.hit.h,
                            m_tileCache.GetScanByte());

//					m_canvas.viewer.canvas.repaintRect(rc);
                }
            }
        } else {
            while (nCount-- > 0) {
                m_decoder.Read(tu);
                if (m_tileCache == null) return;
                bitsIndex = m_tileCache.AddTile(tu.bitIndex);

                if (bitsIndex != -1) {
                    m_canvas.GetBits(m_tileCache.getBits(), bitsIndex,
                            tu.add.x, tu.add.y, tu.add.w, tu.add.h,
                            TileCache.WIDTH*m_canvas.ByteCount());
                }
            }
        }
    }

    public void Decode(scapSeamlessMsg msg) {}

    public void Decode(scapDebugMsg msg, byte[] buf, int offset) {
        m_nLastPacket = ScapContants.scapDebug;

        long crc = 0;
        m_decoder.Read(buf, offset, 4);
        crc = (Converter.readIntLittleEndian(buf, offset) & 0xffffffffL);
    }

    public void Decode(scapColorMapMsg pMsg) {
        int nClrCount = pMsg.count;
        RGBQUAD[] ptDelta = new RGBQUAD[nClrCount];
        for(int i = 0 ; i < ptDelta.length ; i++) {
            ptDelta[i] = new RGBQUAD();
            m_decoder.Read(ptDelta[i]);
        }
        m_canvas.Update(ptDelta, nClrCount);
    }


    public void DecodeBulk(scapMsg msg, byte[] buf, int offset) {
        switch(msg.type) {
            case ScapContants.scapColorMap:
                m_nLastPacket = msg.type;
                break;

            default:
                break;
        }
    }

    public SCAP_CLIENT_INFORMATION GetOption() {
        return null;
    }

    public boolean GetStretchRatio(SIZE xr, SIZE yr) {
        return false;
    }

    public void Paint() {
    }

    public void SetScrollPos(int offset, int offset2) {
    }

    public boolean SetStretchRatio(SIZE xr, SIZE yr) {
        return false;
    }
}
