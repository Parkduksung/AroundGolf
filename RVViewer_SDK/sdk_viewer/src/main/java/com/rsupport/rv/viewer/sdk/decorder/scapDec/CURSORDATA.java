package com.rsupport.rv.viewer.sdk.decorder.scapDec;

public class CURSORDATA {
//	U16 cjBits; ///< length of changed cursor info
//	U8 index;
//	U8 iClrBpp; ///< if 0, mono cursor
//
//	U8 xHot;
//	U8 yHot;
//	U8 cx;
//	U8 cy;
//
//	U16 iClrUsed;
//	U8 swap; ///< if 1, swap image vertically.
//	U8 iCompression;
//
//	U8 pvBits[4];
	
	public int cjBits; ///< length of changed cursor info
	public short index;
	public short iClrBpp; ///< if 0, mono cursor
	
	public short xHot;
	public short yHot;
	public short cx;
	public short cy;
	
	public int iClrUsed;
	public short swap; ///< if 1, swap image vertically.
	public short iCompression;
	public short pvBits[] = new short[4];
	
	public CURSORDATA() {
		
	}
	
	public void copyPvBits(short pvBits[]) {
		for(int i=0; i<pvBits.length; i++) {
			this.pvBits[i] = pvBits[i];
		}
	}
	
}
