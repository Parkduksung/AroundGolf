package com.rsupport.rv.viewer.sdk.decorder.viewer.channel.ftp;

import java.io.File;
import java.util.ArrayList;

public class CRFileFind64 {

	private long m_FileSize = 0;
	private ArrayList m_listFindFile;
	private long m_dwSizes = 0;
	
	public CRFileFind64() {
		m_listFindFile = new ArrayList();
	}

	public boolean AddFileInfo(String szPath) {
		File file = new File(szPath);
		if (!file.exists())
			return false;

		m_listFindFile.add(file);
		m_FileSize += file.length();
		return true;
	}
	
	boolean AddFileInfo(File file) {
		if (!file.exists())
			return false;
		
		m_listFindFile.add(file);
		m_FileSize += file.length();
		return true;
	}
	
	public boolean FindAllFilesInFolder(File fl, String szExt, boolean IsIncludeDirectory) {
		if(!fl.exists())
			return false;
		
		String strExt = "";
		int	nFind = szExt.indexOf('.');
		if(nFind > 0) {
			strExt = szExt.substring(nFind+1);
		}
		
		if(fl.isDirectory()) {
			if(IsIncludeDirectory) {
				AddFileInfo(fl);
			}
			
			String[] children = fl.list();
            for (int i=0; i<children.length; i++) {            	
            	FindAllFilesInFolder(new File(fl, children[i]),
            		szExt, IsIncludeDirectory);
            }
		}
		else {
			if(szExt.equals("*") || szExt.equals("*.*")) {
				AddFileInfo(fl);
			}
			else
			{
				String strFind = fl.getName();
				if(strFind.indexOf(strExt) >= 0)
					AddFileInfo(fl);
			}
		}
		
		return true;
	}
	
	public boolean FindAllFilesInFolder(String szPath, String szExt, boolean IsIncludeDirectory) {
		File fl = new File(szPath);
		return FindAllFilesInFolder(fl, szExt, IsIncludeDirectory);		
	}
	
	public ArrayList GetFileInfoList() {
		return m_listFindFile;
	}
	
	public void Close() {
		m_listFindFile.clear();
	}
	
	public int GetCountInList() {
		return m_listFindFile.size();
	};

	public long GetFindFileSizes() {
		return m_FileSize;
	};
}
