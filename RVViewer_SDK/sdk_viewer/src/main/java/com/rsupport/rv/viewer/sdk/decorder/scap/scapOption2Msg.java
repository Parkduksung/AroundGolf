package com.rsupport.rv.viewer.sdk.decorder.scap;



import com.rsupport.rv.viewer.sdk.decorder.Converter;
import com.rsupport.rv.viewer.sdk.decorder.model.IModel;

public class scapOption2Msg implements IModel{
//	U8				type;
//	U8				subtype;
//
//#	define			SCAPOPT_DESKTOP				(0x0001)
//#	define				SCAPOPT_DESKTOP_TYPE	(0x0100)	// hooking type
//#	define				SCAPOPT_DESKTOP_STCH	(0x0200)	// stretch mode
//#	define			SCAPOPT_ENCODER				(0x0002)
//#	define				SCAPOPT_ENCODER_BPP		(0x0100)
//#	define				SCAPOPT_ENCODER_TYPE	(0x0200)
//#	define			SCAPOPT_UPDATE				(0x0004)
//	U16				flags;
//
//	scapOptDeskMsg		hook;
//	scapOptEncMsg		encoder;

	public short type;
	public short subtype;

	public static final int SCAPOPT_DESKTOP = (0x0001);
	public static final int SCAPOPT_DESKTOP_TYPE = (0x0100); // hooking type
	public static final int SCAPOPT_DESKTOP_STCH = (0x0200); // stretch mode
	public static final int SCAPOPT_ENCODER = (0x0002);
	public static final int SCAPOPT_ENCODER_BPP = (0x0100);
	public static final int SCAPOPT_ENCODER_TYPE = (0x0200);
	public static final int SCAPOPT_UPDATE = (0x0004);
	
	public int flags;
	public scapOptDeskMsg hook = new scapOptDeskMsg();
	public scapOptEncMsg encoder = new scapOptEncMsg();
	
	public void save(byte[] szBuffer, int nStart) {
		int nIndex = nStart;

		type = (short) ((int) szBuffer[nIndex] & 0xff);
		nIndex++;
		subtype = (short) ((int) szBuffer[nIndex] & 0xff);
		nIndex++;

		flags = ((int) Converter.readShortLittleEndian(szBuffer, nIndex) & 0xffff);
		nIndex += 2;
		
		hook.save(szBuffer, nIndex);
		nIndex += hook.size();
		
		encoder.save(szBuffer, nIndex);
		nIndex += encoder.size();
	}
	
	public void push(byte[] szBuffer, int nStart) {
		int nIndex = nStart;

		// 패킷 만들기
		szBuffer[nIndex] = (byte)type;
		nIndex ++;
		szBuffer[nIndex] = (byte)subtype;
		nIndex ++;
		System.arraycopy(Converter.getBytesFromShortLE((short) flags), 0, szBuffer,
			nIndex, 2);
		nIndex += 2;
		hook.push(szBuffer, nIndex);
		nIndex += hook.size();
		encoder.push(szBuffer, nIndex);
		nIndex += encoder.size();		
	}
	
	public int size() {
		// return 0;
		return 4 + hook.size() + encoder.size();
	}

	public void save2(byte[] szBuffer, int start, int dstOffset, int dstLen) {
		// TODO Auto-generated method stub
		
	}	
}
