package com.rsupport.rv.viewer.sdk.session;


import static com.rsupport.rv.viewer.sdk.constant.ConstantsKt.MODE_XENC;

import android.util.Log;

import androidx.annotation.NonNull;

import com.rsupport.android.push.RSPushMessaging;
import com.rsupport.rscommon.crypto.Base64;
import com.rsupport.rv.viewer.sdk.common.RuntimeData;
import com.rsupport.rv.viewer.sdk.common.log.RLog;
import com.rsupport.rv.viewer.sdk.commons.rc45.RC45Stream;
import com.rsupport.rv.viewer.sdk.decorder.Converter;
import com.rsupport.rv.viewer.sdk.decorder.viewer.channel.CRCChannel;
import com.rsupport.rv.viewer.sdk.decorder.viewer.channel.CRCSoundChannel;
import com.rsupport.rv.viewer.sdk.decorder.viewer.channel.CRCVDataChannel;
import com.rsupport.rv.viewer.sdk.decorder.viewer.channel.CRCVScreenChannel;
import com.rsupport.rv.viewer.sdk.decorder.viewer.channel.CRCVSessionChannel;
import com.rsupport.rv.viewer.sdk.decorder.viewer.channel.rcpChannelMsg;
import com.rsupport.rv.viewer.sdk.decorder.viewer.channel.rcpKeyEventMsg;
import com.rsupport.rv.viewer.sdk.decorder.viewer.channelConstants;
import com.rsupport.rv.viewer.sdk.setting.ConnectValueKt;
import com.rsupport.rv.viewer.sdk.setting.GlobalStatic;
import com.rsupport.rv.viewer.sdk.util.NetworkUtil;

import org.jetbrains.annotations.NotNull;


/**
 * Created by hyosang on 2017. 10. 10..
 */

public class SessionManager {
    public static final int CHANNELNUM_DATA = 0;
    public static final int CHANNELNUM_SCREEN = 1;
    public static final int CHANNELNUM_SOUND = 6;
    public static final int CHANNELNUM_SESSION = 99;

    private static final int RECONNECT_TIMEOUT = 60 * 1000;     //60 sec

    public enum SessionStatus {
        CREATED,
        CONNECTING,
        CONNECTED,
        CONNECT_FAILED,
        DISCONNECTING,
        DISCONNECTED,
        PENDING_FOR_RECONNECT,
        RECONNECTING_WITH_SESSIONCHANNEL
    }

    public enum ConnectForceStatus {
        UNDEFINED,
        WAITING,
        ACCEPTED,
        DENIED
    }


    private SessionStatus sessionStatus = SessionStatus.CREATED;
    private ISessionListener sessionListener = null;

    private CRCVDataChannel dataChannel = null;
    private CRCVScreenChannel screenChannel = null;
    private CRCVSessionChannel sessionChannel = null;

    private CRCSoundChannel soundChannel = null;

    private CRCVDataChannel.IDataChannelListener dataChannelPacketListener = null;

    private String dataGuid;
    private String screenGuid;
    private String sessionGuid;
    private SessionOptions sessionOptions;

    private rcpKeyEventMsg keyEventMsg = new rcpKeyEventMsg();

    private SessionManager(SessionOptions sessionOptions, ISessionListener listener) {
        sessionListener = listener;
        this.sessionOptions = sessionOptions;
        RuntimeData.getInstance().initSession();
    }

    public static SessionManager create(@NotNull SessionOptions sessionOptions, @NotNull ISessionListener listener) {
        return new SessionManager(sessionOptions, listener);
    }

    public void startSession() {
        startSession(false);
    }

    private void startSession(boolean usePreviousGuid) {
        if (usePreviousGuid) {
            sessionStatus = SessionStatus.RECONNECTING_WITH_SESSIONCHANNEL;
        } else {
            sessionStatus = SessionStatus.CONNECTING;
        }

        connectDataChannel(dataChannelListener, usePreviousGuid);
    }

