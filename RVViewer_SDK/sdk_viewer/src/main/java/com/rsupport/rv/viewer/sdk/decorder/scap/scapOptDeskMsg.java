package com.rsupport.rv.viewer.sdk.decorder.scap;

import com.rsupport.rv.viewer.sdk.common.log.RLog;
import com.rsupport.rv.viewer.sdk.decorder.Converter;
import com.rsupport.rv.viewer.sdk.decorder.model.IModel;
import com.rsupport.rv.viewer.sdk.decorder.model.RECT;
import com.rsupport.rv.viewer.sdk.decorder.model.stretch;
import com.rsupport.rv.viewer.sdk.setting.GlobalStatic;


public class scapOptDeskMsg  implements IModel{

//	#	define scapHookNul		('?') // initial value
//	#	define scapHookGdi		('G') // Gdi-Hooking
//	#	define scapHookDdi		('D') // Ddi-Hooking
//	#	define scapHookDgi		('d') // Ddi-detection & Gdi-capturing : not used.
//	#	define scapHookPol		('P') // GotoMyPC Scanning
//	#	define scapHookFilter	('F') // GotoMyPC Scanning
//		U8 		scapHookType;
//
//	#	define scapMonitorVirtual	(0)
//	#	define scapMonitorPrimary	(1)
//	#	define scapMonitorSecondary	(2)
//		U8 		scapHookMonitor;	// 0 : all monitor, 1 : primary, 2 : secondary
//		U8		scapLocalPxlCnt;	// 고객측 BitsperPixel (고객 => 상담원)
//		U8		scapPad;
//
//	#	define scapOptEnableFlash		(0x0001)	// (Gdi hook mode only)
//	#	define scapOptLayeredWnd		(0x0002)	// (Gdi hook mode only)
//	#	define scapOptDxHook			(0x0004)	// HOOK DX, OPENGL(Gdi hook mode)
//	#	define scapOptLazySrn2Srn		(0x0008)	// Scroll(Gdi hook mode), WindowMove(Gdi hook mode) : always used.
//	#	define scapOptAccurPoll			(0x0020)	// for CAD application(Gdi hook mode)
//	#	define scapOptStretchRatio		(0x0040)	// Server side stretch for CE(Mobile 2003 Se)
//	#	define scapOptStretchPixel		(0x0080)	// Server side stretch for CE(Mobile 2003 Se)
//	#	define scapOptStretchEncoder	(0x0400)	// Server side stretch for CE(Mobile 2003 Se)
//	#	define scapOptSrn2Srn			(0x0100)	// hook srn to srn move action.
//	#	define scapOptSrnPos			(0x0200)
//		U32 	scapHookFlags;
//
//
//		U16		scapTriggingTime;	// 다음 전송까지 최소 대기시간. (ms)
//		U16		scapPad2;
//
//			// server side stretch(method1). num/denom
//			struct { 
//				U16		numerator;
//				U16		denominator;
//			}
//			scapXRatio, scapYRatio;
//			
//			// server side stretch(method2) : fixed pixel for WinCE.
//			U32			xFixedWidth; // server side stretch (xFixedWidth/real_width)
//			U32			yFixedHeight;// server side stretch (yFixedHeight/real_height)
//
//		RECT	rcSrn;				// screen coordinate. (Host => View)
	
	public static final char scapHookNul ='?'; // initial value
	public static final char scapHookGdi = 'G';
	public static final char scapHookDdi = 'D';
	public static final char scapHookDgi = 'd';
	public static final char scapHookPol = 'P';
	public static final char scapHookFilter = 'F';
	
	public short scapHookType;
	
	public static final int scapMonitorVirtual	=0;
	public static final int  scapMonitorPrimary	=1;
	public static final int scapMonitorSecondary =2;
	
	public short scapHookMonitor;
	public short scapLocalPxlCnt;
//	public short scapPad;	
	
