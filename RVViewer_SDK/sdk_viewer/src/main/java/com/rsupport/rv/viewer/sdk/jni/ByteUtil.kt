package com.rsupport.rv.viewer.sdk.jni

import java.nio.ByteBuffer
import java.nio.ByteOrder.LITTLE_ENDIAN

fun ByteArray.toIntLittleEndian() = ByteBuffer.wrap(this).order(LITTLE_ENDIAN).int

fun Int.toHeaderBytes(header: Int) = byteArrayOf(header.toByte()) + toBytesLittleEndian()

fun Int.toBytesLittleEndian(): ByteArray =
        ByteBuffer.allocate(Int.SIZE_BYTES).also {
            it.order(LITTLE_ENDIAN)
            it.putInt(this)
        }.array()

fun String.toLengthBytes(): ByteArray {
    if (isEmpty()) return 0.toBytesLittleEndian()

    val valueBytes = toByteArray()
    val lengthBytes = valueBytes.size.toBytesLittleEndian()

    return lengthBytes + valueBytes
}

fun ByteArray.toIntLittleEndianValues(): List<Int> {
    val values = mutableListOf<Int>()
    var start = 0

    for (i in Int.SIZE_BYTES..size step Int.SIZE_BYTES) {
        values.add(copyOfRange(start, i).toIntLittleEndian())
        start = i
        println(start.toString())
    }

    return values
}