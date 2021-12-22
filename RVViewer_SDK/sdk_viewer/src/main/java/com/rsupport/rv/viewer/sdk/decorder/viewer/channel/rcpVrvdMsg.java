package com.rsupport.rv.viewer.sdk.decorder.viewer.channel;


import com.rsupport.rv.viewer.sdk.common.log.RLog;
import com.rsupport.rv.viewer.sdk.decorder.Converter;
import com.rsupport.rv.viewer.sdk.decorder.model.IModel;
import com.rsupport.rv.viewer.sdk.phone2phone.I16Rect;

public class rcpVrvdMsg implements IModel {
	
	public byte id;
	public int dataSize;
//	public int encodeLength;
	public int rectCount;
	public byte[] data;
	public int msgID;
	public I16Rect[] rects;
	public int encode;

	@Override
	public void save(byte[] szBuffer, int nStart) {
		// TODO Auto-generated method stub
		int nIndex = nStart;
		encode = 2;
		// id
		id = szBuffer[nIndex];
		nIndex++;
		
		msgID = (int)(id & 0xff);
		
		// datasize
		dataSize = Converter.readIntLittleEndian(szBuffer, nIndex);
		nIndex += 4;
		
		RLog.d("rcpVrvdMsg data dataSize : " + dataSize);
		
		// encodeLength
//		encodeLength = Converter.readIntLittleEndian(szBuffer, nIndex);
//		nIndex += 4;
//		
//		RLog.d("rcpVrvdMsg data encodeLength : " + encodeLength);
		
		// rectCount Skip 4 
//		nIndex += 4;
//		dataSize -= 4;
		
		// rectCount
		rectCount = Converter.readShortLittleEndian(szBuffer, nIndex);
		nIndex += 2;
//		encodeLength -= 2;
		dataSize -= 2;
		
		RLog.d("rcpVrvdMsg data rectCount : " + rectCount);
		
		// I16Rect
		rects = I16Rect.fromStream(szBuffer, nIndex, rectCount);
//		encodeLength -= rectCount*I16Rect.SIZE;
		nIndex += rectCount* I16Rect.SIZE;
		dataSize -= rectCount* I16Rect.SIZE;
		
		// data
//		if (szBuffer.length >= nIndex + dataSize) {
		data = new byte[dataSize];
		System.arraycopy(szBuffer, nIndex, data, 0, dataSize);
//		}
		
		RLog.d("rcpVrvdMsg data msgID : " + msgID + ", dataSize : " + dataSize);
	}

	@Override
	public void save2(byte[] szBuffer, int nStart, int dstOffset, int dstLen) {
		int nIndex = nStart;
		int st = 0;
		encode = 0;
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
			dataSize = (Converter.readIntLittleEndian(szBuffer, nIndex));
			nIndex += 4;
		}
		st++;
		if (nIndex - nStart >= dstLen)
			return;

		// data
		if (st >= dstOffset) {
			if (szBuffer.length >= nIndex + dataSize) {
				data = new byte[dataSize];
				System.arraycopy(szBuffer, nIndex, data, 0, dataSize);
			}
		}
		
	}

	@Override
	public void push(byte[] szBuffer, int nStart) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return 1 + 4 + dataSize;
	}

}
