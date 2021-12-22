package com.rsupport.rv.viewer.sdk.data.packet


import com.rsupport.rv.viewer.sdk.common.Util
import java.nio.ByteBuffer
import java.nio.ByteOrder
import kotlin.experimental.and

class HostInfo {
    val platform: Byte
    val account: Byte
    val appMode: Byte
    val osMajorVersion: Int
    val osMinorVersion: Int
    val pcName: String
    val remoteIp: String
    val os: String
    val lang: Short

    constructor(data: ByteArray) {
        var idx = 0
        platform = data[idx++]
        account = data[idx++]
        appMode = data[idx++]
        osMajorVersion = ByteBuffer.wrap(data, idx, 4).order(ByteOrder.LITTLE_ENDIAN).int; idx+=4
        osMinorVersion = ByteBuffer.wrap(data, idx, 4).order(ByteOrder.LITTLE_ENDIAN).int; idx+=4

        var len = ByteBuffer.wrap(data, idx, 4).order(ByteOrder.LITTLE_ENDIAN).int; idx+=4
        pcName = Util.getStringFromWCHAR(data, idx, len); idx+=len

        len = ByteBuffer.wrap(data, idx, 4).order(ByteOrder.LITTLE_ENDIAN).int; idx+=4
        remoteIp = Util.getStringFromWCHAR(data, idx, len); idx+=len

        len = ByteBuffer.wrap(data, idx, 4).order(ByteOrder.LITTLE_ENDIAN).int; idx+=4
        os = Util.getStringFromWCHAR(data, idx, len); idx+=len

        var langId = ByteBuffer.wrap(data, idx, 2).order(ByteOrder.LITTLE_ENDIAN).short; idx+=2
        lang = langId.and(0x03FF)

        //?
        idx+=2

        //사전동의여부
        idx+=2

        //사후동의서
        idx+=2
    }

    override fun toString(): String {
        return "HostInfo: Platform=$platform, Account=$account, appMode=$appMode, " +
                "osMajor=$osMajorVersion, osMinor=$osMinorVersion, " +
                "pcname=$pcName, ip=$remoteIp, os=$os, lang=$lang"
    }
}