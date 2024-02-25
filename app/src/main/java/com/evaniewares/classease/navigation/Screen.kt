package com.evaniewares.classease.navigation

sealed class Screen(val route: String) {
    data object Home: Screen(route = "home")
    data object Students: Screen(route = "students")
    data object Progress: Screen(route = "progress")
    data object Scoring: Screen(route = "update")
    data object Settings: Screen(route = "settings")
    data object Account: Screen(route = "account")
}