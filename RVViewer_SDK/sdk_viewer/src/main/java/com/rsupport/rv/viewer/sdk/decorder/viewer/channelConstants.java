package com.rsupport.rv.viewer.sdk.decorder.viewer;

//rcp50_cmd.h ���ϳ����� ������
public class channelConstants {
	// channel type
//	enum rcpChannelId
//	{
//		rcpChannelData,						// ����Ÿ ä��
//		rcpChannelScreen,					// ��ũ�� ä��
//		rcpChannelSFTP,						// SFTP ä��
//		rcpChannelRVS,						// RVS ä��
//		rcpChannelPrint,					// Print ä��
//		rcpChannelVoice,					// Voice ä��
//		rcpChannelSound,					// Sound ä��
//		rcpChannelDragDropSFTP,				// Drag Drop SFTP ä��
//		rcpWhiteBoardChannel,
//		
//		rcpChannelSession	= 99,			// Session ä��
//		rcpChannelLast,
//	};
	
	public static final int rcpChannelData = 0;
	public static final int rcpChannelScreen = 1;
	public static final int rcpChannelSFTP = 2;
	public static final int rcpChannelRVS = 3;
	public static final int rcpChannelPrint = 4;
	public static final int rcpChannelVoice = 5;
	public static final int rcpChannelSound = 6;
	public static final int rcpChannelDragDropSFTP = 7;
	public static final int rcpWhiteBoardChannel = 8;
	public static final int rcpChannelHXVideo = 60;
	public static final int rcpChannelHXSound = 61;
	
	public static final int rcpChannelSession	= 99;
	public static final int rcpChannelLast = 100;
		

//	enum rcpFuncType
//	{
//		rcpFuncKB				= 0x00000001,	// Ű���� ��� ����
//		rcpFuncMO				= 0x00000002,	// ���콺 ��� ����
//		rcpFuncDR				= 0x00000004,	// �ܹ��� �׸��� ���
//		rcpFuncSF				= 0x00000008,	// SIMPLE FTP ���
//		rcpFuncVoice			= 0x00000010,	// ����ä��
//		rcpFuncRS				= 0x00000020,	// Viewer ��ȭ�� ��� ���
//		rcpFuncURLP				= 0x00000040,	// URL Push
//		rcpFuncCT				= 0x00000080,	// ä�� ���
//		rcpFuncPL				= 0x00000100,	// ���μ��� ����Ʈ ����
//		rcpFuncSI				= 0x00000200,	// �ý��� �ڻ����� ���� ���
//		rcpFuncSC				= 0x00000400,	// ȭ�� ��ȭ ���
//		rcpFuncCA				= 0x00000800,	// ȭ������
//		rcpFuncCB				= 0x00001000,	// Ŭ������
//		rcpFuncFV				= 0x00002000,	// ���ã�� ���
//		rcpFuncRC				= 0x00004000,	// ������
//		rcpFuncCtrlAltDel		= 0x00008000,	// Ctrl + Alt + Del
//		rcpFuncBlankScreen		= 0x00010000,	// Blank Screen ���
//		rcpFuncMouseTrace		= 0x00020000,	// Mouse Trace
//		rcpFuncLaserPointer		= 0x00040000,	// Laser Pointer
//		rcpFuncWhiteBoard		= 0x00080000,	// White Board
//	};
	public static final int rcpFuncKB = 0x00000001;
	public static final int rcpFuncMO = 0x00000002;
	public static final int rcpFuncDR = 0x00000004;
	public static final int rcpFuncSF = 0x00000008;
	public static final int rcpFuncVoice = 0x00000010;
	public static final int rcpFuncRS = 0x00000020;
	public static final int rcpFuncURLP = 0x00000040;
	public static final int rcpFuncCT = 0x00000080;
	public static final int rcpFuncPL = 0x00000100;
	public static final int rcpFuncSI = 0x00000200;
	public static final int rcpFuncSC = 0x00000400;
	public static final int rcpFuncCA = 0x00000800;
	public static final int rcpFuncCB = 0x00001000;
	public static final int rcpFuncFV = 0x00002000;

	public static final int rcpFuncRC = 0x00004000;
	public static final int rcpFuncCtrlAltDel = 0x00008000;
	public static final int rcpFuncBlankScreen = 0x00010000;
	public static final int rcpFuncMouseTrace = 0x00020000;
	public static final int rcpFuncLaserPointer = 0x00040000;
	public static final int rcpFuncWhiteBoard = 0x00080000;
	
	
	// #define sz_ChannelMsg		(5)
	public static final int sz_ChannelMsg = 5;
	
	
	// ��ſ� ���� type
	
