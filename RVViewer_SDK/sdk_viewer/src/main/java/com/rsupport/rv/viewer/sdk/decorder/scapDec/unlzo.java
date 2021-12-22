package com.rsupport.rv.viewer.sdk.decorder.scapDec;

public class unlzo {
	
	static {
	}
	
	public int decompress(byte[] dst, byte[] src, int len) {
		int nDecomp = 0;
		nDecomp = lzo1xDecompress(dst, src, len);
		return nDecomp;
	}
	
	public int compress(byte[] dst, byte[] src, int len) {
		int nDecomp = 0;
		nDecomp = lzo1xCompress(dst, src, len);
		return nDecomp;
	}
	
	private native static int lzo1xDecompress(byte[] dst, byte[] src, int len);
	private native static int lzo1xCompress(byte[] dst, byte[] src, int len);
	
}
