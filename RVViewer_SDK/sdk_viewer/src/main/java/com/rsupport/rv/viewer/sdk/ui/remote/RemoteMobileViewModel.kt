package com.rsupport.rv.viewer.sdk.ui.remote

import android.app.Application
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import androidx.lifecycle.*
import com.rsupport.rv.viewer.sdk.common.log.RLog
import com.rsupport.rv.viewer.sdk.service.RemoteSession
import com.rsupport.rv.viewer.sdk.service.RemoteViewServiceImpl
import com.rsupport.rv.viewer.sdk.service.RemoteViewServiceLocalBinder
import com.rsupport.rv.viewer.sdk.session.SessionOptions
import kotlinx.coroutines.launch


class RemoteMobileViewModel(app: Application) : AndroidViewModel(app), LifecycleObserver {

    private val context: Context = app
    private var remoteSession: RemoteSession? = null

    private val _remoteStatusLiveData = MutableLiveData<RemoteViewModel.RemoteStatus>()
    val remoteStatusLiveData: LiveData<RemoteViewModel.RemoteStatus> = _remoteStatusLiveData.distinctUntilChanged()

    private val _canvasStatusLiveData = MutableLiveData<RemoteViewModel.CanvasStatus>()
    val canvasStatusLiveData: LiveData<RemoteViewModel.CanvasStatus> = _canvasStatusLiveData

    private var isConnectable = false

    fun setConnectable() {
        isConnectable = true
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart() {
        context.bindService(Intent(context, RemoteViewServiceImpl::class.java), remoteViewServiceConnection, Context.BIND_AUTO_CREATE)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStop() {
        remoteSession?.pauseScreen()
        remoteSession?.unRegisterSessionListener(sessionListener)
        context.unbindService(remoteViewServiceConnection)
    }

    private val remoteViewServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val remoteViewService = (service as RemoteViewServiceLocalBinder).getRemoteViewService()
            remoteSession = remoteViewService.getRemoteSession().apply {
                registerSessionListener(sessionListener)
                val remoteStatus = getStatus()
                RLog.i("remoteStatus.$remoteStatus")
                when (remoteStatus) {
                    RemoteSession.Status.Idle -> {
                        if (isConnectable) {
                            connect(
                                SessionOptions(
                                    isUseServerRec = true,
                                    isUseSessionChannel = false,
                                    isUseMqttSession = true)
                            )
                            isConnectable = false
                        } else {
                            _remoteStatusLiveData.value = RemoteViewModel.RemoteStatus.Disconnected
                        }
                    }

                    RemoteSession.Status.Connected -> {
                        resumeScreen()
                        _canvasStatusLiveData.value = RemoteViewModel.CanvasStatus.UpdateCanvas
                    }
                    RemoteSession.Status.Disconnected -> {
                        _remoteStatusLiveData.value = RemoteViewModel.RemoteStatus.Disconnected
                    }
                    RemoteSession.Status.Connecting -> {
                        _remoteStatusLiveData.value = RemoteViewModel.RemoteStatus.Connecting
                    }
                    is RemoteSession.Status.ForceConnectAsking -> {
                        _remoteStatusLiveData.value = RemoteViewModel.RemoteStatus.ForceConnectAsking(remoteStatus.accept, remoteStatus.denied)
                    }
                    is RemoteSession.Status.Error -> {
                        _remoteStatusLiveData.value = RemoteViewModel.RemoteStatus.Error(remoteStatus.exception)
                    }
                }
            }
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            remoteSession = null
        }
    }

    override fun onCleared() {
        super.onCleared()
    }

    fun disconnect() {
        remoteSession?.release()
        remoteSession = null
    }

    fun updateUserActionTime() {
        remoteSession?.updateUserActionTime()
    }

    private val sessionListener = object : RemoteSession.OnSessionListener {
        override fun onChangeStatus(status: RemoteSession.Status) {
            viewModelScope.launch {
                when (status) {
                    RemoteSession.Status.Connecting -> {
                        _remoteStatusLiveData.value = RemoteViewModel.RemoteStatus.Connecting
                    }
                    RemoteSession.Status.Connected -> {
                        _remoteStatusLiveData.value = RemoteViewModel.RemoteStatus.Connected
                    }
                    RemoteSession.Status.SessionRecovering -> {
                        _remoteStatusLiveData.value = RemoteViewModel.RemoteStatus.Recovering
                    }
                    RemoteSession.Status.SessionReconnected -> {
                        _remoteStatusLiveData.value = RemoteViewModel.RemoteStatus.Recovered
                    }
                    RemoteSession.Status.Disconnected -> {
                        _remoteStatusLiveData.value = RemoteViewModel.RemoteStatus.Disconnected
                    }
                    is RemoteSession.Status.ForceConnectAsking -> {
                        _remoteStatusLiveData.value = RemoteViewModel.RemoteStatus.ForceConnectAsking(status.accept, status.denied)
                    }
                    is RemoteSession.Status.Error -> {
                        _remoteStatusLiveData.value = RemoteViewModel.RemoteStatus.Error(status.exception)
                    }
                    RemoteSession.Status.Disconnecting -> {
                        _remoteStatusLiveData.value = RemoteViewModel.RemoteStatus.Disconnecting
                    }
                }
            }
        }
    }
}