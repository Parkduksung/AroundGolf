package com.rsupport.rv.viewer.sdk.decorder.model;


import com.rsupport.rv.viewer.sdk.common.log.RLog;
import com.rsupport.rv.viewer.sdk.decorder.Converter;




public class HXENGINEHEADER implements IModel {
	
	public int videoWidth = 1920;
	public int videoHeight = 1080;
	public int videoSize = 100;
	public int framepersecond = 20;
	public int videoQuality = 9;
	public int audioQUality = 0;
	public int audioChannel = 0;
	public int samplingRate = 44100;
	public int monitorIndex = 1; // 0 : 화면전체, 1 : 1번모니터
	
	public static int SIZE = 18;

	@Override
	public void save(byte[] szBuffer, int nStart) {
		int nIndex = nStart;
		
		videoWidth = Converter.readShortLittleEndian(szBuffer, nIndex);
		nIndex += 2;
		videoHeight = Converter.readShortLittleEndian(szBuffer, nIndex);
		nIndex += 2;
		videoSize = Converter.readShortLittleEndian(szBuffer, nIndex);
		nIndex += 2;
		framepersecond = Converter.readShortLittleEndian(szBuffer, nIndex);
		nIndex += 2;
		videoQuality = Converter.readShortLittleEndian(szBuffer, nIndex);
		nIndex += 2;
		audioQUality = Converter.readShortLittleEndian(szBuffer, nIndex);
		nIndex += 2;
		audioChannel = Converter.readShortLittleEndian(szBuffer, nIndex);
		nIndex += 2;
		samplingRate = Converter.readShortLittleEndian(szBuffer, nIndex);
		nIndex += 2;
		monitorIndex = Converter.readShortLittleEndian(szBuffer, nIndex);
		nIndex += 2;
	}

	@Override
	public void save2(byte[] szBuffer, int nStart, int dstOffset, int dstLen) {
	}

	@Override
	public void push(byte[] szBuffer, int nStart) {
		int nIndex = nStart;

		System.arraycopy(Converter.getBytesFromShortLE((short)videoWidth), 0, szBuffer, nIndex, 2);
		nIndex += 2;
		System.arraycopy(Converter.getBytesFromLongLE((short)videoHeight), 0, szBuffer, nIndex, 2);
		nIndex += 2;
		System.arraycopy(Converter.getBytesFromLongLE((short)videoSize), 0, szBuffer, nIndex, 2);
		nIndex += 2;
		System.arraycopy(Converter.getBytesFromLongLE((short)framepersecond), 0, szBuffer, nIndex, 2);
		nIndex += 2;
		System.arraycopy(Converter.getBytesFromShortLE((short)videoQuality), 0, szBuffer, nIndex, 2);
		nIndex += 2;
		System.arraycopy(Converter.getBytesFromLongLE((short)audioQUality), 0, szBuffer, nIndex, 2);
		nIndex += 2;
		System.arraycopy(Converter.getBytesFromLongLE((short)audioChannel), 0, szBuffer, nIndex, 2);
		nIndex += 2;
		System.arraycopy(Converter.getBytesFromLongLE((short)samplingRate), 0, szBuffer, nIndex, 2);
		nIndex += 2;
		System.arraycopy(Converter.getBytesFromLongLE((short)monitorIndex), 0, szBuffer, nIndex, 2);
		nIndex += 2;
	}

	@Override
	public int size() {
		return 18;
	}
	
	public void print() {
		RLog.d("HXHEADER SEND -----------------------");
		RLog.d("HXHEADER SEND videoWidth : " + videoWidth);
		RLog.d("HXHEADER SEND videoHeight : " + videoHeight);
		RLog.d("HXHEADER SEND videoSize : " + videoSize);
		RLog.d("HXHEADER SEND framepersecond : " + framepersecond);
		RLog.d("HXHEADER SEND videoQuality : " + videoQuality);
		RLog.d("HXHEADER SEND audioQUality : " + audioQUality);
		RLog.d("HXHEADER SEND samplingRate : " + samplingRate);
		RLog.d("HXHEADER SEND monitorIndex : " + monitorIndex);
	}
}
