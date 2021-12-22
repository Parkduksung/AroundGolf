package com.rsupport.rv.viewer.sdk.decorder.model;

public class BITMAPINFOHEADER {
//	U32     biSize;
//	I32     biWidth;
//	I32     biHeight;
//	U16     biPlanes;
//	U16     biBitCount;
//	U32     biCompression;
//	U32     biSizeImage;
//	I32     biXPelsPerMeter;
//	I32		biYPelsPerMeter;
//	U32		biClrUsed;
//	U32		biClrImportant;
		
	public long biSize;
	public int biWidth;
	public int biHeight;
	public int biPlanes;
	public int biBitCount;
	public long biCompression;
	public long biSizeImage;
	public int biXPelsPerMeter;
	public int biYPelsPerMeter;
	public long biClrUsed;
	public long biClrImportant;
	
	
	public void copy(BITMAPINFOHEADER that) {
		biSize = that.biSize;
		biWidth = that.biWidth;
		biHeight = that.biHeight;
		biPlanes = that.biPlanes;
		biBitCount = that.biBitCount;
		biCompression = that.biCompression;
		biSizeImage = that.biSizeImage;
		biXPelsPerMeter = that.biXPelsPerMeter;
		biYPelsPerMeter = that.biYPelsPerMeter;
		biClrUsed = that.biClrUsed;
		biClrImportant = that.biClrImportant;
	}
}
