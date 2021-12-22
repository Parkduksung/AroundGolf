
package com.rsupport.rv.viewer.sdk.decorder.scapDec;

import com.rsupport.rv.viewer.sdk.decorder.Converter;
import com.rsupport.rv.viewer.sdk.decorder.model.RECT;
import com.rsupport.rv.viewer.sdk.decorder.model.RECTS;
import com.rsupport.rv.viewer.sdk.decorder.model.SCAP_ENCODER_INFORMATION;
import com.rsupport.rv.viewer.sdk.decorder.scap.scapEncMsg;
import com.rsupport.rv.viewer.sdk.setting.Global;

import java.nio.ByteBuffer;
import java.util.Arrays;


public class Jex24Dec extends Decoder {
    private byte m_jpgBuf[] = new byte[64*1024*2];
    private long jppDec = 0l;

    private byte[] m_bgClr = new byte[3];
    private int m_nRawLen;
    private byte[] m_pRawBuf;

    LastPalette m_txtPal;
    LastPalette m_jpgPal;

    private final int jexEncCachedPalIndex = 0x80;
    private final int jexAllShapeEnc = 0x80;
    private final int jexFgPalEnc = 0x40;

    private final int MAX_TXT_PALETTE = 32;
    private final int MAX_JPG_PALETTE = 16;
    private final int MAX_COLOR_COUNT = 256;

    public static final int HIF_MAX_RECT = 512*512;

    public static final int Z_INBUF = 64 * 1024 - 1;
    public static final int Z_OUTBUF = 128 * 1024;
    byte[] bRead = new byte[500];
    byte[] m_revbuf = new byte[Z_OUTBUF + Z_INBUF];
    public final int BPPx = 3;
    byte m_jppbuf[] = new byte[256 * BPPx];
    JppDec m_jppdec;

    public Jex24Dec() {
        m_jppdec = new JppDec(this);
        this.init();
    }

    public void init() {
        m_txtPal = new LastPalette(MAX_TXT_PALETTE);
        m_jpgPal = new LastPalette(MAX_JPG_PALETTE);

        m_nRawLen = 0;
        m_pRawBuf = null;
        m_bgClr[0] = (byte)0xff;
        m_bgClr[1] = (byte)0xff;
        m_bgClr[2] = (byte)0xff;

        this.m_encType = 'J';
    }

    public int get_max_zcomp_size(int size) {
        return ((size) + (size)/100 + 8);
    }


    public boolean createBuffer(int nBitDepth) {
        return true;
    }

    public boolean initialize(SCAP_ENCODER_INFORMATION new_ei) {
        int nRawLen = (HIF_MAX_RECT + 15 * 2048) * m_canvasHandler.ByteCount();
        m_nNetBuf = get_max_zcomp_size(nRawLen);
        nRawLen *= 2;

        if (nRawLen != m_nRawLen) {
            m_nRawLen = nRawLen;
            m_pNetBuf =  new byte[m_nNetBuf + m_nRawLen];
        }
        return true;
    }


    private int decodeCnt = 0;
    private RECTS m_src = new RECTS();
    private RECT m_rc = new RECT();
    private int m_trash = 0;
    private byte[] m_trashBytes = new byte[10];
    private ByteBuffer renderBuffer = ByteBuffer.allocate(16);

    public void Decode(scapEncMsg pMsg) {
        int nCount = pMsg.RectCount;

        decodeCnt = 0;

        while (nCount-- > 0) {
            Read(m_src);

            m_rc.left = m_src.left;
            m_rc.right = m_src.right;
            m_rc.top = 0;
            m_rc.bottom = 0;

            int nUnitLine = (HIF_MAX_RECT / m_rc.getWidth() + 15) &~15;
            for (m_rc.top = m_src.top; m_rc.top < m_src.bottom; m_rc.top += nUnitLine) {
                m_rc.bottom = Math.min(m_rc.top + nUnitLine, m_src.bottom);
                Jex_DecodeSubRect(m_rc);
            }
//			m_canvasHandler.viewer.canvas.repaintRect(m_src);

            renderBuffer.clear();
            renderBuffer.putInt(m_src.left);
            renderBuffer.putInt(m_src.top);
            renderBuffer.putInt(m_src.getWidth());
            renderBuffer.putInt(m_src.getHeight());
            renderBuffer.flip();
            Global.GetInstance().getScreenController().render(renderBuffer.array(), renderBuffer.limit());
        }
    }

