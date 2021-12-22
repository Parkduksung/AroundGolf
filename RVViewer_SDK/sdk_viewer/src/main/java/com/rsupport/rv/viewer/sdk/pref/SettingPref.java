package com.rsupport.rv.viewer.sdk.pref;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;


import com.rsupport.rscommon.util.StringUtil;
import com.rsupport.rv.viewer.sdk.common.Define;
import com.rsupport.rv.viewer.sdk.common.Enums;
import com.rsupport.rv.viewer.sdk.common.log.RLog;
import com.rsupport.rv.viewer.sdk.constant.FPSLevel;
import com.rsupport.rv.viewer.sdk.setting.GlobalStatic;
import com.rsupport.rv.viewer.sdk.util.AlternativeMacAddress;
import com.rsupport.rv.viewer.sdk.util.NetworkUtil;
import com.rsupport.rv.viewer.sdk.util.PackageUtil;


import org.koin.java.KoinJavaComponent;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 로그인 정보 저장 전용.
 * 로그인시 저장 체크박스에 체크하지 않으면 실제 데이터가 없으므로,
 * 앱 내부에서 id/pw/cid가 필요한 경우 {@see RuntimeData.userInfo} 와 {@see UserDataVO}를 참고한다
 */
public class SettingPref {
    private static final String PREF_NAME = "rssetting.init";

    @Deprecated private static final String KEY_SERVER_TYPE = "producttype";
    @Deprecated private static final String KEY_SERVER_IP_PERSONAL = "serverip_personal";
    @Deprecated private static final String KEY_SERVER_IP_CORP = "serverip_corp";
    @Deprecated private static final String KEY_SERVER_IP_CUSTOM = "serverip_server";

    private static final String KEY_SERVER_ADDRESS = "server_address";
    private static final String KEY_SERVER_ADDR_CUSTOM = "server_addr_custom";
    private static final String KEY_CORP_ID = "corpid";
    private static final String KEY_PROXYUSE = "proxyuse";
    private static final String KEY_PROXYADDR = "proxyaddr";
    private static final String KEY_PROXYPORT = "proxyport";
    private static final String KEY_PROXYID = "proxyid";
    private static final String KEY_PROXYPASS = "proxypasswd";
    private static final String KEY_CONTROLEXPRESS = "controlspeed";
    private static final String KEY_COLORSET = "colorset";
    private static final String KEY_SCREENLOCK = "screenlock";
    private static final String KEY_BITRATE = "bitrate";
    private static final String KEY_CANVASTYPE = "canvastype";
    private static final String KEY_AUTORESOLUTION = "autoresolution";
    private static final String KEY_FPS_LEVEL = "fpslevel";
    private static final String KEY_BACKGROUNDMODE_TIME = "background_mode_time";

    // 2018. 07. Android M 부터 MAC 조회 API가 차단되어 임의 생성한 값을 MAC 주소 대체값으로 사용한다.
    private static final String KEY_DEVICE_ID = "device_id";
    private static final String KEY_DEVICE_ID_EQUAL_MAC = "mac_as_device_id";

    private static SettingPref mInstance;

    public synchronized static SettingPref createInstance(Context context, boolean bNewInst) {
        if((mInstance == null) || bNewInst) {
            mInstance = new SettingPref(context.getApplicationContext());
        }

        return mInstance;
    }

    public synchronized static SettingPref getInstance() {
        return mInstance;
    }

    private Context mContext;
    private SharedPreferences mPref;
    private Map<String, ScreenSet> mAgentSetting;

    public Enums.ProductType productType;
    public String enterpriseId;
    public String serverAddr;
    public String serverCustomAddr = "";
    public boolean screenLock;
    public boolean autoResolution;
    @Deprecated
    public int canvasType;
    public boolean controlSpeed;
    @Deprecated
    public int colorSet;
    public boolean proxyUse;
    public String proxyAddress;
    public String proxyPort;
    public String proxyUserId;
    public String proxyUserPass;
    public short[] deviceId;
    public boolean isMACasDeviceId;
    public long backgroundModeTime;

