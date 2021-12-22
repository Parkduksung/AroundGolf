package com.rsupport.rv.viewer.sdk.decorder.model;


import com.rsupport.rv.viewer.sdk.decorder.Converter;

public class SCAP_STRETCH_INFORMATION implements IModel{
	public SIZE ratioWidth = new SIZE();
	public SIZE ratioHeight = new SIZE();
	
	public long fixedWidth;
	public long fixedHeight;
	
	public SCAP_STRETCH_INFORMATION()
	{
		fixedWidth = 0;
		fixedHeight = 0;
	}
	
	public void save(byte[] szBuffer, int nStart) {
		int nIndex = nStart;
		ratioWidth.cx = Converter.readIntLittleEndian(szBuffer, nIndex);
		nIndex += 4;
		ratioWidth.cy = Converter.readIntLittleEndian(szBuffer, nIndex);
		nIndex += 4;		
		
		ratioHeight.cx = Converter.readIntLittleEndian(szBuffer, nIndex);
		nIndex += 4;
		ratioHeight.cy = Converter.readIntLittleEndian(szBuffer, nIndex);
		nIndex += 4;
		
		fixedWidth = Converter.readIntLittleEndian(szBuffer, nIndex) & 0xffffffffL;
		nIndex += 4;
		fixedHeight = Converter.readIntLittleEndian(szBuffer, nIndex) & 0xffffffffL;
		nIndex += 4;
	}

	public int size() {
		return 24;
	}

	public void push(byte[] szBuffer, int start) {
		int nIndex = start;
		System.arraycopy(Converter.getBytesFromIntLE(ratioWidth.cx), 0, szBuffer, nIndex, 4);
		nIndex += 4;
		System.arraycopy(Converter.getBytesFromIntLE(ratioWidth.cy), 0, szBuffer, nIndex, 4);
		nIndex += 4;
		
		System.arraycopy(Converter.getBytesFromIntLE(ratioHeight.cx), 0, szBuffer, nIndex, 4);
		nIndex += 4;
		System.arraycopy(Converter.getBytesFromIntLE(ratioHeight.cy), 0, szBuffer, nIndex, 4);
		nIndex += 4;
		
		System.arraycopy(Converter.getBytesFromIntLE((int)fixedWidth), 0, szBuffer, nIndex, 4);
		nIndex += 4;
		System.arraycopy(Converter.getBytesFromIntLE((int)fixedHeight), 0, szBuffer, nIndex, 4);
		nIndex += 4;
	}

	public void save2(byte[] szBuffer, int start, int dstOffset, int dstLen) {
	}
}
