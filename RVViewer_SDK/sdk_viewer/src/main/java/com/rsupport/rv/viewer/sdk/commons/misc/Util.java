/********************************************************************************
*       ______   _____    __    __ _____   _____   _____    ______  _______
*      / ___  | / ____|  / /   / // __  | / ___ | / __  |  / ___  ||___  __|
*     / /__/ / | |____  / /   / // /  | |/ /  | |/ /  | | / /__/ /    / /
*    / ___  |  |____  |/ /   / // /__/ // /__/ / | |  | |/ ___  |    / /
*   / /   | |   ____| || |__/ //  ____//  ____/  | |_/ // /   | |   / /
*  /_/    |_|  |_____/ |_____//__/    /__/       |____//_/    |_|  /_/
*
********************************************************************************
*
* Copyright (c) 2013 RSUPPORT Co., Ltd. All Rights Reserved.
*
* NOTICE:  All information contained herein is, and remains the property
* of RSUPPORT Company Limited and its suppliers, if any. The intellectual
* and technical concepts contained herein are proprietary to RSUPPORT
* Company Limited and its suppliers and are protected by trade secret
* or copyright law. Dissemination of this information or reproduction
* of this material is strictly forbidden unless prior written permission
* is obtained from RSUPPORT Company Limited.
*
* FileName: Util.java
* Author  : "kyeom@rsupport.com"
* Date    : 2013. 7. 30.
* Purpose : 다양한 곳에서 반복적으로 호출되는 속성을 가진 메소드들을 모아둠.
*
* [History]
*
* 2013. 7. 30. -Initialize
*
*/
package com.rsupport.rv.viewer.sdk.commons.misc;

import android.content.Context;
import android.view.Display;
import android.view.WindowManager;



import com.rsupport.rv.viewer.sdk.common.log.RLog;

import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author "kyeom@rsupport.com"
 *
 */
public class Util {
	
	public static String DEFAULT_LOCALE = "UTF-8";

	private Util() {
		throw new AssertionError();
	}
	
	public static String getText(byte[] stringBytes, int len) {
		String number = null;
		try {
			number = new String(stringBytes, 0, len, DEFAULT_LOCALE);
		} catch (UnsupportedEncodingException e) {
			RLog.e(e);
		}
		return number;
	}
	
	public static byte[] getBytes(String text) {
		byte[] phoneNumber = null;
		try {
			phoneNumber = text.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			RLog.e(e);
		}
		return phoneNumber;
	}
	
	public static Display getDisplay(Context context)  {
		WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		return display;
	}
	
	public static String makeUUID() {
		
		return UUID.randomUUID().toString();
	}
	
	 public static String getIPAddress(boolean useIPv4) {
		 try {
			 List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
	         for (NetworkInterface intf : interfaces) {
	        	 List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
	        	 for (InetAddress addr : addrs) {
	        		 if (!addr.isLoopbackAddress()) {
	        			 String sAddr = addr.getHostAddress().toUpperCase(new Locale(DEFAULT_LOCALE));
//	        			 boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
	        			 if (useIPv4) {
	        				 if (true)
	        					 return sAddr;
	        				 } else {
//	        					 if (!isIPv4) {
//	        						 int delim = sAddr.indexOf('%'); // drop ip6 port suffix
//	        						 return delim<0 ? sAddr : sAddr.substring(0, delim);
//	        					 }
	        				 }
	                  }
	              }
	          }
	     } catch (Exception ex) { } // for now eat exceptions
	     return "";
	 }
	 
	public static boolean isValidUrl(String url) {
		String regex = "https?://[-\\w.]+(:\\d+)?(/([\\w/_.]*)?)?";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(url);
		return matcher.matches();
	}

}
