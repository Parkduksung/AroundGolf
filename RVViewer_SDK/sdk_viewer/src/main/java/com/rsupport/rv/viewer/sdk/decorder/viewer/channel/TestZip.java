package com.rsupport.rv.viewer.sdk.decorder.viewer.channel;


import com.rsupport.rv.viewer.sdk.common.log.RLog;
import com.rsupport.rv.viewer.sdk.control.DeflaterEx;
import com.rsupport.rv.viewer.sdk.decorder.scapDec.unzip;

import java.io.File;
import java.io.RandomAccessFile;



class TestZipSub {
	
	private unzip m_pUnzip;
	private unzip m_pUnzip2;

	public TestZipSub() {
		init();
	}
	
	public void init() {
		System.loadLibrary("jzlib");
		m_pUnzip = new unzip();
		m_pUnzip2 = new unzip();
		File fl;
//		String strFile = "C:\\자료실\\멜론 9월4주차 1위~100위[2008.09.21] 주간 TOP100.zip";
		String strFile = "C:\\data.txt";

		fl = new File(strFile);
		RandomAccessFile rf = null;
		try {
			String s = "박성연\n";
			byte[] bArray = s.getBytes("UTF-16LE");
			String ss = s.replaceAll("\n", "\r\n");
			bArray = ss.getBytes("UTF-16LE");
			bArray = s.getBytes("UTF-16LE");
			
			 rf = new RandomAccessFile(fl, "r");
			 byte[] data = new byte[10240];
			 int read = rf.read(data, 0, 2);

			 DeflaterEx compresser = new DeflaterEx(DeflaterEx.DEFAULT_COMPRESSION, true);
			 byte[] dataBuf = new byte[read];
			 byte[] zip_buffer = new byte[10240*10];
			 compresser.setInput(data, 0, 2);
			 compresser.finish();
			 int nCompressed = compresser.deflate(zip_buffer);

			 byte[] tempB = new byte[10240];
			 m_pUnzip.set_outbuf(tempB, 0, tempB.length);
			 m_pUnzip.set_inbuf(zip_buffer, 0, nCompressed);
			 int decompressSize = m_pUnzip.decompress();
			 String str = new String(tempB);
			 RLog.d(str);
			 
			 read = rf.read(data, 0, 2);

			 //DeflaterEx compresser2 = new DeflaterEx(DeflaterEx.DEFAULT_COMPRESSION, true);
			 compresser.setInput(data, 0, 2);
			 compresser.finish();
			 nCompressed = compresser.deflate(zip_buffer);

			 tempB = new byte[10240];
			 m_pUnzip.set_outbuf(tempB, 0, tempB.length);
			 m_pUnzip.set_inbuf(zip_buffer, 0, nCompressed);
			 decompressSize = m_pUnzip.decompress();
			 str = new String(tempB);
			 RLog.d(str);
			 
			 RLog.d(nCompressed);
			 int i = 0;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}


public class TestZip {
//	public static void main(String[] args) {
//		TestZipSub test = new TestZipSub();
//	}
}
