package com.rsupport.rv.viewer.sdk.common;

import android.os.Build;

import java.nio.charset.Charset;

/**
 * Created by hyosang on 2016. 8. 10..
 */
public class Define {
    public static final boolean LOG_NETWORK = false;

    //웹 쿼리시 사용되는 ctype. 0=Desktop / 1=iOS / 2=WM / 3=Android
    public static final String CTYPE = "3";

    public static final class Uri {
        public static final String APPINFO = "/remoteview/command/app/appinfo.aspx";
        public static final String ACCESSID_CHECK = "/remoteview/Command/Viewer/viewer_accessid_check.aspx";
        public static final String SERVICE_INFO = "/services/api/console-lg/service-info";
        public static final String MAKE_INSTALL = "/services/api/console/agent-install-file-create";
        public static final String CONSOLE_LOGIN = "/services/api/console-lg/login";
        public static final String AGENT_CALL = "/RemoteView/Command/Viewer/viewer_agent_call.aspx";
        public static final String VPRO_POWER_ON = "/services/api/vpro/power_on";
        public static final String VPRO_POWER_OFF = "/services/api/vpro/power_off";
        public static final String VPRO_RESET = "/services/api/vpro/power_reset";
        public static final String MOBILE_MEMBER_JOIN = "/Mobile/Join";
    }

    public static final class AuthType {
        public static final String DEFAULT      = "0";
        public static final String OTP          = "1";
        public static final String WINDOWS      = "2";
        public static final String NOAUTH       = "3";
        public static final String EMAIL        = "4";
    }

    public static final String PROP_DISABLE     = "0";
    public static final String PROP_ENABLE      = "1";

    /**
     * build.gradle을 통해 BuildConfig.APP_TYPE 으로 설정됨
     */
    public static class AppType {
        public static final int ASP = 1;
        public static final int NATEON = 2;
        public static final int AUTOEVER = 3;
        public static final int AMOREPACIFIC = 4;
        public static final int CHINA = 5;
        public static final int AUSTRALIA = 6;
    }

    public static class ProductType {
        public static final int PERSONAL = 0;
        public static final int CORP     = 1;
        public static final int SERVER   = 2;
    }

    public static class DeviceParam {
        public static final String MODEL = Build.MODEL;
        public static final String MANUFACTURER = Build.MANUFACTURER;
        public static final String OS_VERSION = Build.VERSION.RELEASE;
    }

    public static class ScapType {
        public static final byte OPTION2 = 4;
        public static final byte CURSOR_POS = 7;
        public static final byte CURSOR_CACHED = 8;
        public static final byte CURSOR_NEW = 9;

        public static final byte SCAP_ENC = 13;
        public static final byte SRN_TO_SRN = 108;
    }

    public static class RcpPayload {
        private static final int PAYLOAD_BASE   = 200;
        public static final int RCP_CHANNEL     = PAYLOAD_BASE;
        public static final int CHANNEL_NOP     = RCP_CHANNEL + 1;
        public static final int KEY_MOUSE_CNTL  = RCP_CHANNEL + 2;
        public static final int MONITORS        = RCP_CHANNEL + 5;
        public static final int RCP_OPTION      = RCP_CHANNEL + 17;
        public static final int HX_HEADER       = RCP_CHANNEL + 36;
    }

    public static class RcpMessage {
        public static final int UNDEFINED = 0;

        public static final int NOP_REQUEST = 0;
        public static final int NOP_CONFIRM = 1;
        public static final int NOP_CONFIRM_NO_ACK = 2;

        public static final int OPTION_RCOPTION = 0;
        public static final int OPTION_FUNCTION = 1;
        public static final int OPTION_OPTION = 2;
        public static final int OPTION_SF_MAXFILESIZE = 3;
        public static final int OPTION_SCAP = 4;
        public static final int OPTION_R_PRINTER = 5;
        public static final int OPTION_SETTING = 6;
        public static final int OPTION_KEYBOARD_LANG = 7;
        public static final int OPTION_HOST_OPTION = 8;

        public static final int MONITOR_INFO_REQUEST = 0;
        public static final int MONITOR_INFO_RESPONSE = 1;
        public static final int MONITOR_SELECT = 2;
        public static final int MONITOR_CHANGE = 3;

        public static final int CHANNEL_LISTEN_REQUEST = 0;
        public static final int CHANNEL_LISTEN_CONFIRM = 1;
        public static final int CHANNEL_LISTEN_REJECT = 2;
        public static final int CHANNEL_LISTEN_FAIL = 3;
        public static final int CHANNEL_CONNECT_REQUEST = 4;
        public static final int CHANNEL_CONNECT_CONFIRM = 5;
        public static final int CHANNEL_CONNECT_REJECT = 6;
        public static final int CHANNEL_CONNECT_FAIL = 7;

