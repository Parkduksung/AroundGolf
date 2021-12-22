package com.rsupport.rv.viewer.sdk.decorder.viewer.channel;

import static com.rsupport.rv.viewer.sdk.constant.ConstantsKt.MODE_XENC;

import android.annotation.TargetApi;
import android.graphics.Point;
import android.hardware.Camera.CameraInfo;
import android.media.CamcorderProfile;
import android.os.Build;
import android.view.Display;

import androidx.annotation.NonNull;


import com.rsupport.rv.viewer.sdk.RemoteViewApp;
import com.rsupport.rv.viewer.sdk.agent.AgentCommon;
import com.rsupport.rv.viewer.sdk.common.RuntimeData;
import com.rsupport.rv.viewer.sdk.common.log.RLog;
import com.rsupport.rv.viewer.sdk.commons.rc45.IRC45Observer;
import com.rsupport.rv.viewer.sdk.commons.rc45.RC45Stream;
import com.rsupport.rv.viewer.sdk.constant.Bitrate;
import com.rsupport.rv.viewer.sdk.constant.ConstantsKt;
import com.rsupport.rv.viewer.sdk.data.AgentConnectOption;
import com.rsupport.rv.viewer.sdk.decorder.Converter;
import com.rsupport.rv.viewer.sdk.decorder.model.HXENGINEHEADER;
import com.rsupport.rv.viewer.sdk.decorder.model.SCAP_CLIENT_INFORMATION;
import com.rsupport.rv.viewer.sdk.decorder.model.SCAP_DESK_INFORMATION;
import com.rsupport.rv.viewer.sdk.decorder.model.SCAP_ENCODER_INFORMATION;
import com.rsupport.rv.viewer.sdk.decorder.model.X264Header;
import com.rsupport.rv.viewer.sdk.decorder.scap.ScapContants;
import com.rsupport.rv.viewer.sdk.decorder.scap.scapCursorMsg;
import com.rsupport.rv.viewer.sdk.decorder.scap.scapMsg;
import com.rsupport.rv.viewer.sdk.decorder.scap.scapOptEncMsg;
import com.rsupport.rv.viewer.sdk.decorder.scap.scapOption2Msg;
import com.rsupport.rv.viewer.sdk.decorder.scapDec.Decoder;
import com.rsupport.rv.viewer.sdk.decorder.scapDec.DecorderThread;
import com.rsupport.rv.viewer.sdk.decorder.viewer.RsViewer;
import com.rsupport.rv.viewer.sdk.decorder.viewer.channelConstants;
import com.rsupport.rv.viewer.sdk.extension.ContextExtKt;
import com.rsupport.rv.viewer.sdk.service.RemoteViewContext;
import com.rsupport.rv.viewer.sdk.session.SessionManager;
import com.rsupport.rv.viewer.sdk.setting.Global;
import com.rsupport.rv.viewer.sdk.setting.GlobalStatic;
import com.rsupport.rv.viewer.sdk.ui.IScreenController;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;


public class CRCVScreenChannel extends CRCChannel implements Runnable, IRC45Observer {
    public interface IScreenChannelListener extends IChannelListener {
        boolean isScreenPause();

        void setScreenPause(boolean isPause);

        void onChangeEngine(int engineType);
    }

    DecorderThread m_decoder;
    public Decoder decoder;

    SCAP_CLIENT_INFORMATION sci;
    RsViewer rs;

    private Thread screenChannelThread;
    private Timer packetCheckTimer;
    private boolean isScreenChannelLive;
    private long lastScreenReceiveTime;

    private IScreenChannelListener screenChannelListener = null;
//    private MonitorSwitchController.MonitorSwitchControlListener monitorSwitchControlListener = null;

    public CRCVScreenChannel(boolean useServerRec) {
        super(useServerRec);

        m_opts = new scapOption2Msg();
        decoder = new Decoder();

        RuntimeData.getInstance().currentSessionData.screenGuid = getGuid();
    }

