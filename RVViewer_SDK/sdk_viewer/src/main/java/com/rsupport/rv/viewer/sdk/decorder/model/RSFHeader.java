package com.rsupport.rv.viewer.sdk.decorder.model;


import com.rsupport.rv.viewer.sdk.common.log.RLog;
import com.rsupport.rv.viewer.sdk.decorder.Converter;

public class RSFHeader {
	
	public String fileType = "";
	public RVERSIONINFO versionInfo = new RVERSIONINFO();
	public String passward = "";
	public long dataSize;
	public long startTime;
	public long endTime;
	public long dataCount;
	public UserInfo localuserinfo = new UserInfo();
	public UserInfo remoteuserinfo = new UserInfo();
	public String guid = "";
	public String description = "";
	
	public int reserved1;
	public int reserved2;
	public int reserved3;
	public int reserved4;
	public int reserved5;
	public int reserved6;
	public int reserved7;
	public int reserved8;
	public int reserved9;
	public int reserved10;
	public int reserved11;
	public int reserved12;
	public int reserved13;
	public int reserved14;
	public int reserved15;
	public int reserved16;
	
	public String sReserved1 = "";
	public String sReserved2 = "";
	public String sReserved3 = "";
	public String sReserved4 = "";
	public String sReserved5 = "";
	public String sReserved6 = "";
	
	private byte[] m_bytesFileType = new byte[8*2];
	private byte[] m_bytesPassward = new byte[64*2];
	private byte[] m_bytesGUID = new byte[40*2];
	private byte[] m_bytesDescription = new byte[128*2];

	private byte[] m_bytessReserved1 = new byte[64*2];
	private byte[] m_bytessReserved2 = new byte[64*2];
	private byte[] m_bytessReserved3 = new byte[64*2];
	private byte[] m_bytessReserved4 = new byte[64*2];
	private byte[] m_bytessReserved5 = new byte[64*2];
	private byte[] m_bytessReserved6 = new byte[64*2];
	
	public void assignPushValue() {
		try {
			localuserinfo.assingPushValue();
			remoteuserinfo.assingPushValue();
			m_bytesFileType = fileType.getBytes("UTF-16LE");
			m_bytesPassward = passward.getBytes("UTF-16LE");
			m_bytesGUID = guid.getBytes("UTF-16LE");
			m_bytesDescription = description.getBytes("UTF-16LE");
		} catch (Exception e) {}
	}
	
	public void push(byte[] bytes, int index) {
		System.arraycopy(m_bytesFileType, 0, bytes, index, m_bytesFileType.length);
		index += 8*2;
		versionInfo.push(bytes, index);
		index += RVERSIONINFO.size();
		System.arraycopy(m_bytesPassward, 0, bytes, index, m_bytesPassward.length);
		index += 64*2;
		
		System.arraycopy(Converter.getBytesFromLongLE(dataSize), 0, bytes, index, 8);
		index += 8;
		System.arraycopy(Converter.getBytesFromLongLE(startTime), 0, bytes, index, 4);
		index += 4;
		System.arraycopy(Converter.getBytesFromLongLE(endTime), 0, bytes, index, 4);
		index += 4;
		System.arraycopy(Converter.getBytesFromLongLE(dataCount), 0, bytes, index, 8);
		index += 8;
		
		localuserinfo.push(bytes, index);
		index += UserInfo.size();
		remoteuserinfo.push(bytes, index);
		index += UserInfo.size();
		
		System.arraycopy(m_bytesGUID, 0, bytes, index, m_bytesGUID.length);
		index += 40*2;
		System.arraycopy(m_bytesDescription, 0, bytes, index, m_bytesDescription.length);
		index += 128*2;
		
		System.arraycopy(Converter.getBytesFromLongLE(reserved1), 0, bytes, index, 4);
		index += 4;
		System.arraycopy(Converter.getBytesFromLongLE(reserved2), 0, bytes, index, 4);
		index += 4;
		System.arraycopy(Converter.getBytesFromLongLE(reserved3), 0, bytes, index, 4);
		index += 4;
		System.arraycopy(Converter.getBytesFromLongLE(reserved4), 0, bytes, index, 4);
		index += 4;
		System.arraycopy(Converter.getBytesFromLongLE(reserved5), 0, bytes, index, 4);
		index += 4;
		System.arraycopy(Converter.getBytesFromLongLE(reserved6), 0, bytes, index, 4);
		index += 4;
		System.arraycopy(Converter.getBytesFromLongLE(reserved7), 0, bytes, index, 4);
		index += 4;
		System.arraycopy(Converter.getBytesFromLongLE(reserved8), 0, bytes, index, 4);
		index += 4;
		System.arraycopy(Converter.getBytesFromLongLE(reserved9), 0, bytes, index, 4);
		index += 4;
		System.arraycopy(Converter.getBytesFromLongLE(reserved10), 0, bytes, index, 4);
		index += 4;
		System.arraycopy(Converter.getBytesFromLongLE(reserved11), 0, bytes, index, 4);
		index += 4;
		System.arraycopy(Converter.getBytesFromLongLE(reserved12), 0, bytes, index, 4);
		index += 4;
		System.arraycopy(Converter.getBytesFromLongLE(reserved13), 0, bytes, index, 4);
		index += 4;
		System.arraycopy(Converter.getBytesFromLongLE(reserved14), 0, bytes, index, 4);
		index += 4;
		System.arraycopy(Converter.getBytesFromLongLE(reserved15), 0, bytes, index, 4);
		index += 4;
		System.arraycopy(Converter.getBytesFromLongLE(reserved16), 0, bytes, index, 4);
		index += 4;

		System.arraycopy(m_bytessReserved1, 0, bytes, index, 64);
		index += 64*2;
		System.arraycopy(m_bytessReserved2, 0, bytes, index, 64);
		index += 64*2;
		System.arraycopy(m_bytessReserved3, 0, bytes, index, 64);
		index += 64*2;
		System.arraycopy(m_bytessReserved4, 0, bytes, index, 64);
		index += 64*2;
		System.arraycopy(m_bytessReserved5, 0, bytes, index, 64);
		index += 64*2;
		System.arraycopy(m_bytessReserved6, 0, bytes, index, 64);
		index += 64*2;
	}
	
