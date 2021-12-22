package com.rsupport.rv.viewer.sdk.decorder.scapDec;


import com.rsupport.rv.viewer.sdk.common.Pallete;
import com.rsupport.rv.viewer.sdk.common.log.RLog;
import com.rsupport.rv.viewer.sdk.decorder.lib_gdi.ColorConv;
import com.rsupport.rv.viewer.sdk.decorder.lib_gdi.mac_def;
import com.rsupport.rv.viewer.sdk.decorder.model.DIBINFO;
import com.rsupport.rv.viewer.sdk.decorder.model.POINT;
import com.rsupport.rv.viewer.sdk.decorder.model.POINTS;
import com.rsupport.rv.viewer.sdk.decorder.model.RECT;
import com.rsupport.rv.viewer.sdk.decorder.model.RECTS;
import com.rsupport.rv.viewer.sdk.decorder.model.RGBQUAD;
import com.rsupport.rv.viewer.sdk.decorder.model.SIZE;
import com.rsupport.rv.viewer.sdk.decorder.scap.scapCursorMsg;
import com.rsupport.rv.viewer.sdk.setting.Global;
import com.rsupport.rv.viewer.sdk.setting.GlobalStatic;

import java.nio.ByteBuffer;


public class CanvasHandler {
    DIBINFO m_DibInfo;
    int m_nByteCount;
    int m_nBitCount;
    int m_nBitsPerPixel;
    byte[] m_pvBits;
    int m_pvBitsIndex;
    int m_nScan;
    POINT m_ptSrlPos;
    SIZE m_xRatio, m_yRatio;
    long m_tickLastInv;
    final private ByteBuffer renderBuffer = ByteBuffer.allocate(16);

    public CanvasHandler() {
        m_nScan = 0;
        m_nBitCount = 0;
        m_pvBitsIndex = 0;
        m_xRatio = new SIZE();
        m_yRatio = new SIZE();
        m_xRatio.cx = m_xRatio.cy = 1;
        m_yRatio.cx = m_yRatio.cy = 1;
        m_ptSrlPos = new POINT(0, 0);
        m_tickLastInv = 0;
    }

    public void reInitialize() {
        m_nScan = 0;
        m_nBitCount = 0;
        m_pvBitsIndex = 0;

        m_xRatio.cx = m_xRatio.cy = 1;
        m_yRatio.cx = m_yRatio.cy = 1;

        m_ptSrlPos.x = m_ptSrlPos.y = 0;
        m_tickLastInv = 0;
    }

