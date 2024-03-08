package com.evaniewares.classease.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.evaniewares.classease.navigation.HomeScreenRoutes
import com.evaniewares.classease.ui.theme.BackgroundColor
import com.evaniewares.classease.ui.theme.ClassEaseTheme
import com.evaniewares.classease.ui.theme.TransparentBackgroundColor

/**
 * App's home screen.
 *
 * @param navController the [NavHostController] that will help to go
 * back to the previous page.
 */
@Composable
fun HomeScreen(navController: NavHostController) {
    val scrollState = rememberScrollState()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = BackgroundColor
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
                .clip(RoundedCornerShape(topStartPercent = 10, topEndPercent = 10))
                .background(TransparentBackgroundColor)
                .verticalScroll(scrollState)
        ) {
            ScrollableTopBar(
                onSettingsClick = {
                    navController.navigate(HomeScreenRoutes.Settings.route)
                },
                onAccountClick = {
                    navController.navigate(HomeScreenRoutes.Account.route)
                },
                onNotificationClick = {
                    navController.navigate(HomeScreenRoutes.Notifications.route)
                }
            )
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Home Screen",
                    style = MaterialTheme.typography.titleLarge
                )
            }
        }
    }
}

/**
 * Simple top app bar with icons that navigates to [NotificationScreen], [SettingScreen]
 * and [AccountScreen] respectively
 *
 * @param onSettingsClick handles user click actions on the [Settings] icon
 * @param onAccountClick handles user click actions on the [AccountCircle] icon
 * @param onNotificationClick handles user click actions on the [Notifications] icon
 */
@Composable
private fun ScrollableTopBar(
    onSettingsClick: () -> Unit,
    onAccountClick: () -> Unit,
    onNotificationClick: () -> Unit
) {
    val iconSize = 24.dp

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        contentAlignment = Alignment.CenterEnd
    ) {
        Row(
            modifier = Modifier.padding(10.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onNotificationClick,
                modifier = Modifier.size(iconSize)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Notifications,
                    contentDescription = "Notifications"
                )
            }
            IconButton(
                onClick = onSettingsClick,
                modifier = Modifier.size(iconSize)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Settings,
                    contentDescription = "Settings"
                )
            }
            IconButton(
                onClick = onAccountClick,
                modifier = Modifier.size(iconSize)
            ) {
                Icon(
                    imageVector = Icons.Outlined.AccountCircle,
                    contentDescription = "Account"
                )
            }
        }
    }
}

@Preview(
    //showSystemUi = true
)
@Composable
fun HomePreview() {
    ClassEaseTheme(darkTheme = false) {
        HomeScreen(rememberNavController())
    }
}
