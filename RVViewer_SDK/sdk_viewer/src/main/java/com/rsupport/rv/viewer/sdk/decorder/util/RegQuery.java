package com.rsupport.rv.viewer.sdk.decorder.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;

public class RegQuery {
	
	private static final String REGQUERY_UTIL = "reg query ";
	private static final String REGQUERY_ADD = "reg add ";
	private static final String REGSTR_TOKEN = "REG_SZ";
	private static final String REGMULSTR_TOKEN = "REG_MULTI_SZ";
	private static final String REGDWORD_TOKEN = "REG_DWORD";
	
	private static final String PERSONAL_FOLDER_CMD = REGQUERY_UTIL + 
		"\"HKCU\\Software\\Microsoft\\Windows\\CurrentVersion\\" +
		"Explorer\\Shell Folders\" /v Personal";
	
	private static final String REMOTEVIEW_DEFFOLDER = REGQUERY_UTIL +
		"\"HKCU\\Software\\RSupport\\RemoteView Ver 4.0\\Settings\" /v DocPath";
	
	private static final String CPU_SPEED_CMD = REGQUERY_UTIL +
		"\"HKIM\\HARDWARE\\DESCRIPTION\\System\\CentralProcessor\\0\"" + " /v ~MHz";
	
	private static final String CPU_NAME_CMD = REGQUERY_UTIL + 
		"\"HKLM\\HARDWARE\\DESCRIPTION\\System\\CentralProcessor\\0\"" + " /v ProcessorNameString";
	
	public static String getCurrentUserPersonalFolderPath() {
		try {
			Process process = Runtime.getRuntime().exec(PERSONAL_FOLDER_CMD);
			StreamReader reader = new StreamReader(new InputStreamReader(process.getInputStream()));
			
			reader.start();
			process.waitFor();
			reader.join();
			
			String result = reader.getResult();
			int p = result.indexOf(REGSTR_TOKEN);
			
			if (p == -1)
				return null;
			
			return result.substring(p + REGSTR_TOKEN.length()).trim();
		} catch (Exception e) {
			return null;
		}
	}
	
	public static String getRegRsupportDefaultFolder() {
		try {
			Process process = Runtime.getRuntime().exec(REMOTEVIEW_DEFFOLDER);
			StreamReader reader = new StreamReader(new InputStreamReader(process.getInputStream()));
			
			reader.start();
			process.waitFor();
			reader.join();
			
			String result = reader.getResult();
			int p = result.indexOf(REGSTR_TOKEN);
			
			return result.substring(p + REGSTR_TOKEN.length()).trim();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static void setRegRsupportDefaultFolder() {
	}
	
	public static String getCPUSpeed() {
		try {
			Process process = Runtime.getRuntime().exec(CPU_SPEED_CMD);
			StreamReader reader = new StreamReader(new InputStreamReader(process.getInputStream()));
			
			reader.start();
			process.waitFor();
			reader.join();
			
			String result = reader.getResult();
			int p = result.indexOf(REGDWORD_TOKEN);
			
			if (p == -1 )
				return null;
			
			String temp = result.substring(p + REGDWORD_TOKEN.length()).trim();
			return Integer.toString((Integer.parseInt(temp.substring("0x".length()), 16) + 1));
		} catch (Exception e) {
			return null;
		}
	}
	
	public static String getCPUName() {
		try {
			Process process = Runtime.getRuntime().exec(CPU_NAME_CMD);
			StreamReader reader = new StreamReader(new InputStreamReader(process.getInputStream()));
			
			reader.start();
			process.waitFor();
			reader.join();
			
			String result = reader.getResult();
			int p = result.indexOf(REGSTR_TOKEN);
			
			if (p == -1)
				return null;
			
			return result.substring(p + REGSTR_TOKEN.length()).trim();
		} catch (Exception e) {
			return null; 
		}
	}
	
	static class StreamReader extends Thread {
		private InputStreamReader is;
		private BufferedReader br;
		private StringWriter sw;
		
		StreamReader(InputStreamReader is) {
			this.is = is;
			sw = new StringWriter();
		}
		
		public void run() {
			try {
				int c;
				while ((c = is.read()) != -1) sw.write(c);
			} catch(IOException e) { ; }
		}
		
		String getResult() {
			return sw.toString();
		}
	}
	
//	public static void main(String s[]) {
//		RLog.d("Personal directory : " + getCurrentUserPersonalFolderPath());
//		RLog.d("RSpport dir : " + getRegRsupportDefaultFolder());
//		RLog.d("CPU Name : " + getCPUName());
//		RLog.d("CPU Speed : " + getCPUSpeed() + " Mhz");
//	}
}