    public void setListener(@NonNull final IScreenChannelListener listener) {
        screenChannelListener = listener;

        super.setListener(new IScreenChannelListener() {
            @Override
            public boolean isScreenPause() {
                return listener.isScreenPause();
            }

            @Override
            public void setScreenPause(boolean isPause) {
                listener.setScreenPause(isPause);
            }

            @Override
            public void onChangeEngine(int engineType) {
                listener.onChangeEngine(engineType);
            }

            @Override
            public void requestConnect(@Nullable String guid, int port) {
                listener.requestConnect(guid, port);
            }

            @Override
            public void onConnected() {
                listener.onConnected();
            }

            @Override
            public void onReconnecting() {
                listener.onReconnecting();
                Global.GetInstance().getScreenController().setRenderKeyFrame();
            }

            @Override
            public void onReconnected() {
                listener.onReconnected();
            }

            @Override
            public void onException(@Nullable Exception e) {
                listener.onException(e);
            }
        });
    }

//    public void setMonitorSwitchControlListener(MonitorSwitchController.MonitorSwitchControlListener monitorSwitchControlListener) {
//        this.monitorSwitchControlListener = monitorSwitchControlListener;
//    }

    public void startThread() {
        if (screenChannelThread != null) {
            setThreadRun(false);
            isScreenChannelLive = false;
            screenChannelThread.interrupt();
        }

        setThreadRun(true);
        isScreenChannelLive = true;
        screenChannelThread = new Thread(this);
        screenChannelThread.start();

        // 39초 타임아웃 제외
        startPacketReceiveCheck();
    }

    private boolean isNeedReStart = false;

    private void startReThread() {
        RLog.d("------------------------------ startReThread");

        if (screenChannelThread != null && screenChannelThread.isAlive()) {
            screenChannelThread.interrupt();
            RLog.d("Previous thread interrupted");
        }

        setThreadRun(true);
        isScreenChannelLive = true;
        screenChannelThread = new Thread(this);
        screenChannelThread.start();
    }

    /*
     * If the packet is not transmitted 75(+-5) seconds, this channel will be lost.
     * Proper check time is 60 seconds.
     */
    private void startPacketReceiveCheck() {
        packetCheckTimer = new Timer();

        if (!GlobalStatic.isPhonetoPhone() && !RuntimeData.getInstance().getAgentConnectOption().isHxEngine())
            checkPCPacket();
    }

    private void checkPCPacket() {
        packetCheckTimer.schedule(new TimerTask() {
            public void run() {
                if (screenChannelThread == null || screenChannelThread.isInterrupted()) {

                    stopPacketReceiveCheck();

                } else if (getNopInterval() < (System.currentTimeMillis() - lastScreenReceiveTime)) {

                    if (!sendNop(channelConstants.rcpNopConfirmNoAck)) {
                        getChannelListener().onException(new IllegalStateException("rcpNopConfirmNoAck fail"));
                        stopPacketReceiveCheck();
                    } else {
                        checkScreenReceiveTime();
                    }
                }
            }
        }, getNopInterval() + 1000, getNopInterval() / 2 + 1000);
    }

    private void __stopThread() {
        setThreadRun(false);
        isScreenChannelLive = false;
        if (screenChannelThread != null) {
            screenChannelThread.interrupt();
            screenChannelThread = null;
        }
    }

    private void stopPacketReceiveCheck() {
        if (packetCheckTimer != null) {
            packetCheckTimer.cancel();
            packetCheckTimer = null;
        }
    }

    private void checkScreenReceiveTime() {
        lastScreenReceiveTime = System.currentTimeMillis();
    }

    private scapOption2Msg m_opts;

    public void InitEngine() {
        if (!isThreadRun()) return;

        GetOption(m_opts);

        decoder.SetChannel(this);

        m_opts.type = (short) ScapContants.scapOption2;
        if (!writeExact(m_opts)) {
            RLog.i("Fail InitEngine");
            return;
        }

        decoder.Read(m_opts);

        if (sci == null) sci = new SCAP_CLIENT_INFORMATION();
        if (rs == null) rs = new RsViewer();

        rs.SetOptionHelper(sci, m_opts);
        sci.encoder.encClientChannel = this;
        RuntimeData.getInstance().getAgentConnectOption().applyFrom(m_opts);

        m_decoder = SCapDec_Create(sci);

    }

