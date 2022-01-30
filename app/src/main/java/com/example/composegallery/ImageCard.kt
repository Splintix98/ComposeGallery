package com.example.composegallery

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Parcelable
import android.util.Size
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.LocalImageLoader
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import com.example.composegallery.destinations.ImageDestination
import com.example.composegallery.storage.ExternalStoragePhoto
import com.google.accompanist.coil.rememberCoilPainter
import com.google.accompanist.imageloading.LoadPainterDefaults
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.parcelize.Parcelize


@Composable
fun ImageCard(
    context: Context,
    photo: ExternalStoragePhoto,
    modifier: Modifier = Modifier,
    navigator: DestinationsNavigator
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
            modifier = modifier
                .fillMaxSize()
                .clickable {
                    // Toast.makeText(context, photo.name, Toast.LENGTH_SHORT).show()
                    navigator.navigate(
                        ImageDestination(photo)
                    )
                }
        ) {
            Image(
                painter = rememberImagePainter(
                    data = photo.contentUri,
                    imageLoader = LocalImageLoader.current,
                    builder = {
                        if (false == true) this.crossfade(LoadPainterDefaults.FadeInTransitionDuration)
                        placeholder(0)
                    }
                ),
                contentDescription = "",
                modifier = Modifier
                    .fillMaxSize()
                    .padding(1.dp)
                    .fillMaxWidth(),
                contentScale = ContentScale.Crop
            )
            /*GlideImage(
                contentScale = ContentScale.Crop,
                imageModel = Image(
                    bitmap = getThumbnailBitmapFromUri(
                        context,
                        photo.contentUri
                    ).asImageBitmap(),
                    contentDescription = photo.name
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

            )*/
        }
    }
}


/*
fun getThumbnailBitmapFromUri(
    context: Context,
    uri: Uri
): Bitmap {
    return context.contentResolver.loadThumbnail(uri, Size(200, 200), null)
}*/
