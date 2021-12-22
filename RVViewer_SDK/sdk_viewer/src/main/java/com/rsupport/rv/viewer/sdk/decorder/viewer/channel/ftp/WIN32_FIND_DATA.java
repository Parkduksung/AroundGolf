package com.rsupport.rv.viewer.sdk.decorder.viewer.channel.ftp;


import java.io.File;

import com.rsupport.rv.viewer.sdk.common.log.RLog;
import com.rsupport.rv.viewer.sdk.decorder.Converter;
import com.rsupport.rv.viewer.sdk.decorder.model.IModel;

public class WIN32_FIND_DATA implements IModel{

	public static final int FILE_ATTRIBUTE_READONLY = 0x00000001;
	public static final int FILE_ATTRIBUTE_HIDDEN = 0x00000002;
	public static final int FILE_ATTRIBUTE_SYSTEM = 0x00000004;
	public static final int FILE_ATTRIBUTE_DIRECTORY = 0x00000010;
	public static final int FILE_ATTRIBUTE_ARCHIVE = 0x00000020;
	public static final int FILE_ATTRIBUTE_DEVICE = 0x00000040;
	public static final int FILE_ATTRIBUTE_NORMAL = 0x00000080;
	public static final int FILE_ATTRIBUTE_TEMPORARY = 0x00000100;
	public static final int FILE_ATTRIBUTE_SPARSE_FILE = 0x00000200;
	public static final int FILE_ATTRIBUTE_REPARSE_POINT = 0x00000400;
	public static final int FILE_ATTRIBUTE_COMPRESSED = 0x00000800;
	public static final int FILE_ATTRIBUTE_OFFLINE = 0x00001000;
	public static final int FILE_ATTRIBUTE_NOT_CONTENT_INDEXED = 0x00002000;
	public static final int FILE_ATTRIBUTE_ENCRYPTED = 0x00004000;
	
	public long nFileSize = 0l;
	public String FileName = "";
	
    public int dwFileAttributes = 0;
    public long ftCreationTime = 0l;
    public long ftLastAccessTime = 0l;
//  public long ftLastWriteTime = 0l;
    public int dwLow_DateTime = 0;
    public int dwHigh_DateTime = 0;
    
    public int nFileSizeHigh;
    public int nFileSizeLow;
    public int dwReserved0;
    public int dwReserved1;
    
    public byte cFileName[] = new byte[520];
    public byte cAlternateFileName[] = new byte[28];
    
    public static int DATA_SIZE = 592;
    
	public void packing(File fl) {
		dwFileAttributes = 0;
		
		if (fl.isHidden()) {
			dwFileAttributes |= FILE_ATTRIBUTE_HIDDEN;
		}
		if (!fl.canWrite()) {
			dwFileAttributes |= FILE_ATTRIBUTE_READONLY;
		}
		if (dwFileAttributes == 0) {
			dwFileAttributes = FILE_ATTRIBUTE_ARCHIVE;
		}
		if (fl.isDirectory()) {
			dwFileAttributes |= FILE_ATTRIBUTE_DIRECTORY;
		}

		ftCreationTime = 0l;
		ftLastAccessTime = 0l;

		long time = fl.lastModified();
		time *= 10000;
		time =  time + (0x19db1ded53e8000L); // the difference Win32 date(1/1/1601) and java date(1/1/1970)
		
		dwLow_DateTime = (int) time;
		dwHigh_DateTime = (int) (time >> 32);
		
		long size = nFileSize = fl.length();
		nFileSizeLow = (int) size;
		nFileSizeHigh = (int) (size >> 32);

		dwReserved0 = 0;
		dwReserved1 = 0;

		Converter.convert2ByteArray(fl.getName(), cFileName, true);
		Converter.convert2ByteArray("", cAlternateFileName, true);
		FileName = fl.getName();
	}
	
	public void push(byte[] szBuffer, int nStart) {
		int nIndex = nStart;
		System.arraycopy(Converter.getBytesFromIntLE(dwFileAttributes), 0, szBuffer, nIndex, 4);  
		nIndex += 4;
		System.arraycopy(Converter.getBytesFromLongLE(ftCreationTime), 0, szBuffer, nIndex,	8);
		nIndex += 8;
		System.arraycopy(Converter.getBytesFromLongLE(ftLastAccessTime), 0, szBuffer, nIndex, 8);
		nIndex += 8;
//		System.arraycopy(Converter.getBytesFromLongLE(ftLastWriteTime), 0, szBuffer, nIndex, 8);
//		nIndex += 8;
		System.arraycopy(Converter.getBytesFromIntLE(dwLow_DateTime), 0, szBuffer, nIndex, 4);
		nIndex += 4;
		System.arraycopy(Converter.getBytesFromIntLE(dwHigh_DateTime), 0, szBuffer, nIndex, 4);
		nIndex += 4;
		System.arraycopy(Converter.getBytesFromIntLE(nFileSizeHigh), 0, szBuffer, nIndex, 4);
		nIndex += 4;
		System.arraycopy(Converter.getBytesFromIntLE(nFileSizeLow), 0, szBuffer, nIndex, 4);
		nIndex += 4;
		System.arraycopy(Converter.getBytesFromIntLE(dwReserved0), 0, szBuffer, nIndex, 4);
		nIndex += 4;
		System.arraycopy(Converter.getBytesFromIntLE(dwReserved1), 0, szBuffer, nIndex, 4);
		nIndex += 4;
		System.arraycopy(cFileName, 0, szBuffer, nIndex, cFileName.length);
		nIndex += cFileName.length;
		System.arraycopy(cAlternateFileName, 0, szBuffer, nIndex, cAlternateFileName.length);
		nIndex += cAlternateFileName.length;
	}

