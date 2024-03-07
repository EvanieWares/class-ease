package com.evaniewares.classease

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.TableView
import androidx.compose.material.icons.filled.Update
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.TableView
import androidx.compose.material.icons.outlined.Update
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.evaniewares.classease.domain.model.NavItem
import com.evaniewares.classease.navigation.NavGraph
import com.evaniewares.classease.navigation.Screen
import com.evaniewares.classease.presentation.StudentViewModel
import com.evaniewares.classease.ui.theme.ClassEaseTheme
import com.evaniewares.classease.utils.BackButtonHandler
import com.evaniewares.classease.utils.BottomNavigationBar
import com.evaniewares.classease.utils.CustomNavigationRail
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
                val currentRoute = rememberSaveable {
                    mutableStateOf(Screen.Home.route)
                }
                val showNavigationRail = windowSize.widthSizeClass != WindowWidthSizeClass.Compact

                val navItems = listOf(
                    NavItem(
                        title = "Home",
                        route = Screen.Home.route,
                        selectedIcon = Icons.Filled.Home,
                        unSelectedIcon = Icons.Outlined.Home
                    ),
                    NavItem(
                        title = "Students",
                        route = Screen.Students.route,
                        selectedIcon = Icons.Filled.Person,
                        unSelectedIcon = Icons.Outlined.Person
                    ),
                    NavItem(
                        title = "Scoring",
                        route = Screen.Scoring.route,
                        selectedIcon = Icons.Filled.Update,
                        unSelectedIcon = Icons.Outlined.Update
                    ),
                    NavItem(
                        title = "Progress",
                        route = Screen.Progress.route,
                        selectedIcon = Icons.Filled.TableView,
                        unSelectedIcon = Icons.Outlined.TableView
                    )
                )
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = if (showNavigationRail) 80.dp else 0.dp),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold(
                        bottomBar = {
                            AnimatedVisibility(visible = !showNavigationRail) {
                                BottomNavigationBar(
                                    navItems = navItems,
                                    currentRoute = currentRoute.value,
                                    onClick = { route ->
                                        currentRoute.value = route
                                        navController.navigate(route)
                                    }
                                )
                            }
                        }
                    ) { paddingValues ->
                        Column(
                            modifier = Modifier.padding(paddingValues)
                        ) {
                            NavGraph(
                                studentViewModel = studentViewModel,
                                navController = navController,
                                windowSize = windowSize.widthSizeClass
                            )
                        }
                    }
                    BackButtonHandler {
                        if (currentRoute.value == Screen.Home.route) {
                            this.finishAffinity()
                        } else {
                            navController.navigate(Screen.Home.route)
                        }
                    }
                }
                AnimatedVisibility(visible = showNavigationRail) {
                    CustomNavigationRail(
                        navItems = navItems,
                        currentRoute = currentRoute.value,
                        onClick = { route ->
                            currentRoute.value = route
                            navController.navigate(route)
                        }
                    )
                }
            }
        }
    }
}

const val ROOM_DATABASE = "ROOM_DATABASE"
