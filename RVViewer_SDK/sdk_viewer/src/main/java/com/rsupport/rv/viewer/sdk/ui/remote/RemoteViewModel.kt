package com.rsupport.rv.viewer.sdk.ui.remote

import android.app.Application
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.Bitmap
import android.os.IBinder
import androidx.lifecycle.*

import com.rsupport.rscommon.define.RSErrorCode
import com.rsupport.rscommon.exception.RSException
import com.rsupport.rv.viewer.sdk.common.ComConstant
import com.rsupport.rv.viewer.sdk.common.RuntimeData
import com.rsupport.rv.viewer.sdk.common.log.RLog
import com.rsupport.rv.viewer.sdk.decorder.viewer.channel.CRCSoundChannel
import com.rsupport.rv.viewer.sdk.service.RemoteSession
import com.rsupport.rv.viewer.sdk.service.RemoteViewServiceImpl
import com.rsupport.rv.viewer.sdk.service.RemoteViewServiceLocalBinder
import com.rsupport.rv.viewer.sdk.session.SessionOptions
import com.rsupport.rv.viewer.sdk.setting.Global
import com.rsupport.rv.viewer.sdk.util.AgentInfo
import com.rsupport.rv.viewer.sdk.util.SingleLiveEvent

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

import java.io.ByteArrayOutputStream

class RemoteViewModel(private val app: Application) : AndroidViewModel(app), LifecycleObserver {

    private val _remoteStatusLiveData = MutableLiveData<RemoteStatus>()
    val remoteStatusLiveData: LiveData<RemoteStatus> = _remoteStatusLiveData.distinctUntilChanged()

    private val _canvasStatusLiveData = MutableLiveData<CanvasStatus>()
    val canvasStatusLiveData: LiveData<CanvasStatus> = _canvasStatusLiveData.distinctUntilChanged()

    private val _reconnectStatusLiveData = MutableLiveData<ReconnectStatus>()
    val reconnectStatusLiveData: LiveData<ReconnectStatus> =
        _reconnectStatusLiveData.distinctUntilChanged()

    private val _isScreenLockLiveData = MutableLiveData<Boolean>(false)
    val isScreenLockLiveData: LiveData<Boolean> = _isScreenLockLiveData.distinctUntilChanged()

    private val _viewSingleEvent = SingleLiveEvent<RemoteViewSingleEvent>()
    val viewSingleEvent = _viewSingleEvent.distinctUntilChanged()

    private val agentInfo: AgentInfo? = RuntimeData.getInstance().lastAgentInfo

    private var remoteSession: RemoteSession? = null

    private var isDisconnectCalled = false
    private var isConnectable = false

