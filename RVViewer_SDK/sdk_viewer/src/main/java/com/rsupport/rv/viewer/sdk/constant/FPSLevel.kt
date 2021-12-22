package com.rsupport.rv.viewer.sdk.constant

enum class FPSLevel{
    LOW,
    MEDIUM,
    HIGH,
    ;

    fun toInt(): Int = when(this){
        LOW -> 0
        MEDIUM -> 1
        HIGH -> 2
    }

    companion object{
        @JvmStatic
        fun from(fps: Int): FPSLevel = when{
            fps == 0 -> LOW
            fps == 1 -> MEDIUM
            else -> HIGH
        }
    }
}