    public void GetOption(scapOption2Msg defOpts) {
        AgentConnectOption connectOption = RuntimeData.getInstance().getAgentConnectOption();

        defOpts.type = 0;
        defOpts.subtype = 0;
        defOpts.flags = 0;

        defOpts.hook.scapHookType = connectOption.getHookType();
        defOpts.flags |= SCAP_DESK_INFORMATION.dof_hookType;
        defOpts.hook.scapHookMonitor = 1;

        defOpts.encoder.flags = SCAP_ENCODER_INFORMATION.eof_cache;
        defOpts.encoder.scapEncoderType = scapOptEncMsg.scapEncodingZip;
        defOpts.encoder.scapTileCacheCount = 0;

        defOpts.encoder.flags |= SCAP_ENCODER_INFORMATION.eof_vbpp;
        defOpts.encoder.scapRemoteBpp = connectOption.getScapScreenColor().getViewerBpp();
    }

    public DecorderThread SCapDec_Create(SCAP_CLIENT_INFORMATION sci) {
        DecorderThread pThread = new DecorderThread();

        sci.encoder.flags |= SCAP_ENCODER_INFORMATION.eof_sock;
        if (!pThread.SetOption(sci)) {
            return null;
        }
        return pThread;
    }

    private boolean isXenc() {
        return RuntimeData.getInstance().getAgentConnectOption().getViewerMode().equals(MODE_XENC);
    }

    public void run() {
        isNeedReStart = false;
        GlobalStatic.sChannel_Traffic = 0;
        GlobalStatic.hxVideoFrameIndex = 0;

        if (isConnected()) {
            RLog.i("ScreenChannel already connected");
        } else {
            if (connectChannel(SessionManager.CHANNELNUM_SCREEN, RC45Stream.BUFFER_SIZE, this, fps -> {
                RLog.v("fps." + fps);
            })) {
                Global.GetInstance().setScreenChannel(this);

                keepSessionThread(true);

                if (getChannelListener() != null) {
                    getChannelListener().onConnected();
                }

                if(RuntimeData.getInstance().getAgentConnectOption().isHxEngine() && !isXenc()){
                    int defaultMonitorIndex = 1;
                    sendReqVideoInfo(defaultMonitorIndex);
                }
            } else {
                return;
            }
        }

        if (GlobalStatic.isPhonetoPhone()) {
            requestX264Header();
            procReadX264Video();
        } else {
            if (isXenc()) {
                readXencVideo();
            } else {
                if (RuntimeData.getInstance().getAgentConnectOption().isHxEngine()) {
                    procReadHXVideo();
                } else {
                    procReadJPP();
                }
            }
        }
    }

