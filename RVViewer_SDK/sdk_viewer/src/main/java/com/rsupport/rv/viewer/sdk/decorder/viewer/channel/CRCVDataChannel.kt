package com.rsupport.rv.viewer.sdk.decorder.viewer.channel


import com.rsupport.rv.viewer.sdk.common.CanvasProxy
import com.rsupport.rv.viewer.sdk.common.ComConstant
import com.rsupport.rv.viewer.sdk.common.RuntimeData
import com.rsupport.rv.viewer.sdk.common.log.RLog
import com.rsupport.rv.viewer.sdk.commons.rc45.IRC45Observer
import com.rsupport.rv.viewer.sdk.commons.rc45.RC45Stream
import com.rsupport.rv.viewer.sdk.constant.ENGINE_HX
import com.rsupport.rv.viewer.sdk.constant.FPSLevel
import com.rsupport.rv.viewer.sdk.control.DeflaterEx
import com.rsupport.rv.viewer.sdk.data.getH264Option
import com.rsupport.rv.viewer.sdk.data.packet.HostInfo
import com.rsupport.rv.viewer.sdk.data.packet.ProcessInfo
import com.rsupport.rv.viewer.sdk.data.packet.SystemInfo
import com.rsupport.rv.viewer.sdk.decorder.Converter
import com.rsupport.rv.viewer.sdk.decorder.scap.scapOptionMsg
import com.rsupport.rv.viewer.sdk.decorder.viewer.channelConstants
import com.rsupport.rv.viewer.sdk.session.SessionManager
import com.rsupport.rv.viewer.sdk.setting.Global
import com.rsupport.rv.viewer.sdk.setting.isXenc
import java.io.IOException
import java.nio.ByteBuffer

