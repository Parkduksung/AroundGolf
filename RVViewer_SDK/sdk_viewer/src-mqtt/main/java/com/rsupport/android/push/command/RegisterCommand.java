package com.rsupport.android.push.command;

import android.content.Context;

import com.rsupport.android.push.RSPushMessaging;


public class RegisterCommand extends AbstractPushCommand{
	private String registerID = null;
	private Context context = null;
	
	public RegisterCommand(Context context, String registerID) {
		this.context = context;
		this.registerID = registerID;
	}

	@Override
	protected boolean run() {
		return RSPushMessaging.getInstance(context).register(registerID);
	}

	@Override
	public int getType() {
		return TYPE_REGISTER;
	}
}
