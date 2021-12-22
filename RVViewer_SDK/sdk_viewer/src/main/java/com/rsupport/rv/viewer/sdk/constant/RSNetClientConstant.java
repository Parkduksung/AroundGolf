package com.rsupport.rv.viewer.sdk.constant;

// RSNetClient ��� �������
public class RSNetClientConstant {
	public final static int RSNET_NUMBER = 40000;

	public final static int RSNET_LOGIN = RSNET_NUMBER + 111;
	public final static int RSNET_LOGIN_RES = RSNET_NUMBER + 121;
	public final static int RSNET_LOGIN_FAIL_GUIDDUP = RSNET_NUMBER + 131;
	public final static int RSNET_LOGIN_FAIL_DB = RSNET_NUMBER + 132;
	public final static int RSNET_LOGIN_VIEWERNOTFOUND = RSNET_NUMBER + 133;

	public final static int RSNET_DB_RES = RSNET_NUMBER + 201;
	public final static int RSNET_DB_LOGIN = RSNET_NUMBER + 202;
	public final static int RSNET_DB_LOGOUT = RSNET_NUMBER + 203;

	// #define RSNET_ABNORMALEND RSNET_NUMBER+101
	// #define RSNET_DISCONNECTED RSNET_NUMBER+201
	// #define RSNET_CONNECTED RSNET_NUMBER+301

	public final static int RSNET_NORMAL_END = RSNET_NUMBER + 401;
	public final static int RSNET_ANOTHER_END = RSNET_NUMBER + 402;
	public final static int RSNET_SELF_END = RSNET_NUMBER + 403;
	public final static int RSNET_NORMAL_TUNNEL = RSNET_NUMBER + 404;
	public final static int RSNET_RESET_TUNNEL = RSNET_NUMBER + 405;

	public final static int RSNET_SERVER_ECHO = RSNET_NUMBER + 1001;
	public final static int RSNET_ANOTHER_ECHO = RSNET_NUMBER + 1002;
	public final static int RSNET_FIRST_ECHO = RSNET_NUMBER + 1003;
	public final static int RSNET_FIRSTRES_ECHO = RSNET_NUMBER + 1004;

	public final static int RSNET_ECHO_PACKET = RSNET_NUMBER + 1005;
	public final static int RSNET_DATA_PACKET = RSNET_NUMBER + 1006;
	public final static int RSNET_END_PACKET = RSNET_NUMBER + 1007;
	public final static int RSNET_ACTIVEXINFO_PACKET = RSNET_NUMBER + 1008;

	public final static int RSNET_TUNNEL_PACKET = RSNET_NUMBER + 1009;
	public final static int RSNET_RETUNNEL_PACKET = RSNET_NUMBER + 1010;
	public final static int RSNET_COMM_MODE = RSNET_NUMBER + 1011;
	public final static int RSNET_DUMMY_PACKET = RSNET_NUMBER + 1012;
	public final static int RSNET_HOST_RECONNECT_TIME = RSNET_NUMBER+1013;
	public final static int RSNET_VIEWER_SERVER_REC_OPT = RSNET_NUMBER+1014;

	public final static int RSNET_SERVER_MODE = RSNET_NUMBER + 2001;
	// public final static int RSNET_GATEWAY_MODE =RSNET_NUMBER+2002
	// public final static int RSNET_P2P_MODE =RSNET_NUMBER+2003

	public final static int RSNET_RCNT_MODE = RSNET_NUMBER + 3001; // Gateway���
	public final static int RSNET_ORGRSUP_MODE = RSNET_NUMBER + 3002; // P2P ��� V->H
	public final static int RSNET_NEWRSUP_MODE = RSNET_NUMBER + 3003; // P2P ��� H->V

}
