package com.rsupport.rv.viewer.sdk.service

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import com.rsupport.rv.viewer.sdk.RemoteViewApp
import com.rsupport.rv.viewer.sdk.common.RuntimeData
import com.rsupport.rv.viewer.sdk.common.log.RLog
import com.rsupport.rv.viewer.sdk.pref.SettingPref
import com.rsupport.rv.viewer.sdk.setting.GlobalStatic
import com.rsupport.rv.viewer.sdk_viewer.R
import kotlinx.coroutines.*
import java.util.*


class RemoteViewContext {

    companion object {
        @JvmStatic
        var reqHXVideoWidth = 0

        @JvmStatic
        var reqHXVideoHeight = 0

        @JvmStatic
        var reqHXVideoColor = 0
    }

    private var remoteSession: RemoteSession? = null

    fun getRemoteSession(): RemoteSession {
        return remoteSession ?: RemoteSession(RemoteViewApp.context!!).apply {
            remoteSession = this
        }
    }
}

class RemoteViewServiceLocalBinder(private val remoteViewService: RemoteViewContext) : Binder() {
    fun getRemoteViewService(): RemoteViewContext {
        return remoteViewService
    }
}

class RemoteViewServiceImpl : Service(), LifecycleObserver {

    private val ioScope = CoroutineScope(Dispatchers.IO)
    private val connectTimer: ConnectTimeout = ConnectTimeout {
        remoteSession?.release()
    }

    private var tokenRefreshThread: GlobalStatic.TokenRefreshThread? = null
    private var remoteSession: RemoteSession? = null

    private var backgroundModeTime: RemoteSessionTimer? = null

    override fun onBind(intent: Intent?): IBinder {
        RLog.i("RemoteService bind.")
        return RemoteViewServiceLocalBinder(RemoteViewContext().apply {
            getRemoteSession().apply {
                registerSessionListener(connectTimer)
                registerSessionListener(sessionCallback)
                registerScreenLockListener(screenLockTimer)
            }.run {
                remoteSession = this
            }
        })
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_NOT_STICKY
    }

    override fun onCreate() {
        super.onCreate()
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
        tokenRefresh()
        RLog.i("RemoteService create")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun inBackground() {
        startBackgroundMode()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun inForeground() {
        stopBackgroundMode()
    }

    private fun startBackgroundMode() {
        backgroundModeTime = RemoteSessionTimer(
            SettingPref.getInstance().backgroundModeTime, 0,
            expiredRunnable = {
//                    closeRemotingActivity()
            },
            expiredMessageRunnable = {}
        ).apply { start() }
    }

    private fun stopBackgroundMode() {
        if (backgroundModeTime != null) {
            try {
                backgroundModeTime?.stop()
                backgroundModeTime = null
            } catch (e: NullPointerException) {
                //None
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        RLog.i("RemoteService destroy")
        ioScope.cancel()
        remoteSession?.unRegisterSessionListener(connectTimer)
        remoteSession?.unRegisterSessionListener(sessionCallback)
        remoteSession?.unRegisterScreenLockListener(screenLockTimer)

        connectTimer.release()
        tokenRefreshThread?.interrupt()
        tokenRefreshThread = null
    }

    private val screenLockTimer = object : RemoteSession.OnScreenLockListener {
        override fun onScreenLockEvent(lockStatus: RemoteSession.LockStatus) {
            CoroutineScope(Dispatchers.Main).launch {
                when (lockStatus) {
                    RemoteSession.LockStatus.Disconnect -> {
                        remoteSession?.release()
                    }
                    is RemoteSession.LockStatus.WillScreenLock -> {
//                        Toast.makeText(this@RemoteViewServiceImpl, String.format(getString(R.string.screen_lock_auto_logout_message), Global.GetInstance().webAccessInfo.locktime), Toast.LENGTH_SHORT).show()
                    }
                    is RemoteSession.LockStatus.WillShutdown -> {
//                        Toast.makeText(this@RemoteViewServiceImpl, String.format(getString(R.string.screen_lock_auto_shotdown_message), Global.GetInstance().webAccessInfo.locktime), Toast.LENGTH_SHORT).show()
                    }
                    RemoteSession.LockStatus.LockFailNoAuth -> {
                        Toast.makeText(
                            this@RemoteViewServiceImpl,
                            getString(R.string.screen_lock_auto_cannot_use),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    RemoteSession.LockStatus.UsedNotLockAuth -> {
                        Toast.makeText(
                            this@RemoteViewServiceImpl,
                            getString(R.string.used_not_viewer_lock_message),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }
    }

    private fun Long.toMinute(): Int {
        return (this / (60 * 1000)).toInt()
    }

    private val sessionCallback = object : RemoteSession.OnSessionListener {
        override fun onChangeStatus(status: RemoteSession.Status): Unit = when (status) {
            RemoteSession.Status.Connecting -> {
                startForegroundService()
            }
            is RemoteSession.Status.Disconnected -> {
                releaseRemoteSession()
                stopForegroundService()
            }
            is RemoteSession.Status.ConnectRemainTime -> {
                showRemainConnectTimeToast(status)
            }
            else -> {
            }
        }
    }

    private fun releaseRemoteSession() {
        remoteSession?.release()
        remoteSession = null
    }

    private fun showRemainConnectTimeToast(status: RemoteSession.Status.ConnectRemainTime) {
        CoroutineScope(Dispatchers.Main).launch {
            val min = status.timeSecond / 60
            val msg = getString(R.string.msg_viewer_timeout, min)
            Toast.makeText(this@RemoteViewServiceImpl, msg, Toast.LENGTH_LONG).show()
        }
    }

    private fun tokenRefresh() {
        if (RuntimeData.getInstance().appProperty?.isApiAuthByToken == true) {
            tokenRefreshThread = GlobalStatic.TokenRefreshThread().apply {
                start()
            }
        }
    }

    private fun stopForegroundService() {
        RLog.v("stopForegroundService")
        stopForeground(true)
        stopSelf()
    }

    private fun startForegroundService() {
        startService(Intent(this@RemoteViewServiceImpl, RemoteViewServiceImpl::class.java))
//        val resultIntent = Intent(this, MainActivity::class.java)
//        resultIntent.action = "android.intent.action.MAIN"
//        resultIntent.addCategory("android.intent.category.LAUNCHER")
//        val pendingIntent = PendingIntent.getActivity(
//            this,
//            0,
//            resultIntent,
//            PendingIntent.FLAG_UPDATE_CURRENT
//        )
//
//        val builder: NotificationCompat.Builder = NotificationCompat.Builder(this, "RemoteService")
//            .setSmallIcon(R.drawable.icon)
//            .setContentTitle(getString(R.string.remote_service_notification_title))
//            .setContentText(getString(R.string.remote_service_notification_description))
//            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//            .setContentIntent(pendingIntent)
//            .setAutoCancel(false)
//
//        val notification = builder.build()
//        startForeground(10, notification)
    }


    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        remoteSession?.release()
        RLog.i("RemoteService TaskRemoved")
    }
}