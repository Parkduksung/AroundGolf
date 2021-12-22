package com.rsupport.rv.viewer.sdk.decorder.viewer.channel.ftp;



import java.io.File;

import com.rsupport.rv.viewer.sdk.common.log.RLog;
import com.rsupport.rv.viewer.sdk.control.StringEx;
import com.rsupport.rv.viewer.sdk.decorder.Converter;
import com.rsupport.rv.viewer.sdk.decorder.model.IModel;
import com.rsupport.rv.viewer.sdk.setting.GlobalStatic;

public class rcpSFTPFileHeader implements IModel {

	public String realPath = "";
	public String orgPath = "";
	
	protected byte bRealPath[] = new byte[520];
	protected byte bOrgPath[] = new byte[520];
	    
	public WIN32_FIND_DATA wfd = new WIN32_FIND_DATA();

	protected String getFileSlash() {
		if (GlobalStatic.g_operationType == GlobalStatic.WIN) {
			return "\\";
		} else {
			return "/";
		}
	}

	public void packing(File fl) {
		String tmp1 = StringEx.replaceString("/", getFileSlash(), realPath);
		String tmp2 = StringEx.replaceString("/", getFileSlash(), orgPath);
		
		Converter.convert2ByteArray(tmp1, bRealPath, true);
		Converter.convert2ByteArray(tmp2, bOrgPath, true);
		
		wfd.packing(fl);
	}
	
	public void packingForHost(File fl) {
		if (GlobalStatic.g_hostOsType == GlobalStatic.HOST_WINDOWS) {
			packingForWin(fl);
		} else {
			packingForMac(fl);
		}
	}
	
	public void packingForWin(File fl) {
		RLog.d("packingForWin");
		String tmp1 = StringEx.replaceString("/", "\\", realPath);
		String tmp2 = StringEx.replaceString("/", "\\", orgPath);
		
//		RLog.d("packingForWin realpath : " + tmp1);
//		RLog.d("packingForWin orgPath : " + tmp2);

		Converter.convert2ByteArray(tmp1, bRealPath, true);
		Converter.convert2ByteArray(tmp2, bOrgPath, true);
		
		wfd.packing(fl);
	}
	
	public void packingForMac(File fl) {
		String tmp1 = StringEx.replaceString("\\", "/", realPath);
		String tmp2 = StringEx.replaceString("\\", "/", orgPath);
		
		Converter.convert2ByteArray(tmp1, bRealPath, true);
		Converter.convert2ByteArray(tmp2, bOrgPath, true);
		
		wfd.packing(fl);
	}

	public void push(byte[] szBuffer, int nStart) {
		int nIndex = nStart;
		
		System.arraycopy(bRealPath, 0, szBuffer, nIndex, bRealPath.length);
		nIndex += bRealPath.length;
		System.arraycopy(bOrgPath, 0, szBuffer, nIndex, bOrgPath.length);
		nIndex += bOrgPath.length;
		
		wfd.push(szBuffer, nIndex);
	}
	
	private String convertFilename(String filename) {
		return StringEx.replaceString("\\", getFileSlash(), filename);
	}

	public void save(byte[] szBuffer, int nStart) {
		int nIndex = nStart;
		System.arraycopy(szBuffer, nIndex, bRealPath, 0, bRealPath.length);
		nIndex += bRealPath.length;
		System.arraycopy(szBuffer, nIndex, bOrgPath, 0, bOrgPath.length);
		nIndex += bOrgPath.length;
		
		try {
			realPath = new String(bRealPath, "UTF-16LE");
			realPath = convertFilename(realPath);
			orgPath = new String(bOrgPath, "UTF-16LE");
			orgPath = convertFilename(orgPath);
		} catch (Exception e) {}

		wfd.save(szBuffer, nIndex);
	}

	public void save2(byte[] szBuffer, int nStart, int dstOffset, int dstLen) {
	}

	public int size() {
		return wfd.size() + 1040;
	}
}
