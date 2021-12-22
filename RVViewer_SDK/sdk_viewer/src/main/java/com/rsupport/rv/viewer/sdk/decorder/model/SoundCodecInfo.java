package com.rsupport.rv.viewer.sdk.decorder.model;


import com.rsupport.rv.viewer.sdk.decorder.Converter;

public class SoundCodecInfo implements IModel {
	
	public int sampleRate;
//	public int nChannel = 1; // 1 or 2 // RemotePc는 1로 세팅
	public SoundCodecInfo() {
		sampleRate = 8000;
	}

	public void push(byte[] szBuffer, int start) {
		int nIndex = start;
		System.arraycopy(Converter.getBytesFromIntLE(sampleRate), 0, szBuffer, nIndex, 4);
		nIndex += 4;
	}
	
	public void save(byte[] szBuffer, int start) {
		int nIndex = start;
		sampleRate = Converter.readIntLittleEndian(szBuffer, nIndex);
		nIndex += 4;
	}
	
	public void save2(byte[] szBuffer, int start, int dstOffset, int dstLen) {
	}
	
	public int size() {
			return 4;
			
	}
	
	public void setSampleRate(int sample) {
		sampleRate = sample;
	}

}
