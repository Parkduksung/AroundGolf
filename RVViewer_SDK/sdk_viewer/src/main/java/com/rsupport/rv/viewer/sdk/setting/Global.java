package com.rsupport.rv.viewer.sdk.setting;

import android.graphics.Bitmap;

import androidx.appcompat.app.AppCompatActivity;


import com.rsupport.rv.viewer.sdk.decorder.model.MobileSystemInfo;
import com.rsupport.rv.viewer.sdk.decorder.model.ScreenConnectionInfo;
import com.rsupport.rv.viewer.sdk.decorder.model.SystemInfo;
import com.rsupport.rv.viewer.sdk.decorder.viewer.channel.CRCVDataChannel;
import com.rsupport.rv.viewer.sdk.decorder.viewer.channel.CRCVScreenChannel;
import com.rsupport.rv.viewer.sdk.decorder.viewer.channel.rcpResolutionMsg;
import com.rsupport.rv.viewer.sdk.ui.IScreenController;
import com.rsupport.rv.viewer.sdk.ui.ScreenCanvasMobileActivity;
import com.rsupport.rv.viewer.sdk.ui.SendAction;


public class Global {
	
	/** Singleton **/
	private static Global m_global = null;
	
	private SystemInfo systemInfo = null;
	private MobileSystemInfo mobileSysInfo = null;
	private ScreenConnectionInfo screenConnectInfo = new ScreenConnectionInfo();
	private Bitmap bitmap;
	private IScreenController screenController = null;
	private SendAction sendAction = null;

	private CRCVDataChannel dataChannel;
	private CRCVScreenChannel screenChannel;
	private RemoteMonitorInfo remoteMonitorInfo;
	private rcpResolutionMsg resolutionMsg;
	private AppCompatActivity currentActivity = null;

	private ScreenCanvasMobileActivity mobileViewer;

	public Global() {
		setSystemInfo(new SystemInfo());
		setMobileSysInfo(new MobileSystemInfo());
	}
	
	public static void CreateInstance() {
		m_global = new Global();
	}

	public synchronized static Global GetInstance() {
		if (m_global == null) {
			CreateInstance();
		}
		return m_global;
	}

	public CRCVDataChannel getDataChannel() {
		return dataChannel;
	}

	public void setDataChannel(CRCVDataChannel dataChannel) {
		this.dataChannel = dataChannel;
	}

	public CRCVScreenChannel getScreenChannel() {
		return screenChannel;
	}

	public void setScreenChannel(CRCVScreenChannel screenChannel) {
		this.screenChannel = screenChannel;
	}

	public RemoteMonitorInfo getRemoteMonitorInfo() {
		return remoteMonitorInfo;
	}

	public void setRemoteMonitorInfo(RemoteMonitorInfo remoteMonitorInfo) {
		this.remoteMonitorInfo = remoteMonitorInfo;
	}

	public rcpResolutionMsg getResolutionMsg() {
		return resolutionMsg;
	}

	public void setResolutionMsg(rcpResolutionMsg resolutionMsg) {
		this.resolutionMsg = resolutionMsg;
	}

	public AppCompatActivity getCurrentActivity() {
		return currentActivity;
	}

	public void setCurrentActivity(AppCompatActivity currentActivity) {
		this.currentActivity = currentActivity;
	}

	public ScreenCanvasMobileActivity getMobileViewer() {
		return mobileViewer;
	}

	public void setMobileViewer(ScreenCanvasMobileActivity mobileViewer) {
		this.mobileViewer = mobileViewer;
	}

	public SystemInfo getSystemInfo() {
		return systemInfo;
	}

	public void setSystemInfo(SystemInfo systemInfo) {
		this.systemInfo = systemInfo;
	}

	public MobileSystemInfo getMobileSysInfo() {
		return mobileSysInfo;
	}

	public void setMobileSysInfo(MobileSystemInfo mobileSysInfo) {
		this.mobileSysInfo = mobileSysInfo;
	}

	public ScreenConnectionInfo getScreenConnectInfo() {
		return screenConnectInfo;
	}

	public void setScreenConnectInfo(ScreenConnectionInfo screenConnectInfo) {
		this.screenConnectInfo = screenConnectInfo;
	}

	public Bitmap getBitmap() {
		return bitmap;
	}

	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}

	public IScreenController getScreenController() {
		return screenController;
	}

	public void setScreenController(IScreenController screenController) {
		this.screenController = screenController;
	}

	public SendAction getSendAction() {
		return sendAction;
	}

	public void setSendAction(SendAction sendAction) {
		this.sendAction = sendAction;
	}
}
