package com.rsupport.rv.viewer.sdk.decorder.viewer.channel;


import com.rsupport.rv.viewer.sdk.common.log.RLog;
import com.rsupport.rv.viewer.sdk.decorder.model.IModel;

public class rcpSettingInfoPacket implements IModel {

	public int chatType; 			// 1 bit : 키보드 사용 여부 : 0 - Web Chat, 1 - Text Chat
	public int viewerOS; 			// 2 bit : 뷰어가 맥인지 여부 : 0 - Window, 1 - Mac OS X, 2 - Linux, 3 - Reserved
	public int autoCtrlRequest; 	// 1 bit : 키보드/마우스 자동 요청 : 0 - Deactive, 1 - Activate
	public int disablesasMode; 		// 1 bit : SAS 모드 사용 유무 :  0 - SAS Activate, 1 - SAS Deactive // 0이 SAS Activate 가 맞음.
	public int inputType; 			// 2 bit : 제어방식 선택 : 0 - RC5, 1 - RC4, 2 - WINEVENT
	public int screenEngineType; 	// 2 bit : 캡처 엔진 : 0 - SCAP, 1 - HX, 2 - reserved1, 3 - reserved2
	public int reserved; 			// 55 bit : 
	
	public void push(byte[] szBuffer, int nStart) {
		byte b = 0;
		b |= (byte)(screenEngineType << 7);
		b |= (byte)(inputType << 6);
		b |= (byte)(disablesasMode << 4);
		b |= (byte)(autoCtrlRequest << 3);
		b |= (byte)(viewerOS << 1);
		b |= (byte)(chatType);
		
		int i = (int)(b & 0xff);
		
		RLog.d("-------------------- rcpSettingInfoPacket setting val : " + Integer.toBinaryString(i));
		
		szBuffer[nStart] = b;
	}
	
	public void save(byte[] szBuffer, int nStart) {
	}

	public void save2(byte[] szBuffer, int start, int dstOffset, int dstLen) {
	}

	public int size() {
		return 8;
	}
}
