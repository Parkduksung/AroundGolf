package com.rsupport.rv.viewer.sdk.util;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.ViewConfiguration;
import android.view.inputmethod.InputMethodInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.rsupport.rv.viewer.sdk_viewer.R;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by hyosang on 2016. 7. 28..
 */
public class DeviceUtil {
    public static boolean hasSoftKeyDevice(Context context) {
        if (Build.VERSION.SDK_INT <= 13) return false;
        boolean hasMenuKey = ViewConfiguration.get(context).hasPermanentMenuKey();
        boolean hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);
        boolean isSoft = false;
        if(!hasMenuKey && !hasBackKey) {
            Log.e("","no hasMenuKey");
            isSoft = true;
        } else {
            Log.e("","hasMenuKey "+hasMenuKey+" " +hasBackKey);
            isSoft = false;
        }

        return isSoft;
    }

    public String getCurrentInputMethodPackageName(Context context, ContentResolver resolver,
                                                   InputMethodManager imm, List<InputMethodInfo> imis, PackageManager pm) {
        if (resolver == null || imis == null) return null;
        final String currentInputMethodId = Settings.Secure.getString(resolver,
                Settings.Secure.DEFAULT_INPUT_METHOD);
        if (TextUtils.isEmpty(currentInputMethodId)) return null;
        for (InputMethodInfo imi : imis) {
            if (currentInputMethodId.equals(imi.getId())) {
                return imi.getPackageName();
            }
        }
        return null;
    }

    /**
     * 디바이스 아이디 가져오기
     * @param activity
     * @return String
     */
    @SuppressLint("MissingPermission")
    public static String getDeviceId(AppCompatActivity activity){
        TelephonyManager tm = (TelephonyManager) activity.getSystemService(activity.TELEPHONY_SERVICE);
        return tm.getDeviceId();
    }

    /**
     * 폰번호 가져오기
     * @param activity
     * @return phoneNumber
     */
    @SuppressLint("MissingPermission")
    public static String getPhoneNumber(AppCompatActivity activity) {
        TelephonyManager systemService = (TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE);

        String phoneNumber = systemService.getLine1Number();

        phoneNumber = phoneNumber.substring(phoneNumber.length()-10, phoneNumber.length());

        phoneNumber = "0"+phoneNumber;

        return phoneNumber;
    }

    public static boolean isGooglePixel() {
        if("Google".equals(Build.MANUFACTURER) && "Pixel".equals(Build.MODEL)) {
            return true;
        }

        return false;
    }

    public static String getScreenShotSavePath() {
        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/RemoteView_Screenshots";
        File dir = new File(path);

        if(!dir.exists() || (dir.exists() && !dir.isDirectory())) {
            if(!dir.mkdirs()) {
            }
        }
        String filename = createPictureFileName();
        String filepath = String.format("%s/%s", path , filename);
        return filepath;
    }

    @NotNull
    public static String createPictureFileName() {
        String format ="yyyyMMdd_HHmmss";
        SimpleDateFormat df = new SimpleDateFormat(format);
        String datetimeStr = df.format(new Date(System.currentTimeMillis()));
        return String.format("remote_view_capture_%s.jpg", datetimeStr);
    }

    public static void pictureScanner(AppCompatActivity activity, final String filePath) {
        final AppCompatActivity fActivity = activity;

        (new Handler(Looper.getMainLooper())).post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(fActivity, fActivity.getString(R.string.msg_screen_capture_success), Toast.LENGTH_LONG).show();
            }
        });

        MediaScanner.scan(fActivity, filePath, new MediaScanner.IMediaScannerListener() {
            @Override
            public void onScanCompleted(String path) {
                //start gallery activity
                try {
                    Intent intent = new Intent();
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.parse("file://" + path), "image/*");
                    fActivity.startActivity(intent);
                }catch(ActivityNotFoundException e) {
                }
            }
        });

    }
}
