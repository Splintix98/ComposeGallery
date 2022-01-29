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
import androidx.compose.runtime.currentCompositionLocalContext
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.composegallery.destinations.ImageDestination
import com.example.composegallery.storage.ExternalStoragePhoto
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.parcelize.Parcelize

@Parcelize
data class Photo(
    val id: Long,
    val name: String,
    val width: Int,
    val height: Int,
    val contentUri: Uri,
) : Parcelable

@Composable
fun ImageCard(
    context: Context,
    photo: ExternalStoragePhoto,
    modifier: Modifier = Modifier,
    navigator: DestinationsNavigator
) {
    Box(
        modifier = Modifier
            .fillMaxWidth(0.5f)
            .padding(16.dp)
    ) {

        Card(
            shape = RoundedCornerShape(15.dp),
            elevation = 5.dp,
            modifier = modifier
                .fillMaxWidth()
                .clickable {
                    Toast
                        .makeText(context, photo.name, Toast.LENGTH_SHORT)
                        .show()
                    navigator.navigate(
                        ImageDestination(
                            Photo(
                                contentUri = photo.contentUri,
                                height = photo.height,
                                width = photo.width,
                                id = photo.id,
                                name = photo.name,
                            )
                        )
                    )
                }
        ) {
            Box(
                modifier = Modifier
                    .height(200.dp)
            ) {
                /*Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = contentDescription,
                    contentScale = ContentScale.Crop
                )*/
                GlideImage(
//                                imageModel = it.contentUri,
                    imageModel = Image(
                        bitmap = getBitmapFromUri(context, photo.contentUri).asImageBitmap(),
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
                    contentScale = ContentScale.FillHeight,
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
                    Text(
                        text = photo.name,
                        style = TextStyle(color = Color.White, fontSize = 16.sp)
                    )
                }
            }
        }
    }
}

fun getBitmapFromUri(
    context: Context,
    uri: Uri
): Bitmap {
    return context.contentResolver.loadThumbnail(uri, Size(300, 300), null)
}