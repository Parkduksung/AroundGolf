package com.rsupport.rv.viewer.sdk.decorder.model;


import com.rsupport.rv.viewer.sdk.decorder.Converter;

public class POINT implements IModel {
	public int x = 0;
	public int y = 0;

	public POINT(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public void push(byte[] szBuffer, int nStart) {
		int nIndex = nStart;

		// 패킷 만들기
		// x
		System.arraycopy(Converter.getBytesFromIntLE(x), 0, szBuffer, nIndex,
					4);
		nIndex += 4;

		// y
		System.arraycopy(Converter.getBytesFromIntLE(y), 0, szBuffer, nIndex,
					4);
		nIndex += 4;
	}

	public void save(byte[] szBuffer, int nStart) {
		int nIndex = nStart;
		x = Converter.readIntLittleEndian(szBuffer, nIndex);
		nIndex += 4;

		y = Converter.readIntLittleEndian(szBuffer, nIndex);
		nIndex += 4;
	}

	public int size() {
		return 8;
	}

	public void save2(byte[] szBuffer, int start, int dstOffset, int dstLen) {
		// TODO Auto-generated method stub
		
	}
}
