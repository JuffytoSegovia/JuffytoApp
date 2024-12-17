package com.juffyto.juffyto.ui.screens.calculator

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

enum class CalculatorScreen {
    MAIN,           // Pantalla principal con las dos opciones
    PRESELECTION,   // Calculadora de preselección
    SELECTION       // Calculadora de selección (para futura implementación)
}

class CalculatorViewModel : ViewModel() {
    private val _currentScreen = MutableStateFlow(CalculatorScreen.MAIN)
    val currentScreen: StateFlow<CalculatorScreen> = _currentScreen.asStateFlow()

    fun showMain() {
        _currentScreen.value = CalculatorScreen.MAIN
    }

    fun showPreselection() {
        _currentScreen.value = CalculatorScreen.PRESELECTION
    }

    fun showSelection() {
        _currentScreen.value = CalculatorScreen.SELECTION
    }
}