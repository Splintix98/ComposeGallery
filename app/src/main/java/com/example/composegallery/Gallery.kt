package com.example.composegallery

import android.content.Context
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
import com.example.composegallery.destinations.ImageDestination
import com.example.composegallery.storage.ExternalStoragePhoto
import com.example.composegallery.storage.StorageUtils
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.skydoves.landscapist.glide.GlideImage


@Destination()
@Composable
fun GalleryGrid(
    context: Context,
    navigator: DestinationsNavigator,
) {
    val photos = StorageUtils.getPhotos()
    val scaffoldState = rememberScaffoldState()

    if (photos.isEmpty()) {
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
                        photos = photos,
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
    photos: SnapshotStateList<ExternalStoragePhoto>,
    navigator: DestinationsNavigator,
    context: Context
) {
    LazyVerticalGrid(
        cells = GridCells.Adaptive(minSize = 120.dp)
    ) {
        items(photos) { photo ->
            Box(
                modifier = Modifier
                    .clickable {
                        Toast
                            .makeText(context, photo.contentUri.toString(), Toast.LENGTH_SHORT)
                            .show()
                        navigator.navigate(
                            ImageDestination(photo)
                        )
                    }
            ) {
                ImageCard(context = context, photo = photo, navigator = navigator)
            /*GlideImage(
                    imageModel = Image(
                        bitmap = getThumbnailBitmapFromUri(
                            context,
                            photo.contentUri
                        ).asImageBitmap(),
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
                )*/
            }
        }
    }
}
