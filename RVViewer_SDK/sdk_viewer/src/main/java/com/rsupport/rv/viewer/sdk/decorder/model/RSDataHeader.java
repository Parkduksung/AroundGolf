package com.rsupport.rv.viewer.sdk.decorder.model;


import com.rsupport.rv.viewer.sdk.decorder.Converter;

public class RSDataHeader {
	
	public long dataIndex;
	public long tickCount;
	public long dataSize;
	
	public void push(byte[] bytes, int index) {
		System.arraycopy(Converter.getBytesFromLongLE(dataIndex), 0, bytes, index, 8);
		index += 8;
		System.arraycopy(Converter.getBytesFromLongLE(tickCount), 0, bytes, index, 4);
		index += 4;
		System.arraycopy(Converter.getBytesFromLongLE(dataSize), 0, bytes, index, 8);
		index += 8;
	}
	
	public void save(byte[] bytes, int index) {
		dataIndex = Converter.readLongLittleEndian(bytes, index);
		index += 8;
		tickCount = Converter.readLongLittleEndian4B(bytes, index);
		index += 4;
		dataSize = Converter.readLongLittleEndian(bytes, index);
		index += 8;
		
//		RLog.d("RSDataHeader dataIndex : " + dataIndex);
//		RLog.d("RSDataHeader tickCount : " + tickCount);
//		RLog.d("RSDataHeader dataSize : " + dataSize);
	}
	
	public int size() {
		return 20;
	}
	
	public static final int LENGTH = 20;

}
