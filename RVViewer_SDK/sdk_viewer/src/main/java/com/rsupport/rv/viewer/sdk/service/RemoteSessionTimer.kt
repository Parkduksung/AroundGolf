package com.rsupport.rv.viewer.sdk.service

import kotlinx.coroutines.Runnable
import java.util.*

class RemoteSessionTimer(private val viewerTimeoutSeconds: Long, private val timeoutMessageSecond: Long, private val expiredRunnable: Runnable, private val expiredMessageRunnable: Runnable) {

    private val timeoutTask: TimerTask = object : TimerTask() {
        override fun run() {
            expiredRunnable.run()
        }
    }

    private val timeoutMessageTask: TimerTask = object : TimerTask() {
        override fun run() {
            expiredMessageRunnable.run()
        }
    }

    fun start() {
        try {
            startTimer(viewerTimeoutSeconds, timeoutMessageSecond)
        } catch (e: IllegalArgumentException) {
            // 백그라운드 모드 항상 켜지게 하기 위한 작업.
            //None
        }
    }

    fun stop() {
        timeoutTask.cancel()
        timeoutMessageTask.cancel()
    }

    private fun startTimer(viewerTimeoutSeconds: Long, timeoutMessageSecond: Long) {
        val timeOut = viewerTimeoutSeconds * 1000L
        val timeOutMessage: Long = (viewerTimeoutSeconds - timeoutMessageSecond) * 1000L

        val timer = Timer()
        if (viewerTimeoutSeconds > timeoutMessageSecond) {
            timer.schedule(timeoutMessageTask, timeOutMessage)
        }
        timer.schedule(timeoutTask, timeOut)
    }
}
