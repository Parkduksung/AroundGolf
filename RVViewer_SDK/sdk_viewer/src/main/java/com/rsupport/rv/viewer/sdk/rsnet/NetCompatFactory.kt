package com.rsupport.rv.viewer.sdk.rsnet


import com.rsupport.commons.net.socket.SocketCompat
import com.rsupport.commons.net.socket.compat.RSNetSocketCompat
import com.rsupport.commons.net.socket.compat.RSNetVideoSocketCompat
import com.rsupport.commons.net.socket.compat.policy.RSNetConnectPolicy
import com.rsupport.commons.net.socket.compat.policy.RSNetDisconnectPolicy
import com.rsupport.commons.net.socket.compat.policy.RSNetReConnectPolicy
import com.rsupport.commons.net.socket.rsnetcl.callback.OnFPSChangedCallback
import com.rsupport.commons.net.socket.rsnetcl.protocol.RSNetConnectionParams
import com.rsupport.commons.net.socket.rsnetcl.protocol.RSNetProxyInfo
import com.rsupport.rv.viewer.sdk.commons.rc45.IRC45Observer
import com.rsupport.rv.viewer.sdk.commons.rc45.RC45Stream
import com.rsupport.rv.viewer.sdk.constant.RSNetClientConstant
import com.rsupport.rv.viewer.sdk.data.HostPort
import com.rsupport.rv.viewer.sdk.setting.GlobalStatic


class RS45CompatFactory(private val channelInfo: ChannelInfo) : SocketCompatFactory {
    private lateinit var stream: RC45Stream

    override fun create(): SocketCompat {
        stream = RC45Stream(
                channelInfo.bufferSize,
                channelInfo.channelId,
                channelInfo.userId,
                channelInfo.isRecord
        ).apply {
            setOnListener(channelInfo.observer)
        }

        return stream
    }

    override fun createVideoStream(onFPSChangedCallback: OnFPSChangedCallback): SocketCompat {
        TODO()
    }

    override fun getConnectPath() = stream.connectPath

    override fun getListenPort() = stream.listenPort

    override fun getCurrentDaemon(): HostPort = stream.currentDaemon

    override fun setReqDisConnect() {
        stream.setReqDisConnect()
    }

    override fun isMakeRunningSession() = stream.isRunningMakeSession

    override fun setMakeRunningSession(value: Boolean) {
        stream.isRunningMakeSession = value
    }

    override fun close() = stream.close()
}


class RSNetCompatFactory(
    private val channelInfo: ChannelInfo,
    private val connectPolicy: ConnectPolicy
) : SocketCompatFactory {
    private var stream: RSNetSocketCompat? = null
    private var videoStream: RSNetVideoSocketCompat? = null
    private var makeRunningSession = false

    override fun create(): SocketCompat? {
        val roomId = "${channelInfo.guid}_${channelInfo.channelId}"
        channelInfo.observer.onConnecting()
        makeRunningSession = true

        stream = RSNetSocketCompat(
                name = "channel : ${channelInfo.channelId}",
                channelId = channelInfo.channelId,
                params = RSNetConnectionParams().apply {
                    this.roomId = roomId
                    userId = channelInfo.userId
                    uri = channelInfo.uri
                    port = channelInfo.port.toString()
                    userInfo = "{}"

                    if (GlobalStatic.SCREENSET_PROXYUSE) {
                        this.proxyInfo = RSNetProxyInfo(
                                GlobalStatic.g_setinfoProxyAddr,
                                GlobalStatic.g_setinfoProxyPort.toInt(),
                                GlobalStatic.g_setinfoProxyID,
                                GlobalStatic.g_setinfoProxyPasswd
                        )
                    }
                },
                connectPolicy = connectPolicy.connectPolicy,
                disconnectPolicy = connectPolicy.disconnectPolicy,
                reConnectPolicy = connectPolicy.reConnectPolicy
        )
        makeRunningSession = false
        return stream
    }

    override fun createVideoStream(onFPSChangedCallback: OnFPSChangedCallback): SocketCompat? {
        val roomId = "${channelInfo.guid}_${channelInfo.channelId}"
        channelInfo.observer.onConnecting()
        makeRunningSession = true

        videoStream = RSNetVideoSocketCompat(
                name = "channel : ${channelInfo.channelId}",
                channelId = channelInfo.channelId,
                params = RSNetConnectionParams().apply {
                    this.roomId = roomId
                    userId = channelInfo.userId
                    uri = channelInfo.uri
                    port = channelInfo.port.toString()
                    userInfo = "{}"
                },
                onFPSChangedCallback = onFPSChangedCallback,
                connectPolicy = connectPolicy.connectPolicy,
                disconnectPolicy = connectPolicy.disconnectPolicy,
                reConnectPolicy = connectPolicy.reConnectPolicy
        )
        makeRunningSession = false
        return videoStream
    }

    // RSNet은 중계서버 방식만 있으므로 중계로 설정한다.
    override fun getConnectPath() = RSNetClientConstant.RSNET_RCNT_MODE

    override fun getListenPort() = 0

    override fun getCurrentDaemon() = HostPort(channelInfo.uri, channelInfo.port)

    override fun setReqDisConnect() {}

    override fun isMakeRunningSession() = makeRunningSession

    override fun setMakeRunningSession(value: Boolean) {
        makeRunningSession = value
    }

    override fun close(): Boolean {
        stream?.disconnect()
        videoStream?.disconnect()
        return true
    }
}

data class ChannelInfo(
        val channelId: Int,
        val guid: String,
        val userId: String,
        val uri: String,
        val port: Int,
        val bufferSize: Int,
        val isRecord: Boolean,
        val observer: IRC45Observer
)

data class ConnectPolicy(
    val connectPolicy: RSNetConnectPolicy = RSNetConnectPolicy.create(RSNetConnectPolicy.PolicyType.AnyOne),
    val disconnectPolicy: RSNetDisconnectPolicy = RSNetDisconnectPolicy.create(RSNetDisconnectPolicy.PolicyType.Never),
    val reConnectPolicy: RSNetReConnectPolicy = RSNetReConnectPolicy.create(RSNetReConnectPolicy.PolicyType.Not)
)

interface SocketCompatFactory {
    fun create(): SocketCompat?
    fun createVideoStream(onFPSChangedCallback: OnFPSChangedCallback): SocketCompat?
    fun getConnectPath(): Int
    fun getListenPort(): Int
    fun getCurrentDaemon(): HostPort
    fun setReqDisConnect()
    fun isMakeRunningSession(): Boolean
    fun setMakeRunningSession(value: Boolean)
    fun close(): Boolean
}