    private SettingPref(Context context) {
        mPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        mContext = context.getApplicationContext();
        mAgentSetting = new HashMap<>();

        String stdDefaultServer = PackageUtil.getDefaultServerAddr(context, Enums.ProductType.STANDARD);
        String entDefaultServer = PackageUtil.getDefaultServerAddr(context, Enums.ProductType.ENTERPRISE);
        {
            if(mPref.contains(KEY_SERVER_TYPE)) {
                //기존 저장된 데이터와의 호환성 유지용 (@6.0.416)
                int lgcyPrdtTp = mPref.getInt(KEY_SERVER_TYPE, Define.ProductType.PERSONAL);
                String lgcyServServ = mPref.getString(KEY_SERVER_IP_CUSTOM, "");
                String customServAddr = "";

                switch(lgcyPrdtTp) {
                    case Define.ProductType.PERSONAL:
                        productType = Enums.ProductType.STANDARD;
                        serverAddr = stdDefaultServer;
                        enterpriseId = "";
                        break;

                    case Define.ProductType.CORP:
                        productType = Enums.ProductType.ENTERPRISE;
                        serverAddr = PackageUtil.getDefaultServerAddr(context, Enums.ProductType.ENTERPRISE);
                        enterpriseId = mPref.getString(KEY_CORP_ID, "");
                        break;

                    case Define.ProductType.SERVER:
                        productType = Enums.ProductType.SERVER;
                        serverAddr = lgcyServServ;
                        serverCustomAddr = lgcyServServ;
                        enterpriseId = mPref.getString(KEY_CORP_ID, "");
                        break;
                }

                //기존 데이터들 삭제 및 현재값 셋팅
                SharedPreferences.Editor editor = mPref.edit();
                editor.remove(KEY_SERVER_TYPE);
                editor.remove(KEY_SERVER_IP_PERSONAL);
                editor.remove(KEY_SERVER_IP_CORP);
                editor.remove(KEY_SERVER_IP_CUSTOM);
                editor.putString(KEY_SERVER_ADDRESS, serverAddr);
                editor.putString(KEY_CORP_ID, enterpriseId);
                editor.putString(KEY_SERVER_ADDR_CUSTOM, serverCustomAddr);
                editor.commit();
            }else {
                serverAddr = mPref.getString(KEY_SERVER_ADDRESS, stdDefaultServer);
                enterpriseId = mPref.getString(KEY_CORP_ID, "");
                serverCustomAddr = mPref.getString(KEY_SERVER_ADDR_CUSTOM, "");
            }


            //6.0.4.17 -> 6.0.5.6 업데이트시 custom주소 삭제되는 문제 임시해결
            if(!StringUtil.isEmpty(serverAddr) && StringUtil.isEmpty(serverCustomAddr)) {
                //서버주소가 들어가있는데 커스텀주소가 비어있으면
                serverCustomAddr = serverAddr;
            }
        }

        //determine producttype from values
        if(serverAddr.equals(stdDefaultServer) || serverAddr.equals(entDefaultServer)) {
            if(StringUtil.isEmpty(enterpriseId)) {
                productType = Enums.ProductType.STANDARD;
                serverAddr = stdDefaultServer;
            }else {
                productType = Enums.ProductType.ENTERPRISE;
                serverAddr = entDefaultServer;
            }
        }else {
            productType = Enums.ProductType.SERVER;
        }

        //Load preferences
        screenLock = mPref.getBoolean(KEY_SCREENLOCK, false);
        autoResolution = mPref.getBoolean(KEY_AUTORESOLUTION, false);
        canvasType = mPref.getInt(KEY_CANVASTYPE, GlobalStatic.CANVAS_NORMAL);
        controlSpeed = mPref.getBoolean(KEY_CONTROLEXPRESS, true);
        colorSet = mPref.getInt(KEY_COLORSET, 1);
        proxyUse = mPref.getBoolean(KEY_PROXYUSE, false);
        proxyAddress = mPref.getString(KEY_PROXYADDR, "");
        proxyPort = mPref.getString(KEY_PROXYPORT, "");
        proxyUserId = mPref.getString(KEY_PROXYID, "");
        proxyUserPass = mPref.getString(KEY_PROXYPASS, "");
        backgroundModeTime = mPref.getLong(KEY_BACKGROUNDMODE_TIME, Enums.BackgroundModeTime.MINUTE_5.getValueInSeconds());

        String macAddr = mPref.getString(KEY_DEVICE_ID, "");
        isMACasDeviceId = mPref.getBoolean(KEY_DEVICE_ID_EQUAL_MAC, false);

        //DeviceID가 없으면 셋팅 (최초 1회)
        Pattern p = Pattern.compile("([0-9A-F]{2})([0-9A-F]{2})([0-9A-F]{2})([0-9A-F]{2})([0-9A-F]{2})([0-9A-F]{2})");
        Matcher m = null;

        RLog.d("WIFI MAC = " + NetworkUtil.getMacAddress(context));

        do {
            if(StringUtil.isEmpty(macAddr) || (macAddr.length() != 12)) {
                macAddr = NetworkUtil.getMacAddress(context);
                isMACasDeviceId = true;
            }else if(m != null) {
                //한번 이상 실패하여 다시 들어온 경우...
                macAddr = "";
            }

            if(StringUtil.isEmpty(macAddr) || "020000000000".equals(macAddr) || (isMACasDeviceId && Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)) {
                //API blocked.
                RLog.i("MAC acquire blocked (or failed): " + macAddr);

                //macAddress 를 얻지못하였을때, 대체값을 주입시켜준다.
                macAddr = "020000000000";
//                        KoinJavaComponent.get(AlternativeMacAddress.class).getAlternativeMacAddress();



                isMACasDeviceId = false;

                RLog.i("Generated DeviceID: " + macAddr);
            }

            m = p.matcher(macAddr);
        }while(!m.find());

        deviceId = new short[6];
        for(int i=0;i<6;i++) {
            deviceId[i] = (short) StringUtil.parseInt(m.group(i + 1), 0, 16);
        }
        saveDeviceID();

//        //디버그 출력
//        if(BuildConfig.DEBUG) {
//            String [][] pairs = new String[][] {
//                    {KEY_SERVER_ADDRESS, serverAddr},
//                    {KEY_SERVER_ADDR_CUSTOM, serverCustomAddr},
//                    {KEY_CORP_ID, enterpriseId},
//                    {KEY_SCREENLOCK, String.valueOf(screenLock)},
//                    {KEY_AUTORESOLUTION, String.valueOf(autoResolution)},
//                    {KEY_CANVASTYPE, String.valueOf(canvasType)},
//                    {KEY_CONTROLEXPRESS, String.valueOf(controlSpeed)},
//                    {KEY_COLORSET, String.valueOf(colorSet)},
//                    {KEY_PROXYUSE, String.valueOf(proxyUse)},
//                    {KEY_PROXYADDR, proxyAddress},
//                    {KEY_PROXYPORT, proxyPort},
//                    {KEY_PROXYID, proxyUserId},
//                    {KEY_PROXYPASS, proxyUserPass},
//                    {KEY_DEVICE_ID, getDeviceID("-")},
//                    {KEY_DEVICE_ID_EQUAL_MAC, String.valueOf(isMACasDeviceId)},
//                    {KEY_BACKGROUNDMODE_TIME, String.valueOf(backgroundModeTime)},
//                    {"Resolved:ProductType", productType.toString()}
//            };
//
//            for(String [] pair : pairs) {
//                RLog.d(String.format("[%s] %s = %s", PREF_NAME, pair[0], pair[1]));
//            }
//        }
    }

