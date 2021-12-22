package com.rsupport.rv.viewer.sdk.decorder.scap;


import com.rsupport.rv.viewer.sdk.decorder.Converter;
import com.rsupport.rv.viewer.sdk.decorder.model.IModel;
import com.rsupport.rv.viewer.sdk.decorder.viewer.channelConstants;

public class scapClipboardMsgBegin implements IModel {
	public char format;
	public int datasize = 0;
	
	public scapClipboardMsgBegin() {
		format = channelConstants.Clipboard_Text;
	}
	
	public void push(byte[] szBuffer, int start) {
		int nIndex = start;
		System.arraycopy(Converter.getBytesFromCharLE(format), 0, szBuffer, nIndex, 1);
		nIndex += 1;
		System.arraycopy(Converter.getBytesFromIntLE(datasize), 0, szBuffer, nIndex, 4);
	}
	public void save(byte[] szBuffer, int start) {
		int nIndex = start;
		
		format = (char)szBuffer[0];
		nIndex += 1;
		byte[] datasizeBytes = new byte[4];
		System.arraycopy(szBuffer, nIndex, datasizeBytes, 0, 4);
		datasize = Converter.readIntLittleEndian(datasizeBytes);
//		datasize = Converter.getIntFromBytes(datasizeBytes);
	}
	
	public void save2(byte[] szBuffer, int start, int dstOffset, int dstLen) {}
	
	public int size() {
		return 5;
	}

}
