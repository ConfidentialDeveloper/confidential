package com.confidential.utils

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat


fun Context.checkPermission(permission: String) : Boolean {
    val check = ContextCompat.checkSelfPermission(this,permission)
    return check==PackageManager.PERMISSION_GRANTED
}