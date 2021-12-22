package com.rsupport.rv.viewer.sdk.decorder.lib_gdi;

public class PALETTEENTRY {
	// U8 peRed, peGreen, peBlue;
	// U8 peFlags;
	public short peRed, peGreen, peBlue;
	public short peFlags;

	public void setValue(short peRed, short peGreen, short peBlue, short peFlags) {
		this.peRed = peRed;
		this.peGreen = peGreen;
		this.peBlue = peBlue;
		this.peFlags = peFlags;
	}

	public void setValue(short[] val) {
		this.peRed = val[0];
		this.peGreen = val[1];
		this.peBlue = val[2];
		this.peFlags = val[3];
	}
	
	public int size() {
		return 4;
	}
}
