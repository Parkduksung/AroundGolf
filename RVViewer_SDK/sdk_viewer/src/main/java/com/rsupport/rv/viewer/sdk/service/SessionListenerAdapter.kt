package com.rsupport.rv.viewer.sdk.service

import android.content.Context
import com.rsupport.rscommon.define.RSErrorCode
import com.rsupport.rscommon.exception.RSException
import com.rsupport.rv.viewer.sdk.session.ISessionListener
import com.rsupport.rv.viewer.sdk.session.SessionManager

class SessionListenerAdapter(private val context: Context, private val onSessionListener: RemoteSession.OnSessionListener) :
    ISessionListener {
    private var askStatus = SessionManager.ConnectForceStatus.UNDEFINED

    override fun onSessionConnected() {
        onSessionListener.onChangeStatus(RemoteSession.Status.Connected)
    }

    override fun onSessionDisconnected() {
        onSessionListener.onChangeStatus(RemoteSession.Status.Disconnected)
    }

    override fun onSessionReconnecting() {
        onSessionListener.onChangeStatus(RemoteSession.Status.SessionRecovering)
    }

    override fun onSessionReconnected() {
        onSessionListener.onChangeStatus(RemoteSession.Status.SessionReconnected)
    }

    override fun askForceConnecting(): SessionManager.ConnectForceStatus {
        if(askStatus == SessionManager.ConnectForceStatus.UNDEFINED){
            askStatus = SessionManager.ConnectForceStatus.WAITING
            onSessionListener.onChangeStatus(
                RemoteSession.Status.ForceConnectAsking(
                    accept = {
                        askStatus = SessionManager.ConnectForceStatus.ACCEPTED
                    },
                    denied = {
                        askStatus = SessionManager.ConnectForceStatus.DENIED
                    }
            ))
        }
        return askStatus
    }

    override fun onConnectionError(exception: RSException?) {
        onSessionListener.onChangeStatus(
            RemoteSession.Status.Error(exception
                ?: RSException(RSErrorCode.UNKNOWN)))
    }
}