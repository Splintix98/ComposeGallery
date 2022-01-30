package com.example.composegallery

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.composegallery.ui.theme.ComposeGalleryTheme

class PermissionActivity : ComponentActivity() {

    override fun onResume() {
        super.onResume()

        val neededPermissions = getNeededPermissions(this)
        Log.d("Needed Permissions: ", neededPermissions.toString())

        if (neededPermissions.isEmpty()) {
            navigateHome()
        } else {
            /**
             * In Android < 6.0 must be granted during installation.
             * As Android < 6.0 is basically not present anymore, I will not put a lot of effort
             * into handling that case.
             */
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                setContent { PermissionsScreen() }
            } else {
                setContent {
                    Text(
                        text = "In Android Version 6.0 and before, permissions have to " +
                                "be granted during installation. You seem to have failed to do that." +
                                "Please reconsider and grant the permissions."
                    )
                }
            }
        }
    }


    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
        finish()
    }


    fun navigateHome() {
        setContent {
            val context = LocalContext.current
            val intent = Intent(context, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }
    }


    @Composable
    fun PermissionsScreen() {
        val context = LocalContext.current

        val permissionLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestMultiplePermissions()
        ) { resultPermissions ->
            if (resultPermissions[Manifest.permission.READ_EXTERNAL_STORAGE] != null) {
                navigateHome()
            } else {
                setContent {
                    Text(
                        text = "This application requires R/W access to the devices storage " +
                                "to show and delete pictures. \n Please reconsider and grant the permissions."
                    )
                }
            }
        }

        ComposeGalleryTheme {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "This application requires R/W access to the devices storage to show " +
                            "and delete pictures.",
                )

                Button(
                    modifier = Modifier.padding(30.dp),
                    onClick = {
                        val permissionsToRequest = getNeededPermissions(context)
                        Log.d("Needed Permissions", permissionsToRequest.toString())
                        permissionLauncher.launch(permissionsToRequest.toTypedArray())
                    }
                ) {
                    Text(text = "Grant Permissions")
                }

            }
        }
    }
}