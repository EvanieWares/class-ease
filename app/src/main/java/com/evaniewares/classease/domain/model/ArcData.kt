package com.evaniewares.classease.domain.model

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.ui.graphics.Color

data class ArcData(
    val targetSweepAngle: Float,
    val color: Color,
    val animation: Animatable<Float, AnimationVector1D>
)
