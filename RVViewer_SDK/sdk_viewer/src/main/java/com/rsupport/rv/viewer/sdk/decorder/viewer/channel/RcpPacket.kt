package com.rsupport.rv.viewer.sdk.decorder.viewer.channel

import com.rsupport.rv.viewer.sdk.jni.toBytesLittleEndian
import com.rsupport.rv.viewer.sdk.jni.toIntLittleEndian
import com.rsupport.rv.viewer.sdk.decorder.model.IModel

open class RcpPacket : IModel {
    @JvmField
    var payload = 0

    @JvmField
    var msgSize = 0

    override fun push(buffer: ByteArray, start: Int) {
        buffer[start] = payload.toByte()
        msgSize.toBytesLittleEndian().copyInto(buffer, start + 1)
    }

    override fun save(buffer: ByteArray, start: Int) {
        payload = buffer[start].toInt() and 0xff
        msgSize = buffer.copyOfRange(start + 1, start + 5).toIntLittleEndian()
    }

    override fun save2(buffer: ByteArray, start: Int, dstOffset: Int, dstLen: Int) {}

    override fun size(): Int {
        return 5
    }

//    0, 0, 0, 0, 0, 0, 0, 0, 64, 11, 0, 0, 8, 7, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 64, 11, 0, 0, 8, 7, 0, 0, 32, 0, 0, 0
//    1, 0, 0, 0, 0, 0, 0, 0, 64, 11, 0, 0, 8, 7, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 64, 11, 0, 0, 8, 7, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 64, 11, 0, 0, 8, 7, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
}