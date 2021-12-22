package com.rsupport.rv.viewer.sdk.decorder.model;

public class UserInfo {

	public String id = "";
	public String name = "";
	public String pcname = "";
	public String ip = "";
	public String mac = "";
	
	private byte[] m_bytesID = new byte[64*2];
	private byte[] m_bytesName = new byte[64*2];
	private byte[] m_bytesPcName = new byte[32*2];
	private byte[] m_bytesIP = new byte[24*2];
	private byte[] m_bytesMac = new byte[20*2];
	
	public void assingPushValue() {
		try {
			m_bytesID = id.getBytes("UTF-16LE");
			m_bytesName = name.getBytes("UTF-16LE");
			m_bytesPcName = pcname.getBytes("UTF-16LE");
			m_bytesIP = ip.getBytes("UTF-16LE");
			m_bytesMac = mac.getBytes("UTF-16LE");		
		} catch (Exception e) {}
	}
	
	public void assingSaveValue() {
		try {
			id = new String(m_bytesID, "UTF-16LE");
			name = new String(m_bytesName, "UTF-16LE");
			pcname = new String(m_bytesPcName, "UTF-16LE");
			ip = new String(m_bytesIP, "UTF-16LE");
			mac = new String(m_bytesMac, "UTF-16LE");		
		} catch (Exception e) {}
	}
	
	public void push(byte[] bytes, int index) {
		System.arraycopy(m_bytesID, 0, bytes, index, m_bytesID.length);
		index += 64*2;
		System.arraycopy(m_bytesName, 0, bytes, index, m_bytesName.length);
		index += 64*2;
		System.arraycopy(m_bytesPcName, 0, bytes, index, m_bytesPcName.length);
		index += 32*2;
		System.arraycopy(m_bytesIP, 0, bytes, index, m_bytesIP.length);
		index += 24*2;
		System.arraycopy(m_bytesMac, 0, bytes, index, m_bytesMac.length);
		index += 20*2;
	}
	
	public void save(byte[] bytes, int index) {
		System.arraycopy(bytes, index, m_bytesID, 0, 64*2);
		index += 64*2;
		System.arraycopy(bytes, index, m_bytesName, 0, 64*2);
		index += 64*2;
		System.arraycopy(bytes, index, m_bytesPcName, 0, 32*2);
		index += 32*2;
		System.arraycopy(bytes, index, m_bytesIP, 0, 24*2);
		index += 24*2;
		System.arraycopy(bytes, index, m_bytesMac, 0, 20*2);
		index += 20*2;
	}
	
	public static int size() {
		return 204*2;
	}
}
