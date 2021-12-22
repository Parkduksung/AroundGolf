package com.rsupport.rv.viewer.sdk.decorder.model;


import com.rsupport.rv.viewer.sdk.decorder.Converter;

public class Snapshot {
	
	public int slotSize = 0;
	public int bCreate = 1;
	public int lru = 0;
	public int saved = 0;
	
	public static int LENGTH = 16;
	
	public void save(byte[] szBuffer, int index) {
		slotSize = Converter.readIntLittleEndian(szBuffer, index);
		index += 4;
		bCreate = Converter.readIntLittleEndian(szBuffer, index);
		index += 4;
		lru = Converter.readIntLittleEndian(szBuffer, index);
		index += 4;
		saved = Converter.readIntLittleEndian(szBuffer, index);
		index += 4;
	}

	public void push(byte[] szBuffer, int index) {
		System.arraycopy(Converter.getBytesFromIntLE(slotSize), 0, szBuffer, index, 4);
		index += 4;
		System.arraycopy(Converter.getBytesFromIntLE(bCreate), 0, szBuffer, index, 4);
		index += 4;
		System.arraycopy(Converter.getBytesFromIntLE(lru), 0, szBuffer, index, 4);
		index += 4;
		System.arraycopy(Converter.getBytesFromIntLE(saved), 0, szBuffer, index, 4);
		index += 4;
	}
}