	public static final int RCP_PROTOCOL_START		= 200;
	public static final int PROTOCOL_COMMAND_START =RCP_PROTOCOL_START;
	
	// enum rcpPayloadType
	public static final int rcpChannel			= PROTOCOL_COMMAND_START;
	public static final int rcpChannelNop = rcpChannel+1;			// ä�� ���� ����Ÿ
	public static final int rcpKeyMouseCtrl = rcpChannel+2;			// ���콺 Ű���� ����
	public static final int rcpLaserPointer = rcpChannel+3;			// ������ ������
	public static final int rcpDraw= rcpChannel+4;					// �׸���
	public static final int rcpMonitors= rcpChannel+5;				// ����� ����
	public static final int rcpFavorite= rcpChannel+6;				// ���ã��
	public static final int rcpSysInfo= rcpChannel+7;				// system info
	public static final int rcpProcess= rcpChannel+8;				// process info
	public static final int rcpSFTP= rcpChannel+9;					// ���� ���
	public static final int rcpDragDropFTP= rcpChannel+10;			// Drag&Drop ���� ���
	public static final int rcpPrint= rcpChannel+11;				// ��� ����Ʈ
	public static final int rcpRebootConnect= rcpChannel+12;		// ��� ������� ����
	public static final int rcpSessionTransfer= rcpChannel+13;		// ���� ���
	public static final int rcpSessionShare= rcpChannel+13;			// ���� ����
	public static final int rcpReConnect= rcpChannel+14;			// �翬��
	public static final int rcpClipboard= rcpChannel+15;			// Ŭ������
	public static final int rcpWhiteBoard= rcpChannel+16;			// ȭ��Ʈ����
	public static final int rcpOption= rcpChannel+17;				// �ɼ�
	public static final int rcpAnotherAccountReConnect= rcpChannel+18;	// ��� PC�� �ٸ� �������� ����
	public static final int rcpRVScreenShow= rcpChannel+19;			// ��ȭ�� ����
	public static final int rcpRemoteInfo= rcpChannel+20;			// ���� ����
	public static final int rcpChat = rcpChannel+21;				// ä��
	public static final int rcpScreenCtrl = rcpChannel+22;			// ����Ʈ ȭ�� ���� ��ɾ�
	public static final int rcpResolution = rcpChannel+23;			// �ػ� ���� ���
	public static final int rcpSessionState = rcpChannel+26;		// ��� PC�� ���� ����
	public static final int rcpScreenEngine = rcpChannel+27;
	public static final int rcpChangeEngine = rcpChannel+28;
	public static final int rcpMobile = rcpChannel+31;
	public static final int rcpHXEngineData  = rcpChannel+36;
	public static final int rcpX264Stream = rcpChannel+42;
	public static final int rcpXENC264Stream = rcpChannel+43;
	public static final int rcpRemoteInfoRequest = 0;
	public static final int rcpRemoteInfoData = 1;
	public static final int rcpChannelClose = 8;
	public static final int rcpMobileSpeekerPhoneOnRequest = 70;
	public static final int rcpMobileSpeekerPhoneOffRequest = 72;
	public static final int rcpMobileNotificationDownRequest = 85;
	public static final int rcpMobileNotificationUpRequest = 86;

	public static final int rcpPaloadTypeLast = rcpChannel+22;
	public static final int PROTOCOL_COMMAND_LAST = rcpChannel+23;
	
	public static final int rcpEngineScap = 0;
	public static final int rcpEngineHX = 1;
	
	public static final int rcpClipboardDataRequest 	= 0;
	public static final int rcpClipboardDataInfo 		= 1;
	public static final int rcpClipboardData 			= 2;
	public static final int rcpClipboardDataBegin		= 3;
	public static final int rcpClipboardDataEnd			= 4;
	public static final int rcpClipboardDataString		= 5;
	public static final int rcpClipboardDataString2		= 6;
	public static final int rcpClipboardDataString3		= 7;
	
	public static final int Clipboard_None 			= 0;
	public static final int Clipboard_Text 			= 1;
	public static final int Clipboard_UnicodeText 	= 2;
	public static final int Clipboard_Bitmap 		= 3;

