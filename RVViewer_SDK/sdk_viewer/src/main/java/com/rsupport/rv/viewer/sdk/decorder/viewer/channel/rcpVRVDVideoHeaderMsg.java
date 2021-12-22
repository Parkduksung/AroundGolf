package com.rsupport.rv.viewer.sdk.decorder.viewer.channel;

import com.rsupport.rv.viewer.sdk.decorder.Converter;
import com.rsupport.rv.viewer.sdk.decorder.model.IModel;

public class rcpVRVDVideoHeaderMsg implements IModel {
	public int framewidth = 0;
	public int frameheight = 0;
	public int colorbit = 0;
	public int rotation = 0;
	public int deviceWidth = 0;
	public int deviceHeight = 0;

	
	public void push(byte[] szBuffer, int nIndex) {
		System.arraycopy(Converter.getBytesFromShortLE((short) colorbit), 0, szBuffer, nIndex, 2);
		nIndex += 2;
		System.arraycopy(Converter.getBytesFromShortLE((short) framewidth), 0, szBuffer, nIndex, 2);
		nIndex += 2;
		System.arraycopy(Converter.getBytesFromShortLE((short) frameheight), 0, szBuffer, nIndex, 2);
		nIndex += 2;
		System.arraycopy(Converter.getBytesFromShortLE((short) rotation), 0, szBuffer, nIndex, 2);
		nIndex += 2;
		System.arraycopy(Converter.getBytesFromShortLE((short) deviceWidth), 0, szBuffer, nIndex, 2);
		nIndex += 2;
		System.arraycopy(Converter.getBytesFromShortLE((short) deviceHeight), 0, szBuffer, nIndex, 2);
	}

	public void save(byte[] szBuffer, int nStart) {
		byte[] bFrameWidth = new byte[2];
		byte[] bFrameHeight = new byte[2];
		byte[] bColorBit = new byte[2];
		byte[] bRotation = new byte[2];
		byte[] bDeviceWidth = new byte[2];
		byte[] bDeviceHeight = new byte[2];
		
		System.arraycopy(szBuffer, nStart, bColorBit, 0, 2);
		colorbit = (int) Converter.readShortLittleEndian(bColorBit);
		nStart += 2;
		System.arraycopy(szBuffer, nStart, bFrameWidth, 0, 2);
		framewidth = (int) Converter.readShortLittleEndian(bFrameWidth);
		nStart += 2;
		System.arraycopy(szBuffer, nStart, bFrameHeight, 0, 2);
		frameheight = (int) Converter.readShortLittleEndian(bFrameHeight);
		nStart += 2;
		System.arraycopy(szBuffer, nStart, bRotation, 0, 2);
		rotation = (int) Converter.readShortLittleEndian(bRotation);
		nStart += 2;
		System.arraycopy(szBuffer, nStart, bDeviceWidth, 0, 2);
		deviceWidth = (int) Converter.readShortLittleEndian(bDeviceWidth);
		nStart += 2;
		System.arraycopy(szBuffer, nStart, bDeviceHeight, 0, 2);
		deviceHeight = (int) Converter.readShortLittleEndian(bDeviceHeight);
	}

	public void save2(byte[] szBuffer, int nStart, int dstOffset, int dstLen) {}

	public int size() {
		return 12;
	}
}
