package com.rsupport.rv.viewer.sdk.decorder.model;

public class VRVDHeader {
	private int framewidth = 0;
	private int frameheight = 0;
	private int colorbit = 0;
	private int rotation = 0;
	private int deviceWidth = 0;
	private int deviceHeight = 0;
	
	public VRVDHeader() {
		// TODO Auto-generated constructor stub
	}
	
	public int getFrameWidth() {
		return framewidth;
	}
	
	public void setFrameWidth(int width) {
		this.framewidth = width;
	}
	
	public int getFrameHeight() {
		return frameheight;
	}
	
	public void setFrameHeight(int height) {
		this.frameheight = height;
	}

	public int getColorbit() {
		return colorbit;
	}

	public void setColorbit(int colorbit) {
		this.colorbit = colorbit;
	}

	public int getRotation() {
		return rotation;
	}

	public void setRotation(int rotation) {
		this.rotation = rotation;
	}

	public int getDeviceWidth() {
		return deviceWidth;
	}

	public void setDeviceWidth(int deviceWidth) {
		this.deviceWidth = deviceWidth;
	}

	public int getDeviceHeight() {
		return deviceHeight;
	}

	public void setDeviceHeight(int deviceHeight) {
		this.deviceHeight = deviceHeight;
	}
}