	public static final int scapRotate0	= 0;
	public static final int scapRotate90 = 1; 
	public static final int scapRotate180 = 2;
	public static final int scapRotate270 = 3;
	public short scapRotate;

	public static final int scapOptEnableFlash		=0x0001;	// (Gdi hook mode only)
	public static final int scapOptLayeredWnd		=0x0002;	// (Gdi hook mode only)
	public static final int scapOptDxHook			=0x0004;	// HOOK DX, OPENGL(Gdi hook mode)
	public static final int scapOptLazySrn2Srn		=0x0008;	// Scroll(Gdi hook mode), WindowMove(Gdi hook mode) : always used.
	public static final int scapOptAccurPoll		=0x0020;	// for CAD application(Gdi hook mode)
	public static final int scapOptStretchRatio		=0x0040;	// Server side stretch for CE(Mobile 2003 Se)
	public static final int scapOptStretchPixel		=0x0080;	// Server side stretch for CE(Mobile 2003 Se)
	public static final int scapOptStretchEncoder	=0x0400;	// Server side stretch for CE(Mobile 2003 Se)
	public static final int scapOptSrn2Srn			=0x0100;	// hook srn to srn move action.
	public static final int scapOptSrnPos			=0x0200;
	
	public long scapHookFlags;
	
	public int scapTriggingTime;
	public int scapPad2;
	
	public stretch scapXRatio = new stretch();
	public stretch scapYRatio = new stretch();
		
	public long xFixedWidth;
	public long yFixedHeight;
	
	public RECT rcSrn = new RECT();

	public void save(byte[] szBuffer, int nStart) {
		int nIndex = nStart;

		scapHookType = (short) ((int) szBuffer[nIndex] & 0xff);
		nIndex++;
		scapHookMonitor = (short) ((int) szBuffer[nIndex] & 0xff);
		nIndex++;
		scapLocalPxlCnt = (short) ((int) szBuffer[nIndex] & 0xff);
		nIndex++;
//		scapPad = (short) ((int) szBuffer[nIndex] & 0xff);
//		nIndex++;
		scapRotate = (short)(szBuffer[nIndex] & 0xff);
		nIndex++;

		scapHookFlags = Converter.readIntLittleEndian(szBuffer, nIndex) & 0xffffffffL;
		nIndex += 4;
		scapTriggingTime = ((int) Converter.readShortLittleEndian(szBuffer,	nIndex) & 0xffff);
		nIndex += 2;
		scapPad2 = ((int) Converter.readShortLittleEndian(szBuffer,	nIndex) & 0xffff);
		nIndex += 2;		
		
		// scapXRatio
		scapXRatio.numerator = ((int) Converter.readShortLittleEndian(szBuffer,	nIndex) & 0xffff);
		nIndex += 2;
		scapXRatio.denominator = ((int) Converter.readShortLittleEndian(szBuffer, nIndex) & 0xffff);
		nIndex += 2;
		
		// scapYRatio
		scapYRatio.numerator = ((int) Converter.readShortLittleEndian(szBuffer,	nIndex) & 0xffff);
		nIndex += 2;
		scapYRatio.denominator = ((int) Converter.readShortLittleEndian(szBuffer, nIndex) & 0xffff);
		nIndex += 2;
		
		xFixedWidth = Converter.readIntLittleEndian(szBuffer, nIndex) & 0xffffffffL;
		nIndex += 4;
		yFixedHeight = Converter.readIntLittleEndian(szBuffer, nIndex) & 0xffffffffL;
		nIndex += 4;
		
		// rcSrn
		rcSrn.left =  Converter.readIntLittleEndian(szBuffer, nIndex);
		nIndex += 4;
		rcSrn.top =  Converter.readIntLittleEndian(szBuffer, nIndex);
		nIndex += 4;
		rcSrn.right =  Converter.readIntLittleEndian(szBuffer, nIndex);
		nIndex += 4;
		rcSrn.bottom =  Converter.readIntLittleEndian(szBuffer, nIndex);
		nIndex += 4;
		
		GlobalStatic.ORIGIN_SCREEN_X = (short)(rcSrn.right-rcSrn.left);
		GlobalStatic.ORIGIN_SCREEN_Y = (short)rcSrn.bottom;
		// khkim : bit값 셋팅 - 해당값이 정확한 bit값인지 설명이 없음. 아닐 경우, 최승훈 대리님께 문의 or default bit 값으로.
		GlobalStatic.ORIGIN_SCREEN_BIT = (short) scapLocalPxlCnt;
		RLog.d("scapOptDesk : GlobalStatic.ORIGIN_SCREEN_BIT : " + GlobalStatic.ORIGIN_SCREEN_BIT);
		
		if (xFixedWidth == 0 || yFixedHeight == 0) {
			GlobalStatic.g_fixRatio = 0;
			GlobalStatic.g_fixRatioY = 0;
		} else {
			GlobalStatic.g_fixRatio = ((float)xFixedWidth/(float)(rcSrn.right-rcSrn.left)*100);
			GlobalStatic.g_fixRatioY = ((float)yFixedHeight/(float)(rcSrn.bottom-rcSrn.top)*100);
		}
		
		RLog.d("scapHookType=" + scapHookType + ",scapHookMonitor=" +scapHookMonitor +
			",scapLocalPxlCnt=" + scapLocalPxlCnt + ",scapHookFlags=" + scapHookFlags +
			",scapTriggingTime=" + scapTriggingTime + ",scapPad2=" + scapPad2 + ", scapXRatio.numerator=" + scapXRatio.numerator +
			",scapYRatio.denominator=" + scapYRatio.denominator + ",xFixedWidth=" + xFixedWidth + ",yFixedHeight" + yFixedHeight +
			",rcSrn.left=" + rcSrn.left + ",rcSrn.top=" + rcSrn.top + ",rcSrn.right=" + rcSrn.right + ",rcSrn.bottom=" + rcSrn.bottom);
	}