	public static final int rcpChannelListenRequest		= 0;
	public static final int rcpChannelListenConfirm 	= 1;
	public static final int rcpChannelListenReject 		= 2;
	public static final int rcpChannelListenFail		= 3;

	public static final int rcpChannelConnectRequest	= 4;	
	public static final int rcpChannelConnectConfirm	= 5;
	public static final int rcpChannelConnectReject		= 6;
	public static final int rcpChannelConnectFail		= 7;
	
	public static final int rcpNopRequest 	= 0;
	public static final int rcpNopConfirm 	= 1;
	public static final int rcpNopConfirmNoAck 	= 2;

	public static final int rcpRebootConnectResponse = 1;
	public static final int rcpKbdMouseControlAllow =2;
	public static final int rcpAnotherAccountResponse = 3;
	public static final int rcpAnotherAccountReConnectInfo =4;
	public static final int rcpRVScreenShowRequestAllow =5;
	//---------------------------->>
	
	// rcpKeyMouseCtrl
	// enum rcpKeyMouseCtrlMsgId
	public static final int rcpKeyMouseCtrlRequest =0;			// ���콺/Ű���� ��Ʈ�� ��û
	public static final int rcpKeyMouseCtrlConfirm =1;			// ���콺/Ű���� ��Ʈ�� ��û ����
	public static final int rcpKeyMouseCtrlReject =2;			// ���콺/Ű���� ��Ʈ�� ��û �ź�
	public static final int rcpKeyMouseCtrlSuspend =3;			// ���콺/Ű���� ��Ʈ�� �Ͻ�����
	public static final int rcpKeyMouseCtrlResume =4;			// ���콺/Ű���� ��Ʈ�� �ٽ� ����
	public static final int rcpMouseTraceActive =5;				// Host ���콺 ������ ��� Ȱ��ȭ
	public static final int rcpMouseTraceInActive =6;			// Host ���콺 ������ ��� ��Ȱ��ȭ
	public static final int rcpKeyMouseCtrlInputBlock=7;		// ���콺/Ű���� ��Ʈ�� �Ͻ� ����
	public static final int rcpKeyMouseCtrlInputBlockRelease=8;	// ���콺/Ű���� ��Ʈ�� �Ͻ� ���� ����
	public static final int rcpInputClearRequest=11;	// ���콺/Ű���� ��Ʈ�� �Ͻ� ���� ����
	public static final int rcpHostInputLock=14;	//Agent 입력 잠금
	public static final int rcpKeyMouseCtrlMsgId_Last =20;
	public static final int rcpMonkeyTouch = 24;
	public static final int rcpMonkeyKeypad = 26;
	//---------------------------->>	
	
	public static final int rcpButton1Mask =1;
	public static final int rcpButton2Mask =2;
	public static final int rcpButton3Mask =4;
	public static final int rcpButton4Mask =8;
	public static final int rcpButton5Mask =16;
	public static final int rcpWheelUpMask		=rcpButton4Mask;
	public static final int rcpWheelDownMask	=rcpButton5Mask;
	// public static final int sz_rcpMouseEventMsg =6;
	public static final int sz_rcpMouseEventMsg =5;
	//---------------------------->>
	
	// enum rcpKeyMouseEventId
	public static final int rcpKeyEvent	= rcpKeyMouseCtrlMsgId_Last;
	public static final int rcpMouseEvent = rcpKeyMouseCtrlMsgId_Last+1;
	//---------------------------->>
	
	// rcpLaserPointer
	// enum rcpLaserPointerMsgId
	public static final int rcpLaserPointerStart = 0;		// ������������ ���� (rcpMsg.data = rcpLaserPointerType, rcpMsg.data + 1 = POINTS)
	public static final int rcpLaserPointerEnd =1;			// ������������ ���� (noting)
	public static final int rcpLaserPointerPos=2;			// ������������ ��ǥ (rcpMsg.data = POINTS)
	public static final int rcpLaserPointerClick = 3;
	//---------------------------->>
	
	// enum rcpLaserPointerType
	public static final int rcpLaserPointerArrow = 0; // ȭ��ǥ
	public static final int rcpLaserPointerCircle = 1; // ��
	//---------------------------->>

	public static final int rcpSessionLogoffStart = 0;
	
