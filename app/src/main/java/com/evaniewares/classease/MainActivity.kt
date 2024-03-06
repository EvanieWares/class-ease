package com.evaniewares.classease

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.viewModels
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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.evaniewares.classease.domain.model.NavItem
import com.evaniewares.classease.navigation.NavGraph
import com.evaniewares.classease.navigation.Screen
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
                    mutableStateOf(Screen.Home.route)
                }

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
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold(
                        bottomBar = {
                            NavigationBar {
                                navItems.forEach { navItem ->
                                    NavigationBarItem(
                                        selected = currentRoute.value == navItem.route,
                                        onClick = {
                                            currentRoute.value = navItem.route
                                            navController.navigate(navItem.route)
                                        },
                                        icon = {
                                            Icon(
                                                imageVector = if (currentRoute.value == navItem.route) {
                                                    navItem.selectedIcon
                                                } else {
                                                    navItem.unSelectedIcon
                                                },
                                                contentDescription = navItem.title
                                            )
                                        },
                                        label = {
                                            Text(text = navItem.title)
                                        }
                                    )
                                }
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
                }
                BackHandler(enabled = true) {
                    if (currentRoute.value == Screen.Home.route) {
                        finishAffinity()
                    } else {
                        navController.navigate(Screen.Home.route)
                    }
                }
            }
        }
    }
}

const val ROOM_DATABASE = "ROOM_DATABASE"