    private void connectDataChannel(CRCChannel.IChannelListener listener, boolean isReconnectUsingSession) {
        if (isReconnectUsingSession) {
            if (screenChannel != null) {
                screenChannel.releaseAll();
                screenChannel = null;
            }

            if (dataChannel != null) {
                dataChannel.releaseAll();
                dataChannel = null;
            }
        }

        if (dataChannel == null) {

            String guid = (isReconnectUsingSession ? dataGuid : null);

            dataChannel = new CRCVDataChannel(sessionOptions.isUseServerRec(), guid);
//            monitorSwitchController = new MonitorSwitchController(dataChannel);
            dataChannel.setListener(listener);
            dataChannel.setDataChannelListener(dataChannelPacketListener);
//            dataChannel.setMonitorSwitchControlListener(monitorSwitchController.getMonitorSwitchControlListener());
            dataChannel.startThread();
            dataGuid = dataChannel.getGuid();
        } else {
            RLog.w("Already exists datachannel instance!");
        }
    }

    private void connectScreenChannel(@NonNull CRCVScreenChannel.IScreenChannelListener listener) {
        if (screenChannel == null) {
            screenChannel = new CRCVScreenChannel(sessionOptions.isUseServerRec());
            screenChannel.setListener(listener);
//            screenChannel.setMonitorSwitchControlListener(monitorSwitchController.getMonitorSwitchControlListener());
            screenChannel.startThread();
            screenGuid = screenChannel.getGuid();
        } else {
            RLog.w("Already exists screenchannel instance!");
        }
    }

    private void connectSessionChannel(CRCVSessionChannel.ISessionChannelListener listener) {
        if (sessionChannel == null) {
            sessionChannel = new CRCVSessionChannel(sessionOptions.isUseServerRec());
            sessionChannel.setListener(listener);
            sessionChannel.startThread();

            sessionGuid = sessionChannel.getGuid();
        } else {
            RLog.w("Already exists sessionchannel instance!");
        }
    }

//    public boolean connectToAgentPage(boolean bForce, String guid) {
//        try {
//            if (ConnectValueKt.isXenc()) {
//                WebConnection.getInstance().requestSessionConnect(bForce, guid, RuntimeData.getInstance().currentSessionData.userGuid, dataChannel.getSocket().getListenPort(), false);
//            } else {
//                WebConnection.getInstance().requestSessionConnect(bForce, guid, dataChannel.getSocket().getListenPort(), false);
//            }
//        } catch (RSException e) {
//            RLog.e(e);
//            return false;
//        }
//        return true;
//    }

    public void setDataPacketListener(CRCVDataChannel.IDataChannelListener packetListener) {
        if (dataChannel != null) {
            dataChannel.setDataChannelListener(packetListener);
        }
        this.dataChannelPacketListener = packetListener;

    }

    public boolean sendChannelConnectRequest(CRCChannel sendChannel, int connectChannelNum, String guid, int port) {
        int nDataSize = ((guid.length() + 1) * 2);
        int nPacketSize = channelConstants.sz_ChannelMsg + nDataSize;

        rcpChannelMsg msg = new rcpChannelMsg();
        msg.idchannel = (byte) connectChannelNum;
        msg.port = port;
        msg.data = new byte[nDataSize];
        Converter.convert2ByteArray(guid, msg.data, true);

        byte[] buf = new byte[nDataSize + 5];
        msg.push(buf, 0);
        boolean bSend = sendChannel.sendPacket(channelConstants.rcpChannel, channelConstants.rcpChannelConnectRequest, buf, nPacketSize);

        return bSend;
    }

