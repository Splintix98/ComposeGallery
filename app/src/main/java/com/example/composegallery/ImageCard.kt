package com.example.composegallery

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.Size
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.LocalImageLoader
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import com.example.composegallery.storage.ExternalStoragePhoto


@Composable
fun ImageURICard(
    context: Context,
    imageURI: Uri
) {
    Box(
        modifier = Modifier
            .padding(1.dp)
            .width(((getScreenDimensionsInDp(context).x / 3) - 2).dp)
            .height(((getScreenDimensionsInDp(context).x / 3) - 2).dp)
    ) {
        Card(
            shape = RoundedCornerShape(5.dp),
            elevation = 5.dp,
            modifier = Modifier
                .fillMaxSize()
        ) {
            Image(
                painter = rememberImagePainter(
                    data = getThumbnailBitmapFromUri(context, imageURI),
                    imageLoader = LocalImageLoader.current
                ),
                contentDescription = "",
                modifier = Modifier
                    .fillMaxSize()
                    .padding(1.dp)
                    .fillMaxWidth(),
                contentScale = ContentScale.Crop
            )
        }
    }
}


@Composable
fun ImageCard(
    context: Context,
    photo: ExternalStoragePhoto
) {
    Box(
        modifier = Modifier
            .padding(1.dp)
            .width(((getScreenDimensionsInDp(context).x / 3) - 2).dp)
            .height(((getScreenDimensionsInDp(context).x / 3) - 2).dp)
    ) {
        Card(
            shape = RoundedCornerShape(5.dp),
            elevation = 5.dp,
            modifier = Modifier
                .fillMaxSize()
        ) {
            Image(
                painter = rememberImagePainter(
                    data = getThumbnailBitmapFromUri(context, photo.contentUri),
                    imageLoader = LocalImageLoader.current
                ),
                contentDescription = "",
                modifier = Modifier
                    .fillMaxSize()
                    .padding(1.dp)
                    .fillMaxWidth(),
                contentScale = ContentScale.Crop
            )
        }
    }
}


fun getThumbnailBitmapFromUri(
    context: Context,
    uri: Uri
): Bitmap {
    return context.contentResolver.loadThumbnail(uri, Size(300, 300), null)
}
