package com.rsupport.android.push.service;

import android.content.Context;
import android.content.Intent;

import com.rsupport.android.push.IPushMessaging;
import com.rsupport.rv.viewer.sdk.common.log.RLog;


import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class Publisher implements Runnable, MqttCallback, IPushMessaging{
	// second
	private final int KEEP_ALIVE = 60 * 20;
	private String identityKey = null;
	private String topicFilter = null; 
	private MqttClient client = null;
	private Context context = null;
	private OnConnectLostListener connectLostListener = null;

	public Publisher(Context context, String serverURI, String topicFilter, String clientId){
		this.context = context;
		this.topicFilter = topicFilter;
		this.identityKey= new Identity().create(context, topicFilter);
		try {
			client = new MqttClient(serverURI, clientId, new MemoryPersistence());
		} catch (MqttException e) {
			RLog.e(e);
		}
	}
	
	public void close(){
		if(client != null){
			try {
				client.close();
			} catch (Exception e) {
				RLog.e(e);
			}
			client = null;
		}
		
		context = null;
		connectLostListener = null;
		topicFilter = null;
		identityKey = null;
	}

	public void disconnect() {
		if(client != null){
			try {
				client.unsubscribe(topicFilter);
				client.disconnect();
			} catch (Exception e) {
				RLog.w(e);
			}
		}
		sendBroadCastRSPushReceiver(TYPE_DISCONNECTED);
	}

	public String getIdentityKey(){
		return identityKey;
	}

	@Override
	public void connectionLost(Throwable cause) {
		if(connectLostListener != null){
			connectLostListener.connectLost(getIdentityKey());
		}
		sendBroadCastRSPushReceiver(TYPE_CONNECTION_LOST, getIdentityKey().getBytes());
	}

	@Override
	public void messageArrived(String topic, MqttMessage message)
			throws Exception {
		sendBroadCastRSPushReceiver(TYPE_MSG_ARRIVED, message.getPayload());
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken token) {
		RLog.i("deliveryComplete do not thing");
	}

	@Override
	public void run() {
		try {
			RLog.v("Publisher try to connect("+topicFilter+")");
			synchronized (client) {
				RLog.v("client("+topicFilter+"): " + client.isConnected());
				if(client.isConnected() == false){
					MqttConnectOptions options = new MqttConnectOptions();
					options.setKeepAliveInterval(KEEP_ALIVE);
					options.setUserName("90MpdA516uPP79ldh6SNow==-R9SqEAHmNh7Sy5VQ685G7Q==");
					client.setCallback(this);
					client.connect(options);
					client.subscribe(topicFilter);
					sendBroadCastRSPushReceiver(TYPE_CONNECTED);
				}
			}
			RLog.v("Publisher end to connect");
		} catch (Exception e) {
			RLog.e(e);
			if(connectLostListener != null){
				connectLostListener.connectLost(getIdentityKey());
			}
			sendBroadCastRSPushReceiver(TYPE_CONNECT_ERROR);
		}
	}

	private void sendBroadCastRSPushReceiver(int type){
		sendBroadCastRSPushReceiver(type, null);
	}

	private void sendBroadCastRSPushReceiver(int type, byte[] message){
		Intent intent = new Intent();
		intent.setAction(ACTION_PUSH_MESSAGING);
		RLog.v("Publisher end to connect " + context.getPackageName());
		intent.addCategory("com.server.mqtt");
		intent.putExtra(EXTRA_KEY_TYPE, type);
		intent.putExtra(EXTRA_KEY_VALUE, message);
		context.sendBroadcast(intent, RSUPPORT_PERMISSION_PUSH);
	}

	public void setOnConnectLostListener(OnConnectLostListener connectLostListener) {
		this.connectLostListener = connectLostListener;
	}
}
