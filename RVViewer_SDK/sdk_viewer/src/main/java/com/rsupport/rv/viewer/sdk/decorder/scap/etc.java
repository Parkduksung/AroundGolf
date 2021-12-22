package com.rsupport.rv.viewer.sdk.decorder.scap;

public class etc {
	public static int gcm(int r1, int r2) {
		if (r1 == 0 || r2 == 0)
			return 1;

		int gcm;
		while ((gcm = r1 % r2) != 0) 
		{
			r1 = r2;
			r2 = gcm;
		}
		return r2;
	}
}
