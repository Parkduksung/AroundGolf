/**
 * @author kiyoung
 * 
 *  Porting base64.cpp to Java
 */

package com.rsupport.rv.viewer.sdk.control;

public class Base64 {
	
	//256 array size
	public static final char pr2six[] = {
	    64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64,
	    64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64,
	    64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 62, 64, 64, 64, 63,
	    52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 64, 64, 64, 64, 64, 64,
	    64,  0,  1,  2,  3,  4,  5,  6,  7,  8,  9, 10, 11, 12, 13, 14,
	    15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 64, 64, 64, 64, 64,
	    64, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40,
	    41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 64, 64, 64, 64, 64,
	    64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64,
	    64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64,
	    64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64,
	    64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64,
	    64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64,
	    64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64,
	    64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64,
	    64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64
	};

	public static int ap_base64decode_len(final char[] bufcoded) {
	    int nbytesdecoded;
	    final char[] bufin;
	    int nprbytes;
	    int bufinPos = 0;

	    bufin = bufcoded;
	    while (pr2six[bufin[bufinPos++]] <= 63 && bufin.length > bufinPos);

	    nprbytes = (bufinPos - 0) - 1;
	    nbytesdecoded = ((nprbytes + 3) / 4) * 3;

	    return nbytesdecoded + 1;
	}

	public static int ap_base64decode_binary(char[] bufplain, final char[] bufcoded) {
	    int nbytesdecoded;
	    char[] bufin;
	    char[] bufout;
	    int nprbytes;
	    int bufinPos = 0;

	    bufin = bufcoded;
	    while (pr2six[bufin[bufinPos++]] <= 63 && bufin.length > bufinPos);
	    nprbytes = (bufinPos - 0) - 1;
	    nbytesdecoded = ((nprbytes + 3) / 4) * 3;

	    bufout = bufplain;
	    bufin = bufcoded;
	    bufinPos = 0;
	    int bufoutPos = 0;

	    while (nprbytes > 4) {
	    	bufout[bufoutPos++] = (char)(pr2six[bufin[bufinPos]] << 2 | pr2six[bufin[bufinPos + 1]] >> 4);
	    	bufout[bufoutPos++] = (char)(pr2six[bufin[bufinPos + 1]] << 4 | pr2six[bufin[bufinPos + 2]] >> 2);
	    	bufout[bufoutPos++] = (char)(pr2six[bufin[bufinPos + 2]] << 6 | pr2six[bufin[bufinPos + 3]]);
	    	bufinPos += 4;
	    	nprbytes -= 4;
	    }

	    /* Note: (nprbytes == 1) would be an error, so just ingore that case */
	    if (nprbytes > 1) {
	    	bufout[bufoutPos++] = (char)(pr2six[bufin[bufinPos]] << 2 | pr2six[bufin[bufinPos + 1]] >> 4);
	    }
	    if (nprbytes > 2) {
	    	bufout[bufoutPos++] = (char)(pr2six[bufin[bufinPos + 1]] << 4 | pr2six[bufin[bufinPos + 2]] >> 2);
	    }
	    if (nprbytes > 3) {
	    	bufout[bufoutPos++] = (char)(pr2six[bufin[bufinPos + 2]] << 6 | pr2six[bufin[bufinPos + 3]]);
	    }

	    nbytesdecoded -= (4 - nprbytes) & 3;
	    return nbytesdecoded;
	}
	
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
	
}