	public void save(byte[] bytes, int index) {
		System.arraycopy(bytes, index, m_bytesFileType, 0, 8*2);
		index += 8*2;
		versionInfo.save(bytes, index);
		index += versionInfo.size();
		System.arraycopy(bytes, index, m_bytesPassward, 0, 64*2);
		index += 64*2;
		
		dataSize = Converter.readLongLittleEndian(bytes, index);
		index += 8;
		startTime = Converter.readLongLittleEndian4B(bytes, index);
		index += 4;
		endTime = Converter.readLongLittleEndian4B(bytes, index);
		index += 4;
		dataCount = Converter.readLongLittleEndian(bytes, index);
		index += 8;
		
		localuserinfo.save(bytes, index);
		index += localuserinfo.size();
		remoteuserinfo.save(bytes, index);
		index += remoteuserinfo.size();

		System.arraycopy(bytes, index, m_bytesGUID, 0, 40*2);
		index += 40*2;
		System.arraycopy(bytes, index, m_bytesDescription, 0, 128*2);
		index += 128*2;
	
		reserved1 = Converter.readIntLittleEndian(bytes, index);
		index += 4;
		reserved2 = Converter.readIntLittleEndian(bytes, index);
		index += 4;
		reserved3 = Converter.readIntLittleEndian(bytes, index);
		index += 4;
		reserved4 = Converter.readIntLittleEndian(bytes, index);
		index += 4;
		reserved5 = Converter.readIntLittleEndian(bytes, index);
		index += 4;
		reserved6 = Converter.readIntLittleEndian(bytes, index);
		index += 4;
		reserved7 = Converter.readIntLittleEndian(bytes, index);
		index += 4;
		reserved8 = Converter.readIntLittleEndian(bytes, index);
		index += 4;
		reserved9 = Converter.readIntLittleEndian(bytes, index);
		index += 4;
		reserved10 = Converter.readIntLittleEndian(bytes, index);
		index += 4;
		reserved11 = Converter.readIntLittleEndian(bytes, index);
		index += 4;
		reserved12 = Converter.readIntLittleEndian(bytes, index);
		index += 4;
		reserved13 = Converter.readIntLittleEndian(bytes, index);
		index += 4;
		reserved14 = Converter.readIntLittleEndian(bytes, index);
		index += 4;
		reserved15 = Converter.readIntLittleEndian(bytes, index);
		index += 4;
		reserved16 = Converter.readIntLittleEndian(bytes, index);
		index += 4;
	
		System.arraycopy(bytes, index, m_bytessReserved1, 0, 64*2);
		index += 64*2;
		System.arraycopy(bytes, index, m_bytessReserved2, 0, 64*2);
		index += 64*2;
		System.arraycopy(bytes, index, m_bytessReserved3, 0, 64*2);
		index += 64*2;
		System.arraycopy(bytes, index, m_bytessReserved4, 0, 64*2);
		index += 64*2;
		System.arraycopy(bytes, index, m_bytessReserved5, 0, 64*2);
		index += 64*2;
		System.arraycopy(bytes, index, m_bytessReserved6, 0, 64*2);
		index += 64*2;

		try {
			fileType = new String(m_bytesFileType, "UTF-16LE");
			passward = new String(m_bytesPassward, "UTF-16LE");
			guid = new String(m_bytesGUID, "UTF-16LE");
			description = new String(m_bytesDescription, "UTF-16LE");
			sReserved1 = new String(m_bytessReserved1, "UTF-16LE");
			sReserved2 = new String(m_bytessReserved2, "UTF-16LE");
			sReserved3 = new String(m_bytessReserved3, "UTF-16LE");
			sReserved4 = new String(m_bytessReserved4, "UTF-16LE");
			sReserved5 = new String(m_bytessReserved5, "UTF-16LE");
			sReserved6 = new String(m_bytessReserved6, "UTF-16LE");

			RLog.d("fileType : " + fileType);
			RLog.d("passward : " + passward);
			RLog.d("guid : " + guid);
			RLog.d("description : " + description);

			RLog.d("dataSize : " + dataSize);
			RLog.d("startTime : " + startTime);
			RLog.d("endTime : " + endTime);
			RLog.d("dataCount : " + dataCount);
		} catch (Exception e) {}
	}
	
	public int size() {
		return 2168;
	}
	
	public static final int LENGTH = 2168;
}
