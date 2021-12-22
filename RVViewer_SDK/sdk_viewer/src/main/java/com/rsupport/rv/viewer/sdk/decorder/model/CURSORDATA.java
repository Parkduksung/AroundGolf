package com.rsupport.rv.viewer.sdk.decorder.model;


import com.rsupport.rv.viewer.sdk.decorder.Converter;

public class CURSORDATA {
	
	public int cjBits;
	public short index;
	public short iClrBpp;
	
	public short xHot;
	public short yHot;
	public short cx;
	public short cy;
	
	public int iClrUsed;
	public short b2t;
	public short iCompression;
	
	byte[] pvBits;
	
	public static int LENGTH = 12;
	
	public byte[] getBits() {
		return pvBits;
	}
	
	public void setDataBuffer(int len) {
		pvBits = new byte[len];
	}
	
	public void save(byte[] szBuffer, int nIndex) {
		cjBits = ((int) Converter.readShortLittleEndian(szBuffer, nIndex) & 0xffff);
		nIndex += 2;
		index = (short)((int)szBuffer[nIndex] & 0xff);
		nIndex++;
		iClrBpp = (short)((int)szBuffer[nIndex] & 0xff);
		nIndex++;
		xHot = (short)((int)szBuffer[nIndex] & 0xff);
		nIndex++;
		yHot = (short)((int)szBuffer[nIndex] & 0xff);
		nIndex++;
		cx = (short)((int)szBuffer[nIndex] & 0xff);
		nIndex++;
		cy = (short)((int)szBuffer[nIndex] & 0xff);
		nIndex++;
		iClrUsed = ((int)Converter.readShortLittleEndian(szBuffer, nIndex) & 0xffff);
		nIndex += 2;
		b2t = (short)((int)szBuffer[nIndex] & 0xff);
		nIndex++;
		iCompression = (short)((int)szBuffer[nIndex] & 0xff);
		nIndex++;
	}
	
	public void push(byte[] szBuffer, int nIndex) {
		System.arraycopy(Converter.getBytesFromShortLE((short)cjBits), 0, szBuffer, nIndex, 2);
		nIndex += 2;
		szBuffer[nIndex] = (byte)index;
		nIndex++;
		szBuffer[nIndex] = (byte)iClrBpp;
		nIndex++;
		szBuffer[nIndex] = (byte)xHot;
		nIndex++;
		szBuffer[nIndex] = (byte)yHot;
		nIndex++;
		szBuffer[nIndex] = (byte)cx;
		nIndex++;
		szBuffer[nIndex] = (byte)cy;
		nIndex++;
		System.arraycopy(Converter.getBytesFromShortLE((short)iClrUsed), 0, szBuffer, nIndex, 2);
		nIndex += 2;
		szBuffer[nIndex] = (byte)b2t;
		nIndex++;
		szBuffer[nIndex] = (byte)iCompression;
		nIndex++;
	}
	
	public void save2(byte[] szBuffer, int nStart, int dstOffset, int dstLen) {
		int nIndex = nStart;
		int st = 0;
		
		if(st >= dstOffset) {
			cjBits = ((int)Converter.readShortLittleEndian(szBuffer, nIndex) & 0xffff);
			nIndex += 2;
		}
		st += 2;
		if(nIndex-nStart >= dstLen) return;
		
		if(st >= dstOffset) {
			index = (short)((int)szBuffer[nIndex] & 0xff);
			nIndex ++;
		}
		st ++;
		if(nIndex-nStart >= dstLen) return;

		if(st >= dstOffset) {
			iClrBpp = (short)((int)szBuffer[nIndex] & 0xff);
			nIndex ++;
		}
		st ++;
		if(nIndex-nStart >= dstLen) return;

		if(st >= dstOffset) {
			xHot = (short)((int)szBuffer[nIndex] & 0xff);
			nIndex ++;
		}
		st ++;
		if(nIndex-nStart >= dstLen) return;

		if(st >= dstOffset) {
			yHot = (short)((int)szBuffer[nIndex] & 0xff);
			nIndex ++;
		}
		st ++;
		if(nIndex-nStart >= dstLen) return;

		if(st >= dstOffset) {
			cx = (short)((int)szBuffer[nIndex] & 0xff);
			nIndex ++;
		}
		st ++;
		if(nIndex-nStart >= dstLen) return;

		if(st >= dstOffset) {
			cy = (short)((int)szBuffer[nIndex] & 0xff);
			nIndex ++;
		}
		st ++;
		if(nIndex-nStart >= dstLen) return;
		
		if(st >= dstOffset) {
			iClrUsed = ((int)Converter.readShortLittleEndian(szBuffer, nIndex) & 0xffff);
			nIndex += 2;
		}
		st += 2;
		if(nIndex-nStart >= dstLen) return;

		if(st >= dstOffset) {
			b2t = (short)((int)szBuffer[nIndex] & 0xff);
			nIndex ++;
		}
		st ++;
		if(nIndex-nStart >= dstLen) return;

		if(st >= dstOffset) {
			iCompression = (short)((int)szBuffer[nIndex] & 0xff);
			nIndex ++;
		}
		st ++;
		if(nIndex-nStart >= dstLen) return;
	}
	
}
