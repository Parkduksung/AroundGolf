package com.rsupport.rv.viewer.sdk.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Just pair of Host and Port
 */
@Parcelize
class HostPort(val host: String, val port: Int) : Parcelable{

    override fun toString(): String {
        return "HostPort(host='$host', port=$port)"
    }
}