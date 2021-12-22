package com.rsupport.rv.viewer.sdk.decorder.scapDec;

public class LastPalette32 {
	// U8 m_table[MAX_ENTRY *BPP16];
	// U8 m_cache[MAX_ENTRY *BPP16];

	int max_entry = 0;

	public LastPalette32(int max_entry) {
		this.max_entry = max_entry;

		// BPP16 =2
		m_table = new byte[max_entry * 3];
		m_cache = new byte[max_entry * 3];
	}

	
	public byte[] m_table;
	public byte[] m_cache;

	// inline U8* GetColor(int idx) { return &m_table[idx * BPP16]; }
	// inline U8* GetCacheColor(int idx) { return &m_cache[idx*BPP16]; }
	public int GetCacheColorIndex(int idx) {
		return idx*3;
	}
	public int GetColorIndex(int idx) {
		return idx*3;
	}
	
	
	public void Update(byte[] clr, int offset,  int count) {
		System.arraycopy(clr, offset, m_table, 0, 3*count);
	}
	
	public void UpdateCache(byte[] palNew, int offset, int nNew,
			boolean[] hitTable) {
		if (nNew > 0) {
			for (int i = 0, slot = 0; i < nNew; ++i) {
				while (hitTable[slot]) {
					++slot;
				}
				System.arraycopy(palNew, i * 3, m_cache, slot * 3, 3);
				++slot;
			}
		}
	}
}
