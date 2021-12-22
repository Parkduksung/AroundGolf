package com.rsupport.rv.viewer.sdk.common;


public class ComConstant {
	//Agent_expired_check
	public static final String AGENT_OK = "0";		//Available
	public static final String AGENT_EXPIRED = "1";	//Expiration of service life
	public static final String AGENT_OVER = "2";	//Exceeded Agent number
	
	public static final int RANGE_TYPE_IP = 0;
	public static final int RANGE_TYPE_MAC = 1;

	public static final int NETWORK_TYPE_IPV4 = 4;
	public static final int NETWORK_TYPE_IPV6 = 6;

	public static String PRODUCT_REMOTECALL_KEY = "RCALL";
	public static String PRODUCT_REMOTEHELPER_KEY = "RHELP";
	public static String PRODUCT_REMOTESALES_KEY = "RSALE";

	public static String OK = "100";
	public static String ERR_WEBPAGE_ERROR = "999";
	public static String NOT_READY = "998";

	public static String VIEWER_ERR_NOTREGISTRED = "101";
	public static String VIEWER_ERR_INVALID_ACCOUNT = "102";
	public static String VIEWER_ERR_INVALID_IP = "103";
	public static String VIEWER_ERR_NO_MAC = "104";
	public static String VIEWER_ERR_INVALID_MAC = "105";
	public static String VIEWER_ERR_LOGIN_FAIL_COUNT_OVER = "106";
	public static String VIEWER_ERR_INVALID_PINCODE = "107";
	public static String VIEWER_ERR_EXPIRED = "108";
	public static String VIEWER_ERR_USING_NOW = "109";
	public static String VIEWER_ERR_INVALID_LICENSE = "110";
	public static String VIEWER_ERR_INVALID_LICENSE_COUNT = "111";
	public static String VIEWER_ERR_OVER_SESSION = "113";
	public static String VIEWER_ERR_OVER_CONSURRENT_USER_COUNT = "114";
	public static String VIEWER_ERR_DENY_PRODUCT_USER = "120";	
	
	public static String VIEWER_ERR_NEED_UPGRADE = "200";

	public static String VIEWER_ERR_WAITNUMBER = "301";
	public static String VIEWER_ERR_DATABASE_UPDATE_FALSE = "302";
	public static String VIEWER_ERR_GUID_EMPTY = "990";

	public static String LOGOUT_CANCEL_OK = "101";
	public static String LOGOUT_INSERLOG_OK = "102";
	public static String LOGOUT_ERROR = "999";
	public static String LOGOUT_NODATA = "901";
	public static String LOGOUT_NOPARAM = "902";

	public static int rcpFuncKB = 0x00000001;
	public static int rcpFuncMO	= 0x00000002;
	public static int rcpFuncDR	= 0x00000004;
	public static int rcpFuncSF	= 0x00000008;
	public static int rcpFuncVoice = 0x00000010;
	public static int rcpFuncRS	= 0x00000020;
	public static int rcpFuncURLP = 0x00000040;
	public static int rcpFuncCT	= 0x00000080;
	public static int rcpFuncPL	= 0x00000100;
	public static int rcpFuncSI	= 0x00000200;
	public static int rcpFuncSC	= 0x00000400;
	public static int rcpFuncCA	= 0x00000800;
	public static int rcpFuncCB	= 0x00001000;
	public static int rcpFuncFV	= 0x00002000;
	public static int rcpFuncRC	= 0x00004000;
	public static int rcpFuncCtrlAltDel = 0x00008000;
	public static int rcpFuncBlankScreen = 0x00010000;
	public static int rcpFuncMouseTrace	= 0x00020000;
	public static int rcpFuncLaserPointer = 0x00040000;
	public static int rcpFuncWhiteBoard = 0x00080000;
	public static int rcpFuncAPSH = 0x00100000;
	public static int rcpFuncCamChat = 0x00200000;
	public static int rcpFuncSoundShare = 0x00400000;
	public static int rcpFuncSF_DnD = 0x00800000;
	public static int rcpFuncCrossRemoteCtrl = 0x01000000;
	public static int rcpFuncPrint = 0x02000000;
	public static int rcpFuncSessionShare = 0x04000000;
	public static int rcpFuncLock = 0x08000000;
	public static int rcpFuncRCSafeMode = 0x10000000;
	public static int rcpFuncRCAnotherAccount	= 0x20000000;

	public static int rcpOptAP = 0x00000001;
	public static int rcpOptUP = 0x00000002;
	public static int rcpOptNP = 0x00000004;
	public static int rcpOptNC = 0x00000008;
	public static int rcpOptCB_Auto = 0x00000010;
	public static int rcpOpt_LocalRecord = 0x00000020;
	public static int rcpOpt_ServerRecord = 0x00000040;
	public static int rcpOpt_ChangeAccount = 0x00000080;
	public static int rcpOpt_AutoDeleteModule = 0x00000100;
	public static int rcpOpt_ConnectMail = 0x00000200;
	public static int rcpOpt_AutoConfirmSS = 0x00000400;
	public static int rcpOpt_CopyClipboard = 0x00000800;
	public static int rcpOpt_ShowConnectCode = 0x00004000;
	public static int rcpOpt_ShowBrowserInfo = 0x00008000;
	
