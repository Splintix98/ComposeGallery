package com.example.composegallery

import androidx.compose.runtime.Composable
import com.ramcosta.composedestinations.DestinationsNavHost

@Composable
fun NavigationMain() {
    DestinationsNavHost(navGraph = NavGraphs.root)
}