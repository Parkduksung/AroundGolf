package com.rsupport.rv.viewer.sdk.decorder.model;


import com.rsupport.rv.viewer.sdk.decorder.Converter;

public class SoundVolInfo {

	public int volume;
	
	public SoundVolInfo() {
	}

	public void push(byte[] szBuffer, int start) {
		int nIndex = start;
		System.arraycopy(Converter.getBytesFromIntLE(volume), 0, szBuffer, nIndex, 2);
		nIndex += 4;
	}
	
	public void save(byte[] szBuffer, int start) {
		int nIndex = start;
		volume = Converter.readIntLittleEndian(szBuffer, nIndex);
		nIndex += 4;
	}
	
	public void save2(byte[] szBuffer, int start, int dstOffset, int dstLen) {
	}
	
	public int size() {
		return 4;
	}

}
