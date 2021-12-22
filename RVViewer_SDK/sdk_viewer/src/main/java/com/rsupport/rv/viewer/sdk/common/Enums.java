package com.rsupport.rv.viewer.sdk.common;

import android.content.Context;

import com.rsupport.rv.viewer.sdk_viewer.R;


/**
 * Created by hyosang on 2016. 8. 10..
 */
public class Enums {
    public enum ProductType {
        STANDARD,
        ENTERPRISE,
        SERVER
    }

    public enum Channel {
        DATA,
        SCREEN
    }

    public enum BackgroundModeTime {
        ALL_OFF(0L),
        MINUTE_5(5 * 60L), MINUTE_10(10 * 60L), MINUTE_30(30 * 60L), //5분, 10분, 30분
        ALL_ON(-1L);  //Exception 발생하여 catch 문이 실행되게 하여 항상 켜지게 구현.

        //초단위
        private final long valueInSeconds;


        BackgroundModeTime(long valueInSeconds) {
            this.valueInSeconds = valueInSeconds;
        }

        public long getValueInSeconds() {
            return valueInSeconds;
        }

        public String getConvertTime(Context context) {
            if (valueInSeconds == ALL_OFF.valueInSeconds) {
                return context.getResources().getString(R.string.background_mode_time_all_off);
            } else if (valueInSeconds == MINUTE_5.valueInSeconds) {
                return context.getResources().getString(R.string.background_mode_time_5);
            } else if (valueInSeconds == MINUTE_10.valueInSeconds) {
                return context.getResources().getString(R.string.background_mode_time_10);
            } else if (valueInSeconds == MINUTE_30.valueInSeconds) {
                return context.getResources().getString(R.string.background_mode_time_30);
            } else {
                return context.getResources().getString(R.string.background_mode_time_all_on);
            }
        }

        public static BackgroundModeTime fromTime(long backgroundModeTime) {
            if (backgroundModeTime == BackgroundModeTime.ALL_OFF.getValueInSeconds()) {
                return BackgroundModeTime.ALL_OFF;
            } else if (backgroundModeTime == BackgroundModeTime.MINUTE_5.getValueInSeconds()) {
                return BackgroundModeTime.MINUTE_5;
            } else if (backgroundModeTime == BackgroundModeTime.MINUTE_10.getValueInSeconds()) {
                return BackgroundModeTime.MINUTE_10;
            } else if (backgroundModeTime == BackgroundModeTime.MINUTE_30.getValueInSeconds()) {
                return BackgroundModeTime.MINUTE_30;
            } else {
                return BackgroundModeTime.ALL_ON;
            }
        }
    }
}
