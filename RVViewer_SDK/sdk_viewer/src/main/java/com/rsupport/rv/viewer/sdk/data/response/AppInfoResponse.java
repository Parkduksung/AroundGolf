package com.rsupport.rv.viewer.sdk.data.response;


import com.rsupport.rscommon.parser.annotation.JsonKey;
import com.rsupport.rscommon.parser.annotation.XMLNodeName;

/**
 * Created by hyosang on 2017. 8. 23..
 */

public class AppInfoResponse extends BaseResponse {
    private static final String OEMTYPE_EXPLORER_DISABLED   = "7";

    private static final String APPFTP_ENABLE               = "1";
    private static final String APPFTP_DISABLE              = "0";

    private static final String AUTH_TYPE_DEFAULT           = "0";
    private static final String AUTH_TYPE_OTP               = "1";
    private static final String AUTH_TYPE_WINDOWS           = "2";
    private static final String AUTH_TYPE_NOAUTH            = "3";

    private static final String AUTH_ENABLE                 = "1";
    private static final String AUTH_DISABLE                = "0";

    private static final String TWOFACTOR_ENABLE            = "1";
    private static final String TWOFACTOR_DISABLE           = "0";

    ///////////////// Properties
    @JsonKey("productVersion")
    @XMLNodeName("WEBSERVER_PRODUCT_VERSION")
    public String webserverProductVersion = "";

    //에이전트 로그인 방식. 에이전트 목록에서 내려오는 값을 사용하므로 이 필드는 실제 사용하지는 않음.
    @JsonKey("accessAuthType")
    @XMLNodeName("ACCESSAUTHTYPE")
    public String accessAuthType = "";

    @JsonKey("recentNoticeId")
    @XMLNodeName("NEWNOTICESEQ")
    public String newNoticeSeq = "";

    @JsonKey("licenseExpiredNotifyDay")
    @XMLNodeName("LICENSE_EXPIRED_NOTIFY_DAY")
    public String licenseExpiredNotifyDay = "";

    @JsonKey("licenseEndDt")
    @XMLNodeName("LICENSE_END_DT")
    public String licenseEndDt = "";

    @JsonKey("useFavorite")
    @XMLNodeName("FAVORITE")
    public String favorite = "";

    @JsonKey("idType")
    @XMLNodeName("IDTYPE")
    public String idType = "";

    @XMLNodeName("ACCOUNTLOCK")
    public String accountLock = "0";

    @JsonKey("useHxengine")
    @XMLNodeName("USE_NEW_HXENGINE")
    public String useNewHxengine = "";

    @JsonKey("useNormalUserDeleteAgentDisabled")
    @XMLNodeName("DELAGENTDISABLED")
    public String delAgentDisabled = "";

    @JsonKey("useLiveview")
    @XMLNodeName("LIVEVIEW")
    public String liveview = "";

    //원격탐색기 사용여부: 구 서버에서는 해당 값이 없기때문에 사용안함을 기본값으로 한다.
    @JsonKey("useAppFtp")
    @XMLNodeName("APPFTP")
    public String appFtp = APPFTP_DISABLE;

    @JsonKey("liveviewGetImageTermSeconds")
    @XMLNodeName("IMAGECALLSECOND")
    public String imageCallSecond = "";

    @JsonKey("userKey")
    @XMLNodeName("USERKEY")
    public String userKey = "";

    @JsonKey("useTwofactorAuth")
    @XMLNodeName("USE_TWOFACTOR_AUTH")
    public String useTwoFactorAuth = "";

    @JsonKey("")
    @XMLNodeName("DATA")
    public String data = "";

    @JsonKey("newVersion")
    @XMLNodeName("NEWVERSION")
    public String newVersion = "";

    @JsonKey("liveviewPollingSeconds")
    @XMLNodeName("LIVEVIEWCALLSECOND")
    public String liveviewCallSecond = "";

    @XMLNodeName("USE_HXENGINE")
    public String useHxengine = "";

    @JsonKey("securityType")
    @XMLNodeName("SECURITYTYPE")
    public String securityType = "";

    @XMLNodeName("WAITLOCKTIME")
    public String waitLockTime = "";

    @JsonKey("oemType")
    @XMLNodeName("RVOEMTYPE")
    public String rvOemType = "";

    @XMLNodeName("LOGINFAILCOUNT")
    public String loginFailCount = "";

    @JsonKey("passwordChangeRemainingDays")
    @XMLNodeName("PASSWORDLIMITDAYS")
    public String passwordLimitDays = "";

