package com.rsupport.rv.viewer.sdk.session

data class SessionOptions(
        val isUseServerRec: Boolean,
        val isUseSessionChannel: Boolean,
        val isUseMqttSession: Boolean = true
)