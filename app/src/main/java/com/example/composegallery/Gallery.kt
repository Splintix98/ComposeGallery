package com.example.composegallery

import android.Manifest
import android.content.Context
import android.widget.Gallery
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.example.composegallery.storage.ExternalStoragePhoto
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun Gallery(
    permissionsList: SnapshotStateList<String>,
    permissionsNeeded: MutableList<String>,
    context: Context
) {
    val photos = remember { mutableStateListOf<ExternalStoragePhoto>() }


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

                        Card() {
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