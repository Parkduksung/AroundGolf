package com.rsupport.rv.viewer.sdk.jni

import android.content.Context
import com.rsupport.rv.viewer.sdk.decorder.util.OpenForTesting

@OpenForTesting
class RC45 {

    fun load() = try {
        System.loadLibrary("rc45")
        true
    } catch (unsatisfiedLinkError: UnsatisfiedLinkError) {
        unsatisfiedLinkError.printStackTrace()
        false
    } catch (exception: Exception) {
        exception.printStackTrace()
        false
    }

    external fun rc45_d2n(data: ByteArray, length: Int): Int
    external fun rc45_scb(obj: Any, context: Context): Boolean
}