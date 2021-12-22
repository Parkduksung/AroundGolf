package com.rsupport.rv.viewer.sdk.constant;

/**
 * Created by hyosang on 2017. 8. 30..
 */

public enum ScapScreenColor {
    COLOR_4,
    COLOR_256,
    COLOR_HIGH,
    COLOR_TRUE
    ;

    public short asPacketValue() {
        switch(this) {
            case COLOR_4: return 0;
            case COLOR_256: return 1;
            case COLOR_HIGH: return 2;
            case COLOR_TRUE: return 3;
        }

        return 1;
    }

    public short asPacketValueForAndroidViewer() {
        switch(this) {
            case COLOR_4: return 0;
            case COLOR_256: return 1;
            case COLOR_HIGH: return 2;
            case COLOR_TRUE: return 2;
        }

        return 1;
    }

    public short getViewerBpp() {
        switch(this) {
            case COLOR_4: return 2;
            case COLOR_256: return 8;
            case COLOR_HIGH: return 16;
        }

        return 8;
    }

    public static ScapScreenColor getColorByViewerBpp(short remoteBpp) {
        switch(remoteBpp) {
            case 2:
                return COLOR_4;
            case 8:
                return COLOR_256;
            case 15:
            case 16:
                return COLOR_HIGH;

            default:
                return COLOR_256;
        }
    }

    public static ScapScreenColor fromPacketValue(int value) {
        switch(value) {
            case 0: return ScapScreenColor.COLOR_4;
            case 1: return ScapScreenColor.COLOR_256;
            case 2: return ScapScreenColor.COLOR_HIGH;
            case 3: return ScapScreenColor.COLOR_TRUE;
        }

        return ScapScreenColor.COLOR_256;
    }

    public static ScapScreenColor fromPacketValueForAndroidViewer(int value) {
        switch(value) {
            case 0: return ScapScreenColor.COLOR_4;
            case 1: return ScapScreenColor.COLOR_256;
            case 2: return ScapScreenColor.COLOR_HIGH;
            case 3: return ScapScreenColor.COLOR_HIGH;
        }

        return ScapScreenColor.COLOR_256;
    }

}