    private void procReadJPP() {
        InitEngine();
        scapMsg msg = new scapMsg();
        byte[] rcvBuf = new byte[2048];
        int nIndex = 0;
        short msgType;

        GlobalStatic.isEngineChanged = false;

        try {
            while (isScreenChannelLive && !screenChannelThread.isInterrupted()) {
                if(!decoder.Read(rcvBuf, 0, 4)) {
                    throw new IOException("ReadExact returns 0");
                }

                checkScreenReceiveTime();

                nIndex = 4;
                msgType = (short)(rcvBuf[0] & 0xff);

                switch(msgType) {
                    case ScapContants.scapDebug:
                        m_decoder.Decode(msg.Debug, rcvBuf, nIndex);
                        break;
                    case ScapContants.scapEnc:
//					RLog.d("--------------------------- scapEnc");
                        msg.EncInv.save(rcvBuf, 0);
                        int ret = m_decoder.Decode(msg.EncInv, rcvBuf, nIndex);
                        if (ret == 0) break;
                        break;
                    case ScapContants.scapSrnToSrn:
                        msg.SrnToSrn.save2(rcvBuf, 0, 0, 4);
                        m_decoder.Decode(msg.SrnToSrn, rcvBuf, nIndex);
                        break;
                    case ScapContants.scapTileCache:
                        msg.TileCache.save(rcvBuf, 0);
                        m_decoder.Decode(msg.TileCache, rcvBuf, nIndex);
                        break;
                    case ScapContants.scapCursorPos:
                        if(!decoder.Read(rcvBuf, nIndex, scapCursorMsg.sz_scapCursorPosMsg-4))
                            break;

                        msg.CursorPos.savePos(rcvBuf, 0);
                        msg.Cursor.ptCur.x = msg.CursorPos.ptCur.x;
                        msg.Cursor.ptCur.y = msg.CursorPos.ptCur.y;

                        m_decoder.Decode(msg.CursorPos);
                        break;
                    case ScapContants.scapCursorCached:
                        m_decoder.Decode(msg.Cursor, rcvBuf, 0);
                        break;
                    case ScapContants.scapCursorNew:
                        m_decoder.Decode(msg.Cursor, rcvBuf, 0);
                        break;
                    case ScapContants.scapOption2:
                        RLog.i("scapOption2");
                        if(!decoder.Read(rcvBuf, nIndex, m_opts.size()-4))
                            break;
                        System.gc();
                        m_opts.save(rcvBuf, 0);
                        rs.SetOptionHelper(sci, m_opts);
                        sci.encoder.encClientChannel = this;
                        if(!m_decoder.SetOption(sci)) {
                            System.err.println("failed to call m_decoder.SetOption");
                        }

                        RuntimeData.getInstance().getAgentConnectOption().applyFrom(m_opts);

                        byte[] bShortMonitorNumber = ByteBuffer.allocate(2).putShort(m_opts.hook.scapHookMonitor).array();

//                        monitorSwitchControlListener.onReceived(channelConstants.rcpMonitors, channelConstants.rcpMonitorsInfoRequest, bShortMonitorNumber);
//                        Global.GetInstance().getPcViewer().isSwitchWindow = false;
                        break;
                    case ScapContants.scapNotify:
                        if(!decoder.Read(rcvBuf, nIndex, msg.Notify.size()-4))
                            break;
                        msg.Notify.save(rcvBuf, 0);
                        break;
                    case ScapContants.scapColorMap:
                        msg.ColorMap.save(rcvBuf, 0);
                        RLog.d("msg.ColorMap.count : " + msg.ColorMap.count);
                        m_decoder.Decode(msg.ColorMap);
                        break;
                    case channelConstants.rcpChannelNop:
                        readNop();
                        break;
                    case channelConstants.rcpHXEngineData:
                        RLog.d("--------------------------  procReadJPP rcpHXEngineData");
                        if (!decoder.Read(rcvBuf, 4, 1)) throw new Exception();
                        RcpPacket packet = new RcpPacket();
                        packet.save(rcvBuf, 0);
                        readMsg(packet);
                        break;
                    default:
                        RLog.d("============= default procReadJPP msgType : " + msgType);
                        break;
                }
            }
            if (isNeedReStart) startReThread();
        } catch (Exception ex) {
            RLog.d("============= default procReadJPP exception : ");
            exceptionProc(ex);
        }
    }

    private void readXencVideo() {
        RcpPacket packet = new RcpPacket();

        GlobalStatic.G_CONNECTEDSESSION = true;

        try {
            while (isScreenChannelLive && !screenChannelThread.isInterrupted()) {
                if (!readExact(packet)) {
                    throw new IOException("ReadExact returns 0");
                }
                rcpMsg msg = new rcpMsg();
                readMsgPacket(packet, msg);
                if (packet.payload == channelConstants.rcpXENC264Stream) {
                    if (!screenChannelListener.isScreenPause() && msg.data != null) {
                        Global.GetInstance().getScreenController().render(msg.data, msg.datasize);
                    }
                }
                else {
                    RLog.w("not defined payload." + packet.payload);
                }
            }
            if (isNeedReStart) startReThread();
        } catch (Exception ex) {
            exceptionProc(ex);
        }
    }

    private void readMsgPacket(RcpPacket packet, rcpMsg msg) {
        if (packet.msgSize > 0) {
            byte[] buf = readExact(packet.msgSize);
            if (buf != null) {
                msg.save2(buf, 0, 0, packet.msgSize);
            }
        }
    }

    private void procReadHXVideo() {
        GlobalStatic.isEngineChanged = false;
        RcpPacket packet = new RcpPacket();

        try {
            while (isScreenChannelLive && !screenChannelThread.isInterrupted()) {
                if (!readExact(packet)) {
                    throw new IOException("ReadExact returns 0");
                }

                switch (packet.payload) {
                    case channelConstants.rcpChangeEngine:
                        readMsg(packet);
                        return;
                    case channelConstants.rcpHXEngineData:
                        readMsg(packet);
                        break;
                    case channelConstants.rcpChannelNop:
                        readNop();
                        break;
                    default:
                        break;
                }
            }
            if (isNeedReStart) startReThread();
        } catch (Exception ex) {
            exceptionProc(ex);
        }
    }

