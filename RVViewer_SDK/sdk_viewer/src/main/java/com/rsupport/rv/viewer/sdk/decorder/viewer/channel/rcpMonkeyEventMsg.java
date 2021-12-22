package com.rsupport.rv.viewer.sdk.decorder.viewer.channel;

import com.rsupport.rv.viewer.sdk.decorder.Converter;
import com.rsupport.rv.viewer.sdk.decorder.model.IModel;

public class rcpMonkeyEventMsg implements IModel {

	public int count;
	public int id = 0;
	public int action;
	public short x1;
	public short y1;
	public short x2;
	public short y2;

	public void push(byte[] szBuffer, int nStart) {
		int nIndex = nStart;
		szBuffer[nIndex] = (byte) count;
		nIndex++;
		szBuffer[nIndex] = (byte) action;
		nIndex++;
		szBuffer[nIndex] = (byte) id;
		nIndex++;
		System.arraycopy(Converter.getBytesFromShortLE(x1), 0, szBuffer, nIndex, 2);
		nIndex += 2;
		System.arraycopy(Converter.getBytesFromShortLE(y1), 0, szBuffer, nIndex, 2);
		nIndex += 2;
		System.arraycopy(Converter.getBytesFromShortLE(x2), 0, szBuffer, nIndex, 2);
		nIndex += 2;
		System.arraycopy(Converter.getBytesFromShortLE(y2), 0, szBuffer, nIndex, 2);
	}

	public void save(byte[] szBuffer, int nStart) {
	}

	public void save2(byte[] szBuffer, int nStart, int dstOffset, int dstLen) {
	}

	public int size() {
		return 11;
	}
}