	public static int rcpOptAutoKeyMouseCtrl = 0x00100000;
	public static int rcpOptAutoBlankScreen	= 0x00200000;
	public static int rcpOptAutoSystemLock = 0x00400000;
	public static int rcpOptAutoMouseTrace = 0x00800000;
	public static int rcpOpt_autorecord = 0x10000000;
	public static int rcpOpt_remove_background = 0x20000000;
	public static int rcpOpt_display_outline = 0x40000000;

	public static final int	rcpAFuncNone				= 0x00000000;
	public static final int rcpAFuncConnectMail			= 0x00000001;
	public static final int rcpAFuncReserve				= 0x00000002;
	public static final int rcpAFuncReport				= 0x00000004;
	public static final int rcpAFuncCamChat				= 0x00000008;
	public static final int rcpAFuncVoice				= 0x00000010;
	public static final int rcpAFuncCopyClipboard		= 0x00000020;
	public static final int rcpAFuncSC			    	= 0x00000400;
	
	public static final int	IME_CHECK_TIMEOUT 			= 100;
	public static final int	IME_LANG_NONE 				= 0;
	public static final int	IME_LANG_DEFAULT 			= 1;
	public static final int	IME_LANG_ENG 				= 2;
	public static final int	IME_LANG_KOR 				= 3;
	public static final int	IME_LANG_JAP_HITAKANA 		= 4;
	public static final int	IME_LANG_JAP_KATAKANA 		= 5;
	public static final int	IME_LANG_JAP_ENGLISH 		= 6;
	public static final int	IME_LANG_MASK 				= 0x0F;
	public static final int	IME_LANG_CAPTICAL 			= 0x10;
	public static final int	IME_LANG_KANAMODE 			= 0x20;
	public static final int	IME_LANG_FULLSHAPE			= 0x40;
	public static final int	IME_KEYBD_DEFAULT			= 0x00000000;
	public static final int	IME_KEYBD_ENGLISH			= 0x01000000;
	public static final int	IME_KEYBD_KOREAN			= 0x02000000;
	public static final int	IME_KEYBD_JAPANESE			= 0x03000000;
	public static final int	IME_KEYBD_MASK				= 0xFF000000;
	
	public static final int PROTOCOL_CAM_START 	= 150;
	
	public static final int rcpCamInitEngine	= PROTOCOL_CAM_START;
	public static final int rcpCamInitScreen 	= PROTOCOL_CAM_START + 1;
	public static final int rcpCamZipState 		= PROTOCOL_CAM_START + 2;
	public static final int rcpCamCacheState 	= PROTOCOL_CAM_START + 3;
	public static final int rcpCamCursorState 	= PROTOCOL_CAM_START + 4;
	
	public static final int PROTOCOL_CAM_LAST 	= PROTOCOL_CAM_START + 5;
	
	public static final int ZSTREAMSIZE = 56;
	
	//rcpSpecialKeyStateType
	public static final short rcpKeyShift = 0x08;
	
	/*
	 * RemoteView Code Start
	 */
	public static final int rcpExOptInvite				= 0x00000001;
	public static final String Product_RemoteView = "202";
	/*
	 * RemoteView Code End 
	 */
	
	public static final int VK_HOME = 0xff50;
	public static final int VK_LEFT = 0xff51;
	public static final int VK_UP = 0xff52;
	public static final int VK_RIGHT = 0xff53;
	public static final int VK_DOWN = 0xff54;
	public static final int VK_PAGE_UP = 0xff55;
	public static final int VK_PAGE_DOWN = 0xff56;
	public static final int VK_END = 0xff57;
	public static final int VK_INSERT = 0xff63;
	public static final int VK_F1 = 0xffbe;
	public static final int VK_F2 = 0xffbf;
	public static final int VK_F3 = 0xffc0;
	public static final int VK_F4 = 0xffc1;
	public static final int VK_F5 = 0xffc2;
	public static final int VK_F6 = 0xffc3;
	public static final int VK_F7 = 0xffc4;
	public static final int VK_F8 = 0xffc5;
	public static final int VK_F9 = 0xffc6;
	public static final int VK_F10 = 0xffc7;
	public static final int VK_F11 = 0xffc8;
	public static final int VK_F12 = 0xffc9;
	public static final int VK_CAPS_LOCK = 0xffe5;
//	public static final int VK_WINDOWS = 91;
	public static final int VK_NUM_LOCK = 0xFF7F;

	public static final int VK_CTRL = 0xFFE3;
	public static final int VK_SHIFT = 0xFFE1;
	public static final int VK_META = 0xffe7;
	public static final int VK_ALT = 0xFFE9;
	
	public static final int VK_MOUSE_RIGHT = 0xFFEF;
	public static final int VK_CTRL_RIGHT = 0xFFE4;
	public static final int VK_PRT_SC = 0xFF61;
	
