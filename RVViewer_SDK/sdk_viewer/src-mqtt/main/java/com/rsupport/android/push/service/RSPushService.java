package com.rsupport.android.push.service;

import android.app.Notification;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Binder;
import android.os.IBinder;
import android.telephony.TelephonyManager;

import com.rsupport.android.push.IRSBinder;
import com.rsupport.android.push.IRSPushService;
import com.rsupport.rv.viewer.sdk.common.log.RLog;
import com.rsupport.rv.viewer.sdk.setting.GlobalStatic;


import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.Collections;
import java.util.Hashtable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;



public class RSPushService extends Service implements IRSPushService, OnConnectLostListener{
	private final int CONNECT_LOST_TIME_SEC = 5;

	public final static String KEY_REGISTER_ID = "key_register_id";
	public final static String KEY_UN_REGISTER_ID = "key_un_register_id";

	public final static String KEY_SEND_TOPIC = "key_send_topic";
	public final static String KEY_SEND_MESSAGE = "key_send_message";
	public final static String KEY_SEND_TOPIC_BYTE = "key_send_topic_byte";
	public final static String KEY_SEND_MESSAGE_BYTE = "key_send_message_byte";

	private String TCP_SERVER_URI = "tcp://%s:%d";
	private String SSL_SERVER_URI = "ssl://%s:%d";
	private String TCP_CONNECT_TYPE = "0";
	private String serverURI = "";
	private int serverPort = 0;

	private Hashtable<String, Publisher> registerTable = null;
	private ExecutorService executorService = null;
	private ScheduledExecutorService scheduledExecutorService = null;
	private String networkInfo = null;

//	private WakeLock cpuWakeLock = null;
//	private WifiLock wifiWakeLock = null;
	
	private static String PREF_PRIVATE_PUSH_SERVER = "pref_private_push_server";
	private static String PREF_PRIVATE_PUSH_SERVER_ADDRESS = "pref_private_push_server_address";
	private static String  PREF_PRIVATE_PUSH_SERVER_PORT = "pref_private_push_server_port";

	@Override
	public void onCreate() {
		RLog.v("onCreate");
//		PowerManager powerManager = (PowerManager)getSystemService(POWER_SERVICE);
//		cpuWakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "RSPushCPUWakeLock");
//		cpuWakeLock.acquire();

//		WifiManager wifiManager = (WifiManager)getSystemService(WIFI_SERVICE);
//		wifiWakeLock = wifiManager.createWifiLock("RSPushWiFiWakeLock");
//		wifiWakeLock.setReferenceCounted(true);
//		wifiWakeLock.acquire();

		startForeground(0, new Notification());
		registerTable = new Hashtable<String, Publisher>();
		executorService = Executors.newCachedThreadPool(new ThreadFactory() {
			@Override
			public Thread newThread(Runnable r) {
				return new Thread(r, "RSPushService");
			}
		});

		scheduledExecutorService = Executors.newScheduledThreadPool(10, new ThreadFactory() {
			@Override
			public Thread newThread(Runnable r) {
				return new Thread(r, "RSPushService connection lost");
			}
		});

		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		registerReceiver(netReceiver, intentFilter);
		networkInfo = getNetworkInfo();
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// 강제종료후 다시 살아날때 복구.
		if(intent == null){
			RLog.i("onStartCommand by sticky");
			// 뷰어는 강제 종료후 다시 살아날 필요가 없음.
//			restoreServerInfo();
//			String[] registedList = new PushRegister(getApplicationContext()).getRegistedList();
//			if(registedList != null){
//				for(String topic : registedList){
//					register(this, topic);
//				}
//			}
		}
		else{
			RLog.v("onStartCommand");
		}
		return START_STICKY;
	}
	
	@Override
	public void setServerInfo(String privateAddress, int privatePort) {
		if(privateAddress != null && privateAddress.equals("") == false && privatePort != 0){
			RLog.d("setServerInfo success - privateAddress : " + privateAddress + ", privatePort : " + privatePort);
			clearPublisher();
			if (GlobalStatic.pushServerConnectType.equals(TCP_CONNECT_TYPE)) {
				serverURI = String.format(TCP_SERVER_URI, privateAddress, privatePort);
			} else {
				serverURI = String.format(SSL_SERVER_URI, privateAddress, privatePort);
			}
			serverPort = privatePort;
			saveServerInfo(privateAddress, serverPort);
		}
		else{
			RLog.e("setServerInfo fail -  privateAddress : " + privateAddress + ", privatePort : " + privatePort);
		}
	}

	private void saveServerInfo(String address, int port){
		SharedPreferences pref = getSharedPreferences(PREF_PRIVATE_PUSH_SERVER, MODE_PRIVATE);
		Editor e = pref.edit();
		e.putString(PREF_PRIVATE_PUSH_SERVER_ADDRESS, address);
		e.putInt(PREF_PRIVATE_PUSH_SERVER_PORT, port);
		e.commit();
	}

