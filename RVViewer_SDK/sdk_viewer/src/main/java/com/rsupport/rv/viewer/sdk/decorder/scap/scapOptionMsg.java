package com.rsupport.rv.viewer.sdk.decorder.scap;


import com.rsupport.rv.viewer.sdk.common.log.RLog;
import com.rsupport.rv.viewer.sdk.decorder.Converter;
import com.rsupport.rv.viewer.sdk.decorder.model.IModel;

public class scapOptionMsg implements IModel {
	private int m_function;
	private int m_option;
	private int m_sizeFTP;
	
	public void setFunction(int val) {
		m_function = val;
	}
	
	public void setOption(int val) {
		m_option = val;
	}
	
	public void addOption(int val) {
		m_option |= val;
	}
	
	public void setMaxFTPSize(int val) {
		m_sizeFTP = val;
	}
	
	public void save(byte[] szBuffer, int nStart) {}
	
	public void push(byte[] szBuffer, int nStart) {
		int nIndex = nStart;
		System.arraycopy(Converter.getBytesFromIntLE(m_function), 0, szBuffer, nIndex, 4);
		nIndex += 4;
		System.arraycopy(Converter.getBytesFromIntLE(m_option), 0, szBuffer, nIndex, 4);
		nIndex += 4;
		RLog.d("option : " + m_option);
		System.arraycopy(Converter.getBytesFromIntLE(m_sizeFTP), 0, szBuffer, nIndex, 4);
	}
	
	public int size() {
		return 4 * 3;
	}

	public void save2(byte[] szBuffer, int start, int dstOffset, int dstLen) {}
}
