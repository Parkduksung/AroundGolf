package com.rsupport.rv.viewer.sdk.data.packet

import java.nio.ByteBuffer
import java.nio.ByteOrder

class ProcessInfo {
    val pcName: String
    val processList = ArrayList<ProcessItem>()

    constructor(data: ByteArray) {
        var idx = 0
        var len: Int


        len = ByteBuffer.wrap(data, idx, 4).order(ByteOrder.LITTLE_ENDIAN).int; idx+=4
        pcName = String(data, idx, len); idx+=len

        val count = ByteBuffer.wrap(data, idx, 4).order(ByteOrder.LITTLE_ENDIAN).int; idx+=4
        var name: String
        var image: ByteArray?
        for(i in 1..count) {
            len = ByteBuffer.wrap(data, idx, 4).order(ByteOrder.LITTLE_ENDIAN).int; idx+=4

            name = String(data, idx, len); idx+=len

            len = ByteBuffer.wrap(data, idx, 4).order(ByteOrder.LITTLE_ENDIAN).int; idx+=4
            if(len > 0) {
                image = ByteArray(len)
                System.arraycopy(data, idx, image, 0, len); idx+=len
            }else {
                image = null
            }

            processList.add(ProcessItem(name, image))
        }
    }

    class ProcessItem {
        var processId: Int = -1
        val processPath: String
        val processName: String
        val imageData: ByteArray?

        constructor(path: String, image: ByteArray?) {
            processPath = path
            imageData = image

            processName = processPath.substring(processPath.lastIndexOf("\\") + 1)
        }
    }
}