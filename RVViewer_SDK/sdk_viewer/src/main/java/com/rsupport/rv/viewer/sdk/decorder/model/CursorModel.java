package com.rsupport.rv.viewer.sdk.decorder.model;


import com.rsupport.rv.viewer.sdk.decorder.scap.scapCursorMsg;

public class CursorModel {

//	public Cursor cursor;
//	public Image image;
	public int[] buf;
	public int cursorWidth;
	public int cursorHeight;
	public short hotX;
	public short hotY;
	public short iClrBpp;
	public int ime;
	public short cx;
	public short cy;
	public short b2t;
	public short iCompression;
	public int iClrUsed;
	public int cjBits;
	public short idx;
	public scapCursorMsg cursormsg = new scapCursorMsg();
	public byte[] pvBits;
	
	public void setDataBuffer(int len) {
		pvBits = new byte[len];
	}
	
	public byte[] getBits() {
		return pvBits;
	}
	
}