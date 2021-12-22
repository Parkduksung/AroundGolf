/********************************************************************************
 *       ______   _____    __    __ _____   _____   _____    ______  _______
 *      / ___  | / ____|  / /   / // __  | / ___ | / __  |  / ___  ||___  __|
 *     / /__/ / | |____  / /   / // /  | |/ /  | |/ /  | | / /__/ /    / /
 *    / ___  |  |____  |/ /   / // /__/ // /__/ / | |  | |/ ___  |    / /
 *   / /   | |   ____| || |__/ //  ____//  ____/  | |_/ // /   | |   / /
 *  /_/    |_|  |_____/ |_____//__/    /__/       |____//_/    |_|  /_/
 *
 ********************************************************************************
 *
 * Copyright (c) 2013 RSUPPORT Co., Ltd. All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains the property
 * of RSUPPORT Company Limited and its suppliers, if any. The intellectual
 * and technical concepts contained herein are proprietary to RSUPPORT
 * Company Limited and its suppliers and are protected by trade secret
 * or copyright law. Dissemination of this information or reproduction
 * of this material is strictly forbidden unless prior written permission
 * is obtained from RSUPPORT Company Limited.
 *
 * FileName: AgentCommonUI.java
 * Author  : khkim@rsupport.com
 * Date    : 2014. 3. 20.
 * Purpose : AgentList / AgentInfo 공용 UI 및 기능을 위한 common UI control
 *
 * [History]
 *
 */

package com.rsupport.rv.viewer.sdk.agent;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Message;
import android.util.Log;

import com.rsupport.rv.viewer.sdk.common.Base64;
import com.rsupport.rv.viewer.sdk.common.ComConstant;
import com.rsupport.rv.viewer.sdk.common.RuntimeData;
import com.rsupport.rv.viewer.sdk.common.log.RLog;
import com.rsupport.rv.viewer.sdk.data.UserInfoVO;
import com.rsupport.rv.viewer.sdk.decorder.Converter;
import com.rsupport.rv.viewer.sdk.setting.GlobalStatic;
import com.rsupport.rv.viewer.sdk.util.AgentInfo;
import com.rsupport.rv.viewer.sdk.util.PackageUtil;
import com.rsupport.rv.viewer.sdk_viewer.R;


public class AgentCommon implements IEventIds {

    public static final int FUNMODE_REMOTECONTROL = 0; // 원격제어
    public static final int FUNMODE_REMOTEEXPLORER = 1; // 원격탐색기
    public static final int FUNMODE_WAKEONLAN = 2; // WOL
    public static final int FUNMODE_VPROPOWER = 3; // vPro 전원관리
    public static final int FUNMODE_REMOTECAPTURE = 4; // 원격캡처
    public static final int FUNMODE_AGENTUNINSTALL = 5; // 에이전트삭제
    public static final int FUNMODE_AGENTINFO = 6; // 등록정보
    public static final int FUNMODE_REMOTEREBOOT = 7; // 원격재부팅
    public static final int FUNMODE_REMOTELOGOFF = 8; // 원격로그오프
    public static final int FUNMODE_REMOTESHUTDOWN = 9; // 원격종료
    public static final int FUNMODE_REMOTESESSIONCLOSE = 10; // 원격강제종료
    public static final int FUNMODE_ALL_WAKEONLAN = 12; // 전체 전원 켜기

    public static final int FUNMODE_LIST_REFRESH = 99; // 새로 고침
    public static final int FUNMODE_ALERT_WAIT_LIST_REFRESH = 98; // 새로 고침

    public static int funcMode = FUNMODE_REMOTECONTROL;
    public static boolean isProcessing = false;

    /**
     * 동작 취소시 돌아갈 Activity 가 InfoActivity 라면 정보 저장
     **/
    public static boolean backInfoActivity = false;

    public AgentCommon() {
    }

