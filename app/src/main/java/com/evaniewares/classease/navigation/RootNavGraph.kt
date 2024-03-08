package com.evaniewares.classease.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.evaniewares.classease.presentation.StudentViewModel
import com.evaniewares.classease.utils.Graph

@Composable
fun RootNavGraph(
    navController: NavHostController,
    studentViewModel: StudentViewModel
) {
    NavHost(
        navController = navController,
        route = Graph.ROOT,
        startDestination = Graph.HOME
    ) {
        composable(route = Graph.HOME) {
            HomeActivity(studentViewModel = studentViewModel)
        }
    }
}
