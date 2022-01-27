package com.example.composegallery

import android.Manifest
import android.content.ContentUris
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.composegallery.storage.ExternalStoragePhoto
import com.example.composegallery.ui.theme.ComposeGalleryTheme
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ComposeGalleryTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    MyApp()
                }
            }
        }
    }
}


@Composable
fun MyApp() {
    val context = LocalContext.current
    val permissionsList = remember { mutableStateListOf<String>() }
    val permissionsNeeded = getNeededPermissions(context)

    Gallery(
        permissionsList = permissionsList,
        permissionsNeeded = permissionsNeeded,
        context = context
    )
}


/*@Composable
fun <I, O> registerForActivityResult(
    contract: ActivityResultContract<I, O>,
    onResult: (O) -> Unit
): ActivityResultLauncher<I> {
    // First, find the ActivityResultRegistry by casting the Context
    // (which is actually a ComponentActivity) to ActivityResultRegistryOwner
    val owner = LocalContext.current as ActivityResultRegistryOwner
    val activityResultRegistry = owner.activityResultRegistry

    // Tracking current onResult listener
    val currentOnResult = rememberUpdatedState(onResult)

    // Only need to be unique and consistent across configuration changes.
    val key = remember { UUID.randomUUID().toString() }

    val realLauncher = remember<ActivityResultLauncher<I>> {
        activityResultRegistry.register(key, contract) {
            currentOnResult.value(it)
        }
    }

    DisposableEffect(activityResultRegistry, key, contract) {
        onDispose {
            realLauncher.unregister()
        }
    }

    return realLauncher
}*/