package com.rsupport.rv.viewer.sdk.decorder.model;


import com.rsupport.rv.viewer.sdk.decorder.Converter;

public class XVIDEOHEADER implements IModel {
	
	public int frameWidth		= 0;
	public int frameHeight		= 0;
	public int rotation			= 0;
	public int videoWidth 		= 0;
	public int videoHeight 		= 0;
	public int videoRatio 		= 100;
	public int framepersecond 	= 20;
	public int videoQuality 	= 10;
	public int monitorIndex 	= 1; // 0 : 화면전체, 1 : 1번모니터
	public int valOption1 		= 0;
	public int valOption2 		= 0;
	public int valOption3 		= 0;
	
	public static int SIZE = 24;

	@Override
	public void save(byte[] szBuffer, int nStart) {
		int nIndex = nStart;
		
		frameWidth = Converter.readShortLittleEndian(szBuffer, nIndex);
		nIndex += 2;
		frameHeight = Converter.readShortLittleEndian(szBuffer, nIndex);
		nIndex += 2;
		rotation = Converter.readShortLittleEndian(szBuffer, nIndex);
		nIndex += 2;
		videoWidth = Converter.readShortLittleEndian(szBuffer, nIndex);
		nIndex += 2;
		videoHeight = Converter.readShortLittleEndian(szBuffer, nIndex);
		nIndex += 2;
		videoRatio = Converter.readShortLittleEndian(szBuffer, nIndex);
		nIndex += 2;
		framepersecond = Converter.readShortLittleEndian(szBuffer, nIndex);
		nIndex += 2;
		videoQuality = Converter.readShortLittleEndian(szBuffer, nIndex);
		nIndex += 2;
		monitorIndex = Converter.readShortLittleEndian(szBuffer, nIndex);
		nIndex += 2;
		valOption1 = Converter.readShortLittleEndian(szBuffer, nIndex);
		nIndex += 2;
		valOption2 = Converter.readShortLittleEndian(szBuffer, nIndex);
		nIndex += 2;
		valOption3 = Converter.readShortLittleEndian(szBuffer, nIndex);
		nIndex += 2;
	}

	@Override
	public void save2(byte[] szBuffer, int nStart, int dstOffset, int dstLen) {
	}

	@Override
	public void push(byte[] szBuffer, int nStart) {
		int nIndex = nStart;

		System.arraycopy(Converter.getBytesFromShortLE((short)frameWidth), 0, szBuffer, nIndex, 2);
		nIndex += 2;
		System.arraycopy(Converter.getBytesFromLongLE((short)frameHeight), 0, szBuffer, nIndex, 2);
		nIndex += 2;
		System.arraycopy(Converter.getBytesFromLongLE((short)rotation), 0, szBuffer, nIndex, 2);
		nIndex += 2;
		System.arraycopy(Converter.getBytesFromShortLE((short)videoWidth), 0, szBuffer, nIndex, 2);
		nIndex += 2;
		System.arraycopy(Converter.getBytesFromLongLE((short)videoHeight), 0, szBuffer, nIndex, 2);
		nIndex += 2;
		System.arraycopy(Converter.getBytesFromLongLE((short)videoRatio), 0, szBuffer, nIndex, 2);
		nIndex += 2;
		System.arraycopy(Converter.getBytesFromLongLE((short)framepersecond), 0, szBuffer, nIndex, 2);
		nIndex += 2;
		System.arraycopy(Converter.getBytesFromShortLE((short)videoQuality), 0, szBuffer, nIndex, 2);
		nIndex += 2;
		System.arraycopy(Converter.getBytesFromLongLE((short)monitorIndex), 0, szBuffer, nIndex, 2);
		nIndex += 2;
		System.arraycopy(Converter.getBytesFromLongLE((short)valOption1), 0, szBuffer, nIndex, 2);
		nIndex += 2;
		System.arraycopy(Converter.getBytesFromLongLE((short)valOption2), 0, szBuffer, nIndex, 2);
		nIndex += 2;
		System.arraycopy(Converter.getBytesFromLongLE((short)valOption3), 0, szBuffer, nIndex, 2);
		nIndex += 2;
	}

	@Override
	public int size() {
		return 24;
	}
}
