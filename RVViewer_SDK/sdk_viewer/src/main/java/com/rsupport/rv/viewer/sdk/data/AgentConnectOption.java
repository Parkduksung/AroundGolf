package com.rsupport.rv.viewer.sdk.data;


import static com.rsupport.rv.viewer.sdk.constant.ConstantsKt.MODE_XENC;

import com.rsupport.rscommon.util.StringUtil;
import com.rsupport.rv.viewer.sdk.common.RuntimeData;
import com.rsupport.rv.viewer.sdk.common.log.RLog;
import com.rsupport.rv.viewer.sdk.constant.Bitrate;
import com.rsupport.rv.viewer.sdk.constant.ConstantsKt;
import com.rsupport.rv.viewer.sdk.constant.FPSLevel;
import com.rsupport.rv.viewer.sdk.constant.ScapScreenColor;
import com.rsupport.rv.viewer.sdk.data.response.AgentConnectOptionResponse;
import com.rsupport.rv.viewer.sdk.decorder.scap.scapOption2Msg;
import com.rsupport.rv.viewer.sdk.pref.SettingPref;
import com.rsupport.rv.viewer.sdk.setting.GlobalStatic;
import com.rsupport.rv.viewer.sdk.util.VersionUtil;

import org.jetbrains.annotations.NotNull;

/**
 * Created by hyosang on 2017. 8. 30..
 */

public class AgentConnectOption {
    private int engineType;
    private boolean isDeviceSupportHxEngine = false;
    private boolean isAgentSupportHxEngine = false;
    private boolean isWebConfiguredHxEngine = false;
    private boolean isControlExpress = true;
    private ScapScreenColor scapScreenColor = ScapScreenColor.COLOR_256;
    private Bitrate hxBitrate = Bitrate.MEDIUM;
    private char agentHookType = 'D';
    private boolean autoBlankScreen = false;
    private boolean autoSystemLock = false;
    private boolean lastUsedH264 = false;
    private boolean scapOptionChanged = false;
    private boolean useViewerTimeout = false;
    private long viewerTimeoutSeconds = 0;
    private long viewerMessageBeforeTimeout = 0;
    private FPSLevel h264FPSLevel = FPSLevel.HIGH;

    private String viewerMode = MODE_XENC;

    public AgentConnectOption() {
        initScapSetting();
    }

    private void initScapSetting() {
        isDeviceSupportHxEngine = GlobalStatic.isSupportDeviceHXEngine();
        engineType = ConstantsKt.ENGINE_CAPTURE;
    }

    public void loadAgentSetting(String guid) {
        SettingPref.ScreenSet screenSet = SettingPref.getInstance().getAgentSetting(guid);
        isControlExpress = screenSet.bControlExpress;
        hxBitrate = Bitrate.fromPacketValue(screenSet.bitrate);
        h264FPSLevel = FPSLevel.from(screenSet.fpsLevel);
        lastUsedH264 = screenSet.canvasType == 1;
    }

    public void saveLocalSetting(String guid) {
        SettingPref settingPref = SettingPref.getInstance();
        SettingPref.ScreenSet screenSet = settingPref.getAgentSetting(guid);
        screenSet.bControlExpress = isControlExpress;
        screenSet.bitrate = hxBitrate.asPacketValue();
        screenSet.canvasType = engineType;
        screenSet.colorSet = scapScreenColor.asPacketValue();
        screenSet.fpsLevel = h264FPSLevel.toInt();
        settingPref.saveAgentSetting(guid, screenSet);
    }

//    public void applyFrom(CheckAccessIdResponse response) {
//        useViewerTimeout = "1".equals(response.useViewerTimeout);
//        viewerTimeoutSeconds = StringUtil.parseLong(response.viewerTimeoutSeconds, 0L);
//        viewerMessageBeforeTimeout = StringUtil.parseLong(response.viewerTimeoutNotifyBeforeSeconds, 0L);
//        viewerMode = response.viewer_mode;
//
//        if(viewerMode != null && viewerMode.toLowerCase().equals(MODE_XENC)){
//            engineType = ENGINE_HX;
//        }
//        useWatermark = StringUtil.parseInt(response.useWatermark, CheckAccessIdResponse.NOT_DEFINED);
//    }

    public void applyFrom(AgentConnectOptionResponse response) {
        scapScreenColor = ScapScreenColor.fromPacketValueForAndroidViewer(StringUtil.parseInt(response.screenColor, 1));
        isControlExpress = "0".equals(response.controlMode);
        autoBlankScreen = "1".equals(response.autoBlankScreen);
        autoSystemLock = "1".equals(response.autoSystemLock);

        scapOptionChanged = false;

        decideEngine();
    }

