package com.evaniewares.classease.navigation

import android.widget.Toast
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.evaniewares.classease.presentation.StudentViewModel
import com.evaniewares.classease.screens.AccountScreen
import com.evaniewares.classease.screens.AddEditStudentScreen
import com.evaniewares.classease.screens.HomeScreen
import com.evaniewares.classease.screens.NotificationScreen
import com.evaniewares.classease.screens.ProgressScreen
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
            val studentList = studentViewModel.studentList.collectAsStateWithLifecycle().value
            HomeScreen(
                navController = navController,
                studentList = studentList
            )
        }
        composable(route = HomeScreenRoutes.Students.route) {
            StudentScreen(
                navController = navController,
                studentViewModel = studentViewModel
            )
        }
        composable(route = HomeScreenRoutes.Scoring.route) {
            ScoringScreen(
                studentViewModel = studentViewModel
            )
        }
        composable(route = HomeScreenRoutes.Progress.route) {
            ProgressScreen(
                studentViewModel = studentViewModel
            )
        }
        composable(route = HomeScreenRoutes.Settings.route) {
            val context = LocalContext.current
            SettingScreen(
                navController = navController,
                onClearScores = {
                    studentViewModel.clearAllScores { success ->
                        if (success){
                            Toast.makeText(context, "Scores cleared!", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context, "Failed!", Toast.LENGTH_SHORT).show()
                        }
                    }
                },
                onClearAll = {
                    studentViewModel.deleteAllStudents { success ->
                        if (success){
                            Toast.makeText(context, "All data cleared!", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context, "Failed!", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            )
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