    public void Jex_DecodeSubRect(RECT rcLines) {
        if (!m_channel.readExact(bRead, 0, 2)) return;

        int packetlen = (bRead[1] & 0xff) << 8 | (bRead[0] & 0xff);

        if (packetlen < 0 || packetlen > 0xffff) return;

        if (packetlen > 0 && m_channel.readExact(m_revbuf, 0, packetlen)) {
            m_jppdec.setJpgData(m_revbuf, packetlen);
        }

        if (packetlen == 0xffff) {
            if (!m_channel.readExact(bRead, 0, 2)) return;
            packetlen = (bRead[1] & 0xff) << 8 | (bRead[0] & 0xff);

            if (packetlen > 0 && !m_channel.readExact(m_revbuf, 0, packetlen)) return;
            m_jppdec.setJpgData(m_revbuf, packetlen);
        }

        RECT rcTile = new RECT();
        for (rcTile.top = rcLines.top ; rcTile.top < rcLines.bottom ; rcTile.top += 16) {
            rcTile.bottom = Math.min(rcTile.top + 16, rcLines.bottom);
            for (rcTile.left = rcLines.left ; rcTile.left < rcLines.right ; rcTile.left += 16) {
                rcTile.right = Math.min(rcTile.left + 16, rcLines.right);
                DecodeJexTile(rcTile);
            }
        }
        m_uncomp.read_zip();
    }

    public void DecoderNarrowLine(RECT rcLines) {}

    public byte[] DecodeJexTile(RECT rcTile) {
        byte[] cTile = new byte[256 *BPPx];
        int tw = rcTile.getWidth();
        int th = rcTile.getHeight();

        int color = m_uncomp.read_zipbyte();

        if (color < 0xFD) {
            Txt_DecodeBlock(cTile, tw, th, color);
        } else {
            if (color == 0xFE) {
                m_jppdec.decompress_tile24(tw, th, m_jppbuf, true);
            } else if (color == 0xFF) {
                Jpg_DecodeBlock(cTile, tw, th);
            } else {}
        }
        m_canvasHandler.Update(cTile, 0, rcTile);
        return null;
    }

    public byte[] Txt_DecodePaletteIndex(byte[] cTile, int nPixel, int nColor) {
        int cBits;
        if (nColor == 2) {
            cBits = 1;
        } else if (nColor <= 4) {
            cBits = 2;
        } else if (nColor <= 8) {
            cBits = 3;
        } else if (nColor <= 16) {
            cBits = 4;
        } else {
            cBits = 5;
        }

        long nMask = (1 << cBits) - 1;

        long pack = 0;
        int m = 0;

        int index = 0;
        while (nPixel-- > 0) {
            if (m < cBits) {
                pack |= ((long)m_uncomp.read_zipbyte()) << m;
                m += 8;
            }

            System.arraycopy(m_txtPal.getColor(), (int)(pack&nMask) * 3, cTile, index, 3);
            index += 3;

            pack >>= cBits;
            m -= cBits;
        }
        return null;
    }

