/******************************************************************************
*       ______   _____    __    __ _____   _____   _____    ______  _______
*      / ___  | / ____|  / /   / // __  | / ___ | / __  |  / ___  ||___  __|
*     / /__/ / | |____  / /   / // /  | |/ /  | |/ /  | | / /__/ /    / /
*    / ___  |  |____  |/ /   / // /__/ // /__/ / | |  | |/ ___  |    / /
*   / /   | |   ____| || |__/ //  ____//  ____/  | |_/ // /   | |   / /
*  /_/    |_|  |_____/ |_____//__/    /__/       |____//_/    |_|  /_/
*
********************************************************************************
*
* Copyright (c) 2012 RSUPPORT Co., Ltd. All Rights Reserved.
*
* NOTICE:  All information contained herein is, and remains the property
* of RSUPPORT Company Limited and its suppliers, if any. The intellectual
* and technical concepts contained herein are proprietary to RSUPPORT
* Company Limited and its suppliers and are protected by trade secret
* or copyright law. Dissemination of this information or reproduction
* of this material is strictly forbidden unless prior written permission
* is obtained from RSUPPORT Company Limited.
*
* FileName: RLog.java
* Author  : kwcho
* Date    : 2012. 12. 21.오후 3:39:14
* Purpose : print of android log
*
* [History]
 */
