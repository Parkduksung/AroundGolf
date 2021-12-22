package com.rsupport.rv.viewer.sdk.util;

import com.rsupport.rscommon.util.StringUtil;

/**
 * Created by hyosang on 2017. 9. 15..
 */

public class RVUtil {
    public static String getAgentTypeString(String extend) {
        if(StringUtil.isEmpty(extend)) {
            return "NO_DATA";
        }else {
            int type = StringUtil.parseInt(extend, -1);

            switch(type) {
                case 0x00: return "General";
                case 0x01: return "vPro";
                case 0x02: return "RWT";
                case 0x10: return "HyperV Server";
                case 0x11: return "HyperV Image";
                case 0x12: return "VM Image";
                case 0x13: return "VirtualPC Image";
                case 0x14: return "VBox Image";
                case 0x15: return "XGen Image";
                case 0x30: return "Linux (Ubuntu)";
                case 0x32: return "RemoteKVM";
                case 0x33: return "RemoteWOL Device";
                case 0x34: return "RemoteWOL VirtualPC";
                case 0x40: return "mac OS";
                case 0x50: return "Android";
            }
        }

        return extend;
    }
}
