package com.rsupport.rv.viewer.sdk.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Base64
import com.rsupport.android.push.IPushMessaging
import com.rsupport.rscommon.exception.RSException
import com.rsupport.rv.viewer.sdk.common.CanvasProxy
import com.rsupport.rv.viewer.sdk.common.ComConstant
import com.rsupport.rv.viewer.sdk.common.RuntimeData
import com.rsupport.rv.viewer.sdk.common.log.RLog
import com.rsupport.rv.viewer.sdk.decorder.Converter
import com.rsupport.rv.viewer.sdk.decorder.viewer.channel.CRCSoundChannel
import com.rsupport.rv.viewer.sdk.decorder.viewer.channelConstants
import com.rsupport.rv.viewer.sdk.session.SessionManager
import com.rsupport.rv.viewer.sdk.session.SessionOptions
import com.rsupport.rv.viewer.sdk.setting.Global
import com.rsupport.rv.viewer.sdk.setting.GlobalStatic
import com.rsupport.rv.viewer.sdk.setting.isXenc
import com.rsupport.rv.viewer.sdk.util.AgentInfo
import kotlinx.coroutines.*
import kotlin.experimental.inv
import kotlin.experimental.xor

class RemoteSession(private val context: Context) {
    private val agentInfo: AgentInfo? = RuntimeData.getInstance().lastAgentInfo
    private val startSessionJob = SupervisorJob()

    private var sessionManager: SessionManager? = null
    private val sessionListenerSet = mutableSetOf<OnSessionListener>()

    private var remoteSessionTimer: RemoteSessionTimer? = null
    private var sessionStatus: Status = Status.Idle

    private var oldResolutionWidth: Int = 0
    private var oldResolutionHeight: Int = 0

    private var pushConnectReceiver: PushConnectReceiver? = null

    fun registerSessionListener(sessionListener: OnSessionListener) {
        sessionListenerSet.add(sessionListener)
    }

    fun unRegisterSessionListener(sessionListener: OnSessionListener) {
        sessionListenerSet.remove(sessionListener)
    }

    fun registerScreenLockListener(screenLockListener: OnScreenLockListener) {
    }

    fun unRegisterScreenLockListener(screenLockListener: OnScreenLockListener) {
    }

    fun getStatus(): Status {
        return sessionStatus
    }

    fun connect(sessionOptions: SessionOptions) {
        onSessionListener.onChangeStatus(Status.Connecting)

        if(sessionOptions.isUseMqttSession){
            initialize()

            sessionManager = SessionManager.create(
                    sessionOptions,
                    SessionListenerAdapter(context, onSessionListener)
            ).apply {
                setDataPacketListener(DataChannelListenerImpl())
                registerReceiver(this@RemoteSession,  this, onSessionListener)
                startMqttPush()
            }
        }else {
            CoroutineScope(Dispatchers.IO + startSessionJob).launch {
                initialize()

                sessionManager = SessionManager.create(
                        sessionOptions,
                        SessionListenerAdapter(context, onSessionListener)
                ).apply {
                    setDataPacketListener(DataChannelListenerImpl())
                    startSession()
                }
            }
        }
    }

    fun unlockScreen() {
    }

    fun isRemoteMacPC(): Boolean {
        return agentInfo?.pcType?.contains("MacOS") ?: false
    }

    private fun initialize() {
        RLog.i("initialize")
        GlobalStatic.G_ISCONNECTINGSESSION = true
        GlobalStatic.ORIGIN_RESOLUTION_X = 0
        GlobalStatic.ORIGIN_RESOLUTION_Y = 0
    }

    private fun initSessionTimer() {
        val opt = RuntimeData.getInstance().agentConnectOption
        if (opt.isUseViewerTimeout) {
            remoteSessionTimer?.stop()
            remoteSessionTimer = RemoteSessionTimer(
                    opt.viewerTimeoutSeconds,
                    opt.viewerTimeoutMessageBeforeSeconds,
                    expiredRunnable = {
                        release()
                    },
                    expiredMessageRunnable = {
                        onSessionListener.onChangeStatus(Status.ConnectRemainTime(opt.viewerTimeoutMessageBeforeSeconds))
                    }
            ).apply { start() }
        }
    }


    fun release() {
        RLog.i("release")
        onSessionListener.onChangeStatus(Status.Disconnecting)

        unRegisterConnectReceiver()

        CoroutineScope(Dispatchers.IO).launch{
            Global.GetInstance().screenChannel?.sendPacket(channelConstants.rcpChannel, channelConstants.rcpChannelClose)
            Global.GetInstance().dataChannel?.sendPacket(channelConstants.rcpChannel, channelConstants.rcpChannelClose)


            remoteSessionTimer?.stop()
            remoteSessionTimer = null

            sessionManager?.release()
            sessionManager = null

            GlobalStatic.G_ISCONNECTINGSESSION = false
            GlobalStatic.G_CONNECTEDSESSION = false
            GlobalStatic.SCREENSET_SHARE_SOUND = false
        }
    }

    fun closeSoundChannel() {
        sessionManager?.closeSoundChannel()
    }

