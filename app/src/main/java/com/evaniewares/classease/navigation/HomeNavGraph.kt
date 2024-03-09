package com.evaniewares.classease.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.evaniewares.classease.presentation.StudentViewModel
import com.evaniewares.classease.screens.AccountScreen
import com.evaniewares.classease.screens.AddEditStudentScreen
import com.evaniewares.classease.screens.HomeScreen
import com.evaniewares.classease.screens.NotificationScreen
import com.evaniewares.classease.screens.ProgressScreen
import com.evaniewares.classease.screens.ProgressScreenBackup
import com.evaniewares.classease.screens.ScoringScreen
import com.evaniewares.classease.screens.SettingScreen
import com.evaniewares.classease.screens.StudentScreen
import com.evaniewares.classease.utils.Graph

@Composable
fun HomeNavGraph(
    navController: NavHostController,
    studentViewModel: StudentViewModel
) {
    NavHost(
        navController = navController,
        route = Graph.HOME,
        startDestination = HomeScreenRoutes.Home.route
    ) {
        composable(route = HomeScreenRoutes.Home.route) {
            HomeScreen(navController)
        }
        composable(route = HomeScreenRoutes.Students.route) {
            StudentScreen(
                navController = navController,
                studentViewModel = studentViewModel
            )
        }
        composable(route = HomeScreenRoutes.Scoring.route) {
            ScoringScreen(
                navController = navController,
                studentViewModel = studentViewModel
            )
        }
        composable(route = HomeScreenRoutes.Progress.route) {
            ProgressScreen(
                navController = navController,
                studentViewModel = studentViewModel
            )
        }
        composable(route = HomeScreenRoutes.Settings.route) {
            SettingScreen(navController = navController)
        }
        composable(route = HomeScreenRoutes.Account.route) {
            AccountScreen(navController = navController)
        }
        composable(route = HomeScreenRoutes.Notifications.route) {
            NotificationScreen(navController = navController)
        }
        composable(route = HomeScreenRoutes.AddEditStudent.route) {
            AddEditStudentScreen(
                navController = navController,
                studentViewModel = studentViewModel
            )
        }
    }
}
