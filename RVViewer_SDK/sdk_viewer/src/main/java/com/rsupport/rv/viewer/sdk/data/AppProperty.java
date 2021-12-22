package com.rsupport.rv.viewer.sdk.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.rsupport.rscommon.util.StringUtil;
import com.rsupport.rv.viewer.sdk.common.Define;
import com.rsupport.rv.viewer.sdk.constant.RouteType;
import com.rsupport.rv.viewer.sdk.data.response.AppInfoResponse;
import com.rsupport.rv.viewer.sdk.data.response.BaseResponse;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by hyosang on 2017. 8. 23..
 */

public class AppProperty implements Parcelable {
    private static final int SECURITY_TYPE_WEB_ID       = 0x0001;
    private static final int SECURITY_TYPE_WEB_PASS     = 0x0002;
    private static final int SECURITY_TYPE_AGENT_ID     = 0x0004;
    private static final int SECURITY_TYPE_AGENT_PASS   = 0x0008;

    private String userKey = "";
    private String idType = "";
    private int securityType = 0;
    private boolean newVersion = false;
    private boolean accountLock = false;
    private String waitLockTime = "00:00";
    private String rvOemType = "";
    private boolean appFtp = false;
    private int loginFailCount = 0;
    private int newNoticeSeq = 0;
    private int passwordLimitDays = -1;
    private boolean useHxEngine = false;
    private boolean useNewHxEngine = false;
    private boolean useTwoFactorAuth = false;
    private boolean showLicenseExpireNotify = false;
    private String licenseEndDt = "";
    private String webserverProductVersion = "";

    private String useQuickMenu = "";
    private int processExecTimeoutMinute = Define.DIRECT_EXEC_INPUT_LIMIT;
    private boolean webserver5x = true;
    private boolean isAdminRole = true;

    private String apiVersion = "";
    private String accessToken = "";
    private String refreshToken = "";

    private boolean useRVSE = false;

    private SolutionTarget solutionTarget = SolutionTarget.Standard;
    private boolean useSecurityCaution = false;
    private int applyFormUsageMaxHours = 12;

    private int twofactorAuthType = RouteType.TYPE_OTP;
    private String twofactorEmailSendUrl = "";
    private String twofactorEmailCheckUrl = "";
    private String email = "";

    public AppProperty() {

    }


    private AppProperty(Parcel in) {
        userKey = in.readString();
        idType = in.readString();
        securityType = in.readInt();
        newVersion = in.readInt() == 1;
        accountLock = in.readInt() == 1;
        waitLockTime = in.readString();
        rvOemType = in.readString();
        appFtp = in.readInt() == 1;
        loginFailCount = in.readInt();
        newNoticeSeq = in.readInt();
        passwordLimitDays = in.readInt();
        useHxEngine = in.readInt() == 1;
        useNewHxEngine = in.readInt() == 1;
        useTwoFactorAuth = in.readInt() == 1;
        showLicenseExpireNotify = in.readInt() == 1;
        licenseEndDt = in.readString();
        webserverProductVersion = in.readString();
        processExecTimeoutMinute = in.readInt();
        webserver5x = in.readInt() == 1;

        apiVersion = in.readString();
        accessToken = in.readString();
        refreshToken = in.readString();

        useRVSE = in.readInt() == 1;
        solutionTarget = SolutionTarget.fromString(in.readString());
        useSecurityCaution = in.readInt() == 1;

        applyFormUsageMaxHours = in.readInt();
        isAdminRole = in.readInt() == 1;
        useQuickMenu = in.readString();
        twofactorAuthType = in.readInt();
        twofactorEmailSendUrl = in.readString();
        twofactorEmailCheckUrl = in.readString();
        email = in.readString();
    }

