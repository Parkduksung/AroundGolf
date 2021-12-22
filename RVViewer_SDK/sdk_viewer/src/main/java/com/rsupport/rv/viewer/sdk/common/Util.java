package com.rsupport.rv.viewer.sdk.common;

import android.app.ActivityManager;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Message;
import android.os.StatFs;
import android.provider.Settings;
import android.text.format.DateUtils;
import android.webkit.MimeTypeMap;

import com.rsupport.rv.viewer.sdk.common.log.RLog;
import com.rsupport.rv.viewer.sdk.setting.Global;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.StringTokenizer;



public class Util {
	
	private static final String tag = "Util";
	private static Context mContext;
	
	public static void setContext(Context context) {
		mContext = context;
	}
	
	public static String getString(byte[] data) {
		String text = null;
		try {
			text = new String(data, 0, data.length-2, "UTF-16LE").replaceAll("\r\n", "\n");//"UTF-16LE").replaceAll("\r\n", "\n");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		return text.trim();
	}
	
	/**
	 * MFC 멀티바이트 프로젝트 시 유니코드 문자열이 Encoding은 ms949 방식
	 * @author kim kun seok (kskim@rsupport.com)
	 * */
	public static byte[] getByteEncoding(String text, String charname)
	{
		byte[] textBytes = null, textBytesContainingNull = null;
		try 
		{
			textBytes = text.getBytes(charname);
			textBytesContainingNull= new byte[textBytes.length + 2];
			System.arraycopy(textBytes, 0, textBytesContainingNull, 0, textBytes.length);
		} 
		catch (UnsupportedEncodingException e) 
		{
			e.printStackTrace();
			RLog.e(tag, "UnsupportedEncodingException : " + e.getLocalizedMessage());
		}
		
		textBytes = null;
		return textBytesContainingNull;
	}

	/**
	 * {@link #getByteEncoding(String, String)}을 지정한 길이만큼 byte 배열을 채워서 반환.
	 * 나머지 공간은 NULL문자로 채우며, 가득 차더라도 Null-terminate가 되어야 함.
	 * @param text a string
	 * @param charname Character encoding
	 * @param length Fixed length
     * @return length-size byte array
     */
	public static byte[] getByteEncoding(String text, String charname, int length) {
		byte [] bytes = new byte[length];
		int nullSize = 2;		//인코딩방식에 따라 Null문자가 차지하는 공간이 다른데, 일단 2바이트로 잡음.
		int maxByteSize = length - nullSize;

		try {
			byte [] encoded;
			for(;;) {
				encoded = text.getBytes(charname);

				if(encoded.length <= maxByteSize) {
					System.arraycopy(encoded, 0, bytes, 0, encoded.length);
					break;
				}else {
					//byte 변환 후 원하는 길이만 취하면 multi-byte 문자가 잘릴 수 있음...
					text = text.substring(0, text.length() - 1);
				}
			}
		}catch(UnsupportedEncodingException e) {
			RLog.e(tag, e);
		}

		//fill Null
		Arrays.fill(bytes, maxByteSize, bytes.length, (byte)0x00);

		return bytes;
	}

	/**
	 * Epoch time 을 WIN32 time으로 변경
	 * Win32 Date origin = 1/1/1601, Java Date Origin = 1/1/1970
	 * @param milliseconds
	 * @return
     */
	public static long epochTime2Win32Time(long milliseconds) {
		long time = milliseconds * 10000;
		time = time + (0x19DB1DED53E8000L);
		return time;
	}

	/**
	 * Win32 Time을 Epoch time으로 변경
	 * Win32 Date origin = 1/1/1601, Java Date Origin = 1/1/1970
	 * @param win32Time
	 * @return
	 */
	public static long win32Time2EpochTime(long win32Time) {
		long time = win32Time - 0x19DB1DED53E8000L;
		time = time / 10000;
		return time;
	}
	
	/**
	 * MFC 멀티바이트 프로젝트 시 유니코드 문자열이 Encoding은 ms949 방식 그러므로 디버딩을 위해서 필요함
	 * @author kim kun seok (kskim@rsupport.com)
	 * */
	public static String getStringDecoding(byte[] data, String charname)
	{
		String text = null;
		try {
			text = new String(data, 0, data.length, charname);
				
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		return text.trim();
	}
	
	public static String getMultiByteString(byte[] data, boolean trim) {
		String text = null;
		try {
			if(trim)
				text = new String(data, 0, data.length, "UTF-8").replaceAll("\r\n", "\n");//"UTF-16LE").replaceAll("\r\n", "\n");
			else
				text = new String(data, 0, data.length, "UTF-8");
				
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		return text.trim();
	}
	
	public static String getStringNotToIncludeNull(byte[] data) {
		String text = null;
		try {
			text = new String(data, 0, data.length-2, "UTF-16LE");//.replaceAll("\r\n", "\n");//"UTF-16LE").replaceAll("\r\n", "\n");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		return text;
	}
	
	public static String getStringToIncludeNull(byte[] data) {
		String text = null;
		try {
			text = new String(data, 0, data.length, "UTF-16LE");//.replaceAll("\r\n", "\n");//"UTF-16LE").replaceAll("\r\n", "\n");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		return text;
	}

	public static String getStringFromWCHAR(byte[] data, int index, int length) {
		//WCHAR = Windows wide character = UTF-16LE
		int len = 0;

		while((len < length) && !((data[index+len] == 0x00) && (data[index+len+1] == 0x00))) {
			len += 2;
		}

		return new String(data, index, len, Charset.forName("UTF-16LE"));
	}
	
	public static String getString(int id) {
		return mContext.getResources().getString(id);
	}
	
	public static Message getHandlerMessage(String text) {
    	Message msg = Message.obtain();
    	msg.obj = text;
    	return msg;
    }
    
	public static Message getHandlerMessage(byte[] bytes) {
    	Message msg = Message.obtain();
    	msg.obj = bytes;
    	return msg;
    }
	
	public static String getEncodeString(String target) {
		return Base64.encodeBytes(target.getBytes());
	}
	
	public static String getDecodeString(String target) {
		try {
			return new String(Base64.decode(target));
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static boolean isFileExist(String file) 
	{
		try 
		{ 
			return (new File(file)).exists(); 
		} 
		catch (Exception e) {}
		return false;
	}
	
	public static long getWinOSDateFormat(long time)
	{
		time *= 10000;
		time += 0x19db1ded53e8000L;
		
		return time;
	}
	
	public static long getJavaDateFormat(long time)
	{
		time -= 0x19db1ded53e8000L;
		time /= 10000;
		
		return time;
	}
	
	public static String getText(byte[] data)
	{
		return getText(data, 0, data.length);
	}
	
	public static String getText(byte[] data, int offset, int len)
	{
		String text = null;
		
		if (len <= 0)
		{
			return null;
		}

		//Check NUL
		if (data[offset + (len - 1) - 1] == 0 && data[offset + (len - 1)] == 0) //'\0' (NUL)
		{
			len -= 2;
		}
		
		try 
		{
			text = new String(data, offset, len, "UTF-16LE").replaceAll("\r\n", "\n");
		} 
		catch (UnsupportedEncodingException e) 
		{
			e.printStackTrace();
			RLog.e(tag, "UnsupportedEncodingException : " + e.getLocalizedMessage());
		}
		
		return text;
	}
	
	//containing null
	public static byte[] getTextBytes(String text)
	{
		byte[] textBytes = null, textBytesContainingNull = null;
		try 
		{
			textBytes = text.getBytes("UTF-16LE");
			textBytesContainingNull= new byte[textBytes.length + 2];
			System.arraycopy(textBytes, 0, textBytesContainingNull, 0, textBytes.length);
		} 
		catch (UnsupportedEncodingException e) 
		{
			e.printStackTrace();
			RLog.e(tag, "UnsupportedEncodingException : " + e.getLocalizedMessage());
		}
		
		textBytes = null;
		return textBytesContainingNull;
	}
	
	// containing null
	public static byte[] getTextMultiBytes(String text) 
	{
		byte[] textBytes = null, textBytesContainingNull = null;
		try {
			textBytes = text.getBytes("UTF-8");
			textBytesContainingNull = new byte[textBytes.length + 2];
			System.arraycopy(textBytes, 0, textBytesContainingNull, 0,
					textBytes.length);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			RLog.e(tag,
					"UnsupportedEncodingException : " + e.getLocalizedMessage());
		}

		textBytes = null;
		return textBytesContainingNull;
	}
	
	/**
	 * Null을 포함하지 않는 텍스트 바이트가져오기
	 * @author kim kun seok (kskim@rsupport.com)
	 * */
	public static byte[] getTextBytesNotToIncludeNull(String text)
	{
		byte[] textBytes = null;//, textBytesContainingNull = null;
		try 
		{
			textBytes = text.getBytes("UTF-16LE");
			//textBytesContainingNull= new byte[textBytes.length + 2];
			//System.arraycopy(textBytes, 0, textBytesContainingNull, 0, textBytes.length);
		} 
		catch (UnsupportedEncodingException e) 
		{
			e.printStackTrace();
			RLog.e(tag, "UnsupportedEncodingException : " + e.getLocalizedMessage());
		}
		
		//textBytes = null;
		return textBytes;
	}
	
	public static int splitString(String originalStr, String delimeter, ArrayList<String> list)
	{
		if(originalStr == null || list == null || delimeter == null)
			return 0;
		
		if(originalStr.length() <=0 || delimeter.length() <=0)
			return 0;
		
		String token="";
		StringTokenizer st = new StringTokenizer(originalStr, delimeter);
		while(st.hasMoreElements())
		{
			token = st.nextToken();
			
			list.add(token);
		}
		
		st = null;
		
		return list.size();
	}
	
	public static boolean deleteFile(File file)
	{
		if (file.exists())
		{
			return file.delete();
		}
		return false;
	}
		
	public static boolean deleteFiles(File parentFile)
	{
		boolean ret = true;
		
		if (parentFile.isDirectory())
		{
			File[] listFiles = parentFile.listFiles();
			
			for (File childFile : listFiles)
			{
				if (childFile.isFile())
				{
					ret &= childFile.delete();
					
					RLog.i(tag, "delete_target_file : " + childFile.getAbsolutePath());
				}
				else //Directory
				{
					ret &= deleteFiles(childFile);
				}
			}
		}
		
		ret &= parentFile.delete();
		
		RLog.i(tag, "delete_target_file : " + parentFile.getAbsolutePath());
		
		return ret;
	}
	
	public static long getFileCountIncludedSubFolder(File parentFile)
	{
		long totalSize = 0;
				
		if (parentFile.isDirectory())
		{
			File[] listFiles = parentFile.listFiles();
			
			if(listFiles ==null)
			{
				RLog.i(tag, "listFiles is null(path:"+parentFile.getAbsolutePath()+")");
				return totalSize;
			}
			
			for (File childFile : listFiles)
			{
				if(childFile == null)
				{
					RLog.i(tag, "child file is null");
					continue;
				}
				if(childFile.isDirectory())
					totalSize += getFileCountIncludedSubFolder(childFile);
				else if(childFile.isFile())
					totalSize++;
			}
		}
		else
		{
			totalSize++;
		}
		return totalSize;
	}
	
	public static long getFileListIncludedSubFolder(ArrayList<File> arrayList, File parentFile)
	{
		long totalSize = 0;
		
		arrayList.add(parentFile);
		
		if (parentFile.isDirectory())
		{
			File[] listFiles = parentFile.listFiles();
			
			for (File childFile : listFiles)
			{
				totalSize += getFileListIncludedSubFolder(arrayList, childFile);
			}
		}
		else
		{
			totalSize += parentFile.length();
		}
		return totalSize;
	}
	
	public static long getFileListIncludedSubFolder(ArrayList<File> arrayList, File parentFile, boolean[] isCopyableResult)
	{
		long totalSize = 0;
		
		if (parentFile.isDirectory())
		{
			arrayList.add(parentFile);
			
			File[] listFiles = parentFile.listFiles();
			
			for (File childFile : listFiles)
			{
				totalSize += getFileListIncludedSubFolder(arrayList, childFile, isCopyableResult);
			}
		}
		else if (parentFile.isFile() && parentFile.canRead())
		{
			arrayList.add(parentFile);
			
			RLog.i(tag, "FileSize(Name) : " + parentFile.getAbsolutePath());
			RLog.i(tag, "FileSize(Len)  : " + parentFile.length());
			totalSize += parentFile.length();
		}
		else
		{
			arrayList.add(parentFile);
			
			RLog.i(tag, "FileSize(Name) : " + parentFile.getAbsolutePath());
			RLog.i(tag, "FileSize(Len)  : " + parentFile.length());
			totalSize += parentFile.length();
			
			isCopyableResult[0] = false;
			
			RLog.i(tag, "can not read : " + parentFile.getAbsolutePath());
		}
		return totalSize;
	}
	
	public static long getFileListIncludedSubFolder(ArrayList<File> arrayList, File parentFile, boolean isOnlyReadable)
	{
		long totalSize = 0;
		
		if (parentFile.isDirectory())
		{
			arrayList.add(parentFile);
			
			File[] listFiles = parentFile.listFiles();
			if(listFiles == null)
			{
				RLog.i(tag, "first listFiles is null("+parentFile.getAbsolutePath()+")");
				return 0;
			}
			
			for (File childFile : listFiles)
			{
				totalSize += getFileListIncludedSubFolder(arrayList, childFile, isOnlyReadable);
			}
		}
		else if (parentFile.isFile() && parentFile.canRead())
		{
			arrayList.add(parentFile);
			
			RLog.i(tag, "FileSize(Name) : " + parentFile.getAbsolutePath());
			RLog.i(tag, "FileSize(Len)  : " + parentFile.length());
			totalSize += parentFile.length();
		}
		else if (!isOnlyReadable)
		{
			arrayList.add(parentFile);
			
			RLog.i(tag, "FileSize(Name) : " + parentFile.getAbsolutePath());
			RLog.i(tag, "FileSize(Len)  : " + parentFile.length());
			totalSize += parentFile.length();
		}
		return totalSize;
	}
	
	public static boolean renameFile(String originalName, String changingName)
	{
		File originalFile = new File(originalName);
		File changingFile = new File(changingName);
		
		RLog.i(tag, "renameFile : originalFile_pull_path : " + originalFile.getAbsolutePath());
		RLog.i(tag, "renameFile : changingFile_pull_path : " + changingFile.getAbsolutePath());
		
		boolean ret = originalFile.renameTo(changingFile);
		
		originalFile = null; changingFile = null;
		
		return ret;
	}
	
	public static boolean makeFolder(String path)
	{
		RLog.i(tag, "makeFolder : " + path);
		
		File folder = new File(path);
		boolean ret = folder.mkdirs();
		folder = null;
		return ret;
	}
	
	public static File[] getFileList(String path, FileFilter fileFilter)
	{
		File[] fileList = null;
		try
		{
			File folder = new File(path);
			if (fileFilter == null) {	
				fileList = folder.listFiles();
			} else {
				fileList = folder.listFiles(fileFilter);
			}
		} 
		catch(SecurityException e)
		{
			e.printStackTrace();
			RLog.e(tag,"SecurityException : " + e.getLocalizedMessage());
		}
		return fileList;
	}
	
	public static boolean setAuthorityToFile(String fileName, boolean readable, boolean writable, boolean executable)
	{
		RLog.i(tag, "setAuthorityToFile : " + fileName);
		
		Process authProcess = null;
		
		int permission = 0;
		permission += (readable   ? 4 : 0);
		permission += (writable   ? 2 : 0);
		permission += (executable ? 1 : 0);
		
		String permissionText = String.valueOf(permission) + String.valueOf(permission) + String.valueOf(permission);
		
//		RLog.e(tag, "permission : " + permissionText);
		
		try 
		{
			authProcess = Runtime.getRuntime().exec("chmod " + permissionText + " " + fileName);
			authProcess.waitFor();
			
//			authProcess = Runtime.getRuntime().exec("chmod -R " + permissionText + " " + fileName);
//			authProcess.waitFor();
			
			return true;
		} 
		catch (Exception e)
		{
			e.printStackTrace();
			RLog.e(tag,"Runtime exception : " + e.getLocalizedMessage());
		}
		return false;
	}
	
	public static boolean linuxCmd(String cmd, boolean suNeeded) {
		OutputStream suos = null;
		try {
			Process sh = Runtime.getRuntime().exec(suNeeded ? "su" : cmd);
			if (suNeeded) {
				suos = sh.getOutputStream();
				writeCommand(suos, cmd);
				writeCommand(suos, "exit");
			}
		} catch (Exception e) {
			return false;
		}
		try {
			if (suos != null) {
				suos.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}

	private static void writeCommand(OutputStream os, String command) throws Exception {
		os.write((command + "\n").getBytes("ASCII"));
	}
	
	public static String formatSize(long value) 
	{
		String suffix = null;
	    
		final int KB = 1024;
	    final int MB = 1024 * KB;
		final int GB = 1024 * MB;
		
		long size = value;
		
		if(size >=0 && size < KB)
		{
			suffix = "B";
		}
		else if(size >=KB && size <MB)
		{
			suffix = "KB";
			size /=KB;
		}
		else if(size >=MB && size <GB)
		{
			suffix = "MB";
			size /=MB;
		}
		else if(size >=GB)
		{
			suffix = "GB";
			size /=GB;
		}
		else
		{
			suffix="";
			size = 0;
		}
//		if (size >= 1024) 
//	    {
//	    	suffix = "Kb";
//	    	size /= 1024;
//	    	if (size >= 1024) 
//	    	{
//	    		suffix = "Mb";
//	    		size /= 1024;
//	    	}
//	    }
	    
	    StringBuilder resultBuffer = new StringBuilder(Long.toString(size));
	    
	    int commaOffset = resultBuffer.length() - 3;
	    while (commaOffset > 0) 
	    {
	    	resultBuffer.insert(commaOffset, ',');
	    	commaOffset -= 3;
	    }
	    
	    if (suffix != null)
	    {
	    	resultBuffer.append(suffix);
	    }    
	    return resultBuffer.toString();
	}
	
	private static String linuxResultCmd(String cmd, boolean isNeedSu) {
		OutputStream suos = null;
		try {
			Process sh = null;
			if(isNeedSu == true){
				sh = Runtime.getRuntime().exec("su");
			}else{
				sh = Runtime.getRuntime().exec("sh");
			}
			suos = sh.getOutputStream();
			writeCommand(suos, cmd);
			writeCommand(suos, "exit");
			suos.flush();
			sh.waitFor();
			InputStream is = sh.getInputStream();
			if(is != null){
				byte[] buffer = new byte[is.available()];
				is.read(buffer);
				is.close();
				return new String(buffer);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			try{
				if (suos != null) {
					suos.close();
				}
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
/*********************************************************************************************************
 ********************************************  Android dedicated  ****************************************
 *********************************************************************************************************/
		
	public static boolean executeFile(String fileName)
	{
		String mimeType = getMimeType(fileName);
		
		RLog.i(tag, "executeFile : mimeType : " + mimeType);
		
		if (mimeType == null)
		{
			return false;
		}
		
		File file = new File(fileName);
		
//		Intent intent = new Intent();
//		intent.setAction(Intent.ACTION_VIEW);
//		intent.setDataAndType(Uri.fromFile(file), mimeType);
//		intent = Intent.createChooser(intent, "Open...");
//		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_VIEW);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setDataAndType(Uri.fromFile(file), mimeType);
		
		try
		{
			mContext.startActivity(intent);
			
			RLog.i(tag, "executeFile : startActivity : " + file.getAbsolutePath());
		} 
		catch (ActivityNotFoundException e)
		{
			RLog.e(tag, "ActivityNotFoundException : " + e.getLocalizedMessage());
			
			return false;
		}
		
		return true;
	}
	
	public static String getMimeType(String file)
	{
		String mimeType = null;
		
		//Fail to UpperCase extension file
//		String extension = MimeTypeMap.getFileExtensionFromUrl(file);
		String extension = file.substring(file.lastIndexOf(".") + 1, file.length()).toLowerCase();
		
		if (MimeTypeMap.getSingleton().hasExtension(extension))
		{
			mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
		}
		
		if (mimeType == null)
		{
			if (extension.equals("rmvb"))
			{
				mimeType = "video/*";
			}
			else if (extension.equals("ini"))
			{
				mimeType = "text/plain";
			}
		}
		return mimeType;
	}
	
	public static boolean isExternalStorageAvailable()
	{
		return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
	}
	
	public static String getExternalStorageAbsolutePath()
	{
		return Environment.getExternalStorageDirectory().getAbsolutePath();
	}
	
/*
 * http://www.androidsnippets.org/snippets/48/
 */
	public static long getAvailableExternalMemorySize() 
	{
		if(isExternalStorageAvailable()) 
		{
			File path = Environment.getExternalStorageDirectory();
			StatFs stat = new StatFs(path.getPath());
	    	long blockSize = stat.getBlockSize();
	    	long availableBlocks = stat.getAvailableBlocks();
	    	return availableBlocks * blockSize;
	    } 
	    else 
	    {
	    	return 0;    
	    }
	}
	    
	public static long getTotalExternalMemorySize() 
	{
		if(isExternalStorageAvailable()) 
		{
	    	File path = Environment.getExternalStorageDirectory();
	    	StatFs stat = new StatFs(path.getPath());
	    	long blockSize = stat.getBlockSize();
	    	long totalBlocks = stat.getBlockCount();
	    	return totalBlocks * blockSize;
	    } 
	    else 
	    {
	    	return 0;
	    }
	}
	
	public static long getAvailableInternalMemorySize() 
	{
		File path = Environment.getDataDirectory();
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long availableBlocks = stat.getAvailableBlocks();
		return availableBlocks * blockSize;
	}
	    
	public static long getTotalInternalMemorySize() 
	{
	    File path = Environment.getDataDirectory();
	    StatFs stat = new StatFs(path.getPath());
	    long blockSize = stat.getBlockSize();
	    long totalBlocks = stat.getBlockCount();
	    return totalBlocks * blockSize;
	}

	public static int dipToPixel(Context context, int dip){
		float scale = context.getResources().getDisplayMetrics().density;
		return (int)(dip * scale);
	}

	public static int pixelToDip(Context context, float pixel){
		float scale = context.getResources().getDisplayMetrics().density;
		return (int)(pixel / scale);
	}
	
	public synchronized static String getKey(HashMap<String, Object> map, String key, String defaultKey){
		if(map != null){
			Object obj = map.get(key);
			if(obj != null){
				return (String)obj;
			}
		}
		return defaultKey;
	}
	
	public synchronized static int getKey(HashMap<String, Object> map, String key, int defaultKey){
		if(map != null){
			Object obj = map.get(key);
			if(obj != null){
				return (Integer)obj;
			}
		}
		return defaultKey;
	}
	
	public synchronized static boolean getKey(HashMap<String, Object> map, String key, boolean defaultKey){
		if(map != null){
			Object obj = map.get(key);
			if(obj != null){
				return (((String)obj).equals("1"))? true:false;
			}
		}
		return defaultKey;
	}
	
	public static String getTime(long time){
		if(time > 0){
			return DateUtils.formatDateTime(mContext, time, DateUtils.FORMAT_SHOW_YEAR|DateUtils.FORMAT_ABBREV_ALL|DateUtils.LENGTH_MEDIUM|DateUtils.FORMAT_SHOW_TIME); 
		}
		return "";
	}
		
	private static void endProcess(ActivityManager am, String[] pkgs) {
		if (pkgs == null) return;
		for (String pkg : pkgs) {
			if (pkg == null) continue;
			am.restartPackage(pkg);
//			am.killBackgroundProcesses(pkg);
		}
	}
	
	public static boolean isHaveGraphicsPermission() {
		File graphicsFile = new File("/dev/graphics/fb0");
		if (graphicsFile.canRead()) {
			return true;
		}
		return false;
	}
	
	public static boolean isHaveUInputPermission() {
		File uInputFile = new File("/dev/uinput");
		if (uInputFile.canRead() && uInputFile.canWrite()) {
			return true;
		}
		return false;
	}
	
	public static void setPermissionToGraphicsAndInput() {	
		if (!isHaveGraphicsPermission() || !isHaveUInputPermission()) {
			/* 
			   The following code causes a stopping phenomenon at a particular terminal. 
			   because has been handled by a thread. 
			 */
			new Thread() {
				public void run() {
					Util.linuxCmd("chmod 666 /dev/graphics/fb0\n" + 
							      "chmod 666 /dev/uinput\n", 
							       true);
				}
			}.start();
		}
	}
	
	
	static class CommonTime {
		int low;
		int high;
	}

	public static final long EPOCH_DIFF = 11644473600000L;
//	public static CommonTime m_bufCT = new CommonTime();
//	
//	 // time은 jvm 환경에서 파일에 수정일자이며 low, high에 window환경과 호환될수 있도록 변환이 된다.
//	// 수정일자는 RCMP에서는 4byte low, 4byte high로 처리됨
//	private static CommonTime getCommonDateFormat(long time) 
//	{
//		time *= 10000;
//		time = time + (0x19db1ded53e8000L);
//		m_bufCT.low = (int) time;
//		m_bufCT.high = (int) (time >> 32);
//		return m_bufCT;
//	}
	 
    public static Date filetimeToDate(final int high, final int low)
    {
        final long filetime = ((long) high) << 32 | (low & 0xffffffffL);
        final long ms_since_16010101 = filetime / (1000 * 10);
        final long ms_since_19700101 = ms_since_16010101 - EPOCH_DIFF;
        return new Date(ms_since_19700101);
    }
    
    public static long filetimeToLong(final int high, final int low)
    {
    	final long filetime = ((long)high) << 32 | (low & 0xffffffffL);
    	final long ms_since_16010101 = filetime / (1000 * 10);
    	final long ms_since_19700101 = ms_since_16010101 - EPOCH_DIFF;
    	return ms_since_19700101;
    }
    
    public static long dateToFileTime(final Date date)
    {
        long ms_since_19700101 = date.getTime();
        long ms_since_16010101 = ms_since_19700101 + EPOCH_DIFF;
        return ms_since_16010101 * (1000 * 10);
    }
    
    // 여기에 설정된 키보드들은 setText를 허용한다.
    public static boolean isKeyboardInputType(ContentResolver cResolver) {
    	String currentKeyboard = Settings.Secure.getString(cResolver, Settings.Secure.DEFAULT_INPUT_METHOD);
    	RLog.d("Util", "TEST Keyboard Name : " + currentKeyboard);
    	if (currentKeyboard.contains("com.sec.android.inputmethod.iwnnime.japan") ||
    		currentKeyboard.contains("net.cdeguet.smartkeyboard") ||
    		currentKeyboard.contains("com.diotek.ime") /*||
    		currentKeyboard.contains("AtokInputMethodService") ||
    		currentKeyboard.contains("standardcommon.IWnnLanguageSwitcher")*/) {
    		return true;
    	}
    	
    	return false;
    }
    
//    public static String getKeyboardName() {
//    	return Settings.Secure.getString(Global.GetInstance().getPcViewer().getContentResolver(), Settings.Secure.DEFAULT_INPUT_METHOD);
//    }
}