    public boolean Create(int width, int height, int nBitCount) {
        m_nBitsPerPixel = nBitCount;
        m_nBitCount = (nBitCount+1)/8*8;

        if (m_DibInfo == null) {
            DIBINFO dibinfo = new DIBINFO();
            dibinfo.bmiHeader.biWidth = width;
            dibinfo.bmiHeader.biHeight= -height; // top 2 btm
            dibinfo.bmiHeader.biBitCount = Math.max(8, m_nBitCount);
            dibinfo.bmiHeader.biSizeImage = width*dibinfo.bmiHeader.biBitCount/8*height;

            int BI_RGB = 0;
            if (nBitCount == 24 || nBitCount <= 8) dibinfo.bmiHeader.biCompression = BI_RGB;
            dibinfo.bmiHeader.biClrUsed = 0;

            dibinfo.bmiMask = ColorConv.GetColorMask(nBitCount);
            m_DibInfo = dibinfo;
        } else {
            m_DibInfo.bmiHeader.biWidth = width;
            m_DibInfo.bmiHeader.biHeight= -height;
            m_DibInfo.bmiHeader.biBitCount = Math.max(8, m_nBitCount);
            m_DibInfo.bmiHeader.biSizeImage = width*m_DibInfo.bmiHeader.biBitCount/8*height;

            int BI_RGB = 0;
            if (nBitCount == 24 || nBitCount <= 8) m_DibInfo.bmiHeader.biCompression = BI_RGB;
            m_DibInfo.bmiHeader.biClrUsed = 0;

            m_DibInfo.bmiMask = ColorConv.GetColorMask(nBitCount);
        }
        m_nByteCount = m_DibInfo.bmiHeader.biBitCount/8;

        if (m_nByteCount == 1) {
            m_nScan = (width * m_DibInfo.bmiHeader.biBitCount / 8);
        } else {
            m_nScan = width;
        }

        GlobalStatic.G_BITCOUNT = (short)nBitCount;
        GlobalStatic.G_BYTESCOUNT = (short)nBitCount;
        GlobalStatic.G_BITMAPWIDTH = (short)width;
        GlobalStatic.G_BITMAPHEIGHT = (short)height;


        RLog.d("GlobalStatic.G_BITMAPWIDTH : " + GlobalStatic.G_BITMAPWIDTH);
        RLog.d("GlobalStatic.G_BITMAPHEIGHT : " + GlobalStatic.G_BITMAPHEIGHT);

        if (GlobalStatic.ORIGIN_RESOLUTION_X != 0 && GlobalStatic.isCloseSessionMode()) {
            if (width == GlobalStatic.ORIGIN_RESOLUTION_X && height == GlobalStatic.ORIGIN_RESOLUTION_Y) {
                GlobalStatic.AUTORESOLUTION_DURATIONTIME = 0;
//                Global.GetInstance().getPcViewer().forceCloseProc();
                GlobalStatic.sourceBitmapPixels = null;
                return true;
            }
        }

        if (GlobalStatic.G_CONNECTEDSESSION) {
            Global.GetInstance().getScreenController().setSourceSize(width, height);
        } else {
            Global.GetInstance().getScreenController().setSourceSize(width, height);
        }

        GlobalStatic.G_CONNECTEDSESSION = true;

        return true;
    }

    public void Update(byte[] pSrc, int pSrcIndex, int left, int top, int width, int height, int nSrcScan) {
        Update_JavaDrawing(pSrc, pSrcIndex, left, top, width, height, nSrcScan);
    }

    public void Update_JavaDrawing(byte[] pSrc, int pSrcIndex, int left, int top, int width, int height, int nSrcScan) {
        int h = height;
        int nBytesPerRow = width * ByteCount();

        int pDstIndex = m_pvBitsIndex + (left  + top * m_nScan);
        int pBytesDstIndex = m_pvBitsIndex + (left + top*m_nScan) * m_nByteCount;

        if(pDstIndex < 0) {
            System.err.println("pDstIndex error " + pDstIndex);
            return;
        }

        int pixel24[] =  GlobalStatic.sourceBitmapPixels;

        if (pixel24 == null || GlobalStatic.G_BITMAPWIDTH*GlobalStatic.G_BITMAPHEIGHT > GlobalStatic.getMaxResolution()) {
            return;
        }

        do {
            int srcSt = pSrcIndex;
            int dstSt = pDstIndex;
            int index = 0;
            if (m_nByteCount == 1) {
                for (int i = 0; i < nBytesPerRow; i += m_nByteCount) {
                    index = pSrc[srcSt] & 0xFF;
                    if (m_nBitsPerPixel == 8) {
                        pixel24[dstSt] = Pallete.getColor256(index);
                    } else {
                        pixel24[dstSt] = Pallete.getColor4(index);
                    }

                    srcSt += m_nByteCount;
                    pBytesDstIndex += m_nByteCount;
                    dstSt++;
                }
            } else if(m_nByteCount == 2) {
                short color;
                int red;
                int green;
                int blue;

                int pixelSize = pixel24.length;

                if(m_nBitsPerPixel == 15) {
                    for (int i = 0; i < nBytesPerRow; i += m_nByteCount) {
                        color = (short)((pSrc[srcSt+1] & 0xFF) << 8 | (pSrc[srcSt] & 0xFF));

                        red = (color & 0x1f) * 8;
                        green = ((color >>>5) & 0x1f) * 8;
                        blue = ((color >>>10) & 0x1f) * 8;

                        pixel24[dstSt] = ( (blue&0xff) << 16) | ( (green&0xff) <<8) | (red&0xff);

                        srcSt += m_nByteCount;
                        pBytesDstIndex += m_nByteCount;
                        dstSt++;
                    }
                } else {
                    for (int i = 0; i < nBytesPerRow; i += m_nByteCount) {
                        color = (short)((pSrc[srcSt+1] & 0xFF) << 8 | (pSrc[srcSt] & 0xFF));
                        red    = ((color >>>11) &0x1f )*8;
                        green =  ((color >>>6)&0x1f)*8;
                        blue   = (color &0x1f)*8;

                        pixel24[dstSt] = ( (blue&0xff) << 16) | ( (green&0xff) <<8) | (red&0xff);

                        srcSt += m_nByteCount;
                        dstSt++;
                    }
                }
            } else if (m_nByteCount == 3 || m_nByteCount == 4) {
                for (int i = 0; i < nBytesPerRow; i += m_nByteCount) {
                    pixel24[dstSt] = (pSrc[srcSt + 2] & 0xFF) << 16
                            | (pSrc[srcSt + 1] & 0xFF) << 8
                            | (pSrc[srcSt] & 0xFF);
                    srcSt += m_nByteCount;
                    dstSt++;
                }
            } else {
                RLog.d("not support byte count. : " + m_nByteCount);
                return;
            }
            pDstIndex = pDstIndex + m_nScan;
            pSrcIndex += nSrcScan;
        } while (--h > 0);
    }