    private void requestX264Header() {
        rcpX264VideoHeaderMsg msg = new rcpX264VideoHeaderMsg();
        byte[] arrbModelName = Build.MODEL.getBytes(StandardCharsets.UTF_16LE);
        msg.modelname = arrbModelName;
        msg.modelnameLen = arrbModelName.length;
        Global.GetInstance().getDataChannel().sendPacket2(channelConstants.rcpX264Stream, channelConstants.rcpX264StreamStart, msg, msg.size());
    }

    private void procReadX264Video() {
        RcpPacket packet = new RcpPacket();
        try {
            while (isScreenChannelLive && !screenChannelThread.isInterrupted()) {
                if (!readExact(packet)) {
                    RLog.e("IOException");
                    throw new IOException("ReadExact returns 0");
                }

                checkScreenReceiveTime();

                switch (packet.payload) {
                    case channelConstants.rcpX264Stream:
                    case channelConstants.rcpXENC264Stream:
                        readMsg(packet);
                        break;
                    case channelConstants.rcpChannelNop:
                        readNop();
                        break;
                    default:
                        break;
                }
            }
            if (isNeedReStart) startReThread();
        } catch (Exception ex) {
            exceptionProc(ex);
        }
    }

    public void readX264HeaderMsg(byte[] data) {
        rcpX264VideoHeaderMsg msg = new rcpX264VideoHeaderMsg();
        msg.save(data, 0);

        X264Header header = new X264Header();
        header.setFrameWidth(msg.frameWidth);
        header.setFrameHeight(msg.frameHeight);
        header.setWidth(msg.width);
        header.setHeight(msg.height);
        header.setRotation(msg.rotation);
        header.setFramepersecond(msg.videoQuality);
        header.setMonitorIndex(msg.monitorIndex);
        header.setVideoRatio(msg.videoRatio);
        header.setVideoQuality(msg.videoQuality);
        header.setValOption1(msg.valOption1);
        header.setValOption2(msg.valOption2);
        header.setValOption3(msg.valOption3);
        header.setSourceType(msg.sourceType);
        header.setIsLandscape(msg.isLandscape);

//        if (PackageUtil.isAutoeverPackage()) {
//            header.setVideoQuality(20);        //고화질 설정
//        }

        // 실제 화면 크기
        RLog.d("------------------ size width " + msg.width);
        RLog.d("------------------ size height " + msg.height);
        RLog.d("------------------ size frameWidth " + msg.frameWidth);
        RLog.d("------------------ size frameHeight " + msg.frameHeight);
        RLog.d("------------------ rotation " + msg.rotation);
        RLog.d("------------------ rcpX264VideoHeaderMsg.ENCODER_TYPE_OMX : " + rcpX264VideoHeaderMsg.ENCODER_TYPE_OMX);
        RLog.d("------------------ rcpX264VideoHeaderMsg.ENCODER_TYPE_OMX_FOR_VD : " + rcpX264VideoHeaderMsg.ENCODER_TYPE_OMX_FOR_VD);
        RLog.d("------------------ sourceType " + msg.sourceType);
        RLog.d("------------------ isLandscape " + msg.isLandscape);
        RLog.d("------------------ videoQuality " + msg.videoQuality);
        RLog.d("------------------ vframepersecond " + msg.videoQuality);
        GlobalStatic.x264VideoHeader = header;
    }

    public void readMsg(RcpPacket packet) {
        rcpMsg msg = new rcpMsg();

        if (packet.msgSize > 0) {
            byte[] buf = readExact(packet.msgSize);
            if (buf != null) {
                msg.save2(buf, 0, 0, packet.msgSize);
                ReadChannelFunc(packet.payload, msg);
            }
        }
    }