        public static final int HX_ENGINE_HEADER = 0;
        public static final int HX_AV_CHANNEL_READY = 1;
        public static final int HX_VIDEO_START   = 2;
        public static final int HX_VIDEO_HEADER_N_DATA = 17;
    }

    public static class RcpFuncMask {
        public static final int KB                = 0x0000_0001;
        public static final int MO                = 0x0000_0002;
        public static final int DR                = 0x0000_0004;
        public static final int SF                = 0x0000_0008;
        public static final int Voice             = 0x0000_0010;
        public static final int RS                = 0x0000_0020;
        public static final int URLP              = 0x0000_0040;
        public static final int CT                = 0x0000_0080;
        public static final int PL                = 0x0000_0100;
        public static final int SI                = 0x0000_0200;
        public static final int SC                = 0x0000_0400;
        public static final int CA                = 0x0000_0800;
        public static final int CB                = 0x0000_1000;
        public static final int FV                = 0x0000_2000;
        public static final int RC                = 0x0000_4000;
        public static final int CtrlAltDel        = 0x0000_8000;
        public static final int BlankScreen       = 0x0001_0000;
        public static final int MouseTrace        = 0x0002_0000;
        public static final int LaserPointer      = 0x0004_0000;
        public static final int WhiteBoard        = 0x0008_0000;
        public static final int APSH              = 0x0010_0000;
        public static final int CamChat           = 0x0020_0000;
        public static final int SoundShare        = 0x0040_0000;
        public static final int SF_DnD            = 0x0080_0000;
        public static final int CrossRemoteCtrl   = 0x0100_0000;
        public static final int Print             = 0x0200_0000;
        public static final int SessionShare      = 0x0400_0000;
        public static final int Lock              = 0x0800_0000;
        public static final int RCSafeMode        = 0x1000_0000;
        public static final int RCAnotherAccount  = 0x2000_0000;

        public static final int _DEFAULT = Lock | Print |
                SF_DnD | SoundShare |
                LaserPointer | MouseTrace | BlankScreen |
                CtrlAltDel | FV | CB |
                CA | SC | SI | PL |
                URLP |
                SF | DR | MO | KB;
    }

    public static class RcpOptMask {
        public static final int AP                  = 0x0000_0001;
        public static final int UP                  = 0x0000_0002;
        public static final int NP                  = 0x0000_0004;
        public static final int NC                  = 0x0000_0008;
        public static final int CB_Auto             = 0x0000_0010;
        public static final int LocalRecord         = 0x0000_0020;
        public static final int ServerRecord        = 0x0000_0040;
        public static final int ChangeAccount       = 0x0000_0080;
        public static final int AutoDeleteModule    = 0x0000_0100;
        public static final int ConnectMail         = 0x0000_0200;
        public static final int AutoConfirmSS       = 0x0000_0400;
        public static final int CopyClipboard       = 0x0000_0800;

        public static final int ShowConnectCode     = 0x0000_4000;
        public static final int ShowBrowserInfo     = 0x0000_8000;

        public static final int AutoKeyMouseCtrl    = 0x0010_0000;
        public static final int AutoBlankScreen     = 0x0020_0000;
        public static final int AutoSystemLock      = 0x0040_0000;
        public static final int AutoMouseTrace      = 0x0080_0000;

        public static final int AutoRecord          = 0x1000_0000;
        public static final int RemoveBackground    = 0x2000_0000;
        public static final int DisplayOutline      = 0x4000_0000;

        public static final int _DEFAULT = RemoveBackground | AutoKeyMouseCtrl;
    }

    public static class RcpChannel {
        public static final int DATA = 0;
        public static final int SCREEN = 1;
        public static final int SFTP = 2;
        public static final int RVS = 3;
        public static final int PRINT = 4;
        public static final int VOICE = 5;
        public static final int SOUND = 6;
        public static final int DRAGDROP_SFTP = 7;
        public static final int WHITEBOARD = 8;
        public static final int HXVIDEO = 60;
        public static final int HXAUDIO = 61;
        public static final int SESSION = 99;
        public static final int LAST = 100;
    }

    /**
     * Originally located GlobalStatic.setTextSize()
     */
    public static class TextSize {
        public static final int NORMAL = 17;
        public static final int TITLE = 18;
        public static final int COPYRIGHT = 10;
        public static final int AGENTEXPLAIN = 12;
        public static final int OSNAME = 11;
        public static final int PRODUCT_NAME = 16;
    }

    public static final int PORT_RANGE_START = 9500;
    public static final int PORT_SCAN_MAX_COUNT = 20;
    public static final Charset CHARSET_UTF8 = Charset.forName("UTF-8");
    public static final int DIRECT_EXEC_INPUT_LIMIT = 5;

    public static class OAuthLogin {
        public static final String ENABLE = "Y";
        public static final String DISABLE = "N";
    }
}
