package com.rsupport.rv.viewer.sdk.service

import com.rsupport.rv.viewer.sdk.data.packet.HostInfo
import com.rsupport.rv.viewer.sdk.data.packet.ProcessInfo
import com.rsupport.rv.viewer.sdk.data.packet.SystemInfo
import com.rsupport.rv.viewer.sdk.decorder.viewer.channel.CRCVDataChannel
import com.rsupport.rv.viewer.sdk.setting.GlobalStatic

class DataChannelListenerImpl : CRCVDataChannel.IDataChannelListener {
    override fun onReceived(hostInfo: HostInfo?) {
        GlobalStatic.isHostMacPC = hostInfo!!.platform.toInt() != 1
        GlobalStatic.g_languageID = hostInfo.lang.toInt()
    }

    override fun onReceived(systemInfo: SystemInfo?) {
//        val activity = SystemActivity.currentActivity
//        activity?.systemInfoUpdated(systemInfo)
    }

    override fun onReceived(processInfo: ProcessInfo?) {
//        val activity = ProcessActivity.currentActivity
//        activity?.addContent(processInfo)
    }

}