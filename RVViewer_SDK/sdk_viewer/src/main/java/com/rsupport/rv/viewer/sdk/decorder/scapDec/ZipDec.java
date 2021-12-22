package com.rsupport.rv.viewer.sdk.decorder.scapDec;


import com.rsupport.rv.viewer.sdk.common.log.RLog;
import com.rsupport.rv.viewer.sdk.decorder.Converter;
import com.rsupport.rv.viewer.sdk.decorder.model.RECT;
import com.rsupport.rv.viewer.sdk.decorder.model.RECTS;
import com.rsupport.rv.viewer.sdk.decorder.model.SCAP_ENCODER_INFORMATION;
import com.rsupport.rv.viewer.sdk.decorder.scap.scapEncMsg;
import com.rsupport.rv.viewer.sdk.decorder.scap.scapOptEncMsg;
import com.rsupport.rv.viewer.sdk.setting.Global;

import java.nio.ByteBuffer;
import java.util.Arrays;


public class ZipDec extends Decoder {
	final int BPP24 = 3;
	final boolean JPGQ_LOG = true;
	final boolean JPGQ_HIGH = false;

	int m_pRawBufIndex;
	int m_nRawBuf;
	byte m_XTable[];

	public ZipDec() {
		m_nNetBuf = 0;
		m_pNetBuf = null;
		m_pNetBufIndex = 0;
		m_pRawBufIndex = 0;
		m_nRawBuf = 0;
		m_encType = scapOptEncMsg.scapEncodingZip;
		m_XTable = null;
	}

	public boolean CreateBuffer(int nBitDepth) {
		nBitDepth = (nBitDepth+1)/8*8;
		int nRawLen = ZIPENC_MAX_RECT * Math.max(8, nBitDepth)/8;
		nRawLen = get_max_zcomp_size(nRawLen);

		if (nRawLen > m_nNetBuf) {
			m_nNetBuf = nRawLen;
			m_pNetBufIndex = 0;
//			m_pNetBuf = new byte[m_nNetBuf+nRawLen];
			m_pRawBufIndex = m_nNetBuf;
			m_pRawBufIndex = 0;
			m_nRawBuf = nRawLen;
		}
		return true;
	}

	public boolean Initialize(SCAP_ENCODER_INFORMATION new_ei) {
		if(!super.Initialize(new_ei)) return false;

		int nRawLen = ZIPENC_MAX_RECT * m_canvasHandler.ByteCount();
		nRawLen = get_max_zcomp_size(nRawLen);

		if (nRawLen > m_nNetBuf) {
			m_nNetBuf = nRawLen;
			m_pNetBufIndex = 0;
//			m_pNetBuf = new byte[m_nNetBuf+nRawLen];
			m_pRawBufIndex = m_nNetBuf;
			m_nRawBuf = nRawLen;
		}
		return true;
	}

	public void Decode(scapEncMsg pMsg) {
		int nRet = 0;
		int nRect = pMsg.RectCount;
		if (0 == nRect) return;

		if (pMsg.pad == 0) {
			nRet = DecodeZip(nRect);
		} else {
			DecodeJpg(pMsg.RectCount, pMsg.pad);
		}
		return;
	}

	private boolean isInitializedCanvas() {
		return true;
	}

	private static short SHORT(byte b) {
		return Converter.toUnsignedChar(b);
	}

	private void read8Color(int len) {
		int scope = 0;
		byte val;

		for (int i = 0 ; i < len ; ) {
			val = m_uncomp.read_zipbyte2();
			if (SHORT(val) < SHORT((byte)0xfe)) {
				m_pNetBuf[i++] = val;
				continue;
			}

			if (val == (byte)0xfe) {
				temprun2[0] = m_uncomp.read_zipbyte2();
				temprun2[0] &= 0xff;
				temprun2[1] &= 0x00;
			} else {
				m_uncomp.read_zipshort(temprun2);
			}

			val = m_uncomp.read_zipbyte2();
			scope = Converter.read2BytesToIntLittleEndian(temprun2);

			Arrays.fill(m_pNetBuf, i, i + scope, val);
			i += scope;
		}
	}

	private void read8Color(int len, final RECT rc) {
		int scope = 0;
		byte val;
		int perLen = 0;
		int startIndex = 0;
		int maxBuf = BUFSIZE - (BUFSIZE/3);

		for (int i = 0 ; i < len ; ) {
			val = m_uncomp.read_zipbyte2();
			if (SHORT(val) < SHORT((byte)0xfe)) {
				m_pNetBuf[perLen++] = val;
				i++;
				continue;
			}

			if (val == (byte)0xfe) {
				temprun2[0] = m_uncomp.read_zipbyte2();
				temprun2[0] &= 0xff;
				temprun2[1] &= 0x00;
			} else {
				m_uncomp.read_zipshort(temprun2);
			}

			val = m_uncomp.read_zipbyte2();
			scope = Converter.read2BytesToIntLittleEndian(temprun2);

			Arrays.fill(m_pNetBuf, perLen, perLen + scope, val);
			i += scope;
			perLen += scope;

			if (perLen > maxBuf) {
				m_canvasHandler.UpdateByIndex(m_pNetBuf, startIndex, perLen, rc);
				startIndex += perLen;
				perLen = 0;
			}
		}
		if (perLen > 0) {
			m_canvasHandler.UpdateByIndex(m_pNetBuf, startIndex, perLen, rc);
		}
	}