	private void restoreServerInfo(){
		SharedPreferences pref = getSharedPreferences(PREF_PRIVATE_PUSH_SERVER, MODE_PRIVATE);
		String ip = pref.getString(PREF_PRIVATE_PUSH_SERVER_ADDRESS, "");
		serverPort = pref.getInt(PREF_PRIVATE_PUSH_SERVER_PORT, 0);
		serverURI = String.format(TCP_SERVER_URI, ip, serverPort);
	}
	
	@Override
	public void onDestroy() {
		RLog.v("onDestroy");
		stopForeground(true);
//		if(cpuWakeLock != null){
//			cpuWakeLock.release();
//			cpuWakeLock = null;
//		}
//
//		if(wifiWakeLock != null){
//			wifiWakeLock.release();
//			wifiWakeLock = null;
//		}

		if(executorService != null){
			executorService.shutdown();
			executorService = null;
		}

		if(scheduledExecutorService != null){
			scheduledExecutorService.shutdown();
			scheduledExecutorService = null;
		}

		unregisterReceiver(netReceiver);		

		super.onDestroy();
	}

	@Override
	public IBinder onBind(Intent intent) {
		RLog.i("called onBind.");
		if(intent.hasExtra(EXTRA_KEY_SERVER_URL) == true){
			String privateAddress = intent.getStringExtra(EXTRA_KEY_SERVER_URL);
			int privatePort = intent.getIntExtra(EXTRA_KEY_SERVER_PORT, 0);
			setServerInfo(privateAddress, privatePort);
		}
		else if(intent.hasExtra(KEY_REGISTER_ID) == true){
			String topicFilter = intent.getStringExtra(KEY_REGISTER_ID);
			register(getApplicationContext(), topicFilter);
		}
		else if(intent.hasExtra(KEY_UN_REGISTER_ID) == true){
			String topicFilter = intent.getStringExtra(KEY_UN_REGISTER_ID);
			unregister(getApplicationContext(), topicFilter);
		}
		else if(intent.hasExtra(KEY_SEND_TOPIC) == true){
			String topicFilter = intent.getStringExtra(KEY_SEND_TOPIC);
			String message = intent.getStringExtra(KEY_SEND_MESSAGE);
			pushNotification(topicFilter, message);
		}
		else if(intent.hasExtra(KEY_SEND_TOPIC_BYTE) == true){
			String topicFilter = intent.getStringExtra(KEY_SEND_TOPIC_BYTE);
			byte[] message = intent.getByteArrayExtra(KEY_SEND_MESSAGE_BYTE);
			pushNotification(topicFilter, message);
		}
		return new RSBinder();
	}
	
	private void clearPublisher(){
		new PushRegister(this).unRegistAll();
		if(registerTable != null){
			synchronized (registerTable) {
				for(Publisher client : Collections.list(registerTable.elements())){
					if(client != null){
						client.disconnect();
					}
				}
				registerTable.clear();
			}
		}
	}

	@Override
	public void register(Context context, String topicFilter) {
		Publisher client = new Publisher(context, serverURI, topicFilter, MqttClient.generateClientId());
		client.setOnConnectLostListener(this);
		String identityKey = client.getIdentityKey();
		if(registerTable != null){
			synchronized (registerTable) {
				if(registerTable.containsKey(identityKey) == false){
					RLog.i("register");
					new PushRegister(context).register(topicFilter);
					registerTable.put(identityKey, client);
					executorService.execute(client);
				}
				else{
					RLog.w("already register " + identityKey);
				}
			}
		}
		else{
			RLog.w("register error. : " + identityKey);
			client.disconnect();
			client.close();
			client = null;
		}
	}

	@Override
	public void unregister(Context context, String topicFilter) {
		if(registerTable != null){
			synchronized (registerTable) {
				new PushRegister(context).unRegist(topicFilter);
				String key = new Identity().create(context, topicFilter);
				Publisher client = registerTable.remove(key);
				if(client != null){
					client.disconnect();
					client.close();
				}
			}
		}
	}

