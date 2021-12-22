package com.rsupport.rv.viewer.sdk.mqtt;

import static androidx.appcompat.app.AppCompatActivity.RESULT_OK;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.rsupport.android.push.IPushMessaging;
import com.rsupport.rv.viewer.sdk.common.Base64;
import com.rsupport.rv.viewer.sdk.common.ComConstant;
import com.rsupport.rv.viewer.sdk.common.RuntimeData;
import com.rsupport.rv.viewer.sdk.common.log.RLog;
import com.rsupport.rv.viewer.sdk.decorder.Converter;
import com.rsupport.rv.viewer.sdk.extension.ActivityExtKt;
import com.rsupport.rv.viewer.sdk.setting.Global;
import com.rsupport.rv.viewer.sdk.setting.GlobalStatic;
import com.rsupport.rv.viewer.sdk.util.PictureUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

import kotlin.Unit;


public class RSPushReceiver extends BroadcastReceiver implements IPushMessaging {
	private boolean isShowDialog = false;

	@Override
	public void onReceive(Context context, Intent intent) {
		if(intent.getAction().equals(ACTION_PUSH_MESSAGING)) {
			byte[] message = null;
			int type = intent.getIntExtra(EXTRA_KEY_TYPE, -1);
			RLog.d("RSPushReceiver type : " + type);
			switch(type) {
			case TYPE_MSG_ARRIVED:
				message = intent.getByteArrayExtra(EXTRA_KEY_VALUE);

				dec_bitcrosswise(message, 0);

				int index = 0;
				int sessionPacket = Converter.readIntLittleEndian(message, index);
				index += 4;
				int errorCode = Converter.readIntLittleEndian(message, index);
				index += 4;
				int baseLength = Converter.readIntLittleEndian(message, index);
				index += 4;

				byte[] datas = new byte[baseLength];
				System.arraycopy(message, index, datas, 0, baseLength);

//					message = Base64.decode(datas);
				message = android.util.Base64.decode(datas, android.util.Base64.DEFAULT);

				index = 0;

				if (message.length <= 3) {
					RLog.d("message size 0");
					return;
				}

				int commandKey = Converter.readIntLittleEndian(message, index);
				index += 4;
				int encordingDataLength = Converter.readIntLittleEndian(message, index);
				index += 4;

				RLog.d( "commandKey : " + commandKey);

				if (commandKey == ComConstant.VIEWER_GET_SCREENSHOT_REQUEST) {
					byte[] jpgBytes = Converter.readByte(message, index, encordingDataLength);
					RLog.d("ScreenShot", "responseData : " + jpgBytes);
					try {
						PictureUtils.INSTANCE.store(
								context.getApplicationContext(),
								jpgBytes,
								uri -> {
									if(Global.GetInstance().getCurrentActivity() != null){
										RLog.d("ScreenShot Success");
										(Global.GetInstance().getCurrentActivity()).setResult(RESULT_OK);
										ActivityExtKt.launchImageView(Global.GetInstance().getCurrentActivity(), uri);
									}
									GlobalStatic.unregisterPushMessaging(context, RuntimeData.getInstance().getLastAgentInfo().guid);
									return Unit.INSTANCE;
								}
						);
					} catch (Exception e) {
						RLog.e(e);
					}
				}

				RLog.d("TYPE_MSG_ARRIVED sessionPacket : " + sessionPacket);
				RLog.d("TYPE_MSG_ARRIVED errorCode : " + errorCode);
				RLog.d("TYPE_MSG_ARRIVED baseLength : " + baseLength);
				RLog.d("TYPE_MSG_ARRIVED commandKey : " + commandKey);
				RLog.d("TYPE_MSG_ARRIVED encordingDataLength : " + encordingDataLength);
				
				break;
			case TYPE_CONNECT_ERROR:
				RLog.v("TYPE_CONNECT_ERROR");
				break;
			case TYPE_CONNECTED:
				RLog.v("TYPE_CONNECTED");
				break;
			case TYPE_CONNECTION_LOST:
				message = intent.getByteArrayExtra(EXTRA_KEY_VALUE);
				RLog.v("TYPE_CONNECTION_LOST : " + new String(message));
				break;
			case TYPE_DISCONNECTED:
				RLog.v("TYPE_DISCONNECTED");
				break;
			}
		} else {
			RLog.e("not define action");
		}
	}
	
	public void dec_bitcrosswise(byte[] p, int offset) {
		// byte c = (byte)0xBE;
		byte c = (byte) 'r';

		for (int i = offset; i < p.length; ++i) {
			p[i] = (byte) (~(p[i]));
			p[i] = (byte) (p[i] ^ c);
		}
	}
	
	private String getDateTime() {
		String format ="yyyyMMdd_HHmmss";
		
		SimpleDateFormat df = new SimpleDateFormat(format);
		return df.format(new Date(System.currentTimeMillis()));
	}
	
	private byte[] getDecodeImageBytes(String responseData) {
		byte[] valueBytes = responseData.getBytes();
		char[] encodedChars = new char[valueBytes.length];
		
		for (int i = 0 ; i < valueBytes.length ; i++) {
			encodedChars[i] = Converter.getUnsignedChar(valueBytes[i]);
		}
		
		char[] decodeChars = new char[Base64.ap_base64decode_len(encodedChars)];
		Base64.ap_base64decode_binary(decodeChars, encodedChars);
		
		byte[] decodedBytes = new byte[decodeChars.length];
		for (int i = 0 ; i < decodeChars.length ; i++) {
			decodedBytes[i] = (byte) decodeChars[i];
		}
		return decodedBytes;
	}
}
