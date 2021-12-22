package com.rsupport.rv.viewer.sdk.decorder.scapDec;

public class JppDec {
	
	public final int JPG_BUFSIZE = 256 * 3;
	
	public void align_tile24(int scan, int th, byte[] pTile) {
		int dstIndex = 0, srcIndex = 16 * 3;
		while (--th > 0) {
			dstIndex += scan;
			System.arraycopy(pTile, srcIndex, pTile, dstIndex, scan);
			srcIndex += 16 * 3;
		}
	}
	
	public void align_tile15(int scan, int th, byte[] pTile) {
		int srcIndex = 16 * 2, dstIndex = 0;
		while (--th > 0) {
			dstIndex += scan;
			System.arraycopy(pTile, srcIndex, pTile, dstIndex, scan);
			srcIndex += 16 * 2;
		}
	}
	
	private byte[] m_tileBuf;
	
	public byte[] get_tilebuf() {
		return m_tileBuf;
	}
	
	private Decoder m_decoder;
	public JppDec(Decoder decoder) {
		m_decoder = decoder;
		initIDs();
		setQuality(decoder.getLowQuality(), decoder.getHighQuality());
	}
	
	public void decompress_tile24(int tw, int th, byte[] pOutBGR, boolean bLow) {
		int flag = 1; 
		if (!bLow) flag = 0;
		DecompressTile24(pOutBGR, flag);
		if (tw < 16) align_tile24(tw*3, th, pOutBGR);
	}
	
	public void decompress_tile15(int tw, int th, byte[] pOutBGR, boolean bLow) {
		int flag = 1;
		if (!bLow) flag = 0;
		DecompressTile15(pOutBGR, flag);
	}
	
	public void endJppDec() {
		end();
	}

	private native static void initIDs();
	public native void setJpgData(byte[] pSrc, int size);
	public native void setQuality(int lowQuality, int highQuality);
	private native void DecompressTile15(byte[] pDst, int low);
	private native void DecompressTile24(byte[] pDst, int low);
	private native static void end();
	
}