	public static final int KEY_FIRST = 400;
    public static final int KEY_LAST = 402;
    public static final int KEY_TYPED = 400;
    public static final int KEY_PRESSED = 401;
    public static final int KEY_RELEASED = 402;
    public static final int VK_ENTER = 0xff0d;
    public static final int VK_BACK_SPACE = 0xFF08;
    public static final int VK_TAB = 0xFF09;
    public static final int VK_CANCEL = 3;
    public static final int VK_CLEAR = 12;
    public static final int VK_CONTROL = 17;
    public static final int VK_PAUSE = 19;
    public static final int VK_ESCAPE = 0xff1b;
    public static final int VK_SPACE = 32;
    public static final int VK_COMMA = 44;
    public static final int VK_MINUS = 45;
    public static final int VK_PERIOD = 46;
    public static final int VK_SLASH = 47;
    public static final int VK_0 = 48;
    public static final int VK_1 = 49;
    public static final int VK_2 = 50;
    public static final int VK_3 = 51;
    public static final int VK_4 = 52;
    public static final int VK_5 = 53;
    public static final int VK_6 = 54;
    public static final int VK_7 = 55;
    public static final int VK_8 = 56;
    public static final int VK_9 = 57;
    public static final int VK_SEMICOLON = 59;
//    public static final int VK_EQUALS = 61;
    public static final int VK_A = 65;
    public static final int VK_B = 66;
    public static final int VK_C = 67;
    public static final int VK_D = 68;
    public static final int VK_E = 69;
    public static final int VK_F = 70;
    public static final int VK_G = 71;
    public static final int VK_H = 72;
    public static final int VK_I = 73;
    public static final int VK_J = 74;
    public static final int VK_K = 75;
    public static final int VK_L = 76;
    public static final int VK_M = 77;
    public static final int VK_N = 78;
    public static final int VK_O = 79;
    public static final int VK_P = 80;
    public static final int VK_Q = 81;
    public static final int VK_R = 82;
    public static final int VK_S = 83;
    public static final int VK_T = 84;
    public static final int VK_U = 85;
    public static final int VK_V = 86;
    public static final int VK_W = 87;
    public static final int VK_X = 88;
    public static final int VK_Y = 89;
    public static final int VK_Z = 90;
    public static final int VK_BACK_SLASH = 92;
    public static final int VK_EQUALS = 61;
    public static final int VK_BACK_QUOTE = 192;
    public static final int VK_COMPOSE = 65312;
    public static final int VK_OPEN_BRACKET = 91;
    public static final int VK_CLOSE_BRACKET = 93;
//    public static final int VK_NUMPAD0 = 96;
//    public static final int VK_NUMPAD1 = 97;
//    public static final int VK_NUMPAD2 = 98;
//    public static final int VK_NUMPAD3 = 99;
//    public static final int VK_NUMPAD4 = 100;
//  
    public static final int VK_NUMPAD5 = 101;
//    public static final int VK_NUMPAD6 = 102;
//    public static final int VK_NUMPAD7 = 103;
//    public static final int VK_NUMPAD8 = 104;
//    public static final int VK_NUMPAD9 = 105;
//    public static final int VK_MULTIPLY = 106;
//    public static final int VK_ADD = 107;
//    public static final int VK_SEPARATER = 108;
//    public static final int VK_SEPARATOR = 108;
//    public static final int VK_SUBTRACT = 109;
//    public static final int VK_DECIMAL = 110;
//    public static final int VK_DIVIDE = 111;
//    public static final int VK_DELETE = 127;
    public static final int VK_DELETE = 65535;
//    public static final int VK_SCROLL_LOCK = 145;
//    public static final int VK_PRINTSCREEN = 154;
//    public static final int VK_HELP = 156;
    public static final int VK_QUOTE = 39;
//    public static final int VK_KP_UP = 224;
//    public static final int VK_KP_DOWN = 225;
//    public static final int VK_KP_LEFT = 226;
//    public static final int VK_KP_RIGHT = 227;
//    public static final int VK_DEAD_GRAVE = 128;
//    public static final int VK_DEAD_ACUTE = 129;
//    public static final int VK_DEAD_CIRCUMFLEX = 130;
//    public static final int VK_DEAD_TILDE = 131;
//    public static final int VK_DEAD_MACRON = 132;
//    public static final int VK_DEAD_BREVE = 133;
//    public static final int VK_DEAD_ABOVEDOT = 134;
//    public static final int VK_DEAD_DIAERESIS = 135;
//    public static final int VK_DEAD_ABOVERING = 136;
//    public static final int VK_DEAD_DOUBLEACUTE = 137;
//    public static final int VK_DEAD_CARON = 138;
//    public static final int VK_DEAD_CEDILLA = 139;
//    public static final int VK_DEAD_OGONEK = 140;
//    public static final int VK_DEAD_IOTA = 141;
//    public static final int VK_DEAD_VOICED_SOUND = 142;
//    public static final int VK_DEAD_SEMIVOICED_SOUND = 143;
//    public static final int VK_AMPERSAND = 150;
//    public static final int VK_ASTERISK = 151;
    public static final int VK_QUOTEDBL = 152;
//    public static final int VK_LESS = 153;
//    public static final int VK_GREATER = 160;
//    public static final int VK_BRACELEFT = 161;
//    public static final int VK_BRACERIGHT = 162;
//    public static final int VK_AT = 512;
//    public static final int VK_COLON = 513;
//    public static final int VK_CIRCUMFLEX = 514;
//    public static final int VK_DOLLAR = 515;
//    public static final int VK_EURO_SIGN = 516;
//    public static final int VK_EXCLAMATION_MARK = 517;
//    public static final int VK_INVERTED_EXCLAMATION_MARK = 518;
//    public static final int VK_LEFT_PARENTHESIS = 519;
//    public static final int VK_NUMBER_SIGN = 520;
//    public static final int VK_PLUS = 521;
//    public static final int VK_RIGHT_PARENTHESIS = 522;
//    public static final int VK_UNDERSCORE = 523;
//    public static final int VK_FINAL = 24;
//    public static final int VK_WINDOWS 		= 524; 
    public static final int VK_HANJA 		= 0x19;
    public static final int VK_PROCESSKEY 	= 0xE5;
    public static final int VK_HANGUL		= 0x15;
    public static final int VK_LWIN 		= 0x5B;
    public static final int VK_RWIN 		= 0x5C;
	
