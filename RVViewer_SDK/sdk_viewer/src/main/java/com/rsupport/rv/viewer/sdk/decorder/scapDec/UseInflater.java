package com.rsupport.rv.viewer.sdk.decorder.scapDec;


import com.rsupport.rv.viewer.sdk.control.InflaterEx;

import java.util.zip.Deflater;



//InflaterEx 테스트용 클래스
public class UseInflater {

//	public static void main(String[] args) {
//		// TODO Auto-generated method stub
//		try {
//			new UseInflater();
//		} catch (Exception ex) {
//			ex.printStackTrace();
//		}
//	}
	
	public UseInflater() throws Exception{
		// Encode a String into bytes
		 String inputString = "blahblahblah??";
		 byte[] input = inputString.getBytes("UTF-8");

		 // Compress the bytes
		 byte[] output = new byte[100];
		 Deflater compresser = new Deflater();
		 compresser.setInput(input);
		 compresser.finish();
		 int compressedDataLength = compresser.deflate(output);

		 // Decompress the bytes
		 InflaterEx decompresser = new InflaterEx();
//		 long v = decompresser.TestLib(10L);
		 
		 decompresser.setInput(output, 0, compressedDataLength);
		 byte[] result = new byte[100];
		 int resultLength = decompresser.inflate(result);
		 decompresser.end();

		 // Decode the bytes into a String
		 String outputString = new String(result, 0, resultLength, "UTF-8");

	}

}
