package com.rsupport.rv.viewer.sdk.decorder.scap;


import com.rsupport.rv.viewer.sdk.decorder.Converter;
import com.rsupport.rv.viewer.sdk.decorder.model.IModel;
import com.rsupport.rv.viewer.sdk.decorder.model.POINTS;

public class scapCursorMsg implements IModel {
	public short type;
	public short ime;
	public POINTS ptCur = new POINTS();
	public short idx;
	
	public int cjBits;
	public short b2t;
	public short iClrBpp;
	
	public short xHot;
	public short yHot;
	public short cx;
	public short cy;
	
	public short iCompression;
	public int iClrUsed;
	public short pvBits;
	
	public static final int sz_scapCursorPosMsg = 6;
	public static final int sz_scapCursorCachedMsg = 1 + sz_scapCursorPosMsg;
	public static final int sz_scapCursorNewMsg = 19 - 1;
	
	public void save(byte[] szBuffer, int nStart) {
		int nIndex = nStart;
		
		type = (short) ((int)szBuffer[nIndex] & 0xff);
		nIndex ++;
		ime = (short) ((int)szBuffer[nIndex] & 0xff);
		nIndex ++;
		//ptDelta 부분
		ptCur.x = Converter.readShortLittleEndian(szBuffer, nIndex);
		nIndex += 2;
		ptCur.y = Converter.readShortLittleEndian(szBuffer, nIndex);
		nIndex += 2;		
		idx = (short) ((int)szBuffer[nIndex] & 0xff);
		nIndex ++;
		
		cjBits =  ( (int)Converter.readShortLittleEndian(szBuffer, nIndex) & 0xffff);
		nIndex += 2;		
		b2t = (short) ((int)szBuffer[nIndex] & 0xff);
		nIndex ++;
		iClrBpp = (short) ((int)szBuffer[nIndex] & 0xff);
		nIndex ++;
		
		xHot = (short) ((int)szBuffer[nIndex] & 0xff);
		nIndex ++;
		yHot = (short) ((int)szBuffer[nIndex] & 0xff);
		nIndex ++;
		cx = (short) ((int)szBuffer[nIndex] & 0xff);
		nIndex ++;
		cy = (short) ((int)szBuffer[nIndex] & 0xff);
		nIndex ++;
		
		iCompression = (short) ((int)szBuffer[nIndex] & 0xff);
		nIndex ++;
		iClrUsed =( (int)Converter.readShortLittleEndian(szBuffer, nIndex) & 0xffff);
		nIndex += 2;
		pvBits = (short) ((int)szBuffer[nIndex] & 0xff);
		nIndex ++;
	}
	
	// szBuffer내용을 읽어서, 객체에 저장시, 객체내에 dstOffset위치 부터, dstLen 크기만큼 저장한다.
	public void save2(byte[] szBuffer, int nStart, int dstOffset, int dstLen) {
		// TODO Auto-generated method stub
		int nIndex = nStart;
		int st = 0;
		
		if(st >= dstOffset) {
			type = (short) ((int)szBuffer[nIndex] & 0xff);
			nIndex ++;
		}
		st ++;
		if(nIndex-nStart >= dstLen)
			return;
		
		if(st >= dstOffset) {
			ime = (short) ((int)szBuffer[nIndex] & 0xff);
			nIndex ++;
		}
		st ++;
		if(nIndex-nStart >= dstLen)
			return;

		//ptDelta 부분
		if(st >= dstOffset) {
			ptCur.x = Converter.readShortLittleEndian(szBuffer, nIndex);
			nIndex += 2;
		
			ptCur.y = Converter.readShortLittleEndian(szBuffer, nIndex);
			nIndex += 2;
		}
		st += ptCur.size();
		if(nIndex-nStart >= dstLen)
			return;
		
		if(st >= dstOffset) {
			idx = (short) ((int)szBuffer[nIndex] & 0xff);
			nIndex ++;
		}
		st ++;
		if(nIndex-nStart >= dstLen)
			return;
		
		if(st >= dstOffset) {
			cjBits =  ( (int)Converter.readShortLittleEndian(szBuffer, nIndex) & 0xffff);
			nIndex += 2;
		}
		st += 2;
		if(nIndex-nStart >= dstLen)
			return;
		
		if(st >= dstOffset) {
			b2t = (short) ((int)szBuffer[nIndex] & 0xff);
			nIndex ++;
		}		
		st ++;
		if(nIndex-nStart >= dstLen)
			return;
		
		if(st >= dstOffset) {
			iClrBpp = (short) ((int)szBuffer[nIndex] & 0xff);
			nIndex ++;
		}
		st ++;
		if(nIndex-nStart >= dstLen)
			return;
		
		if(st >= dstOffset) {
			xHot = (short) ((int)szBuffer[nIndex] & 0xff);
			nIndex ++;
		}
		st ++;
		if(nIndex-nStart >= dstLen)
			return;
		
		if(st >= dstOffset) {
			yHot = (short) ((int)szBuffer[nIndex] & 0xff);
			nIndex ++;
		}
		st ++;
		if(nIndex-nStart >= dstLen)
			return;
		
		if(st >= dstOffset) {
			cx = (short) ((int)szBuffer[nIndex] & 0xff);
			nIndex ++;
		}
		st ++;
		if(nIndex-nStart >= dstLen)
			return;
		
		if(st >= dstOffset) {
			cy = (short) ((int)szBuffer[nIndex] & 0xff);
			nIndex ++;
		}
		st ++;
		if(nIndex-nStart >= dstLen)
			return;
		
		if(st >= dstOffset) {
			iCompression = (short) ((int)szBuffer[nIndex] & 0xff);
			nIndex ++;
		}
		st ++;
		if(nIndex-nStart >= dstLen)
			return;
		
		if(st >= dstOffset) {
			iClrUsed =( (int)Converter.readShortLittleEndian(szBuffer, nIndex) & 0xffff);
			nIndex += 2;
		}
		st += 2;
		if(nIndex-nStart >= dstLen)
			return;
		
		if(st >= dstOffset) {
			pvBits = (short) ((int)szBuffer[nIndex] & 0xff);
			nIndex ++;
		}
		st ++;
		if(nIndex-nStart >= dstLen)
			return;
	}
	
	public void push(byte[] szBuffer, int start) {
	}
	
	public int size() {
		return 15+ptCur.size();
	}

	

}