    public static final int KEY_HANGULE		= 0xFFEB;
    public static final int KEY_LWIN		= 0xFFEC;
    public static final int KEY_RWIN		= 0xFFED;
    public static final int KEY_HANJA		= 0xFFF0;
    public static final int KEY_ALT_L		= 0xFFE9;
    public static final int KEY_TAB			= 0xFF09;
    public static final int KEY_F4			= 0xFF94;
//    public static final int VK_CONTEXT_MENU = 525;
//    public static final int VK_CONVERT = 28;
//    public static final int VK_NONCONVERT = 29;
//    public static final int VK_ACCEPT = 30;
//    public static final int VK_MODECHANGE = 31;
//    public static final int VK_KANA = 21;
//    public static final int VK_KANJI = 25;
//    public static final int VK_ALPHANUMERIC = 240;
//    public static final int VK_KATAKANA = 241;
//    public static final int VK_HIRAGANA = 242;
//    public static final int VK_FULL_WIDTH = 243;
//    public static final int VK_HALF_WIDTH = 244;
//    public static final int VK_ROMAN_CHARACTERS = 245;
//    public static final int VK_ALL_CANDIDATES = 256;
//    public static final int VK_PREVIOUS_CANDIDATE = 257;
//    public static final int VK_CODE_INPUT = 258;
//    public static final int VK_JAPANESE_KATAKANA = 259;
//    public static final int VK_JAPANESE_HIRAGANA = 260;
//    public static final int VK_JAPANESE_ROMAN = 261;
//    public static final int VK_KANA_LOCK = 262;
//    public static final int VK_INPUT_METHOD_ON_OFF = 263;
//    public static final int VK_CUT = 65489;
//    public static final int VK_COPY = 65485;
//    public static final int VK_PASTE = 65487;
//    public static final int VK_UNDO = 65483;
//    public static final int VK_AGAIN = 65481;
//    public static final int VK_FIND = 65488;
//    public static final int VK_PROPS = 65482;
//    public static final int VK_STOP = 65480;
//    public static final int VK_COMPOSE = 65312;
//    public static final int VK_ALT_GRAPH = 65406;
//    public static final int VK_BEGIN = 65368;
//    public static final int VK_UNDEFINED = 0;
//    public static final char CHAR_UNDEFINED = (char)(-1);
//    public static final int KEY_LOCATION_UNKNOWN = 0;
//    public static final int KEY_LOCATION_STANDARD = 1;
//    public static final int KEY_LOCATION_LEFT = 2;
//    public static final int KEY_LOCATION_RIGHT = 3;
//    public static final int KEY_LOCATION_NUMPAD = 4;
    
    public static final short AVK_Q 		= 45;
    public static final short AVK_W 		= 51;
    public static final short AVK_E 		= 33;
    public static final short AVK_R 		= 46;
    public static final short AVK_T 		= 48;
    public static final short AVK_Y 		= 53;
    public static final short AVK_U 		= 49;
    public static final short AVK_I 		= 37;
    public static final short AVK_O 		= 43;
    public static final short AVK_P 		= 44;
    public static final short AVK_A 		= 29;
    public static final short AVK_S 		= 47;
    public static final short AVK_D 		= 32;
    public static final short AVK_F 		= 34;
    public static final short AVK_G 		= 35;
    public static final short AVK_H 		= 36;
    public static final short AVK_J 		= 38;
    public static final short AVK_K 		= 39;
    public static final short AVK_L 		= 40;
    public static final short AVK_Z 		= 54;
    public static final short AVK_X 		= 52;
    public static final short AVK_C 		= 31;
    public static final short AVK_V 		= 50;
    public static final short AVK_B 		= 30;
    public static final short AVK_N 		= 42;
    public static final short AVK_M 		= 41;
    public static final short AVK_SLASH 	= 76;
    public static final short AVK_SHIFT 	= 59;
	public static final short AVK_HOME 		= 82;
	public static final short AVK_LANG 		= 92;
	public static final short AVK_SPACE 	= 62;
	public static final short AVK_SPACIAL 	= 57;
	public static final short AVK_BACK 		= 67;
	public static final short AVK_ENTER 	= 66;
	public static final short AVK_LEFT		= 21;
	public static final short AVK_RIGHT		= 22;
	public static final short AVK_UP		= 19;
	public static final short AVK_DOWN		= 20;
	public static final short AVK_TAB		= 61;
//	public static final short AVK_HOME 		= 82;
	public static final short AVK_END 		= 123;
	public static final short AVK_PAGEUP	= 92;
	public static final short AVK_PAGEDOWN	= 93;
//	public static final short AVK_LANG 		= 92;
	public static final short AVK_CTRL		= 113;
	public static final short AVK_DEL		= 112;
	public static final short AVK_ESC		= 111;
	public static final short AVK_F1		= 131;
	public static final short AVK_F2		= 132;
	public static final short AVK_F3		= 133;
	public static final short AVK_F4		= 134;
	public static final short AVK_F5		= 135;
	public static final short AVK_F6		= 136;
	public static final short AVK_F7		= 137;
	public static final short AVK_F8		= 138;
	public static final short AVK_F9		= 139;
	public static final short AVK_F10		= 140;
	public static final short AVK_F11		= 141;
	public static final short AVK_F12		= 142;
	
