package com.juffyto.juffyto.ui.screens.settings

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.juffyto.juffyto.data.model.NotificationSettings
import com.juffyto.juffyto.data.preferences.SettingsPreferences
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(application: Application) : AndroidViewModel(application) {
    private val settingsPreferences = SettingsPreferences(application)

    val settings: StateFlow<NotificationSettings> = settingsPreferences.settings
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = NotificationSettings()
        )

    fun updateSettings(settings: NotificationSettings) {
        viewModelScope.launch {
            settingsPreferences.updateSettings(settings)
        }
    }
}