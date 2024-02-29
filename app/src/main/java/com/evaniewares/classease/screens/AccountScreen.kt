package com.evaniewares.classease.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.evaniewares.classease.utils.CustomTopBar

@Composable
fun AccountScreen(navController: NavHostController) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            CustomTopBar(
                activity = "Account",
                onBackClick = { navController.popBackStack() }
            )
        }
    ) { paddingValues ->
        Surface(
            modifier = Modifier.padding(paddingValues)
        ) {}
    }
}