	//AGENT_STATUS
	public static final short AGENT_STATUS_NOLOGIN		= 0x00;	//0, Not logged in after installation
	public static final short AGENT_STATUS_LOGIN		= 0x01;	//1, Agent is logged in. 
	public static final short AGENT_STATUS_LOGOUT		= 0x02;	//2. Agent is logged out.
	public static final short AGENT_STATUS_REMOTING		= 0x03;	//3. Agent is remoting.

	//PC_Type
	public static final short RVFLAG_KEY_NONE		= 0x00;	//0
	public static final short RVFLAG_KEY_VPRO		= 0x01;	//1, vPro
	public static final short RVFLAG_KEY_RWT		= 0x02;	//2, RWT
	public static final short RVFLAG_KEY_HVS		= 0x10;	//16, HyperV Server
	public static final short RVFLAG_KEY_HVI		= 0x11;	//17, HyperV Image
	public static final short RVFLAG_KEY_VMI		= 0x12;	//18, VM Image
	public static final short RVFLAG_KEY_VHDI		= 0x13;	//19, VirtualPC Image
	public static final short RVFLAG_KEY_VBOX		= 0x14; //20, VBox Image
	public static final short RVFLAG_KEY_XGEN		= 0x15;	//21, XGen Image
	public static final short RVFLAG_KEY_UBUN		= 0x30;	//48, Linux - Ubuntu
	public static final short RVFLAG_KEY_CENT		= 0x31;	//49, Linux - CentOS
	public static final short RVFLAG_KEY_KVM		= 0x32;	//50  KVM
	public static final short RVFLAG_KEY_REMOTEWOL	= 0x33;	//51, Remote WOL
	public static final short RVFLAG_KEY_HWWOL		= 0x34;	//52, HW WOL
	public static final short RVFLAG_KEY_MAC		= 0x40;	//64, MacOS X
	public static final short RVFLAG_KEY_ANDROID	= 0x50;	//80, ANDROID
	public static final short RVFLAG_KEY_ANDROID_TABLET = 0x51; //81, Android tablet
	public static final short RVFLAG_KEY_IPHONE     = 0x52;	//82, iPhonen
	public static final short RVFLAG_KEY_IPAD		= 0x53; //83, iPad
	
	//vPro_AMT_Power_Status
	public static final short VPRO_AMT_POWER_RESET	= 0x10; //16
	public static final short VPRO_AMT_POWER_ON		= 0x11; //17
	public static final short VPRO_AMT_POWER_OFF	= 0X12;	//18
	
	//vPro_AMT_Error_case
	public static final int AMTERR_NONE			= 0;
	public static final int AMTERR_NOTACCESS	= 1;
	public static final int AMTERR_INVALIDAUTH	= 2;
	
//	public static final long RVPOPTS_RVPRO = 0x0200;	//33554432, 	VPRO (License)
	
	//RView Server Privilege Values(Season 1)
	public static final long RVPOPTS_RCONTROL 			= 0x0001;
	public static final long RVPOPTS_REXPLORER 			= 0x0002;
	public static final long RVPOPTS_RSCAPTURE          = 0x0004;
	public static final long RVPOPTS_RPROCESS           = 0x0008;
	public static final long RVPOPTS_RSYSTEM            = 0x0010;
	public static final long RVPOPTS_RVPN               = 0x0020;  	  //         - 32
	public static final long RVPOPTS_RVPRO              = 0x0040;  	  //         - 64
	public static final long RVPOPTS_RV_ALL             = 0xFFFF;
	//RView Server Privilege Values(Season 2)
	public static final long RVPOPTS2_RCONTROL          = 0x00010000;
	public static final long RVPOPTS2_RCONTROL_FILE     = 0x00000001;
	public static final long RVPOPTS2_RCONTROL_CAPTURE  = 0x00000002;
	public static final long RVPOPTS2_RCONTROL_RPRINT   = 0x00000004;
	public static final long RVPOPTS2_RCONTROL_CAM      = 0x00000008;
	public static final long RVPORTS2_RCONTROL_CLIP     = 0x00000010;
	public static final long RVPOPTS2_RCONTROL_ALL      = 0x000100FF;
	public static final long RVPOPTS2_REXPLORER         = 0x00020000;
	public static final long RVPOPTS2_EXTEND_IVPN       = 0x01000000;
	public static final long RVPOPTS2_EXTEND_VPRO       = 0x02000000;
	public static final long RVPOPTS2_EXTEND_MOBILE     = 0x04000000;
	public static final long RVPOPTS2_EXTEND_VIRTUAL    = 0x08000000;
	public static final long RVPORTS2_EXTEND_LIVEVIEW   = 0x10000000;
	public static final long RVPOPTS2_ALL               = 0xFFFFFFFF;
	