    public static void FillTile(byte[] cTile, byte[] clr, int index) {
        System.arraycopy(clr, index, cTile, 3*0, 3);
        System.arraycopy(cTile, 0, cTile, 3*1, 3*1);
        System.arraycopy(cTile, 0, cTile, 3*2, 3*2);
        System.arraycopy(cTile, 0, cTile, 3*4, 3*4);
        System.arraycopy(cTile, 0, cTile, 3*8, 3*8);
        System.arraycopy(cTile, 0, cTile, 3*16, 3*16);
        System.arraycopy(cTile, 0, cTile, 3*32, 3*32);
        System.arraycopy(cTile, 0, cTile, 3*64, 3*64);
        System.arraycopy(cTile, 0, cTile, 3*128, 3*128);
    }
    public byte[] Txt_DecodeBlock(byte[] cTile, int tw, int th, int nTxtFlags) {
        Txt_DecodePaletteColor(nTxtFlags);

        int nClrCount = (nTxtFlags & 0x3F);
        if (nClrCount <= 1) {
            FillTile(cTile, m_bgClr, 0);
            return null;
        }

//		if ((nTxtFlags & jexAllShapeEnc) > 0) {
//			FillTile(cTile, m_txtPal.getColor(), m_uncomp.read_zipbyte()*3);
//			return Txt_DecodeShape(cTile, tw, th, nClrCount);
//		} else if ((nTxtFlags & jexFgPalEnc) > 0) {
//			FillTile(cTile, m_txtPal.getColor(), m_uncomp.read_zipbyte()*3);
//			int fgIdx = m_uncomp.read_zipbyte();
//			Txt_DecodeShape(cTile, tw, th, nClrCount);
//			return Txt_DecodePaletteFgIndex(cTile, tw, th, fgIdx);
//		}

        return Txt_DecodePaletteIndex(cTile, tw*th, nClrCount);
    }