    private void readProcEngineData(rcpMsg msg) {
        switch (msg.id) {
            case channelConstants.rcpAVChannelReady:
                RLog.d("------------------------------------- rcpAVChannelReady : " + Arrays.toString(msg.data));
                procVideoReady(msg.data);
                break;
            case channelConstants.rcpVideoDataHeader:
                GlobalStatic.hxVideoFrameIndex++;
                if (GlobalStatic.isLogHXEngineTraffic) {
                    GlobalStatic.sChannel_Traffic += msg.data.length;
                }
                break;
            case channelConstants.rcpVideoDataHeaderNData:
                int nIndex = 0;
                int iHeaderSize = Converter.readIntLittleEndian(msg.data, nIndex);
                nIndex += 4;
                int frameDataSize = Converter.readIntLittleEndian(msg.data, nIndex);

                byte[] hxVideoData = new byte[frameDataSize];
                System.arraycopy(msg.data, iHeaderSize, hxVideoData, 0, frameDataSize);

                IScreenController screenController = Global.GetInstance().getScreenController();
                if(screenController != null){
                    if (!screenChannelListener.isScreenPause() && msg.data != null) {
                        screenController.render(hxVideoData, hxVideoData.length);
                    }
                }
                break;
            case channelConstants.rcpVideoData:
                GlobalStatic.sChannel_Traffic += msg.data.length;
                Global.GetInstance().getScreenController().render(msg.data, msg.data.length);
                break;
            case channelConstants.rcpEngineChanged:
                int engineType = Converter.readIntLittleEndian(msg.data, 0);
                RLog.d("----------------------------- rcpEngineChanged : " + engineType);
                if(screenChannelListener != null){
                    screenChannelListener.onChangeEngine(engineType);
                }
                isScreenChannelLive = false;
                isNeedReStart = true;
                break;
            case channelConstants.rcpEngineOption:
                RLog.d("----------------------------- rcpEngineOption");
                procOption(msg.data);
                break;
            default:
                break;
        }
    }

    //rsnet
    private void readMobileDataXenc(rcpMsg msg) {
        switch (msg.id) {
            case channelConstants.rcpXENC264StreamHeader:
                readX264HeaderMsg(msg.data);

                if (!GlobalStatic.G_CONNECTEDSESSION) {
                    Global.GetInstance().getScreenController().setSourceSize(GlobalStatic.x264VideoHeader.getWidth(), GlobalStatic.x264VideoHeader.getHeight());
                } else if (rcpX264VideoHeaderMsg.ENCODER_TYPE_OMX_FOR_VD == GlobalStatic.x264VideoHeader.getSourceType()) {
                    Global.GetInstance().getScreenController().setSourceSize(GlobalStatic.x264VideoHeader.getWidth(), GlobalStatic.x264VideoHeader.getHeight());
                }

                GlobalStatic.G_CONNECTEDSESSION = true;

                break;
            case channelConstants.rcpXENC264StreamData:
                Global.GetInstance().getScreenController().render(msg.data, msg.datasize);
                break;
            default:
                break;
        }
    }

    //rc45
    private void readProcX264EngineData(rcpMsg msg) {
        switch (msg.id) {
            case channelConstants.rcpX264StreamHeader:
                readX264HeaderMsg(msg.data);

                if (!GlobalStatic.G_CONNECTEDSESSION) {
                    Global.GetInstance().getScreenController().setSourceSize(GlobalStatic.x264VideoHeader.getWidth(), GlobalStatic.x264VideoHeader.getHeight());
                } else if (rcpX264VideoHeaderMsg.ENCODER_TYPE_OMX_FOR_VD == GlobalStatic.x264VideoHeader.getSourceType()) {
                    Global.GetInstance().getScreenController().setSourceSize(GlobalStatic.x264VideoHeader.getWidth(), GlobalStatic.x264VideoHeader.getHeight());
                }

                GlobalStatic.G_CONNECTEDSESSION = true;

                break;
            case channelConstants.rcpX264StreamData:
            case channelConstants.rcpX264StreamSPPS:
                Global.GetInstance().getScreenController().render(msg.data, msg.datasize);
                break;
            default:
                break;
        }

    }

    private void readProcChangeEngine(rcpMsg msg) {
        switch (msg.id) {
            case channelConstants.rcpEngineScap:
                RuntimeData.getInstance().getAgentConnectOption().changeEngine(ConstantsKt.ENGINE_CAPTURE);
                this.startThread();
                break;
            case channelConstants.rcpEngineHX:
                RuntimeData.getInstance().getAgentConnectOption().changeEngine(ConstantsKt.ENGINE_HX);
                this.startThread();
                break;
        }
    }

    public void ReadChannelFunc(int payloadtype, rcpMsg msg) {
        if (GlobalStatic.isPhonetoPhone()) {
            if (channelConstants.rcpXENC264Stream == payloadtype) {
                readMobileDataXenc(msg);
            } else {
                readProcX264EngineData(msg);
            }
        } else {
            if (payloadtype == channelConstants.rcpHXEngineData) {
                readProcEngineData(msg);
            } else if (payloadtype == channelConstants.rcpChangeEngine) {
                readProcChangeEngine(msg);
            } else {
                readProcEngineData(msg);
            }
        }
    }

