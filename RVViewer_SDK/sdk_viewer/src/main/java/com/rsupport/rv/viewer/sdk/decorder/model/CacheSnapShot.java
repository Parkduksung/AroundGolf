package com.rsupport.rv.viewer.sdk.decorder.model;


import com.rsupport.rv.viewer.sdk.decorder.Converter;

public class CacheSnapShot {
	
	public int maxTileCount = 0;
	public int bytePerPixel = 0;
	public byte[] data;
	
	public static int LENGTH = 8;
	
	public byte[] getData() {
		return data;
	}
	
	public void setDataLen(int len) {
		data = new byte[len];
	}
	
	public void save(byte[] szBuffer, int index) {
		maxTileCount = Converter.readIntLittleEndian(szBuffer, index);
		index += 4;
		bytePerPixel = Converter.readIntLittleEndian(szBuffer, index);
		index += 4;
	}

	public void push(byte[] szBuffer, int index) {	
		System.arraycopy(Converter.getBytesFromIntLE(maxTileCount), 0, szBuffer, index, 4);
		index += 4;
		System.arraycopy(Converter.getBytesFromIntLE(bytePerPixel), 0, szBuffer, index, 4);
		index += 4;
		System.arraycopy(data, 0, szBuffer, index, data.length);
	}
	

}
