package com.rsupport.rv.viewer.sdk.util;


import com.rsupport.android.rscommon.BuildConfig;
import com.rsupport.rscommon.util.StringUtil;
import com.rsupport.rv.viewer.sdk.common.log.RLog;


/**
 * Created by hyosang on 2017. 9. 6..
 */

public class VersionUtil {
    private static final String SUPPORTS_WOL_POWERON_NEW_URL = "6.0.9";
    private static final String SUPPORTS_QUICKMENU_FIXED_ITEMS = "6.0.8";
    private static final String SUPPORTS_QUICKMENU_USER_ITEMS = "6.0.9";
    private static final String SUPPORTS_FILEMGR_WINDOWS_LOGIN = "6.0.11.2";

    private static int[] parseVersion(String version) {
        if (!StringUtil.isEmpty(version)) {
            String[] arr = version.split("\\.");
            int[] versionArr = new int[arr.length];

            for (int i = 0; i < arr.length; i++) {
                try {
                    String v = arr[i];
                    if (BuildConfig.DEBUG) {
                        if (v.endsWith("-SNAPSHOT")) {
                            v = v.replaceAll("-SNAPSHOT", "");
                        }
                    }
                    versionArr[i] = Integer.parseInt(v, 10);
                } catch (NumberFormatException e) {
                    RLog.w("Parse version fail: " + arr[i]);
                    versionArr[i] = 0;
                }
            }

            return versionArr;
        } else {
            return new int[] {0};
        }
    }

    public static boolean isSupportVersion(String baseVersion, String currentVersion) {
        int[] baseVer = parseVersion(baseVersion);
        int[] currVer = parseVersion(currentVersion);

        for (int i = 0; i < Math.min(baseVer.length, currVer.length); i++) {
            if (baseVer[i] < currVer[i]) {
                return true;
            } else if (baseVer[i] == currVer[i]) {
                continue;
            } else {
                return false;
            }
        }

        //동일한 버전으로 판단
        return true;
    }

    public static boolean isSupportWolNewUrl(String serverProductVersion) {
        return isSupportVersion(SUPPORTS_WOL_POWERON_NEW_URL, serverProductVersion);
    }

    public static boolean isSupportQuickMenuFixedItems(String serverProductVersion) {
        return isSupportVersion(SUPPORTS_QUICKMENU_FIXED_ITEMS, serverProductVersion);
    }

    public static boolean isSupportQuickMenuUserItems(String serverProductVersion) {
        return isSupportVersion(SUPPORTS_QUICKMENU_USER_ITEMS, serverProductVersion);
    }

    public static boolean isSupportH264Config(String serverProductVersion) {
        //서버의 [동영상모드] 설정값 여부.
        //원래 6.0.9에 추가될 예정이었으나 지연되었음. 서버 기능 추가시 해당 서버버전으로 체크할것.
        return false;
    }

    public static boolean isSupportWindowsLoginAuthInFileExplorer(String serverProductVersion) {
        return isSupportVersion(SUPPORTS_FILEMGR_WINDOWS_LOGIN, serverProductVersion);
    }

}
