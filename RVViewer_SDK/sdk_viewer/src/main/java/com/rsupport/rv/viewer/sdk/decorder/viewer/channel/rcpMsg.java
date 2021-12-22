package com.rsupport.rv.viewer.sdk.decorder.viewer.channel;

import com.rsupport.rv.viewer.sdk.decorder.Converter;
import com.rsupport.rv.viewer.sdk.decorder.model.IModel;

public class rcpMsg implements IModel {
	// RU8 id; // MsgId
	// RU32 datasize; // data size
	// RU8 data[1]; // data , It must have allocated dynamically.

	public byte id;
	public int datasize;
	public byte[] data;
	public int msgID;

	// public String data;

	// 패킷 만들기
	public void push(byte[] szBuffer, int nStart) {
		int nIndex = nStart;
		szBuffer[nIndex] = id;
		nIndex++;
		System.arraycopy(Converter.getBytesFromIntLE(datasize), 0, szBuffer,
			nIndex, 4);
		nIndex += 4;
		if (data != null) {
			System.arraycopy(data, 0, szBuffer, nIndex, data.length);
		}
	}

	public void save(byte[] szBuffer, int nStart) {
	}

	public void save2(byte[] szBuffer, int nStart, int dstOffset, int dstLen) {
		int nIndex = nStart;
		int st = 0;
		// id
		if (st >= dstOffset) {
			id = szBuffer[nIndex];
			nIndex++;
		}
		
		msgID = (int)(id & 0xff);
		
		st++;
		if (nIndex - nStart >= dstLen)
			return;

		// datasize
		if (st >= dstOffset) {
			datasize = (Converter.readIntLittleEndian(szBuffer, nIndex));
			nIndex += 4;
		}
		st++;
		if (nIndex - nStart >= dstLen)
			return;

		// data
		if (st >= dstOffset) {
			if (szBuffer.length >= nIndex + datasize) {
				data = new byte[datasize];
				System.arraycopy(szBuffer, nIndex, data, 0, datasize);
			}
		}
	}

	public int size() {
		return 5 + (data != null ? data.length : 0);
	}
}
