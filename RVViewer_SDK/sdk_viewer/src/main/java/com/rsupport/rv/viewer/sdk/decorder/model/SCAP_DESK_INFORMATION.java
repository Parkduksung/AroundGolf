package com.rsupport.rv.viewer.sdk.decorder.model;


import com.rsupport.rv.viewer.sdk.decorder.Converter;

public class SCAP_DESK_INFORMATION implements IModel {
	
	public static final int DdiHook = 1214538857;
	public static final int PolHook = 1215328108;

	public static final int dof_hookType = 0x00000001;
	public static final int dof_hookMonitor = 0x00000002;
	public static final int dof_gdiHookFlags = 0x00000004;
	public static final int dof_hookTimer = 0x00000008;
	public static final int dof_all = 0x0000000F;
	
	public static final int gdiHookFlash = 0x0001;
	public static final int gdiHookLayeredWnd = 0x0002;
	public static final int gdiHookDxHook = 0x0004;
	public static final int gdiHookAccurPoll = 0x0008;
	public static final int gdiHookSrn2Srn = 0x0010;
	
	public long flags;
	public long hookType;
	public long hookMonitor; 

	public long deskBitsPerPixel;
	public long deskBitDepth;
	public long deskRotate;			

	// int deskBitsPerPixel; 
//	public long gdiHookFlags;
	public RECT rcSrn;
	public long hookTimer;
	
	public void save(byte[] szBuffer, int nStart) {
		int nIndex = nStart;
		flags = Converter.readIntLittleEndian(szBuffer, nIndex) & 0xffffffffL;
		nIndex += 4;
		
		hookType = Converter.readIntLittleEndian(szBuffer, nIndex) & 0xffffffffL;
		nIndex += 4;
		hookMonitor = Converter.readIntLittleEndian(szBuffer, nIndex) & 0xffffffffL;
		nIndex += 4;
		deskBitsPerPixel = Converter.readIntLittleEndian(szBuffer, nIndex) & 0xffffffffL;
		nIndex += 4;
		deskBitDepth = Converter.readIntLittleEndian(szBuffer, nIndex) & 0xffffffffL;
		nIndex += 4;
		deskRotate = Converter.readIntLittleEndian(szBuffer, nIndex) & 0xffffffffL;
		nIndex += 4;
//		gdiHookFlags = Converter.readIntLittleEndian(szBuffer, nIndex) & 0xffffffffL;
//		nIndex += 4;
		
		rcSrn.left = Converter.readIntLittleEndian(szBuffer, nIndex);
		nIndex += 4;
		rcSrn.top = Converter.readIntLittleEndian(szBuffer, nIndex);
		nIndex += 4;
		rcSrn.right = Converter.readIntLittleEndian(szBuffer, nIndex);
		nIndex += 4;
		rcSrn.bottom = Converter.readIntLittleEndian(szBuffer, nIndex);
		nIndex += 4;
		
		hookTimer = Converter.readIntLittleEndian(szBuffer, nIndex) & 0xffffffffL;
		nIndex += 4;
	}

	public void push(byte[] szBuffer, int start) {		
		int nIndex = start;
		System.arraycopy(Converter.getBytesFromIntLE((int)flags), 0, szBuffer, nIndex, 4);
		nIndex += 4;
		System.arraycopy(Converter.getBytesFromIntLE((int)hookType), 0, szBuffer, nIndex, 4);
		nIndex += 4;
		System.arraycopy(Converter.getBytesFromIntLE((int)hookMonitor), 0, szBuffer, nIndex, 4);
		nIndex += 4;
//		System.arraycopy(Converter.get4BytesFromLongLE(hookMonitor), 0, szBuffer, nIndex, 4);
//		nIndex += 4;
		System.arraycopy(Converter.getBytesFromIntLE((int)deskBitsPerPixel), 0, szBuffer, nIndex, 4);
		nIndex += 4;
		System.arraycopy(Converter.getBytesFromIntLE((int)deskBitDepth), 0, szBuffer, nIndex, 4);
		nIndex += 4;
		System.arraycopy(Converter.getBytesFromIntLE((int)deskRotate), 0, szBuffer, nIndex, 4);
		nIndex += 4;
//		System.arraycopy(Converter.getBytesFromIntLE((int)gdiHookFlags), 0, szBuffer, nIndex, 4);
//		nIndex += 4;
		
		System.arraycopy(Converter.getBytesFromIntLE(rcSrn.left), 0, szBuffer, nIndex, 4);
		nIndex += 4;
		System.arraycopy(Converter.getBytesFromIntLE(rcSrn.top), 0, szBuffer, nIndex, 4);
		nIndex += 4;
		System.arraycopy(Converter.getBytesFromIntLE(rcSrn.right), 0, szBuffer, nIndex, 4);
		nIndex += 4;
		System.arraycopy(Converter.getBytesFromIntLE(rcSrn.bottom), 0, szBuffer, nIndex, 4);
		nIndex += 4;
		
		System.arraycopy(Converter.getBytesFromIntLE((int)hookTimer), 0, szBuffer, nIndex, 4);
		nIndex += 4;
	}
	
	public int size() {
		return 44;
	}

	public void save2(byte[] szBuffer, int start, int dstOffset, int dstLen) {
	}

}