    public byte[] Jex_RLE(int nColor, byte[] byteLen) {
        int cnt = 0, run = 0;
        for (int i = 0 ; i < nColor ; i++) {
            cnt = m_uncomp.read_zipbyte();
            if (cnt == 255) {
                cnt = m_uncomp.read_zipbyte();
                run = m_uncomp.read_zipbyte();

                Arrays.fill(byteLen, i, i+run, (byte)cnt);
                i += run - 1;
            } else {
                byteLen[i] = (byte)cnt;
            }
        }
        return null;
    }
//	public byte[] Txt_DecodeShape(byte[] cTile, int tw, int th, int nColor) {
//		byte[] cPixel = new byte[MAX_JPG_PALETTE];
//		byte[] cRect = new byte[MAX_JPG_PALETTE];
//		byte[] cVLine = new byte[MAX_JPG_PALETTE];
//		byte[] cHLine = new byte[MAX_JPG_PALETTE];
//		byte[] cRow = new byte[16*3];
//
//		Jex_RLE(nColor, cPixel);
//		Jex_RLE(nColor, cHLine);
//		Jex_RLE(nColor, cVLine);
//		Jex_RLE(nColor, cRect);
//
//		byte[] clr;
//		int i = 0, count = 0;
//		int cnt = 0;
//		int thew = 0, theh = 0;
//		byte[] pRow;
//
//		for (i = 0 ; i < nColor ; i++) {
//			count = cPixel[i];
//			if (count > 0) {
////				clr = m_txtPal.getColor(i);
//				do {
//					cnt = m_uncomp.read_zipbyte();
//					System.arraycopy(m_txtPal.getColor(), i*3, cTile, (cnt >> 4) * tw * 3 + (cnt & 0xF) * 3, 3);
//				} while (--count > 0);
//			}
//		}
//
//		int m = 0;
//		int index = 0;
//		for (i = 0 ; i < nColor ; i++) {
//			count = cHLine[i];
//			if (count > 0) {
//				System.arraycopy(m_txtPal.getColor(), i*3, cRow, 3*0, 3);
//				System.arraycopy(cRow, 0, cRow, 3*1, 3*1);
//				System.arraycopy(cRow, 0, cRow, 3*2, 3*2);
//				System.arraycopy(cRow, 0, cRow, 3*4, 3*4);
//				System.arraycopy(cRow, 0, cRow, 3*8, 3*8);
//
//				do {
//					if (m == 0) {
//						m = 1;
//						cnt = m_uncomp.read_zipbyte();
//						index = (cnt >> 4) * tw * 3 + (cnt & 0xF) * 3;
//						cnt = m_uncomp.read_zipbyte();
//						thew = (cnt & 0xF) + 1;
//					} else {
//						m = 0;
//						thew = (cnt >> 4) + 1;
//						cnt = m_uncomp.read_zipbyte();
//						index = ((cnt >> 4) * tw + (cnt &0xF)) * 3;
//					}
//					System.arraycopy(cRow, 0, cTile, index, 3*thew);
//				} while (--count > 0);
//			}
//		}
//
//		m = 0;
//		index = 0;
//		for (i = 0 ; i < nColor ; i++) {
//			count = cVLine[i];
//			if (count > 0) {
////				clr = m_txtPal.getColor(i);
//				do {
//					if (m == 0) {
//						m++;
//						cnt = m_uncomp.read_zipbyte();
//						index = ((cnt >> 4) * tw + (cnt & 0xF)) * 3;
//						cnt = m_uncomp.read_zipbyte();
//						theh = (cnt & 0xF) + 1;
//					} else {
//						m = 0;
//						theh = (cnt >> 4) + 1;
//						cnt = m_uncomp.read_zipbyte();
//						index = ((cnt >> 4) * tw +(cnt & 0xF)) * 3;
//					}
//
//					do {
//						// park sung yeon
////						if (index > cTile.length-3) {
////							RLog.i("park sung yeon safe source 330");
////							index = cTile.length-3;
////							RLog.i("m_txtPal.getColor().length : " + m_txtPal.getColor().length);
////							RLog.i("cTile.length : " + cTile.length);
////							RLog.i("i : " + i);
////						}
//						System.arraycopy(m_txtPal.getColor(), i*3, cTile, index, 3);
//						index += tw * 3;
//					} while (--theh > 0);
//				} while (--count > 0);
//			}
//		}
//
//		for (i = 0 ; i < nColor ; i++) {
//			count = cRect[i];
//			if (count > 0) {
//				System.arraycopy(m_txtPal.getColor(), i*3, cRow, 3*0, 3);
//				System.arraycopy(cRow, 0, cRow, 3*1, 3*1);
//				System.arraycopy(cRow, 0, cRow, 3*2, 3*2);
//				System.arraycopy(cRow, 0, cRow, 3*4, 3*4);
//				System.arraycopy(cRow, 0, cRow, 3*8, 3*8);
//			}
//
//			do {
//				cnt  = m_uncomp.read_zipbyte();
//				index = (cnt >> 4) * tw * 3 + (cnt & 0xF) * 3;
//				cnt = m_uncomp.read_zipbyte();
//				thew = (cnt & 0xF) * 3 + 3;
//				theh = (cnt >> 4) + 1;
//
//				do {
////					if (index > cTile.length-thew) {
////						RLog.i("park sung yeon safe source 362");
////						index = cTile.length-thew;
////						RLog.i("cRow.length : " + cRow.length);
////						RLog.i("cTile.length : " + cTile.length);
////					}
//					System.arraycopy(cRow, 0, cTile, index, thew);
//					index += tw * 3;
//				} while (--theh > 0);
//			} while (--count > 0);
//		}
//
//		return null;
//	}
//
//	public byte[] Txt_DecodePaletteFgIndex(byte[] cTile, int tw, int th, int fgIdx) {
//		int cnt = 0;
//
//		for (int i = 0, m = 1 ; i < tw * th;) {
//			if (--m == 0) {
//				cnt = m_uncomp.read_zipbyte();
//				m = 8;
//			}
//
//			if (cnt == 0) {
//				i += m;
//				m = 1;
//				continue;
//			}
//
//			if ((cnt & 1) > 0) {
//				byte[] colorBytes = m_txtPal.getColor();
//				// park sung yeon defendency
////				if (colorBytes.length <= (fgIdx*3)+3) fgIdx = (colorBytes.length-3)/3-1;
//				System.arraycopy(m_txtPal.getColor(), fgIdx*3, cTile, i * 3, 3);
//			}
//			cnt >>= 1;
//			++i;
//		}
//		return null;
//	}

