package com.rsupport.rv.viewer.sdk.control;

public class StringEx {
	
	// 문자열(str)내 널문자를 제거해서 리턴해주는 메소드
	public static String RemoveNullChar(String str) {
		int pos = str.indexOf('\u0000');
		if(pos != -1) {
			str = str.substring(0, pos);
		}
		return str;
	}
	
	// 문자열 str에 대해서 널문자를 포함해서 byte[]로 반환해주는 메소드
	public static byte[] getBytes(String str) {
		int len = str.getBytes().length;
		len++;
		byte dst[] = new byte[len+1];
		System.arraycopy(str.getBytes(), 0, dst, 0, len-1);
		dst[len-1] = '\0'; // 널문자 대입
		return dst;		
	}
	
	
	//문자열(str)에서 old를 neo로 대체
	  public static String replaceString(String old, String neo, String str) {
	    if (str == null || str.length() == 0) {
	      return str;
	    }
	    final int oldSize = old.length();
	    final int neoSize = neo.length();
	    final StringBuffer sb = new StringBuffer(str);
	    int begin = 0;
	    int idx = sb.indexOf(old, begin);
	    while (idx != -1) {
	      sb.delete(idx, idx + oldSize);
	      sb.insert(idx, neo);
	      begin = idx + neoSize;
	      if (begin < sb.length()) {
	        idx = sb.indexOf(old, begin);
	      } else {
	        idx = -1;
	      }
	    }
	    return sb.toString();
	  } 


}
