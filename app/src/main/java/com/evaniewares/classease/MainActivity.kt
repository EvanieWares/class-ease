package com.evaniewares.classease

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.navigation.compose.rememberNavController
import com.evaniewares.classease.navigation.HomeScreenRoutes
import com.evaniewares.classease.navigation.RootNavGraph
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
                val currentRoute = rememberSaveable {
                    mutableStateOf(HomeScreenRoutes.Home.route)
                }
                RootNavGraph(
                    navController = navController,
                    studentViewModel = studentViewModel
                )

                /*val showNavigationRail = windowSize.widthSizeClass != WindowWidthSizeClass.Compact

                val navItems = listOf(
                    NavItem(
                        title = "Home",
                        route = HomeScreen.Home.route,
                        selectedIcon = Icons.Filled.Home,
                        unSelectedIcon = Icons.Outlined.Home
                    ),
                    NavItem(
                        title = "Students",
                        route = HomeScreen.Students.route,
                        selectedIcon = Icons.Filled.Person,
                        unSelectedIcon = Icons.Outlined.Person
                    ),
                    NavItem(
                        title = "Scoring",
                        route = HomeScreen.Scoring.route,
                        selectedIcon = Icons.Filled.Update,
                        unSelectedIcon = Icons.Outlined.Update
                    ),
                    NavItem(
                        title = "Progress",
                        route = HomeScreen.Progress.route,
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
                        if (currentRoute.value == HomeScreen.Home.route) {
                            this.finishAffinity()
                        } else {
                            navController.navigate(HomeScreen.Home.route)
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
                }*/
            }
        }
    }
}

const val ROOM_DATABASE = "ROOM_DATABASE"
