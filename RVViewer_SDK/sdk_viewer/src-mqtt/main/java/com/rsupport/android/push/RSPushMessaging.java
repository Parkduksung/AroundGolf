package com.rsupport.android.push;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.rsupport.android.push.command.IPushCommand;
import com.rsupport.android.push.command.RegisterCommand;
import com.rsupport.android.push.command.SetServerInfoCommand;
import com.rsupport.android.push.command.UnRegisterCommand;
import com.rsupport.android.push.service.CommandExecutor;
import com.rsupport.android.push.service.RSPushService;
import com.rsupport.rv.viewer.sdk.common.log.RLog;


public class RSPushMessaging {
	private final int BIND_TIME_OUT =500;
	private static RSPushMessaging instance = null;
	private Context context = null;
	private IRSBinder binderService = null;
	private boolean isBinded = false;
	private Object lockObject = null;
	private CommandExecutor commandExecutor = null;
	
	private RSPushMessaging(Context context){
		lockObject = new Object();
		Intent service = new Intent(context, RSPushService.class);
		context.startService(service);
		setContext(context);
		commandExecutor = new CommandExecutor();
	}
	
	public synchronized static RSPushMessaging getInstance(Context context){
		if(instance == null){
			instance = new RSPushMessaging(context);
		}
		return instance;
	}

	private void setContext(Context context){
		this.context = context;
	}
	
	private void retryCommandExecute(IPushCommand command){
		RLog.i("retryCommandExecute : " + command.getType());
		commandExecutor.execute(command);
	}
			
	public boolean register(String registerID){
		RLog.d("PUSH REGISTER: " + registerID);
		synchronized (lockObject) {
			if(isBinded == false && binderService == null){
				Intent intent = new Intent(context, RSPushService.class);
				intent.putExtra(RSPushService.KEY_REGISTER_ID, registerID);
				isBinded = context.bindService(intent, connection, Context.BIND_AUTO_CREATE);
				RLog.i("register bindService : " + isBinded);
				if(isBinded == false){
					retryCommandExecute(new RegisterCommand(context, registerID));
					return false;
				}
				return true;
			}
			else{
				if(waitForBind(BIND_TIME_OUT, "register") == true){
					binderService.getRSPushService().register(context, registerID);
					return true;
				}
				else{
					retryCommandExecute(new RegisterCommand(context, registerID));
					return false;
				}
			}
		}
	}
	
	private boolean waitForBind(int timeOut, String message){
		if(binderService != null){
			return true;
		}
		
		long startTime = System.currentTimeMillis();
		while((System.currentTimeMillis() - startTime) < timeOut){
			if(binderService != null){
				return true;
			}
		}
		RLog.w("waitForBind fail.("+message+")");
		return false;
	}
	
	public boolean unregister(String registerID){
		RLog.d("PUSH UNREGISTER: " + registerID);
		synchronized (lockObject) {
			if(isBinded == false && binderService == null){
				Intent intent = new Intent(context, RSPushService.class);
				intent.putExtra(RSPushService.KEY_UN_REGISTER_ID, registerID);
				isBinded = context.bindService(intent, connection, Context.BIND_AUTO_CREATE);
				RLog.i("unregister bindService : " + isBinded);
				if(isBinded == false){
					retryCommandExecute(new UnRegisterCommand(context, registerID));
					return false;
				}
				else{
					return true;
				}
			}
			else{
				if(waitForBind(BIND_TIME_OUT, "unregister") == true){
					binderService.getRSPushService().unregister(context, registerID);
					return true;
				}
				else{
					retryCommandExecute(new UnRegisterCommand(context, registerID));
					return false;
				}
			}
		}
	}

	public boolean setServerInfo(String privateAddress, int privatePort) {
		synchronized (lockObject) {
			if(isBinded == false && binderService == null){
				Intent service = new Intent(context, RSPushService.class);
				service.putExtra(RSPushService.EXTRA_KEY_SERVER_URL, privateAddress);
				service.putExtra(RSPushService.EXTRA_KEY_SERVER_PORT, privatePort);
				isBinded = context.bindService(service, connection, Context.BIND_AUTO_CREATE);
				RLog.i("setServerInfo bindService : " + isBinded);
				if(isBinded == false){
					retryCommandExecute(new SetServerInfoCommand(context, privateAddress, privatePort));
					return false;
				}
				return true;
			}
			else{
				if(waitForBind(BIND_TIME_OUT, "setServerInfo") == true){
//					binderService.getRSPushService().setServerInfo(privateAddress, privatePort);
					return true;
				}
				else{
					retryCommandExecute(new SetServerInfoCommand(context, privateAddress, privatePort));
					return false;
				}
			}
		}
	}
	
	public void send(String topic, String message) {
		synchronized (lockObject) {
			if(isBinded == false && binderService == null){
				Intent intent = new Intent(context, RSPushService.class);
				intent.putExtra(RSPushService.KEY_SEND_TOPIC, topic);
				intent.putExtra(RSPushService.KEY_SEND_MESSAGE, message);
				isBinded = context.bindService(intent, connection, Context.BIND_AUTO_CREATE);
			}
			else{
				binderService.getRSPushService().pushNotification(topic, message);
			}
		}
	}
	
	public void send(String topic, byte[] message) {
		synchronized (lockObject) {
			if(isBinded == false && binderService == null){
				Intent intent = new Intent(context, RSPushService.class);
				intent.putExtra(RSPushService.KEY_SEND_TOPIC_BYTE, topic);
				intent.putExtra(RSPushService.KEY_SEND_MESSAGE_BYTE, message);
				isBinded = context.bindService(intent, connection, Context.BIND_AUTO_CREATE);
			}
			else{
				binderService.getRSPushService().pushNotification(topic, message);
			}
		}
	}
	
	private ServiceConnection connection = new ServiceConnection(){
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			RLog.i("onServiceConnected");
			if(service instanceof IRSBinder){
				binderService = (IRSBinder)service;
			}
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			binderService = null;
			isBinded = false;
		}
	};
}