    private void requestAgentConnect(String channelGuid, int listenPort) {
//        boolean isForceConnect = isForceConnect();

        boolean retry;
        do {
            retry = false;
            if (ConnectValueKt.isXenc()) {
//                    WebConnection.getInstance().requestSessionConnect(isForceConnect, channelGuid, RuntimeData.getInstance().currentSessionData.userGuid, listenPort, false);
                String localIp = NetworkUtil.getLocalIP();
                RuntimeData rtData = RuntimeData.getInstance();
                byte [] macAddr = rtData.mac16Upper.getBytes();
                byte [] loginid = rtData.userInfo.loginId.getBytes();
                byte [] daemonHost = "strelay2.rview.com".getBytes();
                byte [] byteGuid = channelGuid.getBytes();
                byte [] byteLogkey = "logkey".getBytes();
                byte [] viewerMode = "xenc".getBytes();

                byte[] memStream = new byte[
                        (4 + loginid.length) + // Web Login ID
                        (4 + localIp.getBytes().length) + // Viewer Listen IP
                        4 + // Viewer Listen Port
                        (4 + daemonHost.length) + // Daemon IP
                        4 + // Daemon Port
                        1 + // Use SSL
                        (4 + byteGuid.length) + // GUID
                         4 + //RemoteControlForceConnect
                        (4 + macAddr.length) + // MacAddr
                        (4 + byteLogkey.length) + // LogKey
                         1 + //AutoSystemLock
                         4 + //LegacyType
                         4 +	 //SSL TYPE
                         4 + // ExecuteRestriction
                         4 + // SystemLockMode
                         4 + // UseUrlFilter
                         4 + // UseVideoMode
                         4 + viewerMode.length + // viewerMode
                         4 + // AutoScreenLock
                         4 + // UseScreenLockType
                         4]; // ScreenLockType

                int startPos = 0;

                // Web Login ID
                System.arraycopy(Converter.getBytesFromIntLE(loginid.length), 0, memStream, startPos, 4);
                startPos += 4;
                System.arraycopy(loginid, 0, memStream, startPos, loginid.length);
                startPos += loginid.length;

                // Viewer Listen IP
                System.arraycopy(
                        Converter.getBytesFromIntLE(localIp.getBytes().length), 0,
                        memStream, startPos, 4);
                startPos += 4;
                System.arraycopy(localIp.getBytes(), 0, memStream, startPos,
                        localIp.getBytes().length);
                startPos += localIp.getBytes().length;

                // Viewer Listen Port
                System.arraycopy(Converter.getBytesFromIntLE(443), 0, memStream, startPos, 4);
                startPos += 4;

                // Daemon IP
                System.arraycopy(Converter.getBytesFromIntLE(daemonHost.length), 0, memStream, startPos, 4);
                startPos += 4;
                System.arraycopy(daemonHost, 0, memStream, startPos, daemonHost.length);
                startPos += daemonHost.length;

                // Daemon Port
                System.arraycopy(Converter.getBytesFromIntLE(443), 0, memStream, startPos, 4);
                startPos += 4;

                // Use SSL
                byte[] sslByte = Converter.getBytesFromIntLE(1);	//SSL 사용
                System.arraycopy(new byte[] { sslByte[0] }, 0, memStream, startPos, 1);
                startPos += 1;

                // GUID
                System.arraycopy(
                        Converter.getBytesFromIntLE(byteGuid.length),
                        0, memStream, startPos, 4);
                startPos += 4;
                System.arraycopy(byteGuid, 0, memStream, startPos, byteGuid.length);
                startPos += byteGuid.length;

                //RemoteControlForceConnect
                System.arraycopy(Converter.getBytesFromIntLE(0), 0, memStream,
                startPos, 4);
                startPos += 4;

                // MacAddr
                System.arraycopy(Converter.getBytesFromIntLE(macAddr.length), 0, memStream, startPos, 4);
                startPos += 4;
                System.arraycopy(macAddr, 0, memStream, startPos, macAddr.length);
                startPos += macAddr.length;

                // LogKey
                System.arraycopy(Converter.getBytesFromIntLE(byteLogkey.length),0, memStream, startPos, 4);
                startPos += 4;
                System.arraycopy(byteLogkey, 0, memStream, startPos, byteLogkey.length);
                startPos += byteLogkey.length;

                //AutoSystemLock
                byte[] lockByte = Converter.getBytesFromIntLE(0);
                System.arraycopy(new byte[]{lockByte[0]}, 0, memStream, startPos, 1);
                startPos += 1;

                //LegacyType //new version : 3(4byte), old version : 0(4byte)
                System.arraycopy(Converter.getBytesFromIntLE(3), 0, memStream, startPos, 4);
                startPos += 4;

                //SSL COMMTYPE
                System.arraycopy(Converter.getBytesFromIntLE(RC45Stream.SSL_TYPE), 0, memStream, startPos, 4);
                startPos += 4;

                System.arraycopy(Converter.getBytesFromIntLE(0), 0, memStream, startPos, 4);
                startPos += 4;

                System.arraycopy(Converter.getBytesFromIntLE(0), 0, memStream, startPos, 4);
                startPos += 4;

                System.arraycopy(Converter.getBytesFromIntLE(0), 0, memStream, startPos, 4);
                startPos += 4;

                System.arraycopy(Converter.getBytesFromIntLE(0), 0, memStream, startPos, 4);
                startPos += 4;

                System.arraycopy(Converter.getBytesFromIntLE(viewerMode.length), 0, memStream, startPos, 4);
                startPos += 4;
                System.arraycopy(viewerMode, 0, memStream, startPos, viewerMode.length);
                startPos += viewerMode.length;

                System.arraycopy(Converter.getBytesFromIntLE(0), 0, memStream, startPos, 4);
                startPos += 4;

                System.arraycopy(Converter.getBytesFromIntLE(0), 0, memStream, startPos, 4);
                startPos += 4;

                System.arraycopy(Converter.getBytesFromIntLE(0), 0, memStream, startPos, 4);
                startPos += 4;

                RLog.d("Push Send Message");
                RSPushMessaging.getInstance(null).send("OK12345/agent", Base64.encodeBytesToBytes(memStream));
            } else {
//                    WebConnection.getInstance().requestSessionConnect(isForceConnect, channelGuid, listenPort, false);
            }
        } while (retry);
    }