	public static final short WEB_ERR							= 0;
	public static final short WEB_ERR_NO						= WEB_ERR+110;
	public static final short WEB_ERR_INVALID_PARAMETER			= WEB_ERR+111;
	public static final short WEB_ERR_NOT_FOUND_USERID			= WEB_ERR+112;
	public static final short WEB_ERR_NOT_FOUND_AGENTID			= WEB_ERR+113;
	public static final short WEB_ERR_INVALID_USER_ACCOUNT		= WEB_ERR+114;
	public static final short WEB_ERR_ALREADY_USINGSESSION		= WEB_ERR+115;
	public static final short WEB_ERR_BLOCK_MOBILELOGIN			= WEB_ERR+116;
	public static final short WEB_ERR_INVITE_EXPIRED			= WEB_ERR+120;
	public static final short WEB_ERR_INVITE_ALREADY			= WEB_ERR+121;
	public static final short WEB_ERR_APP_VERSION				= WEB_ERR+130;
	public static final short WEB_ERR_INVAILD_COMPANYID			= WEB_ERR+131;
	public static final short WEB_ERR_NEED_SWITCH_MEMBER		= WEB_ERR+132;
	public static final short WEB_ERR_NEED_UPGRADE_MEMBER		= WEB_ERR+133;
	public static final short WEB_ERR_AES_NOT_FOUND_USERID      = WEB_ERR+140;
	public static final short WEB_ERR_AES_INVALID_USER_ACCOUNT  = WEB_ERR+141;
	public static final short WEB_ERR_USER_ACCOUNT_LOCK         = WEB_ERR+142;
	public static final short WEB_ERR_OTP_AUTH_FAIL				= WEB_ERR+145;
	public static final short WEB_ERR_WOL_NOT_FOUND_AGENT	    = WEB_ERR+146;
	public static final short WEB_ERR_ARP_NOT_FOUND_AGENT	    = WEB_ERR+147;
	public static final short WEB_ERR_ALREADY_SAME_WORKING		= WEB_ERR+211;
	public static final short WEB_ERR_ALREADY_DELETE_AGENTID	= WEB_ERR+212;
	public static final short WEB_ERR_AGENT_NOT_LOGIN			= WEB_ERR+213;
	public static final short WEB_ERR_ONLY_WEBSETUP				= WEB_ERR+214;
	public static final short WEB_ERR_AGENT_EXPIRED				= WEB_ERR+215;
	public static final short WEB_ERR_INVALID_MACADDRESS		= WEB_ERR+300;
	public static final short WEB_ERR_INVALID_LOCALIP			= WEB_ERR+301;
	public static final short WEB_ERR_INVALID_ROLE				= WEB_ERR+310;
	public static final short WEB_ERR_LIC_EXPIRED				= WEB_ERR+405;
	public static final short WEB_ERR_LIC_SERVICE_ERROR			= WEB_ERR+406;
	public static final short WEB_ERR_ACTIVE					= WEB_ERR+413;
	public static final short WEB_ERR_UNAUTH_MAC_ADDRESS		= WEB_ERR+600;
	public static final short WEB_ERR_UNAUTH_DEVICE				= WEB_ERR+601;
	public static final short WEB_ERR_UNAUTH_USER				= WEB_ERR+700;
	public static final short WEB_ERR_UNAUTH_PASSWORD_FAIL		= WEB_ERR+701;
	public static final short WEB_ERR_PASSWORD_EXPIRED  		= WEB_ERR+702;
	public static final short WEB_ERR_DIRECT_EXEC_EXPIRED       = WEB_ERR+704;
	public static final short WEB_ERR_OTP_AUTH_FAILED 	 		= WEB_ERR+826;
	public static final short WEB_ERR_OTP_KEY_INVALID  			= WEB_ERR+840;
    public static final short WEB_ERR_SERVICE_LIMIT_TIME  		= WEB_ERR+846;
    public static final short WEB_ERR_NO_EMAIL                  = WEB_ERR+850;
	public static final short WEB_ERR_ADMINISTRATORS_LIMIT_CONNECT = WEB_ERR+852;
	public static final short WEB_ERR_DIFFERENT_LOGIN_IP_AND_REMOTE_IP = WEB_ERR+882;
	public static final short WEB_ERR_NO_AUTH_EMAIL             = WEB_ERR+824;
	
