package com.rsupport.rv.viewer.sdk.decorder.scap;


import com.rsupport.rv.viewer.sdk.decorder.Converter;
import com.rsupport.rv.viewer.sdk.decorder.model.IModel;
import com.rsupport.rv.viewer.sdk.decorder.viewer.channelConstants;

public class scapDrawInfoMsg implements IModel {
	private char drawtype = channelConstants.rcpDrawFreeLine;
	private int color = 0xff0000;
	private char thickness = 3;
	private int realtime = 0;
	
	public void setDrawType(char drawtype) {
		this.drawtype = drawtype;
	}
	
	public void setDrawColor(int color) {
		this.color = color;
	}
	
	public void setDrawThickness(char thickness) {
		this.thickness = thickness;
	}
	
	public void push(byte[] szBuffer, int start) { 
		int nIndex = start;
		
		System.arraycopy(Converter.getBytesFromCharLE(drawtype), 0, szBuffer, nIndex, 1);
		nIndex += 1;
		System.arraycopy(Converter.getBytesFromIntForColor(color), 0, szBuffer, nIndex, 4);
		nIndex += 4;
		System.arraycopy(Converter.getBytesFromCharLE(thickness), 0, szBuffer, nIndex, 1);
		nIndex += 1;
		System.arraycopy(Converter.getBytesFromIntLE(realtime), 0, szBuffer, nIndex, 4);
		nIndex += 4;
	}

	public void save(byte[] szBuffer, int start) {
		int nIndex = start;
		
		drawtype = (char)szBuffer[nIndex];
		nIndex += 1;
		color = Converter.readIntLittleEndian(szBuffer, nIndex);
		nIndex += 4;
		thickness = (char)szBuffer[nIndex];
		nIndex += 1;
		realtime = Converter.readIntLittleEndian(szBuffer, nIndex);
	}

	public void save2(byte[] szBuffer, int start, int dstOffset, int dstLen) {
	}
	public int size() {
		return 10;
	}
}
