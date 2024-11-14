package com.juffyto.juffyto.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "test_mode_settings")

class TestModePreferences(private val context: Context) {
    private val testModeKey = booleanPreferencesKey("test_mode_enabled")

    val testModeEnabled: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[testModeKey] ?: false
        }

    suspend fun setTestModeEnabled(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[testModeKey] = enabled
        }
    }
}