package com.rsupport.rv.viewer.sdk.decorder.lib_gdi;



//lib_gdi/ColorConv.h 헤더파일 포팅

import com.rsupport.rv.viewer.sdk.decorder.model.COLOR_MASK;
import com.rsupport.rv.viewer.sdk.decorder.model.RGBQUAD;

public class ColorConv {
	public static final int MASK16_RED = 0xF800;
	public static final int MASK16_GREEN = 0x07E0;
	public static final int MASK16_BLUE = 0x001F;

	public static final int MASK15_RED = 0x7E00;
	public static final int MASK15_GREEN = 0x03E0;
	public static final int MASK15_BLUE = 0x001F;

	public static final int MASK24_RED = 0xff0000;
	public static final int MASK24_GREEN = 0x00ff00;
	public static final int MASK24_BLUE = 0x0000ff;

	// typedef U32 COLOR32;
	// typedef U16 COLOR16;
	// typedef U8 COLOR8;
	// typedef struct { U8 clr[3];} COLOR24;

	PALETTEENTRY pe256[] = new PALETTEENTRY[256];

	public class COLOR24 {
		public short clr[] = new short[3];
	}

	public ColorConv() {
		for (int i = 0; i < pe256.length; i++) {
			pe256[i] = new PALETTEENTRY();
		}
	}

	// bool_t IsEqual(const COLOR24& c1, const COLOR24& c2)
	public boolean IsEqual(final COLOR24 c1, final COLOR24 c2) {
		return c1.clr[0] == c2.clr[0] && c1.clr[1] == c2.clr[1]
				&& c1.clr[2] == c2.clr[2];
	}

	// int gdi::GetConvertTable(int nBppFrom, int nBppTo, U8* pConvertTable)
	public int GetConvertTable(int nBppFrom, int nBppTo, short[] pConvertTable,
			int index) {

		int nClr = 0, nOutClrMax = 0, nInClrMax = 0;
		if (nBppFrom <= nBppTo) {
			// ASSERT(FALSE);
			return 0;
		}

		if (nBppTo > 8)
			return 0; // Not need convert table.

		if (nBppFrom > 16)
			nBppFrom = 15; // 32->15

		// PALETTEENTRY pe256[256]; -> 멤버변수로 대체 (메모리문제)
		int nPal = pal.GetSudoColor(nBppTo, pe256);

		nOutClrMax = 1 << nBppTo;
		nInClrMax = 1 << nBppFrom;

		switch (nBppFrom) {
		case 16: // 16 -> 8
			for (nClr = 0; nClr < nInClrMax; ++nClr) {
				pConvertTable[nClr] = (short) GetNearestColorIndex(pe256, nPal,
					mac_def.C16_RGB(nClr));
				// ASSERT(pConvertTable[nClr] < nOutClrMax);
			}
			break;

		case 15: // 15 -> 8
			for (nClr = 0; nClr < nInClrMax; ++nClr) {
				pConvertTable[nClr] = (short) GetNearestColorIndex(pe256, nPal,
					mac_def.C15_RGB(nClr));
				// ASSERT(pConvertTable[nClr] < nOutClrMax);
			}
			break;

		case 8: // 256color -> 4color
			break;
		}

		return nClr;
	}

	// static int GetNearestColorIndex(const PALETTEENTRY* pe, int nMaxPE, U32
	// clr)
	public int GetNearestColorIndex(PALETTEENTRY[] pe, int nMaxPE, long clr) {
		int nMinIdx = 0;
		// U32 nMinDiff = 0xFFFFFFFF, diff;
		long nMinDiff = 0xFFFFFFFF, diff;

		int theR = mac_def.GetRValue(clr);
		int theG = mac_def.GetGValue(clr);
		int theB = mac_def.GetBValue(clr);

		for (int i = 0; i < nMaxPE; ++i) {
			// diff = _pow2(theR-pe[i].peRed) +
			// _pow2(theG-pe[i].peGreen) +
			// _pow2(theB-pe[i].peBlue) ;
			diff = (theR - pe[i].peRed) * (theR - pe[i].peRed)
					+ (theG - pe[i].peGreen) * (theG - pe[i].peGreen)
					+ (theB - pe[i].peBlue) * (theB - pe[i].peBlue);

			if (diff < nMinDiff) {
				nMinDiff = diff;
				nMinIdx = i;
				if (diff == 0)
					break;
			}
		}
		return nMinIdx;
	}

