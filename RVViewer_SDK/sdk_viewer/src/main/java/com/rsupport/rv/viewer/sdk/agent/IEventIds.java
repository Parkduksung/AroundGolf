package com.rsupport.rv.viewer.sdk.agent;

/**
 * Created by tonykim on 2016. 3. 16..
 */
public interface IEventIds {
    public final static int EVENT_ID_COMMON_BASE = 9000;
    public final static int EVENT_ID_COMMON_PROG_CLOSE = EVENT_ID_COMMON_BASE + 1;

    /** Main **/
    public final static int EVENT_ID_FAIL_LOGIN = 0;
    public final static int EVENT_ID_SHOW_URL = 1;
    public final static int EVENT_ID_EXPIRED = 2;
    public final static int EVENT_ID_UPDATE = 3;
    public final static int EVENT_ID_UPDATE_LATE = 4;
    public final static int EVENT_ID_CHANGE_NEW_JOIN = 5;
    public final static int EVENT_ID_SHOW_PROXY_HELP = 6;

    /** AgentLogin, GoogleOTP, EMAIL TWOFACTOR **/
    public final static int EVENT_ID_ERROR_MESSAGE = 0;
    public final static int EVENT_ID_COMPUTER_ACTIVE_OK = 1;
    public final static int EVENT_ID_MORE_SETTING = 2;
    public final static int EVENT_ID_CONNECT_ERROR = 3;
    public final static int EVENT_ID_EMAIL_RESEND = 4;


    /** AMTPower **/
    public final static int EVENT_ID_MESSAGE = 0;
    public final static int EVENT_ID_NOTHING_GATEWAY = 1;
    public final static int EVENT_ID_TOP_LISTVIEW = 2;
    public final static int EVETN_ID_SEL_GATEWAY = 1000;	// event check >> EVETN_ID_SEL_GATEWAY + gate selet number
    public final static int EVENT_ID_POWER_ON = 60;				// 로그오프
    public final static int EVENT_ID_POWER_OFF = 70;			// 시스템 종료
    public final static int EVENT_ID_POWER_RESET = 80;		// 시스템 재시작

    /** AgentInfo **/
    public final static int EVENT_ID_NO = 0;
    public final static int EVENT_ID_CONNECT_CHECK1 = 11;		// 연결 확인 WOL HW
    public final static int EVENT_ID_CONNECT_CHECK2 = 12;		// 연결 확인 WOL MC
    public final static int EVENT_ID_EXPLOLER = 20;			    // 원격 탐색기 dialog
    public final static int EVENT_ID_REMOTE_EXPLORER = 21;		// 원격 탐색기
    public final static int EVENT_ID_VPRO_POWER = 30;			// 원격 해상도 설정
    public final static int EVENT_ID_CAPTURE = 40;				// 원격화면 캡쳐
    public final static int EVENT_ID_INFORMATION = 50;			// 등록정보
    public final static int EVENT_ID_LOGOFF = 60;				// 로그오프
    public final static int EVENT_ID_SYSTEM_EXIT = 70;			// 시스템 종료
    public final static int EVENT_ID_SYSTEM_RESATRT = 80;		// 시스템 재시작
    public final static int EVENT_ID_WOL_SYSTEM_ON = 90;		// WOL 시스템 재시작 dialog
    public final static int EVENT_ID_WOL_SYSTEM_ON_ALL = 91;	// WOL all 시스템 재시작dialog
    public final static int EVENT_ID_START_WAKEON_LAN = 92;	// WOL 시스템 재시작
    public final static int EVENT_ID_START_WAKEON_ALL_LAN = 93;// WOL all 시스템 재시작
    public final static int EVENT_ID_AGENT_DELETE = 100;		// 에이전트 삭제
    public final static int EVENT_ID_UNINSTALL = 101;			// 에이전트 삭제 확보
    public final static int EVENT_ID_LIC_ACTIVATE = 102;		// 라이센스 활성화
    public final static int EVENT_ID_SESSION_CLOSE = 103;		// 원격제어 강제 종료

    /** AgentList **/
    public final static int EVENT_ID_MSG_CALCEL = 0;
//    public final static int EVENT_ID_SESSION_CLOSE = 2;
    public final static int EVENT_ID_START_REMOTE_CONTROL = 3;
    public final static int EVENT_ID_START_VPRO_POWER = 5;
    public final static int EVENT_ID_START_APPLY_FORM = 8;

    /** Computer **/
    public final static int EVENT_ID_MESSAGE_CANCEL	    = 0;
    public final static int EVENT_ID_COMPUTER_ACTIVE 	= 1;
//    public final static int EVENT_ID_MORE_SETTING 		= 2;

    /** ScreenCanvas **/
//    public final static int EVENT_ID_CONNECT_ERROR			= 0;
    public final static int EVENT_ID_CONNECT_AGENT_INFO 	= 1;
    public final static int EVENT_ID_FORCE_CONNECT_YES      = 4;
    public final static int EVENT_ID_FORCE_CONNECT_NO       = 5;
    public final static int EVENT_ID_ALERT_LOGOFF 			= 10;
    public final static int EVENT_ID_ALERT_RESTART 		    = 11;
    public final static int EVENT_ID_ALERT_WINCLOSE 		= 12;
    public final static int EVENT_ID_ALERT_CONNCLOSE		= 13;
    public final static int EVENT_ID_ALERT_CLOSE 			= 14;
    public final static int EVENT_ID_ALERT_CANCEL 			= 15;
    public final static int EVENT_ID_ALERT_SYSEXIT			= 16;
    public final static int EVENT_ID_ALERT_PROGEXEC 		= 17;
    public final static int EVENT_ID_RESOLUTION_RESTRICTION = 18;
    public final static int EVENT_ID_MONITOR_SWITCHING      = 19;
    
    /** About **/
    public final static int EVENT_ID_LANGAUGE_SETTING = 41;
    public final static int EVENT_ID_VALIDATE_FAIL_URL = 42;

    /** AgentCommon **/
    public final static int EVENT_ID_START_EXTRA_FUNC = 200;
    public final static int EVENT_ID_MORE_SETTING_URL = 201;

    /** Process **/
    /** 프로세스 종료의 경우, process 값을 일일이 저장할 수 없으므로 이벤트ID 값 + 프로세스id 를 이벤트로 전달함 **/
    public final static int EVENT_ID_KILL_PROCESS = 1000;
    public final static int EVENT_ID_KILL_PROCESS_OK = 1;
    public final static int EVENT_ID_KILL_PROCESS_CANCEL = 2;

    /** ProgramExec **/
    public final static int EVENT_ID_EXE_NOTEPAD		= 1;
    public final static int EVENT_ID_EXE_FILEEXPLORE	= 2;
    public final static int EVENT_ID_EXE_DRAWBOARD		= 3;
    public final static int EVENT_ID_EXE_IEEXPLORER	= 4;
    //	public final static int EVENT_ID_EXE_MS_EXCEL		= 6;
    public final static int EVENT_ID_EXE_TASKMGR		= 7;
    public final static int EVENT_ID_EXE_TEXTEDIT		= 10;
    public final static int EVENT_ID_EXE_SAFARI		= 11;
    public final static int EVENT_ID_EXE_CALCULATOR	= 12;
    public final static int EVENT_ID_EXE_FINDER 		= 13;
    public final static int EVENT_ID_EXE_DIRECT_INPUT_OK		= 8;
    public final static int EVENT_ID_EXE_DIRECT_INPUT_CANCEL	= 9;
}