    public void procVideoReady(byte[] data) {
        saveVideoHeader(data);
        Global.GetInstance().getScreenController().setSourceSize(GlobalStatic.hxVideoHeader.videoWidth, GlobalStatic.hxVideoHeader.videoHeight);

        sendVideoStart();
    }

    public void procOption(byte[] data) {
        saveVideoHeader(data);
        Global.GetInstance().getScreenController().setSourceSize(GlobalStatic.hxVideoHeader.videoWidth, GlobalStatic.hxVideoHeader.videoHeight);

        Global.GetInstance().getScreenController().videoOptionChanged();

        sendVideoStart();
    }

    public void sendChangedEngine(int engineType) {
        byte[] bytes = new byte[4];
        System.arraycopy(Converter.getBytesFromIntLE(engineType), 0, bytes, 0, 4);
        Global.GetInstance().getDataChannel().sendPacket(channelConstants.rcpHXEngineData, channelConstants.rcpEngineChange, bytes, bytes.length);
        GlobalStatic.isEngineChanged = true;
    }

    public void sendVideoQuality(int quality) {
        byte[] bytes = new byte[4];
        System.arraycopy(Converter.getBytesFromIntLE(quality), 0, bytes, 0, 4);
        Global.GetInstance().getDataChannel().sendPacket(channelConstants.rcpHXEngineData, channelConstants.rcpHXQualityChange, bytes, bytes.length);

        RuntimeData.getInstance().getAgentConnectOption().setHxBitrate(Bitrate.fromPacketValue(quality));
    }

    public void saveVideoHeader(byte[] data) {
        HXENGINEHEADER videoHeader = new HXENGINEHEADER();
        RLog.d("------- data : " + Arrays.toString(data));
        videoHeader.save(data, 0);

        RLog.d("-------------------- saveVideoHeader : videoWidth : " + videoHeader.videoWidth);
        RLog.d("-------------------- saveVideoHeader : videoHeight : " + videoHeader.videoHeight);
        RLog.d("-------------------- saveVideoHeader : videoSize : " + videoHeader.videoSize);
        RLog.d("-------------------- saveVideoHeader : framepersecond : " + videoHeader.framepersecond);
        RLog.d("-------------------- saveVideoHeader : videoQuality : " + videoHeader.videoQuality);
        RLog.d("-------------------- saveVideoHeader : audioQUality : " + videoHeader.audioQUality);
        RLog.d("-------------------- saveVideoHeader : audioChannel : " + videoHeader.audioChannel);
        RLog.d("-------------------- saveVideoHeader : samplingRate : " + videoHeader.samplingRate);
        RLog.d("-------------------- saveVideoHeader : monitorIndex : " + videoHeader.monitorIndex);

        GlobalStatic.hxVideoHeader = videoHeader;

        byte[] bIntMonitorNumber = ByteBuffer.allocate(4).putInt(videoHeader.monitorIndex).array();

        RLog.v("monitorSelected ScreenChannel." + videoHeader.monitorIndex);
//        monitorSwitchControlListener.onReceived(channelConstants.rcpMonitors, channelConstants.rcpMonitorSelect, bIntMonitorNumber);
    }

