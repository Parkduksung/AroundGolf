package com.rsupport.rv.viewer.sdk.util;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by hyosang on 2016. 7. 28..
 */
public class DataUtil {

    public static void recursiveRecycle(View root) {
        if (root == null)
            return;

        if (Build.VERSION.SDK_INT < 16)
            root.setBackgroundDrawable(null);
        else
            root.setBackground(null);

        if (root instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) root;
            int count = group.getChildCount();
            for (int i = 0; i < count; i++) {
                recursiveRecycle(group.getChildAt(i));
            }

            if (!(root instanceof AdapterView)) {
                group.removeAllViews();
            }

        }

        if (root instanceof ImageView) {
            ((ImageView) root).setImageDrawable(null);
        }

        root = null;

        return;
    }

    public static void recursiveRecycle(List<WeakReference<View>> recycleList) {
        for (WeakReference<View> ref : recycleList) {
            recursiveRecycle(ref.get());
        }
    }



    public static String getDefaultBundleString(Bundle bundle, String key, String defaultValue) {
        if (Build.VERSION.SDK_INT < 12) {
            String returns = bundle.getString(key);
            if (returns == null || returns.equals("")) returns = defaultValue;

            return returns;
        } else {
            return bundle.getString(key, defaultValue);
        }
    }

    public static int getDefaultBundleInt(Bundle bundle, String key, int defaultValue) {
        if (Build.VERSION.SDK_INT < 12) {
            int returns = bundle.getInt(key);
            if (returns == 0) returns = defaultValue;

            return returns;
        } else {
            return bundle.getInt(key, defaultValue);
        }
    }

}
