package com.example.composegallery.storage

import android.graphics.Bitmap
import android.net.Uri

data class ExternalStoragePhoto(
    val id: Long,
    val name: String,
    val width: Int,
    val height: Int,
    val contentUri: Uri,
)

data class BitmapPhoto(
    val name: String,
    val bitmap: Bitmap,
)