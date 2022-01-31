package com.example.composegallery.storage

import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.example.composegallery.sdk29AndUp
import java.text.SimpleDateFormat
import java.util.*

class StorageUtils {

    companion object {

        private val imageUris = mutableStateListOf<Uri>()
        private val photos = mutableStateListOf<ExternalStoragePhoto>()

        private val queryOrder = "DESC"


        fun getImageUris(): SnapshotStateList<Uri> {
            return imageUris
        }


        fun getPhotos(): SnapshotStateList<ExternalStoragePhoto> {
            return photos
        }


        fun fetchPhotos(
            context: Context
        ) {
            val collection = sdk29AndUp {
                MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
            } ?: MediaStore.Images.Media.EXTERNAL_CONTENT_URI

            val columns = arrayOf(
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.WIDTH,
                MediaStore.Images.Media.HEIGHT,
//                MediaStore.Images.Media.DATE_TAKEN,
            )

            context.contentResolver.query(
                collection,
                columns,
                null,
                null,
                "${MediaStore.Images.Media.DISPLAY_NAME} ASC"
            )?.use { cursor ->
                val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
                val displayNameColumn =
                    cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
                val widthColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.WIDTH)
                val heightColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.HEIGHT)
//                val dateTakenColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_TAKEN)


                while (cursor.moveToNext()) {
                    val id = cursor.getLong(idColumn)
                    val displayName = cursor.getString(displayNameColumn)
                    val width = cursor.getInt(widthColumn)
                    val height = cursor.getInt(heightColumn)
//                    val dateTaken = cursor.getString(dateTakenColumn)

                    val contentUri = ContentUris.withAppendedId(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        id
                    )

                    photos.add(
                        ExternalStoragePhoto(
                            id = id,
                            name = displayName,
                            width = width,
                            height = height,
                            contentUri = contentUri,
                            // dateTaken = dateTaken
                        )
                    )
                }
            }
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