	// inline PALETTEENTRY* RgbQuad2PaletteEntry(RGBQUAD* rq, int n)
	public PALETTEENTRY[] RgbQuad2PaletteEntry(RGBQUAD[] rq, int n) {
		PALETTEENTRY pal[] = new PALETTEENTRY[n];

		// U8 tmp;
		short tmp;
		for (int i = 0; i < n; ++i) {
			tmp = rq[i].rgbBlue;
			rq[i].rgbBlue = rq[i].rgbRed;
			rq[i].rgbRed = tmp;
			rq[i].rgbReserved = 0;

			pal[i] = new PALETTEENTRY();
			pal[i].setValue(rq[i].rgbBlue, rq[i].rgbGreen, rq[i].rgbRed,
				rq[i].rgbReserved);
		}

		// return (PALETTEENTRY*)rq;
		return pal;
	}

	// inline void RgbQuad2PaletteEntry(PALETTEENTRY* pe, RGBQUAD* rq, int n)
	public static void RgbQuad2PaletteEntry(PALETTEENTRY[] pe, RGBQUAD[] rq, int n) {
		for (int i = 0; i < n; ++i) {
			pe[i].peBlue = rq[i].rgbBlue;
			pe[i].peGreen = rq[i].rgbGreen;
			pe[i].peRed = rq[i].rgbRed;
			pe[i].peFlags = 0;
		}
	}
	
	// inline RGBQUAD* PaletteEntry2RgbQuad(PALETTEENTRY* pe, int n)
	public static RGBQUAD[] PaletteEntry2RgbQuad(PALETTEENTRY[] pe, int n)
	{
		RGBQUAD rg[] = new RGBQUAD[n];
		
		// U8	tmp;
		short tmp;
		for(int i=0; i<n; ++i) {
			tmp = pe[i].peBlue;
			pe[i].peBlue = pe[i].peRed;
			pe[i].peRed = tmp;
			pe[i].peFlags = 0;
			
			rg[i] = new RGBQUAD();
			rg[i].setValue(pe[i].peRed, pe[i].peGreen, pe[i].peBlue, pe[i].peFlags);
		}
		// return (RGBQUAD*)pe;
		return rg;
	}
	
	
	// inline void PaletteEntry2RgbQuad(RGBQUAD* rq, PALETTEENTRY* pe, int n)
	public static void PaletteEntry2RgbQuad(RGBQUAD[] rq, PALETTEENTRY[] pe, int n)
	{
		for(int i=0; i<n; ++i) {
			rq[i].rgbBlue = pe[i].peBlue;
			rq[i].rgbGreen = pe[i].peGreen;
			rq[i].rgbRed = pe[i].peRed;
			rq[i].rgbReserved = 0;
		}
	}	
	
	// inline U8 GetR15(COLOR16 clr) { return (clr & MASK15_RED)   >> 7; }
	public short GetR15(int clr) { return (short) ((clr & MASK15_RED)   >> 7); }
	// inline U8 GetG15(COLOR16 clr) { return (clr & MASK15_GREEN) >> 2; }
	public short GetG15(int clr) { return (short) ((clr & MASK15_GREEN) >> 2); }
	// inline U8 GetB15(COLOR16 clr) { return (clr & MASK15_BLUE)  << 3; }
	public short GetB15(int clr) { return (short) ((clr & MASK15_BLUE)  << 3); }
	
	
	// inline U8 GetR16(COLOR16 clr) { return (clr & MASK16_RED)   >> 8; }
	public short GetR16(int clr) { return (short) ((clr & MASK16_RED) >> 8); }
	// inline U8 GetG16(COLOR16 clr) { return (clr & MASK16_GREEN) >> 3; }
	public short GetG16(int clr) { return (short) ((clr & MASK16_GREEN) >> 3); }
	// inline U8 GetB16(COLOR16 clr) { return (clr & MASK16_BLUE)  << 3; }
	public short GetB16(int clr) { return (short) ((clr & MASK16_BLUE)  << 3); }

	// inline U8 GetR15(COLOR32 clr) { return (U8)((clr & MASK15_RED)   >> 7); }
	public short GetR15(long clr) { return (short)((clr & MASK15_RED)   >> 7); }	
	// inline U8 GetG15(COLOR32 clr) { return (U8)((clr & MASK15_GREEN) >> 2); }
	public short GetG15(long clr) { return (short)((clr & MASK15_GREEN) >> 2); }
	// inline U8 GetB15(COLOR32 clr) { return (U8)((clr & MASK15_BLUE)  << 3); }
	public short GetB15(long clr) { return (short)((clr & MASK15_BLUE)  << 3); }
	
