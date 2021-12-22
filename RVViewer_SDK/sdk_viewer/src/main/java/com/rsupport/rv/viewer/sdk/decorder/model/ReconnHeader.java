package com.rsupport.rv.viewer.sdk.decorder.model;


import com.rsupport.rv.viewer.sdk.decorder.Converter;

public class ReconnHeader implements IModel {
	
	public int type;
	public static int qCounter;
	public static int number;
	public int len;
	
	public ReconnHeader() {
	}

	public void push(byte[] szBuffer, int start) {
		int nIndex = start;
		System.arraycopy(Converter.getBytesFromIntLE(type), 0, szBuffer, nIndex, 2);
		nIndex += 4;

		System.arraycopy(Converter.getBytesFromIntLE(qCounter), 0, szBuffer, nIndex, 2);
		nIndex += 4;

		System.arraycopy(Converter.getBytesFromIntLE(number), 0, szBuffer, nIndex, 2);
		nIndex += 4;

		System.arraycopy(Converter.getBytesFromIntLE(len), 0, szBuffer,	nIndex, 2);
		nIndex += 4;
	}
	public void save(byte[] szBuffer, int start) {
		int nIndex = start;
		type = Converter.readIntLittleEndian(szBuffer, nIndex);
		nIndex += 4;

		qCounter = Converter.readIntLittleEndian(szBuffer, nIndex);
		nIndex += 4;

		number = Converter.readIntLittleEndian(szBuffer, nIndex);
		nIndex += 4;

		len = Converter.readIntLittleEndian(szBuffer, nIndex);
		nIndex += 4;
	}
	public void save2(byte[] szBuffer, int start, int dstOffset, int dstLen) {
	}
	public int size() {
		return 16;
	}
}