	private byte[] temprun2 = new byte[2];
	private byte[] tempval = new byte[2];
	private void read16Color(int len) {
		int index = 0;
		int scope = 0;

		for (int i = 0 ; i < len ;) {
			m_uncomp.read_zipshort(tempval);
			if (tempval[0] == (byte)0xff && tempval[1] == (byte)0xff) {
			} else {
				i++;
				m_pNetBuf[index++] = tempval[0];
				m_pNetBuf[index++] = tempval[1];
				continue;
			}

			m_uncomp.read_zipshort(temprun2);
			m_uncomp.read_zipshort(tempval);

			scope = Converter.read2BytesToIntLittleEndian(temprun2);

			do {
				i++;
				m_pNetBuf[index++] = tempval[0];
				m_pNetBuf[index++] = tempval[1];
			} while (--scope > 0);
		}
	}

	private void read16Color(int len, RECT rc) {
		int index = 0;
		int scope = 0;
		int perLen = 0;
		int startIndex = 0;
		int maxBuf = BUFSIZE - (BUFSIZE/3);

		for (int i = 0 ; i < len ;) {
			m_uncomp.read_zipshort(tempval);
			if (tempval[0] == (byte)0xff && tempval[1] == (byte)0xff) {
			} else {
				i++;
				m_pNetBuf[perLen++] = tempval[0];
				m_pNetBuf[perLen++] = tempval[1];
				continue;
			}

			m_uncomp.read_zipshort(temprun2);
			m_uncomp.read_zipshort(tempval);

			scope = Converter.read2BytesToIntLittleEndian(temprun2);

			do {
				i++;
				m_pNetBuf[perLen++] = tempval[0];
				m_pNetBuf[perLen++] = tempval[1];
			} while (--scope > 0);

			if (perLen > maxBuf) {
				m_canvasHandler.UpdateByIndex(m_pNetBuf, startIndex, perLen, rc);
				startIndex += perLen;
				perLen = 0;
			}
		}
		if (perLen > 0) {
			m_canvasHandler.UpdateByIndex(m_pNetBuf, startIndex, perLen, rc);
		}
	}

	private final int BUFSIZE = 2200 * 100;
	private ByteBuffer renderBuffer = ByteBuffer.allocate(16);
	public int DecodeZip(int nCount) {
		RECTS sRC = new RECTS();
		RECT rc = new RECT();
		while(nCount-- > 0) {
			m_uncomp.read_zip(sRC);
			if (sRC.left < 0 || sRC.right < 0 || sRC.top < 0 || sRC.bottom < 0) {
				RLog.d("Decode Zip exception");
				return 0;
			}

			rc.left= sRC.left;
			rc.right = sRC.right;
			rc.top = sRC.top;
			rc.bottom = sRC.bottom;

			while (!isInitializedCanvas()) {
				try {
					this.wait(10);
				} catch (Exception e) {}
			}

			int len = sRC.getWH();
			if (m_pNetBuf == null) {
				m_pNetBuf = new byte[BUFSIZE];
			}
			if (getHostBpp() == 8) {
//				m_uncomp.read_zip(m_pNetBuf, m_pRawBufIndex, len);
			} else {
				if (m_canvasHandler.BitCount() <= 8) {
					read8Color(len, rc);
				} else {
					read16Color(len, rc);
				}
			}

			renderBuffer.clear();
			renderBuffer.putInt(sRC.left);
			renderBuffer.putInt(sRC.top);
			renderBuffer.putInt(sRC.getWidth());
			renderBuffer.putInt(sRC.getHeight());
			renderBuffer.flip();
			Global.GetInstance().getScreenController().render(renderBuffer.array(), renderBuffer.limit());
		}
		return 0;
	}

	public void DecodeJpg(int nCount, int nJpgQuality) {}

	public void ConvertJpgTile(byte[] cTile, int nPixel) {
		if (m_canvasHandler.ByteCount() == 1) {}
		else if (m_canvasHandler.ByteCount() == 2) {
			short pClr16 = 0;
			for (int i = 0; i < nPixel; ++i) {
				pClr16 = C24_15_BGR(cTile, i * 3);
				cTile[i * 2] = (byte) (pClr16 & 0xff);
				cTile[i * 2 + 1] = (byte) ((pClr16 >> 8) & 0xff);
			}
		}
	}

	private static short C24_15_BGR(byte[] c24, int idx) {
		short s = (short) ((c24[idx] >> 3) | ( (short)((c24[idx +1] >> 3)&0xffff) <<  5) | ((short)((c24[idx+2] >> 3)&0xffff) << 10));
		return s;
	}

	void Jpg_DecodePaletteColor(int nColor, short[] palJpg) {
	}

	short[] Jpg_DecodeShape(short[] pLess, short[] pal24, int nColor, short[] cTile, int tscan) {
		return null;
	}

	public byte[] Jpg_DecodeShape24(byte[] pLess, byte[] pal24, int nColor, byte[] cTile, int tscan) {
		return null;
	}

	short[] Hif_RLE(int nColor, short[] len, short[] pLess) {
		return null;
	}

	public int C24_16_BGR(short[] c24, int nIndex) {
		return (int) ((c24[0] >> 3) | (((int) (c24[1] >> 2)) << 5) | (((int) (c24[2] >> 3)) << 11));
	}

	public int get_max_zcomp_size(int size) {
		return size + size/100 + 8;
	}
}
