package com.juffyto.juffyto.ui.screens.enp

import androidx.lifecycle.ViewModel
import com.juffyto.juffyto.ui.screens.enp.model.EnpQuestion
import com.juffyto.juffyto.ui.screens.enp.model.EnpQuestions
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

enum class EnpMode {
    HOME,       // Pantalla principal con botones
    SIMULATION, // Modo examen
    SOLUTIONS,   // Modo solucionario
}

data class EnpState(
    val mode: EnpMode = EnpMode.HOME,
    val simulationCompleted: Boolean = false,
    val score: Int? = null
)

class EnpViewModel : ViewModel() {
    private val _currentQuestionIndex = MutableStateFlow(0)
    val currentQuestionIndex: StateFlow<Int> = _currentQuestionIndex.asStateFlow()

    private val _showSolution = MutableStateFlow(false)
    val showSolution: StateFlow<Boolean> = _showSolution.asStateFlow()

    private val _userAnswers = MutableStateFlow<MutableMap<Int, Int>>(mutableMapOf())
    val userAnswers: StateFlow<Map<Int, Int>> = _userAnswers.asStateFlow()

    private val _enpState = MutableStateFlow(EnpState())
    val enpState: StateFlow<EnpState> = _enpState.asStateFlow()

    private val _simulationAnswers = MutableStateFlow<MutableMap<Int, Int>>(mutableMapOf())
    val simulationAnswers: StateFlow<Map<Int, Int>> = _simulationAnswers.asStateFlow()

    val questions: List<EnpQuestion> = EnpQuestions.questions

    private val _showExitConfirmation = MutableStateFlow(false)
    val showExitConfirmation: StateFlow<Boolean> = _showExitConfirmation.asStateFlow()

    fun showExitConfirmation() {
        _showExitConfirmation.value = true
    }

    fun hideExitConfirmation() {
        _showExitConfirmation.value = false
    }

    fun canNavigateBack(): Boolean {
        return enpState.value.mode != EnpMode.HOME
    }

    fun moveToNextQuestion() {
        if (_currentQuestionIndex.value < questions.size - 1) {
            _currentQuestionIndex.value += 1
            _showSolution.value = false
        }
    }

    fun moveToPreviousQuestion() {
        if (_currentQuestionIndex.value > 0) {
            _currentQuestionIndex.value -= 1
            _showSolution.value = false
        }
    }

    fun toggleSolution() {
        if (_enpState.value.mode == EnpMode.SOLUTIONS) {
            _showSolution.value = !_showSolution.value
        }
    }

    fun selectAnswer(optionIndex: Int) {
        when (_enpState.value.mode) {
            EnpMode.SIMULATION -> {
                _simulationAnswers.value = _simulationAnswers.value.toMutableMap().apply {
                    put(_currentQuestionIndex.value, optionIndex)
                }
            }
            EnpMode.SOLUTIONS -> {
                _userAnswers.value = _userAnswers.value.toMutableMap().apply {
                    put(_currentQuestionIndex.value, optionIndex)
                }
            }
            else -> {} // No hacer nada en modo HOME
        }
    }

    fun canMoveToNext(): Boolean {
        return _currentQuestionIndex.value < questions.size - 1
    }

    fun canMoveToPrevious(): Boolean {
        return _currentQuestionIndex.value > 0
    }

    fun getUnansweredQuestions(): List<Int> {
        return questions.indices.filterNot { _simulationAnswers.value.containsKey(it) }
    }

    fun getCurrentProgress(): Float {
        return (_currentQuestionIndex.value + 1).toFloat() / questions.size
    }

    // Funciones de navegación entre modos
    fun setMode(mode: EnpMode) {
        _enpState.value = _enpState.value.copy(mode = mode)
        when (mode) {
            EnpMode.SIMULATION -> {
                _showSolution.value = false
                _simulationAnswers.value.clear()
            }
            EnpMode.SOLUTIONS -> {
                _showSolution.value = false
                _currentQuestionIndex.value = 0  // Añadir esta línea
            }
            EnpMode.HOME -> {
                _currentQuestionIndex.value = 0
                _showSolution.value = false
            }
        }
    }

    fun calculateScore() {
        val correctAnswers = _simulationAnswers.value.count { (index, answer) ->
            answer == questions[index].correctOptionIndex
        }
        val finalScore = correctAnswers * 2 // 2 puntos por respuesta correcta
        _enpState.value = _enpState.value.copy(
            simulationCompleted = true,
            score = finalScore
        )
    }

    fun getCorrectAnswers(): Int {
        return _simulationAnswers.value.count { (index, answer) ->
            answer == questions[index].correctOptionIndex
        }
    }

    fun resetSimulation() {
        _simulationAnswers.value.clear()
        _currentQuestionIndex.value = 0
        _enpState.value = _enpState.value.copy(
            simulationCompleted = false,
            score = null
        )
    }
}

