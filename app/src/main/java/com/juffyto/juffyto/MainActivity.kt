package com.juffyto.juffyto

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import com.juffyto.juffyto.ui.screens.SplashScreen
import com.juffyto.juffyto.ui.theme.JuffytoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JuffytoTheme {
                var showSplash by remember { mutableStateOf(true) }

                if (showSplash) {
                    SplashScreen(
                        onSplashFinished = {
                            showSplash = false
                        }
                    )
                } else {
                    // Aquí irá la navegación principal del app
                    // Por ahora solo mostraremos un placeholder
                }
            }
        }
    }
}