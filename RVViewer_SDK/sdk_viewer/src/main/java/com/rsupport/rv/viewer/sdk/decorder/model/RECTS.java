package com.rsupport.rv.viewer.sdk.decorder.model;


import com.rsupport.rv.viewer.sdk.decorder.Converter;

public class RECTS implements IModel {
	public short left;
	public short top;
	public short right;
	public short bottom;

	public static final int SCRCPY = 0;
	public static final int PAINT = 1;
	public int type = PAINT;
	public int dx, dy;
	
	public RECTS() {
	}

	public RECTS(short left, short top, short right, short bottom) {
		this.left = left;
		this.top = top;
		this.right = right;
		this.bottom = bottom;
	}

	public int getWidth() {
		return right - left;
	}

	public int getHeight() {
		return bottom - top;
	}

	public int getWH() {
		return getWidth() * getHeight();
	}
	
	public void reshape(int x, int y, int width, int height) {
		this.left = (short) x;
		this.top = (short) y;
		this.right = (short) (width + left);
		this.bottom = (short) (height + top);
	}	

	public void push(byte[] szBuffer, int nStart) {
		int nIndex = nStart;

		// 패킷 만들기
		// left
		System.arraycopy(Converter.getBytesFromShortLE(left), 0, szBuffer,
			nIndex, 2);
		nIndex += 2;

		// top
		System.arraycopy(Converter.getBytesFromShortLE(top), 0, szBuffer,
			nIndex, 2);
		nIndex += 2;

		// right
		System.arraycopy(Converter.getBytesFromShortLE(right), 0, szBuffer,
			nIndex, 2);
		nIndex += 2;

		// bottom
		System.arraycopy(Converter.getBytesFromShortLE(bottom), 0, szBuffer,
			nIndex, 2);
		nIndex += 2;
	}

	public void save(byte[] szBuffer, int nStart) {
		int nIndex = nStart;
		left = Converter.readShortLittleEndian(szBuffer, nIndex);
		nIndex += 2;

		top = Converter.readShortLittleEndian(szBuffer, nIndex);
		nIndex += 2;

		right = Converter.readShortLittleEndian(szBuffer, nIndex);
		nIndex += 2;

		bottom = Converter.readShortLittleEndian(szBuffer, nIndex);
		nIndex += 2;
	}

	public int size() {
		return 8;
	}

	public void save2(byte[] szBuffer, int start, int dstOffset, int dstLen) {
		// TODO Auto-generated method stub
		
	}
	
	
}
