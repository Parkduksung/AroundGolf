package com.rsupport.rv.viewer.sdk.control;


import com.rsupport.rv.viewer.sdk.common.log.RLog;

import java.util.ResourceBundle;

public class ResourceBundleTest {
//	public static void main(String[] args) {
//		ResourceBundle rb = ResourceBundle.getBundle("hello", Locale.getDefault());
//		greeting(rb);
//		
//		rb = ResourceBundle.getBundle("hello", Locale.US);
//		greeting(rb);
//	}
	
	private static void greeting(ResourceBundle resourceBundle) {
		RLog.d(resourceBundle.getString("hello.morning"));
		RLog.d(resourceBundle.getString("hello.afternoon"));
		RLog.d(resourceBundle.getString("hello.evening"));
	}
}