    public void openSoundChannel(CRCSoundChannel.SoundChannelProgressListener soundChannelProgressListener) {
        if ((soundChannel != null) && (soundChannel.isThreadRun())) {
            RLog.w("SoundChannel is already running...");
        } else {
            soundChannel = new CRCSoundChannel();
            soundChannel.setListener(soundChannelListener);
            soundChannel.setSoundChannelProgressListener(soundChannelProgressListener);
            soundChannel.startThread();
        }
    }

    public void setScreenPause() {
        screenChannelListener.setScreenPause(true);
        if (GlobalStatic.isPhonetoPhone()) {
            if (dataChannel != null) {
                dataChannel.sendPacket(channelConstants.rcpX264Stream, channelConstants.rcpX264StreamStop);
            }
        } else {
            if (RuntimeData.getInstance().getAgentConnectOption().isHxEngine()) {
                if (!ConnectValueKt.isXenc()) {
                    if (screenChannel != null) {
                        screenChannel.sendVideoStop();
                    }
                }
            }
        }

        if (dataChannel != null) {
            dataChannel.sendPacket(channelConstants.rcpScreenCtrl, channelConstants.rcpScreenSuspend);
        }
    }


    public void setScreenResume() {
        screenChannelListener.setScreenPause(false);
        if (GlobalStatic.isPhonetoPhone()) {
            if (dataChannel != null) {
                dataChannel.sendPacket(channelConstants.rcpX264Stream, channelConstants.rcpX264StreamStart);
            }
        } else {
            if (RuntimeData.getInstance().getAgentConnectOption().isHxEngine()) {
                if (!ConnectValueKt.isXenc()) {
                    if(screenChannel != null){
//                        screenChannel.sendReqVideoInfo(monitorSwitchController.getSelectMonitorNumber());
                    }
                } else {
                    dataChannel.reqResetFrame();
                }
            }
        }

        if (dataChannel != null) {
            dataChannel.sendPacket(channelConstants.rcpScreenCtrl, channelConstants.rcpScreenResume);
        }
    }

    public void closeSoundChannel() {
        if (soundChannel != null) {
            soundChannel.stopAudio();
            soundChannel.releaseAll();
            soundChannel = null;
        }
    }

    public void release() {
        sessionStatus = SessionStatus.DISCONNECTING;

        //세션채널 먼저 닫는다.
        if (sessionChannel != null) {
            sessionChannel.releaseAll();
            sessionChannel = null;
        }

        closeSoundChannel();

        if (screenChannel != null) {
            screenChannel.releaseAll();
            screenChannel = null;
        }

        if (dataChannel != null) {
            dataChannel.releaseAll();
            dataChannel = null;
        }
    }

    public void sendKeyEvent(int keyCode, boolean isPress, int specialKey) {
        if (dataChannel != null) {
            dataChannel.sendPacket(channelConstants.rcpScreenCtrl, channelConstants.rcpScreenResume);
            keyEventMsg.down = isPress ? (byte) 1 : (byte) 0;
            keyEventMsg.key = keyCode;
            keyEventMsg.specialkeystate = (short) specialKey;

            dataChannel.sendPacket2(channelConstants.rcpKeyMouseCtrl, channelConstants.rcpKeyEvent, keyEventMsg, keyEventMsg.size());
        }
    }


