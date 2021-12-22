package com.rsupport.rv.viewer.sdk.decorder.viewer.channel;

import com.rsupport.rv.viewer.sdk.decorder.Converter;
import com.rsupport.rv.viewer.sdk.decorder.model.IModel;

public class rcpKeyEventMsg implements IModel {
	// RU8 down; // true if down (press), false if up
	// RU16 pad;
	// RU32 key; // key is specified as an X keysym
	// RU16 specialkeystate;

	public byte down;
	public short pad;
	public int key;
	public short specialkeystate;

	// 패킷 만들기
	public void push(byte[] szBuffer, int nStart) {
		int nIndex = nStart;
		szBuffer[nIndex] = down;
		nIndex++;
		System.arraycopy(Converter.getBytesFromShortLE(pad), 0, szBuffer, nIndex, 2);
		nIndex += 2;
		System.arraycopy(Converter.getBytesFromIntLE(key), 0, szBuffer, nIndex, 4);
		nIndex += 4;
		System.arraycopy(Converter.getBytesFromShortLE(specialkeystate), 0, szBuffer, nIndex, 2);
		nIndex += 2;
	}

	public void save(byte[] szBuffer, int nStart) {
	}

	public void save2(byte[] szBuffer, int nStart, int dstOffset, int dstLen) {
	}

	public int size() {
		return 9;
	}
	
	public int swap32(int key) {
		return (key&0xff000000) >> 24 | (key&0xff0000) >> 8 | (key&0xff00) << 8 | (key&0xff) << 24;
	}
	
	public long unsigned32(int n) {
		return n & 0xFFFFFFFFL;
	}
}
