package com.evaniewares.classease.domain.model

import androidx.compose.ui.graphics.vector.ImageVector

data class NavItem(
    val title: String,
    val route: String,
    val selectedIcon: ImageVector,
    val unSelectedIcon: ImageVector
)
