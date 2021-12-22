package com.rsupport.rv.viewer.sdk.constant;

/**
 * Created by hyosang on 2017. 8. 30..
 */

public enum Bitrate {
    LOW,
    MEDIUM,
    HIGH
    ;

    public static Bitrate fromPacketValue(int value) {
        switch(value) {
            case 2: return LOW;
            case 4: return MEDIUM;
            case 9: return HIGH;
        }

        return MEDIUM;
    }

    public short asPacketValue() {
        switch(this) {
            case LOW: return 2;
            case MEDIUM: return 4;
            case HIGH: return 9;
        }

        return 4;
    }
}
