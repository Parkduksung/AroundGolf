package com.rsupport.rv.viewer.sdk.extension

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.rsupport.rv.viewer.sdk.common.log.RLog
import com.rsupport.rv.viewer.sdk_viewer.R


fun Activity.launchImageView(uri: Uri){
    Handler(Looper.getMainLooper()).post { Toast.makeText(this, this.getString(R.string.msg_screen_capture_success), Toast.LENGTH_LONG).show() }
    try {
        val intent = Intent()
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.action = Intent.ACTION_VIEW
        intent.setDataAndType(uri, "image/*")
        startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        RLog.w(e)
    }
}
