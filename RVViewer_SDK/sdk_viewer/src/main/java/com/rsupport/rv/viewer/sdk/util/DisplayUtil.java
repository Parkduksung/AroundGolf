package com.rsupport.rv.viewer.sdk.util;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

/**
 * Created by hyosang on 2016. 7. 28..
 */
public class DisplayUtil {
    public static int Dip2Pixel(Context context, float dip) {
        int ret = 0;
        final float scale = context.getResources().getDisplayMetrics().density;
        float px = 0.0f;
        if (DisplayMetrics.DENSITY_XHIGH <= scale) {
            px = (int) (dip * 1.125 * scale + 0.5f);
        } else {
            px = (int) (dip * scale + 0.5f);
        }
        ret = (int) px;
        return ret;
    }

    public static int Dip2Pixel(ViewGroup viewGroup, float dip) {
        int ret = 0;
        final float scale = viewGroup.getResources().getDisplayMetrics().density;
        float px = (int) (dip * scale + 0.5f);
        ret = (int) px;
        return ret;
    }

    public static float getDisplayMetricsDensity(Context context) {
        return context.getResources().getDisplayMetrics().density;
    }

    public static int getPixel(Context context, int p) {
        float den = getDisplayMetricsDensity(context);
        if (den != 1) {
            return (int) (p * den + 0.5);
        }
        return p;
    }


    public static int getProperResolution(long maxMem) {
        int ret = 0;

        if (maxMem > 200000000) {
            ret = 4096 * 2160 + 100;
        } else if (maxMem > 100000000) {
            ret = 3840 * 2160 + 100;
        } else if (maxMem > 38000000) {
            ret = 2000 * 1800 + 100;
        } else if (maxMem > 31000000) {
            ret = 2560 * 1440 + 100;
        } else if (maxMem > 24000000) {
            ret = 1920 * 1080;
        } else {
            ret = 1024 * 768 + 100;
        }
        return ret;
    }

    public static int getProperResolutionHTC(long maxMem) {
        int ret = 0;

        if (maxMem > 100000000) {
            ret = 3840 * 2160 + 100;
        } else if (maxMem > 38000000) {
            ret = 2000 * 1800 + 100;
        } else if (maxMem > 31000000) {
            ret = 1920 * 1080;
        } else if (maxMem > 24000000) {
            ret = 1920 * 1080;
        } else if (maxMem > 17000000) {
            ret = 1680 * 1050 + 100;
        } else {
            ret = 1024 * 768 + 100;
        }
        return ret;
    }

    public static Animation getAnimation(Context context, int id) {
        Animation anim = AnimationUtils.loadAnimation(context, id);
        anim.setRepeatMode(Animation.RESTART);
        return anim;
    }

}
