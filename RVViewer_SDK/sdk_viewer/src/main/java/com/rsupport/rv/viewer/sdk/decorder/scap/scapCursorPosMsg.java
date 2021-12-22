package com.rsupport.rv.viewer.sdk.decorder.scap;


import com.rsupport.rv.viewer.sdk.decorder.model.IModel;
import com.rsupport.rv.viewer.sdk.decorder.model.POINTS;

public class scapCursorPosMsg implements IModel {

// scapCursorMsg에서 포지션 관련된 변수정보만 추출
//	U8		type;
//	POINTS	ptCur;	// -------------------- sz_scapCursorPosMsg
//	U8		idx;	// -------------------- sz_scapCursorCachedMsg
	
	public short type;
	public short ime;
	public POINTS ptCur = new POINTS();
	public short idx;
	
	public void push(byte[] szBuffer, int nStart) {
		int nIndex = nStart;
		
		szBuffer[nIndex] = (byte)type;		
		nIndex++;
		szBuffer[nIndex] = (byte)ime;
		nIndex++;
		ptCur.push(szBuffer, nIndex);
		nIndex += ptCur.size();
		szBuffer[nIndex] = (byte)idx;
		nIndex ++;
	}

	public void save(byte[] szBuffer, int nStart) {
		int nIndex = nStart;

		type = (short) ((int) szBuffer[nIndex] & 0xff);
		nIndex++;
		ime = (short) ((int) szBuffer[nIndex] & 0xff);
		nIndex++;
		// ptCur 부분
		ptCur.push(szBuffer, nIndex);
		nIndex += ptCur.size();
		idx = (short) ((int) szBuffer[nIndex] & 0xff);
		nIndex++;
	}
	
	
	// type, ptCur만 저장
	public void savePos(byte[] szBuffer, int nStart) {
		int nIndex = nStart;

		type = (short) ((int) szBuffer[nIndex] & 0xff);
		nIndex++;
		ime = (short) ((int) szBuffer[nIndex] & 0xff);
		nIndex++;
		// ptCur 부분
		ptCur.save(szBuffer, nIndex);
	}
	
	public void save2(byte[] szBuffer, int start, int dstOffset, int dstLen) {
		// TODO Auto-generated method stub
	}

	public int size() {
		// TODO Auto-generated method stub
		return 7;
	}

}
