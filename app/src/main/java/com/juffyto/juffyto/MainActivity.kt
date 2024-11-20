package com.juffyto.juffyto

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.juffyto.juffyto.navigation.Screen
import com.juffyto.juffyto.ui.screens.SplashScreen
import com.juffyto.juffyto.ui.screens.chronogram.ChronogramScreen
import com.juffyto.juffyto.ui.screens.chronogram.components.ChronogramViewModel
import com.juffyto.juffyto.ui.screens.menu.MenuScreen
import com.juffyto.juffyto.ui.screens.settings.SettingsViewModel
import com.juffyto.juffyto.ui.theme.JuffytoTheme
import com.juffyto.juffyto.utils.DateUtils

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Asegurar que empezamos desde cero
        DateUtils.resetToRealTime()

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
                            },
                            onBackPressed = {
                                showExitConfirmationDialog()
                            }
                        )
                    }

                    composable(Screen.Chronogram.route) {
                        val chronogramViewModel: ChronogramViewModel = viewModel(
                            factory = ViewModelProvider.AndroidViewModelFactory.getInstance(application)
                        )
                        val settingsViewModel: SettingsViewModel = viewModel(
                            factory = ViewModelProvider.AndroidViewModelFactory.getInstance(application)
                        )
                        ChronogramScreen(
                            viewModel = chronogramViewModel,
                            settingsViewModel = settingsViewModel,
                            onBackClick = { navController.navigateUp() },
                            onSettingsClick = { /* Ya no se usa */ }
                        )
                    }
                }
            }
        }
    }

    override fun onStop() {
        super.onStop()
        // Resetear cuando la app va a background
        DateUtils.resetToRealTime()
    }

    override fun onDestroy() {
        super.onDestroy()
        // Resetear cuando la app se destruye
        DateUtils.resetToRealTime()
    }

    private fun showExitConfirmationDialog() {
        val builder = android.app.AlertDialog.Builder(this)
        builder.apply {
            setTitle("Salir")
            setMessage("¿Deseas salir de la aplicación?")
            setPositiveButton("Sí") { _, _ ->
                finish()
            }
            setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            create()
            show()
        }
    }
}