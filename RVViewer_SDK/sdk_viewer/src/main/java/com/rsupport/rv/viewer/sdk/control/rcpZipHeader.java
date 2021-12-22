package com.rsupport.rv.viewer.sdk.control;


import com.rsupport.rv.viewer.sdk.decorder.model.IModel;

public class rcpZipHeader implements IModel {

	public int originalsize;
	public int compresssize;
	
	public void push(byte[] szBuffer, int nStart) {
		int nIndex = nStart;

		System.arraycopy(Converter.getBytesFromIntLE(originalsize), 0, szBuffer, nIndex, 4);
		nIndex += 4;
		System.arraycopy(Converter.getBytesFromIntLE(compresssize), 0, szBuffer, nIndex, 4);
		nIndex += 4;
	}

	public void save(byte[] szBuffer, int nStart) {
		int nIndex = nStart;

		originalsize = Converter.readIntLittleEndian(szBuffer, nIndex);
		nIndex += 4;
		compresssize = Converter.readIntLittleEndian(szBuffer, nIndex);
	}

	public void save2(byte[] szBuffer, int start, int dstOffset, int dstLen) {
	}

	public int size() {
		return 8;
	}

}