	// rcpDraw
	// enum rcpDrawMsgId
	public static final int rcpDrawStart =0;
	public static final int rcpDrawEnd =1;
	public static final int rcpDrawInfo =2;
	public static final int rcpDrawData =3;
	
	public static final int rcpDrawObjectType = 4;
	public static final int rcpDrawObjectColor = 5;
	public static final int rcpDrawObjectWidth = 6;

	public static final int rcpDrawClear = 7;

	public static final int rcpDrawLast = 30;

	//enum rcpDrawType
	public static final int rcpDrawFreeLine = 0;
	public static final int rcpDrawLine = 1;
	public static final int rcpDrawRectangle = 2;
	public static final int rcpDrawEllipse = 3;
	public static final int rcpDrawArrow = 4;
	public static final int rcpDrawEraser = 8;
	
	// rcpFavorite
	// enum rcpFavoriteMsgId
	public static final int rcpFavoriteURL = 0;
	public static final int rcpFavoriteCP = 1;					// Control panel
	public static final int rcpFavoriteFolder = 2;				// folder
	public static final int rcpFavoriteEX = 3;					// ����
	public static final int rcpFavoriteHotkey = 4;				// hotkey
	public static final int rcpFavoriteMsgIdLast = 100;
	//---------------------------->>
	
	// enum rcpFavoriteCPMsgId
	public static final int rcpFavoriteCPListRequest = rcpFavoriteMsgIdLast; // ������ ��� �䱸(.cpl)
	public static final int rcpFavoriteCPList = rcpFavoriteCPListRequest + 1;		 // ������ ���
	public static final int rcpFavoriteCPMsgIdLast = 150;
	//---------------------------->>
	
	// enum rcpFavoriteHotkeyMsgId
	public static final int rcpHotkeyCtrlAltDel = rcpFavoriteCPMsgIdLast; // Ctrl+Alt+Del
	public static final int rcpHotkeyFileSendDialog = rcpHotkeyCtrlAltDel +1;
	public static final int rcpFavoriteHotkeyMsgIdLast = 200;
	
	// rcpSysInfo
	//enum rcpSysInfoMsgId
	public static final int rcpCpuMemInfoRequest = 0;
	public static final int rcpCpuMemInfo = 1;
	public static final int rcpSystemInfoRequest = 2;
	public static final int rcpSystemInfo = 3;
	public static final int rcpSystemInfoError = 4;
	
	// rcpProcess
	//enum rcpProcessMsgId
	public static final int rcpProcessListRequest = 0;
	public static final int rcpProcessList = 1;
	public static final int rcpProcessKillRequest = 2;
	
	// rcpSFTP
	// enum rcpSFTPMsgId
	public static final int rcpSFTPNone			= -1;
	public static final int rcpSFTPRequest=0;					// file ��� Ȯ��(file ������ ������) - on datachannel
	public static final int rcpSFTPConfirm=1;					// file ��� ����(Yes) - on DataChannel
	public static final int rcpSFTPReject=2;					// file ��� �ź�(No)
	public static final int rcpSFTPStart=3;					// SFTP Start
	public static final int rcpSFTPHeader=4;					// File Header
	public static final int rcpSFTPStartPos=5;				// File position(�̾�ޱ����� file�� ���� ��ġ)
	public static final int rcpSFTPNext=6;					// file ���� ���Ϸ� skip
	public static final int rcpSFTPData=7;					// file data
	public static final int rcpSFTPDataEnd=8;					// file data ��
	public static final int rcpSFTPEnd=9;						// end
	public static final int rcpSFTPCancel=10;					// ���
	public static final int rcpSFTPError=11;					// error
	public static final int rcpSFTPSendRequest=12;				// ���濡�� ����Ÿ ��� �䱸		
	public static final int rcprcpSFTPMsgId_Last=13;
	//---------------------------->>
	
	// rcpDragDropFTP
	// enum rcpDragDropFTPMsgId
	public static final int rcpDragDropFTP_Data = rcprcpSFTPMsgId_Last;
	public static final int rcpDragDropFTP_DND_LEAVE_LBTN = rcpDragDropFTP_Data+1;
	public static final int rcpDragDropFTP_CNP_TARGET=rcpDragDropFTP_Data+2;
	public static final int rcpDragDropFTP_CNP_CLEAR=rcpDragDropFTP_Data+3;
	//---------------------------->>
	