    public AppProperty(AppInfoResponse response) {
        this.userKey = response.userKey;
        this.idType = response.idType;
        this.securityType = StringUtil.parseInt(response.securityType, 0);
        this.newVersion = (Define.PROP_ENABLE.equals(response.newVersion));
        this.accountLock = (Define.PROP_ENABLE.equals(response.accountLock));
        this.waitLockTime = response.waitLockTime;
        this.rvOemType = response.rvOemType;
        this.appFtp = (Define.PROP_ENABLE.equals(response.appFtp));
        this.loginFailCount = StringUtil.parseInt(response.loginFailCount, 0);
        this.newNoticeSeq = StringUtil.parseInt(response.newNoticeSeq, 0);
        this.passwordLimitDays = StringUtil.parseInt(response.passwordLimitDays, -1);
        this.useHxEngine = (Define.PROP_ENABLE.equals(response.useHxengine));
        this.useNewHxEngine = (Define.PROP_ENABLE.equals(response.useNewHxengine));
        this.useTwoFactorAuth = (Define.PROP_ENABLE.equals(response.useTwoFactorAuth));
        this.showLicenseExpireNotify = (Define.PROP_ENABLE.equals(response.licenseExpiredNotifyDay));
        this.licenseEndDt = response.licenseEndDt;
        this.webserverProductVersion = response.webserverProductVersion;
        this.processExecTimeoutMinute = StringUtil.parseInt(response.processExecTimeoutMinute, Define.DIRECT_EXEC_INPUT_LIMIT);
        this.isAdminRole = response.isAdminRole;

        if(!StringUtil.isEmpty(this.webserverProductVersion)) {
            //해당 필드가 내려오면 5x 서버는 아님
            this.webserver5x = false;
        }

        this.accessToken = response.accessToken;
        this.refreshToken = response.refreshToken;

        if(!StringUtil.isEmpty(response.apiVersion)) {
            this.apiVersion = response.apiVersion;
        }else {
            this.apiVersion = "";
        }

        this.useRVSE = "1".equals(response.useSecurityEdition);
        this.solutionTarget = SolutionTarget.fromString(response.securityEditionCustomDomain);
        this.useSecurityCaution = "true".equalsIgnoreCase(response.useApplyFormCaution);

        this.applyFormUsageMaxHours = StringUtil.parseInt(response.applyFormUsageMaxHours, 12);
        this.useQuickMenu = response.useQuickMenu;

        this.twofactorAuthType = StringUtil.parseInt(response.twofactorAuthType, 0);
        this.twofactorEmailSendUrl = response.twofactorEmailSendUrl;
        this.twofactorEmailCheckUrl = response.twofactorEmailCheckUrl;
        this.email = response.userEmail;
    }

    public void logoutClear() {
        userKey = "";
        idType = "";
        securityType = 0;
        newVersion = false;
        accountLock = false;
        waitLockTime = "00:00";
        rvOemType = "";
        appFtp = false;
        loginFailCount = 0;
        newNoticeSeq = 0;
        passwordLimitDays = -1;
        useHxEngine = false;
        useNewHxEngine = false;
        useTwoFactorAuth = false;
        showLicenseExpireNotify = false;
        licenseEndDt = "";
        webserverProductVersion = "";
        processExecTimeoutMinute = Define.DIRECT_EXEC_INPUT_LIMIT;
        webserver5x = true;

        apiVersion = "";
        accessToken = "";
        refreshToken = "";

        useRVSE = false;
        solutionTarget = SolutionTarget.Standard;
        useSecurityCaution = false;

        twofactorAuthType = 0;
        twofactorEmailSendUrl = "";
        twofactorEmailCheckUrl = "";
        email = "";
    }

    public boolean isShowLicenseExpireNotify() {
        return showLicenseExpireNotify;
    }

    public void resetLicenseNotify() {
        showLicenseExpireNotify = false;
    }

    public boolean isUseTwoFactorAuth() {
        return useTwoFactorAuth;
    }

    public boolean isUseHxEngine() {
        return useHxEngine;
    }

    public boolean isUseNewHxEngine() {
        return useNewHxEngine;
    }

    public int getPasswordLimitDays() {
        return passwordLimitDays;
    }

    public int getNewNoticeSeq() {
        return newNoticeSeq;
    }

    public int getLoginFailCount() {
        return loginFailCount;
    }

    public boolean isAccountLock() {
        return accountLock;
    }

    public int getApplyFormUsageMaxHours() {
        return applyFormUsageMaxHours;
    }

    public int getWaitLockTimeSec() {
        Pattern p = Pattern.compile("([0-9]{2}):([0-9]{2})");
        Matcher m = p.matcher(waitLockTime);
        if(m.find()) {
            int min = StringUtil.parseInt(m.group(1), 0);
            int sec = StringUtil.parseInt(m.group(2), 0);
            return (min * 60) + sec;
        }else {
        }

        return 0;
    }

    public boolean isAppFtp() {
        return appFtp;
    }

    public boolean isRvOemTypeExplorerDisabled() {
        return (BaseResponse.RVOEMTYPE_EXPLORER_DISABLE.equals(rvOemType));
    }

    public boolean isNewVersion() {
        return newVersion;
    }