    @XMLNodeName("PROCESS_EXEC_TIMEOUT_MINUTE")
    public String processExecTimeoutMinute = "";

    @XMLNodeName("EMAIL")
    public String userEmail = "";

    @JsonKey("useSecurityEdition")
    @XMLNodeName("USE_SECURITY_EDITION")
    public String useSecurityEdition = "0";


    public boolean isAdminRole = true;
    @JsonKey("roleAdmin")
    @XMLNodeName("ROLE_ADMIN")
    public void isRoleAdmin(String nodeName, String value) {
        if("ROLE_ADMIN".equals(nodeName.toUpperCase())) {
            if("true".equals(value)) {
                isAdminRole = true;
            }else {
                isAdminRole = false;
            }
        }

    }

    @XMLNodeName("APPLY_FORM_USAGE_MAX_HOURS")
    public String applyFormUsageMaxHours = "";


    ///////////////// Update server info
    // 업데이트 정보: PC viewer 전용. 모바일에서는 사용하지 않음.
    @JsonKey("updateClientDir")
    @XMLNodeName("UPDATECLIENTDIR")
    public String updateClientDir = "";

    @JsonKey("multiUpdateServer")
    @XMLNodeName("MULTIUPDATESERVER")
    public String multiUpdateServer = "";

    @JsonKey("updateServerAddress")
    @XMLNodeName("UPDATESERVER")
    public String updateServer = "";

    @JsonKey("updateServerPort")
    @XMLNodeName("UPDATESERVERPORT")
    public String updateServerPort = "";

    @JsonKey("consoleUpdatePath")
    @XMLNodeName("CONSOLEUPDATEPATH")
    public String consoleUpdatePath = "";



    /////////////// Token
    /////////// 2018. 10. 추가. userkey 대신 token 방식으로 전환, 구 서버 호환을 위해 userkey 관련 필드는 유지
    @JsonKey("accessToken")
    @XMLNodeName("ACCESS_TOKEN")
    public String accessToken = "";

    @JsonKey("refreshToken")
    @XMLNodeName("REFRESH_TOKEN")
    public String refreshToken = "";

    @JsonKey("refreshTokenUrl")
    @XMLNodeName("REFRESH_TOKEN_URL")
    public String refreshTokenUri = "";

    @JsonKey("apiVersion")
    @XMLNodeName("API_VERSION")
    public String apiVersion = "";

    @JsonKey("sendAccessCodeUrl")
    @XMLNodeName("SEND_ACCESS_CODE_URL")
    public String sendAccessCodeUrl = "";

    @JsonKey("checkAccessCodeUrl")
    @XMLNodeName("CHECK_ACCESS_CODE_URL")
    public String checkAccessCodeUrl = "";

    @JsonKey("useApplyFormCaution")
    @XMLNodeName("USE_APPLY_FORM_CAUTION")
    public String useApplyFormCaution = "";

    @JsonKey("securityEditionCustomDomain")
    @XMLNodeName("SECURITY_EDITION_CUSTOM_DOMAIN")
    public String securityEditionCustomDomain = "";


    ///////////////// URLs
    @JsonKey("urlAgentUpdate")
    @XMLNodeName("AGENTUPDATEURL")
    public String agentUpdateUrl = "";

    @JsonKey("urlAgentDelete")
    @XMLNodeName("AGENTDELETEURL")
    public String agentDeleteUrl = "";

    @JsonKey("multiWebServer")
    @XMLNodeName("MULTIWEBSERVER")
    public String multiWebserver = "";

    @JsonKey("webServerAddress")
    @XMLNodeName("WEBSERVER")
    public String webserver = "";

    @JsonKey("webServerPort")
    @XMLNodeName("WEBSERVERPORT")
    public String webserverPort = "";

    @JsonKey("urlAgentCheck")
    @XMLNodeName("AGENTCHECKURL")
    public String agentCheckUrl = "";

    @JsonKey("urlGetLiveviewImageAsync")
    @XMLNodeName("LIVEVIEWGETVIEW")
    public String liveviewGetView = "";

    @JsonKey("urlAgentList")
    @XMLNodeName("AGENTLISTURL")
    public String agentListUrl = "";

    @JsonKey("urlForceToDisconnect")
    @XMLNodeName("REMOTESTOP")
    public String remoteStop = "";

    @JsonKey("urlGetLiveviewPreviewImage")
    @XMLNodeName("LIVEVIEWPREVIEW")
    public String liveviewPreview = "";

