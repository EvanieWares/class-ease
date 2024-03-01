package com.evaniewares.classease.presentation

import androidx.lifecycle.ViewModel
import com.evaniewares.classease.utils.SubjectType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

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

    fun onIdChange(score: String) {
        _scoreState.update {
            it.copy(
                studentId = score
            )
        }
    }

    fun onExpandStateChange(expanded: Boolean){
        _scoreState.update {
            it.copy(
                isLearningAreaExpanded = expanded
            )
        }
    }

    fun onSubjectChange(subject: SubjectType){
        _scoreState.update {
            it.copy(
                subject = subject
            )
        }
    }

    data class ScoreState(
        val studentId: String = "",
        val score: String = "",
        val isLearningAreaExpanded: Boolean = false,
        val subject: SubjectType = SubjectType.ENGLISH
    )
}