    fun openSoundChannel(soundChannelProgressListener: CRCSoundChannel.SoundChannelProgressListener) {
        sessionManager?.openSoundChannel(soundChannelProgressListener)
    }

    fun pauseScreen() {
        if (isConnected()) {
            sessionManager?.setScreenPause()
        }
    }

    fun resumeScreen() {
        if (isConnected()) {
            sessionManager?.setScreenResume()
        }
    }

//    fun getScreenLockStatus(): LockStatus {
//        return remoteScreenLockTimer.getStatus()
//    }

    private fun isConnected(): Boolean {
        return getStatus() == Status.Connected
    }

    fun updateUserActionTime() {
//        remoteScreenLockTimer.initLockTime()
    }

    fun sendKeyEvent(keyCode: Int, isPress: Boolean, specialKey: Int) {
        sessionManager?.sendKeyEvent(keyCode, isPress, specialKey)
    }

    private fun requestCommand(lastAgentGuid: String, requestCommand: String, requestData: String, errorMsg: Array<String?>): Boolean {
        val responseData = arrayOfNulls<String>(1)
        return try {
//            WebConnection.getInstance().callViewerConnectToAgentPage(
//                    lastAgentGuid,
//                    Global.GetInstance().webAccessInfo.id,
//                    requestCommand,
//                    requestData,
//                    GlobalStatic.g_deviceIP,
//                    Global.GetInstance().webAccessInfo.logkey,
//                    RuntimeData.getInstance().appProperty.userKey,
//                    "0",
//                    null,
//                    responseData,
//                    errorMsg)
            true
        } catch (e: RSException) {
            GlobalStatic.g_err = "cannot connect to agent"
//            if (e is WebErrorException) {
//                val resp = e.response
//                GlobalStatic.CMD_PROC_ERR = resp.retCode
//                GlobalStatic.g_errNumber = StringUtil.parseInt(resp.result, 0, 10)
//            }
            false
        }
    }

    private fun recoverResolution() {
        if (RuntimeData.getInstance().agentConnectOption.isHxEngine) {
            if(!isXenc()){
                RLog.d("recoverResolution : " + oldResolutionWidth + " / " + RemoteViewContext.reqHXVideoWidth)
                if (GlobalStatic.ORIGIN_SCREEN_X.toInt() != RemoteViewContext.reqHXVideoWidth) {
                    CanvasProxy.getInstance().sendResolutionPacket(oldResolutionWidth, oldResolutionHeight,
                        RemoteViewContext.reqHXVideoColor
                    )
                }
            }
        }else {
            RLog.d("recoverResolution : " + oldResolutionWidth + " / " + GlobalStatic.ORIGIN_SCREEN_X)

            if (GlobalStatic.ORIGIN_SCREEN_X.toInt() != oldResolutionWidth) {
                CanvasProxy.getInstance().sendResolutionPacket(oldResolutionWidth, oldResolutionHeight, GlobalStatic.ORIGIN_RESOLUTION_BIT.toInt())
            }
        }
    }

    private fun startMqttPush() {
        GlobalStatic.registerPushMessaging(context, RuntimeData.getInstance().lastAgentInfo.guid)
    }

    private fun registerReceiver(remoteSession: RemoteSession, sessionManager: SessionManager, sessionListener: OnSessionListener) {
        pushConnectReceiver = PushConnectReceiver(remoteSession, sessionManager, sessionListener)
        val connectIntentFilter = IntentFilter()
        connectIntentFilter.addAction(IPushMessaging.ACTION_PUSH_MESSAGING)
        connectIntentFilter.addCategory("com.server.mqtt")
        context.registerReceiver(pushConnectReceiver, connectIntentFilter)
    }

    private fun unRegisterConnectReceiver() {
        if (pushConnectReceiver != null) {
            context.unregisterReceiver(pushConnectReceiver)
            pushConnectReceiver = null
        }
    }

    private val onSessionListener: OnSessionListener = object : OnSessionListener {
        override fun onChangeStatus(status: Status) {
            sessionStatus = status
            when(status){
                Status.Connected -> onConnected()
                Status.Disconnecting -> onDisconnecting()
            }
            sessionListenerSet.forEach { it.onChangeStatus(status) }
        }

        private fun onDisconnecting() {
            recoverResolution()
        }

        private fun onConnected() {
            if (RuntimeData.getInstance().agentConnectOption.isHxEngine) {
                oldResolutionWidth = RemoteViewContext.reqHXVideoWidth
                oldResolutionHeight = RemoteViewContext.reqHXVideoHeight
            } else {
                oldResolutionWidth = GlobalStatic.ORIGIN_RESOLUTION_X.toInt()
                oldResolutionHeight = GlobalStatic.ORIGIN_RESOLUTION_Y.toInt()
            }

            initSessionTimer()
//            remoteScreenLockTimer.start()

            Global.GetInstance().screenConnectInfo.duration = System.currentTimeMillis()
            Global.GetInstance().screenConnectInfo.pcname = agentInfo?.name
            Global.GetInstance().screenConnectInfo.pcos = agentInfo?.osname
            Global.GetInstance().screenConnectInfo.ipAddress = agentInfo?.localip
        }
    }


