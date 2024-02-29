package com.evaniewares.classease

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.navigation.compose.rememberNavController
import com.evaniewares.classease.navigation.NavGraph
import com.evaniewares.classease.presentation.StudentViewModel
import com.evaniewares.classease.ui.theme.ClassEaseTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val studentViewModel by viewModels<StudentViewModel>()

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ClassEaseTheme {
                val navController = rememberNavController()
                val windowSize = calculateWindowSizeClass(activity = this)
                NavGraph(
                    studentViewModel = studentViewModel,
                    navController = navController,
                    windowSize = windowSize.widthSizeClass
                )
            }
        }
    }
}

const val ROOM_DATABASE = "ROOM_DATABASE"