	// rcpPrint
	// enum rcprcpPrintMsgId
	// rcpPrint
	public static final int rcpPrintEvent=0;						// ���� ��� ���� Event
	public static final int rcpPrintStart=1;					// ���� ��� ���� (rcpPrintInfoMsg)
	public static final int rcpPrintDocInfo=2;					// ���� ���� ����Ÿ (.prd ���� ���)
	public static final int rcpPrintStartDoc=3;					// ���� ����
	public static final int rcpPrintStartPage=4;				// ���� ������ ���� (rcpPrintPageInfo)
	public static final int rcpPrintPageData=5;					// ���� ������ ����Ÿ
	public static final int rcpPrintEndPage=6;					// ���� ������ �� (�� command�� ���� �� file print)
	public static final int rcpPrintEnd=7;						// ���� ��� ��
	public static final int rcpPrintCancel=8;						// ���
	public static final int rcprcpPrintMsgId_Last =9;
	//---------------------------->>
	
	public static final int sz_rcpPacket =5;
	public static final int  sz_rcpMessage	=1;
	public static final int  sz_rcpDataMessage	=5;
	public static final int  sz_rcpZipHeader = 8; //sizeof(rcpZipHeader)
	
	
	// KeyMouseControlState
	public static final int KeyMouseControl_None = 0x00;
	public static final int KeyMouseControl_Keyboard = 0x01; // Ű���� ����(0x01)
	public static final int KeyMouseControl_Mouse = 0x02; // ���콺 ����(0x02)
	public static final int KeyMouseControl_Suspend = 0x04; // Ű����/���콺 �Ͻ�����
	
	// rcpSpecialKeyStateType
	public static final int rcpKeyCapsLock = 0x01;
	public static final int rcpKeyNumLock = 0x02;
	public static final int rcpKeyScrollLock = 0x04;
	public static final int rcpKeyShift = 0x08;
	
	public static final int szrcpSFTPDataSize = 1024 * 1000;
	
	public static final int rcpChatOpen = 0;
	public static final int rcpChatClose = 1;
	public static final int rcpChatInput = 2;
	
	public static final int rcpDrawInteractiveStart = 30;
	public static final int rcpDrawInteractiveEnd = 31;

	public static final int rcpOption_rcOption = 0;			// _Function + _Opt + _SF_MaxFileSize
	public static final int rcpOption_Funtion = 1;			// 기능
	public static final int rcpOption_Opt = 2;				// 옵션
	public static final int rcpOption_SF_MaxFileSize = 3;	// 파일 전송시 제한 용량
	public static final int rcpOption_SCap = 4;				// SCAP
	public static final int rcpOption_RPrinter = 5;			// RemotePrinter 버전
	public static final int rcpOption_Setting = 6;			// SETTINGINFO
	public static final int rcpOption_KeybdLang = 7;		// 키보드언어
	public static final int rcpOption_hostOption = 8;		// 상담원 쪽에서 고객쪽 옵션 제어용

	public static final int rcpSessionShareRequest = 0;					// ������� ��û
	public static final int rcpSessionShareConfirm = 1;					// ���ǰ��� ����
	public static final int rcpSessionShareReject = 2;					// ���ǰ��� ����
	public static final int rcpSessionShareInfo = 3;					// ���ǰ��� ����
	public static final int rcpSessionShareConnected = 4;				// ���ǰ����� ������ ����Ǿ���.
	public static final int rcpSessionShareDisconnected = 5;			// ���ǰ����� ������ ������ ������.
	
	//rcpMonitors
	public static final int rcpMonitorsInfoRequest = 0;	
	public static final int rcpMonitorsInfoResponse = 1;	
	public static final int rcpMonitorSelect = 2;
	public static final int rcpMonitorChangeRequest = 3;
	public static final int rcpMonitorChangeResponse = 4;

	//rcpResolution
	public static final int rcpResolutionCurrentMode = 0;			// ���� �ػ�
	public static final int rcpResolutionEnumMode = 1;				// ���� ������ �ý��� �ػ�
	public static final int rcpResolutionChange = 2;				// ����� �ػ� ����
	
