package com.rsupport.rv.viewer.sdk.decorder.cache;


public class TileCache {
    public static final int WIDTH = 0x40;
    public static final int MASK = WIDTH - 1;

    int m_nTileBitSize;
    int m_nTileBitScan;
    int m_nBytesPerPixel;
    int m_nMaxTileCount;
    byte[] m_bits;

    public TileCache(int nMaxTile, int nBytePerPixel) {
        m_bits = null;
        m_nTileBitSize = 0;
        m_nBytesPerPixel = 0;
        m_nMaxTileCount = 0;

        SetMaxTile(nMaxTile, nBytePerPixel);
    }

    public byte[] getBits() {
        return m_bits;
    }

    public int GetScanByte() {
        return m_nTileBitScan;
    }

    public int AddTile(long bitIndex) {
        return (int) bitIndex * m_nTileBitSize;
    }

    public int GetTile(long bitIndex) {
        return (int) bitIndex * m_nTileBitSize;
    }

    public void SetMaxTile(int nMaxTile, int nBytePerPixel) {
        m_bits = null;
        m_nBytesPerPixel = 0;

        if (nMaxTile > 0) {
            m_nBytesPerPixel = nBytePerPixel;
            m_nTileBitScan = (WIDTH * nBytePerPixel);
            m_nTileBitSize = (WIDTH * nBytePerPixel) * WIDTH;
            m_bits = new byte[nMaxTile * m_nTileBitSize];
        }

        m_nMaxTileCount = nMaxTile;
    }

}
