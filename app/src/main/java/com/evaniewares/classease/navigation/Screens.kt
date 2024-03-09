package com.evaniewares.classease.navigation

sealed class HomeScreenRoutes(val route: String) {
    data object Home: HomeScreenRoutes(route = "home")
    data object Students: HomeScreenRoutes(route = "students")
    data object Progress: HomeScreenRoutes(route = "progress")
    data object Scoring: HomeScreenRoutes(route = "update")
    data object Settings: HomeScreenRoutes(route = "settings")
    data object Account: HomeScreenRoutes(route = "account")
    data object Notifications: HomeScreenRoutes(route = "notifications")
    data object AddEditStudent: HomeScreenRoutes(route = "add_edit_student")
}
