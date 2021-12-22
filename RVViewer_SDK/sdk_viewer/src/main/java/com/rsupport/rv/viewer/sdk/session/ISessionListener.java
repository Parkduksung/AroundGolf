package com.rsupport.rv.viewer.sdk.session;

import com.rsupport.rscommon.exception.RSException;

public interface ISessionListener {
    void onSessionConnected();

    void onSessionDisconnected();

    void onSessionReconnecting();

    void onSessionReconnected();

    SessionManager.ConnectForceStatus askForceConnecting();

    void onConnectionError(RSException exception);
}
