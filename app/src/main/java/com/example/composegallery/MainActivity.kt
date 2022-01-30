package com.example.composegallery

import android.Manifest
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.Navigation
import com.example.composegallery.ui.theme.ComposeGalleryTheme
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /*setContent {
            ComposeGalleryTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    NavigationMain()
                }
            }
        }*/
    }

    override fun onResume() {
        super.onResume()

        val neededPermissions = getNeededPermissions(this)

        if(neededPermissions.isEmpty()) {
            setContent {
                // get Photos

                // Navigate to Gallery
                NavigationMain()
            }
        }
        // not all permissions are still available
        else {
            setContent{
                val context = LocalContext.current
                val intent = Intent(context, PermissionActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                context.startActivity(intent)
            }
        }
    }
}


@Composable
fun NavigationMain() {
    DestinationsNavHost(navGraph = NavGraphs.root)
}


@Destination(start = true)
@Composable
fun MyApp(
    navigator: DestinationsNavigator
) {
    val context = LocalContext.current
    val permissionsList = remember { mutableStateListOf<String>() }
    val permissionsNeeded = getNeededPermissions(context)

    Gallery(
        permissionsList = permissionsList,
        permissionsNeeded = permissionsNeeded,
        context = context,
        navigator = navigator,
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