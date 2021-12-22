package com.rsupport.rv.viewer.sdk.decorder.viewer.channel;


import com.rsupport.rv.viewer.sdk.commons.misc.Conv;
import com.rsupport.rv.viewer.sdk.decorder.model.IModel;

public class rcpAgentLock implements IModel {

	public boolean agentLock = false;
	
	public void push(byte[] szBuffer, int nStart) {
		Conv.int2byte4((agentLock ? 1 : 0), szBuffer, 0);
	}
	
	public void save(byte[] szBuffer, int nStart) {
	}

	public void save2(byte[] szBuffer, int start, int dstOffset, int dstLen) {
	}

	public int size() {
		return 4;
	}
}