    override fun onCleared() {
        disconnect()
        super.onCleared()
        RLog.i("onCleared")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStopScreen() {
        RLog.i("onPauseScreen")
        remoteSession?.pauseScreen()
        remoteSession?.unRegisterSessionListener(sessionListener)
        remoteSession?.unRegisterScreenLockListener(screenLockListener)
        app.unbindService(remoteViewServiceConnection)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStartScreen() {
        RLog.i("onResumeScreen")
        app.bindService(
            Intent(app, RemoteViewServiceImpl::class.java),
            remoteViewServiceConnection,
            Context.BIND_AUTO_CREATE
        )
    }

    fun openSoundChannel(soundChannelProgressListener: CRCSoundChannel.SoundChannelProgressListener) {
        remoteSession?.openSoundChannel(soundChannelProgressListener)
    }

    fun closeSoundChannel() {
        remoteSession?.closeSoundChannel()
    }

    fun disconnect() {
        isDisconnectCalled = true
        remoteSession?.release()
        remoteSession = null
    }

    fun setConnectable() {
        isConnectable = true
    }

    private fun isMacAgent(): Boolean {
        try {
            val extendConst: Int = agentInfo?.extend?.trim()?.toIntOrNull()
                ?: ComConstant.RVFLAG_KEY_NONE.toInt()
            if (extendConst == ComConstant.RVFLAG_KEY_MAC.toInt()) {
                return true
            }
        } catch (e: NumberFormatException) {
            return false
        }
        return false
    }

    private val remoteViewServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val remoteViewService = (service as RemoteViewServiceLocalBinder).getRemoteViewService()
            remoteSession = remoteViewService.getRemoteSession().apply {
                registerSessionListener(sessionListener)
                registerScreenLockListener(screenLockListener)

                val remoteStatus = getStatus()
                RLog.i("remoteStatus.$remoteStatus")
                when (remoteStatus) {
                    RemoteSession.Status.Idle -> {
                        if (isConnectable) {
                            connect(
                                SessionOptions(
                                    isUseServerRec = true,
                                    isUseSessionChannel = !isMacAgent()
                                )
                            )
                            isConnectable = false
                        } else {
                            _remoteStatusLiveData.value = RemoteStatus.Disconnected
                        }
                    }
                    RemoteSession.Status.Connected -> {
                        _canvasStatusLiveData.value = CanvasStatus.UpdateCanvas
                        resumeScreen()
                    }
                    RemoteSession.Status.Disconnected -> {
                        if (isConnectable) {
                            connect(
                                SessionOptions(
                                    isUseServerRec = true,
                                    isUseSessionChannel = !isMacAgent()
                                )
                            )
                            isConnectable = false
                        } else {
                            _remoteStatusLiveData.value = RemoteStatus.Disconnected
                        }
                    }

                    RemoteSession.Status.Disconnecting -> {
                        if (isConnectable) {
                            connect(
                                SessionOptions(
                                    isUseServerRec = true,
                                    isUseSessionChannel = !isMacAgent()
                                )
                            )
                            isConnectable = false
                        } else {
                            _remoteStatusLiveData.value = RemoteStatus.Disconnecting
                        }
                    }
                }

//                val screenLockStatus = getScreenLockStatus()
//                _isScreenLockLiveData.value =
//                    screenLockStatus is RemoteSession.LockStatus.ScreenLock
            }
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            remoteSession = null
        }
    }

    private val screenLockListener = object : RemoteSession.OnScreenLockListener {
        override fun onScreenLockEvent(lockStatus: RemoteSession.LockStatus) {
            viewModelScope.launch {
                when (lockStatus) {
                    RemoteSession.LockStatus.ScreenLock -> {
                        _isScreenLockLiveData.value = true
                    }
                    else -> {
                        _isScreenLockLiveData.value = false
                    }
                }
            }
        }
    }

