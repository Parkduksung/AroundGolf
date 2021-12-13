package com.example.aroundgolf

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import com.example.aroundgolf.di.AppKoinSetup
import com.example.aroundgolf.di.KoinBaseKoinSetup

class App : Application() {

    private val appKoinSetup: KoinBaseKoinSetup = AppKoinSetup()

    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        appKoinSetup.setup(this)
        instance = this
//        KakaoSdk.init(this, "{NATIVE_APP_KEY}")
    }

    fun context(): Context = applicationContext

    companion object {
        lateinit var instance: App
            private set
    }


}