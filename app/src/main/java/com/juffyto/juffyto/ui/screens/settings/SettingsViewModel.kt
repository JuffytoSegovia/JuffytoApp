package com.juffyto.juffyto.ui.screens.settings

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.WorkManager
import com.juffyto.juffyto.data.model.NotificationFrequency
import com.juffyto.juffyto.data.model.NotificationSettings
import com.juffyto.juffyto.data.notifications.NotificationWorker
import com.juffyto.juffyto.data.preferences.SettingsPreferences
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class SettingsViewModel(application: Application) : AndroidViewModel(application) {
    private val settingsPreferences = SettingsPreferences(application)

    val settings: StateFlow<NotificationSettings> = settingsPreferences.settings
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = NotificationSettings(enabled = false)
        )

    fun updateSettings(settings: NotificationSettings) {
        viewModelScope.launch {
            settingsPreferences.updateSettings(settings)
            // Programar notificaciones solo si están habilitadas
            if (settings.enabled) {
                scheduleNotificationsBasedOnSettings(settings)
            } else {
                // Cancelar notificaciones existentes si se deshabilitan
                cancelExistingNotifications()
            }
        }
    }

    private fun scheduleNotificationsBasedOnSettings(settings: NotificationSettings) {
        val context = getApplication<Application>()
        val formatter = DateTimeFormatter.ofPattern("hh:mm a")

        try {
            val notificationTime = LocalTime.parse(settings.notificationTime, formatter)
            NotificationWorker.schedule(
                context = context,
                title = "Beca 18",
                message = getInitialNotificationMessage(settings.notificationFrequency),
                notificationTime = notificationTime
            )
        } catch (e: Exception) {
            Log.e("SettingsViewModel", "Error scheduling notifications", e)
        }
    }

    private fun cancelExistingNotifications() {
        val context = getApplication<Application>()
        WorkManager.getInstance(context).cancelUniqueWork(NotificationWorker.WORK_NAME)
    }

    private fun getInitialNotificationMessage(frequency: NotificationFrequency): String {
        return when (frequency) {
            NotificationFrequency.DAILY -> "Se han activado las notificaciones diarias de Beca 18"
            NotificationFrequency.WEEKLY -> "Se han activado las notificaciones semanales de Beca 18"
            NotificationFrequency.IMPORTANT_ONLY -> "Se han activado las notificaciones para eventos importantes de Beca 18"
        }
    }
}