	public final static short WEB_ERR_LGU_NORMAL                           = WEB_ERR + 611;
	public final static short WEB_ERR_LGU_NON_PARTICIPATION_PARTY_SYSTEM   = WEB_ERR + 612;
	public final static short WEB_ERR_LGU_USING_EXTERNAL_SYSTEM_PAUSED     = WEB_ERR + 613;
	public final static short WEB_ERR_LGU_LOST_PHONE_STATUS                = WEB_ERR + 614;
	public final static short WEB_ERR_LGU_NON_PARTICIPATION_PARTY_SERVICE  = WEB_ERR + 615;
	
	public static final short WEB_ERR_SQL_ERROR					= WEB_ERR+911;
    
    public static final int CMD_ERR								= 90000;
    public static final int CMD_ERR_NOAGENT						= CMD_ERR+100;
    public static final int CMD_ERR_TOKEN						= CMD_ERR+200;
    public static final int CMD_ERR_COMSND						= CMD_ERR+300;
    public static final int CMD_ERR_DUPAGENT					= CMD_ERR+400;
    public static final int CMD_ERR_SESSION_CONNECT_FAIL		= CMD_ERR+500;
    public static final int CMD_ERR_SESSION_SEND_FAIL			= CMD_ERR+501;
    public static final int CMD_ERR_SESSION_SOCKET_FAIL			= CMD_ERR+502;
    public static final int CMD_ERR_ALREADY_REMOTE_CONTROL		= CMD_ERR+503;
    public static final int CMD_ERR_OPTION_FILE_CREATE_FAIL		= CMD_ERR+504;
    public static final int CMD_ERR_PROCESS_EXCUTE_FAIL			= CMD_ERR+505;
    public static final int CMD_ERR_PROCESS_KILL_FAIL			= CMD_ERR+506;
    public static final int CMD_ERR_GET_PROCESS_LIST_FAIL		= CMD_ERR+507;
    public static final int CMD_ERR_AGENT_RESET_CONFIG_FAIL		= CMD_ERR+508;
    public static final int CMD_ERR_NOT_ALLOWED_IP_ADDRESS		= CMD_ERR+509;
    public static final int CMD_ERR_GET_SCREENSHOT_FAIL			= CMD_ERR+510;
    public static final int CMD_ERR_GET_SYSTEM_INFO_FAIL		= CMD_ERR+511;
    public static final int CMD_ERR_AMT_EXECUTE_FAIL			= CMD_ERR+512;
    public static final int CMD_ERR_CSSLEEP_EXECUTE_FAIL		= CMD_ERR+513;
    public static final int CMD_ERR_CSWAKEUP_EXECUTE_FAIL		= CMD_ERR+514;
    public static final int CMD_ERR_REMOTE_CONNECT_FAIL			= CMD_ERR+515;
    public static final int CMD_ERR_AMT_INVALID_AUTH			= CMD_ERR+517;
    public static final int CMD_ERR_AMT_NOT_ACCESS				= CMD_ERR+518;
    public static final int CMD_ERR_AMT_INVALID_COMMAND			= CMD_ERR+519;
    public static final int CMD_ERR_SYSTEM_REBOOT_FAIL			= CMD_ERR+520;
    public static final int CMD_ERR_AGENTCONNECT_UNABLE			= CMD_ERR+521;
    public static final int CMD_ERR_ALREADY_REMOTE_CONTROL_WEB_VIEWER =  CMD_ERR+542;
    public static final int CMD_ERR_ETC							= CMD_ERR+9000;
    
    public static final short NET_ERR					= 10000;
    public static final short NET_ERR_BIND				= NET_ERR+100;
    public static final short NET_ERR_CONNECT			= NET_ERR+200;
    public static final short NET_ERR_HTTPRETRY			= NET_ERR+300;
    public static final short NET_ERR_MALFORMED			= NET_ERR+400;
    public static final short NET_ERR_NOROUTE			= NET_ERR+500;
    public static final short NET_ERR_PORTUNREACHABLE	= NET_ERR+600;
    public static final short NET_ERR_PROTOCOL			= NET_ERR+700;
    public static final short NET_ERR_TIMEOUT			= NET_ERR+800;
    public static final short NET_ERR_UNKNOWNSERVER		= NET_ERR+900;
    public static final short NET_ERR_UNKNOWNHOST		= NET_ERR+110;
    public static final short NET_ERR_SOCKET			= NET_ERR+120;
    public static final short NET_ERR_EXCEPTION			= NET_ERR+130;
    public static final short NET_ERR_WAS				= NET_ERR+140;
	public static final short NET_ERR_PROXYINFO_NULL	= NET_ERR+301;
	public static final short NET_ERR_PROXY_VERIFY		= NET_ERR+302;
	
