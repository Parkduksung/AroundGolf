package com.rsupport.rv.viewer.sdk.setting;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Build.VERSION_CODES;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.rsupport.android.push.RSPushMessaging;
import com.rsupport.rscommon.util.StringUtil;
import com.rsupport.rv.viewer.sdk.common.ComConstant;
import com.rsupport.rv.viewer.sdk.common.Define;
import com.rsupport.rv.viewer.sdk.common.Enums;
import com.rsupport.rv.viewer.sdk.common.RuntimeData;
import com.rsupport.rv.viewer.sdk.common.log.RLog;
import com.rsupport.rv.viewer.sdk.data.AgentConnectOption;
import com.rsupport.rv.viewer.sdk.data.AppProperty;
import com.rsupport.rv.viewer.sdk.decorder.model.ConnectionInfo;
import com.rsupport.rv.viewer.sdk.decorder.model.HXENGINEHEADER;
import com.rsupport.rv.viewer.sdk.decorder.model.VRVDHeader;
import com.rsupport.rv.viewer.sdk.decorder.model.X264Header;
import com.rsupport.rv.viewer.sdk.pref.SettingPref;
import com.rsupport.rv.viewer.sdk.util.AgentInfo;
import com.rsupport.rv.viewer.sdk.util.DisplayUtil;
import com.rsupport.rv.viewer.sdk_viewer.R;

