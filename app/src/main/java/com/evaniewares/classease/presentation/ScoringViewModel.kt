package com.evaniewares.classease.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ScoringViewModel : ViewModel() {
    private val _scoreState = MutableStateFlow(ScoreState())
    val scoreState: StateFlow<ScoreState> = _scoreState.asStateFlow()

    fun onScoreChange(score: String) {
        _scoreState.update {
            it.copy(
                score = score
            )
        }
    }

    fun onIdChange(score: String, studentName: (Boolean, String) -> Unit) {
        _scoreState.update {
            it.copy(
                score = score
            )
        }
    }

    data class ScoreState(
        val studentId: String = "",
        val score: String = ""
    )
}