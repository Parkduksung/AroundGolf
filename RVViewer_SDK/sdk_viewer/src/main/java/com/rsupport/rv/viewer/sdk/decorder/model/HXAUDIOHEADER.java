package com.rsupport.rv.viewer.sdk.decorder.model;

import com.rsupport.rv.viewer.sdk.decorder.Converter;




public class HXAUDIOHEADER implements IModel {
	
	public short bandWidth;
	public short bitrate;

	@Override
	public void save(byte[] szBuffer, int nStart) {
		int nIndex = nStart;
		
		bandWidth = Converter.readShortLittleEndian(szBuffer, nIndex);
		nIndex += 2;
		bitrate = Converter.readShortLittleEndian(szBuffer, nIndex);
		nIndex += 2;
	}

	@Override
	public void save2(byte[] szBuffer, int nStart, int dstOffset, int dstLen) {
	}

	@Override
	public void push(byte[] szBuffer, int nStart) {
		int nIndex = nStart;

		System.arraycopy(Converter.getBytesFromShortLE(bandWidth), 0, szBuffer, nIndex, 4);
		nIndex += 2;
		System.arraycopy(Converter.getBytesFromLongLE(bitrate), 0, szBuffer, nIndex, 4);
		nIndex += 2;
	}

	@Override
	public int size() {
		return 17;
	}
}
