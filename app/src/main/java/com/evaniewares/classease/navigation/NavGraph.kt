package com.evaniewares.classease.navigation

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.evaniewares.classease.presentation.StudentViewModel
import com.evaniewares.classease.screens.AccountScreen
import com.evaniewares.classease.screens.HomeScreen
import com.evaniewares.classease.screens.ProgressScreen
import com.evaniewares.classease.screens.ScoringScreen
import com.evaniewares.classease.screens.SettingScreen
import com.evaniewares.classease.screens.StudentScreen

@Composable
fun NavGraph(
    studentViewModel: StudentViewModel,
    navController: NavHostController,
    windowSize: WindowWidthSizeClass
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(navController)
        }
        composable(Screen.Students.route) {
            StudentScreen(
                studentViewModel = studentViewModel,
                navController = navController
            )
        }
        composable(Screen.Account.route){
            AccountScreen(navController = navController)
        }
        composable(Screen.Settings.route){
            SettingScreen(navController = navController)
        }
        composable(Screen.Progress.route){
            ProgressScreen(
                navController = navController,
                windowSize = windowSize,
                studentViewModel = studentViewModel
            )
        }
        composable(Screen.Scoring.route){
            ScoringScreen(
                navController = navController,
                studentViewModel = studentViewModel
            )
        }
    }
}