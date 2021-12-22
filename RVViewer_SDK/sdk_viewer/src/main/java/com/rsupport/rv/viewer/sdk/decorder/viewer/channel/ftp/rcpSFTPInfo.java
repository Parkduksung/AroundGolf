package com.rsupport.rv.viewer.sdk.decorder.viewer.channel.ftp;

import com.rsupport.rv.viewer.sdk.decorder.Converter;
import com.rsupport.rv.viewer.sdk.decorder.model.IModel;

public class rcpSFTPInfo implements IModel{
	// 파일 전송 요청을 할 경우 보낼 파일 정보
//	typedef struct _tagSFTPInfo
//	{
//		//RU32				size;
//		DWORD64				size64;	 //대용량 파일 지원
//		RU32				count;
//	} rcpSFTPInfo;
	
	public long size64;
	public int count;
	
	
	// 패킷 만들기
	public void push(byte[] szBuffer, int nStart) {
		int nIndex = nStart;

		System.arraycopy(Converter.getBytesFromLongLE(size64), 0, szBuffer,
			nIndex, 8);
		nIndex += 8;

		System.arraycopy(Converter.getBytesFromIntLE(count), 0, szBuffer,
			nIndex, 4);
		nIndex += 4;
	}

	public void save(byte[] szBuffer, int nStart) {
	}

	public void save2(byte[] szBuffer, int nStart, int dstOffset, int dstLen) {
	}

	public int size() {
		return 12;
	}
}