    public void setServerInfo(Context context, Enums.ProductType type) {
        RLog.d("setServerInfo : " + type);
        productType = type;
        if(type != Enums.ProductType.SERVER) {
            serverAddr = PackageUtil.getDefaultServerAddr(context, type);
        }else {
            serverAddr = serverCustomAddr;
        }
    }

    public void setCustomServerAddress(String addr) {
        serverCustomAddr = addr;

        if(productType == Enums.ProductType.SERVER) {
            serverAddr = serverCustomAddr;
        }
    }

    public void setEnterpriseId(Context context, String entId) {
        enterpriseId = entId;
        if(productType != Enums.ProductType.SERVER) {
            if (StringUtil.isEmpty(entId)) {
                productType = Enums.ProductType.STANDARD;
            } else {
                productType = Enums.ProductType.ENTERPRISE;
            }
            serverAddr = PackageUtil.getDefaultServerAddr(context, productType);
        }
    }

    public void removeLoginInfo() {
        if(mPref != null) {
            SharedPreferences.Editor editor = mPref.edit();
            editor.remove(KEY_SERVER_ADDRESS);
            editor.remove(KEY_CORP_ID);
            editor.remove(KEY_SERVER_ADDR_CUSTOM);
            editor.commit();
        }
    }

    public String getDeviceID() {
        return getDeviceID(null);
    }

    public String getDeviceID(String separator) {
        if(StringUtil.isEmpty(separator)) {
            return String.format("%02X%02X%02X%02X%02X%02X", deviceId[0], deviceId[1], deviceId[2], deviceId[3], deviceId[4], deviceId[5]);
        }else {
            return String.format("%02X%s%02X%s%02X%s%02X%s%02X%s%02X",
                    deviceId[0], separator,
                    deviceId[1], separator,
                    deviceId[2], separator,
                    deviceId[3], separator,
                    deviceId[4], separator,
                    deviceId[5]
            );
        }
    }

    private void saveDeviceID() {
        if(mPref != null) {
            SharedPreferences.Editor editor = mPref.edit();
            editor.putString(KEY_DEVICE_ID, getDeviceID());
            editor.putBoolean(KEY_DEVICE_ID_EQUAL_MAC, isMACasDeviceId);
            editor.commit();
        }
    }

