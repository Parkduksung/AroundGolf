package com.rsupport.rv.viewer.sdk.decorder.viewer.channel;

import com.rsupport.rv.viewer.sdk.decorder.Converter;
import com.rsupport.rv.viewer.sdk.decorder.model.IModel;

public class rcpX264VideoHeaderMsg implements IModel {
	/**
	 *  Ashm H.264일 경우
	 */
	public static final int ENCODER_TYPE_OMX = 'O';

	public static final int ENCODER_TYPE_KNOX = 'N';

	/**
	 * 5.0 MediaProjection 일 경우
	 */
	public static final int ENCODER_TYPE_OMX_FOR_VD = 'V';
	// 화면 회전시 디코더 재시작할 경우 엔코더도 다시시작 요청하기 위한 프로토콜
	public static final int ENCODER_TYPE_OMX_FOR_RELOAD_VD = 'R';

	public int frameWidth = 0;
	public int frameHeight = 0;
	public int rotation	= 0;
	public int width = 0;
	public int height = 0;
	public int videoRatio = 100;
	public int framepersecond = 20;
	public int videoQuality = 3;
	public int monitorIndex = 1; // 0 : 화면전체, 1 : 1번모니터
	public int valOption1 = 0;
	public int valOption2 = 0;
	public int valOption3 = 0;
	public int modelnameLen = 0;
	public byte[] modelname;
	public int sourceType = ENCODER_TYPE_OMX;
	public int isLandscape = 0;


	public void push(byte[] szBuffer, int nIndex) {
		System.arraycopy(Converter.getBytesFromShortLE((short)framepersecond), 0, szBuffer, nIndex, 1);
		nIndex += 1;
		System.arraycopy(Converter.getBytesFromShortLE((short)width), 0, szBuffer, nIndex, 2);
		nIndex += 2;
		System.arraycopy(Converter.getBytesFromShortLE((short)height), 0, szBuffer, nIndex, 2);
		nIndex += 2;
		System.arraycopy(Converter.getBytesFromShortLE((short)frameWidth), 0, szBuffer, nIndex, 2);
		nIndex += 2;
		System.arraycopy(Converter.getBytesFromShortLE((short)frameHeight), 0, szBuffer, nIndex, 2);
		nIndex += 2;
		System.arraycopy(Converter.getBytesFromShortLE((short)rotation), 0, szBuffer, nIndex, 2);
		nIndex += 2;
		System.arraycopy(Converter.getBytesFromShortLE((short)videoRatio), 0, szBuffer, nIndex, 2);
		nIndex += 2;
		System.arraycopy(Converter.getBytesFromShortLE((short)videoQuality), 0, szBuffer, nIndex, 2);
		nIndex += 2;
		System.arraycopy(Converter.getBytesFromShortLE((short)monitorIndex), 0, szBuffer, nIndex, 2);
		nIndex += 2;
		System.arraycopy(Converter.getBytesFromShortLE((short)valOption1), 0, szBuffer, nIndex, 2);
		nIndex += 2;
		System.arraycopy(Converter.getBytesFromShortLE((short)valOption2), 0, szBuffer, nIndex, 2);
		nIndex += 2;
		System.arraycopy(Converter.getBytesFromShortLE((short)valOption3), 0, szBuffer, nIndex, 2);
		nIndex += 2;
		System.arraycopy(Converter.getBytesFromShortLE((short)modelnameLen), 0, szBuffer, nIndex, 2);
		nIndex += 2;
		System.arraycopy(modelname, 0, szBuffer, nIndex, modelnameLen);
		nIndex += modelnameLen;
		System.arraycopy(Converter.getBytesFromShortLE((short)sourceType), 0, szBuffer, nIndex, 2);
		nIndex += 2;
		System.arraycopy(Converter.getBytesFromShortLE((short)isLandscape), 0, szBuffer, nIndex, 2);
	}