    public void applyFrom(scapOption2Msg packet) {
        scapScreenColor = ScapScreenColor.getColorByViewerBpp(packet.encoder.scapRemoteBpp);
        char hooktype = (char) packet.hook.scapHookType;
        if (hooktype == 'D') {
            isControlExpress = true;
            agentHookType = 'D';
        } else if (hooktype == 'P') {
            isControlExpress = false;
            agentHookType = 'P';
        }

        scapOptionChanged = false;
    }

    public boolean isUseViewerTimeout() {
        return useViewerTimeout;
    }

    public long getViewerTimeoutSeconds() {
        return viewerTimeoutSeconds;
    }

    public long getViewerTimeoutMessageBeforeSeconds() {
        return viewerMessageBeforeTimeout;
    }

    public boolean isHxEngine() {
        if (viewerMode.equals(MODE_XENC)) {
            return true;
        } else {
            return engineType == ConstantsKt.ENGINE_HX;
        }
    }

    public String getViewerMode() {
        return viewerMode;
    }


    private void decideEngine() {
        if (VersionUtil.isSupportH264Config(RuntimeData.getInstance().appProperty.getWebserverProductVersion())) {
            //서버 설정에 따름
            if (isDeviceSupportHxEngine && isAgentSupportHxEngine && isWebConfiguredHxEngine) {
                engineType = ConstantsKt.ENGINE_HX;
            } else {
                initScapSetting();
            }
        } else {
            //기존 연결했던 설정으로 연결
            if (lastUsedH264 && isDeviceSupportHxEngine && isAgentSupportHxEngine) {
                engineType = ConstantsKt.ENGINE_HX;
            } else {
                initScapSetting();
                //컬러 설정은 서버설정 수신시 셋팅된다.
            }
        }


        RLog.d("AgentConnectOption.decideEngine: " + toString());
    }

    public void setUseHxEngine(boolean bUse) {
        isWebConfiguredHxEngine = bUse;

        decideEngine();
    }

    public void setAgentSupportHxEngine(boolean bSupport) {
        isAgentSupportHxEngine = bSupport;

        decideEngine();
    }

    public int getEngineType() {
        if (viewerMode.equals(MODE_XENC)) {
            return ConstantsKt.ENGINE_HX;
        } else {
            return engineType;
        }
    }

    public void changeEngine(int type) {
        if (engineType != ConstantsKt.ENGINE_HX && type == ConstantsKt.ENGINE_HX) {
            if (isDeviceSupportHxEngine) {
                if (isAgentSupportHxEngine) {
                    engineType = ConstantsKt.ENGINE_HX;
                } else {
                    RLog.w("Cannot change engine : agent not support hxengine");
                }
            } else {
                RLog.w("Cannot change engine : device not supports hxengine");
            }
        } else if (engineType != ConstantsKt.ENGINE_CAPTURE && type == ConstantsKt.ENGINE_CAPTURE) {
            initScapSetting();
        } else {
            RLog.w("Not changed engine. current=" + engineType + ", to " + type);
        }
    }

    public boolean isControlExpress() {
        return isControlExpress;
    }

    public ScapScreenColor getScapScreenColor() {
        return scapScreenColor;
    }

    public boolean canUseHxEngine() {
        return (isDeviceSupportHxEngine && isAgentSupportHxEngine);
    }

    public void setHxBitrate(Bitrate b) {
        hxBitrate = b;
    }

    public Bitrate getHxBitrate() {
        return hxBitrate;
    }

    public boolean setScapConfiguration(boolean controlExpress, ScapScreenColor color) {
        boolean ret = (isControlExpress != controlExpress);

        ret = ret || (scapScreenColor != color);

        isControlExpress = controlExpress;
        scapScreenColor = color;

        scapOptionChanged = ret;

        return ret;
    }

    public FPSLevel getH264FPSLevel() {
        return h264FPSLevel;
    }

    public void setH264FPSLevel(FPSLevel h264FPSLevel) {
        this.h264FPSLevel = h264FPSLevel;
    }

    public boolean isHookTypeChanged() {
        return scapOptionChanged;
    }

    public short getHookType() {
        return (short) (isControlExpress ? 'D' : 'P');
    }

    public boolean isAutoBlankScreen() {
        return autoBlankScreen;
    }

    public boolean isAutoSystemLock() {
        return autoSystemLock;
    }

    @NotNull
    @Override
    public String toString() {
        return "HX Supports=Device:" + isDeviceSupportHxEngine +
                "/Agent:" + isAgentSupportHxEngine +
                "/WebConfig:" + isWebConfiguredHxEngine + ", " +
                "Engine=" + engineType + ", " +
                "CtrlExp=" + isControlExpress + ", " +
                "ScreenColor=" + scapScreenColor + ", " +
                "Bitrate=" + hxBitrate + ", " +
                "FPSLevel=" + h264FPSLevel.toInt() + ", " +
                "Agent Hooktype=" + agentHookType + "," +
                "viewerTimeout:use=" + useViewerTimeout + ", messageBefore=" +
                viewerMessageBeforeTimeout + ", timeout=" + viewerTimeoutSeconds;
    }
}
