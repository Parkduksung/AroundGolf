package com.rsupport.rv.viewer.sdk.decorder.model;


import com.rsupport.rv.viewer.sdk.decorder.Converter;

public class RVERSIONINFO {
	
	public long major;
	public long minor;
	public long build;
	public long privateBuild;
	
	public void push(byte[] bytes, int index) {
		System.arraycopy(Converter.getBytesFromLongLE(major), 0, bytes, index, 4);
		index += 4;
		System.arraycopy(Converter.getBytesFromLongLE(minor), 0, bytes, index, 4);
		index += 4;
		System.arraycopy(Converter.getBytesFromLongLE(build), 0, bytes, index, 4);
		index += 4;
		System.arraycopy(Converter.getBytesFromLongLE(privateBuild), 0, bytes, index, 4);
		index += 4;
	}
	
	public void save(byte[] bytes, int index) {
		major = Converter.readLongLittleEndian4B(bytes, index);
		index += 4;
		minor = Converter.readLongLittleEndian4B(bytes, index);
		index += 4;
		build = Converter.readLongLittleEndian4B(bytes, index);
		index += 4;
		privateBuild = Converter.readLongLittleEndian4B(bytes, index);
		index += 4;
	}
	
	public static int size() {
		return 16;
	}
}