import java.io.InputStream;
import java.net.NetworkInterface;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GlobalStatic {
	
	//public static String APPVERSION_NAME = "";
	//public static short APPVERSION_CODE = 0;

	public static final int REMOTECALL = 0;
	public static final int REMOTEVIEW = 1;
	
	public static int PRODUCT_TYPE = REMOTEVIEW;

	//원격제어중 웹API용 토큰 갱신 주기
	public static long TOKEN_REFRESH_INTERVAL = (5 * 60 * 1000);
	
	public static final short WIN = 0;
	public static final short MAC = 1;
	public static final short LINUX = 2;
	public static short g_operationType = WIN;
	public static String g_osVersion = "";
	public static final short HOST_WINDOWS = 1;
	public static final short HOST_MAC = 2;
	public static final short HOST_LINUX = 3;
	public static short g_hostOsType = 2;
	public static short g_hostOsMajorVersion = 0;
	public static short g_hostOsMinorVersion = 0;
	public static String g_hostBrowserName = "";
	public static String g_hostBrowserVersion = "";
	public static String g_hostBrowserURL = "";
	public static final short HOST_ACCOUNT_ADMIN = 1;
	public static final short HOST_ACCOUNT_LIMITUSER = 2;
	public static short g_hostAccountType = 1;
	public static final short HOST_APPMODE_SERVICE = 1;
	public static final short HOST_APPMODE_NORMAL = 2;
	public static short g_hostAppMode = 1;
	public static final short CONN_PINCODE = 0;
	public static final short CONN_ICON = 1;
	public static short g_connType = 0; 
	public static final short RECORD_START = 0;
	public static final short RECORD_STOP = 1;
	public static short g_autoRecordOnViewer = 1;
	public static final short AES_OPEN = 0;
	public static final short AES_STANDARD = 1;
	public static int g_cryptotype = 0;
	public static final short MOUSE_TRACE = 0;
	public static final short MOUSE_USE = 1;
	public static short MouseMode = MOUSE_USE;
	@Deprecated
	public static int openPort = 9500;
	public static boolean isForceGarbageCollection = false;
	
	public static boolean RECONN_FLAG = false;

	public static int D_RECONN_COUNT = 0;
	public static int D_RECONN_QCOUNTER = 0;
	public static int D_RECONN_NUMBER = 0;
	public static int D_RECONN_QCOUNTERRECV = 0;
	public static int D_RECONN_NUMBERRECV = 0;
	public static int D_RECONN_LENRECV = 0;
	
	public static int S_RECONN_COUNT = 0;
	public static int S_RECONN_QCOUNTER = 0;
	public static int S_RECONN_NUMBER = 0;
	public static int S_RECONN_QCOUNTERRECV = 0;
	public static int S_RECONN_NUMBERRECV = 0;
	public static int S_RECONN_LENRECV = 0;
	
	public static final short FONT_DEFAULT = 0;
	public static final short FONT_BOLD = 1;
	public static final short FONT_BIGBOLD = 2;
	
	public static final short UserType_Etc = 0;
	public static final short UserType_Normal = 1;
	public static final short UserType_Demo = 2;
	public static final short UserType_DemoFree = 3;
	
	public static String hostPCName = "";
	public static String hostIP = "";
	public static String hostOS ="";
	
	public static final short REHEAD_REQ = 1;
	public static final short REHEAD_ANS = 2;
	public static final short CHECK_LINE_REQ = 3;
	public static final short CHECK_LINE_ANS = 4;
	public static final short NORMAL_END_REQ = 5;
	public static final short NORMAL_END_ANS = 6;
	public static final short RECONNECT_END = 7;
	
	public static String localServerPort = "6107";
	
	public static short G_BITCOUNT;
	public static short G_BYTESCOUNT;
	public static short G_BITMAPWIDTH;
	public static short G_BITMAPHEIGHT;
	public static boolean G_CONNECTEDSESSION = false;
	public static boolean G_CONNECTEDFTPSESSION = false;
	public static boolean G_ISCONNECTINGSESSION = false;
	public static String g_err = "";
	public static int g_errNumber;
	public static float g_realBitmapRatioX;
	public static float g_realBitmapRatioY;
	public static boolean SCREENSET_LOCKSCREEN = false;
	public static boolean SCREENSET_LOCKMONITOR = false;
	public static boolean SCREENSET_SHARE_SOUND = false;
	public static int SCREENSET_AUTO_ROTATE = 1;
	public static int     SCREENSET_SOUNDQUALITY = 16000;
	
	public static final String WOL_HARDWARE = "52"; // REMOTEWOL 전용으로 등록된 PC
	public static final String WOL_MACHINE = "51";  // REMOTEWOL 장비
	
	/** 
	 * 2012/11/14 
	 * kyeom
	 * 자동해상도가 정상적으로 동작하지 않음을 확인(원격연결이 맺어진 후에는 정상작동하나, 연결중일 경우에는 실패) 하여
     * 기본값을 false 로 변경함
	 */
	public static boolean SCREENSET_AUTORESOLUTION = false;
	public static boolean SCREENSET_PROXYUSE = false;
	public static boolean ISTOUCHVIEWGUIDE = true;
	public static boolean ISSHOWSCROLLWHEEL = true;
	public static boolean ISCURSORVIEWGUIDE = true;
	public static short ISVIEWGUIDETYPE = 0;
	public static final short VIEWGUIDE_TOUCH  = 0;
	public static final short VIEWGUIDE_CURSOR = 1;
	public static final short AGENTLIST_GROUP = 0;
	public static final short AGENTLIST_AGENT = 1;
	public static final short ALERT_LOGOFF = 0;
	public static final short ALERT_RESTART = 1;
	public static final short ALERT_WINCLOSE = 2;
	public static final short ALERT_CONNCLOSE = 3;
	public static final short ALERT_CLOSE = 4;
	public static final short ALERT_DISCONNECT = 5;
	public static final short ALERT_RESOLUTION = 6;
	public static int CMD_PROC_ERR = 0;
	public static boolean WAITING_CONFIRM = false;

	public static String g_deviceIP = "";
	public static Vector<AgentInfo> g_vecVproAgents = new Vector<AgentInfo>();
	// public static Vector g_vecVirtuals = new Vector();
	public static Vector g_vecBackWebLogins = new Vector();
	public static String g_param = "";
	public static String g_setinfoLanguage = "";
	public static String g_setinfoProxyAddr = "";
	public static String g_setinfoProxyPort = "";
	public static String g_setinfoProxyID = "";
	public static String g_setinfoProxyPasswd = "";

	public static final int LANG_KOREAN = 0x12;
	public static final int LANG_JAPANESE = 0x11;
	public static int g_languageID = LANG_KOREAN; 
	
	public static final String CLICK_GUIDE = "Click Here";
	
	public static ConnectionInfo g_connectionInfo = new ConnectionInfo();

	public static short g_canvasRatio = 0;
	public static float g_fixRatio = 0;
	public static float g_fixRatioY = 0;
	//public static int MAX_RESOLUTION = 1680 * 1050 + 100;
//	public static final int MAX_RESOLUTION = 1280 * 1024 + 100;
	public static int MAX_DESIRE_RESOLUTION = 1280 * 1024 + 100;
	public static final int MAX_LIBERTY_RESOLUTION = 1024 * 768 + 100;

	public static int FIXWIDTH;
	public static int FIXHEIGHT;

	public static short ORIGIN_RESOLUTION_X = 0;
	public static short ORIGIN_RESOLUTION_Y = 0;
	public static short ORIGIN_RESOLUTION_BIT = 0;
	public static short ORIGIN_SCREEN_X = 0;
	public static short ORIGIN_SCREEN_Y = 0;
	public static short ORIGIN_SCREEN_BIT = 0;


	/** 호스트로부터 불러온 해상도 정보가 있는지 여뷰 **/
	public static boolean ISSAVERESOLUTION = false;
	public static boolean ISSAVEPROCESSINFO = false;

	public static long AUTORESOLUTION_DURATIONTIME = 0;
	
	public static short FIRSTMONITOR_WIDTH = 0;
	public static short FIRSTMONITOR_HEIGHT = 0;
	public static int[] sourceBitmapPixels;
	
	public static boolean G_CLOSEAPP = false;
	public static boolean G_CLOSECONNECTION = false;
	public static boolean G_REMOTE_LOGOFF = false;
	public static boolean G_REMOTE_RESTART = false;
	public static boolean G_REMOTE_CLOSE = false;
	
	public static final int SETTINGURL_PRODUCTTITLE = 0;
	public static final int SETTINGURL_PRODUCT_PERSONAL = 1;
	public static final int SETTINGURL_PRODUCT_CORP = 2;
	public static final int SETTINGURL_PRODUCT_SERVER = 3;
	public static final int SETTINGURL_URL = 4;
	public static final int SETTINGURL_COMPANYID = 5;
	public static final int SETTINGURL_END = 6;
	
	public static int SCREEN_OFFTIME = 0;
	
	public static String macAddress;
	
	public static boolean isCloseChannel = false;
	public static boolean isWaitTransferData = false;
	public static boolean isCancelConnect = false;
	
	public static boolean isWaitSessionResponse = false;
	public static boolean isWaitSessionCloseResponse = false;
	public static boolean isWaitSessionForceCloseResponse = false;
	
	public static boolean G_REFRESH_AGENT = false;
	public static boolean G_REFRESH_STATUS = false;
	public static boolean isHostMacPC = false;
	
	public static boolean isConnectEndAgentRefrash = false;
	
	public static long readRemoteScreenPacketSize = 0;
	
	public static void setRemoteScreenPacketSize(int read) {
		readRemoteScreenPacketSize += read;
	}
	
	public static int canvasType = 0;

	public static final int CANVAS_NORMAL 	= 0;
	public static final int CANVAS_HXOPENGL	= 1;
	public static final int CANVAS_OPENGL 	= 2;
	public static final int CANVAS_X264 	= 3;
	
	public static final int ENGINETYPE_SCAP = 0;
	public static final int ENGINETYPE_HXENGINE = 1;

	public static boolean isHXDemo = false;
	public static boolean isLogHXEngineTraffic = false;
	public static boolean isEngineChanged = false;
	
	public static X264Header x264VideoHeader;
	public static VRVDHeader vrvdVideoHeader;
	public static HXENGINEHEADER hxVideoHeader;

	public static long sChannel_Traffic = 0;
	public static long hxVideoFrameIndex = 0;
	
	public static final String DIRECT_INPUT_WIN_AGENT_VERSION = "5.2.24.1";
	public static final String DIRECT_INPUT_MAC_AGENT_VERSION = "5.0.8.3";
	
	public static short waitShowTime = 0; // 동시에 Toast가 show할 경우 waitTime을 부여함.
	
	public static String makeInstallUrl = "";
	public static String webId = "";
	public static String webPassword = "";
	
	public static boolean isOtp = false;
    public static boolean isShareSound = false;
	public static String pushServerAddr = "";
	public static String pushServerPort = "";
	public static String pushServerConnectType = "0";
	public static String connectSuccessUrl = "";
	
	public static int remoteAgreeTimeOut = 0;
	public static int remoteAgreeTimeCheck = 0;
	
	public static String smsToken = "";

	public static boolean isPointerVisible = false;
	public static boolean isDrawEraser = false;

	public static boolean isWebServer5x = true; // default true
	public static int screenAngle = 0;

	public static String getLogWithCurDate(String log) {
		String ret = "";
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss.SSSSSS");
		ret = df.format(new Date(System.currentTimeMillis())) + " : " + log;
		return ret;
	}

    public static int getBitmapWidth(Context context, int resid) {
        
    	InputStream is = context.getResources().openRawResource(resid);
        
        Bitmap bitmap = BitmapFactory.decodeStream(is); 
        
        float f =  GlobalStatic.g_realBitmapRatioX * bitmap.getWidth();
        return (int)f;
    }


	public static int getMaxResolution() {
		int ret = 0;
		long maxMem = Runtime.getRuntime().maxMemory();
		if(Define.DeviceParam.MANUFACTURER.contains("HTC") && !Define.DeviceParam.MODEL.contains("Nexus")) {
			ret = DisplayUtil.getProperResolutionHTC(maxMem);
		} else {
			ret = DisplayUtil.getProperResolution(maxMem);
		}
		return ret;
	}   

	public static boolean isCloseSessionMode() {
		boolean ret = false;
		if (G_CLOSEAPP == true ||
			G_CLOSECONNECTION == true ||
			G_REMOTE_LOGOFF == true ||
			G_REMOTE_RESTART == true ||
			G_REMOTE_CLOSE == true) {
			ret = true;
		}
		return ret;
	} 

	public static String getProductName() {
		if (PRODUCT_TYPE == REMOTECALL) {
			return "RemoteCall";
		} else if (PRODUCT_TYPE == REMOTEVIEW) {
			return "RemoteView";
		} else {
			return null;
		}
	}
	public static String intToIp(int i) {
		return ((i & 0xff) + "." + ((i >> 8) & 0xff) + "." + ((i >> 16) & 0xFF) + "." + ((i >> 24) & 0xFF));
	}

	@SuppressLint("MissingPermission")
	public static String getMacAddress(Context context) {
		if (macAddress != null) return macAddress;

		if (Build.VERSION.SDK_INT >= VERSION_CODES.M) {
			String macAddress = getWifiMacAddress();
			// 맥주소가 없을 경우 대체 주소로 리턴
			if(StringUtil.isEmpty(macAddress)) macAddress = SettingPref.createInstance(context, false).getDeviceID("-");

			return macAddress;
		}

		WifiManager wifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);

		if (wifiManager == null) return "";
		
		try {
			RLog.d("GlobalStatic getMacAddress wifiManager.getConnectionInfo() : " + wifiManager.getConnectionInfo());
			
			macAddress = wifiManager.getConnectionInfo().getMacAddress();
			RLog.d("GlobalStatic getMacAddress macAddress : " + macAddress);
			
			if (macAddress == null && 
					!wifiManager.isWifiEnabled() && 
					(SettingPref.getInstance().productType != Enums.ProductType.STANDARD)) {
				
				wifiManager.setWifiEnabled(true);
				macAddress = wifiManager.getConnectionInfo().getMacAddress();
				wifiManager.setWifiEnabled(false);
			}

			if(macAddress != null) {
				macAddress = macAddress.toUpperCase();

				//MAC 구분자 - 으로 조정 (#99091)
				Pattern p = Pattern.compile("([0-9A-F]{2}).([0-9A-F]{2}).([0-9A-F]{2}).([0-9A-F]{2}).([0-9A-F]{2}).([0-9A-F]{2})");
				Matcher m = p.matcher(macAddress);
				if(m.matches()) {
					macAddress = String.format("%s-%s-%s-%s-%s-%s", m.group(1), m.group(2), m.group(3), m.group(4), m.group(5), m.group(6));
				}
			}
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		
		return (macAddress == null) ? "" : macAddress;
	}

    public static String getControlDriverString() {
    	String ret = "High-speed";
    	if(!RuntimeData.getInstance().getAgentConnectOption().isControlExpress()) {
    		ret = "General";
    	}
    	return ret;
    }

    public static int getControlDriverStringResource() {
		AgentConnectOption opt = RuntimeData.getInstance().getAgentConnectOption();

		if(opt.isHxEngine()) {
			return R.string.control_video;
		}else {
			if(opt.isControlExpress()) {
				return R.string.control_speed;
			}else {
				return R.string.control_general;
			}
		}
	}

    public static short getHookType() {
		short ret = 'D';
		if (!RuntimeData.getInstance().getAgentConnectOption().isControlExpress()) ret = 'P';
		return ret;
	}

	//////////////////////////////////////// INSERT


	public static int getKeycodeInMap(int keycode) {
//    	int ret = getKeycodeInMapCommon(keycode);
		int ret = getKeycodeInSpecialKey(keycode);
		return ret;
	}

	public static int getKeycodeInChar(char ch) {
		int ret = 0;
		ret = getKeycodeInCharCommon(ch);
		return ret;
	}

	public static boolean isShiftChar(char ch) {
		boolean ret = false;
		if (g_languageID == LANG_JAPANESE) {
			ret = isShiftCharJapen(ch);
		} else {
			ret = isShiftCharCommon(ch);
		}
		return ret;
	}

	public static boolean isSingleKey(char ch) {
		return isSingleKeyCommon(ch);
	}


	private static int getKeycodeInSpecialKey(int keycode) {
		int ret = 0;
		switch (keycode) {
			case ComConstant.AVK_SHIFT:
				RLog.d("AVK_SHIFT");
				ret = ComConstant.VK_SHIFT;
				break;
			case ComConstant.AVK_LANG:
				RLog.d("AVK_LANG");
				ret = ComConstant.KEY_HANGULE;
				break;
			case ComConstant.AVK_BACK:
				RLog.d("AVK_BACK");
				ret = ComConstant.VK_BACK_SPACE;
				break;
			case ComConstant.AVK_ENTER:
				RLog.d("AVK_ENTER");
				ret = ComConstant.VK_ENTER;
				break;
			case ComConstant.AVK_TAB:
				RLog.d("AVK_TAB");
				ret = ComConstant.VK_TAB;
				break;

//      (Not working as expected)
//
			case ComConstant.AVK_LEFT:
//			if (Util.getKeyboardName().contains("standardcommon.IWnnLanguageSwitcher")) {
				RLog.d("AVK_LEFT");
				ret = ComConstant.VK_LEFT;
//			}
				break;
			case ComConstant.AVK_RIGHT:
//			if (Util.getKeyboardName().contains("standardcommon.IWnnLanguageSwitcher")) {
				RLog.d("AVK_RIGHT");
				ret = ComConstant.VK_RIGHT;
//			}
				break;
			case ComConstant.AVK_UP:
//			RLog.d("AVK_UP");
				ret = ComConstant.VK_UP;
				break;
			case ComConstant.AVK_DOWN:
//			RLog.d("AVK_DOWN");
				ret = ComConstant.VK_DOWN;
				break;
		}
		return ret;
	}

	private static int getKeycodeInCharCommon(char ch) {
		RLog.v("getKeycodeInCharCommon : " + ch + "(" + (int)ch + ")");

		int ret = 0;
		switch (ch) {
			case 'A':
			case 'a':
				ret = ComConstant.VK_A;
				break;
			case 'B':
			case 'b':
				ret = ComConstant.VK_B;
				break;
			case 'C':
			case 'c':
				ret = ComConstant.VK_C;
				break;
			case 'D':
			case 'd':
				ret = ComConstant.VK_D;
				break;
			case 'E':
			case 'e':
				ret = ComConstant.VK_E;
				break;
			case 'F':
			case 'f':
				ret = ComConstant.VK_F;
				break;
			case 'G':
			case 'g':
				ret = ComConstant.VK_G;
				break;
			case 'H':
			case 'h':
				ret = ComConstant.VK_H;
				break;
			case 'I':
			case 'i':
				ret = ComConstant.VK_I;
				break;
			case 'J':
			case 'j':
				ret = ComConstant.VK_J;
				break;
			case 'K':
			case 'k':
				ret = ComConstant.VK_K;
				break;
			case 'L':
			case 'l':
				ret = ComConstant.VK_L;
				break;
			case 'M':
			case 'm':
				ret = ComConstant.VK_M;
				break;
			case 'N':
			case 'n':
				ret = ComConstant.VK_N;
				break;
			case 'O':
			case 'o':
				ret = ComConstant.VK_O;
				break;
			case 'P':
			case 'p':
				ret = ComConstant.VK_P;
				break;
			case 'Q':
			case 'q':
				ret = ComConstant.VK_Q;
				break;
			case 'R':
			case 'r':
				ret = ComConstant.VK_R;
				break;
			case 'S':
			case 's':
				ret = ComConstant.VK_S;
				break;
			case 'T':
			case 't':
				ret = ComConstant.VK_T;
				break;
			case 'U':
			case 'u':
				ret = ComConstant.VK_U;
				break;
			case 'V':
			case 'v':
				ret = ComConstant.VK_V;
				break;
			case 'W':
			case 'w':
				ret = ComConstant.VK_W;
				break;
			case 'X':
			case 'x':
				ret = ComConstant.VK_X;
				break;
			case 'Y':
			case 'y':
				ret = ComConstant.VK_Y;
				break;
			case 'Z':
			case 'z':
				ret = ComConstant.VK_Z;
				break;
			case '1':
				ret = ComConstant.VK_1;
				break;
			case '2':
				ret = ComConstant.VK_2;
				break;
			case '3':
				ret = ComConstant.VK_3;
				break;
			case '4':
				ret = ComConstant.VK_4;
				break;
			case '5':
				ret = ComConstant.VK_5;
				break;
			case '6':
				ret = ComConstant.VK_6;
				break;
			case '7':
				ret = ComConstant.VK_7;
				break;
			case '8':
				ret = ComConstant.VK_8;
				break;
			case '9':
				ret = ComConstant.VK_9;
				break;
			case '0':
				ret = ComConstant.VK_0;
				break;

			case '.':
				ret = 0x2E;
				break;
			case ' ':
				ret = 0x20;
				break;
			case '#':
				ret = 0x23;
				break;
			case '$':
				ret = 0x24;
				break;
			case '%':
				ret = 0x25;
				break;
			case '-':
				ret = 0x2D;
				break;
			case '!':
				ret = 0x21;
				break;
			case '\"':
				ret = 0x22;
				break;
			case '\'':
				ret = 0x27;
				break;
			case ';':
				ret = 0x3B;
				break;
			case '/':
				ret = 0x2F;
				break;
			case '?':
				ret = 0x3f;
				break;
			case ',':
				ret = 0X2C;
				break;
			case '`':
				ret = 0x60;
				break;
			case '|':
				ret = 0x7C;
				break;
			case '<':
				ret = 0x3C;
				break;
			case '>':
				ret = 0x3E;
				break;
			case '=':
				ret = 0x3D;
				break;
			case '{':
				ret = 0x7B;
				break;
			case '}':
				ret = 0x7D;
				break;
			case '[':
				ret = 0x5B;
				break;
			case ']':
				ret = 0x5D;
				break;

			// difference on japen
			case '~':
				ret = 0x7E;
				break;
			case '@':
				ret = 0x40;
				break;
			case '^':
				ret = 0x5E;
				break;
			case '&':
				ret = 0x26;
				break;
			case '*':
				ret = 0x2A;
				break;
			case '(':
				ret = 0x28;
				break;
			case ')':
				ret = 0x29;
				break;
			case '_':
				ret = 0x5F;
				break;
			case '\\':
			case '¥':
				ret = 0x5C;
				break;
			case '+':
				ret = 0x2B;
				break;
			case ':':
				ret = 0x3A;
				break;
		}
		return ret;
	}

	private static boolean isShiftCharCommon(char ch) {
		boolean ret = false;
		switch (ch) {
			case 'A':
			case 'B':
			case 'C':
			case 'D':
			case 'E':
			case 'F':
			case 'G':
			case 'H':
			case 'I':
			case 'J':
			case 'K':
			case 'L':
			case 'M':
			case 'N':
			case 'O':
			case 'P':
			case 'Q':
			case 'R':
			case 'S':
			case 'T':
			case 'U':
			case 'V':
			case 'W':
			case 'X':
			case 'Y':
			case 'Z':
			case '~':
			case '!':
			case '@':
			case '#':
			case '$':
			case '%':
			case '^':
			case '&':
			case '*':
			case '(':
			case ')':
			case '_':
			case '+':
			case '|':
			case '{':
			case '}':
			case ':':
			case '\"':
			case '<':
			case '>':
			case '?':
				ret = true;
		}
		return ret;
	}

	private static boolean isShiftCharJapen(char ch) {
		boolean ret = false;
		switch (ch) {
			case 'A':
			case 'B':
			case 'C':
			case 'D':
			case 'E':
			case 'F':
			case 'G':
			case 'H':
			case 'I':
			case 'J':
			case 'K':
			case 'L':
			case 'M':
			case 'N':
			case 'O':
			case 'P':
			case 'Q':
			case 'R':
			case 'S':
			case 'T':
			case 'U':
			case 'V':
			case 'W':
			case 'X':
			case 'Y':
			case 'Z':
			case '!':
			case '"':
			case '#':
			case '$':
			case '%':
			case '&':
			case '\'':
			case '(':
			case ')':
			case '=':
			case '~':
			case '|':
			case '`':
			case '{':
			case '+':
			case '*':
			case '}':
			case '<':
			case '>':
			case '?':
			case '_':
				ret = true;
		}
		return ret;
	}

	private static boolean isSingleKeyCommon(char s) {
		boolean ret = false;
		switch (s) {
			case 'A':
			case 'a':
			case 'B':
			case 'b':
			case 'C':
			case 'c':
			case 'D':
			case 'd':
			case 'E':
			case 'e':
			case 'F':
			case 'f':
			case 'G':
			case 'g':
			case 'H':
			case 'h':
			case 'I':
			case 'i':
			case 'J':
			case 'j':
			case 'K':
			case 'k':
			case 'L':
			case 'l':
			case 'M':
			case 'm':
			case 'N':
			case 'n':
			case 'O':
			case 'o':
			case 'P':
			case 'p':
			case 'Q':
			case 'q':
			case 'R':
			case 'r':
			case 'S':
			case 's':
			case 'T':
			case 't':
			case 'U':
			case 'u':
			case 'V':
			case 'v':
			case 'W':
			case 'w':
			case 'X':
			case 'x':
			case 'Y':
			case 'y':
			case 'Z':
			case 'z':
				ret = true;
				break;
			case '1':
			case '2':
			case '3':
			case '4':
			case '5':
			case '6':
			case '7':
			case '8':
			case '9':
			case '0':
			case '.':
			case ' ':
				ret = true;
				break;
			case '@':
			case '#':
			case '$':
			case '%':
			case '`':
			case '_':
			case '*':
			case '-':
			case '+':
			case '(':
			case ')':
			case '!':
			case '\"':
			case '\'':
			case '\\':
			case ':':
			case ';':
			case '/':
			case '?':
			case ',':
			case '^':
			case '~':
			case '=':
			case '&':
			case '|':
			case '<':
			case '>':
			case '{':
			case '}':
			case '[':
			case ']':

			case '¥':
				ret = true;
				break;
		}
		return ret;
	}
	////////////////////////////////////////////////INSERT END

	@SuppressLint("StringFormatMatches")
	public static String errMessageProc(Context context) {
		switch (GlobalStatic.g_errNumber) {
		case ComConstant.NET_ERR_BIND:
		case ComConstant.NET_ERR_CONNECT:
		case ComConstant.NET_ERR_HTTPRETRY:
		case ComConstant.NET_ERR_MALFORMED:
		case ComConstant.NET_ERR_NOROUTE:
		case ComConstant.NET_ERR_PORTUNREACHABLE:
		case ComConstant.NET_ERR_PROTOCOL:
		case ComConstant.NET_ERR_UNKNOWNSERVER:
		case ComConstant.NET_ERR_UNKNOWNHOST:
		case ComConstant.NET_ERR_SOCKET:
		case ComConstant.NET_ERR_EXCEPTION:
			GlobalStatic.g_err = context.getString(R.string.msg_unableserver) + " " + context.getString(R.string.msg_checkagain);
			break;
		case ComConstant.NET_ERR_TIMEOUT:
			GlobalStatic.g_err = context.getString(R.string.weberr_server_timeout);
			break;
		case ComConstant.WEB_ERR_INVALID_PARAMETER:
			GlobalStatic.g_err = context.getString(R.string.weberr_invalid_parameter);
			break;
		case ComConstant.WEB_ERR_NOT_FOUND_USERID:
		case ComConstant.WEB_ERR_AES_NOT_FOUND_USERID:
			GlobalStatic.g_err = context.getString(R.string.weberr_invalid_user_account);
			break;
		case ComConstant.WEB_ERR_NOT_FOUND_AGENTID:
			GlobalStatic.g_err = context.getString(R.string.weberr_not_found_agentid);
			break;
		case ComConstant.WEB_ERR_INVALID_USER_ACCOUNT:
		case ComConstant.WEB_ERR_AES_INVALID_USER_ACCOUNT:
			GlobalStatic.g_err = context.getString(R.string.weberr_invalid_user_account);
			break;
		case ComConstant.WEB_ERR_ALREADY_USINGSESSION:
			GlobalStatic.g_err = context.getString(R.string.weberr_aleady_usingsession);
			break;
		case ComConstant.WEB_ERR_BLOCK_MOBILELOGIN:
			GlobalStatic.g_err = context.getString(R.string.weberr_block_mobilelogin);
			break;
		case ComConstant.WEB_ERR_INVITE_EXPIRED:
			GlobalStatic.g_err = context.getString(R.string.weberr_invite_expired);
			break;
		case ComConstant.WEB_ERR_INVITE_ALREADY:
			GlobalStatic.g_err = context.getString(R.string.weberr_invite_already);
			break;
		case ComConstant.WEB_ERR_ALREADY_SAME_WORKING:
			GlobalStatic.g_err = context.getString(R.string.weberr_already_same_working);
			break;
		case ComConstant.WEB_ERR_ALREADY_DELETE_AGENTID:
			GlobalStatic.g_err = context.getString(R.string.weberr_already_delete_agentid);
			break;
		case ComConstant.WEB_ERR_AGENT_NOT_LOGIN:
			GlobalStatic.g_err = context.getString(R.string.weberr_agent_not_login);
			break;
		case ComConstant.WEB_ERR_ONLY_WEBSETUP:
			GlobalStatic.g_err = context.getString(R.string.weberr_only_websetup);
			break;
		case ComConstant.WEB_ERR_AGENT_EXPIRED:
			GlobalStatic.g_err = context.getString(R.string.weberr_agent_expired);
			break;
		case ComConstant.WEB_ERR_SQL_ERROR:
			GlobalStatic.g_err = context.getString(R.string.weberr_sql_error);
			break;
		case ComConstant.WEB_ERR_APP_VERSION:
			GlobalStatic.g_err = context.getString(R.string.weberr_app_version);
			break;
		case ComConstant.WEB_ERR_INVAILD_COMPANYID:
			GlobalStatic.g_err = context.getString(R.string.weberr_invalid_companyid);
			break;
		case ComConstant.WEB_ERR_NEED_SWITCH_MEMBER:
			GlobalStatic.g_err = context.getString(R.string.weberr_need_switch_member);
			break;
		case ComConstant.WEB_ERR_NEED_UPGRADE_MEMBER:
			GlobalStatic.g_err = context.getString(R.string.weberr_need_update_member);
			break;
		case ComConstant.WEB_ERR_WOL_NOT_FOUND_AGENT:
			GlobalStatic.g_err = context.getString(R.string.weberr_wol_not_found_agent);
			break;
		case ComConstant.WEB_ERR_INVALID_MACADDRESS:
		case ComConstant.WEB_ERR_INVALID_LOCALIP:
			GlobalStatic.g_err = context.getString(R.string.weberr_invalid_macaddress_localip);
			break;
		case ComConstant.WEB_ERR_LIC_EXPIRED:
			GlobalStatic.g_err = context.getString(R.string.weberr_lic_expired);
			break;
		case ComConstant.WEB_ERR_LIC_SERVICE_ERROR:
			GlobalStatic.g_err = context.getString(R.string.weberr_lic_service_error);
			break;
		case ComConstant.WEB_ERR_INVALID_ROLE:
			GlobalStatic.g_err = context.getString(R.string.weberr_invalid_role);
			break;
		case ComConstant.WEB_ERR_UNAUTH_MAC_ADDRESS:
			GlobalStatic.g_err = context.getString(R.string.weberr_unauth_macaddress);
			break;
		case ComConstant.WEB_ERR_UNAUTH_DEVICE:
			GlobalStatic.g_err = context.getString(R.string.weberr_unauth_device);
			break;
		case ComConstant.WEB_ERR_USER_ACCOUNT_LOCK:
			GlobalStatic.g_err = context.getString(R.string.weberr_unauth_user);
			break;
		case ComConstant.WEB_ERR_ARP_NOT_FOUND_AGENT:
			GlobalStatic.g_err = context.getString(R.string.weberr_arp_not_found_woldevice);
			break;

		case ComConstant.WEB_ERR_DIRECT_EXEC_EXPIRED:
			GlobalStatic.g_err = context.getString(R.string.msg_direct_exec_input_expired);
			break;

		case ComConstant.WEB_ERR_UNAUTH_USER: {
            AppProperty appProp = RuntimeData.getInstance().appProperty;

            if (appProp.isAccountLock()) {
                int waitSec = appProp.getWaitLockTimeSec();
                if (waitSec == 0) {
                    GlobalStatic.g_err = context.getString(R.string.weberr_login_lock2);
                } else {
                    String content = context.getString(R.string.weberr_login_lock1);
                    int min = (int)(waitSec / 60);
                    int sec = (waitSec % 60);

                    GlobalStatic.g_err = content.format(content, String.valueOf(min), String.valueOf(sec));
                }
            } else {
                GlobalStatic.g_err = context.getString(R.string.weberr_unauth_user);
            }
        }
			break;
		case ComConstant.WEB_ERR_UNAUTH_PASSWORD_FAIL: {
            AppProperty appProp = RuntimeData.getInstance().appProperty;

            if (appProp.isAccountLock()) {
                int waitSec = appProp.getWaitLockTimeSec();

                if (waitSec == 0) {
                    String content = context.getString(R.string.weberr_login_wait2);
                    GlobalStatic.g_err = content.format(content, String.valueOf(appProp.getLoginFailCount()));
                } else {
                    String content = context.getString(R.string.weberr_login_wait1);
                    int min = (int)(waitSec / 60);
                    int sec = (waitSec % 60);

                    GlobalStatic.g_err = content.format(content, String.valueOf(appProp.getLoginFailCount()),
                            String.valueOf(min), String.valueOf(sec));
               }
            } else {
                GlobalStatic.g_err = context.getString(R.string.weberr_unauth_user);
            }
        }
			break;	
		case ComConstant.WEB_ERR_ACTIVE:
			GlobalStatic.g_err = context.getString(R.string.weberr_active_fail);
			break;
		case ComConstant.WEB_ERR:
			break;
		case ComConstant.NET_ERR_WAS:
		case ComConstant.WEB_ERR_NO:
			GlobalStatic.g_err = String.format(context.getString(R.string.weberr_etc_error), String.valueOf(GlobalStatic.g_errNumber));
			break;
		case ComConstant.WEB_ERR_PASSWORD_EXPIRED:
            // 비밀번호 만료 기간이 없음.
		{
			String str = context.getString(R.string.weberr_password_expired);
		}
			break;
		case ComConstant.NET_ERR_PROXYINFO_NULL:
			GlobalStatic.g_err = context.getString(R.string.proxyinfonull_msg);
			break;
		case ComConstant.NET_ERR_PROXY_VERIFY:
			GlobalStatic.g_err = context.getString(R.string.proxyverifyerr_msg);
			break;
			
		case ComConstant.CMD_ERR:
		case ComConstant.CMD_ERR_TOKEN:
		case ComConstant.CMD_ERR_COMSND:
		case ComConstant.CMD_ERR_DUPAGENT:
		case ComConstant.CMD_ERR_SESSION_CONNECT_FAIL:
			GlobalStatic.g_err = context.getString(R.string.cmderr_session_connect_fail);
			break;
		case ComConstant.CMD_ERR_NOAGENT:
			GlobalStatic.g_err = context.getString(R.string.cmderr_noagent);
			break;
		case ComConstant.CMD_ERR_SESSION_SEND_FAIL:
			GlobalStatic.g_err = context.getString(R.string.cmderr_session_send_fail);
			break;
		case ComConstant.CMD_ERR_SESSION_SOCKET_FAIL:
			GlobalStatic.g_err = context.getString(R.string.cmderr_session_socket_fail);
			break;
		case ComConstant.CMD_ERR_ALREADY_REMOTE_CONTROL:
			GlobalStatic.g_err = context.getString(R.string.cmderr_already_remote_control);
			break;
		case ComConstant.CMD_ERR_OPTION_FILE_CREATE_FAIL:
			GlobalStatic.g_err = context.getString(R.string.cmderr_option_file_create_fail);
			break;
		case ComConstant.CMD_ERR_PROCESS_EXCUTE_FAIL:
			GlobalStatic.g_err = context.getString(R.string.cmderr_process_execute_fail);
			break;
		case ComConstant.CMD_ERR_PROCESS_KILL_FAIL:
			GlobalStatic.g_err = context.getString(R.string.cmderr_process_kill_fail);
			break;
		case ComConstant.CMD_ERR_GET_PROCESS_LIST_FAIL:
			GlobalStatic.g_err = context.getString(R.string.cmderr_get_process_list_fail);
			break;
		case ComConstant.CMD_ERR_AGENT_RESET_CONFIG_FAIL:
			GlobalStatic.g_err = context.getString(R.string.cmderr_agent_reset_config_fail);
			break;
		case ComConstant.CMD_ERR_NOT_ALLOWED_IP_ADDRESS:
			GlobalStatic.g_err = context.getString(R.string.cmderr_not_allowed_ip_address);
			break;
		case ComConstant.CMD_ERR_GET_SCREENSHOT_FAIL:
			GlobalStatic.g_err = context.getString(R.string.cmderr_get_screenshot_fail);
			break;
		case ComConstant.CMD_ERR_GET_SYSTEM_INFO_FAIL:
			GlobalStatic.g_err = context.getString(R.string.cmderr_get_system_info_fail);
			break;
		case ComConstant.CMD_ERR_AMT_INVALID_AUTH:
			GlobalStatic.g_err = context.getString(R.string.cmderr_amt_invalid_auth);
			break;
		case ComConstant.CMD_ERR_AMT_NOT_ACCESS:
			GlobalStatic.g_err = context.getString(R.string.cmderr_amt_not_access);
			break;
		case ComConstant.CMD_ERR_AMT_INVALID_COMMAND:
			GlobalStatic.g_err = context.getString(R.string.cmderr_amt_invalid_command);
			break;
		case ComConstant.CMD_ERR_SYSTEM_REBOOT_FAIL:
			GlobalStatic.g_err = context.getString(R.string.cmderr_system_reboot_fail);
			break;
		case ComConstant.CMD_ERR_AGENTCONNECT_UNABLE:
			GlobalStatic.g_err = context.getString(R.string.cmderr_agentconnect_unable);
			break;
		case ComConstant.WEB_ERR_OTP_AUTH_FAILED:
			GlobalStatic.g_err = context.getString(R.string.weberr_otp_auth_failed);
			break;
		case ComConstant.WEB_ERR_OTP_KEY_INVALID:
			GlobalStatic.g_err = context.getString(R.string.weberr_otp_not_auth);
			break;
		case ComConstant.WEB_ERR_SERVICE_LIMIT_TIME:
			GlobalStatic.g_err = context.getString(R.string.weberr_time_limit_used);
			break;
		case ComConstant.WEB_ERR_ADMINISTRATORS_LIMIT_CONNECT:
			GlobalStatic.g_err = context.getString(R.string.weberr_connect_limit_administrators);
			break;
		case ComConstant.CMD_ERR_AMT_EXECUTE_FAIL:
		case ComConstant.CMD_ERR_CSSLEEP_EXECUTE_FAIL:
		case ComConstant.CMD_ERR_CSWAKEUP_EXECUTE_FAIL:
		case ComConstant.CMD_ERR_REMOTE_CONNECT_FAIL:
		case ComConstant.CMD_ERR_ETC:
		default:
			GlobalStatic.g_err = String.format(context.getString(R.string.weberr_etc_error), String.valueOf(GlobalStatic.g_errNumber));
		}

		return GlobalStatic.g_err;
	}
	
    public interface ActiveCallbackHandler {
    	void startAction();
    }

	public static void setEditTextPosition(final EditText editText) {
		editText.setOnFocusChangeListener(new OnFocusChangeListener() {
    		public void onFocusChange(View v, boolean hasFocus) {
    			editText.setSelection(editText.getText().length());
    		}
    	});
	}
	


	/**
	 * 일본어 직접 입력 방식 - 김민석
	 * @param ch
	 * @return
	 */
	public static int getKeycodeInCharJapen(char ch) {
		int ret = 0;
		switch (ch) {
			case 'A':
			case 'a':
				RLog.d("getKeycodeInChar A VK_A");
				ret = ComConstant.VK_A;
				break;
			case 'B':
			case 'b':
				RLog.d("getKeycodeInChar B VK_B");
				ret = ComConstant.VK_B;
				break;
			case 'C':
			case 'c':
				RLog.d("getKeycodeInChar C VK_C");
				ret = ComConstant.VK_C;
				break;
			case 'D':
			case 'd':
				RLog.d("getKeycodeInChar D VK_D");
				ret = ComConstant.VK_D;
				break;
			case 'E':
			case 'e':
				RLog.d("getKeycodeInChar E VK_E");
				ret = ComConstant.VK_E;
				break;
			case 'F':
			case 'f':
				RLog.d("getKeycodeInChar F VK_F");
				ret = ComConstant.VK_F;
				break;
			case 'G':
			case 'g':
				RLog.d("getKeycodeInChar G VK_G");
				ret = ComConstant.VK_G;
				break;
			case 'H':
			case 'h':
				RLog.d("getKeycodeInChar H VK_H");
				ret = ComConstant.VK_H;
				break;
			case 'I':
			case 'i':
				RLog.d("getKeycodeInChar I VK_I");
				ret = ComConstant.VK_I;
				break;
			case 'J':
			case 'j':
				RLog.d("getKeycodeInChar J VK_J");
				ret = ComConstant.VK_J;
				break;
			case 'K':
			case 'k':
				RLog.d("getKeycodeInChar K VK_K");
				ret = ComConstant.VK_K;
				break;
			case 'L':
			case 'l':
				RLog.d("getKeycodeInChar L VK_L");
				ret = ComConstant.VK_L;
				break;
			case 'M':
			case 'm':
				RLog.d("getKeycodeInChar M VK_M");
				ret = ComConstant.VK_M;
				break;
			case 'N':
			case 'n':
				RLog.d("getKeycodeInChar N VK_N");
				ret = ComConstant.VK_N;
				break;
			case 'O':
			case 'o':
				RLog.d("getKeycodeInChar O VK_O");
				ret = ComConstant.VK_O;
				break;
			case 'P':
			case 'p':
				RLog.d("getKeycodeInChar P VK_P");
				ret = ComConstant.VK_P;
				break;
			case 'Q':
			case 'q':
				RLog.d("getKeycodeInChar Q VK_Q");
				ret = ComConstant.VK_Q;
				break;
			case 'R':
			case 'r':
				RLog.d("getKeycodeInChar R VK_R");
				ret = ComConstant.VK_R;
				break;
			case 'S':
			case 's':
				RLog.d("getKeycodeInChar S VK_S");
				ret = ComConstant.VK_S;
				break;
			case 'T':
			case 't':
				RLog.d("getKeycodeInChar T VK_T");
				ret = ComConstant.VK_T;
				break;
			case 'U':
			case 'u':
				RLog.d("getKeycodeInChar U VK_U");
				ret = ComConstant.VK_U;
				break;
			case 'V':
			case 'v':
				RLog.d("getKeycodeInChar V VK_V");
				ret = ComConstant.VK_V;
				break;
			case 'W':
			case 'w':
				RLog.d("getKeycodeInChar W VK_W");
				ret = ComConstant.VK_W;
				break;
			case 'X':
			case 'x':
				RLog.d("getKeycodeInChar X VK_X");
				ret = ComConstant.VK_X;
				break;
			case 'Y':
			case 'y':
				RLog.d("getKeycodeInChar Y VK_Y");
				ret = ComConstant.VK_Y;
				break;
			case 'Z':
			case 'z':
				RLog.d("getKeycodeInChar Z VK_Z");
				ret = ComConstant.VK_Z;
				break;
			case '1':
				RLog.d("getKeycodeInChar 1 VK_1");
				ret = ComConstant.VK_1;
				break;
			case '2':
				RLog.d("getKeycodeInChar 2 VK_2");
				ret = ComConstant.VK_2;
				break;
			case '3':
				RLog.d("getKeycodeInChar 3 VK_3");
				ret = ComConstant.VK_3;
				break;
			case '4':
				RLog.d("getKeycodeInChar 4 VK_4");
				ret = ComConstant.VK_4;
				break;
			case '5':
				RLog.d("getKeycodeInChar 5 VK_5");
				ret = ComConstant.VK_5;
				break;
			case '6':
				RLog.d("getKeycodeInChar 6 VK_6");
				ret = ComConstant.VK_6;
				break;
			case '7':
				RLog.d("getKeycodeInChar 7 VK_7");
				ret = ComConstant.VK_7;
				break;
			case '8':
				RLog.d("getKeycodeInChar 8 VK_8");
				ret = ComConstant.VK_8;
				break;
			case '9':
				RLog.d("getKeycodeInChar 9 VK_9");
				ret = ComConstant.VK_9;
				break;
			case '0':
				RLog.d("getKeycodeInChar 0 VK_0");
				ret = ComConstant.VK_0;
				break;

			case '.':
				RLog.d("getKeycodeInChar . VK_PERIOD");
				ret = 0x2E;
				break;
			case ' ':
				RLog.d("getKeycodeInChar  VK_SPACE");
				ret = 0x20;
				break;
			case '#':
				RLog.d("getKeycodeInChar # VK_3");
				ret = 0x23;
				break;
			case '$':
				RLog.d("getKeycodeInChar $ VK_4");
				ret = 0x24;
				break;
			case '%':
				RLog.d("getKeycodeInChar % VK_5");
				ret = 0x25;
				break;
			case '-':
				RLog.d("getKeycodeInChar - VK_MINUS");
				ret = 0x2D;
				break;
			case '!':
				RLog.d("getKeycodeInChar ! VK_1");
				ret = 0x21;
				break;
			case '\"':
				RLog.d("getKeycodeInChar \" VK_QUOTE");
				ret = 0x22;
				break;
			case '\'':
				RLog.d("getKeycodeInChar \' VK_QUOTE");
				ret = 0x27;
				break;
			case ';':
				RLog.d("getKeycodeInChar ; VK_SEMICOLON");
				ret = 0x3B;
				break;
			case '/':
				RLog.d("getKeycodeInChar / VK_SLASH");
				ret = 0x2F;
				break;
			case '?':
				RLog.d("getKeycodeInChar ? VK_SLASH");
				ret = 0x3f;
				break;
			case ',':
				RLog.d("getKeycodeInChar , VK_COMMA");
				ret = 0X2C;
				break;
			case '`':
				RLog.d("getKeycodeInChar ` VK_BACK_QUOTE");
				ret = 0x60;
				break;
			case '|':
				RLog.d("getKeycodeInChar | VK_BACK_SLASH");
				ret = 0x7C;
				break;
			case '<':
				RLog.d("getKeycodeInChar < VK_COMMA");
				ret = 0x3C;
				break;
			case '>':
				RLog.d("getKeycodeInChar > VK_PERIOD");
				ret = 0x3E;
				break;
			case '=':
				RLog.d("getKeycodeInChar = VK_EQUALS");
				ret = 0x3D;
				break;
			case '{':
				RLog.d("getKeycodeInChar { VK_OPEN_BRACKET");
				ret = 0x7B;
				break;
			case '}':
				RLog.d("getKeycodeInChar } VK_CLOSE_BRACKET");
				ret = 0x7D;
				break;
			case '[':
				RLog.d("getKeycodeInChar [ VK_OPEN_BRACKET");
				ret = 0x5B;
				break;
			case ']':
				RLog.d("getKeycodeInChar ] VK_CLOSE_BRACKET");
				ret = 0x5D;
				break;

			// difference on japen
			case '~':
				RLog.d("getKeycodeInChar ~ VK_BACK_QUOTE");
				ret = 0x7E;
				break;
			case '@':
				RLog.d("getKeycodeInChar @ VK_2");
				ret = 0x40;
				break;
			case '^':
				RLog.d("getKeycodeInChar ^ VK_6");
				ret = 0x5E;
				break;
			case '&':
				RLog.d("getKeycodeInChar & VK_7");
				ret = 0x26;
				break;
			case '*':
				RLog.d("getKeycodeInChar * VK_8");
				ret = 0x2A;
				break;
			case '(':
				RLog.d("getKeycodeInChar ( VK_9");
				ret = 0x28;
				break;
			case ')':
				RLog.d("getKeycodeInChar ) VK_0");
				ret = 0x29;
				break;
			case '_':
				RLog.d("getKeycodeInChar _ VK_MINUS");
				ret = 0x5F;
				break;
			case '\\':
				RLog.d("getKeycodeInChar \\ VK_BACK_SLASH");
				ret = 0x5C;
				break;
			case '+':
				RLog.d("getKeycodeInChar + VK_EQUALS");
				ret = 0x2B;
				break;
			case ':':
				RLog.d("getKeycodeInChar : VK_SEMICOLON");
				ret = 0x3A;
				break;
		}
		return ret;
	}

	public static int getKeycodeInMapCommon(int keycode) {
		int ret = 0;
		switch (keycode) {
			case ComConstant.AVK_Q:
				RLog.d("AVK_Q");
				ret = ComConstant.VK_Q;
				break;
			case ComConstant.AVK_W:
				RLog.d("AVK_W");
				ret = ComConstant.VK_W;
				break;
			case ComConstant.AVK_E:
				RLog.d("AVK_E");
				ret = ComConstant.VK_E;
				break;
			case ComConstant.AVK_R:
				RLog.d("AVK_R");
				ret = ComConstant.VK_R;
				break;
			case ComConstant.AVK_T:
				RLog.d("AVK_T");
				ret = ComConstant.VK_T;
				break;
			case ComConstant.AVK_Y:
				RLog.d("AVK_Y");
				ret = ComConstant.VK_Y;
				break;
			case ComConstant.AVK_U:
				RLog.d("AVK_U");
				ret = ComConstant.VK_U;
				break;
			case ComConstant.AVK_I:
				RLog.d("AVK_I");
				ret = ComConstant.VK_I;
				break;
			case ComConstant.AVK_O:
				RLog.d("AVK_O");
				ret = ComConstant.VK_O;
				break;
			case ComConstant.AVK_P:
				RLog.d("AVK_P");
				ret = ComConstant.VK_P;
				break;
			case ComConstant.AVK_A:
				RLog.d("AVK_A");
				ret = ComConstant.VK_A;
				break;
			case ComConstant.AVK_S:
				RLog.d("AVK_S");
				ret = ComConstant.VK_S;
				break;
			case ComConstant.AVK_D:
				RLog.d("AVK_D");
				ret = ComConstant.VK_D;
				break;
			case ComConstant.AVK_F:
				RLog.d("AVK_F");
				ret = ComConstant.VK_F;
				break;
			case ComConstant.AVK_G:
				RLog.d("AVK_G");
				ret = ComConstant.VK_G;
				break;
			case ComConstant.AVK_H:
				RLog.d("AVK_H");
				ret = ComConstant.VK_H;
				break;
			case ComConstant.AVK_J:
				RLog.d("AVK_J");
				ret = ComConstant.VK_J;
				break;
			case ComConstant.AVK_K:
				RLog.d("AVK_K");
				ret = ComConstant.VK_K;
				break;
			case ComConstant.AVK_L:
				RLog.d("AVK_L");
				ret = ComConstant.VK_L;
				break;
			case ComConstant.AVK_Z:
				RLog.d("AVK_Z");
				ret = ComConstant.VK_Z;
				break;
			case ComConstant.AVK_X:
				RLog.d("AVK_X");
				ret = ComConstant.VK_X;
				break;
			case ComConstant.AVK_C:
				RLog.d("AVK_C");
				ret = ComConstant.VK_C;
				break;
			case ComConstant.AVK_V:
				RLog.d("AVK_V");
				ret = ComConstant.VK_V;
				break;
			case ComConstant.AVK_B:
				RLog.d("AVK_B");
				ret = ComConstant.VK_B;
				break;
			case ComConstant.AVK_N:
				RLog.d("AVK_N");
				ret = ComConstant.VK_N;
				break;
			case ComConstant.AVK_M:
				RLog.d("AVK_M");
				ret = ComConstant.VK_M;
				break;
			case ComConstant.AVK_SLASH:
				RLog.d("AVK_SLASH");
				ret = ComConstant.VK_SLASH;
				break;
			case ComConstant.AVK_SHIFT:
				RLog.d("AVK_SHIFT");
				ret = ComConstant.VK_SHIFT;
				break;
//		case ComConstant.AVK_LANG:
//			RLog.i("AVK_LANG");
//			ret = ComConstant.KEY_HANGULE;
//			break;
			case ComConstant.AVK_SPACE:
				RLog.d("AVK_SPACE");
				ret = ComConstant.VK_SPACE;
				break;
			case ComConstant.AVK_BACK:
				RLog.d("AVK_BACK");
				ret = ComConstant.VK_BACK_SPACE;
				break;
			case ComConstant.AVK_ENTER:
				RLog.d("AVK_ENTER");
				ret = ComConstant.VK_ENTER;
				break;
		}
		return ret;
	}



	/*	kyeom@rsupport.com
	 *  2012-12-11
	 * 	getCountry() 를 가져오지 못하는 경우가 있어, locale 비교로 대체함.
	 */
	public static String getSystemLanguage(Context context) {
		String ret = "";
//    	String country = getResources().getConfiguration().locale.getCountry();
		String locale = context.getResources().getConfiguration().locale.toString();
		
		if (locale.contains("ko")) {
			ret = context.getResources().getString(R.string.lang_korea);
		} else if (locale.contains("en")) {
			ret = context.getResources().getString(R.string.lang_us);
		} else if (locale.contains("ja")) {
			ret = context.getResources().getString(R.string.lang_japan);
		} else if (locale.equals("zh") || locale.contains("zh_CN")) {
			ret = context.getResources().getString(R.string.lang_china);
		} else if (locale.contains("zh_TW")) {
			ret = context.getResources().getString(R.string.lang_taiwan);
		} else if (locale.contains("es")) {
			ret = context.getResources().getString(R.string.lang_spain);
		} else if (locale.contains("pt")) {
			ret = context.getResources().getString(R.string.lang_portugal);
		}
		return ret;
	}

	public static boolean isSupportDeviceHXEngine() {
		boolean ret = false;

        if(!RuntimeData.getInstance().appProperty.isUseNewHxEngine()) {
            return false;
        }

		final int MIN_SDKINT = 16;
		
		int SDKINT = Build.VERSION.SDK_INT;
			
		if (SDKINT >= MIN_SDKINT) ret = true;

		AgentInfo agentInfo = RuntimeData.getInstance().getLastAgentInfo();
		if(agentInfo != null) {
			if(agentInfo.osname.toLowerCase().trim().equals("microsoft windows 2000")) {
                ret = false;
            }
        }
		
		return ret;
	}


	
	/**
	 * SubscriberId
	 * @param activity
	 * @return phoneNumber
	 */
	public static String getSubscriberId(AppCompatActivity activity) {
		TelephonyManager systemService = (TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE);

		@SuppressLint("MissingPermission") String subscriberId = systemService.getSubscriberId();
		
		return subscriberId;
	}
	
	/**
	 * Check if there is fast connectivity
	 * 
	 * @param context
	 * @return
	 */
	public static String getConnectionType(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		@SuppressLint("MissingPermission") NetworkInfo info = cm.getActiveNetworkInfo();

		if ((info != null && info.isConnected())) {
			return getConnectionType(info.getType(), info.getSubtype());
		} else
			return "No NetWork Access";

	}
	
	public static final int NETWORK_TYPE_EHRPD = 14; // Level 11
	public static final int NETWORK_TYPE_EVDO_B = 12; // Level 9
	public static final int NETWORK_TYPE_HSPAP = 15; // Level 13
	public static final int NETWORK_TYPE_IDEN = 11; // Level 8
	public static final int NETWORK_TYPE_LTE = 13; // Level 11

	/**
	 * Check if the connection is fast
	 * 
	 * @param type
	 * @param subType
	 * @return
	 */
	private static String getConnectionType(int type, int subType) {
		String NetType_LTE = "0";
		String NetType_3G = "1";
		String NetType_WIFI = "2";
		String NetType_ETC = "3";
		
		if (type == ConnectivityManager.TYPE_WIFI) {
			RLog.d("CONNECTED VIA WIFI");
			return NetType_WIFI;
		} else if (type == ConnectivityManager.TYPE_MOBILE) {
			switch (subType) {
			case TelephonyManager.NETWORK_TYPE_1xRTT:
				return NetType_ETC; // ~ 50-100 kbps
			case TelephonyManager.NETWORK_TYPE_CDMA:
				return NetType_3G; // ~ 14-64 kbps
			case TelephonyManager.NETWORK_TYPE_EDGE:
				return NetType_ETC; // ~ 50-100 kbps							
			case TelephonyManager.NETWORK_TYPE_EVDO_0:
				return NetType_ETC; // ~ 400-1000 kbps
			case TelephonyManager.NETWORK_TYPE_EVDO_A:
				return NetType_ETC; // ~ 600-1400 kbps
			case TelephonyManager.NETWORK_TYPE_GPRS:
				return NetType_ETC; // ~ 100 kbps
			case TelephonyManager.NETWORK_TYPE_HSDPA:
				return NetType_ETC; // ~ 2-14 Mbps
			case TelephonyManager.NETWORK_TYPE_HSPA:
				return NetType_ETC; // ~ 700-1700 kbps
			case TelephonyManager.NETWORK_TYPE_HSUPA:
				return NetType_3G; // ~ 1-23 Mbps
			case TelephonyManager.NETWORK_TYPE_UMTS:
				return NetType_3G; // ~ 400-7000 kbps
				// NOT AVAILABLE YET IN API LEVEL 7
			case NETWORK_TYPE_EHRPD:
				return NetType_ETC; // ~ 1-2 Mbps
			case NETWORK_TYPE_EVDO_B:
				return NetType_ETC; // ~ 5 Mbps
			case NETWORK_TYPE_HSPAP:
				return NetType_ETC; // ~ 10-20 Mbps
			case NETWORK_TYPE_IDEN:
				return NetType_ETC; // ~25 kbps
			case NETWORK_TYPE_LTE:
				return NetType_LTE; // ~ 10+ Mbps
				// Unknown
			case TelephonyManager.NETWORK_TYPE_UNKNOWN:
				return NetType_ETC;
			default:
				return NetType_ETC;
			}
		} else {
			return "";
		}
	}

	public static boolean isPhonetoPhone() {
		return true;
	}
	
	public static void registerPushMessaging(Context context, String guid) {
		RSPushMessaging messaging = RSPushMessaging.getInstance(context);
		if (GlobalStatic.pushServerPort.length() <= 0) GlobalStatic.pushServerPort = "80";
		messaging.setServerInfo(GlobalStatic.pushServerAddr, Integer.parseInt(GlobalStatic.pushServerPort));
		messaging.register(guid + "/console");
	}
	
	public static void unregisterPushMessaging(Context context, String guid) {
		RSPushMessaging messaging = RSPushMessaging.getInstance(context);
		if (GlobalStatic.pushServerPort.length() <= 0) GlobalStatic.pushServerPort = "80";
		messaging.setServerInfo(GlobalStatic.pushServerAddr, Integer.parseInt(GlobalStatic.pushServerPort));
		messaging.unregister(guid + "/console");
	}
	
	public static String getOtpString() {
		String ret = "";
		if (isOtp) {
			ret = "OTP";
		}
		
		return ret;
	}


    public static String getWifiMacAddress() {
        try {
            String interfaceName = "wlan0";
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                if (!intf.getName().equalsIgnoreCase(interfaceName)) {
                    continue;
                }

                byte[] mac = intf.getHardwareAddress();
                if (mac == null) {
                    return "";
                }

                StringBuilder buf = new StringBuilder();
                for (byte aMac : mac) {
                    buf.append(String.format("%02X-", aMac));
                }
                if (buf.length() > 0) {
                    buf.deleteCharAt(buf.length() - 1);
                }
                macAddress = buf.toString();
                return macAddress;
            }
        } catch (Exception exp) {

            exp.printStackTrace();
        }

        return macAddress == null ? "" : macAddress;
    }



	public static class TokenRefreshThread extends Thread {
		@Override
		public void run() {
			long lastRefresh = System.currentTimeMillis();
			RLog.i("Token refresh thread started...");
			try {
				while (!this.isInterrupted()) {
					long diff = System.currentTimeMillis() - lastRefresh;
					if (diff >= GlobalStatic.TOKEN_REFRESH_INTERVAL) {
//						if(WebConnection.getInstance().refreshToken()) {
//							lastRefresh = System.currentTimeMillis();
//						}
					} else {
						Thread.sleep(Math.min(5000, (GlobalStatic.TOKEN_REFRESH_INTERVAL - diff)));
					}
				}
			}catch(Exception e) {
				RLog.w(e);
			}
			RLog.i("Token refresh thread terminate");
		}
	};

}
