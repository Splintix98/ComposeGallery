package com.example.composegallery

import android.Manifest
import android.content.Context
import android.graphics.Bitmap
import android.util.Size
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.composegallery.storage.ExternalStoragePhoto
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator


@Destination()
@Composable
fun Gallery(
    permissionsList: SnapshotStateList<String>,
    permissionsNeeded: MutableList<String>,
    context: Context,
    navigator: DestinationsNavigator
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
                    val mContentResolver = context.contentResolver
                    val thumbnail: Bitmap =
                        mContentResolver.loadThumbnail(it.contentUri, Size(300, 300), null)

                    ImageCard(
                        context = context,
                        bitmap = thumbnail,
                        title = it.name,
                        contentDescription = it.name,
                        navigator = navigator
                    )
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