	public void push(byte[] szBuffer, int nStart) {  
		int nIndex = nStart;

		// 패킷 만들기
		szBuffer[nIndex] = (byte)scapHookType;
		nIndex ++;
		szBuffer[nIndex] = (byte)scapHookMonitor;
		nIndex ++;
		szBuffer[nIndex] = (byte)scapLocalPxlCnt;
		nIndex ++;
//		szBuffer[nIndex] = (byte)scapPad;
//		nIndex ++;
		szBuffer[nIndex] = (byte)scapRotate;
		nIndex ++;
		
		System.arraycopy(Converter.getBytesFromIntLE((int) scapHookFlags), 0, szBuffer, nIndex, 4);
		nIndex += 4;
		System.arraycopy(Converter.getBytesFromShortLE((short) scapTriggingTime), 0, szBuffer, nIndex, 2);
		nIndex += 2;
		System.arraycopy(Converter.getBytesFromShortLE((short) scapPad2), 0, szBuffer, nIndex, 2);
		nIndex += 2;
		
		scapXRatio.push(szBuffer, nIndex);
		nIndex += scapXRatio.size();
		scapYRatio.push(szBuffer, nIndex);
		nIndex += scapYRatio.size();
		
		System.arraycopy(Converter.getBytesFromIntLE((int) xFixedWidth), 0, szBuffer, nIndex, 4);
		nIndex += 4;
		System.arraycopy(Converter.getBytesFromIntLE((int) yFixedHeight), 0, szBuffer, nIndex, 4);
		nIndex += 4;
		
		rcSrn.push(szBuffer, nIndex);
		nIndex += rcSrn.size();
	}
	
	public int size() {
		return 44;
	}

	public void save2(byte[] szBuffer, int start, int dstOffset, int dstLen) {
		// TODO Auto-generated method stub
	}

}
