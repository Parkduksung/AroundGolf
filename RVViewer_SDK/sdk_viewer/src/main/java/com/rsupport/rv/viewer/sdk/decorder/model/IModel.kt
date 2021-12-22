package com.rsupport.rv.viewer.sdk.decorder.model

interface IModel {
    fun save(buffer: ByteArray, start: Int)
    fun save2(buffer: ByteArray, start: Int, dstOffset: Int, dstLen: Int)
    fun push(buffer: ByteArray, start: Int)
    fun size(): Int
}