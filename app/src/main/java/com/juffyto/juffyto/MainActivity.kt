package com.juffyto.juffyto

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.juffyto.juffyto.navigation.Screen
import com.juffyto.juffyto.ui.screens.SplashScreen
import com.juffyto.juffyto.ui.screens.chronogram.ChronogramScreen
import com.juffyto.juffyto.ui.screens.menu.MenuScreen
import com.juffyto.juffyto.ui.theme.JuffytoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JuffytoTheme {
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = Screen.Splash.route
                ) {
                    composable(Screen.Splash.route) {
                        SplashScreen(
                            onSplashFinished = {
                                navController.navigate(Screen.Menu.route) {
                                    popUpTo(Screen.Splash.route) { inclusive = true }
                                }
                            }
                        )
                    }

                    composable(Screen.Menu.route) {
                        MenuScreen(
                            onNavigateToChronogram = {
                                navController.navigate(Screen.Chronogram.route)
                            }
                        )
                    }

                    composable(Screen.Chronogram.route) {
                        ChronogramScreen(
                            onBackClick = { navController.navigateUp() },
                            onSettingsClick = { /* Implementaremos después */ }
                        )
                    }
                }
            }
        }
    }
}