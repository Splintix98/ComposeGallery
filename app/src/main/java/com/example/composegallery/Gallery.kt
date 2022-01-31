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
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import com.example.composegallery.destinations.ImageDestination
import com.example.composegallery.destinations.ImageURIDestination
import com.example.composegallery.storage.ExternalStoragePhoto
import com.example.composegallery.storage.StorageUtils
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator


@Destination()
@Composable
fun GalleryGrid(
    context: Context,
    navigator: DestinationsNavigator,
) {
    val photos = StorageUtils.getPhotos()
//    val imageURIs = StorageUtils.getImageUris()

    val scaffoldState = rememberScaffoldState()

    if (photos.isEmpty() /*|| imageURIs.isEmpty()*/) {
        Column {
            Text(text = "No pictures were found on your device.")
            Text(text = "Photos: ${photos.size}")
//            Text(text = "ImageURIs: ${imageURIs.size}")
        }

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
                    ImagesGridExternalStoragePhotos(
                        photos = photos,
//                    ImagesGridImageURIs(
//                        imageURIs = imageURIs,
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
fun ImagesGridExternalStoragePhotos(
    photos: SnapshotStateList<ExternalStoragePhoto>,
    navigator: DestinationsNavigator,
    context: Context
) {
    LazyVerticalGrid(
        cells = GridCells.Adaptive(minSize = 120.dp)
    ) {
        items(photos.size) { index ->
            Box(
                modifier = Modifier
                    .clickable {
                        /*Toast
                            .makeText(context, photo.contentUri.toString(), Toast.LENGTH_SHORT)
                            .show()*/
                        navigator.navigate(
                            ImageDestination(index = index)
                        )
                    }
            ) {
                ImageCard(context = context, photo = photos[index])
            }
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ImagesGridImageURIs(
    imageURIs: SnapshotStateList<Uri>,
    navigator: DestinationsNavigator,
    context: Context,
) {
    LazyVerticalGrid(
        cells = GridCells.Adaptive(minSize = 120.dp)
    ) {
        items(imageURIs) { imageURI ->
            Box(
                modifier = Modifier
                    .clickable {
                        /*Toast
                            .makeText(context, imageURI.toString(), Toast.LENGTH_SHORT)
                            .show()*/
                        navigator.navigate(
                            ImageURIDestination(imageURI)
                        )
                    }
            ) {
                ImageURICard(context = context, imageURI = imageURI)
            }
        }
    }
}



