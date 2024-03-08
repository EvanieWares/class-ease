package com.evaniewares.classease.navigation

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.evaniewares.classease.domain.model.HomeBottomBarItem
import com.evaniewares.classease.presentation.StudentViewModel
import com.evaniewares.classease.ui.theme.AppColor
import com.evaniewares.classease.ui.theme.ClassEaseTheme

/**
 * The app's main landing screen.
 *
 * @param navController the [NavHostController] that will help navigating
 * to different screens.
 */
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeActivity(
    studentViewModel: StudentViewModel,
    navController: NavHostController = rememberNavController()
) {
    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController = navController)
        }
    ) {
        HomeNavGraph(
            navController = navController,
            studentViewModel = studentViewModel
        )
    }
}

/**
 * Uses [NavigationBar] to offer a persistent and convenient way to switch between primary
 * destinations in an app.
 *
 * @param navController the [NavHostController] to help navigating through different screens
 */
@Composable
private fun BottomNavigationBar(
    navController: NavHostController
) {
    val screens = listOf(
        HomeBottomBarItem.Home,
        HomeBottomBarItem.Students,
        HomeBottomBarItem.Scoring,
        HomeBottomBarItem.Progress
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val showBottomBar = screens.any { it.route == currentDestination?.route }

    if (showBottomBar) {
        NavigationBar {
            screens.forEach { screen ->
                AddNavItem(
                    screen = screen,
                    navController = navController,
                    currentDestination = currentDestination
                )
            }
        }
    }
}

/**
 * Adds a [NavigationBarItem] to the [BottomNavigationBar]
 *
 * @param screen the [HomeBottomBarItem] to add to the [BottomNavigationBar]
 * @param navController the [NavHostController] to help navigating to the [screen]
 * @param currentDestination the current screen
 */
@Composable
private fun RowScope.AddNavItem(
    screen: HomeBottomBarItem,
    navController: NavHostController,
    currentDestination: NavDestination?
) {
    val isSelected = currentDestination?.hierarchy?.any {
        it.route == screen.route
    } == true

    NavigationBarItem(
        selected = isSelected,
        onClick = {
            navController.navigate(screen.route) {
                popUpTo(navController.graph.findStartDestination().id)
                launchSingleTop = true
            }
        },
        icon = {
            Icon(
                imageVector = if (isSelected) {
                    screen.selectedIcon
                } else {
                    screen.unSelectedIcon
                },
                contentDescription = screen.title,
                tint = if (isSelected) AppColor else LocalContentColor.current
            )
        },
        label = {
            Text(
                text = screen.title,
                color = if (isSelected) MaterialTheme.colorScheme.primary else LocalContentColor.current
            )
        }
    )
}

@Preview(showSystemUi = true)
@Composable
fun HomePreview(){
    ClassEaseTheme {
        //HomeActivity()
    }
}
