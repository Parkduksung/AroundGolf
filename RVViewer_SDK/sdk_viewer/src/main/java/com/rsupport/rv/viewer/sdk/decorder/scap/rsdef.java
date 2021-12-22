package com.rsupport.rv.viewer.sdk.decorder.scap;

public class rsdef {

	// #define AlignN(val, unit)	((val+(unit)-1) & (~((unit)-1)))
	// #define Align4(val)			AlignN(val, 4)
	
	public static int AlignN(int val, int unit) {
		return (val + (unit) - 1) & (~((unit) - 1));
	}

	public static int Align4(int val) {
		return AlignN(val, 4);
	}
}
