package com.rsupport.android.push;

import android.content.Context;

public interface IRSPushService {
	public static final String EXTRA_KEY_SERVER_URL = "extra_key_server_url";
	public static final String EXTRA_KEY_SERVER_PORT = "extra_key_server_port";
	
	public void register(Context context, String topicFilter);
	public void unregister(Context context, String topicFilter);
	public void pushNotification(String topic, String message);
	public void pushNotification(String topic, byte[] message);
	public void setServerInfo(String privateAddress, int privatePort);
}
