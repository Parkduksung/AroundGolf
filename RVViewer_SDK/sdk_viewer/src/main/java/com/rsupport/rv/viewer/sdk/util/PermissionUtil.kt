package com.rsupport.rv.viewer.sdk.util

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import com.rsupport.rv.viewer.sdk.common.log.RLog


class PermissionUtil {
    companion object {
        val PERMISSION_UTIL_REQUEST = 1
    }

    val requirePermissions: Array<String>
    var requestPermissions = ArrayList<String>()

    constructor(vararg permissions:String) {
        requirePermissions = arrayOf(*permissions)
    }

    fun hasAllPermissions(context: Context): Boolean {
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true
        }else {
            requestPermissions.clear()

            for (perm in requirePermissions) {
                if (context.checkSelfPermission(perm) != PackageManager.PERMISSION_GRANTED) {
                    RLog.i("No permission: $perm")
                    requestPermissions.add(perm)
                }
            }

            return (requestPermissions.size == 0)
        }
    }

    fun requestPermission(activity: AppCompatActivity) {
        if(requestPermissions.size == 0) {
            RLog.w("No more require permissions or not call hasAllPermissions()")
            return
        }

        for(perm in requestPermissions) {
            if(activity.shouldShowRequestPermissionRationale(perm)) {
                RLog.d("Should show rationale of permission: $perm")
            }
        }

        activity.requestPermissions(
                requestPermissions.toArray(arrayOfNulls<String>(requestPermissions.size)),
                PERMISSION_UTIL_REQUEST
        )
    }

    fun checkPermissionResult(requestCode: Int, permissions: Array<String>, grantResults:IntArray): Boolean {
        if(requestCode == PERMISSION_UTIL_REQUEST) {
            if(permissions.size == grantResults.size) {
                RLog.i("Permission result check...")

                var allGranted = true
                for(i in 0..(permissions.size - 1)) {
                    RLog.i("Permission ${permissions[i]} -> ${grantResults[i]}")
                    allGranted = allGranted.and(grantResults[i] == PackageManager.PERMISSION_GRANTED)
                }

                return allGranted
            }else {
                RLog.w("Permission array and result array size not matches. $permissions / $grantResults")
            }
        }else {
            RLog.w("Not PermissionUtil request")
        }

        return false
    }

}