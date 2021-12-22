package com.rsupport.rv.viewer.sdk.decorder.viewer.channel;


import com.rsupport.rv.viewer.sdk.decorder.Converter;

public class rcpRemoteInfoMsg {
	private short platform;
	private short account;
	private short appMode;
	private long majorVersion;
	private long minorVersion;
	private short data;
	
	public int payloadtype;
	public int msgsize;

	public void save(byte[] szBuffer, int nStart) {
		int nIndex = nStart;
		
		platform = Converter.toUnsignedChar(szBuffer[nIndex]);
		nIndex++;
		account = Converter.toUnsignedChar(szBuffer[nIndex]);
		nIndex++;
		appMode = Converter.toUnsignedChar(szBuffer[nIndex]);
		nIndex++;
		majorVersion = Converter.readLongLittleEndian4B(szBuffer, nIndex);
		nIndex += 4;
		minorVersion = Converter.readLongLittleEndian4B(szBuffer, nIndex);
		nIndex += 4;
		data = Converter.toUnsignedChar(szBuffer[nIndex]);
	}
	
	public int size() {
		return 1 + 1 + 1 + 4 + 4 + 1;
	}
}
