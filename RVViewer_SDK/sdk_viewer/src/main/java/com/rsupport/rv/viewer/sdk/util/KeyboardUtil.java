package com.rsupport.rv.viewer.sdk.util;



import com.rsupport.rv.viewer.sdk.common.ComConstant;
import com.rsupport.rv.viewer.sdk.common.log.RLog;
import com.rsupport.rv.viewer.sdk.setting.GlobalStatic;


/**
 * Created by hyosang on 2016. 9. 1..
 */
public class KeyboardUtil {
    private static final int [][] mKeycodeMap = {
            {ComConstant.AVK_A, ComConstant.VK_A},
            {ComConstant.AVK_B, ComConstant.VK_B},
            {ComConstant.AVK_C, ComConstant.VK_C},
            {ComConstant.AVK_D, ComConstant.VK_D},
            {ComConstant.AVK_E, ComConstant.VK_E},
            {ComConstant.AVK_F, ComConstant.VK_F},
            {ComConstant.AVK_G, ComConstant.VK_G},
            {ComConstant.AVK_H, ComConstant.VK_H},
            {ComConstant.AVK_I, ComConstant.VK_I},
            {ComConstant.AVK_J, ComConstant.VK_J},
            {ComConstant.AVK_K, ComConstant.VK_K},
            {ComConstant.AVK_L, ComConstant.VK_L},
            {ComConstant.AVK_M, ComConstant.VK_M},
            {ComConstant.AVK_N, ComConstant.VK_N},
            {ComConstant.AVK_O, ComConstant.VK_O},
            {ComConstant.AVK_P, ComConstant.VK_P},
            {ComConstant.AVK_Q, ComConstant.VK_Q},
            {ComConstant.AVK_R, ComConstant.VK_R},
            {ComConstant.AVK_S, ComConstant.VK_S},
            {ComConstant.AVK_T, ComConstant.VK_T},
            {ComConstant.AVK_U, ComConstant.VK_U},
            {ComConstant.AVK_V, ComConstant.VK_V},
            {ComConstant.AVK_W, ComConstant.VK_W},
            {ComConstant.AVK_X, ComConstant.VK_X},
            {ComConstant.AVK_Y, ComConstant.VK_Y},
            {ComConstant.AVK_Z, ComConstant.VK_Z},
            {ComConstant.AVK_SLASH, ComConstant.VK_SLASH},
            {ComConstant.AVK_SHIFT, ComConstant.VK_SHIFT},
            {ComConstant.AVK_SPACE, ComConstant.VK_SPACE},
            {ComConstant.AVK_BACK, ComConstant.VK_BACK_SPACE},
            {ComConstant.AVK_ENTER, ComConstant.VK_ENTER},
    };

    private static final int [][] mKeycodeSpecial = {
            {ComConstant.AVK_SHIFT, ComConstant.VK_SHIFT},
            {ComConstant.AVK_LANG, ComConstant.KEY_HANGULE},
            {ComConstant.AVK_BACK, ComConstant.VK_BACK_SPACE},
            {ComConstant.AVK_ENTER, ComConstant.VK_ENTER},
            {ComConstant.AVK_TAB, ComConstant.VK_TAB},
            {ComConstant.AVK_LEFT, ComConstant.VK_LEFT},
            {ComConstant.AVK_RIGHT, ComConstant.VK_RIGHT},
            {ComConstant.AVK_UP, ComConstant.VK_UP},
            {ComConstant.AVK_DOWN, ComConstant.VK_DOWN},
    };

    public static int getKeycode(char ch) {
        int v = (int)ch;

        if(ch == '¥') {
            v = '\\';
        }

        if((v >= 0x20) && (v <= 0x7E)) {    //제어문자가 아니면...
            if ((v >= 'a') && (v <= 'z')) {  //소문자는 대문자로...
                int diff = 'a' - 'A';
                v = v - diff;
            }

            return v;
        }else {
            RLog.e(String.format("Cannot convert char %02X", ch));
        }

        return ch;
    }

    public static boolean isShiftChar(char ch) {
        String common = "ABCDEFGHIJKLMNOPQRSTUVWXYZ~!#$%&*()_+|{}<>?\"";
        String kronly = ":@^";
        String jponly = "'=`";

        if(common.indexOf(ch) < 0) {
            if (GlobalStatic.g_languageID == GlobalStatic.LANG_JAPANESE) {
                return (jponly.indexOf(ch) >= 0);
            } else {
                return (kronly.indexOf(ch) >= 0);
            }
        }else {
            return true;
        }
    }

    public static int getKeycodeInMapCommon(int keycode) {
        for(int [] k : mKeycodeMap) {
            if(k[0] == keycode) {
                return k[1];
            }
        }

        return keycode;
    }

    public static int getKeycodeInMapSpecial(int keycode) {
        for(int [] k : mKeycodeSpecial) {
            if(k[0] == keycode) {
                return k[1];
            }
        }

        return keycode;
    }

    public static boolean isSingleKey(char c) {
        int v = (int)c;
        return ((v >= 0x20) && (v <= 0x7E)) || (c == '¥');
    }
}
