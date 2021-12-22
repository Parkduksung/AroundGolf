package com.rsupport.rv.viewer.sdk.decorder.viewer.channel;


import com.rsupport.rv.viewer.sdk.decorder.Converter;
import com.rsupport.rv.viewer.sdk.decorder.model.IModel;

public class rcpChannelMsg implements IModel {

	// typedef unsigned long RU32, RCOLOR32;
	// typedef unsigned short RU16, RCOLOR16;
	// typedef unsigned char RU8, RCOLOR8;
	//
	// typedef signed char RS8;
	// typedef signed short RS16;
	// typedef signed long RS32;

	// RU8 idchannel; // 채널 id
	// RU32 port; // 채널 port
	// RU8 data[1]; // GUID 또는 그외 데이타

	public byte idchannel;
	public int port;
	public byte[] data;

	public void push(byte[] szBuffer, int nStart) {
		int nIndex = nStart;
		szBuffer[nIndex] = idchannel;
		nIndex++;
		System.arraycopy(Converter.getBytesFromIntLE(port), 0, szBuffer, nIndex, 4);
		nIndex += 4;
		if (data != null) {
			System.arraycopy(data, 0, szBuffer, nIndex, data.length);
		}
	}

	public void save(byte[] szBuffer, int nStart) {
	}
	public void save2(byte[] szBuffer, int start, int dstOffset, int dstLen) {
	}
	
	public int size() {
		return 6;
	}
}