package com.rsupport.rv.viewer.sdk.common.log;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RLog {
	private static final long LOG_EXPIRE = 1 * 24 * 60 * 60 * 1000;		//1 day
	private static StringBuffer stringBuffer = new StringBuffer();
	private static String tag = "RV6.0";
	
	public static final int LEVEL_FULL 		= -1;
	public static final int LEVEL_VERVOSE 	= 0;
	public static final int LEVEL_DEBUG 	= 1;
	public static final int LEVEL_INFO 		= 2;
	public static final int LEVEL_WARN 		= 3;
	public static final int LEVEL_ERROR 	= 4;
	public static final int LEVEL_NONE		= 99;
	
	private static int LEVEL = LEVEL_FULL;

	private static Process fullLogProcess = null;
	private static Process filteredLogProcess = null;
	
	protected RLog(){}
	
	public static void setLevel(int level){
		RLog.LEVEL = level;
	}
	
	public static void setTag(String tag2) {
		RLog.tag = tag2;
	}
	
	public static void v(String message){
		print(LEVEL_VERVOSE, null, message);
	}
	public static void d(String message){
		print(LEVEL_DEBUG, null, message);
	}
	public static void i(String message){
		print(LEVEL_INFO, null, message);
	}
	public static void w(String message){
		print(LEVEL_WARN, null, message);
	}
	public static void e(String message){
		print(LEVEL_ERROR, null, message);
	}
	
	public static void v(Exception exception){
		print(LEVEL_VERVOSE, null, getExceptionMessage(exception));
	}
	
	public static void d(Exception exception){
		print(LEVEL_DEBUG, null, getExceptionMessage(exception));
	}
	
	public static void i(Exception exception){
		print(LEVEL_INFO, null, getExceptionMessage(exception));
	}
	
	public static void w(Exception exception){
		print(LEVEL_WARN, null, getExceptionMessage(exception));
	}
	
	public static void e(Exception exception){
		print(LEVEL_ERROR, null, getExceptionMessage(exception));
	}
	
	public static void v(int message){
		print(LEVEL_VERVOSE, null, String.valueOf(message));
	}
	
	public static void d(int message){
		print(LEVEL_DEBUG, null, String.valueOf(message));
	}
	
	public static void i(int message){
		print(LEVEL_INFO, null, String.valueOf(message));
	}
	
	public static void w(int message){
		print(LEVEL_WARN, null, String.valueOf(message));
	}
	
	public static void e(int message){
		print(LEVEL_ERROR, null, String.valueOf(message));
	}
	
	public static void v(String className, String message){
		print(LEVEL_VERVOSE, className, message);
	}
	public static void d(String className, String message){
		print(LEVEL_DEBUG, className, message);
	}
	public static void i(String className, String message){
		print(LEVEL_INFO, className, message);
	}
	public static void w(String className, String message){
		print(LEVEL_WARN, className, message);
	}
	public static void e(String className, String message){
		print(LEVEL_ERROR, className, message);
	}
	
	public static void v(String className, Exception exception){
		print(LEVEL_VERVOSE, className, getExceptionMessage(exception));
	}
	
	public static void d(String className, Exception exception){
		print(LEVEL_DEBUG, className, getExceptionMessage(exception));
	}
	
	public static void i(String className, Exception exception){
		print(LEVEL_INFO, className, getExceptionMessage(exception));
	}
	
	public static void w(String className, Exception exception){
		print(LEVEL_WARN, className, getExceptionMessage(exception));
	}
	
	public static void e(String className, Exception exception){
		print(LEVEL_ERROR, className, getExceptionMessage(exception));
	}
	
	public static void v(String className, int message){
		print(LEVEL_VERVOSE, className, String.valueOf(message));
	}
	
	public static void d(String className, int message){
		print(LEVEL_DEBUG, className, String.valueOf(message));
	}
	
	public static void i(String className, int message){
		print(LEVEL_INFO, className, String.valueOf(message));
	}
	
	public static void w(String className, int message){
		print(LEVEL_WARN, className, String.valueOf(message));
	}
	
	public static void e(String className, int message){
		print(LEVEL_ERROR, className, String.valueOf(message));
	}

	private synchronized static void print(int level, String className, String message){
		if(level < RLog.LEVEL) return;
		stringBuffer.setLength(0);
		StackTraceElement[] elements = new Throwable().getStackTrace();
		
		StackTraceElement ste = null;
		if(elements != null && elements.length > 2){
			ste = elements[2];
		}
		
		switch(level){
		case LEVEL_VERVOSE:
			stringBuffer.append("[VERV_]");
			break;
		case LEVEL_DEBUG:
			stringBuffer.append("[DEBUG]");
			break;
		case LEVEL_INFO:
			stringBuffer.append("[INFO_]");
			break;
		case LEVEL_WARN:
			stringBuffer.append("[WARN_]");
			break;
		case LEVEL_ERROR:
			stringBuffer.append("[ERROR]");
			break;
		}
		
		if(className == null || className.equals("") == true){
			className = ste.getFileName();
		}
		
		if(ste != null){
			stringBuffer.append("(").append(ste.getFileName()).append(", ").append(ste.getLineNumber()).append(") ").append(message);
		}else{
			stringBuffer.append("(").append("unknown").append(", ").append("-1").append(") ").append(message);
		}
		
		switch(level){
		case LEVEL_VERVOSE:
			Log.v(tag, stringBuffer.toString());
			break;
		case LEVEL_DEBUG:
			Log.d(tag, stringBuffer.toString());
			break;
		case LEVEL_INFO:
			Log.i(tag, stringBuffer.toString());
			break;
		case LEVEL_WARN:
			Log.w(tag, stringBuffer.toString());
			break;
		case LEVEL_ERROR:
			Log.e(tag, stringBuffer.toString());
			break;
		}
		
	}
	
	private static String getExceptionMessage(Exception e){
		e.printStackTrace();
		return Log.getStackTraceString(e);
	}

	private static File getLogRoot() {
		File extDir = Environment.getExternalStorageDirectory();
		File logDir = new File(extDir, "RemoteViewLog");

		return logDir;
	}

	public static void deleteOldLogs() {
		deleteRecursive(getLogRoot());
	}

	private static boolean deleteRecursive(File dir) {
		boolean emptyDir = true;
		try {
			if (dir.exists()) {
				for (File f : dir.listFiles()) {
					if (".".equals(f.getName()) || "..".equals(f.getName())) {
						continue;
					} else {
						if (f.isDirectory()) {
							if (deleteRecursive(f)) {
								//delete dir
								f.delete();
							} else {
								emptyDir = false;
							}
						} else {
							if ((System.currentTimeMillis() - f.lastModified()) > LOG_EXPIRE) {
								f.delete();
								RLog.i("Delete log: " + f.getAbsolutePath());
							} else {
								emptyDir = false;
							}
						}
					}
				}
			}
		}catch(NullPointerException e) {
			RLog.w(e);
		}
		return emptyDir;
	}

	public static boolean startSaveLog() {
		if((filteredLogProcess == null) && (fullLogProcess == null)) {
			try {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				Date now = new Date();

				File logDir = getLogRoot();
				File todayDir = new File(logDir, sdf.format(now));
				if(!todayDir.exists() || !todayDir.isDirectory()) {
					todayDir.mkdirs();
				}

				RLog.d("Log dir: " + todayDir.getAbsolutePath());

				sdf = new SimpleDateFormat("HH_mm_ss");
				String fullLogFile = String.format("%s/%s_full.log", todayDir.getAbsolutePath(), sdf.format(now));
				String filteredFile = String.format("%s/%s_filtered.log", todayDir.getAbsolutePath(), sdf.format(now));
				//단말에 따라 (혹은 버전에 따라?) 필터를 주지 않아도 자기 프로세스의 로그만 접근할 수 있는 경우가 있음.

				fullLogProcess = Runtime.getRuntime().exec(new String[]{"logcat", "-v", "threadtime", "-f", fullLogFile});
				filteredLogProcess = Runtime.getRuntime().exec(new String[]{"logcat", "-s", "-v", "threadtime", "-f", filteredFile, "RV6.0:V", "AndroidRuntime:V"});

				return true;
			}catch(IOException e) {
				e.printStackTrace();
			}
		}else {
			RLog.d("Logging process is running..");
		}

		return false;
	}
}