	public static final short OTP_ERR					= 5000;
	public static final short OTP_ERR_NO_INSTALL		= OTP_ERR + 100;
	public static final short OTP_ERR_NO_AUTHINFO		= OTP_ERR + 101;
	public static final short OTP_ERR_INVALID_USER		= OTP_ERR + 102;
	public static final short OTP_ERR_FAIL_AUTH			= OTP_ERR + 103;
	public static final short OTP_ERR_WRONG_PWD			= OTP_ERR + 104;
	
//	public static final String RV_REMOTEDOWNLOAD = "REMOTEDOWNLOAD";
	public static final String RV_REMOTECONTROL  = "REMOTECONTROL";
	public static final String RV_REMOTEEXPLORER = "REMOTEEXPLORER";
	public static final String RV_GETSCREENSHOT  = "GETSCREENSHOT";
	public static final String RV_SYSTEMLOGOFF   = "SYSTEMLOGOFF";
	public static final String RV_SYSTEMSHUTDOWN = "SYSTEMSHUTDOWN";
	public static final String RV_SYSTEMREBOOT   = "SYSTEMREBOOT";
	public static final String RV_PROCESSEXECUTE = "PROCESSEXECUTE";
	public static final String RV_PROCESSLIST    = "PROCESSLIST";
	public static final String RV_PROCESSKILL    = "PROCESSKILL";
	public static final String RV_SYSTEMINFO     = "SYSTEMINFO";
	public static final String RV_AGENTRESTART   = "AGENTRESTART";
	public static final String RV_REMOTEINVITE   = "REMOTEINVITE";
	public static final String RV_AGENTUNINSTALL = "AGENTUNINSTALL";
	public static final String RV_WAKEONLAN      = "RWAKEONLAN";
	public static final String RV_WAKEONLANALL   = "RWAKEONLANALL";

	public static final short RVVIEWER = 5000;
	public static final short RVWEBSVR = 3000;
	public static final short VIEWER_PROCESS_LIST_REQUEST		= RVVIEWER+0x03;
	public static final short VIEWER_PROCESS_KILL_REQUEST		= RVVIEWER+0x04;
	public static final short VIEWER_PROCESS_EXECUTE_REQUEST	= RVVIEWER+0x05;
	public static final short VIEWER_SYSTEM_REBOOT_REQUEST 		= RVVIEWER+0x06;
	public static final short VIEWER_REMOTE_CONTROL_REQUEST		= RVVIEWER+0x07;
	public static final short VIEWER_REMOTE_EXPLORER_REQUEST	= RVVIEWER+0x08;
	public static final short VIEWER_AGENT_RESTART_REQUEST		= RVVIEWER+0x09;
	public static final short VIEWER_GET_SCREENSHOT_REQUEST		= RVVIEWER+0x0A;
	public static final short VIEWER_SYSTEM_INFO_REQUEST		= RVVIEWER+0x0B;
	public static final short VIEWER_REMOTE_VPN_REQUEST			= RVVIEWER+0x0C;
	public static final short VIEWER_REMOTE_INVITE_REQUEST		= RVVIEWER+0x0D;
	public static final short WEBSVR_AGENT_AUTO_UNINSTALL		= RVWEBSVR+0x05;
	public static final short VIEWER_AGENT_WOL_RELAY			= RVVIEWER+0x19;  // 12-0716 by csh, Wake On Lan Relay 처리
	
	public static final short SECURITYTYPE_DEFAULT				= 0X0000;
	public static final short SECURITYTYPE_WEB_ID				= 0X0001;
	public static final short SECURITYTYPE_WEB_PWD				= 0X0002;
	public static final short SECURITYTYPE_AGENT_ID				= 0X0004;
	public static final short SECURITYTYPE_AGENT_PWD			= 0X0008;
	
	public static final int OPT_LEVEL_SERV_REC			=	1000;
	public static final int OPT_SERV_REC_ON				=	OPT_LEVEL_SERV_REC+1;
	public static final int OPT_SERV_REC_USERNAME		=   OPT_LEVEL_SERV_REC+2;
	public static final int OPT_SERV_REC_PCNAME			=	OPT_LEVEL_SERV_REC+3;
	public static final int OPT_SERV_REC_IP				=	OPT_LEVEL_SERV_REC+4;	// IP 주소
	public static final int OPT_SERV_REC_MAC			=	OPT_LEVEL_SERV_REC+5;	// MAC 주소
	public static final int OPT_SERV_REC_WEB_CALLPAGE	=	OPT_LEVEL_SERV_REC+6;
	public static final int OPT_SERV_REC_WEB_CALLDATA	=	OPT_LEVEL_SERV_REC+7;
	public static final int OPT_SERV_REC_MOBILE_TYPE	=	OPT_LEVEL_SERV_REC+8;	// Rec Type
	public static final int OPT_SERV_REC_MOBILE_NAME	=	OPT_LEVEL_SERV_REC+9;	// Rec Mobile Name
	public static final int OPT_SERV_REC_MOBILE_ENG_VER	=   OPT_LEVEL_SERV_REC+10;	// Mobile Engine Version

	public static int getRVDefaultConfigFunction() {
		return (
				rcpFuncKB |
				rcpFuncMO |
				rcpFuncURLP |
				rcpFuncPL |
				rcpFuncSI |
				rcpFuncSC |
				rcpFuncCA |
				rcpFuncCB |
				rcpFuncFV |
				rcpFuncCtrlAltDel |
				rcpFuncBlankScreen |
				rcpFuncMouseTrace |
				rcpFuncLaserPointer |
				rcpFuncSoundShare |
				rcpFuncSF_DnD |
				rcpFuncLock
				);
	}

	public static int getRVDefaultConfigOption() {
		return (
				rcpOptAutoKeyMouseCtrl |
				rcpOpt_remove_background
				);
	}
}
