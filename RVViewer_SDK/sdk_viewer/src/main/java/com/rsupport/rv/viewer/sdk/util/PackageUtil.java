package com.rsupport.rv.viewer.sdk.util;

import android.content.Context;

import com.rsupport.rv.viewer.sdk.common.Enums;
import com.rsupport.rv.viewer.sdk_viewer.R;


/**
 * Created by hyosang on 2016. 7. 28..
 */
public class PackageUtil {

    public static boolean isDSMEPackage(Context context) {
        if (context.getPackageName().equals("rsupport.AndroidViewer.dsme")) {
            return true;
        }
        return false;
    }

    public static boolean isSKTPackage(Context context) {
        if (context.getPackageName().equals("rsupport.AndroidViewer.skt")) {
            return true;
        }
        return false;
    }

    @Deprecated
    public static boolean isNateonPackage(Context context) {
        boolean isRet = false;
        try {
            if (context.getPackageName().equals("rsupport.AndroidViewer.skcomms.nateon")) {
                isRet = true;
            }
        } catch (NullPointerException nullExc) {
        }
        return isRet;
    }

    public static boolean isAlcatelPackage(Context context) {
        return context.getPackageName().equals("rsupport.AndroidViewer.alcatel");
    }

    public static boolean isChinaPackage(Context context) {
        return context.getPackageName().equals("rsupport.AndroidViewer.cn");
    }

    public static boolean isAustraliaPackage(Context context) {
        return context.getPackageName().equals("rsupport.AndroidViewer.anz");
    }

    public static boolean isServerPackage(Context context) {
        return context.getPackageName().equals("rsupport.AndroidViewer.server") || isNateonPackage(context);
    }

    public static boolean isTclPackage(Context context) {
        return context.getPackageName().equals("rsupport.AndroidViewer.partnertcl");
    }

    public static String getDefaultServerAddr(Context context, Enums.ProductType type) {
        int resId;

        switch(type) {
            case ENTERPRISE:
                resId = getServerBizResId(context);
                break;

            case STANDARD:
                resId = getServerStandardResId(context);
                break;

            case SERVER:
            default:
                resId = R.string.serverip_personal;
        }

        return context.getResources().getString(resId);
    }

    public static int getServerStandardResId(Context context) {
        if(isChinaPackage(context)) {
            return R.string.cn_serverip_personal;
        } else if(isAustraliaPackage(context)) {
            return R.string.serverip_australia;
        } else {
            return R.string.serverip_personal;
        }
    }

    public static int getServerBizResId(Context context) {
        if(isChinaPackage(context)) {
            return R.string.cn_serverip_biz;
        } else if(isAustraliaPackage(context)) {
            return R.string.serverip_australia;
        } else {
            return R.string.serverip_biz;
        }
    }
}
