package com.rsupport.rv.viewer.sample

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_DEFAULT
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.rsupport.rv.viewer.sdk.RemoteViewApp
import com.rsupport.rv.viewer.sdk.common.RuntimeData
import com.rsupport.rv.viewer.sdk.pref.SettingPref
import com.rsupport.rv.viewer.sdk.setting.GlobalStatic
import com.rsupport.rv.viewer.sdk.ui.ScreenCanvasMobileActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        RemoteViewApp.context = applicationContext



        findViewById<Button>(R.id.btn_init_mqtt).setOnClickListener {
            initRuntimeData()
            createNotficationChannel()
        }

        findViewById<Button>(R.id.btn_start_mqtt).setOnClickListener {
            startActivity(Intent(this, ScreenCanvasMobileActivity::class.java))
        }

    }

    private fun initRuntimeData() {
        RuntimeData.createInstance(applicationContext, true)
        SettingPref.createInstance(applicationContext, true)
        GlobalStatic.pushServerAddr = "stpush.rview.com"
        GlobalStatic.pushServerPort = "443"
        RuntimeData.getInstance().lastAgentInfo.guid = "OK12345"
    }


    private fun createNotficationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the NotificationChannel
            val name = getString(R.string.remote_service_notification_channel_title)
            val descriptionText =
                getString(R.string.remote_service_notification_channel_description)
            val mChannel = NotificationChannel("RemoteService", name, IMPORTANCE_DEFAULT)
            mChannel.description = descriptionText
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(mChannel)
        }
    }

}