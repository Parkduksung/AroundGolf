package com.rsupport.rv.viewer.sdk.service

import com.rsupport.rv.viewer.sdk.common.log.RLog
import kotlinx.coroutines.*

class ConnectTimeout(private val timeout: Long = 45000, private val timeoutCallback: ()->Unit) :
    RemoteSession.OnSessionListener {
    private val ioScope = CoroutineScope(Dispatchers.IO)

    fun release() {
        ioScope.cancel()
    }

    override fun onChangeStatus(status: RemoteSession.Status): Unit = when (status) {
        RemoteSession.Status.Connected, is RemoteSession.Status.Disconnected -> {
            RLog.i("onChangeStatus.$status")
            ioScope.cancel()
        }
        RemoteSession.Status.Connecting -> {
            RLog.i("onChangeStatus.$status")
            ioScope.launch {
                delay(timeout)
                timeoutCallback.invoke()
            }
            Unit
        }
        else -> { }
    }
}