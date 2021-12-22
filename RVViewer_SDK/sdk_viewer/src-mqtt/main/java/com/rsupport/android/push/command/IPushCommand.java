package com.rsupport.android.push.command;

public interface IPushCommand {
	public int getType();
	public int getCurrentRetryCount();
	public boolean execute();
}
