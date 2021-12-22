package com.rsupport.rv.viewer.sdk.decorder.model;


import com.rsupport.rv.viewer.sdk.decorder.Converter;

public class RECT implements IModel{
	public int left;
	public int top;
	public int right;
	public int bottom;

	public RECT() {
	}

	public RECT(int left, int top, int right, int bottom) {
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
	
	public void SetRect(int left, int top, int right, int bottom) {
		this.left = left;
		this.top = top;
		this.right = right;
		this.bottom = bottom;
	}
	
	public void push(byte[] szBuffer, int nStart) {
		int nIndex = nStart;
		
		System.arraycopy(Converter.getBytesFromIntLE(left), 0, szBuffer,
			nIndex, 4);
		nIndex += 4;
		System.arraycopy(Converter.getBytesFromIntLE(top), 0, szBuffer,
			nIndex, 4);
		nIndex += 4;
		System.arraycopy(Converter.getBytesFromIntLE(right), 0, szBuffer,
			nIndex, 4);
		nIndex += 4;
		System.arraycopy(Converter.getBytesFromIntLE(bottom), 0, szBuffer,
			nIndex, 4);
		nIndex += 4;
	}

	public void save(byte[] szBuffer, int nStart) {		
		int nIndex = nStart;
		
		left =  Converter.readIntLittleEndian(szBuffer, nIndex);
		nIndex += 4;
		top =  Converter.readIntLittleEndian(szBuffer, nIndex);
		nIndex += 4;
		right =  Converter.readIntLittleEndian(szBuffer, nIndex);
		nIndex += 4;
		bottom =  Converter.readIntLittleEndian(szBuffer, nIndex);
		nIndex += 4;
	}

	public void save2(byte[] szBuffer, int start, int dstOffset, int dstLen) {
		// TODO Auto-generated method stub
		
	}

	public int size() {
		// TODO Auto-generated method stub
		return 16;
	}
}
