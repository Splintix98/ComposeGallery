package com.example.composegallery

import android.Manifest
import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.provider.MediaStore
import android.util.Size
import android.widget.Gallery
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
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
                    val mContentResolver = context.contentResolver
                    val thumbnail: Bitmap = mContentResolver.loadThumbnail(it.contentUri, Size(300, 300), null)
                    ImageCard(bitmap = thumbnail, title = it.name, contentDescription = it.name)

                    /*Card(
                        modifier = Modifier
                            .width(300.dp)
                    ) {
                        Column() {
                            Text(text = "Image: ${it.name}")

                            val mContentResolver = context.contentResolver
                            val thumbnail: Bitmap = mContentResolver.loadThumbnail(it.contentUri, Size(300, 300), null)
                            Image(
                                bitmap = thumbnail.asImageBitmap(),
                                contentDescription = "some useful description",
                            )
                        }



                        *//*val thumbnail: Bitmap = if ((true)) {
                            mContentResolver.loadThumbnail(it.contentUri, Size(100, 100), null)
                        } else {
                            MediaStore.Images.Thumbnails.getThumbnail(mContentResolver, id, MediaStore.Images.Thumbnails.MINI_KIND, null)
                        }*//*

                        *//*Card() {
                            GlideImage(
//                                imageModel = it.contentUri,
                                imageModel = Image(bitmap = context.contentResolver.loadThumbnail(it.contentUri, Size(100,100), null), contentDescription = ),
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
                                contentScale = ContentScale.Fit,
//                                placeHolder = ImageBitmap.imageResource(R.drawable.cbr650f_1)
                            )
                        }*//*
                    }*/
                }
            }
        }
    }
}


@Composable
fun ImageCard(
    bitmap: Bitmap,
    title: String,
    contentDescription: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = Modifier
            .fillMaxWidth(0.5f)
            .padding(16.dp)
    ) {

        Card(
            modifier = modifier.fillMaxWidth(),
            shape = RoundedCornerShape(15.dp),
            elevation = 5.dp
        ) {
            Box(modifier = Modifier.height(200.dp)) {
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = contentDescription,
                    contentScale = ContentScale.Crop
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color.Black
                                ),
                                startY = 300f
                            )
                        )
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(12.dp),
                    contentAlignment = Alignment.BottomStart
                ) {
                    Text(text = title, style = TextStyle(color = Color.White, fontSize = 16.sp))
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