class CRCVDataChannel(useServerRec: Boolean, guid: String?) : CRCChannel(useServerRec, guid, useEncrypt = true), Runnable,
    IRC45Observer {
    interface IDataChannelListener {
        fun onReceived(hostInfo: HostInfo?)
        fun onReceived(systemInfo: SystemInfo?)
        fun onReceived(processInfo: ProcessInfo?)
    }

    private val compression = DeflaterEx(DeflaterEx.DEFAULT_COMPRESSION, true)
    private var listener: IDataChannelListener? = null
//    private var monitorSwitchSwitchControlListener: MonitorSwitchController.MonitorSwitchControlListener? = null

    fun startThread() {
        mainThread?.let {
            isThreadRun = false
            it.interrupt()
        }

        isThreadRun = true
        mainThread = Thread(this)
        mainThread?.start()
        sendTime = System.currentTimeMillis()
    }

    private fun stopThread() {
        compression.end()
        isThreadRun = false

        mainThread?.interrupt()
        mainThread = null
    }

    fun setDataChannelListener(listener: IDataChannelListener?) {
        this.listener = listener
    }

//    fun setMonitorSwitchControlListener(monitorSwitchSwitchControlListener: MonitorSwitchController.MonitorSwitchControlListener) {
//        this.monitorSwitchSwitchControlListener = monitorSwitchSwitchControlListener
//    }

    fun reqMonitorInfo() {
        sendPacket(channelConstants.rcpMonitors, channelConstants.rcpMonitorsInfoRequest)
    }

    fun reqResetFrame() {
        sendPacket(channelConstants.rcpXENC264Stream, channelConstants.rcpXENC264StreamMakeKeyFrame)
    }

    fun reqH264Stream(h264FPSLevel: FPSLevel) {
        val h264option = getH264Option(h264FPSLevel)
        sendPacket(channelConstants.rcpXENC264Stream, channelConstants.rcpXENC264StreamOption, h264option, h264option.size)
    }

    private fun startChannelRead() {
        try {
            if (!isXenc()) {        //xenc(rs net)는 스크린 채널 연결후 모니터 정보 요청 해야함.
                reqMonitorInfo()
            }

            val packet = RcpPacket()
            while (mainThread != null && (isThreadRun || mainThread?.isInterrupted == false)) {
                if (!readExact(packet)) {
                    throw IOException("ReadExact returns 0 on DataChannel")
                }

                RLog.i("Data Channel .... looping ... : " + packet.payload)

                when (packet.payload) {
                    channelConstants.rcpRVScreenShow -> RLog.i("rcpRVScreenShow")
                    channelConstants.rcpResolution -> {
                        RLog.i("rcpResolution")
                        readMsgSendToOwner(packet)
                    }
                    channelConstants.rcpScreenCtrl -> {
                        RLog.i("rcpScreenCtrl")
                        readMsgSendToOwner(packet)
                    }
                    channelConstants.rcpSessionState -> {
                        RLog.i("rcpSessionState")
                        readMsgSendToOwner(packet)
                    }
                    channelConstants.rcpSessionShare -> {
                        RLog.i("rcpSessionShare")
                        readMsgSendToOwner(packet)
                    }
                    channelConstants.rcpChannelNop -> {
                        RLog.i("rcpChannelNop")
                        readNop()
                    }
                    channelConstants.rcpChannel -> {
                        RLog.i("rcpChannel")
                        readMsgSendToOwner(packet)
                    }
                    channelConstants.rcpKeyMouseCtrl -> {
                        RLog.i("rcpKeyMouseCtrl")
                        readMsgSendToOwner(packet)
                    }
                    channelConstants.rcpMonitors -> {
                        RLog.i("rcpMonitors")
                        readMsgSendToOwner(packet)
                    }
                    channelConstants.rcpSFTP -> {
                        RLog.i("rcpSFTP")
                        readMsgSendToOwner(packet)
                    }
                    channelConstants.rcpRebootConnect -> {
                        RLog.i("rcpRebootConnect")
                        readMsgSendToOwner(packet)
                    }
                    channelConstants.rcpAnotherAccountReConnect -> {
                        RLog.i("rcpAnotherAccountReConnect")
                        readMsgSendToOwner(packet)
                    }
                    channelConstants.rcpClipboard -> {
                        RLog.i("rcpClipboard")
                        readMsgSendToOwner(packet)
                    }
                    channelConstants.rcpRemoteInfo -> {
                        RLog.i("rcpRemoteInfo")
                        readMsgSendToOwner(packet)
                    }
                    channelConstants.rcpProcess -> {
                        RLog.i("rcpProcess")
                        readMsgSendToOwner(packet)
                    }
                    channelConstants.rcpSysInfo -> {
                        RLog.i("rcpSysInfo")
                        readMsgSendToOwner(packet)
                    }
                    channelConstants.rcpDragDropFTP -> {
                        RLog.i("rcpDragDropFTP")
                        readMsgSendToOwner(packet)
                    }
                    channelConstants.rcpFavorite -> {
                        RLog.i("rcpFavorite")
                        readMsgSendToOwner(packet)
                    }
                    channelConstants.rcpChat -> {
                        RLog.i("rcpChat")
                        readMsgSendToOwner(packet)
                    }
                    channelConstants.rcpDraw -> {
                        RLog.i("rcpDraw")
                        readMsgSendToOwner(packet)
                    }
                    channelConstants.rcpScreenBlankStatus -> {
                        RLog.i("rcpScreenBlankStatus")
                        readMsgSendToOwner(packet)
                    }
                    channelConstants.rcpOption -> {
                        RLog.i("<=== rcpOption ===>")
                        readMsgSendToOwner(packet)
                    }
                    channelConstants.rcpMobile -> {
                        RLog.i("rcpMobile")
                        readMsgSendToOwner(packet)
                    }
                    channelConstants.rcpXENC264Stream -> {
                        RLog.i("rcpXENC264Stream")
                        readMsgSendToOwner(packet)
                    }
                    else -> RLog.i("data channel default case : " + packet.payload)
                }
            }
        } catch (ex: Exception) {
            channelListener?.onException(ex)
        }
    }

    override fun run() {
        if (Global.GetInstance().sendAction == null) {
            CanvasProxy.getInstance()
        }
        RLog.d("connect channel 0")
        if (connectChannel(SessionManager.CHANNELNUM_DATA, RC45Stream.BUFFER_SIZE, this)) {
            Global.GetInstance().dataChannel = this
            Global.GetInstance().sendAction.m_dataChannel = this
            RLog.d("connect channel 1")
            while (!isValidWebInterfacing) {
                try {
                    Thread.sleep(100)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
                if (!isThreadRun) return
            }
            RLog.d("connect channel 2")
            keepSessionThread()
            RLog.d("connect channel 3")
            channelListener?.onConnected()
            RLog.d("connect channel 4")
            sendAgentOption()
            startChannelRead()
        }else {
            channelListener?.onException(IllegalStateException("not connected data channel.$guid"))
        }
    }

    private var isValidWebInterfacing = false
    fun setIsValidWebInterfacing(isValidWebInterfacing: Boolean) {
        this.isValidWebInterfacing = isValidWebInterfacing
    }

    private fun sendAgentOption() {
        val optMsg = scapOptionMsg()
        val configFunction = ComConstant.getRVDefaultConfigFunction()
        val configOption = ComConstant.getRVDefaultConfigOption()
        val packet = rcpSettingInfoPacket()
        val settingInfo = ByteArray(packet.size())

        optMsg.setFunction(configFunction)
        optMsg.setOption(configOption)
        optMsg.setMaxFTPSize(1000000000)
        sendPacket2(channelConstants.rcpOption, channelConstants.rcpOption_rcOption, optMsg, optMsg.size())

        packet.chatType = 1
        packet.autoCtrlRequest = 1
        packet.screenEngineType = RuntimeData.getInstance().agentConnectOption.engineType
        packet.push(settingInfo, 0)
        RLog.d("rcpOption_Setting : " + channelConstants.rcpOption_Setting)
        sendPacket(channelConstants.rcpOption, channelConstants.rcpOption_Setting, settingInfo, settingInfo.size)

        //입력잠금 옵션 전달
        val agentLockPacket = rcpAgentLock()
        val agentLock = ByteArray(agentLockPacket.size())

        agentLockPacket.agentLock = RuntimeData.getInstance().lastAgentInfo.agentInputLock
        agentLockPacket.push(agentLock, 0)
        RLog.d("rcpAgentLock : " + agentLockPacket.agentLock)
        sendPacket(channelConstants.rcpKeyMouseCtrl, channelConstants.rcpHostInputLock, agentLock, agentLock.size)
    }

    override fun readChannelDataFunc(payload: Int, msg: rcpMsg) {
        when (payload) {
            channelConstants.rcpKeyMouseCtrl ->
                if (msg.id.toInt() == channelConstants.rcpKeyMouseCtrlConfirm) {
                    Global.GetInstance().sendAction.m_nKeyMouseControlState = (msg.data[0].toInt() and 0xff)
                }
            channelConstants.rcpRemoteInfo -> {
                val hostInfo = HostInfo(msg.data)
                RLog.i("rcpRemoteHost: $hostInfo")
                listener?.onReceived(hostInfo)
            }
            channelConstants.rcpResolution -> {
                setResolution(msg.id.toInt(), msg.data)
            }
            channelConstants.rcpMonitors ->
                if (msg.id.toInt() == channelConstants.rcpMonitorsInfoResponse) {
                    RLog.i("rcpMonitorsInfoResponse")
                    listener?.let {

//                        monitorSwitchSwitchControlListener?.onReceived(payload, msg.id.toInt(), msg.data)

                        if (isXenc()) {
                            reqH264Stream(RuntimeData.getInstance().agentConnectOption.h264FPSLevel)
                        }
                    }
                } else if (msg.id.toInt() == channelConstants.rcpMonitorSelect) {
                    RLog.i("rcpMonitorSelect")
                    val monitorNumber = Converter.readIntLittleEndian(msg.data)
                    val bIntMonitorNumber = ByteBuffer.allocate(4).putInt(monitorNumber).array()
                    RLog.v("monitorSelected DataChannel.$monitorNumber")
//                    monitorSwitchSwitchControlListener?.onReceived(payload, msg.id.toInt(), bIntMonitorNumber)

                } else if (msg.id.toInt() == channelConstants.rcpMonitorChangeRequest) {
                    if (isXenc()) {
                        listener?.let {
                            RLog.i("rcpMonitorChangeRequest")
//                            monitorSwitchSwitchControlListener?.onReceived(payload, msg.id.toInt(), msg.data)
                            sendPacket(channelConstants.rcpMonitors, channelConstants.rcpMonitorChangeResponse)
                        }
                    }
                } else {
                    RLog.i("rcpMonitor message: " + msg.id)
                }
            channelConstants.rcpSysInfo ->
                if (msg.id.toInt() == channelConstants.rcpSystemInfo) {
                    RLog.i("rcpSystemInfo")
                    val sysInfo = SystemInfo(msg.data, msg.datasize)
                    RLog.d("SystemInfo: $sysInfo")
                    listener?.onReceived(sysInfo)
                } else {
                    RLog.i("rcpSystemInfo message: " + msg.id)
                }
            channelConstants.rcpProcess ->
                if (msg.id.toInt() == channelConstants.rcpProcessList) {
                    RLog.i("rcpProcessList")
                    val processInfo = ProcessInfo(msg.data)
                    listener?.onReceived(processInfo)
                }
            channelConstants.rcpScreenCtrl -> {
                RLog.i("rcpScreenCtrl callback")
                if (msg.data != null) {
                    Global.GetInstance().screenController.saveScreenCtrl(msg.data, msg.datasize)
                }
            }
            channelConstants.rcpXENC264Stream -> {
                if (isXenc()) {
                    if (msg.id.toInt() == channelConstants.rcpXENC264StreamOption) {
                        RLog.i("rcpXENC264StreamOption")
                        sendPacket(channelConstants.rcpXENC264Stream, channelConstants.rcpXENC264StreamMakeKeyFrame)
                    }
                }
            }
            else -> RLog.i("Data channel : default")
        }
    }

    private fun setResolution(msgId: Int, data: ByteArray) {
        RLog.i("setResolution.msgid.$msgId")
        when (msgId) {
            channelConstants.rcpResolutionEnumMode -> {
                CanvasProxy.getInstance().saveRemoteResolutionInfo(data)
            }
            channelConstants.rcpResolutionCurrentMode -> {
                if (isXenc()) {
                    reqH264Stream(RuntimeData.getInstance().agentConnectOption.h264FPSLevel)
                }
                else {
                    if(!CanvasProxy.getInstance().isResolutionChanged && RuntimeData.getInstance().agentConnectOption.engineType == ENGINE_HX){
                        Global.GetInstance().screenChannel?.run {
                            sendVideoStop()
                            val defaultMonitorIndex = 1
                            sendReqVideoInfo(defaultMonitorIndex)
                        }
                    }
                    CanvasProxy.getInstance().isResolutionChanged = false
                }
            }
        }
    }

    fun reqSpeakerPhoneStart() {
        sendPacket(channelConstants.rcpMobile, channelConstants.rcpMobileSpeekerPhoneOnRequest)
    }

    fun reqSpeakerPhoneStop() {
        sendPacket(channelConstants.rcpMobile, channelConstants.rcpMobileSpeekerPhoneOffRequest)
    }

    fun reqNotificationDown() {
        sendPacket(channelConstants.rcpMobile, channelConstants.rcpMobileNotificationDownRequest)
    }

    fun reqNotificationUp() {
        sendPacket(channelConstants.rcpMobile, channelConstants.rcpMobileNotificationUpRequest)
    }

    fun reqPointerStart() {
        sendPacket(channelConstants.rcpLaserPointer, channelConstants.rcpLaserPointerStart)
    }

    fun reqPointerStop() {
        sendPacket(channelConstants.rcpLaserPointer, channelConstants.rcpLaserPointerEnd)
    }

    fun reqDrawStart() {
        RLog.d("rcpDraw", "rcpDrawStart")
        sendPacket(channelConstants.rcpDraw, channelConstants.rcpDrawStart)
    }

    fun sendDrawingData(msgId: Int, data: ByteArray?, dataLength: Int) {
        RLog.d("rcpDraw", "rcpDrawData")
        sendPacket(channelConstants.rcpDraw, msgId, data, dataLength)
    }

    fun reqDrawStop() {
        RLog.d("rcpDraw", "rcpDrawEnd")
        sendPacket(channelConstants.rcpDraw, channelConstants.rcpDrawEnd)
    }

    fun reqDrawClear() {
        RLog.d("rcpDraw", "rcpDrawClear")
        sendPacket(channelConstants.rcpDraw, channelConstants.rcpDrawClear)
    }

    override fun releaseAll(): Boolean {
        isValidWebInterfacing = false
        stopThread()
        return super.releaseAll()
    }

    override fun onConnecting() {
        RuntimeData.getInstance().serverInfo.currentDaemonData = currentDaemon
        channelListener?.requestConnect(connectGuid, streamListenPort)
    }

    override fun onConnected() {
        isValidWebInterfacing = true
    }

    init {
        RuntimeData.getInstance().currentSessionData.dataGuid = super.guid
    }
}