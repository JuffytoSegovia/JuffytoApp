package com.juffyto.juffyto.data.preferences

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.juffyto.juffyto.data.model.NotificationFrequency
import com.juffyto.juffyto.data.model.NotificationSettings
import kotlinx.coroutines.flow.map

private val Context.settingsDataStore by preferencesDataStore(name = "settings")

class SettingsPreferences(private val context: Context) {
    private val enabled = booleanPreferencesKey("notifications_enabled")
    private val time = stringPreferencesKey("notification_time")
    private val frequency = stringPreferencesKey("notification_frequency")

    val settings = context.settingsDataStore.data.map { preferences ->
        NotificationSettings(
            enabled = preferences[enabled] ?: true,
            notificationTime = preferences[time] ?: "09:00",
            notificationFrequency = NotificationFrequency.valueOf(
                preferences[frequency] ?: NotificationFrequency.DAILY.name
            )
        )
    }

    suspend fun updateSettings(settings: NotificationSettings) {
        context.settingsDataStore.edit { preferences ->
            preferences[enabled] = settings.enabled
            preferences[time] = settings.notificationTime
            preferences[frequency] = settings.notificationFrequency.name
        }
    }
}