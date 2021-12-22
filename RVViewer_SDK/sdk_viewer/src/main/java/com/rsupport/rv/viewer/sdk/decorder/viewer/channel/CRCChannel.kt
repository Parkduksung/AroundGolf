package com.rsupport.rv.viewer.sdk.decorder.viewer.channel


import com.rsupport.commons.net.socket.SocketCompat
import com.rsupport.commons.net.socket.compat.policy.*
import com.rsupport.commons.net.socket.rsnetcl.callback.OnFPSChangedCallback
import com.rsupport.rv.viewer.sdk.common.GUID
import com.rsupport.rv.viewer.sdk.common.RuntimeData
import com.rsupport.rv.viewer.sdk.common.log.RLog
import com.rsupport.rv.viewer.sdk.commons.rc45.IRC45Observer
import com.rsupport.rv.viewer.sdk.data.HostPort
import com.rsupport.rv.viewer.sdk.decorder.Converter
import com.rsupport.rv.viewer.sdk.decorder.model.IModel
import com.rsupport.rv.viewer.sdk.decorder.viewer.channelConstants
import com.rsupport.rv.viewer.sdk.rsnet.*
import com.rsupport.rv.viewer.sdk.session.SessionManager
import com.rsupport.rv.viewer.sdk.setting.Global
import com.rsupport.rv.viewer.sdk.setting.isXenc
import java.lang.IllegalStateException
import kotlin.jvm.Throws

open class CRCChannel @JvmOverloads constructor(private val useServerRec: Boolean,  guid: String? = null, private val  useEncrypt : Boolean = false) {
    enum class ChannelStatus {
        CREATED, CONNECTING, CONNECTED, CONNECT_FAILED, DISCONNECTED
    }

    interface IChannelListener {
        fun requestConnect(guid: String?, port: Int)
        fun onConnected()

        /**
         * 재연결시도 시작시 호출된다.
         */
        fun onReconnecting()

        /**
         * 재연결이 완료되면 호출된다
         */
        fun onReconnected()
        fun onException(e: Exception?) //Read loop중 오류가 발생하면 연결이 끊기도록 되어있기때문에 따로 onDisconnect는 두지 않는다.
    }

    private var mChannelNum = 0
    private var stream: SocketCompat? = null
    private val agentGuid = RuntimeData.getInstance().lastAgentInfo.guid

    protected var status = ChannelStatus.CREATED
    protected var channelListener: IChannelListener? = null
    protected var mainThread: Thread? = null

    val nopInterval = 25000L
    var sendTime = 0L
    var isThreadRun = false
    var bReceive = ByteArray(1024 * 10)
    var bSend = ByteArray(1024 * 10)

    var guid: String? = null

    protected val connectGuid: String by lazy {
        // RSNet 은 DataChannel guid 를 RoomID 로 사용한다. 실제 사용되는 room id 는 '$dataGuid_$channelNumber' 이 된다
        if(isXenc()){
            RuntimeData.getInstance().lastAgentInfo.guid
        } else this.guid!!
    }

    var socket: SocketCompatFactory? = null

    private fun getConnectPolicy(agentGuid: String) = ConnectPolicy(
            connectPolicy = RSNetConnectPolicy.create(RSNetConnectPolicy.PolicyType.SpecificUser(agentGuid)),
            disconnectPolicy = RSNetDisconnectPolicy.create(RSNetDisconnectPolicy.PolicyType.EmptyUser)
    )

    private fun getConnectReconnectPolicy(agentGuid: String) = ConnectPolicy(
            connectPolicy = RSNetConnectPolicy.create(RSNetConnectPolicy.PolicyType.SpecificUser(agentGuid)),
            disconnectPolicy = RSNetDisconnectPolicy.create(RSNetDisconnectPolicy.PolicyType.DeferredSpecificUser(
                    userID = agentGuid,
                    onDeferredDisconnectListener = object : DeferredDisconnectListener {
                        override fun onDisconnected() {
                            RLog.v("onDisconnected : $agentGuid")
                        }

                        override fun onJoin() {
                            RLog.v("onJoin : $agentGuid")
                            channelListener?.onReconnected()
                        }

                        override fun onLeave() {
                            RLog.v("onLeave : $agentGuid")
                            channelListener?.onReconnecting()
                        }
                    })),
            reConnectPolicy = RSNetReConnectPolicy.create(RSNetReConnectPolicy.PolicyType.Retry(
                    onReconnectListener = object : OnReconnectListener {
                        override fun onFailure() {
                            RLog.v("channel : $agentGuid")
                        }

                        override fun onReconnecting(count: Int) {
                            RLog.v("channel : $agentGuid")
                            channelListener?.onReconnecting()
                        }

                        override fun onSuccess() {
                            RLog.v("channel : $agentGuid")
                            channelListener?.onReconnected()
                        }
                    }
            ))
    )