    public boolean isSecurityEnable() {
        return (securityType != 0);
    }

    public boolean isClearWebId() {
        return ((securityType & SECURITY_TYPE_WEB_ID) != 0);
    }

    public boolean isClearWebPass() {
        return ((securityType & SECURITY_TYPE_WEB_PASS) != 0);
    }

    public boolean isClearAgentId() {
        return ((securityType & SECURITY_TYPE_AGENT_ID) != 0);
    }

    public boolean isClearAgentPass() {
        return ((securityType & SECURITY_TYPE_AGENT_PASS) != 0);
    }

    public String getIdType() {
        return idType;
    }

    public String getUserKey() {
        return userKey;
    }

    public String getWebserverProductVersion() {
        return webserverProductVersion;
    }

    public boolean isWebserver5x() {
        return webserver5x;
    }

    public int getProcessExecTimeoutMinute() {
        return processExecTimeoutMinute;
    }

    public String getApiVersion() {
        return apiVersion;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public String getLicenseEndDt() {
        return licenseEndDt;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public boolean isApiAuthByToken() {
        return ((accessToken != null) && (accessToken.length() > 0));
    }

    public boolean isUseRVSE() {
        return useRVSE;
    }

    public boolean isUseSecurityCaution() {
        return useSecurityCaution;
    }

    public boolean isAdminRole() {
        return isAdminRole;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userKey);
        dest.writeString(idType);
        dest.writeInt(securityType);
        dest.writeInt((newVersion ? 1 : 0));
        dest.writeInt((accountLock ? 1 : 0));
        dest.writeString(waitLockTime);
        dest.writeString(rvOemType);
        dest.writeInt((appFtp ? 1 : 0));
        dest.writeInt(loginFailCount);
        dest.writeInt(newNoticeSeq);
        dest.writeInt(passwordLimitDays);
        dest.writeInt((useHxEngine ? 1 : 0));
        dest.writeInt((useNewHxEngine ? 1 : 0));
        dest.writeInt((useTwoFactorAuth ? 1 : 0));
        dest.writeInt((showLicenseExpireNotify ? 1 : 0));
        dest.writeString(licenseEndDt);
        dest.writeString(webserverProductVersion);
        dest.writeInt(processExecTimeoutMinute);
        dest.writeInt((webserver5x ? 1 : 0));
        dest.writeString(apiVersion);
        dest.writeString(accessToken);
        dest.writeString(refreshToken);
        dest.writeInt((useRVSE ? 1 : 0));
        dest.writeString(solutionTarget.keyStr);
        dest.writeInt(useSecurityCaution ? 1 : 0);
        dest.writeInt(applyFormUsageMaxHours);
        dest.writeInt((isAdminRole ? 1 : 0));
        dest.writeString(useQuickMenu);
        dest.writeInt(twofactorAuthType);
        dest.writeString(twofactorEmailSendUrl);
        dest.writeString(twofactorEmailCheckUrl);
        dest.writeString(email);
    }

    public static final Creator<AppProperty> CREATOR = new Creator<AppProperty>() {
        @Override
        public AppProperty createFromParcel(Parcel source) {
            return new AppProperty(source);
        }

        @Override
        public AppProperty[] newArray(int size) {
            return new AppProperty[size];
        }
    };

    public enum SolutionTarget {
        Standard("default"),
        ElevenStreet("11st")
        ;

        String keyStr;

        SolutionTarget(String key) {
            keyStr = key;
        }

        static SolutionTarget fromString(String key) {
            if("11st".equals(key)) {
                return ElevenStreet;
            }else {
                return Standard;
            }
        }
    }

    public boolean isSolutionFor11st() {
        return solutionTarget == SolutionTarget.ElevenStreet;
    }

    public boolean getUseQuickMenu() {
        // 공인서버는 USE_QUICK_MENU 응답 파라메터가 없어 사용하는것으로 설정하고
        // 특정 솔루션 서버에서는 USE_QUICK_MENU 값이 "1" 일때만 사용하도록 설정한다.
        return useQuickMenu == null || useQuickMenu.isEmpty() || useQuickMenu.equals("1");
    }

    public int getTwofactorAuthType() {
        return twofactorAuthType;
    }

    public String getTwofactorEmailSendUrl() {
        return twofactorEmailSendUrl;
    }

    public String getTwofactorEmailCheckUrl() {
        return twofactorEmailCheckUrl;
    }

    public String getEmail() {
        return email;
    }
}
