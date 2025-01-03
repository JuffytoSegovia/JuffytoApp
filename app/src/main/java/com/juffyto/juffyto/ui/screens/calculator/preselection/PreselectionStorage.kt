package com.juffyto.juffyto.ui.screens.calculator.preselection

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.juffyto.juffyto.ui.screens.calculator.preselection.model.ActividadExtracurricular
import com.juffyto.juffyto.ui.screens.calculator.preselection.model.CondicionPriorizable
import com.juffyto.juffyto.ui.screens.calculator.preselection.model.PreselectionState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.preselectionDataStore by preferencesDataStore("preselection_preferences")

class PreselectionStorage(context: Context) {
    private val dataStore = context.preselectionDataStore

    private object PreferenceKeys {
        val NOMBRE = stringPreferencesKey("nombre")
        val MODALIDAD = stringPreferencesKey("modalidad")
        val PUNTAJE_ENP = stringPreferencesKey("puntaje_enp")
        val CLASIFICACION_SISFOH = stringPreferencesKey("clasificacion_sisfoh")
        val DEPARTAMENTO = stringPreferencesKey("departamento")
        val LENGUA_ORIGINARIA = stringPreferencesKey("lengua_originaria")
        val ACTIVIDADES = stringSetPreferencesKey("actividades")
        val CONDICIONES = stringSetPreferencesKey("condiciones")
        val CURRENT_STEP = intPreferencesKey("current_step")
        val PUNTAJE_TOTAL = intPreferencesKey("puntaje_total")
        val DESGLOSE_PUNTAJE = stringPreferencesKey("desglose_puntaje")
    }

    val getData: Flow<PreselectionState> = dataStore.data.map { preferences ->
        PreselectionState(
            nombre = preferences[PreferenceKeys.NOMBRE] ?: "",
            modalidad = preferences[PreferenceKeys.MODALIDAD] ?: "",
            puntajeENP = preferences[PreferenceKeys.PUNTAJE_ENP] ?: "",
            clasificacionSISFOH = preferences[PreferenceKeys.CLASIFICACION_SISFOH] ?: "",
            departamento = preferences[PreferenceKeys.DEPARTAMENTO] ?: "",
            lenguaOriginaria = preferences[PreferenceKeys.LENGUA_ORIGINARIA] ?: "",
            actividadesExtracurriculares = (preferences[PreferenceKeys.ACTIVIDADES] ?: emptySet())
                .mapNotNull {
                    try { ActividadExtracurricular.valueOf(it) }
                    catch (_: IllegalArgumentException) { null }
                }.toSet(),
            condicionesPriorizables = (preferences[PreferenceKeys.CONDICIONES] ?: emptySet())
                .mapNotNull {
                    try { CondicionPriorizable.valueOf(it) }
                    catch (_: IllegalArgumentException) { null }
                }.toSet(),
            currentStep = preferences[PreferenceKeys.CURRENT_STEP] ?: 1,
            puntajeTotal = preferences[PreferenceKeys.PUNTAJE_TOTAL],
            desglosePuntaje = preferences[PreferenceKeys.DESGLOSE_PUNTAJE] ?: "",
            mostrarLenguaOriginaria = preferences[PreferenceKeys.MODALIDAD] == "EIB"
        )
    }

    suspend fun saveData(state: PreselectionState) {
        dataStore.edit { preferences ->
            preferences[PreferenceKeys.NOMBRE] = state.nombre
            preferences[PreferenceKeys.MODALIDAD] = state.modalidad
            preferences[PreferenceKeys.PUNTAJE_ENP] = state.puntajeENP
            preferences[PreferenceKeys.CLASIFICACION_SISFOH] = state.clasificacionSISFOH
            preferences[PreferenceKeys.DEPARTAMENTO] = state.departamento
            preferences[PreferenceKeys.LENGUA_ORIGINARIA] = state.lenguaOriginaria
            preferences[PreferenceKeys.ACTIVIDADES] = state.actividadesExtracurriculares
                .map { it.name }
                .toSet()
            preferences[PreferenceKeys.CONDICIONES] = state.condicionesPriorizables
                .map { it.name }
                .toSet()
            preferences[PreferenceKeys.CURRENT_STEP] = state.currentStep
            state.puntajeTotal?.let { preferences[PreferenceKeys.PUNTAJE_TOTAL] = it }
            preferences[PreferenceKeys.DESGLOSE_PUNTAJE] = state.desglosePuntaje
        }
    }

    suspend fun clearData() {
        dataStore.edit { it.clear() }
    }
}