    private val sessionListener = object : RemoteSession.OnSessionListener {
        override fun onChangeStatus(status: RemoteSession.Status) {
            // 재접속 중에는 원격제어 상태를 변경하지 않는다.


            viewModelScope.launch {
                when (status) {
                    RemoteSession.Status.Connecting -> {
                        _remoteStatusLiveData.value = RemoteStatus.Connecting
                    }
                    RemoteSession.Status.Connected -> {
                        _remoteStatusLiveData.value = RemoteStatus.Connected
                    }
                    RemoteSession.Status.SessionRecovering -> {
                        _remoteStatusLiveData.value = RemoteStatus.Recovering
                    }
                    RemoteSession.Status.SessionReconnected -> {
                        _remoteStatusLiveData.value = RemoteStatus.Recovered
                    }
                    RemoteSession.Status.Disconnected -> {
                        _remoteStatusLiveData.value = RemoteStatus.Disconnected
                    }
                    is RemoteSession.Status.ForceConnectAsking -> {
                        _remoteStatusLiveData.value =
                            RemoteStatus.ForceConnectAsking(status.accept, status.denied)
                    }
                    is RemoteSession.Status.Error -> {
                        _remoteStatusLiveData.value = RemoteStatus.Error(status.exception)
                    }
                    RemoteSession.Status.Disconnecting -> {
                        _remoteStatusLiveData.value = RemoteStatus.Disconnecting
                    }
                }
            }
        }
    }

//    private suspend fun reconnectProcess() = withContext(Dispatchers.Main) {
//        if (reconnectThread != null) {
//            RLog.w("already reconnectProcess")
//            return@withContext
//        }
//        RLog.v("reconnectProcess")
//
//        reconnectThread = ReconnectThread(app, remoteSession!!, agentInfo!!, agentInfo!!.guid, isMacAgent(), object : ReconnectListener {
//            override fun onPrepare() {
//                viewModelScope.launch {
//                    _reconnectStatusLiveData.value = ReconnectStatus.Prepare
//                }
//            }
//
//            override fun onCountDown(count: Int) {
//                viewModelScope.launch {
//                    _reconnectStatusLiveData.value = ReconnectStatus.CountDown(count)
//                }
//            }
//
//            override fun onReconnecting() {
//                viewModelScope.launch {
//                    _reconnectStatusLiveData.value = ReconnectStatus.Reconnecting
//                }
//            }
//
//            override fun onError(errorMsg: String?) {
//                viewModelScope.launch {
//                    _reconnectStatusLiveData.value = ReconnectStatus.Error(
//                        errorMsg
//                            ?: "Not Define error"
//                    )
//                }
//            }
//
//            override fun onFinish() {
//                viewModelScope.launch {
//                    _reconnectStatusLiveData.value = ReconnectStatus.Finish
//                }
//                reconnectThread = null
//            }
//
//            override fun onReconnected() {
//                viewModelScope.launch {
//                    _reconnectStatusLiveData.value = ReconnectStatus.Reconnected
//                }
//            }
//
//        })
//        reconnectThread?.start()
//    }

    fun updateUserActionTime() {
        remoteSession?.updateUserActionTime()
    }


    private suspend fun screenCaptureBitmap() = withContext(Dispatchers.IO) {
        ByteArrayOutputStream().use {
            val capturedBitmap = Global.GetInstance().bitmap
            if (capturedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)) {
                withContext(Dispatchers.Main) {
                    _viewSingleEvent.value = RemoteViewSingleEvent.ScreenCaptured(it.toByteArray())
                }
            } else {
                withContext(Dispatchers.Main) {
                    _viewSingleEvent.value =
                        RemoteViewSingleEvent.ScreenCaptureFailure(RSException(RSErrorCode.Runtime.COMMAND_CAPTURE_FAILED))
                }
            }
        }
    }


    sealed class ReconnectStatus {
        object Prepare : ReconnectStatus()
        object Reconnecting : ReconnectStatus()
        data class CountDown(val count: Int) : ReconnectStatus()
        object Reconnected : ReconnectStatus()
        object Finish : ReconnectStatus()
        data class Error(val message: String) : ReconnectStatus()
    }

    sealed class CanvasStatus {
        object UpdateCanvas : CanvasStatus()
    }

    sealed class RemoteStatus {
        object Connecting : RemoteStatus()
        object Connected : RemoteStatus()
        object Disconnecting : RemoteStatus()
        object Disconnected : RemoteStatus()
        object Recovering : RemoteStatus()
        object Recovered : RemoteStatus()
        data class ForceConnectAsking(val accept: () -> Unit, val denied: () -> Unit) :
            RemoteStatus()

        data class Error(val exception: RSException) : RemoteStatus()
    }

    sealed class RemoteViewSingleEvent : SingleLiveEvent.SingleEvent {
        object ShowProgress : RemoteViewSingleEvent()
        object HideProgress : RemoteViewSingleEvent()

        data class ScreenCaptured(val imageData: ByteArray) : RemoteViewSingleEvent()
        data class ScreenCaptureFailure(val exception: RSException) : RemoteViewSingleEvent()
    }
}