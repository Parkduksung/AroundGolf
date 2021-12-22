package com.rsupport.rv.viewer.sdk.commons.rc45

import com.rsupport.commons.net.socket.DataStream
import com.rsupport.commons.net.socket.SocketCompat
import com.rsupport.rv.viewer.sdk.common.RuntimeData
import com.rsupport.rv.viewer.sdk.common.log.RLog
import com.rsupport.rv.viewer.sdk.data.HostPort
import com.rsupport.rv.viewer.sdk.setting.CNetStatus
import com.rsupport.rv.viewer.sdk.setting.Global
import com.rsupport.rv.viewer.sdk.setting.GlobalStatic

class RC45Stream(
        bufSize: Int,
        private val channelID: Int,
        private val guid: String,
        private val useServerRec: Boolean
) : SocketCompat {
    private val readBuffer = ByteArray(bufSize)
    private val rc45Channel = RC45Channel()
    private val hubList = RuntimeData.getInstance().serverInfo.getDaemonList()
    private var retryCount = 0
    private var isDisconnect = false
    private var isReqDisConnect = false
    private var mRC45Observer: IRC45Observer? = null

    private var isEnableEncrypt = false

    var isRunningMakeSession = false
    lateinit var currentDaemon: HostPort

    init {
        if (!rc45Channel.load()) {
            RLog.e("Load fail : $channelID")
            throw RuntimeException()
        }
    }

    override fun connect(): Boolean {
        synchronized(this) {
            sleep()

            if(hubList.isNullOrEmpty()) {
                RLog.e("HostPort empty fail : $channelID")
                return false
            }

            currentDaemon = hubList[0]

            val serverLoginInfo = ServerLoginInfo(
                    channelID,
                    guid,
                    SSL_TYPE
            )

            serverLoginInfo.hubAddress = currentDaemon.host
            serverLoginInfo.hubPort = currentDaemon.port

            val netWorkInfo: NetWorkInfo =
                    if (GlobalStatic.SCREENSET_PROXYUSE) {
                        NetWorkInfo(
                                GlobalStatic.g_setinfoProxyAddr,
                                GlobalStatic.g_setinfoProxyPort.toInt(),
                                GlobalStatic.g_setinfoProxyID,
                                GlobalStatic.g_setinfoProxyPasswd
                        )
                    } else {
                        NetWorkInfo()
                    }

            if (!rc45Channel.create(channelID)) {
                RLog.e("CreateChannel fail : $channelID")
                return false
            }

            RLog.i("CreateRSNet Success : $channelID")

            // 녹화 옵션
            if (useServerRec) {
                setRecOpt()
            }

            //데이터 채널일 경우에만 사용.
            if(isEnableEncrypt){
                rc45Channel.enableEncrypt(channelID)
            }

            RLog.i("setEncKey : true : $channelID")

            val ret = rc45Channel.makeSession(serverLoginInfo, netWorkInfo)
            isRunningMakeSession = ret

            RLog.i("makeSessionEx : ret : $ret")

            if (!ret) {
                while (!reTryMakeSession(serverLoginInfo, netWorkInfo)) {
                    if (!networkCheck()) {
                        RLog.e("Network Connection Broken!!")
                        return false
                    }
                    if (hubList.size <= retryCount) {
                        retryCount = 0
                        RLog.e("MakeSession fail : $channelID")
                        return false
                    }
                }
                isRunningMakeSession = true
            }

            RLog.e("MakeSession Success : $channelID")

            try {
                mRC45Observer?.onConnecting()

                val connectionStartTime = System.currentTimeMillis()
                var diff: Long

                while (isRunningMakeSession && !GlobalStatic.isCloseChannel) {
                    val status = rc45Channel.getState(channelID)

                    RLog.i("Tunnel Status : $status, ID : $channelID")

                    if (isReqDisConnect) {
                        isRunningMakeSession = false
                        return false
                    }

                    if (status == RC45Channel.STATE_CONNECTED) {
                        //Connect Success
                        RLog.e("Connect success : $channelID")
                        break
                    }

                    if (status == RC45Channel.STATE_ERROR) {
                        //Connect Fail
                        RLog.e("Connect fail : $channelID")
                        isRunningMakeSession = false
                        disConnect()
                        return false
                    }

                    if (status != RC45Channel.STATE_END
                            && status != RC45Channel.STATE_NOT_CONNECTED) {
                        //Connect Fail
                        RLog.e("Unknown error : $channelID")
                        isRunningMakeSession = false
                        disConnect()
                        return false
                    }


                    diff = System.currentTimeMillis() - connectionStartTime
                    if (diff >= CONNECTION_TIMEOUT) {
                        RLog.e("Connection timed out : $channelID")
                        return false
                    }

                    sleep()
                }

                isRunningMakeSession = false
                mRC45Observer?.onConnected()
            } catch (e: Exception) {
                RLog.w(e)
                return false
            }
        }

        return true
    }

    private fun reTryMakeSession(serverLoginInfo: ServerLoginInfo, netWorkInfo: NetWorkInfo): Boolean {
        if (!networkCheck()) {
            RLog.e("reTryMakeSession Network Connection Broken!!")
            return false
        }

        retryCount++

        if (hubList.size <= retryCount) return false

        currentDaemon = hubList[retryCount]
        serverLoginInfo.hubAddress = currentDaemon.host
        serverLoginInfo.hubPort = currentDaemon.port

        RLog.i("reTryMakeSession vhubInfo : " + currentDaemon.host + ":" + currentDaemon.port)

        return rc45Channel.makeSession(serverLoginInfo, netWorkInfo)
    }

    override fun isConnected(): Boolean {
        return rc45Channel.getState(channelID) == RC45Channel.STATE_CONNECTED
    }

    fun setReqDisConnect() {
        isReqDisConnect = true
    }

    fun setOnListener(observer: IRC45Observer?) {
        mRC45Observer = observer
    }

    val connectPath: Int
        get() = rc45Channel.getConnectPath(channelID)

    private fun disConnect(): Boolean {
        if (isDisconnect) {
            return true
        }

        isDisconnect = true

        return rc45Channel.closeSession(channelID) and rc45Channel.delete(channelID)
    }

    fun close(): Boolean {
        RLog.e("close")
        return disConnect()
    }

    private fun sleep() {
        try {
            Thread.sleep(500)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    val listenPort: Int
        get() = rc45Channel.getListenPort(channelID)

    private fun networkCheck(): Boolean {
        val netStatus = CNetStatus.getInstance()

        return (netStatus.get3GStatus(Global.GetInstance().currentActivity)
                || netStatus.getWifiStatus(Global.GetInstance().currentActivity))
    }

    private fun setRecOpt() {
        var retCode = 0
//        if (SERVER_REC_OK == Global.GetInstance().webAccessInfo.serverrec) {
//            var isSetRecOpt = false
//            val userInfo = RuntimeData.getInstance().userInfo
//            val agentName = RuntimeData.getInstance().lastAgentInfo.name
//            val deviceID = SettingPref.getInstance().getDeviceID("-")
//            if (userInfo.loginId.isNotEmpty()) {
//                val temp = userInfo.loginId.toByteArray(StandardCharsets.UTF_8)
//                isSetRecOpt = rc45Channel.setRecOpt(channelID, ComConstant.OPT_SERV_REC_USERNAME, temp)
//                if (!isSetRecOpt) retCode = -1
//            }
//            RLog.d("GlobalStatic.g_agentInfo.name : $agentName / $isSetRecOpt")
//            if (agentName.isNotEmpty()) {
//                val temp = agentName.toByteArray(StandardCharsets.UTF_8)
//                isSetRecOpt = rc45Channel.setRecOpt(channelID, ComConstant.OPT_SERV_REC_PCNAME, temp)
//                if (!isSetRecOpt) retCode = -2
//            }
//            RLog.d("GlobalStatic.g_deviceIP : " + GlobalStatic.g_deviceIP + " / " + isSetRecOpt)
//            if (GlobalStatic.g_deviceIP.isNotEmpty()) {
//                val temp = GlobalStatic.g_deviceIP.toByteArray(StandardCharsets.UTF_8)
//                isSetRecOpt = rc45Channel.setRecOpt(channelID, ComConstant.OPT_SERV_REC_IP, temp)
//                if (!isSetRecOpt) retCode = -3
//            }
//            RLog.d("Device ID : $deviceID")
//            if (!StringUtil.isEmpty(deviceID)) {
//                val temp = deviceID.toByteArray(StandardCharsets.UTF_8)
//                isSetRecOpt = rc45Channel.setRecOpt(channelID, ComConstant.OPT_SERV_REC_MAC, temp)
//                if (!isSetRecOpt) retCode = -4
//            }
//            val webcall = WebConnection.getInstance().recWebCallUrl
//            RLog.d("GlobalStatic.g_connectionInfo.recwebcallurl : $webcall")
//            if (!StringUtil.isEmpty(webcall)) {
//                val temp1 = webcall.toByteArray(StandardCharsets.UTF_8)
//                isSetRecOpt = rc45Channel.setRecOpt(channelID, ComConstant.OPT_SERV_REC_WEB_CALLPAGE, temp1)
//                if (!isSetRecOpt) retCode = -5
//                val temp2: ByteArray
//                val logkey = Global.GetInstance().webAccessInfo.logkey
//                temp2 = if (GlobalStatic.isPhonetoPhone() || RuntimeData.getInstance().agentConnectOption.isHxEngine) {
//                    String.format("viewer_guid=%s&android_cam_rec=1", guid).toByteArray(StandardCharsets.UTF_8)
//                } else {
//                    String.format("viewer_guid=%s&room_guid=%s", guid, logkey).toByteArray(StandardCharsets.UTF_8)
//                }
//                isSetRecOpt = rc45Channel.setRecOpt(channelID, ComConstant.OPT_SERV_REC_WEB_CALLDATA, temp2)
//                RLog.d("GlobalStatic.g_connectionInfo.recwebcallurl urlS : " + String.format("viewer_guid=%s&room_guid=%s", guid, logkey))
//                RLog.d("GlobalStatic.g_connectionInfo.recwebcallurl isSetRecOpt : $isSetRecOpt")
//                if (!isSetRecOpt) retCode = -6
//            }
//            isSetRecOpt = rc45Channel.setRecOpt(channelID, ComConstant.OPT_SERV_REC_MOBILE_TYPE, 2, 4)
//            if (!isSetRecOpt) retCode = -7
//            val temp = Build.MODEL.toByteArray(StandardCharsets.UTF_8)
//            isSetRecOpt = rc45Channel.setRecOpt(channelID, ComConstant.OPT_SERV_REC_MOBILE_NAME, temp)
//            if (!isSetRecOpt) retCode = -8
//            isSetRecOpt = rc45Channel.setRecOpt(channelID, ComConstant.OPT_SERV_REC_MOBILE_ENG_VER, 1, 4)
//            if (!isSetRecOpt) retCode = -9
//            isSetRecOpt = rc45Channel.setRecOpt(channelID, ComConstant.OPT_SERV_REC_ON, 1, 4)
//            if (!isSetRecOpt) retCode = -10
//            RLog.d("Last retCode : $retCode")
//        }
    }

    override fun disconnect() {}

    override fun enableEncrypt() {
        isEnableEncrypt = true
    }

    override fun getDataStream(): DataStream {
        return DataStreamImpl()
    }

    internal inner class DataStreamImpl : DataStream {
        override fun read(dst: ByteArray, offset: Int, length: Int): Int {
            var len = length
            require(!(offset < 0 || len <= 0))
            var read = 0
            var index = 0
            while (len > 0) {
                read = rc45Channel.readEx(readBuffer, channelID, len)
                if (read <= 0) return read
                System.arraycopy(readBuffer, 0, dst, offset + index, read)
                len -= read
                index += read
            }
            return read
        }

        override fun write(src: ByteArray, offset: Int, length: Int): Boolean {
            require(!(offset < 0 || length <= 0))
            return rc45Channel.writeEx(src, channelID, length)
        }

        override fun close() {}
    }

    companion object {
        const val BUFFER_SIZE = 64 * 1024
        const val BUFFER_FTP_SIZE = 100 * 1024
        private const val CONNECTION_TIMEOUT = 3 * 60 * 1000

        @JvmField
        var SSL_TYPE = 1
        private const val SERVER_REC_OK = "1"
    }
}