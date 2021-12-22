package com.rsupport.rv.viewer.sdk.decorder.model;

public class WebAccessInfo {
	
	/*
	 * 원격화면잠금 강제 옵션(원격화면잠금이 활성화 상태일때만 적용된다) [0 : 원격화면잠금 체크박스를 활성화(디폴트),  1 : 원격화면잠금 체크박스를 비활성화함]
	 */
	public static final String FORCEDSCREENLOCKSET_DISABLE = "0";
	public static final String FORCEDSCREENLOCKSET_ENABLE  = "1";
	
	public String remoteport;
	public String displayname;
	public String result;
	public String ssl;
	public String logkey;
	public String id;
	public String remoteip;
	public String data;
	public String rvoemopts;
	public long	  locktime;
	public String agentMac;
	public String serverrec = "";
	public String forcedscreenlockset = FORCEDSCREENLOCKSET_DISABLE;
	public String locktype;
	public long	  fileLimitSize;
	public String useAutoScreenLock;
	public String useAutoScreenType;
	public String screenLockType;
}
