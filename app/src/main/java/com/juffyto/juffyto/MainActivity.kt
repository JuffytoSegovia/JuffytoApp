package com.juffyto.juffyto

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
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
import com.juffyto.juffyto.utils.NotificationSettingsHelper

class MainActivity : ComponentActivity() {

    companion object {
        private const val POST_NOTIFICATIONS_PERMISSION = "android.permission.POST_NOTIFICATIONS"
    }

    private var shouldCheckPermissions = true
    private var hasShownDialog = false

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            Log.d("Permissions", "Notification permission granted")
            showNotificationEnabledConfirmation()
        } else {
            Log.d("Permissions", "Notification permission denied")
            showNotificationsDisabledDialog()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
                                // Verificamos los permisos después del splash
                                if (shouldCheckPermissions && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                    checkNotificationPermission()
                                    shouldCheckPermissions = false
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

    @SuppressLint("PermissionLaunchedDuringComposition")
    private fun checkNotificationPermission() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                POST_NOTIFICATIONS_PERMISSION
            ) == PackageManager.PERMISSION_GRANTED -> {
                Log.d("Permissions", "Notification permission already granted")
                return
            }
            shouldShowRequestPermissionRationale(POST_NOTIFICATIONS_PERMISSION) -> {
                if (!hasShownDialog) {
                    showNotificationsDisabledDialog()
                }
            }
            else -> {
                if (!hasShownDialog) {
                    requestPermissionLauncher.launch(POST_NOTIFICATIONS_PERMISSION)
                }
            }
        }
    }

    private fun showNotificationEnabledConfirmation() {
        android.app.AlertDialog.Builder(this)
            .setTitle("Notificaciones activadas")
            .setMessage("Recibirás notificaciones importantes de Beca 18 acordes a como los configures en la app.")
            .setPositiveButton("Entendido", null)
            .show()
    }

    private fun showNotificationsDisabledDialog() {
        if (!hasShownDialog) {
            android.app.AlertDialog.Builder(this)
                .setTitle("Notificaciones de Beca 18 desactivadas")
                .setMessage("Puedes habilitar las notificaciones de Beca 18 en cualquier momento desde la configuración de la aplicación")
                .setPositiveButton("Ir a Configuración") { _, _ ->
                    NotificationSettingsHelper.openNotificationSettings(this)
                }
                .setNegativeButton("Más tarde", null)
                .show()
            hasShownDialog = true
        }
    }

    private fun showExitConfirmationDialog() {
        android.app.AlertDialog.Builder(this)
            .setTitle("Salir")
            .setMessage("¿Deseas salir de la aplicación?")
            .setPositiveButton("Sí") { _, _ ->
                finish()
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    override fun onStop() {
        super.onStop()
        DateUtils.resetToRealTime()
    }

    override fun onDestroy() {
        super.onDestroy()
        DateUtils.resetToRealTime()
    }
}