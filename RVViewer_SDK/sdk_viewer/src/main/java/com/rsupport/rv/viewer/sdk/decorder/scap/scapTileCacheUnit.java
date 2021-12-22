package com.rsupport.rv.viewer.sdk.decorder.scap;




import com.rsupport.rv.viewer.sdk.decorder.Converter;
import com.rsupport.rv.viewer.sdk.decorder.model.IModel;

public class scapTileCacheUnit implements IModel{

	public long bitIndex;
	
	public class data {
		public int x;
		public int y;
		public int w;// partial add
		public int h;
	}
	
	public data add = new data();
	public data hit = new data();
	
	
//	U32		crc;
//	union 
//	{
//		struct {
//			U16		x;
//			U16		y;
//			U16		w; // partial add
//			U16		h;
//		} add; 
//
//		struct {
//			U16		x;
//			U16		y;
//			U16		w; // partial hit
//			U16		h;
//		} hit; 
//	};
	
	public void push(byte[] szBuffer, int nStart) {
		int nIndex = nStart;
		// 패킷 만들기
//		System.arraycopy(Converter.getBytesFromIntLE((int) crc), 0, szBuffer,
//			nIndex, 4);
//		nIndex += 4;
	}

	public void save(byte[] szBuffer, int nStart) {
		int nIndex = nStart;

		bitIndex = Converter.readIntLittleEndian(szBuffer, nIndex) & 0xffffffffL;
		nIndex += 4;

		
		add.x = ((int) Converter.readShortLittleEndian(szBuffer,nIndex) & 0xffff);
		nIndex += 2;
		add.y = ((int) Converter.readShortLittleEndian(szBuffer,nIndex) & 0xffff);
		nIndex += 2;
		add.w = ((int) Converter.readShortLittleEndian(szBuffer,nIndex) & 0xffff);
		nIndex += 2;
		add.h = ((int) Converter.readShortLittleEndian(szBuffer,nIndex) & 0xffff);
		nIndex += 2;

		hit.x = add.x;
		hit.y = add.y;
		hit.w = add.w;
		hit.h = add.h;
	}

	public void save2(byte[] szBuffer, int start, int dstOffset, int dstLen) {
		
	}

	public int size() {
		// TODO Auto-generated method stub
		return 12;
	}

}
