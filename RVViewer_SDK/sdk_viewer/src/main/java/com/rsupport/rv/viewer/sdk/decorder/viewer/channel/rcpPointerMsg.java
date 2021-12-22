package com.rsupport.rv.viewer.sdk.decorder.viewer.channel;


import com.rsupport.rv.viewer.sdk.decorder.Converter;
import com.rsupport.rv.viewer.sdk.decorder.model.IModel;

public class rcpPointerMsg implements IModel {

	public short x;
	public short y;

	public void push(byte[] szBuffer, int nStart) {
		int nIndex = nStart;
		System.arraycopy(Converter.getBytesFromShortLE(x), 0, szBuffer, nIndex, 2);
		nIndex += 2;
		System.arraycopy(Converter.getBytesFromShortLE(y), 0, szBuffer, nIndex, 2);
	}

	public void save(byte[] szBuffer, int nStart) {
	}

	public void save2(byte[] szBuffer, int nStart, int dstOffset, int dstLen) {
	}

	public int size() {
		return 4;
	}
}
