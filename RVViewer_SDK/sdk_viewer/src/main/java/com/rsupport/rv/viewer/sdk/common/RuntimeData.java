package com.rsupport.rv.viewer.sdk.common;

import android.content.Context;
import android.os.Bundle;


import com.rsupport.rscommon.util.StringUtil;
import com.rsupport.rv.viewer.sdk.common.log.RLog;
import com.rsupport.rv.viewer.sdk.data.AgentConnectOption;
import com.rsupport.rv.viewer.sdk.data.AppProperty;
import com.rsupport.rv.viewer.sdk.data.ServerInfo;
import com.rsupport.rv.viewer.sdk.data.UserInfoVO;
import com.rsupport.rv.viewer.sdk.setting.GlobalStatic;
import com.rsupport.rv.viewer.sdk.util.AgentInfo;
import com.rsupport.rv.viewer.sdk_viewer.R;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;



/**
 * Created by hyosang on 2016. 8. 9.
 *
 * 어플리케이션 프로세스가 살아있는 동안 유지되는 값들 singleinstance.
 * 단말마다 다른 값이나, 서버로부터 받은 데이터를 저장해둔다.
 */
public class RuntimeData {
    private static final int INTERVAL_THREADPOOL_CORE_SIZE = 5;

    private static final String BUNDLE_APPPROP = "appProperty";
    private static final String BUNDLE_USERINFO = "userInfo";
    private static final String BUNDLE_LASTAGENTINFO = "lastAgentInfo";
    private static final String BUNDLE_SERVERINFO = "serverInfo";

    private static RuntimeData mInstance = null;

    private String algorithm = "";
    private String keyText = "";
    private String ivParameter = "";

    private ScheduledThreadPoolExecutor mIntervalExecutor;

    public Context mAppContext = null;
    public AppProperty appProperty;
    public UserInfoVO userInfo;
    private AgentInfo lastAgentInfo;
    private ServerInfo serverInfo;
    public String mac16Upper = "";

    private AgentConnectOption agentConnectOption;

    public AgentConnectOption getAgentConnectOption() {
        return agentConnectOption;
    }

    public SessionData currentSessionData = new SessionData();


    public static final boolean LOG_NETWORK_DETAIL = true;




    ////////////////// temporary values
    public byte encodingType = 'Z';
    public byte hostBpp = 0;
    public byte remoteBpp = 0;


    ///////////////////////////////////

    public static synchronized RuntimeData createInstance(Context context, boolean bForce) {
        //어플리케이션 시작시 1회 호출
        if(mInstance == null) {
            mInstance = new RuntimeData();
            mInstance.initData(context);
        }else if((mInstance != null) && bForce) {
            //강제 초기화
            mInstance = new RuntimeData();
            mInstance.initData(context);
        }

        return mInstance;
    }

    public static RuntimeData getInstance() {
        return mInstance;
    }

    public boolean setAutoSystemLock(String agentGuid, boolean isSet) {
        if(!StringUtil.isEmpty(agentGuid)) {
            AgentInfo agentInfo = RuntimeData.getInstance().getLastAgentInfo();
            if(agentInfo != null) {
                if(agentGuid.equals(agentInfo.guid)) {
                    agentInfo.autoSystemLock = isSet;
                    RLog.d("Set autosystemlock = " + isSet);
                    return true;
                }else {
                    RLog.e("Last RLOG is not matches RLOG instance");
                }
            }else {
                RLog.e("AgentInfo is null");
            }
        }else {
            RLog.e("Input guid is null");
        }

        return true;
    }

    private void initData(Context context) {
        loadCryptoParam(context);

        mIntervalExecutor = new ScheduledThreadPoolExecutor(INTERVAL_THREADPOOL_CORE_SIZE);

        mAppContext = context.getApplicationContext();

        //직접 접근시 NullPointerException 방지를 위해 기본 인스턴스를 생성해둔다.
        appProperty = new AppProperty();
        userInfo = new UserInfoVO();
        lastAgentInfo = new AgentInfo();
        serverInfo = new ServerInfo();
        agentConnectOption = new AgentConnectOption();
        mac16Upper = GlobalStatic.getMacAddress(context);
        if(!StringUtil.isEmpty(mac16Upper)) {
            mac16Upper = mac16Upper.replaceAll("[^0-9A-F]", "");
        }else{
            mac16Upper = "";
        }
    }

    public void logoutClear() {
        appProperty.logoutClear();
        userInfo.logoutClear();
        lastAgentInfo = new AgentInfo();
        getServerInfo().logoutClear();

    }

    public void initSession() {
        currentSessionData = new SessionData();
    }

    public ScheduledFuture<?> registerDelayExec(Runnable command, long delay, TimeUnit unit) {
        return mIntervalExecutor.schedule(command, delay, unit);
    }

    public ScheduledFuture<?> registerInterval(Runnable worker, int delaySeconds, int intervalSeconds) {
        return mIntervalExecutor.scheduleAtFixedRate(worker, delaySeconds, intervalSeconds, TimeUnit.SECONDS);
    }

