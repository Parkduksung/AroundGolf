package com.rsupport.rv.viewer.sdk.decorder.model;

//import com.server.JNonBufferingNet;


import com.rsupport.rv.viewer.sdk.decorder.Converter;

import com.rsupport.rv.viewer.sdk.decorder.viewer.channel.CRCChannel;

public class SCAP_ENCODER_INFORMATION implements IModel{

	public static final int eof_type = 0x01;
	public static final int eof_vbpp = 0x02; 
	public static final int eof_jpgq = 0x04;
	public static final int eof_cache = 0x08; 
	public static final int eof_cpu = 0x10; 
	public static final int eof_stretch = 0x20;
	public static final int eof_sock = 0x40; 
	public static final int eof_all = 0x7F; 
			
	public static final int eof_zipcomp = 0x0100;
	public static final int eof_lzocomp = 0x0200;
	public static final int eof_jpgcomp = 0x0400;
	
	public static final int encoderNull= '?';
	public static final int encoderRaw = 'R';
	public static final int encoderZip = 'Z';
	public static final int encoderHif = 'H'; 
	public static final int encoderJex = 'J';
	
	public long flags;
	public int encType;
	public int encViewerBitsPerPixel;
	public int encHostBitsPerPixel;
	public int encHostBitDepth;
	
	public int encJpgHighQuality;
	public int encJpgLowQuality;
	public int encSendTimesWithoutAck;
	public int encMaxTileCache;
	public int encLazyUpdate;
	
	public SCAP_STRETCH_INFORMATION	stretch = new SCAP_STRETCH_INFORMATION();
	

	public CRCChannel encClientChannel;
	
	public void save(byte[] szBuffer, int nStart) {
		
		int nIndex = nStart;
		flags = Converter.readIntLittleEndian(szBuffer, nIndex) & 0xffffffffL;
		nIndex += 4;
		
		encType = Converter.readIntLittleEndian(szBuffer, nIndex);
		nIndex += 4;
		encViewerBitsPerPixel = Converter.readIntLittleEndian(szBuffer, nIndex);
		nIndex += 4;
		encHostBitsPerPixel = Converter.readIntLittleEndian(szBuffer, nIndex);
		nIndex += 4;
		encHostBitDepth = Converter.readIntLittleEndian(szBuffer, nIndex);
		nIndex += 4;
		encJpgHighQuality = Converter.readIntLittleEndian(szBuffer, nIndex);
		nIndex += 4;
		encJpgLowQuality = Converter.readIntLittleEndian(szBuffer, nIndex);
		nIndex += 4;
		encSendTimesWithoutAck = Converter.readIntLittleEndian(szBuffer, nIndex);
		nIndex += 4;
		encMaxTileCache = Converter.readIntLittleEndian(szBuffer, nIndex);
		nIndex += 4;
		encLazyUpdate = Converter.readIntLittleEndian(szBuffer, nIndex);
		nIndex += 4;		

		stretch.save(szBuffer, nIndex);
	}
 
	public int size() {
		return 40 + stretch.size() + 4;
	}

	public void push(byte[] szBuffer, int start) {
		
		int nIndex = start;
		System.arraycopy(Converter.getBytesFromIntLE((int)flags), 0, szBuffer, nIndex, 4);
		nIndex += 4;
		
		System.arraycopy(Converter.getBytesFromIntLE(encType), 0, szBuffer, nIndex, 4);
		nIndex += 4;
		System.arraycopy(Converter.getBytesFromIntLE(encViewerBitsPerPixel), 0, szBuffer, nIndex, 4);
		nIndex += 4;
		System.arraycopy(Converter.getBytesFromIntLE(encHostBitsPerPixel), 0, szBuffer, nIndex, 4);
		nIndex += 4;
		System.arraycopy(Converter.getBytesFromIntLE(encHostBitDepth), 0, szBuffer, nIndex, 4);
		nIndex += 4;
		System.arraycopy(Converter.getBytesFromIntLE(encJpgHighQuality), 0, szBuffer, nIndex, 4);
		nIndex += 4;
		System.arraycopy(Converter.getBytesFromIntLE(encJpgLowQuality), 0, szBuffer, nIndex, 4);
		nIndex += 4;
		System.arraycopy(Converter.getBytesFromIntLE(encSendTimesWithoutAck), 0, szBuffer, nIndex, 4);
		nIndex += 4;
		System.arraycopy(Converter.getBytesFromIntLE(encMaxTileCache), 0, szBuffer, nIndex, 4);
		nIndex += 4;
		System.arraycopy(Converter.getBytesFromIntLE(encLazyUpdate), 0, szBuffer, nIndex, 4);
		nIndex += 4;
		
		stretch.push(szBuffer, nIndex);
		nIndex += stretch.size();
//
//		System.arraycopy(Converter.getBytesFromLongLE(10101010), 0, szBuffer, nIndex, 4);
//		nIndex += 4;
	}

	public void save2(byte[] szBuffer, int start, int dstOffset, int dstLen) {}

}
