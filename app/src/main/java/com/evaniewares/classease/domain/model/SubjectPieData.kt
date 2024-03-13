package com.evaniewares.classease.domain.model

import androidx.compose.ui.graphics.Color
import com.evaniewares.classease.utils.SubjectType

data class SubjectPieData(
    val title: String,
    val colorFamily: List<Color>,
    val subjectType: SubjectType
)