    private void onChannelDisconnected(int channelNum) {
        RLog.i("onChannelDisconnected." + channelNum + ", sessionStatus." + sessionStatus);
        if ((channelNum == CHANNELNUM_DATA) || (channelNum == CHANNELNUM_SCREEN)) {
            //필수 채널에서 끊기는 상황
            if ((sessionChannel != null) && (sessionChannel.isConnected())) {
                if (sessionStatus == SessionStatus.CONNECTED) {
                    sessionStatus = SessionStatus.PENDING_FOR_RECONNECT;

                    if (sessionListener != null) {
                        sessionListener.onSessionReconnecting();
                    }

                    //세션 채널이 살아있다면 재연결 시도. 직후에 세션채널도 닫힐 수 있으므로 약간의 지연을 둔다.
                    (new Thread() {
                        @Override
                        public void run() {
                            long baseTime = System.currentTimeMillis();

                            try {
                                Thread.sleep(5000);

                                while (sessionStatus == SessionStatus.PENDING_FOR_RECONNECT) {
                                    if ((System.currentTimeMillis() - baseTime) > RECONNECT_TIMEOUT) {
                                        RLog.i("Reconnect timed out. Disconnect");
                                        release();
                                        if (sessionListener != null) {
                                            sessionListener.onSessionDisconnected();
                                        }
                                    } else {
                                        Thread.sleep(1000);
                                        RLog.d("Waiting for reconnect...");
                                    }
                                }

                                RLog.i("End waiting session status = " + sessionStatus);
                            } catch (InterruptedException e) {
                                RLog.w(e);
                            }
                        }
                    }).start();
                }
            } else {
                //세션채널도 끊어졌다면 종료 처리
                if (sessionStatus == SessionStatus.CONNECTED || sessionStatus == SessionStatus.DISCONNECTING || sessionStatus == SessionStatus.CONNECTING) {
                    sessionStatus = SessionStatus.DISCONNECTED;
                    RLog.i("Start session disconnecting... (1)");

                    if (sessionListener != null) {
                        sessionListener.onSessionDisconnected();
                    }
                }
            }
        } else if (channelNum == CHANNELNUM_SESSION) {
            //일반 종료 상황에 세션채널 연결종료가 나중에 들어올 수 있으므로
            if (((dataChannel == null) || (!dataChannel.isConnected())) ||
                    ((screenChannel == null) || (!screenChannel.isConnected()))) {
                //종료 상황.
                sessionStatus = SessionStatus.DISCONNECTED;

                RLog.i("Start session disconnecting... (2)");
                if (sessionListener != null) {
                    sessionListener.onSessionDisconnected();
                }
            } else {
                //세션채널 끊김.
                RLog.i("Session channel closed.");
                if (sessionChannel != null) {
                    sessionChannel.releaseAll();
                    sessionChannel = null;
                }
            }
        }
    }

    private CRCChannel.IChannelListener dataChannelListener = new CRCChannel.IChannelListener() {
        @Override
        public void onReconnected() {
        }

        @Override
        public void onReconnecting() {
        }

        @Override
        public void requestConnect(String guid, int port) {
            if (sessionStatus == SessionStatus.RECONNECTING_WITH_SESSIONCHANNEL) {
                sendChannelConnectRequest(sessionChannel, CHANNELNUM_DATA, guid, port);
            } else {
                requestAgentConnect(guid, port);
            }
        }

        @Override
        public void onConnected() {
            if (sessionStatus == SessionStatus.RECONNECTING_WITH_SESSIONCHANNEL) {
                if ((screenChannel != null) && (screenChannel.isConnected())) {
                    RLog.i("Screen Channel is alive.");
                    sessionStatus = SessionStatus.CONNECTED;
                    //connected.
                } else {
                    RLog.i("Data Channel Reconnected. Reconnect Screen Channel...");
                    connectScreenChannel(screenChannelListener);
                }
            } else {
                RLog.i("Data Channel Connected. Connect Screen Channel...");
                connectScreenChannel(screenChannelListener);
            }
        }

        @Override
        public void onException(Exception e) {
            RLog.w("DataChannel exception " + Log.getStackTraceString(e));

            if (dataChannel != null) {
                dataChannel.releaseAll();
                dataChannel = null;
            }

            if (e instanceof CRCChannel.ChannelCloseException) {
                //channelClose 메세지 수신됨. 세션채널 끊음.
                if (sessionChannel != null) {
                    sessionChannel.releaseAll();
                    sessionChannel = null;
                }
            }

            onChannelDisconnected(CHANNELNUM_DATA);
        }
    };

