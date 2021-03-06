package com.rsupport.rv.viewer.sdk.common;

import com.rsupport.rv.viewer.sdk.util.NetworkUtil;

import java.io.Serializable;

/**
 * simple GUID (Globally Unique ID) implementation. A GUID is composed of two
 * parts: 1. The IP-Address of the local machine. 2. A java.rmi.server.UID
 * 
 * @author Thomas Mahler
 * @version $Id: GUID.java,v 1.1 2008/05/23 02:11:52 chinyh Exp $
 */
public class GUID implements Serializable {
	static final long serialVersionUID = -6163239155380515945L;

	/**
	 * holds the hostname of the local machine.
	 */
	private static String localIPAddress;

	/**
	 * String representation of the GUID
	 */
	private String guid;

	/**
	 * compute the local IP-Address
	 */
	static {
//		try {
//			localIPAddress = InetAddress.getLocalHost().getHostAddress();
			localIPAddress = NetworkUtil.getLocalIP();
//		} catch (UnknownHostException e) {
//			localIPAddress = "localhost";
//		}
	}

	private String alphabetGUID() {
		String strRet = "";
	    for (int i = 1; i <= 10; i++) {
	      char ch = (char) ((Math.random() * 26) + 65);
	      strRet += ch;
	    }
	    return strRet;		
	}
	
     /**
	 * public no args constructor.
	 */
//	public GUID() {
////		UID uid = new UID();
//		StringBuffer buf = new StringBuffer();
//		buf.append(localIPAddress);
//		buf.append(":");
////		buf.append(alphabetGUID());
//		buf.append(alphabetGUID());
////		buf.append(uid.toString());
//		guid = buf.toString();
//	}
	
	public GUID() {
//		UID uid = new UID();
		StringBuffer buf = new StringBuffer();		
		
		localIPAddress = localIPAddress.replaceAll("\\W", "");
		
		buf.append(localIPAddress);
		buf.append("-");
//		buf.append(alphabetGUID());
		buf.append(alphabetGUID());
//		buf.append(uid.toString());
		guid = buf.toString();
	}

	/**
	 * public constructor. The caller is responsible to feed a globally unique
	 * String into the theGuidString parameter
	 * 
	 * @param theGuidString
	 *            a globally unique String
	 */
	public GUID(String theGuidString) {
		guid = theGuidString;
	}

	/**
	 * returns the String representation of the GUID
	 */
	public String toString() {
		return guid;
	}

	/**
	 * @see Object#equals(Object)
	 */
	public boolean equals(Object obj) {
		if (obj instanceof GUID) {
			if (guid.equals(((GUID) obj).guid)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @see Object#hashCode()
	 */
	public int hashCode() {
		return guid.hashCode();
	}

}