    protected fun connectChannel(
        channelNum: Int,
        bufferSize: Int,
        observer: IRC45Observer,
        onFPSChangedCallback: OnFPSChangedCallback? = null
    ) = if (isXenc()) {
        connectByRsNet(channelNum, bufferSize, observer, onFPSChangedCallback)
    } else {
        connectByRC45(channelNum, bufferSize, observer)
    }

    private fun connectByRsNet(
            channelNum: Int,
            bufferSize: Int,
            observer: IRC45Observer,
            onFPSChangedCallback: OnFPSChangedCallback?
    ) = if (isConnected) {
        RLog.i("Channel $channelNum already connected.")
        true
    } else {
        status = ChannelStatus.CONNECTING
        mChannelNum = channelNum

        RLog.i("connect channel connectGuid $connectGuid ")
        RLog.i("connect channel userGuid ${RuntimeData.getInstance().currentSessionData.userGuid} ")

        try {
            socket = RSNetCompatFactory(
                    ChannelInfo(
                            channelNum,
                            connectGuid,
                            RuntimeData.getInstance().currentSessionData.userGuid,
                            "strelay2.rview.com",
                            443,
                            bufferSize,
                            useServerRec,
                            observer
                    ),
                    createConnectPolicy(agentGuid)
            )

            onFPSChangedCallback?.let {
                stream = socket?.createVideoStream(it)
            } ?: kotlin.run {
                stream = socket?.create()
            }

            RLog.d("connect channel end")
            stream?.connect() ?: false
        } catch (ex: Exception) {
            RLog.d(ex)
            status = ChannelStatus.CONNECT_FAILED
            false
        } finally {
            status = ChannelStatus.CONNECTED
            observer.onConnected()
        }
    }

    private fun createConnectPolicy(agentGuid: String): ConnectPolicy{
        return when(mChannelNum){
            SessionManager.CHANNELNUM_DATA, SessionManager.CHANNELNUM_SCREEN -> getConnectReconnectPolicy(agentGuid)
            else -> getConnectPolicy(agentGuid)
        }
    }


    protected fun connectByRC45(channelNum: Int, bufferSize: Int, observer: IRC45Observer) =
            if (isConnected) {
                RLog.i("Channel $channelNum already connected.")
                true
            } else {
                status = ChannelStatus.CONNECTING
                mChannelNum = channelNum
                try {
                    socket = RS45CompatFactory(ChannelInfo(
                            channelNum,
                            RuntimeData.getInstance().lastAgentInfo.guid,
                            guid!!,
                            "",
                            0,
                            bufferSize,
                            useServerRec,
                            observer
                    ))
                    stream = socket?.create()?.apply {
                        if(useEncrypt){
                            enableEncrypt()
                        }
                    }
                    stream?.connect() ?: false
                } catch (ex: Exception) {
                    status = ChannelStatus.CONNECT_FAILED
                    false
                } finally {
                    status = ChannelStatus.CONNECTED
                }
            }

    val connectType: Int
        get() = socket?.getConnectPath() ?: 0

    fun setListener(listener: IChannelListener?) {
        channelListener = listener
    }

    fun readExact(pBuf: ByteArray?, offset: Int, len: Int): Boolean {
        if (stream == null || len <= 0) return false
        val read = stream!!.getDataStream().read(pBuf!!, offset, len)
        if (read <= 0) {
            RLog.e("Failed to ReadExact read : $read")
            return false
        }
        return true
    }

    fun readExact(obj: IModel): Boolean {
        if (obj.size() > bReceive.size) {
            RLog.e("Failed to ReadExact 1")
            return false
        }
        if (readExact(bReceive, 0, obj.size())) {
            obj.save(bReceive, 0)
            return true
        }
        return false
    }

    fun readExact(len: Int): ByteArray? {
        try {
            if (bReceive.size < len) {
                bReceive = ByteArray(len)
            }
        } catch (e: OutOfMemoryError) {
            return null
        }
        return if (readExact(bReceive, 0, len)) bReceive else null
    }

    fun writeExact(obj: IModel): Boolean {
        if (stream == null) return false
        obj.push(bSend, 0)
        val bRes = stream!!.getDataStream().write(bSend, 0, obj.size())
        if (!bRes) {
            RLog.e("Failed to Channel.WriteExact(m)")
        }
        sendTime = System.currentTimeMillis()
        return bRes
    }

    fun writeExact(sendBuf: ByteArray?, offset: Int, nSize: Int): Boolean {
        if (stream == null) return false
        val bRes = stream!!.getDataStream().write(sendBuf!!, offset, nSize)
        if (!bRes) {
            RLog.e("Failed to Channel.WriteExact(b)")
        }
        sendTime = System.currentTimeMillis()
        isConnected
        return bRes
    }