    @JsonKey("mobileSkinUpdateUrl")
    @XMLNodeName("MOBILESKIN_UPDATE_URL")
    public String mobileSkinUpdateUrl = "";

    @JsonKey("urlWolPowerCheck")
    @XMLNodeName("WOLPOWERCHECK")
    public String wolPowerCheck = "";

    @JsonKey("urlNewAgentGroupList")
    @XMLNodeName("NEW_AGENT_GROUP_LIST_URL")
    public String newAgentGroupListUrl = "";

    @JsonKey("urlRecord")
    @XMLNodeName("RECWEBCALLURL")
    public String recWebCallUrl = "";

    @JsonKey("urlAgentFavoriteList")
    @XMLNodeName("AGENTFAVORITELISTURL")
    public String agentFavoriteListUrl = "";

    @JsonKey("urlAgentOption")
    @XMLNodeName("AGENTCONNECTOPTIONURL")
    public String agentConnectOptionUrl = "";

    @JsonKey("agentAgreeCheckUrl")
    @XMLNodeName("AGENT_CONNECT_AGREE_URL")
    public String agentConnectAgreeUrl = "";

    @JsonKey("urlLicenseActivationUrl")
    @XMLNodeName("ACTIVEURL")
    public String activeUrl = "";

    @JsonKey("urlVproAgents")
    @XMLNodeName("AGENTVRPONURL")
    public String agentVrponUrl = "";

    @JsonKey("urlGetLiveviewImage")
    @XMLNodeName("LIVEVIEWIMG")
    public String liveviewImg = "";

    @XMLNodeName("TWOFACTOR_TOKEN_REQUEST_URL")
    public String twoFactorTokenRequestUrl = "";

    @JsonKey("twofactorTokenCheckUrl")
    @XMLNodeName("TWOFACTOR_TOKEN_CHECK_URL")
    public String twoFactorTokenCheckUrl = "";

    @JsonKey("urlUnderConstruction")
    @XMLNodeName("UNDER_CONSTRUCTION_URL")
    public String underConstrunctionUrl = "";

    @JsonKey("urlNewAgentList")
    @XMLNodeName("NEW_AGENT_LIST_URL")
    public String newAgentListUrl = "";

    @JsonKey("urlWolDeviceCheck")
    @XMLNodeName("WOLDEVICECHECK")
    public String wolDeviceCheck = "";

    @JsonKey("wolPowerOnUrl")
    @XMLNodeName("WOL_POWER_ON_URL")
    public String wolPowerOnUrl = "";

    @JsonKey("uploadServerAddress")
    @XMLNodeName("UPLOADSERVER")
    public String uploadServer = "";

    @JsonKey("uploadServerPort")
    @XMLNodeName("UPLOADSERVERPORT")
    public String uploadServerPort = "";

    @JsonKey("writeApplyFormUrl")
    @XMLNodeName("WRITE_APPLY_FORM_URL")
    public String writeApplyFormUrl = "";

    @JsonKey("getApplyFormCautionUrl")
    @XMLNodeName("GET_APPLY_FORM_CAUTION_URL")
    public String applyFormCautionUrl = "";

    @JsonKey("checkApplyFormUrl")
    @XMLNodeName("CHECK_APPLY_FORM_URL")
    public String checkApplyFormUrl = "";

    @JsonKey("getRecentApproverUrl")
    @XMLNodeName("GET_RECENT_APPROVER_URL")
    public String recentApproverUrl = "";

    @JsonKey("searchApproverUrl")
    @XMLNodeName("SEARCH_APPROVER_URL")
    public String searchApproverUrl = "";

    @JsonKey("agentApplyFormListUrl")
    @XMLNodeName("AGENT_APPLY_FORM_LIST_URL")
    public String agentApplyFormListUrl = "";

    @JsonKey("urlViewerAccessIdCheckUrl")
    @XMLNodeName("VIEWER_ACCESS_ID_CHECK_URL")
    public String accessIdChecck = "";

    @JsonKey("urlViewerWaitUrl")
    @XMLNodeName("VIEWER_WAIT_URL")
    public String viewerWaitUrl = "";

    @XMLNodeName("USE_QUICK_MENU")
    public String useQuickMenu = "";

    @XMLNodeName("TWOFACTOR_AUTH_TYPE")
    public String twofactorAuthType = "3";

    @XMLNodeName("TWOFACTOR_EMAIL_SEND_URL")
    public String twofactorEmailSendUrl = "";

    @XMLNodeName("TWOFACTOR_EMAIL_CHECK_URL")
    public String twofactorEmailCheckUrl = "";
}