    public void waitReqHXVideoSize() {
        int loop = 0;
        while (RemoteViewContext.getReqHXVideoHeight() <= 0) {
            try {
                Thread.sleep(100);
                if (loop > 150) break;
                loop++;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendReqVideoInfo(int monitorNumber) {
        RLog.d("------------------------------------- sendReqVideoInfo : ");
        waitReqHXVideoSize();
        HXENGINEHEADER videoHeader = new HXENGINEHEADER();
        CamcorderProfile profile = getCamcorderProfile();

        // 단말기가 지원하는 최대 해상도 (최대 1920 * 1080)
        int maxVideoSize = profile.videoFrameWidth * profile.videoFrameHeight;
        Display display = ContextExtKt.getDefaultDisplay(Objects.requireNonNull(RemoteViewApp.INSTANCE.getContext()));
        Point size = getRealSize(display);

        // 단말기 해상도
        int maxDisplaySize = size.x * size.y;
        // 원격지 모니터 해상도
        int hostVideoSize = RemoteViewContext.getReqHXVideoWidth() * RemoteViewContext.getReqHXVideoHeight();

        AgentCommon.setOriginHostResolution(RemoteViewContext.getReqHXVideoWidth(), RemoteViewContext.getReqHXVideoHeight(), RemoteViewContext.getReqHXVideoColor());

        int videoSize = 100;

        // 단말기가 지원하는 해상도보다 단말기 해상도가 크다면
        if (maxVideoSize < maxDisplaySize) {
            videoHeader.videoWidth = RemoteViewContext.getReqHXVideoWidth();
            videoHeader.videoHeight = RemoteViewContext.getReqHXVideoHeight();
        } else {
            if (maxDisplaySize < 1280 * 720) {
                videoSize = (1280 * 720) / (hostVideoSize / 100);
                videoHeader.videoWidth = 1280;
                videoHeader.videoHeight = 960;
            } else {
                videoSize = maxDisplaySize / (hostVideoSize / 100);

                videoHeader.videoWidth = RemoteViewContext.getReqHXVideoWidth();
                videoHeader.videoHeight = RemoteViewContext.getReqHXVideoHeight();
            }
        }
        videoHeader.videoSize = videoSize;

        if (videoHeader.videoSize < 70)
            videoHeader.videoSize = 70;

        videoHeader.videoQuality = RuntimeData.getInstance().getAgentConnectOption().getHxBitrate().asPacketValue();
        videoHeader.monitorIndex = monitorNumber;

        RLog.d("------------------------------------- sendReqVideoInfo : size.x : " + size.x);
        RLog.d("------------------------------------- sendReqVideoInfo : size.y : " + size.y);

        RLog.d("------------------------------------- sendReqVideoInfo : profile.videoFrameWidth : " + profile.videoFrameWidth);
        RLog.d("------------------------------------- sendReqVideoInfo : profile.videoFrameHeight : " + profile.videoFrameHeight);

        RLog.d("------------------------------------- sendReqVideoInfo : videoHeader.videoWidth : " + videoHeader.videoWidth);
        RLog.d("------------------------------------- sendReqVideoInfo : videoHeader.videoHeight : " + videoHeader.videoHeight);

        RLog.d("------------------------------------- sendReqVideoInfo : videoHeader.videoSize : " + videoHeader.videoSize);
        RLog.d("------------------------------------- sendReqVideoInfo : videoHeader.videoQuality : " + videoHeader.videoQuality);

        videoHeader.print();

        sendPacket2(channelConstants.rcpHXEngineData, channelConstants.rcpEngineHeader, videoHeader, videoHeader.size());
    }

    public void sendVideoStart() {
        RLog.i("Send VideoStart...");
        sendPacket(channelConstants.rcpHXEngineData, channelConstants.rcpVideoStart);
        GlobalStatic.G_CONNECTEDSESSION = true;
    }

    public void sendVideoStop() {
        sendPacket(channelConstants.rcpHXEngineData, channelConstants.rcpVideoStop);
    }

    private void exceptionProc(Exception e) {
        if (getChannelListener() != null) {
            getChannelListener().onException(e);
        }
    }

    @Override
    public boolean releaseAll() {
        __stopThread();

        if (m_decoder != null) {
            m_decoder.setFreeCanvasHandler();
            m_decoder = null;
        }
        if (decoder != null) decoder = null;

        return super.releaseAll();
    }

    public void readChannelDataFunc(int payload, @NotNull rcpMsg msg) {
    }

    @Override
    public void onConnecting() {
        if (getChannelListener() != null) {
            getChannelListener().requestConnect(getConnectGuid(), getStreamListenPort());
        }
    }

    @Override
    public void onConnected() {
    }

    public CamcorderProfile getCamcorderProfile() {

        CamcorderProfile profile;

        profile = CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH);

        if (profile == null) {
            profile = CamcorderProfile.get(CameraInfo.CAMERA_FACING_BACK,
                    CamcorderProfile.QUALITY_HIGH);
        }

        return profile;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public Point getRealSize(Display display) {

        Point size = new Point();

        try {
            display.getRealSize(size);
        } catch (NoSuchMethodError e) {
            display.getSize(size);
        }

        return size;
    }
}