    /**
     * AgentInfo 의 현재 상태를 표현하는 이미지아이콘의 리소스 id 를 리턴함
     **/
    public static int getAgentIconResource(AgentInfo agentInfo) {

        int resourceId = 0;

        String expired = agentInfo.expired;
        String extend = agentInfo.extend;
        int status = agentInfo.status;

        if (expired.equals(ComConstant.AGENT_OK) || expired.equals("")) {

            if (extend.equals(GlobalStatic.WOL_MACHINE)
                    && status == ComConstant.AGENT_STATUS_LOGIN) {
                if (expired.equals(ComConstant.AGENT_EXPIRED)) {
                    resourceId = R.drawable.icon_wol_on_expired;
                } else {
                    if (agentInfo.isactive.equals("1")) {
                        resourceId = R.drawable.icon_wol_on;
                    } else {
                        resourceId = R.drawable.icon_wol_on_lock;
                    }
                }
            } else if (extend.equals(GlobalStatic.WOL_MACHINE)
                    && status == ComConstant.AGENT_STATUS_LOGOUT) {
                if (expired.equals(ComConstant.AGENT_EXPIRED)) {
                    resourceId = R.drawable.icon_wol_off_expired;
                } else {
                    if (agentInfo.isactive.equals("1")) {
                        resourceId = R.drawable.icon_wol_off;
                    } else {
                        resourceId = R.drawable.icon_wol_off_lock;
                    }
                }
            } else if (extend.equals(GlobalStatic.WOL_HARDWARE)) {
                resourceId = R.drawable.icon_wol_power_on;
            } else {
                if (agentInfo.extend.equals("80")) {
                    if (status == ComConstant.AGENT_STATUS_LOGIN
                            || status == ComConstant.AGENT_STATUS_REMOTING
                            || agentInfo.type == AgentInfo.VIRTUAL_AGENT) {
                        if (agentInfo.isactive.equals("1")) {
                            if (agentInfo.isremote.equals("1")) {
                                resourceId = R.drawable.icon_mobile_remote;
                            } else {
                                resourceId = R.drawable.icon_mobile_on;
                            }
                        } else {
                            resourceId = R.drawable.icon_mobile_on_lock;
                        }
                    } else {
                        if (agentInfo.isactive.equals("1")) {
                            resourceId = R.drawable.icon_mobile_off;
                        } else {
                            resourceId = R.drawable.icon_mobile_off_lock;
                        }
                    }
                } else {
                    if (status == ComConstant.AGENT_STATUS_LOGIN
                            || status == ComConstant.AGENT_STATUS_REMOTING
                            || agentInfo.type == AgentInfo.VIRTUAL_AGENT) {
                        if (agentInfo.isactive.equals("1")) {
                            if (agentInfo.isremote.equals("1")) {
                                resourceId = R.drawable.icon_agent_remote;
                            } else {
                                resourceId = R.drawable.icon_agent_on;
                            }
                        } else {
                            resourceId = R.drawable.icon_agent_on_lock;
                        }
                    } else {
                        if (agentInfo.isactive.equals("1")) {
                            resourceId = R.drawable.icon_agent_off;
                        } else {
                            resourceId = R.drawable.icon_agent_off_lock;
                        }
                    }
                }
            }
        } else if (expired.equals(ComConstant.AGENT_EXPIRED)) {
            if (agentInfo.extend.equals("80")) {
                if (status == ComConstant.AGENT_STATUS_LOGIN
                        || agentInfo.type == AgentInfo.VIRTUAL_AGENT) {
                    resourceId = R.drawable.icon_mobile_on_expired;
                } else {
                    resourceId = R.drawable.icon_mobile_off_expired;
                }
            } else {
                if (status == ComConstant.AGENT_STATUS_LOGIN
                        || agentInfo.type == AgentInfo.VIRTUAL_AGENT) {
                    resourceId = R.drawable.icon_agent_on_expired;
                } else {
                    resourceId = R.drawable.icon_agent_off_expired;
                }
            }

        } else if (expired.equals(ComConstant.AGENT_OVER)) {
            if (agentInfo.extend.equals("80")) {
                if (status == ComConstant.AGENT_STATUS_LOGIN
                        || agentInfo.type == AgentInfo.VIRTUAL_AGENT) {
                    resourceId = R.drawable.icon_mobile_on_lock;
                } else {
                    resourceId = R.drawable.icon_mobile_off_lock;
                }
            } else {
                if (status == ComConstant.AGENT_STATUS_LOGIN
                        || agentInfo.type == AgentInfo.VIRTUAL_AGENT) {
                    resourceId = R.drawable.icon_agent_on_lock;
                } else {
                    resourceId = R.drawable.icon_agent_off_lock;
                }
            }
        }

        return resourceId;
    }

    public static int getAgentIconResourceForNateon(AgentInfo agentInfo) {
        int icon = R.drawable.status_pc_on;
        if (agentInfo.expired.equals(ComConstant.AGENT_OK) || agentInfo.expired.equals("")) {

            if (agentInfo.status == ComConstant.AGENT_STATUS_LOGIN || agentInfo.type == AgentInfo.VIRTUAL_AGENT) {

                if (agentInfo.devicetype.equals("0") || agentInfo.devicetype.equals("2")) {
                    icon = R.drawable.status_pc_on;
                } else if (agentInfo.devicetype.equals("1")) {
                    icon = R.drawable.status_apple_on;
                } else if (agentInfo.devicetype.equals("3")) {
                    icon = R.drawable.status_android_on;
                }
            } else {// OFF
                if (agentInfo.devicetype.equals("0") || agentInfo.devicetype.equals("2")) {
                    icon = R.drawable.status_pc_off;
                } else if (agentInfo.devicetype.equals("1")) {
                    icon = R.drawable.status_apple_off;
                } else if (agentInfo.devicetype.equals("3")) {
                    icon = R.drawable.status_android_off;
                }
            }
        } else if (agentInfo.expired.equals(ComConstant.AGENT_EXPIRED)) {

            if (agentInfo.devicetype.equals("0") || agentInfo.devicetype.equals("2")) {
                icon = R.drawable.status_pc_exp;
            } else if (agentInfo.devicetype.equals("1")) {
                icon = R.drawable.status_apple_exp;
            } else if (agentInfo.devicetype.equals("3")) {
                icon = R.drawable.status_android_exp;
            }
        } else if (agentInfo.expired.equals(ComConstant.AGENT_OVER)) {

            if (agentInfo.status == ComConstant.AGENT_STATUS_LOGIN) {
                icon = R.drawable.pcover;
            } else {
                icon = R.drawable.pcoveroff;
            }
        }

        return icon;
    }