    public void Jpg_DecodeBlock(byte[] cTile, int tw, int th) {
        m_jppdec.decompress_tile24(tw, th, cTile, false);
        int nColor = m_uncomp.read_zipbyte();

        if (nColor > 0) {
            byte[] palPict24 = new byte[MAX_JPG_PALETTE*3];
            Jpg_DecodePaletteColor(nColor, palPict24);
            Jpg_DecodeShape(palPict24, nColor, cTile, tw * 3);
        }
    }

    private final int BPP24 = 3;
    public byte[] Txt_DecodePaletteColor(int nColorFlag) {
        if (nColorFlag == 0) return null;

        int nColor = nColorFlag & 0x3F;
        if (nColor == 1) {
            if ((nColorFlag & jexEncCachedPalIndex) > 0) {
                System.arraycopy(m_txtPal.getCacheColor(), m_uncomp.read_zipbyte()*3, m_bgClr, 0, 3);
            } else {
                m_uncomp.read_zip(m_bgClr, 0, 3);
            }
            return null;
        }

        int nonHitMask = 0;
        int i = (nColor + 7) / 8;
        byte[] revBuf = new byte[i];
        m_uncomp.read_zip(revBuf, 0, i);
        nonHitMask = Converter.readIntLittleEndian2(revBuf);

        byte[] palTxt = m_txtPal.getColor();
        boolean[] hitTable = new boolean[MAX_TXT_PALETTE];

        int nNew = 0;
        byte[] palNew = new byte[MAX_TXT_PALETTE * 3];

        int index = 0;
        for (i = 0 ; i < nColor ; ++i) {
            if ((nonHitMask & 0x01) != 0) {
                m_uncomp.read_zip(palTxt, index, 3);
                System.arraycopy(palTxt, index, palNew, nNew*3, 3);
                ++nNew;
                index += 3;
            } else {
                int idx = m_uncomp.read_zipbyte();
                hitTable[idx] = true;
                System.arraycopy(m_txtPal.getCacheColor(), idx * 3, palTxt, index, 3);
                index += 3;
            }
            nonHitMask >>= 1;
        }

        m_txtPal.UpdateCache(palNew, nNew, hitTable);

        return null;
    }

