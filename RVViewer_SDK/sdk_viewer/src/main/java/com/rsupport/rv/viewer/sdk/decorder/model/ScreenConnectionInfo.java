package com.rsupport.rv.viewer.sdk.decorder.model;

public class ScreenConnectionInfo {
	public long duration;
	public String pcname;
	public String ipAddress;
	public String pcos;
	
	public String getDurationtime() {
		String ret = "";
		ret = makeTimeFormat((System.currentTimeMillis()-duration)/1000);
		return ret;
	}
	
	private String makeTimeFormat(long secondtime) {
		long quotient = secondtime / 60;
		long second = secondtime % 60;
		long minute = quotient % 60;
		long hour = quotient / 60;
		
		String strRet = String.format("%02d:%02d:%02d", hour, minute, second);
		return strRet;
	}

}