	public void save(byte[] szBuffer, int nStart) {
		int nIndex = nStart;
		dwFileAttributes = Converter.readIntLittleEndian(szBuffer, nIndex);
		nIndex += 4;
		ftCreationTime = Converter.readLongLittleEndian(szBuffer, nIndex);
		nIndex += 8;
		ftLastAccessTime = Converter.readLongLittleEndian(szBuffer, nIndex);
		nIndex += 8;
//		ftLastWriteTime = Converter.readLongLittleEndian(szBuffer, nIndex);
//		nIndex += 8;
		dwLow_DateTime = Converter.readIntLittleEndian(szBuffer, nIndex);
		nIndex += 4;
		dwHigh_DateTime = Converter.readIntLittleEndian(szBuffer, nIndex);
		nIndex += 4;
		nFileSizeHigh = Converter.readIntLittleEndian(szBuffer, nIndex);
		nIndex += 4;
		nFileSizeLow = Converter.readIntLittleEndian(szBuffer, nIndex);
		nIndex += 4;
		dwReserved0 = Converter.readIntLittleEndian(szBuffer, nIndex);
		nIndex += 4;
		dwReserved1 = Converter.readIntLittleEndian(szBuffer, nIndex);
		nIndex += 4;
		System.arraycopy(szBuffer, nIndex, cFileName, 0, cFileName.length);
		nIndex += cFileName.length;
		System.arraycopy(szBuffer, nIndex, cAlternateFileName, 0, cAlternateFileName.length);
		nIndex += cAlternateFileName.length;
		
		RLog.d("WIN32_FIND_DATA : dwFileAttributes : " + dwFileAttributes);
		RLog.d("WIN32_FIND_DATA : ftCreationTime : " + ftCreationTime);
		RLog.d("WIN32_FIND_DATA : ftLastAccessTime : " + ftLastAccessTime);
		RLog.d("WIN32_FIND_DATA : dwLow_DateTime : " + dwLow_DateTime);
		RLog.d("WIN32_FIND_DATA : dwHigh_DateTime : " + dwHigh_DateTime);
		RLog.d("WIN32_FIND_DATA : nFileSizeHigh : " + nFileSizeHigh);
		RLog.d("WIN32_FIND_DATA : nFileSizeLow : " + nFileSizeLow);
		RLog.d("WIN32_FIND_DATA : dwReserved0 : " + dwReserved0);
		RLog.d("WIN32_FIND_DATA : dwReserved1 : " + dwReserved1);
		try {
			String strFileName = new String(cFileName, "UTF-16LE");
			String strAlternateFileName = new String(cAlternateFileName);
			
			RLog.d("WIN32_FIND_DATA : strFileName : " + strFileName);
			RLog.d("WIN32_FIND_DATA : strAlternateFileName : " + strAlternateFileName);
		} catch (Exception e) {}
	}
	
	@Override
	public void save2(byte[] szBuffer, int nStart, int dstOffset, int dstLen) {
		// TODO Auto-generated method stub
		save(szBuffer, nStart);
	}
	
	public int size() {
		return DATA_SIZE;
	}
	
	/**
	 * 
	 * @author kim kun seok (kskim@rsupport.com)
	 * @param wfd wfd 자료형을 복사하여 넘겨줌
	 * */
	public boolean copy(WIN32_FIND_DATA wfd)
	{
		if(wfd ==null)
			return false;
		
		dwFileAttributes = wfd.dwFileAttributes;
		ftCreationTime = wfd.ftCreationTime;
		ftLastAccessTime = wfd.ftLastAccessTime;

		dwLow_DateTime = wfd.dwLow_DateTime;
		dwHigh_DateTime = wfd.dwHigh_DateTime;
		nFileSizeHigh = wfd.nFileSizeHigh;
		nFileSizeLow = wfd.nFileSizeLow;
		dwReserved0 = wfd.dwReserved0;
		dwReserved1 = wfd.dwReserved1;
		
		System.arraycopy(wfd.cFileName, 0, cFileName, 0, wfd.cFileName.length);

		System.arraycopy(wfd.cAlternateFileName, 0, cAlternateFileName, 0, wfd.cAlternateFileName.length);
		
		RLog.d("WIN32_FIND_DATA : dwFileAttributes : " + dwFileAttributes);
		RLog.d("WIN32_FIND_DATA : ftCreationTime : " + ftCreationTime);
		RLog.d("WIN32_FIND_DATA : ftLastAccessTime : " + ftLastAccessTime);
		RLog.d("WIN32_FIND_DATA : dwLow_DateTime : " + dwLow_DateTime);
		RLog.d("WIN32_FIND_DATA : dwHigh_DateTime : " + dwHigh_DateTime);
		RLog.d("WIN32_FIND_DATA : nFileSizeHigh : " + nFileSizeHigh);
		RLog.d("WIN32_FIND_DATA : nFileSizeLow : " + nFileSizeLow);
		RLog.d("WIN32_FIND_DATA : dwReserved0 : " + dwReserved0);
		RLog.d("WIN32_FIND_DATA : dwReserved1 : " + dwReserved1);
		
		try {
			String strFileName = new String(cFileName, "UTF-16LE");
			String strAlternateFileName = new String(cAlternateFileName);
			
			RLog.d("WIN32_FIND_DATA : strFileName : " + strFileName);
			RLog.d("WIN32_FIND_DATA : strAlternateFileName : " + strAlternateFileName);
		} catch (Exception e) {}
		
		return true;
	}

}
