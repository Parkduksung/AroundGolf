package com.rsupport.rv.viewer.sdk.decorder.model;


import com.rsupport.rv.viewer.sdk.decorder.viewer.channel.rcpX264VideoHeaderMsg;

public class X264Header {
	private int framepersecond = 20;
	private int width = 0;
	private int height = 0;
	private int frameWidth = 0;
	private int frameHeight = 0;
	private int rotation = 0;
	private int videoRatio = 100;
	private int videoQuality = 3;
	private int monitorIndex = 1; // 0 : 화면전체, 1 : 1번모니터
	private int valOption1 = 0;
	private int valOption2 = 0;
	private int valOption3 = 0;
	private int modelnameLen = 0;
	private byte[] modelname;
	private int sourceType = rcpX264VideoHeaderMsg.ENCODER_TYPE_OMX;
	private int isLandscape = 0;
	
	public X264Header() {
		// TODO Auto-generated constructor stub
	}
	
	public int getFrameWidth() {
		return frameWidth;
	}

	public void setFrameWidth(int frameWidth) {
		this.frameWidth = frameWidth;
	}

	public int getFrameHeight() {
		return frameHeight;
	}

	public void setFrameHeight(int frameHeight) {
		this.frameHeight = frameHeight;
	}

	public int getRotation() {
		return rotation;
	}

	public void setRotation(int rotation) {
		this.rotation = rotation;
	}

	public int getWidth() {
		return width;
	}
	
	public void setWidth(int width) {
		this.width = width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public void setHeight(int height) {
		this.height = height;
	}
	
	public int getVideoRatio() {
		return videoRatio;
	}
	
	public void setVideoRatio(int videoRatio) {
		this.videoRatio = videoRatio;
	}
	
	public int getFramepersecond() {
		return framepersecond;
	}
	
	public void setFramepersecond(int framepersecond) {
		this.framepersecond = framepersecond;
	}
	
	public int getVideoQuality() {
		return videoQuality;
	}
	
	public void setVideoQuality(int videoQuality) {
		this.videoQuality = videoQuality;
	}
	
	public int getMonitorIndex() {
		return monitorIndex;
	}
	
	public void setMonitorIndex(int monitorIndex) {
		this.monitorIndex = monitorIndex;
	}
	
	public int getValOption1() {
		return valOption1;
	}
	
	public void setValOption1(int valOption1) {
		this.valOption1 = valOption1;
	}
	
	public int getValOption2() {
		return valOption2;
	}
	
	public void setValOption2(int valOption2) {
		this.valOption2 = valOption2;
	}
	
	public int getValOption3() {
		return valOption3;
	}
	
	public void setValOption3(int valOption3) {
		this.valOption3 = valOption3;
	}

	public int getModelnameLen() {
		return modelnameLen;
	}

	public void setModelnameLen(int modelnameLen) {
		this.modelnameLen = modelnameLen;
	}

	public byte[] getModelname() {
		return modelname;
	}

	public void setModelname(byte[] modelname) {
		this.modelname = modelname;
	}

	public int getSourceType() {
		return sourceType;
	}

	public void setSourceType(int sourceType) {
		this.sourceType = sourceType;
	}

	public int getIsLandscape() {
		return isLandscape;
	}

	public void setIsLandscape(int isLandscape) {
		this.isLandscape = isLandscape;
	}
}
