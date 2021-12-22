package com.rsupport.rv.viewer.sdk.control;

//c/c++에서 제공되는 메모리함수들을 구현한 유틸리티 클래스 입니다.
public class Util {

    public static int memcmp(byte[] a, byte[] b) {
        if (a.length != b.length) {
            return a.length - b.length;
        }
        for (int i = 0; i < a.length; i++) {
            if (a[i] != b[i]) {
                return a[i] - b[i];
            }
        }
        return 0;
    }

    public static int memcmp(byte[] a, byte[] b, int len) {
        if (a.length <= len && b.length <= len) {
            if (a.length != b.length) {
                return a.length - b.length;
            }
        }
        for (int i = 0; i < len; i++) {
            if (a[i] != b[i]) {
                return a[i] - b[i];
            }
        }
        return 0;
    }

    public static int memcmp(byte[] a, int pos1, byte[] b, int pos2, int len) {
        int i = pos1;
        int j = pos2;
        for (int index = 0; index < len; index++, i++, j++) {
            if (a[i] != b[j]) {
                return a[i] - b[j];
            }
        }
        return 0;
    }

    // void *memcpy( void *dest, const void *src, size_t count );
    public static int memcpy(byte[] dest, int destPos, byte[] src, int srcPos,
        int count) {

        int n2 = Math.min(src.length, count);
        if (n2 > 0) {
            System.arraycopy(src, srcPos, dest, destPos, n2);
        }
        for (int i = n2; i < count; i++) {
            dest[destPos + i] = 0;
        }
        return n2;
    }

    //초기화
    public static void memset(int[] arr, int v) {
        for (int i = 0; i < arr.length; i++) {
            arr[i] = 0;
        }
    }

    //초기화
    public static void memset(byte[] arr, byte v) {
        for (int i = 0; i < arr.length; i++) {
            arr[i] = 0;
        }
    }

    //초기화.. (객체를 생성하지 않음..)
    public static void memset(Object[] arr, Object v) {
        for (int i = 0; i < arr.length; i++) {
            arr[i] = v;
        }
    }
    /*
	 * RemoteView Code Start
	 */
	public static final char basis_64[] = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".toCharArray();
	
	public static int ap_base64encode_binary(char[] encoded, char[] string, int len) {
	    int i;
	    char[] p;
	    int pos = 0;

	    p = encoded;
	    for (i = 0; i < len - 2; i += 3) {
			p[pos++] = basis_64[(string[i] >> 2) & 0x3F];
			p[pos++] = basis_64[((string[i] & 0x3) << 4) | ((int) (string[i + 1] & 0xF0) >> 4)];
			p[pos++] = basis_64[((string[i + 1] & 0xF) << 2) | ((int) (string[i + 2] & 0xC0) >> 6)];
			p[pos++] = basis_64[string[i + 2] & 0x3F];
	    }
	    if (i < len) {
	    	p[pos++] = basis_64[(string[i] >> 2) & 0x3F];
			if (i == (len - 1)) {
				p[pos++] = basis_64[((string[i] & 0x3) << 4)];
				p[pos++] = '=';
			} else {
				p[pos++] = basis_64[((string[i] & 0x3) << 4) | ((int) (string[i + 1] & 0xF0) >> 4)];
				p[pos++] = basis_64[((string[i + 1] & 0xF) << 2)];
			}
			p[pos++] = '=';
	    }
	    p[pos++] = '\0';
//	    return p - encoded;
	    return 0;
	}
	
	public static int ap_base64encode_len(int len) {
	    return ((len + 2) / 3 * 4) + 1;
	}
	/*
	 * RemoteView Code End 
	 */
}

