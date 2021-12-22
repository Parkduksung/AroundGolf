package com.rsupport.rv.viewer.sdk.decorder.lib_gdi;



// lib_gdi/pal.cpp를 포팅한 내용

import com.rsupport.rv.viewer.sdk.decorder.model.RGBQUAD;

public class pal {
	
	// HPALETTE gdi::CreateSudoPalette(int bpp)
	public void CreateSudoPalette(int bpp)
	{
		//미구현
	}
	
//	public static void main(String args[]) {
//		RLog.d(-91);
//	}
	
	
	// static PALETTEENTRY ms_StdColors[] = { // R.G.B.Flags order
	public static byte ms_StdColors[][] = {  // R.G.B.Flags order
			// 20 reserved pal + 6-6-6
		{-1,-1,-1,0},{0,0,0,0},{-128,0,0,0},{0,-128,0,0},{-128,-128,0,0},{0,0,-128,0},
		{-128,0,-128,0},{0,-128,-128,0},{-64,-64,-64,0},{-64,-36,-64,0},{-90,-54,-16,0},{-1,-5,-16,0},
		{-96,-96,-92,0},{-128,-128,-128,0},{-1,0,0,0},{0,-1,0,0},{-1,-1,0,0},{0,0,-1,0},
		{-1,0,-1,0},{0,-1,-1,0},{-1,-1,-52,0},{-1,-1,-103,0},{-1,-1,102,0},{-1,-1,51,0},
		{-1,-52,-1,0},{-1,-52,-52,0},{-1,-52,-103,0},{-1,-52,102,0},{-1,-52,51,0},{-1,-52,0,0},
		{-1,-103,-1,0},{-1,-103,-52,0},{-1,-103,-103,0},{-1,-103,102,0},{-1,-103,51,0},{-1,-103,0,0},
		{-1,124,-128,0},{-1,102,-1,0},{-1,102,-52,0},{-1,102,-103,0},{-1,102,102,0},{-1,102,51,0}, // -
		{-1,102,0,0},{-1,80,80,0},{-1,51,-1,0},{-1,51,-52,0},{-1,51,-103,0},{-1,51,102,0},
		{-1,51,51,0},{-1,51,0,0},{-1,0,-52,0},{-1,0,-103,0},{-1,0,102,0},{-1,0,51,0},
		{-15,-15,-15,0},{-17,-42,-58,0},{-25,-25,-42,0},{-41,-41,-41,0},{-42,0,-109,0},{-52,-1,-1,0},
		{-52,-1,-52,0},{-52,-1,-103,0},{-52,-1,102,0},{-52,-1,51,0},{-52,-1,0,0},{-52,-20,-1,0},
		{-52,-52,-1,0},{-52,-52,-103,0},{-52,-52,102,0},{-52,-52,51,0},{-52,-52,0,0},{-52,-103,-1,0},
		{-52,-103,-52,0},{-52,-103,-103,0},{-52,-103,102,0},{-52,-103,51,0},{-52,-103,0,0},{-52,102,-1,0},
		{-52,102,-52,0},{-52,102,-103,0},{-52,102,102,0},{-52,102,51,0},{-52,102,0,0},{-52,51,-1,0},
		{-52,51,-52,0},{-52,51,-103,0},{-52,51,102,0},{-52,51,51,0},{-52,51,0,0},{-52,0,-1,0},
		{-52,0,-52,0},{-52,0,102,0},{-52,0,51,0},{-52,0,0,0},{-78,-78,-78,0},{-83,-87,-112,0},
		{-91,0,33,0},{-103,-1,-1,0},{-103,-1,-52,0},{-103,-1,-103,0},{-103,-1,102,0},{-103,-1,51,0},
		{-103,-1,0,0},{-103,-52,-1,0},{-103,-52,-52,0},{-103,-52,-103,0},{-103,-52,102,0},{-103,-52,51,0},
		{-103,-52,0,0},{-103,-103,-1,0},{-103,-103,-52,0},{-103,-103,102,0},{-103,-103,51,0},{-103,-103,0,0},
		{-103,102,-1,0},{-103,102,-52,0},{-103,102,-103,0},{-103,102,102,0},{-103,102,51,0},{-103,102,0,0},
		{-103,51,-1,0},{-103,51,-52,0},{-103,51,-103,0},{-103,51,102,0},{-103,51,51,0},{-103,51,0,0},
		{-103,0,-1,0},{-103,0,-52,0},{-103,0,-103,0},{-103,0,102,0},{-103,0,51,0},{-103,0,0,0},
		{-106,-106,-106,0},{102,-1,-1,0},{102,-1,-52,0},{102,-1,-103,0},{102,-1,102,0},{102,-1,51,0},
		{102,-1,0,0},{102,-52,-1,0},{102,-52,-52,0},{102,-52,-103,0},{102,-52,102,0},{102,-52,51,0},
		{102,-52,0,0},{102,-103,-1,0},{102,-103,-52,0},{102,-103,-103,0},{102,-103,102,0},{102,-103,51,0},
		{102,-103,0,0},{102,102,-1,0},{102,102,-52,0},{102,102,-103,0},{102,102,102,0},{102,102,51,0},
		{102,102,0,0},{102,51,-1,0},{102,51,-52,0},{102,51,-103,0},{102,51,102,0},{102,51,51,0},
		{102,51,0,0},{102,0,-1,0},{102,0,-52,0},{102,0,-103,0},{102,0,102,0},{102,0,51,0},
		{102,0,0,0},{85,85,85,0},{66,66,66,0},{51,-1,-1,0},{51,-1,-52,0},{51,-1,-103,0},
		{51,-1,102,0},{51,-1,51,0},{51,-1,0,0},{51,-52,-1,0},{51,-52,-52,0},{51,-52,-103,0},
		{51,-52,102,0},{51,-52,51,0},{51,-52,0,0},{51,-103,-1,0},{51,-103,-52,0},{51,-103,-103,0},
		{51,-103,102,0},{51,-103,51,0},{51,-103,0,0},{51,102,-1,0},{51,102,-52,0},{51,102,-103,0},
		{51,102,102,0},{51,102,51,0},{51,102,0,0},{51,51,-1,0},{51,51,-52,0},{51,51,-103,0},
		{51,51,102,0},{51,51,51,0},{51,51,0,0},{51,0,-1,0},{51,0,-52,0},{51,0,-103,0},
		{51,0,102,0},{51,0,51,0},{51,0,0,0},{34,34,34,0},{0,-1,-52,0},{0,-1,-103,0},
		{0,-1,102,0},{0,-1,51,0},{0,-52,-1,0},{0,-52,-52,0},{0,-52,-103,0},{0,-52,102,0},
		{0,-52,51,0},{0,-52,0,0},{0,-103,-1,0},{0,-103,-52,0},{0,-103,-103,0},{0,-103,102,0},
		{0,-103,51,0},{0,-103,0,0},{0,102,-1,0},{0,102,-52,0},{0,102,-103,0},{0,102,102,0},
		{0,102,51,0},{0,102,0,0},{0,51,-1,0},{0,51,-52,0},{0,51,-103,0},{0,51,102,0},
		{0,51,51,0},{0,51,0,0},{0,0,-52,0},{0,0,-103,0},{0,0,102,0},{0,0,51,0},	
		
		{0,0,0,0},{0,0,0,0},{0,0,0,0},{0,0,0,0},{0,0,0,0},{0,0,0,0},{0,0,0,0},{0,0,0,0},{0,0,0,0},{ 0,0,0,0},
		{0,0,0,0},{0,0,0,0},{0,0,0,0},{0,0,0,0},{0,0,0,0},{0,0,0,0} 

//		{0x9D,0x9D,0x9D,0},{ 0xA8,0xA8,0xA8,0},{ 0xB4,0xB4,0xB4,0},{ -52,-52,-52,0},
//		{0xD8,0xD8,0xD8,0},{ 0xE5,0xE5,0xE5,0},{ 0xF2,0xF2,0xF2,0},{ -1,-1,-1,0},
//				  
//		// System palette - last 10 colors
//		{-16,-5,-1,0},{ -92,-96,-96,0},{ 0x80,0x80,0x80,0},{ 0,0,-1,0},
//		{0,-1,0,0},{ 0,-1,-1,0},{ -1,0,0,0},{ -1,0,-1,0},
//		{-1,-1,0,0},{ -1,-1,-1,0}
	};	
	
//	public static void main(String[] args) {
//		 //red
//		StringBuffer buf = new StringBuffer();
//		for(int i=0; i<ms_StdColors.length; i++) {
//			short rgb[] = ms_StdColors[i];
//			if(i != 0) {
//				buf.append(',');
//			}
//			buf.append("0x" + Integer.toHexString(rgb[2]).toUpperCase());
//		}
//		
//		RLog.d(buf.toString());
//		
//		int n1 = red.length;
//		int n2 = green.length;
//		int n3 = blue.length;
//		
//		RLog.d(n1 + "," + n2 + "," + n3);
//		
//		byte[] _blue = new byte[256];
//		StringBuffer buf = new StringBuffer();
//		for(int i=0; i<blue.length; i++) {			
//			_blue[i] = blue[i];			
//			if(i != 0) {
//				buf.append(',');
//			}			
//			buf.append(_blue[i]);
//		}
//		RLog.d(buf.toString());
//		
//	}
	// public int GetSudoColor(int bpp, PALETTEENTRY* pe)
	public static int GetSudoColor(int bpp, PALETTEENTRY[] pe)
	{
//		int nClrMax = 1 << bpp;
//		int nClr = 0;
//		
//		if (nClrMax == 2) // 2 color.
//		{
//			pe[nClr].peBlue = pe[nClr].peGreen= pe[nClr].peRed = 0; ++nClr;
//			pe[nClr].peBlue = pe[nClr].peGreen= pe[nClr].peRed = 0xD0; ++nClr;
//		}
//		else if(nClrMax == 4)
//		{
//			pe[nClr].peBlue = pe[nClr].peGreen= pe[nClr].peRed = 0x1C; ++nClr;
//			pe[nClr].peBlue = pe[nClr].peGreen= pe[nClr].peRed = 0x4F; ++nClr;
//			pe[nClr].peBlue = pe[nClr].peGreen= pe[nClr].peRed = 0x82; ++nClr;
//			pe[nClr].peBlue = pe[nClr].peGreen= pe[nClr].peRed = 0xE8; ++nClr;
//		}
//		else if(nClrMax == 16)
//		{
//			int i;
//			for(i = 0; i < 8; i++) {
//				// pe[nClr++] = ms_StdColors[i];
//				pe[nClr++].setValue(ms_StdColors[i]);
//			}
//				
//			
//			for(i = 8; i < 16; i++) {
//				// pe[nClr++] = ms_StdColors[248+i];
//				pe[nClr++].setValue(ms_StdColors[248+i]);
//			}
//		} 
//		else if(nClrMax == 256)
//		{ 
//			for(int i = 0; i < 256; i++) {
//				// pe[nClr++] = ms_StdColors[i];
//				pe[nClr++].setValue(ms_StdColors[i]);
//			}
//		} 
//		
//		return nClr;
		return 0;
	}
	
