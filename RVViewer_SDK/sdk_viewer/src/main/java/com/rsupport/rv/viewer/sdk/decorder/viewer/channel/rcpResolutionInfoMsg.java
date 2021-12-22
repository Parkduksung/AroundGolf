package com.rsupport.rv.viewer.sdk.decorder.viewer.channel;


import com.rsupport.rv.viewer.sdk.decorder.Converter;
import com.rsupport.rv.viewer.sdk.decorder.model.IModel;

public class rcpResolutionInfoMsg implements IModel {
	
	public int width = 0;
	public int height = 0;
	public int colorBit = 0;

	
	public void push(byte[] szBuffer, int nIndex) {
		System.arraycopy(Converter.getBytesFromShortLE((short)width), 0, szBuffer, nIndex, 2);
		nIndex += 2;
		System.arraycopy(Converter.getBytesFromShortLE((short)height), 0, szBuffer, nIndex, 2);
		nIndex += 2;
		System.arraycopy(Converter.getBytesFromCharLE((char)colorBit), 0, szBuffer, nIndex, 1);
	}

	public void save(byte[] szBuffer, int nStart) {
		byte[] bWidth = new byte[2];
		byte[] bHeight = new byte[2];
		byte[] bColorBit = new byte[1];
		
		System.arraycopy(szBuffer, nStart, bWidth, 0, 2);
		width = (int)Converter.readShortLittleEndian(bWidth);
		nStart += 2;
		System.arraycopy(szBuffer, nStart, bHeight, 0, 2);
		height = (int)Converter.readShortLittleEndian(bHeight);
		nStart += 2;
		System.arraycopy(szBuffer, nStart, bColorBit, 0, 1);
		colorBit = (int)bColorBit[0];
		nStart += 1;
	}

	public void save2(byte[] szBuffer, int nStart, int dstOffset, int dstLen) {}

	public int size() {
		return 5;
	}

}

//typedef struct _tagResolutionInfoMsg
//{
//	RU16	width; 
//	RU16	height;
//	RU8	colorbit;				// Color Bit : 4(16),8(256),16,24,32
//} rcpResolutionInfoMsg;
