package com.juffyto.juffyto.ui.screens.enp

import androidx.lifecycle.ViewModel
import com.juffyto.juffyto.ui.screens.enp.model.EnpQuestion
import com.juffyto.juffyto.ui.screens.enp.model.EnpQuestions
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class EnpViewModel : ViewModel() {
    private val _currentQuestionIndex = MutableStateFlow(0)
    val currentQuestionIndex: StateFlow<Int> = _currentQuestionIndex.asStateFlow()

    private val _showSolution = MutableStateFlow(false)
    val showSolution: StateFlow<Boolean> = _showSolution.asStateFlow()

    private val _userAnswers = MutableStateFlow<MutableMap<Int, Int>>(mutableMapOf())
    val userAnswers: StateFlow<Map<Int, Int>> = _userAnswers.asStateFlow()

    val questions: List<EnpQuestion> = EnpQuestions.questions

    val currentQuestion: EnpQuestion
        get() = questions[currentQuestionIndex.value]

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
        _showSolution.value = !_showSolution.value
    }

    fun selectAnswer(optionIndex: Int) {
        _userAnswers.value = _userAnswers.value.toMutableMap().apply {
            put(_currentQuestionIndex.value, optionIndex)
        }
    }

    fun isAnswerCorrect(questionIndex: Int): Boolean? {
        return _userAnswers.value[questionIndex]?.let { selectedAnswer ->
            selectedAnswer == questions[questionIndex].correctOptionIndex
        }
    }

    fun isCurrentQuestionAnswered(): Boolean {
        return _userAnswers.value.containsKey(_currentQuestionIndex.value)
    }

    fun canMoveToNext(): Boolean {
        return _currentQuestionIndex.value < questions.size - 1
    }

    fun canMoveToPrevious(): Boolean {
        return _currentQuestionIndex.value > 0
    }

    fun getCurrentProgress(): Float {
        return (_currentQuestionIndex.value + 1).toFloat() / questions.size
    }

    fun resetQuestion() {
        _showSolution.value = false
        _userAnswers.value = _userAnswers.value.toMutableMap().apply {
            remove(_currentQuestionIndex.value)
        }
    }
}