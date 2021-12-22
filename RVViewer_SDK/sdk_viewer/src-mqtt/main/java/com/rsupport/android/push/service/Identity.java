package com.rsupport.android.push.service;

import android.content.Context;

public class Identity {
	public String create(Context context, String topic){
		return context.getPackageName() + topic; 
	}
}
