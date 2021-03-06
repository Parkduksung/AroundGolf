package com.rsupport.android.push.command;

import android.content.Context;

import com.rsupport.android.push.RSPushMessaging;

public class SetServerInfoCommand extends AbstractPushCommand{
	private String ip = null;
	private int port = 0;
	private Context context = null;

	public SetServerInfoCommand(Context context, String ip, int port) {
		this.context = context;
		this.ip = ip;
		this.port = port;
	}

	@Override
	protected boolean run() {
		return RSPushMessaging.getInstance(context).setServerInfo(ip, port);
	}

	@Override
	public int getType() {
		return TYPE_REGISTER;
	}

}
