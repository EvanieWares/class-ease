package com.evaniewares.classease.domain.model

import androidx.compose.ui.graphics.Color

data class PieChartItem(
    val color: Color,
    val value: Int,
    val description: String,
    val isTapped: Boolean
)