	// inline U8 GetR16(COLOR32 clr) { return (U8)((clr & MASK16_RED)   >> 8); }
	public short GetR16(long clr) { return (short)((clr & MASK16_RED)   >> 8); }
	// inline U8 GetG16(COLOR32 clr) { return (U8)((clr & MASK16_GREEN) >> 3); }
	public short GetG16(long clr) { return (short)((clr & MASK16_GREEN) >> 3); }
	// inline U8 GetB16(COLOR32 clr) { return (U8)((clr & MASK16_BLUE)  << 3); }
	public short GetB16(long clr) { return (short)((clr & MASK16_BLUE)  << 3); }


	// inline U8 GetR15(U8 * pxl) { return (pxl[1] << 1) & 0xF8; }
	public short GetR15(short[] pxl) { return (short) ((pxl[1] << 1) & 0xF8); }
	// inline U8 GetG15(U8 * pxl) { return (* (U16 *) pxl >> 2) & 0xF8; }
	public short GetG15(short[] pxl) { return (short) (((int)pxl[0] >> 2) & 0xF8); }
	// inline U8 GetB15(U8 * pxl) { return (pxl[0] << 3); }
	public short GetB15(short[] pxl) { return (short)((pxl[0] << 3)); }

	// inline U8 GetR16(U8 * pxl) { return pxl[1] & 0xF8; }
	public short GetR16(short[] pxl) { return (short) (pxl[1] & 0xF8); }
	// inline U8 GetG16(U8 * pxl) { return (* (U16 *) pxl >> 3) & 0xFC; }
	public short GetG16(short[] pxl) { return (short) (((int)pxl[0] >> 3) & 0xFC); }
	// inline U8 GetB16(U8 * pxl) { return (pxl[0] << 3); }
	public short GetB16(short[] pxl) { return (short) ((pxl[0] << 3)); }
	
//	inline COLOR16 C24_15(COLOR8 * c24)
//	{
//		return (COLOR16)
//			(
//			(c24[0] >> 3)         | 
//			(((COLOR16)(c24[1] >> 3)) <<  5) | 
//			(((COLOR16)(c24[2] >> 3)) << 10)
//			);
//	}
	
	public static short C24_15(byte[] c24) {
		return (short) ((  (c24[0]&0xff) >> 3)         | 
				(((short)( (c24[1]&0xff) >> 3)) <<  5) | 
				(((short)( (c24[2]&0xff) >> 3)) << 10));
	}
	
	
//	public int GetConvertTable(int nBppFrom, int nBppTo, byte[] pConvertTable) {
//		
//	}
	
	
	
	// COLOR_MASK GetColorMask(HDC hDC, int nBpp = 0);
	public static COLOR_MASK _GetColorMask(int nBitCount)
	{
		COLOR_MASK clrMask = new COLOR_MASK();
		switch(nBitCount)
		{
		case 15:
			clrMask.red		= MASK15_RED;
			clrMask.green	= MASK15_GREEN;
			clrMask.blue	= MASK15_BLUE;
			break;
		case 16:
			clrMask.red		= MASK16_RED;
			clrMask.green	= MASK16_GREEN;
			clrMask.blue	= MASK16_BLUE;
			break;
		case 24:
		case 32:
			clrMask.red		= MASK24_RED;
			clrMask.green	= MASK24_GREEN;
			clrMask.blue	= MASK24_BLUE;
			break;
		}

		return clrMask;
	}
	
	// COLOR_MASK gdi::GetColorMask(HDC hDC, int nBpp)
	public static COLOR_MASK GetColorMask(int nBpp) {
		return _GetColorMask(nBpp);
		
		// 아래코드는 win32 api를 이용한 코드라서 구현할수 없음.
//		COLOR_MASK cm = {0,};
//		#ifdef WIN32
//			if (GetDeviceCaps(hDC, PLANES) == 1) 
//			{
//				gdi::DIBINFO dibinfo = GetDibInfo(hDC);
//				if (nBpp != dibinfo.bmiHeader.biBitCount)
//					return _GetColorMask(nBpp);
//
//				if (dibinfo.bmiHeader.biCompression == BI_BITFIELDS)
//					cm = dibinfo.bmiMask;
//				else if (dibinfo.bmiHeader.biCompression == BI_RGB)
//				{
//					cm = _GetColorMask((int)dibinfo.bmiHeader.biBitCount);
//				}
//			}
//		#endif
//			return cm;
	}

}