    public byte[] Jpg_DecodePaletteColor(int nColor, byte[] palJpg) {
        long nonHitMask = 0;
        int i = (nColor + 7) / 8;
        byte[] revBuf = new byte[i];
        m_uncomp.read_zip(revBuf, 0, i);
        nonHitMask = Converter.readLongLittleEndian2(revBuf, 0);

        boolean[] hitTable = new boolean[MAX_JPG_PALETTE];

        int nNew = 0;
        byte[] palNew = new byte[MAX_JPG_PALETTE * 3];

        int index = 0;
        for (i = 0 ; i < nColor ; ++i) {
            if ((nonHitMask & 0x01) != 0) {
                m_uncomp.read_zip(palJpg, index, 3);
                System.arraycopy(palJpg, index, palNew, nNew*3, 3);
                ++nNew;
            } else {
                int idx = m_uncomp.read_zipbyte();
                hitTable[idx] = true;

                System.arraycopy(m_jpgPal.getCacheColor(), idx * 3, palJpg, index, 3);
            }
            index += 3;
            nonHitMask >>= 1;
        }

        m_jpgPal.UpdateCache(palNew, nNew, hitTable);

        return null;
    }
    public static int GETOFF(int pack, int ts) {
        return ((pack)>>4) * ts + (pack&0xf) * 3;
    }
    public byte[] Jpg_DecodeShape(byte[] pal24, int nColor, byte[] cTile, int tscan) {
        byte[] cPixel = new byte[MAX_JPG_PALETTE];
        byte[] cRect = new byte[MAX_JPG_PALETTE];
        byte[] cVLine = new byte[MAX_JPG_PALETTE];
        byte[] cHLine = new byte[MAX_JPG_PALETTE];
        byte[] fillRow = new byte[16*3];
        Jex_RLE(nColor, cPixel);
        Jex_RLE(nColor, cHLine);
        Jex_RLE(nColor, cVLine);
        Jex_RLE(nColor, cRect);

        int i, j, k, m, tmp = 0;

//		byte[][] clrFillDebug = new byte[][] { // jjKim - not used
//				{0, 0, (byte)0xff},
//				{0, (byte)0xff, 0},
//				{(byte)0xff, (byte)0xff, 0},
//				{0, (byte)0xff, (byte)0xff},
//				{(byte)0xff, 0, (byte)0xff},
//		};

        byte[] pClr, pDst;
        int srcIndex = 0;
        int dstIndex = 0;
        int tempInt = 0;
        for (i = 0 ; i < nColor ; ++i) {
            tempInt = cPixel[i] & 0xff;
            if (tempInt > 0) {
                srcIndex = i * 3;
                for (j = tempInt ; j > 0 ; --j) {
                    dstIndex = GETOFF(m_uncomp.read_zipbyte(), tscan);
                    System.arraycopy(pal24, srcIndex, cTile, dstIndex, 3);
                }
            }
        }

        m = 0;
        for (i = 0 ; i < nColor ; ++i) {
            tempInt = cHLine[i] & 0xff;
            if (tempInt > 0) {
                srcIndex = i * 3;
                for (j = 0 ; j < 16 ; ++j) {
                    System.arraycopy(pal24, srcIndex, fillRow, j*3, 3);
                }

                for (j = tempInt ; j > 0 ; --j) {
                    if (m == 0) {
                        dstIndex = GETOFF(m_uncomp.read_zipbyte(),tscan);
                        tmp = m_uncomp.read_zipbyte();
                        System.arraycopy(fillRow, 0, cTile, dstIndex, 3 * (tmp&0xf) +3);
                        m = 1;
                    } else {
                        System.arraycopy(fillRow, 0, cTile, GETOFF(m_uncomp.read_zipbyte(), tscan), 3*(tmp>>4)+3);
                        m = 0;
                    }
                }
            }
        }

        for (i = 0 ; i < nColor ; ++i) {
            tempInt = cVLine[i] & 0xff;
            if (tempInt > 0) {
                srcIndex = i * 3;

                for (j = tempInt ; j > 0 ; --j) {
                    if (m==0) {
                        dstIndex = GETOFF(m_uncomp.read_zipbyte(),tscan);
                        tmp = m_uncomp.read_zipbyte();
                        for (k = (tmp&0xF)+ 1 ; k > 0 ; --k, dstIndex += tscan) {
                            System.arraycopy(pal24, srcIndex, cTile, dstIndex, 3);
                        }
                        m = 1;
                    }
                    else
                    {
                        dstIndex = GETOFF(m_uncomp.read_zipbyte(),tscan);
                        for (k = (tmp>>4) + 1 ; k > 0 ; --k, dstIndex += tscan) {
                            System.arraycopy(pal24, srcIndex, cTile, dstIndex, 3);
                        }
                        m = 0;
                    }
                }
            }
        }

        int wh;
        for (i = 0 ; i < nColor ; ++i) {
            tempInt = cRect[i] & 0xff;
            if (tempInt > 0) {
                srcIndex = i * 3;
                for (j = 0 ; j < 16 ; ++j) {
                    System.arraycopy(pal24, srcIndex, fillRow, j*3, 3);
                }

                for (j = tempInt ; j > 0 ; --j) {
                    wh = m_uncomp.read_zipbyte();
                    tmp = (wh & 0xF) * 3 + 3;
                    k   = (wh >>  4)   + 1;
                    dstIndex = GETOFF(m_uncomp.read_zipbyte(),tscan);
                    if (dstIndex < 0) {
                        int ii = 0;
                    }

                    for(; k > 0; --k, dstIndex += tscan) {
                        System.arraycopy(fillRow, 0, cTile, dstIndex, tmp);
                    }
                }
            }
        }

        return null;
    }

    @Override
    public void destoryDecorder() {
        if (m_jppdec != null) m_jppdec.endJppDec();
    }

}