    private void loadCryptoParam(Context context) {
        //Seed encrypt key
        int[] seedEncryptKeyRes = {R.integer.i105, R.integer.i115, R.integer.i105, R.integer.i107, R.integer.i101,
                R.integer.i37, R.integer.i111, R.integer.i117, R.integer.i43, R.integer.i108,
                R.integer.i111, R.integer.i118, R.integer.i101, R.integer.i121, R.integer.i38,
                R.integer.i117, R.integer.i105, R.integer.i104, R.integer.i97, R.integer.i116,
                R.integer.i101, R.integer.i121, R.integer.i61, R.integer.i117};

        int[] seedEncryptKeyValue = new int[seedEncryptKeyRes.length];
        for (int i = 0 ; i < seedEncryptKeyRes.length ; i++) {
            seedEncryptKeyValue[i] = context.getResources().getInteger(seedEncryptKeyRes[i]);
        }
        SeedCrypto.encryptKey = new String(seedEncryptKeyValue, 0, seedEncryptKeyValue.length);

        //AES algorithm
        int[] algorithmRes = {R.integer.i113, R.integer.i88, R.integer.i115, R.integer.i80, R.integer.i88, R.integer.i88,
                R.integer.i81, R.integer.i65, R.integer.i108, R.integer.i99, R.integer.i76, R.integer.i115,
                R.integer.i83, R.integer.i71, R.integer.i122, R.integer.i75, R.integer.i48, R.integer.i90,
                R.integer.i81, R.integer.i108, R.integer.i102, R.integer.i99, R.integer.i85, R.integer.i117,
                R.integer.i113, R.integer.i83, R.integer.i87, R.integer.i108, R.integer.i104, R.integer.i89,
                R.integer.i117, R.integer.i88, R.integer.i104, R.integer.i72, R.integer.i43, R.integer.i102,
                R.integer.i73, R.integer.i80, R.integer.i112, R.integer.i73, R.integer.i115, R.integer.i74,
                R.integer.i56, R.integer.i61};
        int[] algorithmValue = new int[algorithmRes.length];
        for (int i = 0 ; i < algorithmRes.length ; i++) {
            algorithmValue[i] = context.getResources().getInteger(algorithmRes[i]);
        }

        String encodeSeedAlgorithm = new String(algorithmValue, 0, algorithmValue.length);
        algorithm = SeedCrypto.decrypt(encodeSeedAlgorithm);

        //AES key
        int[] keyRes = {R.integer.i72, R.integer.i102, R.integer.i67, R.integer.i66, R.integer.i82, R.integer.i71,
                R.integer.i116, R.integer.i88, R.integer.i66, R.integer.i48, R.integer.i85, R.integer.i114,
                R.integer.i79, R.integer.i90, R.integer.i79, R.integer.i49, R.integer.i77, R.integer.i118,
                R.integer.i111, R.integer.i82, R.integer.i117, R.integer.i119, R.integer.i61, R.integer.i61};

        int[] keyValue = new int[keyRes.length];
        for (int i = 0 ; i < keyRes.length ; i++) {
            keyValue[i] = context.getResources().getInteger(keyRes[i]);
        }

        String encodeSeedKeyText = new String(keyValue, 0, keyValue.length);
        keyText = SeedCrypto.decrypt(encodeSeedKeyText);

        //AES ivparameter
        int[] ivRes = {R.integer.i122, R.integer.i107, R.integer.i53, R.integer.i88, R.integer.i78, R.integer.i68,
                R.integer.i67, R.integer.i73, R.integer.i49, R.integer.i78, R.integer.i102, R.integer.i82,
                R.integer.i70, R.integer.i68, R.integer.i70, R.integer.i115, R.integer.i121, R.integer.i113,
                R.integer.i85, R.integer.i81, R.integer.i70, R.integer.i119, R.integer.i61, R.integer.i61};

        int[] ivValue = new int[ivRes.length];
        for (int i = 0 ; i < ivRes.length ; i++) {
            ivValue[i] = context.getResources().getInteger(ivRes[i]);
        }

        String encodeSeedIvParameter = new String(ivValue, 0, ivValue.length);
        ivParameter = SeedCrypto.decrypt(encodeSeedIvParameter);
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public String getKeyText() {
        return keyText;
    }

    public String getIvParam() {
        return ivParameter;
    }

    public AgentInfo getLastAgentInfo() {
        return lastAgentInfo;
    }

    public void setLastAgentInfo(AgentInfo lastAgentInfo) {
        this.lastAgentInfo = lastAgentInfo;
    }

    public ServerInfo getServerInfo() {
        return serverInfo;
    }

    public class SessionData {
        public String userGuid = new GUID().toString();
        public String dataGuid = "";
        public String screenGuid = "";
        public String fileGuid = "";
        public String soundGuid = "";
    }

    public Bundle getBundleData() {
        Bundle bundle = new Bundle();

        bundle.putParcelable(BUNDLE_APPPROP, appProperty);
        bundle.putParcelable(BUNDLE_USERINFO, userInfo);
        bundle.putParcelable(BUNDLE_LASTAGENTINFO, lastAgentInfo);
        bundle.putParcelable(BUNDLE_SERVERINFO, serverInfo);
        return bundle;
    }

    public void restoreBundleData(Bundle bundle) {
        if(bundle.containsKey(BUNDLE_APPPROP)) {
            appProperty = bundle.getParcelable(BUNDLE_APPPROP);
        }

        if(bundle.containsKey(BUNDLE_USERINFO)) {
            userInfo = bundle.getParcelable(BUNDLE_USERINFO);
        }

        if(bundle.containsKey(BUNDLE_LASTAGENTINFO)) {
            lastAgentInfo = bundle.getParcelable(BUNDLE_LASTAGENTINFO);
        }

        if(bundle.containsKey(BUNDLE_SERVERINFO)) {
            serverInfo = bundle.getParcelable(BUNDLE_SERVERINFO);
        }
    }
}
