package com.rsupport.rv.viewer.sdk.decorder.scapDec;

public class LastPalette {
	
	private int m_maxPalette;
	private byte[] m_table;
	private byte[] m_cache;
	
	public LastPalette(int maxPalette) {
		m_maxPalette = maxPalette;
		init();
		clear();
	}
	
	public void init() {
		m_table = new byte[m_maxPalette*3];
		m_cache = new byte[m_maxPalette*3];
	}
	
	public void clear() {}
	
//	public byte[] getColor(int idx) {
//		return null;
//	}
	
	public byte[] getColor() {
		return m_table;
	}
	public byte[] getCacheColor() {
		return m_cache;
	}
	
//	public byte[] getCacheColor(int idx) {
//		return null;
//	}
	
	public void Update(byte[] clr, int count) {
		System.arraycopy(clr, 0, m_table, 0, 3*count);
	}
	
	public void UpdateCache(byte[] palNew, int nNew, boolean[] hitTable) {
		if (nNew > 0) {
			for (int i = 0, slot = 0 ; i < nNew ; ++i) {
				while (hitTable[slot]) {
					++slot;
				}
				System.arraycopy(palNew, i*3, m_cache, slot*3, 3);
				++slot;
			}
		}
	}
}
