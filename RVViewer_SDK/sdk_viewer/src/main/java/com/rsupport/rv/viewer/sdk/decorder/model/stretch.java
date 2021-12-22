package com.rsupport.rv.viewer.sdk.decorder.model;


import com.rsupport.rv.viewer.sdk.decorder.Converter;

//server side stretch(method1)
public class stretch  implements IModel{
	public int numerator;
	public int denominator;
	
	public void push(byte[] szBuffer, int nStart) {
		
		int nIndex = nStart;
		System.arraycopy(Converter.getBytesFromShortLE((short) numerator), 0, szBuffer,
			nIndex, 2);
		nIndex += 2;
		System.arraycopy(Converter.getBytesFromShortLE((short) denominator), 0, szBuffer,
			nIndex, 2);
		nIndex += 2;
	}
	
	public void save(byte[] szBuffer, int nStart) {
		
		int nIndex = nStart;
		numerator = ((int) Converter.readShortLittleEndian(szBuffer, nIndex) & 0xffff);
		nIndex += 2;
		denominator = ((int) Converter.readShortLittleEndian(szBuffer, nIndex) & 0xffff);
		nIndex += 2;
	}
	
	public void save2(byte[] szBuffer, int start, int dstOffset, int dstLen) {
		// TODO Auto-generated method stub		
	}
	
	public int size() {
		// TODO Auto-generated method stub
		return 4;
	}
}
