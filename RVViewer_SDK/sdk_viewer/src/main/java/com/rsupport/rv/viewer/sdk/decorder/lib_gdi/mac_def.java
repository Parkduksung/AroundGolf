package com.rsupport.rv.viewer.sdk.decorder.lib_gdi;


import com.rsupport.rv.viewer.sdk.decorder.model.RGBQUAD;

// scap/mac_def.h 파일을 포팅
public class mac_def {
	
//	#	define GetRValue(rgb)						((U8)       (rgb))
//	#	define GetGValue(rgb)						((U8)(((U16)(rgb)) >> 8))
//	#	define GetBValue(rgb)						((U8)(      (rgb)  >>16))
//	#	define GetAValue(rgb)						((U8)(      (rgb)  >>24))
//	#	define RGB(R,G,B) 		((U32)(((U8)(R)|((U16)((U8)(G))<<8))|(((U32)(U8)(B))<<16)))
	
	public static short GetRValue(long rgb) {
		return (short) rgb;
	}

	public static short GetGValue(long rgb) {
		return ((short) (((int) (rgb)) >> 8));
	}

	public static short GetBValue(long rgb) {
		return ((short) ((rgb) >> 16));
	}

	public static short GetAValue(long rgb) {
		return ((short) ((rgb) >> 24));
	}

	public static long RGB(short R, short G, short B) {
		return ((long) (((short) (R) | ((int) ((short) (G)) << 8)) | (((long) (short) (B)) << 16)));
	}
	
	
	// 아래내용은 lib_gid/ColorConv.h 파일내용주에서 포팅한 부분
	//////////////////////////////////////////////////////////////////////////
	// ?? -> RGB
	// 	inline COLORREF C15_RGB(COLOR32 clr15)
	public static long C15_RGB(long clr15)
	{
		return RGB( ((short)((clr15 >> 7) & 0xF8)),  ((short)((clr15 >> 2) & 0xF8)), ((short)((clr15 << 3) & 0xF8)));
	}

	// inline COLORREF C16_RGB(COLOR32 clr16)
	public static long  C16_RGB(long clr16)
	{
		return RGB( ((short) ((clr16 >> 8) & 0xF8)), ((short)((clr16 >> 3) & 0xFC)), ((short) ((clr16 << 3) & 0xF8)));
	}

	// inline COLORREF C32_RGB(COLOR32 clr32)
	public static long C32_RGB(long clr32) 
	{ 
		return 
			((clr32&0x0000FF) << 16) |
			((clr32&0xFF0000) >> 16) |
			((clr32&0x00FF00)      ) ;

//		U8* p = (U8*)&clr32; U8 t = p[0]; p[0] = p[2]; p[2] = t;
//		return clr32&0x00FFFFFF;
	}
	
	// inline COLORREF CPAL_RGB(int index, PALETTEENTRY * pe)
	public static long CPAL_RGB(int index, PALETTEENTRY[] pe)
	{
		return RGB(pe[index].peRed, pe[index].peGreen, pe[index].peBlue);
	}

	// inline COLORREF CPAL_RGB(int index, RGBQUAD * rq)
	public static long CPAL_RGB(int index, RGBQUAD[] rq)
	{
		return RGB(rq[index].rgbRed, rq[index].rgbGreen, rq[index].rgbBlue);
	}
	
	// 아래코드는 사용되지 않는다.
	// #define COLOR15_TO_RGB(clr15)		RGB( (clr15 & MASK15_RED)>>7, (clr15 & MASK15_GREEN) >> 2, (clr15 & MASK15_BLUE) << 3 )
	// inline COLOR16 C32_16(COLOR32 c32)
	// inline COLOR16 C32_15(COLOR32 c32)
	
	// inline COLOR16 RGB16(int r, int g, int b)
	public static int RGB16(int r, int g, int b)
	{
		return (int)(
			((r & 0xF8) >> 3) |
			((g & 0xFC) << 3) |
			((b & 0xF8) << 8) );
	}
	
	// inline COLOR16 RGB15(int r, int g, int b)
	// inline COLOR16 C32_16(COLOR8 * c32)
	// inline COLOR16 C32_15(COLOR8 * c32)
	
	// inline COLOR16 C24_15(COLOR8 * c24)
	public static int C24_15(short[] c24)
	{
		return (short)
			(
			(c24[0] >> 3)         | 
			(((short)(c24[1] >> 3)) <<  5) | 
			(((short)(c24[2] >> 3)) << 10)
			);
	}
	// inline COLOR16 C24_16(COLOR8 * c24)
	// inline COLOR32 C15_32(COLOR16 c16)
	// inline COLOR8 C32_8(COLOR32 & c32)
	// inline COLOR32 C8_32(COLOR8 c8)
	// inline COLOR8 C15_8(COLOR16 & c16)
	// inline COLOR8 C15_1(COLOR16 clr)
	// inline COLOR8 C32_1(COLOR32 clr)
	
	
}
