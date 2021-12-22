package com.rsupport.rv.viewer.sdk.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by hyosang on 2016. 8. 11..
 */
public class UserInfoVO implements Parcelable {
    public String loginId = "";
    public String loginPass = "";
    public String loginCorpId = "";
    public String agentLoginId = "";
    public String agentLoginPass = "";
    public String userEmail = "";
    public boolean isAdminRole = false;

    public UserInfoVO() {

    }

    public UserInfoVO(Parcel in) {
        loginId = in.readString();
        loginPass = in.readString();
        loginCorpId = in.readString();
        agentLoginId = in.readString();
        agentLoginPass = in.readString();
        userEmail = in.readString();
        isAdminRole = (in.readInt() == 1);
    }

    public void setAgentLoginData(String id, String pw) {
        this.agentLoginId = id;
        this.agentLoginPass = pw;
    }

    public void logoutClear() {
        loginId = "";
        loginPass = "";
        loginCorpId = "";
        agentLoginId = "";
        agentLoginPass = "";
        userEmail = "";
        isAdminRole = false;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(loginId);
        dest.writeString(loginPass);
        dest.writeString(loginCorpId);
        dest.writeString(agentLoginId);
        dest.writeString(agentLoginPass);
        dest.writeString(userEmail);
        dest.writeInt(isAdminRole ? 1 : 0);
    }

    public static final Creator<UserInfoVO> CREATOR = new Creator<UserInfoVO>() {
        @Override
        public UserInfoVO createFromParcel(Parcel source) {
            return new UserInfoVO(source);
        }

        @Override
        public UserInfoVO[] newArray(int size) {
            return new UserInfoVO[size];
        }
    };
}
