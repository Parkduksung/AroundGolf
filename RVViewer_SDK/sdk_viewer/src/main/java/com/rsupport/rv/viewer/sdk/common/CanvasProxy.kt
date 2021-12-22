package com.rsupport.rv.viewer.sdk.common

import android.view.MotionEvent
import com.rsupport.rv.viewer.sdk.common.log.RLog
import com.rsupport.rv.viewer.sdk.decorder.model.RECT
import com.rsupport.rv.viewer.sdk.decorder.model.SCAP_CLIENT_INFORMATION
import com.rsupport.rv.viewer.sdk.decorder.model.SCAP_DESK_INFORMATION
import com.rsupport.rv.viewer.sdk.decorder.model.SCAP_ENCODER_INFORMATION
import com.rsupport.rv.viewer.sdk.decorder.viewer.channel.rcpResolutionInfoMsg
import com.rsupport.rv.viewer.sdk.decorder.viewer.channel.rcpResolutionMsg
import com.rsupport.rv.viewer.sdk.decorder.viewer.channelConstants
import com.rsupport.rv.viewer.sdk.setting.Global
import com.rsupport.rv.viewer.sdk.setting.GlobalStatic
import com.rsupport.rv.viewer.sdk.ui.SendAction
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.delay


class CanvasProxy {
    var completableDeferred: CompletableDeferred<Any>? = null
    var isResolutionChanged: Boolean = false

    // IScreenCanvas에 UI요소외 관련없는 기능들을 제거하기 위해 임시로 해당 기능들을 처리할 임시 클래스.
    // 리팩토링 완료 후 클래스 제거할것.
    companion object {
        private val instance = CanvasProxy()

        @JvmStatic
        fun getInstance(): CanvasProxy {
            instance.checkObject()
            return instance
        }
    }

    var m_sndAct: SendAction? = null

    constructor() {
        checkObject()
    }

    fun checkObject() {
        if (Global.GetInstance().sendAction == null) {
            RLog.i("CanvasProxy initialized")
            isResolutionChanged = false
            Global.GetInstance().sendAction = SendAction()
        }
        m_sndAct = Global.GetInstance().sendAction
    }

    fun requestProcessInfo() {
        m_sndAct?.m_dataChannel?.sendPacket(channelConstants.rcpProcess, channelConstants.rcpProcessListRequest)
    }

    fun requestHostInfo() {
        val guid = toUTF16ForWin(RuntimeData.getInstance().lastAgentInfo.guid)
        m_sndAct?.m_dataChannel?.sendPacket(channelConstants.rcpRemoteInfo, channelConstants.rcpRemoteInfoRequest, guid.toByteArray(), guid.toByteArray().size)
    }

    fun sendResolutionPacket(width: Int, height: Int, colorBit: Int) {
        val resolutionMsg = rcpResolutionInfoMsg()
        resolutionMsg.width = width
        resolutionMsg.height = height
        resolutionMsg.colorBit = colorBit

        RLog.d("sendResolution", "resolutionMsg.width : " + resolutionMsg.width)
        RLog.d("sendResolution", "resolutionMsg.height : " + resolutionMsg.height)
        RLog.d("sendResolution", "resolutionMsg.colorBit : " + resolutionMsg.colorBit)

        m_sndAct?.m_dataChannel?.sendPacket2(channelConstants.rcpResolution, channelConstants.rcpResolutionChange, resolutionMsg, resolutionMsg.size())
    }

    suspend fun changeResolution(width: Int, height: Int, colorBit: Int) {
        isResolutionChanged = true
        sendResolutionPacket(width, height, colorBit)
        try {
            while (isResolutionChanged){
                delay(500)
            }
        }finally {
            isResolutionChanged = false
        }
    }