    private var isDisconnect = false

    private fun closeSocket(): Boolean {
        RLog.d("closeSocket.channelID.$mChannelNum")

        isThreadRun = false
        if (stream == null) {
            return false
        }
        socket?.setReqDisConnect()
        while (socket?.isMakeRunningSession() == true && mChannelNum > 0) {
            try {
                Thread.sleep(300)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
        socket?.close()
        stream = null
        if (isDisconnect) return true
        isDisconnect = true
        return true
    }

    val isRunningMakeSession: Boolean
        get() = socket?.isMakeRunningSession() ?: false

    open fun releaseAll(): Boolean {
        status = ChannelStatus.DISCONNECTED
        return closeSocket()
    }

    fun sendNop(cmd: Int): Boolean {
        return sendPacket(channelConstants.rcpChannelNop, cmd)
    }

    @JvmOverloads
    fun sendPacket(payload: Int, msgId: Int, data: ByteArray? = null, dataSize: Int = 0): Boolean {
        if (payload == -1) return false
        val packet = RcpPacket()
        val nPacketSize = if (data != null && dataSize > 0) {
            channelConstants.sz_rcpPacket + channelConstants.sz_rcpDataMessage + dataSize
        } else {
            channelConstants.sz_rcpPacket + channelConstants.sz_rcpMessage
        }
        val pSendPacket = ByteArray(nPacketSize)
        var nPacketPos = channelConstants.sz_rcpPacket
        packet.payload = payload
        packet.msgSize = nPacketSize - channelConstants.sz_rcpPacket
        packet.push(pSendPacket, 0)
        if (dataSize > 0 && data != null) {
            pSendPacket[nPacketPos] = msgId.toByte()
            nPacketPos++
            System.arraycopy(Converter.getBytesFromIntLE(dataSize), 0, pSendPacket, nPacketPos, 4)
            nPacketPos += 4
            System.arraycopy(data, 0, pSendPacket, nPacketPos, dataSize)
        } else {
            pSendPacket[nPacketPos] = msgId.toByte()
        }
        return writeExact(pSendPacket, 0, nPacketSize)
    }

    fun sendPacket2(payload: Int, msgId: Int, data: IModel, dataSize: Int): Boolean {
        data.push(bSend, 0)
        return sendPacket(payload, msgId, bSend, dataSize)
    }

    fun readNop() {
        val b = ByteArray(1)
        if (!readExact(b, 0, 1)) return
        if (0xff and b[0].toInt() == channelConstants.rcpNopRequest) {
            sendNop(channelConstants.rcpNopConfirm)
        }
    }

    fun readMsgSendToOwner(packet: RcpPacket) {
        val msg = rcpMsg()
        var bResult = false
        if (packet.msgSize > 0) {
            val buf = readExact(packet.msgSize)
            if (buf == null) bResult = false else {
                bResult = true
                msg.save2(buf, 0, 0, packet.msgSize)
            }
        }
        if (bResult) {
            readChannelDataFunc(packet.payload, msg)
        }
    }

    open fun readChannelDataFunc(payload: Int, msg: rcpMsg) {}

    @JvmOverloads
    fun keepSessionThread(isAck: Boolean = false) {
        if(isXenc()) return;

        val th: Thread = object : Thread() {
            override fun run() {
                try {
                    procKeepSession(isAck)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
        }
        th.start()
    }

    @Throws(InterruptedException::class)
    private fun procKeepSession(isAck: Boolean) {
        while (isThreadRun) {
            Thread.sleep(10000)
            val currentTime = System.currentTimeMillis()
            val interval = currentTime - sendTime
            if (Global.GetInstance() == null) break
//            if (Global.GetInstance().pcViewer == null) break
            if (!isConnected) break
            if (nopInterval < interval) {
                val ret = if (isAck) sendNop(channelConstants.rcpNopConfirmNoAck) else sendNop(channelConstants.rcpNopRequest)
                if (!ret && stream?.isConnected() == false) {
                    channelListener?.onException(IllegalStateException("rcpNopRequest fail"));
                }
                sendTime = System.currentTimeMillis()
            }
        }
    }

    protected val streamListenPort: Int
        get() = socket?.getListenPort() ?: 0

    protected val currentDaemon: HostPort?
        get() = socket?.getCurrentDaemon()

    val isConnected: Boolean
        get() = status == ChannelStatus.CONNECTED &&
                stream != null &&
                stream?.isConnected() == true

    class ChannelCloseException : Exception()

    fun isRun() = mainThread != null && (isThreadRun || mainThread?.isInterrupted == false)

    init {
        if (guid == null) {
            this.guid = GUID().toString()
        } else {
            this.guid = guid
        }
    }
}