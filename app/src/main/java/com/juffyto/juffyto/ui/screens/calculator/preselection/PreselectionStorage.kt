package com.juffyto.juffyto.ui.screens.calculator.preselection

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.preselectionDataStore by preferencesDataStore("preselection_data")

class PreselectionStorage(context: Context) {
    private val dataStore = context.preselectionDataStore

    // Keys para los datos
    private object PreselectionKeys {
        val NOMBRE = stringPreferencesKey("nombre")
        val MODALIDAD = stringPreferencesKey("modalidad")
        val PUNTAJE_ENP = stringPreferencesKey("puntaje_enp")
        val SISFOH = stringPreferencesKey("sisfoh")
        val DEPARTAMENTO = stringPreferencesKey("departamento")
        val LENGUA_ORIGINARIA = stringPreferencesKey("lengua_originaria")
        val ACTIVIDADES = stringSetPreferencesKey("actividades")
        val CONDICIONES = stringSetPreferencesKey("condiciones")
    }

    // Guardar datos
    suspend fun saveData(data: PreselectionData) {
        dataStore.edit { preferences ->
            preferences[PreselectionKeys.NOMBRE] = data.nombre
            preferences[PreselectionKeys.MODALIDAD] = data.modalidad
            preferences[PreselectionKeys.PUNTAJE_ENP] = data.puntajeENP
            preferences[PreselectionKeys.SISFOH] = data.sisfoh
            preferences[PreselectionKeys.DEPARTAMENTO] = data.departamento
            preferences[PreselectionKeys.LENGUA_ORIGINARIA] = data.lenguaOriginaria
            preferences[PreselectionKeys.ACTIVIDADES] = data.actividades
            preferences[PreselectionKeys.CONDICIONES] = data.condiciones
        }
    }

    // Obtener datos
    val getData: Flow<PreselectionData> = dataStore.data.map { preferences ->
        PreselectionData(
            nombre = preferences[PreselectionKeys.NOMBRE] ?: "",
            modalidad = preferences[PreselectionKeys.MODALIDAD] ?: "",
            puntajeENP = preferences[PreselectionKeys.PUNTAJE_ENP] ?: "",
            sisfoh = preferences[PreselectionKeys.SISFOH] ?: "",
            departamento = preferences[PreselectionKeys.DEPARTAMENTO] ?: "",
            lenguaOriginaria = preferences[PreselectionKeys.LENGUA_ORIGINARIA] ?: "",
            actividades = preferences[PreselectionKeys.ACTIVIDADES] ?: emptySet(),
            condiciones = preferences[PreselectionKeys.CONDICIONES] ?: emptySet()
        )
    }

    // Limpiar datos
    suspend fun clearData() {
        dataStore.edit { it.clear() }
    }
}

// Data class para manejar los datos
data class PreselectionData(
    val nombre: String = "",
    val modalidad: String = "",
    val puntajeENP: String = "",
    val sisfoh: String = "",
    val departamento: String = "",
    val lenguaOriginaria: String = "",
    val actividades: Set<String> = emptySet(),
    val condiciones: Set<String> = emptySet()
)