    /**
     * 메모리가 부족할 경우, Client 측에서 축소된 화면 Data 를 전송해 줄 것을 요청한다.
     */
    fun screenStretchProc(width: Int, height: Int): Boolean {
        var ret = true
        val maxBufferSize = GlobalStatic.getMaxResolution()
        if (maxBufferSize >= width * height) return ret
        ret = false
        val whRatio = height.toFloat() / width.toFloat()
        //		float square = GlobalStatic.MAX_DESIRE_RESOLUTION / (whRatio);
        val square = maxBufferSize / whRatio
        val reWidth = Math.sqrt(square.toDouble()).toFloat()
        GlobalStatic.FIXWIDTH = reWidth.toInt()
        GlobalStatic.FIXHEIGHT = (reWidth * whRatio).toInt()
        this.requestStrechScreen(reWidth.toInt(), (reWidth * whRatio).toInt())
        return ret
    }

    fun requestStrechScreen(width: Int, height: Int) {
        val sci = SetStrechHelper(width, height)
        m_sndAct?.m_dataChannel?.sendPacket2(channelConstants.rcpOption, channelConstants.rcpOption_SCap, sci, sci.size())
    }

    fun SetStrechHelper(width: Int, height: Int): SCAP_CLIENT_INFORMATION {
        val sci = SCAP_CLIENT_INFORMATION()
        sci.desk.rcSrn = RECT()
        sci.encoder.flags = sci.encoder.flags or SCAP_ENCODER_INFORMATION.eof_stretch.toLong()
        sci.encoder.stretch.fixedWidth = width.toLong()
        sci.encoder.stretch.fixedHeight = height.toLong()
        return sci
    }


    fun sendKeyEvent(key: Int, down: Boolean) {
        m_sndAct?.SendKeyEvent(key, down)
    }

    fun sendKeyEvent(key: Int, down: Boolean, special: Int) {
        m_sndAct?.SendKeyEvent(key, down, special)
    }

    fun sendMouseWheel(x: Int, y: Int, up: Boolean): Boolean {
        return (m_sndAct?.SendMouseWheelEvent(up) ?: false)
    }

    fun requestRecoverStrechScreen() {
        if (GlobalStatic.FIRSTMONITOR_WIDTH.toInt() == 0) return
        val sci = SetStrechHelper(GlobalStatic.FIRSTMONITOR_WIDTH.toInt(), GlobalStatic.FIRSTMONITOR_HEIGHT.toInt())
        m_sndAct?.m_dataChannel?.sendPacket2(channelConstants.rcpOption, channelConstants.rcpOption_SCap, sci, sci.size())
    }

    fun requestSystemInfo() {
        m_sndAct?.m_dataChannel?.sendPacket(channelConstants.rcpSysInfo, channelConstants.rcpSystemInfoRequest)
    }

    fun sendCtrlAltDel() {
        m_sndAct?.m_dataChannel?.sendPacket(channelConstants.rcpFavorite, channelConstants.rcpHotkeyCtrlAltDel)
    }


    fun sendTextPacket(text: String) {
        if (text.length <= 0) return
        try {
            val bytes = text.toByteArray(charset("UTF-16LE"))
            val length = bytes.size
            val bytes2 = ByteArray(length + 2)
            System.arraycopy(bytes, 0, bytes2, 0, length)
            m_sndAct?.m_dataChannel?.sendPacket(channelConstants.rcpClipboard, channelConstants.rcpClipboardDataString, bytes2, bytes2.size)
        } catch (e: Exception) {
        }
    }

    fun sendkillProcessPacket(filename: String) {
        if (filename.length <= 0) return
        try {
            val bytes = filename.toByteArray(charset("UTF-16LE"))
            val length = bytes.size
            val bytes2 = ByteArray(length + 2)
            System.arraycopy(bytes, 0, bytes2, 0, length)
            m_sndAct?.m_dataChannel?.sendPacket(channelConstants.rcpProcess, channelConstants.rcpProcessKillRequest, bytes2, bytes2.size)
        } catch (e: Exception) {
        }
    }

    fun requestScreenSetting() {
        val sci = getCurrentOption()
        m_sndAct?.m_dataChannel?.sendPacket2(channelConstants.rcpOption, channelConstants.rcpOption_SCap, sci, sci.size())
    }

