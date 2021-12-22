package com.rsupport.android.push;


import androidx.multidex.BuildConfig;

public interface IPushMessaging {
	public static final String RSUPPORT_PERMISSION_PUSH = BuildConfig.APPLICATION_ID + ".permission.MESSAGING";
	
	public static final String ACTION_PUSH_MESSAGING = BuildConfig.APPLICATION_ID + ".action.push.MESSAGING";
	
	public static final int TYPE_CONNECTED = 100;
	public static final int TYPE_MSG_ARRIVED = 200;
	public static final int TYPE_CONNECTION_LOST = 400;
	
	public static final int TYPE_DISCONNECTED = 900;
	public static final int TYPE_CONNECT_ERROR = 910;
	
	public static final String EXTRA_KEY_TYPE = "extra_key_type";
	public static final String EXTRA_KEY_VALUE = "extra_key_value";
	
}
