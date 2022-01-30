package com.example.composegallery.storage

import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList

class StorageUtils {

    companion object {

        private val imageUris = mutableStateListOf<Uri>()

        private val queryOrder = "DESC"


        fun getImageUris(): SnapshotStateList<Uri> {
            return imageUris
        }


        fun fetchImageUris(
            context: Context
        ) {
            imageUris.clear()

            val columns = arrayOf(MediaStore.Images.Media._ID)
            val orderBy = MediaStore.Images.Media.DATE_TAKEN

            val imageCursor: Cursor? = context.contentResolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                columns,
                null,
                null,
                "$orderBy $queryOrder"
            )

            val columnIndex = imageCursor!!.getColumnIndex(MediaStore.Images.Media._ID)

            while (imageCursor.moveToNext()) {
                val id = imageCursor.getLong(columnIndex)
                val imageUri =
                    ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
                imageUris.add(imageUri)
            }

            imageCursor.close()
        }
    }
}