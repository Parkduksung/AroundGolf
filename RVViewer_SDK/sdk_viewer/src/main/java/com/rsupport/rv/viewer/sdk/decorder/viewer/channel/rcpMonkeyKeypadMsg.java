package com.rsupport.rv.viewer.sdk.decorder.viewer.channel;

import com.rsupport.rv.viewer.sdk.decorder.Converter;
import com.rsupport.rv.viewer.sdk.decorder.model.IModel;

public class rcpMonkeyKeypadMsg implements IModel {

	public int count;
	public short action;
	public short keycode;

	public void push(byte[] szBuffer, int nStart) {
		int nIndex = nStart;
		szBuffer[nIndex] = (byte) count;
		nIndex++;
		System.arraycopy(Converter.getBytesFromShortLE(action), 0, szBuffer, nIndex, 2);
		nIndex += 2;
		System.arraycopy(Converter.getBytesFromShortLE(keycode), 0, szBuffer, nIndex, 2);
	}

	public void save(byte[] szBuffer, int nStart) {
	}

	public void save2(byte[] szBuffer, int nStart, int dstOffset, int dstLen) {
	}

	public int size() {
		return 5;
	}
}