	@Override
	public void pushNotification(final String topic, final String message) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					// testcode
					String id = MqttClient.generateClientId();
					RLog.v("send serverURI : " + serverURI + ", id : " + id);
					MqttClient client = new MqttClient(serverURI, id, new MemoryPersistence());
					client.setCallback(new MqttCallback() {
						@Override
						public void connectionLost(Throwable cause) {
							RLog.i("send connectionLost");
						}

						@Override
						public void messageArrived(String topic,
								MqttMessage message) throws Exception {
							RLog.i("send messageArrived");
						}

						@Override
						public void deliveryComplete(IMqttDeliveryToken token) {
							RLog.i("send deliveryComplete");

						}
					});
					MqttConnectOptions options = new MqttConnectOptions();
					options.setUserName("90MpdA516uPP79ldh6SNow==-R9SqEAHmNh7Sy5VQ685G7Q==");
					client.connect(options);
					MqttTopic temperatureTopic = client.getTopic(topic);
					MqttMessage mqttMessage = new MqttMessage(message.getBytes());
					mqttMessage.setQos(2);
					temperatureTopic.publish(mqttMessage);
					client.disconnect();
				} catch (Exception e) {
					RLog.e(e);
				}
			}
		}).start();
	}

	@Override
	public void connectLost(String identityKey) {
		if(getNetworkInfo() != null){
			synchronized (registerTable) {
				Publisher client = registerTable.get(identityKey);
				if(client != null){
					scheduledExecutorService.schedule(client, CONNECT_LOST_TIME_SEC, TimeUnit.SECONDS);
				}
			}
		}
	}

	private String getIPAddress(WifiManager wifiManager){
		if(wifiManager != null){
			WifiInfo wifiInfo = wifiManager.getConnectionInfo();
			int ipAddress = wifiInfo.getIpAddress();
			if(ipAddress > 0){
				String resultIp = String.format("%d.%d.%d.%d", (ipAddress & 0xff), (ipAddress >> 8 & 0xff), (ipAddress >> 16 & 0xff), (ipAddress >> 24 & 0xff));
				if(resultIp != null){
					return resultIp;
				}
			}
		}
		return "";
	}

	private String getNetworkInfo(){
		ConnectivityManager connectivityManager = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected() == true) {
			if(networkInfo.getType() == ConnectivityManager.TYPE_WIFI ||
					networkInfo.getType() == ConnectivityManager.TYPE_ETHERNET){
				String netInfo = "";
				WifiManager wifiManager = (WifiManager)getApplicationContext().getSystemService(Context.WIFI_SERVICE);
				netInfo += getIPAddress(wifiManager);
				netInfo += wifiManager.getConnectionInfo().getSSID();
				netInfo += wifiManager.getConnectionInfo().getBSSID();
				return netInfo;
			}
			else if(networkInfo.getType() == ConnectivityManager.TYPE_MOBILE){
				TelephonyManager telephonyManager = (TelephonyManager)getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
				if(telephonyManager != null){
					int dataState = telephonyManager.getDataState();
					if(dataState != telephonyManager.DATA_DISCONNECTED){
						return String.valueOf(dataState);
					}
				}
			}
		} 
		return null;
	}

	private BroadcastReceiver netReceiver = new BroadcastReceiver() {

		public void onReceive(Context context, Intent intent) {	
			String action = intent.getAction();
			if(action.equals(ConnectivityManager.CONNECTIVITY_ACTION) == true){
				String netInfo = getNetworkInfo();
				RLog.v("netInfo : " + netInfo + ", this : " + networkInfo);
				// disconnect
				if(netInfo == null){
					networkInfo = null;
				}
				else if(netInfo != null && netInfo.equals(networkInfo) == false){
					RLog.v("#enter publisher");
					synchronized (registerTable) {
						networkInfo = netInfo;
						for(Publisher client : Collections.list(registerTable.elements())){
							executorService.execute(client);
						}
					}
					RLog.v("#exit publisher");
				}
			}
		}
	};

	class RSBinder extends Binder implements IRSBinder{
		@Override
		public IRSPushService getRSPushService() {
			return RSPushService.this;
		}
	}

	@Override
	public void pushNotification(final String topic,final byte[] message) {
		// TODO Auto-generated method stub
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					// testcode
					String id = MqttClient.generateClientId();
					RLog.v("send serverURI : " + serverURI + ", id : " + id);
					MqttClient client = new MqttClient(serverURI, id, new MemoryPersistence());
					client.setCallback(new MqttCallback() {
						@Override
						public void connectionLost(Throwable cause) {
							RLog.i("send connectionLost");
						}

						@Override
						public void messageArrived(String topic,
								MqttMessage message) throws Exception {
							RLog.i("send messageArrived");
						}

						@Override
						public void deliveryComplete(IMqttDeliveryToken token) {
							RLog.i("send deliveryComplete");

						}
					});
					MqttConnectOptions options = new MqttConnectOptions();
					options.setUserName("90MpdA516uPP79ldh6SNow==-R9SqEAHmNh7Sy5VQ685G7Q==");
					client.connect(options);
					MqttTopic temperatureTopic = client.getTopic(topic);
					MqttMessage mqttMessage = new MqttMessage(message);
					mqttMessage.setQos(2);
					temperatureTopic.publish(mqttMessage);
					client.disconnect();
				} catch (Exception e) {
					RLog.e(e);
				}
			}
		}).start();
	}
}
