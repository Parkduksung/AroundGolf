package com.rsupport.rv.viewer.sdk.data

import android.graphics.Rect
import com.rsupport.rv.viewer.sdk.constant.FPSLevel
import com.rsupport.rv.viewer.sdk.jni.toIntLittleEndianValues

import java.nio.ByteBuffer
import java.nio.ByteOrder
import kotlin.jvm.Throws

class H264MonitorInfo(private val data: ByteArray) {

    //monitor info index
    private val monitorInfoIndexSize = 7
    private val monitorInfoBytesSize = monitorInfoIndexSize * Int.SIZE_BYTES
    private val monitorCount = 0
    private val cx = 1
    private val cy = 2
    private val rectLeft = 3
    private val rectTop = 4
    private val rectRight = 5
    private val rectBottom = 6

    //monitor data index
    private val monitorDataIndexSize = 6
    private val monitorDataBytesSize = 6 * Int.SIZE_BYTES
    private val monitorIndex = 0
    private val monitorLeft = 1
    private val monitorTop = 2
    private val monitorWidth = 3
    private val monitorHeight = 4
    private val primary = 5

    @Throws(ArrayIndexOutOfBoundsException::class)
    fun getMonitorInfo(): MonitorInfo {
        val monitorInfoValues = data.getMonitorInfoValues()
        val allMonitorDataValues =
                data.getAllMonitorDataValues(monitorInfoValues[monitorCount])

        return MonitorInfo(
                monitorInfoValues[monitorCount],
                monitorInfoValues[cx],
                monitorInfoValues[cy],
                Rect(monitorInfoValues[rectLeft],
                        monitorInfoValues[rectTop],
                        monitorInfoValues[rectRight],
                        monitorInfoValues[rectBottom]),
                allMonitorDataValues.getMonitors()
        )
    }

    private fun IntArray.getMonitors(): List<MonitorData> {
        val monitors = mutableListOf<MonitorData>()
        var start = 0

        for (i in monitorDataIndexSize..size step monitorDataIndexSize) {
            monitors.add(copyOfRange(start, i).getMonitorData())
            start = i
        }

        return monitors
    }

    private fun IntArray.getMonitorData() =
            MonitorData(
                    this[monitorIndex],
                    this[monitorLeft],
                    this[monitorTop],
                    this[monitorWidth],
                    this[monitorHeight],
                    this[primary] == 1
            )

    private fun ByteArray.getMonitorInfoValues() =
            copyOfRange(0, monitorInfoBytesSize).toIntLittleEndianValues().toIntArray()

    private fun ByteArray.getAllMonitorDataValues(monitorCount: Int) =
            copyOfRange(
                    monitorInfoBytesSize,
                    monitorCount * monitorDataBytesSize + monitorInfoBytesSize
            ).toIntLittleEndianValues().toIntArray()
}

fun getH264Option(fpsLevel: FPSLevel): ByteArray{
    return ByteBuffer.allocate(Int.SIZE_BYTES).also {
        it.order(ByteOrder.LITTLE_ENDIAN)
        it.put(fpsLevel.toInt().toByte())
        it.put(5)
        it.put(15)
        it.put(30)
    }.array()
}


data class MonitorInfo(
        val count: Int,
        val cX: Int,
        val cY: Int,
        val rect: Rect,
        val monitors: List<MonitorData>
)

data class MonitorData(
        val index: Int,
        val left: Int,
        val top: Int,
        val width: Int,
        val height: Int,
        val primary: Boolean
)