	//rcpRebootConnect
	public static final int rcpRebootConnectRequest = 0;			// ����� ���� ��û
	public static final int rcpRebootConnectConfirm = 1;			// ����� ���� ����
	public static final int rcpRebootConnectReject = 2;				// ����� ���� ����
	public static final int rcpRebootConnectInfo = 3;				// ����� ���� ����
	public static final int rcpRebootConnectFail = 4;				// ����� ���� ����
	public static final int rcpRebootSafemodeConnectRequest = 5;	// ������ ����� ���� ��û
	public static final int rcpRebootSafemodeConnectInfo = 6;		// ������ ����� ���� ����
	public static final int rcpRebootSafemodeConnectConfirm = 7;	// ������ ����� ���� ����
	public static final int rcpRebootSafemodeConnectReject = 8;		// ������ ����� ���� ����
	public static final int rcpRebootSafemodeConnectFail = 9;		// ������ ����� ���� ����
	
	//rcpAnotherAccountReConnect
	public static final int rcpReConnectRequest = 0;
	public static final int rcpReConnectConfirm = 1;
	public static final int rcpReConnectReject = 2;
	public static final int rcpReConnectInfo = 3;
	public static final int rcpReConnectFail = 4;
	
	/*
	 * RemoteView Code Start
	 */
	//rcpScreenCtrl
	public static final int rcpScreenSuspend = 0;
	public static final int rcpScreenResume = 1;
	public static final int rcpScreenBlankStart = 50;
	public static final int rcpScreenBlankEnd = 51;
	public static final int rcpScreenBlankStatus = 52;
	/*
	 * RemoteView Code End 
	 */
	
	public static final int rcpSoundShare = 225;
	
	public static final int rcpSound_Data = 0;     		// host --> viewer
	public static final int rcpSound_Vol = 1;			// viewer <--> host
	public static final int rcpSound_CodeInfo = 2;		// viewer --> host
	public static final int rcpSound_ErrorInfo = 3;		// host --> viewer
	public static final int rcpSound_ErrorInfo_Format = 4;
	public static final int rcpSound_VersionInfo = 5;

	public static final int rcpEngineHeader = 0;
	public static final int rcpAVChannelReady = 1;
	public static final int rcpVideoStart = 2;
	public static final int rcpAudioStart = 3;
	public static final int rcpVideoStop = 4;
	public static final int rcpAudioStop = 5;
	public static final int rcpVideoDataHeader = 6;
	public static final int rcpVideoData = 7;
	public static final int rcpAudioHeader = 8;
	public static final int rcpAudioData = 9;
	public static final int rcpHXMouseEvent = 10;
	public static final int rcpEngineChange = 11;
	public static final int rcpEngineChanged = 12;
	public static final int rcpEngineOption = 13;
	public static final int rcpHXQualityChange = 14;
	public static final int rcpHXRequestCurFrameIndex = 15;
	public static final int rcpHXReceiveCurFrameIndex = 16;
	public static final int rcpVideoDataHeaderNData = 17;
	
	// x264 Video
	// msgID
	// By DataChannel
	public static final int rcpX264StreamStart = 0; // 영상스트리밍 시작
	public static final int rcpX264StreamStop = 1; // 영상스트리밍 정지
	public static final int rcpX264StreamResume = 5;
	public static final int rcpX264StreamPause = 6;
	public static final int rcpX264StreamReload = 10;

	//xenc
	public static final int rcpXENC264StreamStart = 0;
	public static final int rcpXENC264StreamStop = 1;
	public static final int rcpXENC264StreamData = 2;
	public static final int rcpXENC264StreamOption = 3;
	public static final int rcpXENC264StreamHeader = 4;
	public static final int rcpXENC264StreamMakeKeyFrame = 99;

	// By ScreenChannel
	public static final int rcpX264StreamHeader = 2; // Video Header (아래에 정의됨)
	public static final int rcpX264StreamHeaderRec = 100; // Record를 위한 Header		
	public static final int rcpX264StreamSPPS = 102; // Video SPS, PPS
	public static final int rcpX264StreamData = 103; // Video Data
	
	// Mobile
	public static final int rcpMobileSystemInfoRequest = 0;
	public static final int rcpMobileSystemInfoResponse = 1;

	// vrvd Video
	// msgID
	// By DataChannel
	public static final int rcpVRVDStreamStart = 0; // 영상스트리밍 시작
	public static final int rcpVRVDStreamStop = 1; // 영상스트리밍 정지
	public static final int rcpVRVDStreamResume = 5;
	public static final int rcpVRVDStreamPause = 6;

	// By ScreenChannel
	public static final int rcpVRVDStreamHeader = 2; // Video Header (아래에 정의됨)
	public static final int rcpVRVDStreamData = 103; // Video Data
}