	public void save(byte[] szBuffer, int nStart) {
		byte[] bFrameWidth = new byte[2];
		byte[] bFrameHeight = new byte[2];
		byte[] bRotation = new byte[2];
		byte[] bWidth = new byte[2];
		byte[] bHeight = new byte[2];
		byte[] bVideoRatio = new byte[2];
		byte[] bFramePersecond = new byte[1];
		byte[] bVideoQuality = new byte[2];
		byte[] bMonitorIndex = new byte[2];
		byte[] bValOption1 = new byte[2];
		byte[] bValOption2 = new byte[2];
		byte[] bValOption3 = new byte[2];
		byte[] bModelnameLen = new byte[2];
		byte[] bModelname;
		byte[] bSourceType = new byte[2];
		byte[] bIsLandscape = new byte[2];

		System.arraycopy(szBuffer, nStart, bFramePersecond, 0, 1);
		framepersecond = (int)(szBuffer[nStart] & 0xff);
		nStart += 1;
		System.arraycopy(szBuffer, nStart, bWidth, 0, 2);
		width = (int)Converter.readShortLittleEndian(bWidth);
		nStart += 2;
		System.arraycopy(szBuffer, nStart, bHeight, 0, 2);
		height = (int)Converter.readShortLittleEndian(bHeight);
		nStart += 2;
		System.arraycopy(szBuffer, nStart, bFrameWidth, 0, 2);
		frameWidth = (int)Converter.readShortLittleEndian(bFrameWidth);
		nStart += 2;
		System.arraycopy(szBuffer, nStart, bFrameHeight, 0, 2);
		frameHeight = (int)Converter.readShortLittleEndian(bFrameHeight);
		nStart += 2;
		System.arraycopy(szBuffer, nStart, bRotation, 0, 2);
		rotation = (int)Converter.readShortLittleEndian(bRotation);
		nStart += 2;
		System.arraycopy(szBuffer, nStart, bVideoRatio, 0, 2);
		videoRatio = (int)Converter.readShortLittleEndian(bVideoRatio);
		nStart += 2;
		System.arraycopy(szBuffer, nStart, bVideoQuality, 0, 2);
		videoQuality = (int)Converter.readShortLittleEndian(bVideoQuality);
		nStart += 2;
		System.arraycopy(szBuffer, nStart, bMonitorIndex, 0, 2);
		monitorIndex = (int)Converter.readShortLittleEndian(bMonitorIndex);
		nStart += 2;
		System.arraycopy(szBuffer, nStart, bValOption1, 0, 2);
		valOption1 = (int)Converter.readShortLittleEndian(bValOption1);
		nStart += 2;
		System.arraycopy(szBuffer, nStart, bValOption2, 0, 2);
		valOption2 = (int)Converter.readShortLittleEndian(bValOption2);
		nStart += 2;
		System.arraycopy(szBuffer, nStart, bValOption3, 0, 2);
		valOption3 = (int)Converter.readShortLittleEndian(bValOption3);
		nStart += 2;
		System.arraycopy(szBuffer, nStart, bModelnameLen, 0, 2);
		modelnameLen = (int)Converter.readShortLittleEndian(bModelnameLen);
		nStart += 2;
		bModelname = new byte[modelnameLen];
		System.arraycopy(szBuffer, nStart, bModelname, 0, modelnameLen);
		modelname = bModelname;
		nStart += modelnameLen;
		System.arraycopy(szBuffer, nStart, bSourceType, 0, 2);
		sourceType = (int)Converter.readShortLittleEndian(bSourceType);
		nStart += 2;
		System.arraycopy(szBuffer, nStart, bIsLandscape, 0, 2);
		isLandscape = (int)Converter.readShortLittleEndian(bIsLandscape);
	}

	public void save2(byte[] szBuffer, int nStart, int dstOffset, int dstLen) {}

	public int size() {
		return 25 + modelnameLen + 2 + 2;
	}
}
