package com.example.composegallery

import android.content.ContentUris
import android.content.Context
import android.provider.MediaStore
import android.util.Log
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.example.composegallery.storage.ExternalStoragePhoto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


fun loadPhotosFromExternalStorageIntoVariable(
    context: Context,
    photos: SnapshotStateList<ExternalStoragePhoto>
) {
//    val lifecycleScope = LifecycleOwner().lifecycleScope
//    lifecycleScope.launch {
    GlobalScope.launch {
        val photosTemp = loadPhotosFromExternalStorage(context)
        photos.addAll(photosTemp)
        Log.d("externalPhotos", "${photos.size}")
    }
}


private suspend fun loadPhotosFromExternalStorage(
    context: Context
): List<ExternalStoragePhoto> {
    return withContext(Dispatchers.IO) {
        val collection = sdk29AndUp {
            MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
        } ?: MediaStore.Images.Media.EXTERNAL_CONTENT_URI

        val projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.WIDTH,
            MediaStore.Images.Media.HEIGHT,
        )

        val photos = mutableListOf<ExternalStoragePhoto>()

        context.contentResolver.query(
            collection,
            projection,
            null,
            null,
            "${MediaStore.Images.Media.DISPLAY_NAME} ASC"
        )?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            val displayNameColumn =
                cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
            val widthColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.WIDTH)
            val heightColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.HEIGHT)


            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val displayName = cursor.getString(displayNameColumn)
                val width = cursor.getInt(widthColumn)
                val height = cursor.getInt(heightColumn)
                val contentUri = ContentUris.withAppendedId(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    id
                )

                photos.add(ExternalStoragePhoto(id, displayName, width, height, contentUri))
            }
            Log.d("photos", "${photos.toList().size}")
            photos.toList()
        } ?: listOf()
    }
}