	public static int GetSudoColor(int bpp, RGBQUAD[] pe)
	{
		return 0;
//		int nClrMax = 1 << bpp;
//		int nClr = 0;
//		
//		if (nClrMax == 2) // 2 color.
//		{
//			pe[nClr].rgbRed = pe[nClr].rgbGreen= pe[nClr].rgbBlue = 0; ++nClr;
//			pe[nClr].rgbRed = pe[nClr].rgbGreen= pe[nClr].rgbBlue = 0xD0; ++nClr;
//		}
//		else if(nClrMax == 4)
//		{
//			pe[nClr].rgbRed = pe[nClr].rgbGreen= pe[nClr].rgbBlue = 0x1C; ++nClr;
//			pe[nClr].rgbRed = pe[nClr].rgbGreen= pe[nClr].rgbBlue = 0x4F; ++nClr;
//			pe[nClr].rgbRed = pe[nClr].rgbGreen= pe[nClr].rgbBlue = 0x82; ++nClr;
//			pe[nClr].rgbRed = pe[nClr].rgbGreen= pe[nClr].rgbBlue = 0xE8; ++nClr;
//		}
//		else if(nClrMax == 16)
//		{
//			int i;
//			for(i = 0; i < 8; i++) {
//				// pe[nClr++] = ms_StdColors[i];
//				pe[nClr++].setValue(ms_StdColors[i]);
//			}				
//			
//			for(i = 8; i < 16; i++) {
//				// pe[nClr++] = ms_StdColors[248+i];
//				pe[nClr++].setValue(ms_StdColors[248+i]);
//			}
//		} 
//		else if(nClrMax == 256)
//		{ 
//			for(int i = 0; i < 256; i++) {
//				// pe[nClr++] = ms_StdColors[i];
//				pe[nClr++].setValue(ms_StdColors[i]);
//			}
//		}
//		
//		return nClr;
	}	
	
//	{-1,-1,-1,0},{0,0,0,0},{0x80,0,0,0},{0,0x80,0,0},{0x80,0x80,0,0},{0,0,0x80,0},
//	{0x80,0,0x80,0},{0,0x80,0x80,0},{-64,-64,-64,0},{-64,-36,-64,0},{-90,-54,-16,0},{-1,-5,-16,0},
//	{-96,-96,-92,0},{0x80,0x80,0x80,0},{-1,0,0,0},{0,-1,0,0},{-1,-1,0,0},{0,0,-1,0},
//	{-1,0,-1,0},{0,-1,-1,0},{-1,-1,-52,0},{-1,-1,-103,0},{-1,-1,102,0},{-1,-1,51,0},
//	{-1,-52,-1,0},{-1,-52,-52,0},{-1,-52,-103,0},{-1,-52,102,0},{-1,-52,51,0},{-1,-52,0,0},
//	{-1,-103,-1,0},{-1,-103,-52,0},{-1,-103,-103,0},{-1,-103,102,0},{-1,-103,51,0},{-1,-103,0,0},
//	{-1,124,0x80,0},{-1,102,-1,0},{-1,102,-52,0},{-1,102,-103,0},{-1,102,102,0},{-1,102,51,0}, // -
//	{-1,102,0,0},{-1,80,80,0},{-1,51,-1,0},{-1,51,-52,0},{-1,51,-103,0},{-1,51,102,0},
//	{-1,51,51,0},{-1,51,0,0},{-1,0,-52,0},{-1,0,-103,0},{-1,0,102,0},{-1,0,51,0},
//	{-15,-15,-15,0},{-17,-42,-58,0},{-25,-25,-42,0},{-41,-41,-41,0},{-42,0,-109,0},{-52,-1,-1,0},
//	{-52,-1,-52,0},{-52,-1,-103,0},{-52,-1,102,0},{-52,-1,51,0},{-52,-1,0,0},{-52,-20,-1,0},
//	{-52,-52,-1,0},{-52,-52,-103,0},{-52,-52,102,0},{-52,-52,51,0},{-52,-52,0,0},{-52,-103,-1,0},
//	{-52,-103,-52,0},{-52,-103,-103,0},{-52,-103,102,0},{-52,-103,51,0},{-52,-103,0,0},{-52,102,-1,0},
//	{-52,102,-52,0},{-52,102,-103,0},{-52,102,102,0},{-52,102,51,0},{-52,102,0,0},{-52,51,-1,0},
//	{-52,51,-52,0},{-52,51,-103,0},{-52,51,102,0},{-52,51,51,0},{-52,51,0,0},{-52,0,-1,0},
//	{-52,0,-52,0},{-52,0,102,0},{-52,0,51,0},{-52,0,0,0},{-78,-78,-78,0},{-83,-87,-112,0},
//	{-91,0,33,0},{-103,-1,-1,0},{-103,-1,-52,0},{-103,-1,-103,0},{-103,-1,102,0},{-103,-1,51,0},
//	{-103,-1,0,0},{-103,-52,-1,0},{-103,-52,-52,0},{-103,-52,-103,0},{-103,-52,102,0},{-103,-52,51,0},
//	{-103,-52,0,0},{-103,-103,-1,0},{-103,-103,-52,0},{-103,-103,102,0},{-103,-103,51,0},{-103,-103,0,0},
//	{-103,102,-1,0},{-103,102,-52,0},{-103,102,-103,0},{-103,102,102,0},{-103,102,51,0},{-103,102,0,0},
//	{-103,51,-1,0},{-103,51,-52,0},{-103,51,-103,0},{-103,51,102,0},{-103,51,51,0},{-103,51,0,0},
//	{-103,0,-1,0},{-103,0,-52,0},{-103,0,-103,0},{-103,0,102,0},{-103,0,51,0},{-103,0,0,0},
//	{-106,-106,-106,0},{102,-1,-1,0},{102,-1,-52,0},{102,-1,-103,0},{102,-1,102,0},{102,-1,51,0},
//	{102,-1,0,0},{102,-52,-1,0},{102,-52,-52,0},{102,-52,-103,0},{102,-52,102,0},{102,-52,51,0},
//	{102,-52,0,0},{102,-103,-1,0},{102,-103,-52,0},{102,-103,-103,0},{102,-103,102,0},{102,-103,51,0},
//	{102,-103,0,0},{102,102,-1,0},{102,102,-52,0},{102,102,-103,0},{102,102,102,0},{102,102,51,0},
//	{102,102,0,0},{102,51,-1,0},{102,51,-52,0},{102,51,-103,0},{102,51,102,0},{102,51,51,0},
//	{102,51,0,0},{102,0,-1,0},{102,0,-52,0},{102,0,-103,0},{102,0,102,0},{102,0,51,0},
//	{102,0,0,0},{85,85,85,0},{66,66,66,0},{51,-1,-1,0},{51,-1,-52,0},{51,-1,-103,0},
//	{51,-1,102,0},{51,-1,51,0},{51,-1,0,0},{51,-52,-1,0},{51,-52,-52,0},{51,-52,-103,0},
//	{51,-52,102,0},{51,-52,51,0},{51,-52,0,0},{51,-103,-1,0},{51,-103,-52,0},{51,-103,-103,0},
//	{51,-103,102,0},{51,-103,51,0},{51,-103,0,0},{51,102,-1,0},{51,102,-52,0},{51,102,-103,0},
//	{51,102,102,0},{51,102,51,0},{51,102,0,0},{51,51,-1,0},{51,51,-52,0},{51,51,-103,0},
//	{51,51,102,0},{51,51,51,0},{51,51,0,0},{51,0,-1,0},{51,0,-52,0},{51,0,-103,0},
//	{51,0,102,0},{51,0,51,0},{51,0,0,0},{34,34,34,0},{0,-1,-52,0},{0,-1,-103,0},
//	{0,-1,102,0},{0,-1,51,0},{0,-52,-1,0},{0,-52,-52,0},{0,-52,-103,0},{0,-52,102,0},
//	{0,-52,51,0},{0,-52,0,0},{0,-103,-1,0},{0,-103,-52,0},{0,-103,-103,0},{0,-103,102,0},
//	{0,-103,51,0},{0,-103,0,0},{0,102,-1,0},{0,102,-52,0},{0,102,-103,0},{0,102,102,0},
//	{0,102,51,0},{0,102,0,0},{0,51,-1,0},{0,51,-52,0},{0,51,-103,0},{0,51,102,0},
//	{0,51,51,0},{0,51,0,0},{0,0,-52,0},{0,0,-103,0},{0,0,102,0},{0,0,51,0},	
	
}
