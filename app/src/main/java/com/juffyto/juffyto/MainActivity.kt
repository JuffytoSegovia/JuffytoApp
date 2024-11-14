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
import com.juffyto.juffyto.ui.theme.JuffytoTheme
import com.juffyto.juffyto.data.preferences.TestModePreferences
import com.juffyto.juffyto.utils.DateUtils
import kotlinx.coroutines.runBlocking

class MainActivity : ComponentActivity() {
    private lateinit var testModePreferences: TestModePreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        testModePreferences = TestModePreferences(this)
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
                        val viewModel: ChronogramViewModel = viewModel(
                            factory = ViewModelProvider.AndroidViewModelFactory.getInstance(application)
                        )
                        ChronogramScreen(
                            viewModel = viewModel,
                            onBackClick = { navController.navigateUp() },
                            onSettingsClick = { /* Implementaremos después */ }
                        )
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Resetear el modo de prueba cuando se cierra la app
        runBlocking {
            testModePreferences.setTestModeEnabled(false)
            DateUtils.ensureRealTimeMode()
        }
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