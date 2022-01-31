package com.example.composegallery

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.LocalImageLoader
import coil.compose.rememberImagePainter
import com.example.composegallery.storage.ExternalStoragePhoto
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.skydoves.landscapist.glide.GlideImage
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.ArrowBackIos
import androidx.compose.material.icons.outlined.ArrowForwardIos
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Share
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.graphics.Color
import com.example.composegallery.storage.StorageUtils
import com.example.composegallery.storage.StorageUtils.Companion.getPhotoByIndex
import com.example.composegallery.storage.StorageUtils.Companion.getPhotosSize


@Destination
@Composable
fun Image(
    index: Int,
) {
    // var photo = remember { mutableStateOf<ExternalStoragePhoto>(getPhotoByIndex(index)) }
    val photos = StorageUtils.getPhotos()
    var index = remember { mutableStateOf<Int>(index) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = photos[index.value].name) }
            )
        },
        bottomBar = {
            BottomAppBar(
                backgroundColor = Color.Transparent,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    IconButton(onClick = { previousPicture(index) }) {
                        Icon(Icons.Filled.ArrowBackIos, "", tint = Color.White)
                    }
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(Icons.Filled.Share, "", tint = Color.White)
                    }
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(Icons.Filled.Delete, "", tint = Color.White)
                    }
                    IconButton(onClick = { nextPicture(index) }) {
                        Icon(Icons.Filled.ArrowForwardIos, "", tint = Color.White)
                    }
                }

            }
        },
        content = {
            Card(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color.Black),
                backgroundColor = Color.Black
            ) {
                /*GlideImage(
                    imageModel = photo.contentUri,
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
                )*/
                Image(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(1.dp)
                        .fillMaxWidth(),
                    painter = rememberImagePainter(
                        data = photos[index.value].contentUri,
                        imageLoader = LocalImageLoader.current
                    ),
                    contentDescription = "",
                    contentScale = ContentScale.Fit
                )
            }
        },
    )
}


fun nextPicture(
    index: MutableState<Int>
) {
    if (index.value + 1 < getPhotosSize()) {
        index.value++
    }
}

fun previousPicture(
    index: MutableState<Int>
) {
    if (index.value - 1 >= 0) {
        index.value--
    }
}


@Destination
@Composable
fun ImageURI(
    imageURI: Uri
) {
    Image(
        painter = rememberImagePainter(
            data = imageURI,
            imageLoader = LocalImageLoader.current
        ),
        contentDescription = "",
        modifier = Modifier
            .fillMaxSize()
            .padding(1.dp)
            .fillMaxWidth(),
        contentScale = ContentScale.Fit
    )
}