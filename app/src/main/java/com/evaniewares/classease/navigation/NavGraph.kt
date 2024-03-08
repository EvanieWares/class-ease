package com.evaniewares.classease.navigation

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.evaniewares.classease.presentation.StudentViewModel
import com.evaniewares.classease.screens.AccountScreen
import com.evaniewares.classease.screens.HomeScreen
import com.evaniewares.classease.screens.ProgressScreenBackup
import com.evaniewares.classease.screens.ScoringScreen
import com.evaniewares.classease.screens.SettingScreen
import com.evaniewares.classease.screens.StudentScreenBackup

@Composable
fun NavGraph(
    studentViewModel: StudentViewModel,
    navController: NavHostController,
    windowSize: WindowWidthSizeClass
) {
    NavHost(
        navController = navController,
        startDestination = HomeScreenRoutes.Home.route
    ) {
        composable(HomeScreenRoutes.Home.route) {
            HomeScreen(navController)
        }
        composable(HomeScreenRoutes.Students.route) {
            StudentScreenBackup(
                studentViewModel = studentViewModel,
                navController = navController
            )
        }
        composable(HomeScreenRoutes.Account.route){
            AccountScreen(navController = navController)
        }
        composable(HomeScreenRoutes.Settings.route){
            SettingScreen(navController = navController)
        }
        composable(HomeScreenRoutes.Progress.route){
            ProgressScreenBackup(
                navController = navController,
                windowSize = windowSize,
                studentViewModel = studentViewModel
            )
        }
        composable(HomeScreenRoutes.Scoring.route){
            ScoringScreen(
                navController = navController,
                studentViewModel = studentViewModel
            )
        }
    }
}