    public void saveServerInfo() {
        if(mPref != null) {
            SharedPreferences.Editor editor = mPref.edit();
            editor.putString(KEY_SERVER_ADDRESS, serverAddr);
            editor.putString(KEY_SERVER_ADDR_CUSTOM, serverCustomAddr);
            editor.putString(KEY_CORP_ID, enterpriseId);
            editor.commit();
        }
    }

    public void saveProxyInfo() {
        if(mPref != null) {
            SharedPreferences.Editor editor = mPref.edit();
            editor.putBoolean(KEY_PROXYUSE, proxyUse);
            editor.putString(KEY_PROXYADDR, proxyAddress);
            editor.putString(KEY_PROXYPORT, proxyPort);
            editor.putString(KEY_PROXYID, proxyUserId);
            editor.putString(KEY_PROXYPASS, proxyUserPass);
            editor.commit();
        }
    }

    public void saveBackgroundModeTime(){
        if(mPref != null) {
            SharedPreferences.Editor editor = mPref.edit();
            editor.putLong(KEY_BACKGROUNDMODE_TIME , backgroundModeTime);
            editor.commit();
        }
    }

    public void saveAll() {
        if(mPref != null) {
            SharedPreferences.Editor editor = mPref.edit();
            editor.putString(KEY_SERVER_ADDRESS, serverAddr);
            editor.putString(KEY_SERVER_ADDR_CUSTOM, serverCustomAddr);
            editor.putString(KEY_CORP_ID, enterpriseId);

            editor.putBoolean(KEY_AUTORESOLUTION, autoResolution);
            editor.putBoolean(KEY_SCREENLOCK, screenLock);
            editor.putInt(KEY_CANVASTYPE, canvasType);
            editor.putBoolean(KEY_CONTROLEXPRESS, controlSpeed);
            editor.putInt(KEY_COLORSET, colorSet);

            editor.putBoolean(KEY_PROXYUSE, proxyUse);
            editor.putString(KEY_PROXYADDR, proxyAddress);
            editor.putString(KEY_PROXYPORT, proxyPort);
            editor.putString(KEY_PROXYID, proxyUserId);
            editor.putString(KEY_PROXYPASS, proxyUserPass);

            editor.putLong(KEY_BACKGROUNDMODE_TIME, backgroundModeTime);
            editor.commit();
        }
    }

    public ScreenSet getAgentSetting(String guid) {
        ScreenSet agentSetting = mAgentSetting.get(guid);
        if(agentSetting == null) {
            //default setting
            agentSetting = new ScreenSet();

            SharedPreferences pref = mContext.getSharedPreferences(PREF_NAME + guid, Context.MODE_PRIVATE);
            agentSetting.bControlExpress = pref.getBoolean(KEY_CONTROLEXPRESS, true);
            agentSetting.screenLock = pref.getBoolean(KEY_SCREENLOCK, false);
            agentSetting.autoResolution = pref.getBoolean(KEY_AUTORESOLUTION, false);
            agentSetting.colorSet = pref.getInt(KEY_COLORSET, 1);
            agentSetting.bitrate = pref.getInt(KEY_BITRATE, 9);
            agentSetting.canvasType = pref.getInt(KEY_CANVASTYPE, GlobalStatic.CANVAS_NORMAL);
            agentSetting.fpsLevel = pref.getInt(KEY_FPS_LEVEL, FPSLevel.HIGH.toInt());

            mAgentSetting.put(guid, agentSetting);
        }

        return agentSetting;
    }

    public void saveAgentSetting(String guid, ScreenSet agentSetting) {
        mAgentSetting.put(guid, agentSetting);

        SharedPreferences pref = mContext.getSharedPreferences(PREF_NAME + guid, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        editor.putBoolean(KEY_CONTROLEXPRESS, agentSetting.bControlExpress);
        editor.putBoolean(KEY_SCREENLOCK, agentSetting.screenLock);
        editor.putBoolean(KEY_AUTORESOLUTION, agentSetting.autoResolution);
        editor.putInt(KEY_COLORSET, agentSetting.colorSet);
        editor.putInt(KEY_BITRATE, agentSetting.bitrate);
        editor.putInt(KEY_CANVASTYPE, agentSetting.canvasType);
        editor.putInt(KEY_FPS_LEVEL, agentSetting.fpsLevel);
        editor.commit();
    }

    public static class ScreenSet {
        public boolean bControlExpress;
        public int colorSet;
        public int bitrate;
        public int canvasType;
        public boolean screenLock;
        public boolean autoResolution;
        public int fpsLevel;
    }
}
