package com.evaniewares.classease.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.evaniewares.classease.domain.model.CustomTopBar

@Composable
fun ProgressScreen(
    navController: NavHostController
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            CustomTopBar(
                activity = "Progress",
                onBackClick = { navController.popBackStack() }
            )
        }
    ) { paddingValues ->
        Surface(
            modifier = Modifier.padding(paddingValues)
        ) {}
    }
}

@Preview(showSystemUi = true)
@Composable
fun ProgressPreview(){
    ProgressScreen(navController = rememberNavController())
}
