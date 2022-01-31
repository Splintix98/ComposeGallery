package com.example.composegallery.storage

import android.graphics.Bitmap
import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.text.SimpleDateFormat
import java.util.*

@Parcelize
data class ExternalStoragePhoto(
    val id: Long,
    val name: String,
    val width: Int,
    val height: Int,
    val contentUri: Uri,
    // val dateTaken: String,   // SimpleDateFormat("yyyy:MM:dd hh:MM:SS", Locale.US).parse(cursor.getString(dateTakenColumn))
) : Parcelable

data class BitmapPhoto(
    val name: String,
    val bitmap: Bitmap,
)