package com.evaniewares.classease.domain.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.TableView
import androidx.compose.material.icons.filled.Update
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.TableView
import androidx.compose.material.icons.outlined.Update
import androidx.compose.ui.graphics.vector.ImageVector
import com.evaniewares.classease.navigation.HomeScreenRoutes

sealed class HomeBottomBarItem(
    val title: String,
    val route: String,
    val selectedIcon: ImageVector,
    val unSelectedIcon: ImageVector
) {
    data object Home : HomeBottomBarItem(
        title = "Home",
        route = HomeScreenRoutes.Home.route,
        selectedIcon = Icons.Filled.Home,
        unSelectedIcon = Icons.Outlined.Home
    )

    data object Students : HomeBottomBarItem(
        title = "Students",
        route = HomeScreenRoutes.Students.route,
        selectedIcon = Icons.Filled.Person,
        unSelectedIcon = Icons.Outlined.Person
    )

    data object Scoring : HomeBottomBarItem(
        title = "Scoring",
        route = HomeScreenRoutes.Scoring.route,
        selectedIcon = Icons.Filled.Update,
        unSelectedIcon = Icons.Outlined.Update
    )

    data object Progress : HomeBottomBarItem(
        title = "Progress",
        route = HomeScreenRoutes.Progress.route,
        selectedIcon = Icons.Filled.TableView,
        unSelectedIcon = Icons.Outlined.TableView
    )
}