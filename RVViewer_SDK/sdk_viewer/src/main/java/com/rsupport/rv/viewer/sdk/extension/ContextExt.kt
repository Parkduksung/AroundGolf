package com.rsupport.rv.viewer.sdk.extension

import android.content.Context
import android.os.Build
import android.view.Display
import android.view.WindowManager
import androidx.annotation.ColorRes

fun Context.getColorRes(@ColorRes colorResId: Int) : Int {
    return if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
        getColor(colorResId)
    }else {
        resources.getColor(colorResId)
    }
}

fun Context.getDefaultDisplay(): Display{
    return (getSystemService(Context.WINDOW_SERVICE) as WindowManager).getDefaultDisplay()
}