    public void UpdateByIndex(byte[] pSrc, int startIndex, int len, int left, int top, int width, int height, int nSrcScan) {
        int copiedScan = 0;
        int rest = 0;
        int pDstIndex = 0;
        if (startIndex == 0) {
            pDstIndex = left + (top*m_nScan);
        } else {
            copiedScan = startIndex/(width*m_nByteCount);
            rest = startIndex%(width*m_nByteCount);
            if (copiedScan == 0) rest = startIndex;
            pDstIndex = left + (rest/m_nByteCount) + (copiedScan*m_nScan) + (top*m_nScan);
        }

        if(pDstIndex < 0) {
            System.err.println("pDstIndex error " + pDstIndex);
            return;
        }

        int pixel24[] = GlobalStatic.sourceBitmapPixels;
        if (pixel24 == null) return;

        int srcIndex = 0;
        int copiedIndex = 0;
        int perRow = 0;
        int srcSt = 0;

        do {
            int dstSt = pDstIndex;
            int index = 0;
            if ((copiedIndex+nSrcScan) < len) {
                perRow = nSrcScan;
            } else {
                perRow = len - copiedIndex;
            }

            if (m_nByteCount == 1) {
                for (int i = rest ; i < perRow ; i += m_nByteCount) {
                    index = pSrc[srcSt] & 0xFF;
                    if (m_nBitsPerPixel == 8) {
                        pixel24[dstSt] = Pallete.getColor256(index);
                    } else {
                        pixel24[dstSt] = Pallete.getColor4(index);
                    }
                    srcSt++;
                    dstSt++;
                }
            } else if(m_nByteCount == 2) {
                short color;
                int red;
                int green;
                int blue;
                int pixelSize = pixel24.length;

                if(m_nBitsPerPixel == 15) {
                    for (int i = rest ; i < perRow ; i += m_nByteCount) {
                        color = (short)((pSrc[srcSt+1] & 0xFF) << 8 | (pSrc[srcSt] & 0xFF));

                        red = (color & 0x1f) * 8;
                        green = ((color >>>5) & 0x1f) * 8;
                        blue = ((color >>>10) & 0x1f) * 8;

                        pixel24[dstSt] = ( (blue&0xff) << 16) | ( (green&0xff) <<8) | (red&0xff);

                        srcSt += m_nByteCount;
                        dstSt++;

                        if (pixelSize <= dstSt) {
                            return;
                        }
                    }
                } else {
                    for (int i = rest ; i < perRow ; i += m_nByteCount) {
                        color = (short)((pSrc[srcSt+1] & 0xFF) << 8 | (pSrc[srcSt] & 0xFF));
                        red    = ((color >>>11) &0x1f )*8;
                        green =  ((color >>>6)&0x1f)*8;
                        blue   = (color &0x1f)*8;

                        pixel24[dstSt] = ( (blue&0xff) << 16) | ( (green&0xff) <<8) | (red&0xff);

                        srcSt += m_nByteCount;
                        dstSt++;

                        if (pixelSize <= dstSt) {
                            return;
                        }
                    }
                }
            }
            pDstIndex = pDstIndex + m_nScan - (rest/m_nByteCount);
            copiedIndex += perRow - rest;
            rest = 0;
        } while (copiedIndex < len);
    }

