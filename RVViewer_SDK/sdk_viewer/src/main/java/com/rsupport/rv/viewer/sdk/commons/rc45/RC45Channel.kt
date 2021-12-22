package com.rsupport.rv.viewer.sdk.commons.rc45

import com.rsupport.rv.viewer.sdk.jni.RC45
import com.rsupport.rv.viewer.sdk.jni.toBytesLittleEndian
import com.rsupport.rv.viewer.sdk.jni.toHeaderBytes
import com.rsupport.rv.viewer.sdk.jni.toLengthBytes


class RC45Channel {

    //command
    private val deleteChannel = 1
    private val createChannel = 2
    private val makeSession = 10
    private val closeSession = 11
    private val write = 12
    private val read = 13
    private val connectPath = 16
    private val channelState = 18
    private val listenPort = 19
    private val encrypt = 21
    private val recOption = 27

    private val rc45 = RC45()

    fun load() = rc45.load()

    fun create(channelNumber: Int) =
            channelNumber.toHeaderBytes(createChannel).write()

    fun makeSession(serverLoginInfo: ServerLoginInfo, netWorkInfo: NetWorkInfo) =
            (byteArrayOf(makeSession.toByte()) +
                    serverLoginInfo.getLoginInfoData() +
                    netWorkInfo.getNetWorkInfoData()).write()

    fun closeSession(channelNumber: Int) =
            channelNumber.toHeaderBytes(closeSession).write()

    fun delete(channelNumber: Int) =
            channelNumber.toHeaderBytes(deleteChannel).write()

    fun enableEncrypt(channelNumber: Int) =
            channelNumber.toHeaderBytes(encrypt).write()

    fun getState(channelNumber: Int) =
            channelNumber.toHeaderBytes(channelState).read()

    fun getConnectPath(channelNumber: Int) =
            channelNumber.toHeaderBytes(connectPath).read()

    fun getListenPort(channelNumber: Int) =
            channelNumber.toHeaderBytes(listenPort).read()

    fun isConnected(channelNumber: Int) =
            channelNumber.toHeaderBytes(channelState).read() == STATE_CONNECTED

    fun setRecOpt(channelNumber: Int, recOptNumber: Int, data: ByteArray) =
            (channelNumber.toHeaderBytes(recOption) +
                    recOptNumber.toBytesLittleEndian() +
                    data.size.toBytesLittleEndian() +
                    data).write()

    fun setRecOpt(channelNumber: Int, recOptNumber: Int, data: Int, len: Int) =
            (channelNumber.toHeaderBytes(recOption) +
                    recOptNumber.toBytesLittleEndian() +
                    len.toBytesLittleEndian() +
                    data.toBytesLittleEndian()).write()

    fun writeEx(data: ByteArray, channelNumber: Int, length: Int) =
            (channelNumber.toHeaderBytes(write) +
                    length.toBytesLittleEndian() +
                    data).write()

    fun readEx(data: ByteArray, channelNumber: Int, readSize: Int): Int {
        val headerBuffer =
                channelNumber.toHeaderBytes(read) + readSize.toBytesLittleEndian()

        headerBuffer.copyInto(data, 0, 0)
        return data.read(headerBuffer.size)
    }

    private fun ByteArray.read(length: Int) =
            rc45.rc45_d2n(this, length)

    private fun ByteArray.read() =
            rc45.rc45_d2n(this, size)

    private fun ByteArray.write() =
            rc45.rc45_d2n(this, size) == 0

    companion object {
        //SSL type
        const val SSL_FAKE = 1
        const val SSL_REAL = 4

        //channel state
        const val STATE_END = 40401
        const val STATE_NOT_CONNECTED = 40402
        const val STATE_ERROR = 40403
        const val STATE_CONNECTED = 40404
        const val STATE_FORCED_DISCONNECT = -1
        const val STATE_TIMEOUT = -2
        const val STATE_UNKNOWN = -3
    }
}

data class RC45ChannelInfo(
    val serverLoginInfo: ServerLoginInfo,
    val netWorkInfo: NetWorkInfo,
    val isRec: Boolean = false,
    val isEncrypt: Boolean = false
)

data class ServerLoginInfo(
        val channelNumber: Int,
        val guid: String,
        val commandType: Int
) {
    //고정값
    private val productType = 1001
    private val userId = "mobile"
    private val companyId = "RSupport"
    private val group = 0
    private val license = 0
    private val hubConnectTimeout = 0
    private val webIp = ""
    private val webDNS = ""
    private val webPort = 0
    private val logoutPage = ""
    private val webConnectTimeout = 0
    private val webReceiveTimeout = 0
    private val webRetryCount = 0

    var hubAddress = ""
    var hubPort = 0

    fun getLoginInfoData() =
            productType.toBytesLittleEndian() +
                    guid.toLengthBytes() +
                    userId.toLengthBytes() +
                    companyId.toLengthBytes() +
                    group.toBytesLittleEndian() +
                    license.toBytesLittleEndian() +
                    hubAddress.toLengthBytes() +
                    hubPort.toBytesLittleEndian() +
                    hubConnectTimeout.toBytesLittleEndian() +
                    webIp.toLengthBytes() +
                    webDNS.toLengthBytes() +
                    webPort.toBytesLittleEndian() +
                    logoutPage.toLengthBytes() +
                    webConnectTimeout.toBytesLittleEndian() +
                    webReceiveTimeout.toBytesLittleEndian() +
                    webRetryCount.toBytesLittleEndian() +
                    commandType.toBytesLittleEndian() +
                    channelNumber.toBytesLittleEndian()
}

data class NetWorkInfo(
        val proxyIP: String = "",
        val proxyPort: Int = 0,
        val proxyID: String = "",
        val proxyPass: String = ""
) {
    //고정값
    private val activeXIP = ""
    private val activeXPort = 0
    private val viewerIP = ""
    private val viewerPort = 0

    fun getNetWorkInfoData() =
            activeXIP.toLengthBytes() +
                    activeXPort.toBytesLittleEndian() +
                    viewerIP.toLengthBytes() +
                    viewerPort.toBytesLittleEndian() +
                    proxyIP.toLengthBytes() +
                    proxyPort.toBytesLittleEndian() +
                    proxyID.toLengthBytes() +
                    proxyPass.toLengthBytes()
}