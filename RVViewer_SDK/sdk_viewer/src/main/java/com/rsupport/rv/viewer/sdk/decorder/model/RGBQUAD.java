package com.rsupport.rv.viewer.sdk.decorder.model;

public class RGBQUAD implements IModel {
	// U8 rgbBlue, rgbGreen, rgbRed;
	// U8 rgbReserved;

	public RGBQUAD() {
	}

	public short rgbBlue, rgbGreen, rgbRed, rgbReserved;

	public void setValue(short rgbBlue, short rgbGreen, short rgbRed,
			short rgbReserved) {
		this.rgbBlue = rgbBlue;
		this.rgbGreen = rgbGreen;
		this.rgbRed = rgbRed;
	}
	
	public void setValue(short[] val) {
		this.rgbBlue = val[0];
		this.rgbGreen = val[1];
		this.rgbRed = val[2];
		this.rgbReserved = val[3];
	}

	public void save(byte[] szBuffer, int nStart) {
		int nIndex = nStart;

		rgbBlue = (short) ((int) szBuffer[nIndex] & 0xff);
		nIndex++;
		rgbGreen = (short) ((int) szBuffer[nIndex] & 0xff);
		nIndex++;
		rgbRed = (short) ((int) szBuffer[nIndex] & 0xff);
		nIndex++;
		rgbReserved = (short) ((int) szBuffer[nIndex] & 0xff);
		nIndex++;
	}

	public void push(byte[] szBuffer, int nStart) {
		int nIndex = nStart;

		szBuffer[nIndex] = (byte) rgbBlue;
		nIndex++;
		szBuffer[nIndex] = (byte) rgbGreen;
		nIndex++;
		szBuffer[nIndex] = (byte) rgbRed;
		nIndex++;
		szBuffer[nIndex] = (byte) rgbReserved;
		nIndex++;
	}

	public void save2(byte[] szBuffer, int start, int dstOffset, int dstLen) {}
	public int size() {
		return 4;
	}
}