    public void GetBits(byte[] pDst, int pDstIndex, int left, int top, int width, int height, int nDstScan) {
        if (nDstScan == 0) nDstScan = width * ByteCount();

        int nBytesPerBlkRow = width * ByteCount();

        int pSrcIndex = (m_nScan * ByteCount() * top) + (left * ByteCount());

        while (height-- > 0) {
            pDstIndex = pDstIndex + nDstScan;
            pSrcIndex += (m_nScan*ByteCount());
        }
    }

    public boolean GetStretchRatio(SIZE xRatio, SIZE yRatio) {
        xRatio.cx = m_xRatio.cx;
        xRatio.cy = m_xRatio.cy;
        xRatio.cx = m_yRatio.cx;
        xRatio.cy = m_yRatio.cy;
        return true;
    }

    public void Srn2Srn(RECTS rcDst, POINTS ptSrc) {
        RECT rc = new RECT(rcDst.left, rcDst.top, rcDst.right, rcDst.bottom);
        POINT pt = new POINT(ptSrc.x, ptSrc.y);
        Srn2Srn(rc, pt);
    }

    public void Srn2Srn(RECT rcDst, POINT ptSrc) {
        RECTS updateRect = new RECTS();

        int width = rcDst.getWidth();
        int height = rcDst.getHeight()+1;
        int pixelsSize = 0;
        int pixels24[] = GlobalStatic.sourceBitmapPixels;
        byte pixels8[] = null;
        byte[] bytePixel = null;
        int bitCount = m_DibInfo.bmiHeader.biBitCount/8;

        if (pixels24 == null || GlobalStatic.G_BITMAPWIDTH*GlobalStatic.G_BITMAPHEIGHT > GlobalStatic.getMaxResolution()) {
            RLog.d("GlobalStatic.getMaxResolution() : " + GlobalStatic.getMaxResolution());
            return;
        }

        if (ptSrc.y >= rcDst.top) {
            int pDstIndex = rcDst.left + (rcDst.top-1) * m_nScan;
            int pSrcIndex = ptSrc.x + (ptSrc.y-1) * m_nScan;
            if (pDstIndex < 0) {
                pDstIndex = rcDst.left;
            }
            if (rcDst.top == 0) {
                pSrcIndex = ptSrc.x + (ptSrc.y) * m_nScan;
            }
            pixelsSize = pixels24.length;

            do {
                System.arraycopy(pixels24, pSrcIndex, pixels24, pDstIndex, width);
                pDstIndex = pDstIndex + m_nScan;
                pSrcIndex = pSrcIndex + m_nScan;
                if((pDstIndex+width) > pixelsSize) break;
                if((pSrcIndex+width) > pixelsSize) 	break;
            } while (--height > 0);
        } else {
            int pDstIndex = 0, pSrcIndex = 0;
            if (rcDst.bottom >= GlobalStatic.G_BITMAPHEIGHT) {
                pDstIndex = rcDst.left + (rcDst.bottom - 1) * m_nScan;
                pSrcIndex = ptSrc.x + (ptSrc.y + height - 2) * m_nScan;
            } else {
                pDstIndex = rcDst.left + (rcDst.bottom) * m_nScan;
                pSrcIndex = ptSrc.x + (ptSrc.y + height - 1) * m_nScan;
            }
            if (ptSrc.y == 0) pSrcIndex += m_nScan;

            pixelsSize = pixels24.length;

            do {
                System.arraycopy(pixels24, pSrcIndex, pixels24, pDstIndex, width);
                pDstIndex = pDstIndex - m_nScan;
                pSrcIndex = pSrcIndex - m_nScan;
            } while (--height > 0);
        }

        updateRect.left = (short)(rcDst.left);
        updateRect.top = (short)(rcDst.top);
        updateRect.right = (short)(rcDst.right);
        updateRect.bottom = (short)(rcDst.bottom);


        renderBuffer.clear();
        renderBuffer.putInt(updateRect.left);
        renderBuffer.putInt(updateRect.top);
        renderBuffer.putInt(updateRect.getWidth());
        renderBuffer.putInt(updateRect.getHeight());
        renderBuffer.flip();
        Global.GetInstance().getScreenController().render(renderBuffer.array(), renderBuffer.limit());
    }