    class PushConnectReceiver(private val remoteSession: RemoteSession, private val sessionManager: SessionManager, private val sessionListener: OnSessionListener) : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == IPushMessaging.ACTION_PUSH_MESSAGING) {
                when (intent?.getIntExtra(IPushMessaging.EXTRA_KEY_TYPE, -1)) {
                    IPushMessaging.TYPE_CONNECTED -> {
                        sessionManager.startSession()
                    }
                    IPushMessaging.TYPE_MSG_ARRIVED -> {
                        intent.getByteArrayExtra(IPushMessaging.EXTRA_KEY_VALUE)?.let { message ->


                            dec_bitcrosswise(message, 0)

                            var index = 0
                            val sessionPacket = Converter.readIntLittleEndian(message, index)
                            index += 4
                            var errorCode = Converter.readIntLittleEndian(message, index)
                            index += 4
                            val baseLength = Converter.readIntLittleEndian(message, index)
                            index += 4

                            val datas = ByteArray(baseLength)
                            System.arraycopy(message, index, datas, 0, baseLength)

                            val decodedMessage = Base64.decode(datas, Base64.DEFAULT)

                            index = 0

                            if (decodedMessage.size <= 3) {
                                RLog.d("message size 0")
                                return
                            }

                            val commandKey = Converter.readIntLittleEndian(decodedMessage, index)
                            index += 4
                            val encordingDataLength = Converter.readIntLittleEndian(decodedMessage, index)
                            index += 4

                            RLog.d("commandKey : $commandKey")

                            if (commandKey == ComConstant.VIEWER_REMOTE_CONTROL_REQUEST.toInt()) {
                                RLog.d("commandKey 1")
                                if (Global.GetInstance() == null) return
                                RLog.d( "commandKey errorCode : $errorCode")

                                if (errorCode == 0) { // 성공
                                    //연결 성공하면 푸시 리시버 등록 해제
                                    GlobalStatic.unregisterPushMessaging(context, RuntimeData.getInstance().lastAgentInfo.guid)
                                    /**
                                     * datachannel의 isValidWebInterfacing은 CRCVDataChannel클래스의 onConnected() 에서 true로 전환하며,
                                     * onConnected()는 RC45 세션 연결 성공시 호출되는 리스너 메소드이다.
                                     *
                                     * 이 푸시가 들어오는 때는 RC45 세션 연결 요청 후 연결이 되기를 기다리고 있는 중이며,
                                     * 연결이 성공되고 나서야 Global.GetInstance().dataChannel이 설정되므로						 *
                                     * 푸시가 들어오는 시점에서 Global.GetInstance().dataChannel == null이므로
                                     * setIsValidWebInterfacing(true)는 실행되지 않는 코드이다.
                                     */
                                    if (Global.GetInstance().dataChannel == null) return
                                    Global.GetInstance().dataChannel.setIsValidWebInterfacing(true)
                                } else {
                                    GlobalStatic.CMD_PROC_ERR = errorCode
                                    if (errorCode == ComConstant.CMD_ERR_ALREADY_REMOTE_CONTROL) {
                                        sessionListener.onChangeStatus(Status.ForceConnectAsking(
                                            accept = {
                                                RLog.i("force connect accept")
                                                CoroutineScope(Dispatchers.IO).launch {
//                                                    sessionManager.connectToAgentPage(
//                                                        true,
//                                                        RuntimeData.getInstance().currentSessionData.dataGuid
//                                                    )
                                                }
                                            },
                                            denied = {
                                                RLog.i("force connect denied")
                                                remoteSession.release()
                                            }
                                        ))
                                    } else {
                                        sessionListener.onChangeStatus(
                                            Status.Error(
                                                RSException(
                                                    errorCode
                                                )
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        private fun dec_bitcrosswise(p: ByteArray, offset: Int) {
            val c = 'r'.toByte()
            for (i in offset until p.size) {
                p[i] = p[i].inv()
                p[i] = (p[i] xor c)
            }
        }

    }


    sealed class LockStatus {
        object ScreenUnLock : LockStatus()
        object ScreenLock : LockStatus()
        object Disconnect : LockStatus()
        data class WillScreenLock(val timeSecond: Long) : LockStatus()
        data class WillShutdown(val timeSecond: Long) : LockStatus()
        object LockFailNoAuth: LockStatus()
        object UsedNotLockAuth: LockStatus()
    }

    sealed class Status {
        object Idle : Status()
        object Connecting : Status()
        object Connected : Status()

        // 세션 복원중....
        object SessionRecovering : Status()

        // 세션 복원 완료
        object SessionReconnected : Status()
        object Disconnecting : Status()
        object Disconnected : Status()

        data class ForceConnectAsking(val accept: () -> Unit, val denied: () -> Unit) : Status()

        data class Error(val exception: RSException) : Status()

        data class ConnectRemainTime(val timeSecond: Long) : Status()
    }

    interface OnSessionListener {
        fun onChangeStatus(status: Status)
    }

    interface OnScreenLockListener {
        fun onScreenLockEvent(lockStatus: LockStatus)
    }
}