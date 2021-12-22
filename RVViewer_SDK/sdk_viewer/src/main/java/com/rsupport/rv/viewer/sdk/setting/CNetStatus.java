package com.rsupport.rv.viewer.sdk.setting;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;

import com.rsupport.rv.viewer.sdk.common.log.RLog;

public class CNetStatus {
	public static final int NET_TYPE_NONE = 0x00;
	public static final int NET_TYPE_WIFI = 0x01;
	public static final int NET_TYPE_WIFI_P2P = 0x02;
	public static final int NET_TYPE_3G = 0x03;
	public static final int NET_TYPE_WIBRO = 0x04;
    public static final int NET_TYPE_BLUETOOTH = 0x05;
	private static CNetStatus current = null;
			
	public static CNetStatus getInstance() {
		if (current == null)
			current = new CNetStatus();
		return current;
	}
	
	public boolean getWifiStatus(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		@SuppressLint("MissingPermission") NetworkInfo ni = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if (ni == null) return false;
		boolean isConn = ni.isConnected();
		return isConn;
	}
	
	public boolean get3GStatus(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		@SuppressLint("MissingPermission") NetworkInfo ni = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		if (ni == null) return false;
		boolean isConn = ni.isConnected();
		return isConn;
	}
	
	public boolean getWIBROStatus(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		@SuppressLint("MissingPermission") NetworkInfo ni = cm.getNetworkInfo(ConnectivityManager.TYPE_WIMAX);
		if (ni == null) return false;
		boolean isConn = ni.isConnected();
		return isConn;
	}

    public boolean getBluetoothStatus(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        @SuppressLint("MissingPermission") NetworkInfo ni = cm.getNetworkInfo(ConnectivityManager.TYPE_BLUETOOTH);
        if(ni == null) return false;
        return ni.isConnected();
    }
	
	public int getNetType(Context context) {
        int netType = CNetStatus.NET_TYPE_NONE;

		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			@SuppressLint("MissingPermission") Network[] networks = cm.getAllNetworks();

			NetworkInfo netWifi = null;
			NetworkInfo net3g = null;
            NetworkInfo netWibro = null;
            NetworkInfo netBt = null;

			for(Network network : networks) {
				@SuppressLint("MissingPermission") NetworkInfo networkInfo = cm.getNetworkInfo(network);

                if(networkInfo.getDetailedState() == NetworkInfo.DetailedState.CONNECTED) {
                    if(networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                        netWifi = networkInfo;
                    }else if(networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                        net3g = networkInfo;
                    }else if(networkInfo.getType() == ConnectivityManager.TYPE_WIMAX) {
                        netWibro = networkInfo;
                    }else if(networkInfo.getType() == ConnectivityManager.TYPE_BLUETOOTH) {
                        netBt = networkInfo;
                    }
                }

				RLog.d("NET TYPE = " + networkInfo.getTypeName() + ", " + networkInfo.getType() + ", " + networkInfo.getDetailedState());
			}

            if(netWifi != null) {
                netType = CNetStatus.NET_TYPE_WIFI;
            }else if(net3g != null) {
                netType = CNetStatus.NET_TYPE_3G;
            }else if(netWibro != null) {
                netType = CNetStatus.NET_TYPE_WIBRO;
            }else if(netBt != null) {
                netType = CNetStatus.NET_TYPE_BLUETOOTH;
            }
		}else {
            if (getWifiStatus(context)) {
                netType = CNetStatus.NET_TYPE_WIFI;
            } else if (get3GStatus(context)) {
                netType = CNetStatus.NET_TYPE_3G;
            } else if (getWIBROStatus(context)) {
                netType = CNetStatus.NET_TYPE_WIBRO;
            } else if (getBluetoothStatus(context)) {
                netType = CNetStatus.NET_TYPE_BLUETOOTH;
            }
        }
		
		return netType;
	}
}
