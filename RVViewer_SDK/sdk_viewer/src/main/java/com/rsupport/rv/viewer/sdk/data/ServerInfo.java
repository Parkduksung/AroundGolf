package com.rsupport.rv.viewer.sdk.data;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

import com.rsupport.rscommon.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by hyosang on 2016. 8. 17..
 */
public class ServerInfo implements Parcelable {
    public String daemonIp = "";
    public int daemonPort = -1;
    public String logKey = "";
    public List<HostPort> daemonList = new ArrayList<HostPort>();
    public HostPort currentDaemonData;
    public HostPort currentDaemonExplorer;

    public ServerInfo() {
    }

    @SuppressLint("NewApi")
    public ServerInfo(Parcel in) {
        daemonIp = in.readString();
        daemonPort = in.readInt();
        logKey = in.readString();
        Parcelable[] parcelables = in.readParcelableArray(HostPort.class.getClassLoader());
        Arrays.asList(parcelables).forEach(parcelable -> daemonList.add((HostPort) parcelable));
        currentDaemonData = in.readParcelable(HostPort.class.getClassLoader());
        currentDaemonExplorer = in.readParcelable(HostPort.class.getClassLoader());
    }

    public void logoutClear() {
        daemonIp = "";
        daemonPort = -1;
        logKey = "";
        daemonList.clear();
        currentDaemonData = null;
        currentDaemonExplorer = null;
    }

    public boolean addDaemons(String recvList) {
        if(!StringUtil.isEmpty(recvList)) {
            String [] daemons = recvList.split(",");

            for(String dm : daemons) {
                String [] hp = dm.split("\\|");

                int port = StringUtil.parseInt(hp[1], 0);

                if((port > 0) && (port <= 65535)) {
                    //valid port number
                    daemonList.add(new HostPort(hp[0], port));
                }
            }

            return true;
        }
        return false;
    }

    public List<HostPort> getDaemonList() {
        List<HostPort> list = new ArrayList<>();

        if(!StringUtil.isEmpty(daemonIp) && (daemonPort != -1)) {
            list.add(new HostPort(daemonIp, daemonPort));
        }

        list.addAll(daemonList);

        return list;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(daemonIp);
        dest.writeInt(daemonPort);
        dest.writeString(logKey);
        dest.writeParcelableArray(daemonList.toArray(new HostPort[daemonList.size()]), 0);
        dest.writeParcelable(currentDaemonData, 0);
        dest.writeParcelable(currentDaemonExplorer, 0);
    }

    public static final Creator<ServerInfo> CREATOR = new Creator<ServerInfo>() {
        @Override
        public ServerInfo createFromParcel(Parcel source) {
            return new ServerInfo(source);
        }

        @Override
        public ServerInfo[] newArray(int size) {
            return new ServerInfo[size];
        }
    };
}
