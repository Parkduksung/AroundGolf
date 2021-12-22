package com.rsupport.android.push.command;

import android.content.Context;

import com.rsupport.android.push.RSPushMessaging;


public class UnRegisterCommand extends AbstractPushCommand{
	private String registerID = null;
	private Context context = null;
	
	public UnRegisterCommand(Context context, String registerID) {
		this.context = context;
		this.registerID = registerID;
	}

	@Override
	protected boolean run() {
		return RSPushMessaging.getInstance(context).unregister(registerID);
	}

	@Override
	public int getType() {
		return TYPE_REGISTER;
	}
}
