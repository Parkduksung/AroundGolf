package com.rsupport.rv.viewer.sdk.constant

sealed class RouteType {
    companion object {
        const val TYPE_OTP: Int = 3
        const val  TYPE_EMAIL: Int = 4
        const val  TYPE_AGENT_LIST: Int = 0
    }

    object AGENT_LIST : RouteType()
    object OTP : RouteType()
    object EMAIL : RouteType()
}