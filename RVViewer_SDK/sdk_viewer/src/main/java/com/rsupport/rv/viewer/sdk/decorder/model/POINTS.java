package com.rsupport.rv.viewer.sdk.decorder.model;


import com.rsupport.rv.viewer.sdk.decorder.Converter;

public class POINTS implements IModel {
	
	public short x = 0;
	public short y = 0;

	public POINTS(){
	}
	
	public POINTS(short x, short y) {
		this.x = x;
		this.y = y;
	}

	public void push(byte[] szBuffer, int nStart) {
		int nIndex = nStart;

		System.arraycopy(Converter.getBytesFromShortLE(x), 0, szBuffer, nIndex, 2);
		nIndex += 2;

		System.arraycopy(Converter.getBytesFromShortLE(y), 0, szBuffer, nIndex,	2);
		nIndex += 2;
	}

	public void save(byte[] szBuffer, int nStart) {
		int nIndex = nStart;
		x = Converter.readShortLittleEndian(szBuffer, nIndex);
		nIndex += 2;

		y = Converter.readShortLittleEndian(szBuffer, nIndex);
		nIndex += 2;
	}

	public int size() {
		return 4;
	}

	public void save2(byte[] szBuffer, int start, int dstOffset, int dstLen) {
	}
}