    /**
     * Setting original Resolution of Host(PC)
     **/
    public static void setOriginHostResolution(int width, int height, int colorBit) {
        // 기존 해상도 정보가 없을 경우에 보관. 해상도를 여러번 변경할 수 있으므로, 최초 1회만 저장
        if (GlobalStatic.ORIGIN_RESOLUTION_X <= 0) {
            GlobalStatic.ORIGIN_RESOLUTION_X = (short) width;
            GlobalStatic.ORIGIN_RESOLUTION_Y = (short) height;
            GlobalStatic.ORIGIN_RESOLUTION_BIT = (short) colorBit;
        }
    }

    public static boolean isAgentActive(AgentInfo agentInfo) {
        if (agentInfo.isactive.equals("1")) {
            return true;
        }
        return false;
    }

    public static Message getHandlerMessage(Object obj) {
        Message msg = Message.obtain();
        msg.obj = obj;
        return msg;
    }


    public static String getWOLData(int requestType, String remoteIP, String remoteMac) {
        int startPos = 0;
        final int wolPort = 4000;

        byte[] memStream = new byte[4 + // RequestType
                (4 + remoteIP.getBytes().length) + // Remote IP
                (4 + remoteMac.getBytes().length) + // Remote Macaddress
                4]; // WOL Port

        // RequestType
        System.arraycopy(Converter.getBytesFromIntLE(requestType), 0, memStream, startPos, 4);
        startPos += 4;

        // Remote IP
        System.arraycopy(Converter.getBytesFromIntLE(remoteIP.getBytes().length), 0, memStream, startPos, 4);
        startPos += 4;
        System.arraycopy(remoteIP.getBytes(), 0, memStream, startPos, remoteIP.getBytes().length);
        startPos += remoteIP.getBytes().length;

        // Remote Macaddress
        System.arraycopy(Converter.getBytesFromIntLE(remoteMac.getBytes().length), 0, memStream, startPos, 4);
        startPos += 4;
        System.arraycopy(remoteMac.getBytes(), 0, memStream, startPos, remoteMac.getBytes().length);
        startPos += remoteMac.getBytes().length;

        // WOL Port
        System.arraycopy(Converter.getBytesFromIntLE(wolPort), 0, memStream, startPos, 4);
        startPos += 4;

        String base64String = getBase64Text(memStream);
        memStream = null;

        return base64String;
    }

    /**
     * base64 암호화
     **/
    private static String getBase64Text(byte[] memStream) {
        char[] charArray = new char[memStream.length];
        for (int i = 0; i < memStream.length; i++) {
            charArray[i] = (char) memStream[i];
        }
        char[] encoded = new char[Base64.ap_base64encode_len(memStream.length)];
        Base64.ap_base64encode_binary(encoded, charArray, memStream.length);
        String base64String = "";
        for (int i = 0; i < encoded.length; i++) {
            base64String += encoded[i];
        }

        memStream = null;
        charArray = null;
        encoded = null;

        return base64String;
    }

    /**
     * 기간만료, 라이선스 수 초과일 경우 true, 정상일 경우 false
     **/
    public static boolean isExpired(String expired) {
        if ("".equals(expired) || ComConstant.AGENT_OK.equals(expired)) {
            return false;
        }
        return true;
    }

    public static boolean isAgentActivation = false;
    public static boolean isThreadRun = false;



    /**
     * Common control function. capture, reboot, and so on...
     **/

    /**
     * get Agent Name, Group (SharedPreferences)
     **/


    public static boolean chkMobileAgentDecode() {
        String[] oldVersions = Build.VERSION.RELEASE.split("\\.");
        String[] newVersions = "4.4.2".split("\\.");

        int oldVer = Integer.valueOf(Build.VERSION.RELEASE.replaceAll("\\.", ""));
        int newVer = Integer.valueOf("4.4.2".replaceAll("\\.", ""));

        if (oldVer == newVer) return true;

        for (int i = 0; i < newVersions.length; i++) {
            RLog.d("versionCheck : " + oldVersions[i] + "/" + newVersions[i]);
            if (Integer.valueOf(oldVersions[i]) > Integer.valueOf(newVersions[i])) {
                return true;
            } else if (Integer.valueOf(oldVersions[i]) < Integer.valueOf(newVersions[i])) {
                return false;
            }
        }

        return true;
    }



    public static boolean isAgentOSCheck(String agentExtend) {
        boolean ret = true;
        int osType = Integer.parseInt(agentExtend);
        if (ComConstant.RVFLAG_KEY_MAC == osType) {
            ret = false;
        } else if (ComConstant.RVFLAG_KEY_UBUN == osType) {
            ret = false;
        }

        return ret;
    }

}
