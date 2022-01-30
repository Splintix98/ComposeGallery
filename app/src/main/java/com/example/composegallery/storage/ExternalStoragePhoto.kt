package com.example.composegallery.storage

import android.graphics.Bitmap
import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ExternalStoragePhoto(
    val id: Long,
    val name: String,
    val width: Int,
    val height: Int,
    val contentUri: Uri,
) : Parcelable

data class BitmapPhoto(
    val name: String,
    val bitmap: Bitmap,
)