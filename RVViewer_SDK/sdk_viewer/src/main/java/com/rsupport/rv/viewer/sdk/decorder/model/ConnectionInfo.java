package com.rsupport.rv.viewer.sdk.decorder.model;

public class ConnectionInfo {
	
	public static final String RVOEMTYPE_EXPLORER_DISABLE 	= "7";
	
	public static final String APPFTP_ENABLE  				= "1";
	public static final String APPFTP_DISABLE 				= "0";

	public static final String AUTH_TYPE_DEFAULT  				= "0";
	public static final String AUTH_TYPE_OTP 					= "1";
	public static final String AUTH_TYPE_WINDOWS 				= "2";
	public static final String AUTH_TYPE_NOAUTH                 = "3";
	public static final String AUTH_TYPE_DONT_USE 				= "0";
	public static final String AUTH_TYPE_USE 					= "1";

	public static final String AUTH_TWOFACTOR_ENABLE  				= "1";
	public static final String AUTH_TWOFACTOR_DISABLE 				= "0";
	
	public String retcode = "";
	public String webprotocol = "";
	public String webserver = "";
	public String webserverport = "";
}