    fun requestScreenLock(lock: Boolean) {
        val type = RuntimeData.getInstance().lastAgentInfo.type
        RLog.d("GlobalStatic.g_agentInfo.type : $type")
        // 가상 PC인 경우 원격 화면 잠금이 불가능합니다.
        if (RuntimeData.getInstance().lastAgentInfo.checkVirtualDevice()) {
//            activity.showToast(activity.getString(R.string.virtualpc_monitor_not_lock))
        } else {
            if (lock) {
                m_sndAct?.m_dataChannel?.sendPacket(channelConstants.rcpScreenCtrl, channelConstants.rcpScreenBlankStart)
            } else {
                m_sndAct?.m_dataChannel?.sendPacket(channelConstants.rcpScreenCtrl, channelConstants.rcpScreenBlankEnd)
            }
        }
    }

    fun saveRemoteResolutionInfo(resolutionInfo: ByteArray) {
        RLog.d("saveRemoteResolutionInfo resolutionInfo length : " + resolutionInfo.size)

        if (resolutionInfo.size <= 5) {
            completableDeferred?.cancel()
            return
        }

        val resolutionMsg = rcpResolutionMsg()
        resolutionMsg.save(resolutionInfo, 0)

        completableDeferred?.complete(sortResolution(resolutionMsg))
    }

    fun requestResolutionInfo() =
            m_sndAct?.m_dataChannel?.sendPacket(channelConstants.rcpResolution, channelConstants.rcpResolutionEnumMode)

    fun sendMouseClick(x: Int, y: Int) {
        m_sndAct?.SendMousePressedEvent(null, x, y, false)
        m_sndAct?.SendMouseReleasedEvent(null, x, y)
    }

    fun sendMouseRClick(x: Int, y: Int) {
        m_sndAct?.SendMousePressedEvent(null, x, y, true)
        m_sndAct?.SendMouseReleasedEvent(null, x, y)
    }

    fun sendTextPacketEx(text: String) {
        if (text.length <= 0) return
        try {
            val bytes = text.toByteArray(charset("UTF-16LE"))
            val length = bytes.size
            val bytes2 = ByteArray(length + 2)
            System.arraycopy(bytes, 0, bytes2, 0, length)
            m_sndAct?.m_dataChannel?.sendPacket(channelConstants.rcpClipboard, channelConstants.rcpClipboardDataString2, bytes2, bytes2.size)
        } catch (e: Exception) {
        }

    }


    fun sendTextPacketEx2(backCount: Int, text: String) {
        if (text.length <= 0) return
        try {
            val bytes = text.toByteArray(charset("UTF-16LE"))
            val length = bytes.size
            val bytes2 = ByteArray(1 + length + 2)
            val bytesBuf = ByteArray(1)
            bytesBuf[0] = backCount.toByte()
            var index = 0
            System.arraycopy(bytesBuf, 0, bytes2, index, 1)
            index++
            System.arraycopy(bytes, 0, bytes2, index, length)
            if (m_sndAct == null) return
            if (m_sndAct?.m_dataChannel == null) return
            if ((m_sndAct?.m_dataChannel?.isConnected) != true) return
            m_sndAct?.m_dataChannel?.sendPacket(channelConstants.rcpClipboard, channelConstants.rcpClipboardDataString3, bytes2, bytes2.size)
        } catch (e: Exception) {
        }
    }

    fun onDrawTouchEvent(event: MotionEvent) {
        m_sndAct?.sendDrawingEvent(event)
    }

    fun sendStartDrag(x: Int, y: Int) {
        m_sndAct?.SendMousePressedEvent(x, y, false)
    }

    fun sendDragMove(x: Int, y: Int) {
        m_sndAct?.SendMousePressedEvent(x, y, false)
    }

    fun sendEndDrag(x: Int, y: Int) {
        m_sndAct?.SendMouseReleasedEvent(x, y)
    }

