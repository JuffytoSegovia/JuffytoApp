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
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.juffyto.juffyto.data.notifications.NotificationWorker
import com.juffyto.juffyto.data.notifications.NotificationWorker.Companion.WORK_NAME
import com.juffyto.juffyto.navigation.Screen
import com.juffyto.juffyto.ui.screens.SplashScreen
import com.juffyto.juffyto.ui.screens.chronogram.ChronogramScreen
import com.juffyto.juffyto.ui.screens.chronogram.components.ChronogramViewModel
import com.juffyto.juffyto.ui.screens.menu.MenuScreen
import com.juffyto.juffyto.ui.screens.settings.SettingsViewModel
import com.juffyto.juffyto.ui.theme.JuffytoTheme
import com.juffyto.juffyto.utils.DateUtils
import com.juffyto.juffyto.utils.NotificationSettingsHelper
import java.time.LocalTime

class MainActivity : ComponentActivity() {

    companion object {
        private const val POST_NOTIFICATIONS_PERMISSION = "android.permission.POST_NOTIFICATIONS"
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            Log.d("Permissions", "Notification permission granted")
            testNotification()
        } else {
            Log.d("Permissions", "Notification permission denied")
            showSettingsDialog()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            checkNotificationPermission()
        }

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
                showRationaleDialog()
            }
            else -> {
                requestPermissionLauncher.launch(POST_NOTIFICATIONS_PERMISSION)
            }
        }
    }

    private fun showRationaleDialog() {
        android.app.AlertDialog.Builder(this)
            .setTitle("Notificaciones importantes")
            .setMessage("Para mantenerte informado sobre las fechas importantes de Beca 18, necesitamos tu permiso para enviar notificaciones. ¿Deseas habilitarlas?")
            .setPositiveButton("Sí") { _, _ ->
                requestPermissionLauncher.launch(POST_NOTIFICATIONS_PERMISSION)
            }
            .setNegativeButton("Ahora no", null)
            .show()
    }

    private fun showSettingsDialog() {
        android.app.AlertDialog.Builder(this)
            .setTitle("Notificaciones desactivadas")
            .setMessage("Puedes habilitar las notificaciones en cualquier momento desde la configuración de la aplicación")
            .setPositiveButton("Ir a Configuración") { _, _ ->
                NotificationSettingsHelper.openNotificationSettings(this)
            }
            .setNegativeButton("Más tarde", null)
            .show()
    }

    private fun testNotification() {
        val testTime = LocalTime.now().plusSeconds(10)
        NotificationWorker.schedule(
            context = this,
            title = "Prueba de Notificación",
            message = "Esta es una notificación de prueba",
            notificationTime = testTime
        )

        WorkManager.getInstance(this)
            .getWorkInfosForUniqueWorkLiveData(WORK_NAME)
            .observe(this) { workInfoList: List<WorkInfo>? ->
                workInfoList?.forEach { workInfo ->
                    Log.d("WorkManagerTest", "Estado del trabajo: ${workInfo.state.name}")
                }
            }
    }

    override fun onStop() {
        super.onStop()
        DateUtils.resetToRealTime()
    }

    override fun onDestroy() {
        super.onDestroy()
        DateUtils.resetToRealTime()
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
}