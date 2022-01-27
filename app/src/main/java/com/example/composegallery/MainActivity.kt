package com.example.composegallery

import android.Manifest
import android.content.ContentUris
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.lifecycleScope
import com.example.composegallery.storage.ExternalStoragePhoto
import com.example.composegallery.ui.theme.ComposeGalleryTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {
    var externalPhotosMutable by mutableStateOf(listOf<ExternalStoragePhoto>())


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val takePhotoToExternal =
            registerForActivityResult(ActivityResultContracts.TakePicturePreview()) {
                if (it != null) {
                    loadPhotosFromExternalStorageIntoVariable(this, externalPhotosMutable)
                }
            }


        setContent {
            ComposeGalleryTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    // MyApp()
                    Column {
                        Button(onClick = {
                            // loadPhotosFromExternalStorageIntoVariable(context, photos)
                            takePhotoToExternal.launch()
                        }) {
                            Text(text = "Get Photos")
                        }

                        LazyColumn {
                            items(items = externalPhotosMutable) { photo ->
                                Card {
                                    Text(text = "Placeholder for an image.")
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun loadPhotosFromExternalStorageIntoVariable(
        context: Context,
        photosList: List<ExternalStoragePhoto>

    ) {
        GlobalScope.launch {
            val photosTemp = loadPhotosFromExternalStorage(context)
            externalPhotosMutable = photosTemp
            Log.d("externalPhotos", "${photosList.size}")
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
}


@Composable
fun MyApp() {
    val context = LocalContext.current
    val permissionsList = remember { mutableStateListOf<String>() }
    val permissionsNeeded = getNeededPermissions(context)
    val photos = remember { mutableStateListOf<ExternalStoragePhoto>() }


    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { resultPermissions ->
        if (resultPermissions[Manifest.permission.READ_EXTERNAL_STORAGE] != null) {
            addIfNotExists(permissionsList, Manifest.permission.READ_EXTERNAL_STORAGE)
        }

        if (resultPermissions[Manifest.permission.WRITE_EXTERNAL_STORAGE] != null) {
            addIfNotExists(permissionsList, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
    }

    val takePhotoToExternal =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) {
            if (it != null) {
                loadPhotosFromExternalStorageIntoVariable(context, photos)
            }
        }

    Scaffold() {
        Column {
            Button(onClick = {
                launcher.launch(permissionsNeeded.toTypedArray())
            }) {
                Text(text = "Get Permissions")
            }

            Button(onClick = {
//                loadPhotosFromExternalStorageIntoVariable(context, photos)
                takePhotoToExternal.launch()
            }) {
                Text(text = "Get Photos")
            }

            Text(text = "Permissions granted: ${listToString(permissionsList)}")

            LazyColumn {
                items(items = photos) { photo ->
                    Card {
                        Text(text = "Placeholder for an image.")
                    }
                }
            }
        }
    }
}

private fun loadPhotosFromExternalStorageIntoVariable(
    context: Context,
    photosList: SnapshotStateList<ExternalStoragePhoto>
) {
    GlobalScope.launch {
        val photosTemp = loadPhotosFromExternalStorage(context, photosList)
        photosList.addAll(photosTemp)
        Log.d("externalPhotos", "${photosList.size}")
        // externalPhotosMutable.value = externalPhotosMutable +
    }
}

private suspend fun loadPhotosFromExternalStorage(
    context: Context,
    photosList: SnapshotStateList<ExternalStoragePhoto>
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

inline fun <T> sdk29AndUp(onSdk29: () -> T): T? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        onSdk29()
    } else null
}
