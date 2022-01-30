package com.example.composegallery

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat


fun getNeededPermissions(context: Context): MutableList<String> {
    val permissionsOwned = mutableListOf<String>()

    // checking for read permissions
    val hasReadPermission = ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.READ_EXTERNAL_STORAGE
    ) == PackageManager.PERMISSION_GRANTED
    if (hasReadPermission) permissionsOwned.add(Manifest.permission.READ_EXTERNAL_STORAGE)

    // checking for write permissions
    // not neccessary on API 29 and above
    val minSdk29 = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
    val hasWritePermission = (ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    ) == PackageManager.PERMISSION_GRANTED || minSdk29)
    if (hasWritePermission) permissionsOwned.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)


    val permissionsToRequest = mutableListOf<String>()
    if (!hasWritePermission) {
        permissionsToRequest.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    }
    if (!hasReadPermission) {
        permissionsToRequest.add(Manifest.permission.READ_EXTERNAL_STORAGE)
    }

    return permissionsToRequest
}


