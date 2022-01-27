package com.example.composegallery

import android.Manifest
import android.content.ContentUris
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.composegallery.storage.ExternalStoragePhoto
import com.example.composegallery.ui.theme.ComposeGalleryTheme
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {
    var externalPhotosMutable by mutableStateOf(listOf<ExternalStoragePhoto>())

    private var readPermissionGranted = false
    private var writePermissionGranted = false
    private lateinit var permissionsLauncher: ActivityResultLauncher<Array<String>>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ComposeGalleryTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    MyApp()
                }
            }
        }
    }


    /*override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        permissionsLauncher =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
                readPermissionGranted =
                    permissions[Manifest.permission.READ_EXTERNAL_STORAGE] ?: readPermissionGranted
                writePermissionGranted = permissions[Manifest.permission.WRITE_EXTERNAL_STORAGE]
                    ?: writePermissionGranted

                if (readPermissionGranted) {
                    loadPhotosFromExternalStorageIntoVariable()
                    Toast.makeText(
                        this,
                        "Can show photos: ${externalPhotosMutable.size}",
                        Toast.LENGTH_SHORT
                    ).show()
                } else Toast.makeText(this, "Can't show photos -> no rights", Toast.LENGTH_SHORT)
                    .show()
            }
        updateOrRequestPermissions()

        loadPhotosFromExternalStorageIntoVariable()


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
                             loadPhotosFromExternalStorageIntoVariable()
                        }) {
                            Text(text = "Get Photos")
                        }

                        LazyColumn {
                            items(items = externalPhotosMutable) { photo ->
                                Card {
                                    Column {
                                        Text(text = "Name: ${photo.name}")
                                        GlideImage(
                                            imageModel = photo.contentUri,
                                            contentScale = ContentScale.Fit,
                                        )
                                    }

                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun loadPhotosFromExternalStorageIntoVariable() {
        lifecycleScope.launch {
            val photosTemp = loadPhotosFromExternalStorage()
            externalPhotosMutable = photosTemp
            Log.d("externalPhotos", "${externalPhotosMutable.size}")
        }
    }

    private suspend fun loadPhotosFromExternalStorage(): List<ExternalStoragePhoto> {
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

            contentResolver.query(
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

    // ==========================================================================================
    // ====================================== PERMISSIONS =======================================
    // ==========================================================================================
    private fun updateOrRequestPermissions() {
        val hasReadPermission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED

        val hasWritePermission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED

        val minSdk29 = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q

        readPermissionGranted = hasReadPermission
        writePermissionGranted = hasWritePermission || minSdk29

        Log.d(
            "Permissions",
            "ReadPermission: ${readPermissionGranted}, WritePermission: ${writePermissionGranted}"
        )


        val permissionsToRequest = mutableListOf<String>()
        if (!writePermissionGranted) {
            permissionsToRequest.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
        if (!readPermissionGranted) {
            permissionsToRequest.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        }

        Log.d("Permissions to request", "${permissionsToRequest.toList()}")
        if (permissionsToRequest.isNotEmpty()) {
            permissionsLauncher.launch(permissionsToRequest.toTypedArray())
        }

//        if (readPermissionGranted) {
//            loadPhotosFromExternalStorageIntoVariable()
//            Toast.makeText(this, "Can show photos: ${externalPhotosMutable.size}", Toast.LENGTH_SHORT)
//                .show()
//        } else Toast.makeText(this, "Can't show photos -> no rights", Toast.LENGTH_SHORT)
//            .show()
    }*/
}


@Composable
fun MyApp() {
    val context = LocalContext.current
    val permissionsList = remember { mutableStateListOf<String>() }
    val permissionsNeeded = getNeededPermissions(context)
    val photos = remember { mutableStateListOf<ExternalStoragePhoto>() }


    // loadPhotosFromExternalStorageIntoVariable(context, photos)


    Scaffold() {
        Column {
            PermissionButton(
                permissionsList = permissionsList,
                permissionsNeeded = permissionsNeeded,
                context = context,
                photos = photos,
            )

            RefreshPhotosButton(context = context, photos = photos)

            Text(text = "Permissions granted: ${listToString(permissionsList)}")

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn {
                items(items = photos) {
                    Card(
                        modifier = Modifier
                            .width(300.dp)
                    ) {
                        Text(text = "Image: ${it.name}")

                        GlideImage(
                            imageModel = it.contentUri,
                            contentScale = ContentScale.Fit,
//                                placeHolder = ImageBitmap.imageResource(R.drawable.cbr650f_1)
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun RefreshPhotosButton(
    context: Context,
    photos: SnapshotStateList<ExternalStoragePhoto>
) {
    val takePhotoToExternal =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) {
            if (it != null) {
                loadPhotosFromExternalStorageIntoVariable(context, photos)
            }
        }

    Button(onClick = {
        takePhotoToExternal.launch()
        loadPhotosFromExternalStorageIntoVariable(context, photos)
    }) {
        Text(text = "Take Photo and refresh")
    }
}


@Composable
fun PermissionButton(
    permissionsList: SnapshotStateList<String>,
    permissionsNeeded: MutableList<String>,
    context: Context,
    photos: SnapshotStateList<ExternalStoragePhoto>
) {
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { resultPermissions ->
        if (resultPermissions[Manifest.permission.READ_EXTERNAL_STORAGE] != null) {
            addIfNotExists(permissionsList, Manifest.permission.READ_EXTERNAL_STORAGE)
        }

        if (resultPermissions[Manifest.permission.WRITE_EXTERNAL_STORAGE] != null) {
            addIfNotExists(permissionsList, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
    }

    Button(onClick = {
        permissionLauncher.launch(permissionsNeeded.toTypedArray())
        loadPhotosFromExternalStorageIntoVariable(context, photos)
    }) {
        Text(text = "Get Permissions")
    }
}


private fun loadPhotosFromExternalStorageIntoVariable(
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


inline fun <T> sdk29AndUp(onSdk29: () -> T): T? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        onSdk29()
    } else null
}


/*@Composable
fun <I, O> registerForActivityResult(
    contract: ActivityResultContract<I, O>,
    onResult: (O) -> Unit
): ActivityResultLauncher<I> {
    // First, find the ActivityResultRegistry by casting the Context
    // (which is actually a ComponentActivity) to ActivityResultRegistryOwner
    val owner = LocalContext.current as ActivityResultRegistryOwner
    val activityResultRegistry = owner.activityResultRegistry

    // Tracking current onResult listener
    val currentOnResult = rememberUpdatedState(onResult)

    // Only need to be unique and consistent across configuration changes.
    val key = remember { UUID.randomUUID().toString() }

    val realLauncher = remember<ActivityResultLauncher<I>> {
        activityResultRegistry.register(key, contract) {
            currentOnResult.value(it)
        }
    }

    DisposableEffect(activityResultRegistry, key, contract) {
        onDispose {
            realLauncher.unregister()
        }
    }

    return realLauncher
}*/