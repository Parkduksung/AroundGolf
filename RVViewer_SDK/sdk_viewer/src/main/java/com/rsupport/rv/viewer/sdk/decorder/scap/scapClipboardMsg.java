package com.rsupport.rv.viewer.sdk.decorder.scap;


import com.rsupport.rv.viewer.sdk.decorder.Converter;
import com.rsupport.rv.viewer.sdk.decorder.model.IModel;
import com.rsupport.rv.viewer.sdk.decorder.viewer.channelConstants;


public class scapClipboardMsg implements IModel {
	
	public char format;
//	public int length = 0;
	public byte[] data = null;
	
	public scapClipboardMsg() {
		format = channelConstants.Clipboard_Text;
	}
	
	public void push(byte[] szBuffer, int start) {
		int nIndex = start;
		System.arraycopy(Converter.getBytesFromCharLE(format), 0, szBuffer, nIndex, 1);
		nIndex += 1;
		System.arraycopy(data, 0, szBuffer, nIndex, data.length);
	}
	public void save(byte[] szBuffer, int start) {
		int nIndex = start;
		
		format = (char)szBuffer[0];
		nIndex += 1;
		data = new byte[szBuffer.length-nIndex];
		System.arraycopy(szBuffer, nIndex, data, 0, data.length);
	}
	
	public void save2(byte[] szBuffer, int start, int dstOffset, int dstLen) {
	}
	public int size() {
		return data.length + 1;
	}

}
