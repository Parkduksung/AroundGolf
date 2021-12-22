package com.rsupport.rv.viewer.sdk.decorder.scap;


import com.rsupport.rv.viewer.sdk.common.log.RLog;
import com.rsupport.rv.viewer.sdk.decorder.Converter;
import com.rsupport.rv.viewer.sdk.decorder.model.IModel;
import com.rsupport.rv.viewer.sdk.decorder.model.stretch;

public class scapOptEncMsg implements IModel{

	public long flags;
	public static final char scapEncodingNull = '?';
	public static final char scapEncodingHif = 'H';
	public static final char scapEncodingZip = 'Z'; 
	public static final char scapEncodingJex = 'J';
	public static final char scapEncodingRaw = 'R';
	
	public short scapEncoderType;

	public short ValidFlags; // not used
	public short HostPxlCnt; // desktop pixel count(Host -> Viewer).
	public short scapRemoteBpp; // encoder pixel count(1, 4, 8, 15/16, 24, 32 : Viewer <-> Host) : 고객측으로 요청, 결과가 상담원으로 넘어감.

	public short scapEncJpgLowQuality; // for high freq changed region (0~50%)	: default 30%
	public short scapEncJpgHighQuality; // for low freq changed region (50~80%)  : default 75%
	public short scapEncSendTimesNoAck; // not used.
	public short pad;

	public long scapTileCacheCount; // Graph caching size(BYTE) : 비트맵 캐싱: default : 32MB

	public stretch scapXRatio = new stretch();
	public stretch scapYRatio = new stretch();
	
	public long xFixedWidth; // server side stretch (xFixedWidth/real_width)
	public long yFixedHeight;// server side stretch (yFixedHeight/real_height)
	
	public void save(byte[] szBuffer, int nStart) {
		int nIndex = nStart;

		flags = Converter.readIntLittleEndian(szBuffer, nIndex) & 0xffffffffL;
		nIndex += 4;

		scapEncoderType = (short) ((int) szBuffer[nIndex] & 0xff);
		nIndex++;
		ValidFlags = (short) ((int) szBuffer[nIndex] & 0xff);
		nIndex++;
		HostPxlCnt = (short) ((int) szBuffer[nIndex] & 0xff);
		nIndex++;
		scapRemoteBpp = (short) ((int) szBuffer[nIndex] & 0xff);
		nIndex++;

		scapEncJpgLowQuality = (short) ((int) szBuffer[nIndex] & 0xff);
		nIndex++;
		scapEncJpgHighQuality = (short) ((int) szBuffer[nIndex] & 0xff);
		nIndex++;
		scapEncSendTimesNoAck = (short) ((int) szBuffer[nIndex] & 0xff);
		nIndex++;
		pad = (short) ((int) szBuffer[nIndex] & 0xff);
		nIndex++;
		
		scapTileCacheCount = Converter.readIntLittleEndian(szBuffer, nIndex) & 0xffffffffL;
		nIndex += 4;
		
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
		
		RLog.d("flags=" + flags + ",scapEncoderType=" + scapEncoderType + ",ValidFlags=" + ValidFlags +
			",HostPxlCnt=" + HostPxlCnt + ",scapRemoteBpp=" + scapRemoteBpp + ",scapEncJpgLowQuality=" + scapEncJpgLowQuality +
			",scapEncJpgHighQuality=" + scapEncJpgHighQuality + ",scapEncSendTimesNoAck=" + scapEncSendTimesNoAck + ",pad=" + pad+
			",scapRgnBmpCacheSize=" + scapTileCacheCount + ",scapXRatio.numerator=" + scapXRatio.numerator + ",scapXRatio.denominator=" + 
			scapXRatio.denominator + ",xFixedWidth=" + xFixedWidth + ",yFixedHeight=" + yFixedHeight);
			
	}
	
	public void push(byte[] szBuffer, int nStart) {
		int nIndex = nStart;

		// 패킷 만들기
		System.arraycopy(Converter.getBytesFromIntLE((int) flags), 0, szBuffer,
			nIndex, 4);
		nIndex += 4;
		
		szBuffer[nIndex] = (byte)scapEncoderType;
		nIndex ++;
		szBuffer[nIndex] = (byte)ValidFlags;
		nIndex ++;
		szBuffer[nIndex] = (byte)HostPxlCnt;
		nIndex ++;
		szBuffer[nIndex] = (byte)scapRemoteBpp;
		nIndex ++;
		szBuffer[nIndex] = (byte)scapEncJpgLowQuality;
		nIndex ++;
		szBuffer[nIndex] = (byte)scapEncJpgHighQuality;
		nIndex ++;
		szBuffer[nIndex] = (byte)scapEncSendTimesNoAck;
		nIndex ++;
		szBuffer[nIndex] = (byte)pad;
		nIndex ++;
		
		System.arraycopy(Converter.getBytesFromIntLE((int) scapTileCacheCount), 0, szBuffer,
			nIndex, 4);
		nIndex += 4;
		
		scapXRatio.push(szBuffer, nIndex);
		nIndex += scapXRatio.size();
		scapYRatio.push(szBuffer, nIndex);
		nIndex += scapYRatio.size();
		
		System.arraycopy(Converter.getBytesFromIntLE((int) xFixedWidth), 0, szBuffer, nIndex, 4);
		nIndex += 4;
		System.arraycopy(Converter.getBytesFromIntLE((int) yFixedHeight), 0, szBuffer, nIndex, 4);
		nIndex += 4;
	}
	
	public int size() {		
		return 32;
	}

	public void save2(byte[] szBuffer, int start, int dstOffset, int dstLen) {
		// TODO Auto-generated method stub
		
	}

}