    private CRCVScreenChannel.IScreenChannelListener screenChannelListener = new CRCVScreenChannel.IScreenChannelListener() {
        @Override
        public void onReconnected() {
            if (sessionListener != null) {
                sessionListener.onSessionReconnected();
            }
            if (dataChannel != null) {
                dataChannel.sendPacket(channelConstants.rcpXENC264Stream, channelConstants.rcpXENC264StreamMakeKeyFrame);
            }
        }

        @Override
        public void onReconnecting() {
            if (sessionListener != null) {
                sessionListener.onSessionReconnecting();
            }
        }

        private boolean isPause = false;

        @Override
        public void requestConnect(String guid, int port) {
            sendChannelConnectRequest(dataChannel, CHANNELNUM_SCREEN, guid, port);
        }

        @Override
        public void onConnected() {
            RLog.i("Screen Channel Connected. Connect Session channel if needs...");
            if (sessionStatus == SessionStatus.RECONNECTING_WITH_SESSIONCHANNEL) {
                //이미 세션채널이 있음.
                sessionStatus = SessionStatus.CONNECTED;
                if (sessionListener != null) {
                    sessionListener.onSessionReconnected();
                }
            } else {
                sessionStatus = SessionStatus.CONNECTED;

                // rsnet 모드일경우에는 session 채널을 연결하지 않는다.
                if (!RuntimeData.getInstance().getAgentConnectOption().getViewerMode().equals(MODE_XENC)) {
                    if ((sessionChannel == null) || (!sessionChannel.isConnected())) {
                        if (sessionOptions.isUseSessionChannel()) {
                            connectSessionChannel(sessionChannelListener);
                        }
                    }
                }

                if (sessionListener != null) {
                    sessionListener.onSessionConnected();
                }
            }
            if (dataChannel != null
                    && RuntimeData.getInstance().getAgentConnectOption().getViewerMode().equals(MODE_XENC)) {
                dataChannel.reqMonitorInfo();
            }
        }

        @Override
        public void onException(Exception e) {
            RLog.w("ScreenChannel exception " + Log.getStackTraceString(e));

            if (screenChannel != null) {
                screenChannel.releaseAll();
                screenChannel = null;
            }

            onChannelDisconnected(CHANNELNUM_SCREEN);
        }

        @Override
        public boolean isScreenPause() {
            return isPause;
        }

        @Override
        public void setScreenPause(boolean isPause) {
            this.isPause = isPause;
        }

        @Override
        public void onChangeEngine(int engineType) {
            RuntimeData.getInstance().getAgentConnectOption().changeEngine(engineType);
//            Global.GetInstance().getPcViewer().changeCanvasEngine();
        }
    };

    private CRCVSessionChannel.ISessionChannelListener sessionChannelListener = new CRCVSessionChannel.ISessionChannelListener() {
        @Override
        public void onReconnected() {

        }

        @Override
        public void onReconnecting() {

        }

        @Override
        public void onReconnectRequested() {
            startSession(true);
        }

        @Override
        public void requestConnect(String guid, int port) {
            sendChannelConnectRequest(dataChannel, CHANNELNUM_SESSION, guid, port);
        }

        @Override
        public void onConnected() {
            RLog.i("Session channel connected.");
        }

        @Override
        public void onException(Exception e) {
            RLog.w("SessionChannel exception " + Log.getStackTraceString(e));

            if (sessionChannel != null) {
                sessionChannel.releaseAll();
                sessionChannel = null;
            }

            onChannelDisconnected(CHANNELNUM_SESSION);
        }
    };

    private CRCChannel.IChannelListener soundChannelListener = new CRCChannel.IChannelListener() {
        @Override
        public void onReconnected() {

        }

        @Override
        public void onReconnecting() {

        }

        @Override
        public void requestConnect(String guid, int port) {
            sendChannelConnectRequest(dataChannel, CHANNELNUM_SOUND, guid, port);
        }

        @Override
        public void onConnected() {

        }

        @Override
        public void onException(Exception e) {
            RLog.w("soundChannel : " + Log.getStackTraceString(e));
            if (soundChannel != null) {
                soundChannel.releaseAll();
                soundChannel = null;
            }
            GlobalStatic.SCREENSET_SHARE_SOUND = false;
            GlobalStatic.isShareSound = false;
        }
    };
}