    public void Update(byte[] pSrc, int pSrcIndex, final RECT prc) {
        int w = prc.getWidth();
        Update(pSrc, pSrcIndex, prc.left, prc.top, w, prc.getHeight(), w*ByteCount());
    }
    public void UpdateByIndex(byte[] pSrc, int startIndex, int len, final RECT prc) {
        if (GlobalStatic.FIXWIDTH != 0) return;
        int w = prc.getWidth();
        UpdateByIndex(pSrc, startIndex, len, prc.left, prc.top, w, prc.getHeight(), w*ByteCount());
    }

    public void Update(byte[] pSrc, int pSrcIndex, final RECTS prc) {
        int w = prc.getWidth();
        Update(pSrc, pSrcIndex, prc.left, prc.top, w, prc.getHeight(), w * ByteCount());
    }

    public int BitCount() { return m_nBitCount; }
    public int ByteCount() { return m_nByteCount; }
    public boolean IsValid() {
        if(m_pvBits != null) return true;
        return false;
    }
    public RGBQUAD[] GetColorTable() { return m_DibInfo.bmiColors; }
    public long GetColor(short clr8) {
        RGBQUAD rgbq = m_DibInfo.bmiColors[clr8];
        return mac_def.RGB(rgbq.rgbRed, rgbq.rgbGreen, rgbq.rgbBlue);
    }
    public void SetScrollPos(int xOffset, int yOffset) {
        if(xOffset >= 0) m_ptSrlPos.x = xOffset;
        if(yOffset >= 0) m_ptSrlPos.y = yOffset;
    }
    public int GetPaletteIndex(long clr)  { return 0; }
    public void showMessageBox(String title, String content, int msgtype) {}
    public void CursorShapeUpdate(scapCursorMsg msg, byte[] pBits) {
        if (msg.iClrBpp == 0) 	__CursorMonoSapeUpdate(msg, pBits);
        else 					__CursorColorShapeUpdate(msg, pBits);
    }
    private void __CursorMonoSapeUpdate(scapCursorMsg msg, byte[] pBits) {}
    private void __CursorColorShapeUpdate(scapCursorMsg msg, byte[] pBits) {}
    public void CursorShapeUpdate(int idx) {}
    public void Update(RGBQUAD[] pRgbTable, int nCount) {}
    public void setIMECursor(int ime) {}
    public void softCursorMove(int x, int y) {}
    public void softCursorRepaint() {}

}
