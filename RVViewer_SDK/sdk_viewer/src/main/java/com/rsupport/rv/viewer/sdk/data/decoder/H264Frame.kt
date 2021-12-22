package com.rsupport.rv.viewer.sdk.data.decoder

import kotlin.experimental.and

class H264Frame(private val frame: ByteArray){

    companion object{
        private const val KEY_FRAME = 7
    }

    fun isKeyFrame(): Boolean {
        if(!isAvailable()) {
            return false
        }
        return getNalType() == KEY_FRAME
    }

    private fun isAvailable(): Boolean{
        if(frame.size < 4){
            return false
        }
        return true
    }

    private fun getNalType(): Int{
        return when {
            isNaluHeader3() -> {
                (frame[3] and 0x1f).toInt()
            }
            isNaluHeader4() -> {
                (frame[4] and 0x1f).toInt()
            }
            else -> {
                -1
            }
        }
    }

    private fun isNaluHeader3(): Boolean{
        return if(frame.size > 3){
            frame[0] == 0x00.toByte() && frame[1] == 0x00.toByte() && frame[2] == 0x01.toByte()
        }else {
            false
        }
    }

    private fun isNaluHeader4(): Boolean{
        return if(frame.size > 4){
            frame[0] == 0x00.toByte() && frame[1] == 0x00.toByte() && frame[2] == 0x00.toByte() && frame[3] == 0x01.toByte()
        } else {
            false
        }
    }
}