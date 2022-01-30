package com.example.composegallery

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.Image
import androidx.compose.ui.graphics.asImageBitmap
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.composegallery.storage.StorageUtils
import com.google.accompanist.coil.rememberCoilPainter
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.skydoves.landscapist.glide.GlideImage


@Destination()
@Composable
fun GalleryGrid(
    context: Context,
    navigator: DestinationsNavigator,
) {
    val imagesList = StorageUtils.getImageUris()
    val scaffoldState = rememberScaffoldState()

    if (imagesList.isEmpty()) {
        Text(text = "No pictures were found on your device.")
    } else {
        Scaffold(
            scaffoldState = scaffoldState,
            topBar = {
                TopAppBar(
                    title = { Text(text = "Compose Gallery") }
                )
            },
            content = { paddingValues ->
                Box(
                    modifier = Modifier.padding(paddingValues)
                ) {
                    ImagesGrid(
                        photos = imagesList,
                        navigator = navigator,
                        context = context
                    )
                }
            }
        )
    }
}


@OptIn(ExperimentalFoundationApi::class)
private
@Composable
fun ImagesGrid(
    photos: SnapshotStateList<Uri>,
    navigator: DestinationsNavigator,
    context: Context
) {
    LazyVerticalGrid(cells = GridCells.Adaptive(minSize = 120.dp)) {
        items(photos) { photoUri ->
            Box(
                modifier = Modifier
                    .clickable {
                        Toast
                            .makeText(context, photoUri.toString(), Toast.LENGTH_SHORT)
                            .show()
                        /*navigator.navigate(
                            ImageDestination(
                                Photo(
                                    contentUri = photo.contentUri,
                                    height = photo.height,
                                    width = photo.width,
                                    id = photo.id,
                                    name = photo.name,
                                )
                            )
                        )*/
                    }
            ) {
                GlideImage(
                    imageModel = Image(
                        bitmap = getBitmapFromUri(context, photoUri).asImageBitmap(),
                        contentDescription = ""
                    ),
                    loading = {
                        ConstraintLayout(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            val indicator = createRef()
                            CircularProgressIndicator(
                                modifier = Modifier.constrainAs(indicator) {
                                    top.linkTo(parent.top)
                                    bottom.linkTo(parent.bottom)
                                    start.linkTo(parent.start)
                                    end.linkTo(parent.end)
                                }
                            )
                        }
                    },
                    contentScale = ContentScale.FillWidth
                )

                /*Image(
                    painter = rememberCoilPainter(
                        request = photoUri
                    ),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(1.dp)
                        .clickable {
                            *//*navController.navigate(
                                route = Screens.FullImage_ShowFullImage + "/${photos[index]}"
                            )*//*
                        }
                        .fillMaxWidth(),
                    contentScale = ContentScale.Crop
                )*/
            }
        }
    }
}


/*
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
            */
/*PermissionButton(
                permissionsList = permissionsList,
                permissionsNeeded = permissionsNeeded,
                context = context,
                photos = photos,
            )*//*


            RefreshPhotosButton(context = context, photos = photos)

            Text(text = "Permissions granted: ${listToString(permissionsList)}")

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn {
                items(items = photos) { photo ->
                    ImageCard(
                        context = context,
                        photo = photo,
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
}*/