    private fun sortResolution(resolutionMsg: rcpResolutionMsg): rcpResolutionMsg {
        var temp: rcpResolutionInfoMsg? = null

        for (i in 0 until resolutionMsg.counts - 1) {
            for (j in i + 1 until resolutionMsg.counts) {
                val nCompare = compareResolution(resolutionMsg.resolutionInfoMsg[i], resolutionMsg.resolutionInfoMsg[j])

                if (nCompare == 0) {
                    if (resolutionMsg.currentmodeindex == j) {
                        resolutionMsg.currentmodeindex = i
                    }
                } else if (nCompare > 0) {
                    temp = resolutionMsg.resolutionInfoMsg[i]
                    resolutionMsg.resolutionInfoMsg[i] = resolutionMsg.resolutionInfoMsg[j]
                    resolutionMsg.resolutionInfoMsg[j] = temp

                    if (resolutionMsg.currentmodeindex == i) {
                        resolutionMsg.currentmodeindex = j
                    } else if (resolutionMsg.currentmodeindex == j) {
                        resolutionMsg.currentmodeindex = i
                    }
                }
            }
        }
        Global.GetInstance().resolutionMsg = resolutionMsg
        return resolutionMsg
    }

    private fun compareResolution(resol1: rcpResolutionInfoMsg, resol2: rcpResolutionInfoMsg): Int {
        if (resol1.width == 0 && resol2.width == 0)
            return 0
        if (resol1.width == 0 && resol2.width != 0)
            return 1
        if (resol1.width != 0 && resol2.width == 0)
            return -1

        if (resol1.width > resol2.width)
            return 1
        if (resol1.width == resol2.width && resol1.height > resol2.height)
            return 1
        if (resol1.width == resol2.width && resol1.height == resol2.height && resol1.colorBit > resol2.colorBit)
            return 1

        if (resol1.width < resol2.width)
            return -1
        if (resol1.width == resol2.width && resol1.height < resol2.height)
            return -1
        return if (resol1.width == resol2.width && resol1.height == resol2.height && resol1.colorBit < resol2.colorBit) -1 else 0

    }

    private fun getCurrentOption(): SCAP_CLIENT_INFORMATION {
        val connectOption = RuntimeData.getInstance().agentConnectOption

        val sci = SCAP_CLIENT_INFORMATION()
        sci.desk.rcSrn = RECT()

        if (connectOption.isHookTypeChanged && !GlobalStatic.isHostMacPC) {
            sci.desk.flags = sci.desk.flags or SCAP_DESK_INFORMATION.dof_hookType.toLong()
        }

        if (connectOption.hookType == 'D'.toShort()) {
            sci.desk.hookType = SCAP_DESK_INFORMATION.DdiHook.toLong()
        } else {
            sci.desk.hookType = SCAP_DESK_INFORMATION.PolHook.toLong()
        }
        sci.desk.hookMonitor = 0
        sci.encoder.flags = sci.encoder.flags or SCAP_ENCODER_INFORMATION.eof_vbpp.toLong()
        sci.encoder.flags = sci.encoder.flags or SCAP_ENCODER_INFORMATION.eof_type.toLong()
        sci.encoder.encType = 'Z'.toInt()
        sci.encoder.flags = sci.encoder.flags or SCAP_ENCODER_INFORMATION.eof_cache.toLong()
        sci.encoder.encHostBitsPerPixel = 0
        sci.encoder.encViewerBitsPerPixel = connectOption.scapScreenColor.viewerBpp.toInt()
        sci.encoder.encJpgLowQuality = 0
        sci.encoder.encJpgHighQuality = 70
        sci.encoder.encSendTimesWithoutAck = 0
        sci.encoder.stretch.ratioWidth.cx = 0
        sci.encoder.stretch.ratioWidth.cy = 0
        sci.encoder.stretch.ratioHeight.cx = 0
        sci.encoder.stretch.ratioHeight.cy = 0
        sci.encoder.stretch.fixedWidth = 0
        sci.encoder.stretch.fixedHeight = 0

        RLog.d("Request hooktype = " + sci.desk.hookType)

        return sci
    }


    private fun toUTF16ForWin(v: String): String {
        var bytes: ByteArray? = null
        try {
            val size = v.toByteArray(charset("UTF-16LE")).size
            bytes = ByteArray(size + 2)
            System.arraycopy(v.toByteArray(charset("UTF-16LE")), 0, bytes, 0, size)
            bytes[size] = 0
            bytes[size + 1] = 0
        } catch (e: Exception) {
        }

        return String(bytes!!)
    }


}