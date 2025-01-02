package com.juffyto.juffyto.ui.screens.calculator.selection

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.selectionDataStore by preferencesDataStore("selection_data")

class SelectionStorage(context: Context) {
    private val dataStore = context.selectionDataStore

    // Keys para los datos
    private object SelectionKeys {
        val NOMBRE = stringPreferencesKey("nombre")
        val MODALIDAD = stringPreferencesKey("modalidad")
        val PUNTAJE_PRESELECCION = stringPreferencesKey("puntaje_preseleccion")
        val REGION_IES = stringPreferencesKey("region_ies")
        val TIPOS_IES = stringSetPreferencesKey("tipos_ies")
        val GESTIONES_IES = stringSetPreferencesKey("gestiones_ies")
        val IES_SELECCIONADA = stringPreferencesKey("ies_seleccionada")
        val CURRENT_STEP = intPreferencesKey("current_step")
    }

    // Data class para los datos
    data class SelectionData(
        val nombre: String = "",
        val modalidad: String = "",
        val puntajePreseleccion: String = "",
        val regionIES: String = "",
        val tiposIES: Set<String> = emptySet(),
        val gestionesIES: Set<String> = emptySet(),
        val iesSeleccionada: String = "",
        val currentStep: Int = 1
    )

    // Guardar datos
    suspend fun saveData(data: SelectionData) {
        dataStore.edit { preferences ->
            preferences[SelectionKeys.NOMBRE] = data.nombre
            preferences[SelectionKeys.MODALIDAD] = data.modalidad
            preferences[SelectionKeys.PUNTAJE_PRESELECCION] = data.puntajePreseleccion
            preferences[SelectionKeys.REGION_IES] = data.regionIES
            preferences[SelectionKeys.TIPOS_IES] = data.tiposIES
            preferences[SelectionKeys.GESTIONES_IES] = data.gestionesIES
            preferences[SelectionKeys.IES_SELECCIONADA] = data.iesSeleccionada
            preferences[SelectionKeys.CURRENT_STEP] = data.currentStep
        }
    }

    // Obtener datos
    val getData: Flow<SelectionData> = dataStore.data.map { preferences ->
        SelectionData(
            nombre = preferences[SelectionKeys.NOMBRE] ?: "",
            modalidad = preferences[SelectionKeys.MODALIDAD] ?: "",
            puntajePreseleccion = preferences[SelectionKeys.PUNTAJE_PRESELECCION] ?: "",
            regionIES = preferences[SelectionKeys.REGION_IES] ?: "",
            tiposIES = preferences[SelectionKeys.TIPOS_IES] ?: emptySet(),
            gestionesIES = preferences[SelectionKeys.GESTIONES_IES] ?: emptySet(),
            iesSeleccionada = preferences[SelectionKeys.IES_SELECCIONADA] ?: "",
            currentStep = preferences[SelectionKeys.CURRENT_STEP] ?: 1
        )
    }

    // Limpiar datos
    suspend fun clearData() {
        dataStore.edit { it.clear() }
    }
}
