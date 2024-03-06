package com.evaniewares.classease.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.evaniewares.classease.R
import com.evaniewares.classease.navigation.Screen
import com.evaniewares.classease.ui.theme.ClassEaseTheme

@Composable
fun HomeScreen(
    navController: NavHostController
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            HomeTopBar()
        }
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            color = Color.Gray
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxWidth()
            ) {
                item {
                    Column(
                        modifier = Modifier.padding(top = 2.dp),
                        verticalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        NavScreen(
                            iconRes = R.drawable.ic_students,
                            name = "Students",
                            onMenuItemClick = { navController.navigate(Screen.Students.route) },
                            route = Screen.Students.route
                        )
                        NavScreen(
                            iconRes = R.drawable.ic_changes,
                            name = "Update score",
                            onMenuItemClick = { navController.navigate(Screen.Scoring.route) },
                            route = Screen.Scoring.route
                        )
                        NavScreen(
                            iconRes = R.drawable.ic_list,
                            name = "Progress",
                            onMenuItemClick = { navController.navigate(Screen.Progress.route) },
                            route = Screen.Progress.route
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun NavScreen(
    iconRes: Int,
    name: String,
    onMenuItemClick: (String) -> Unit,
    route: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .background(Color.White)
            .clickable { onMenuItemClick(route) }
    ) {
        ViewStudentsIcon(iconRes)
        Text(
            modifier = Modifier
                .padding(start = 10.dp)
                .weight(8f),
            text = name,
            fontSize = 18.sp
        )
        Image(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = "Forward",
            modifier = Modifier
                .size(32.dp)
                .weight(1f)
        )
    }
}

@Composable
private fun ViewStudentsIcon(iconRes: Int) {
    Card(
        shape = CircleShape,
        modifier = Modifier
            .size(40.dp)
            .padding(4.dp)
            .background(color = Color.White),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Image(
            painter = painterResource(id = iconRes),
            contentDescription = "Students icon",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(48.dp)
        )
    }
}

@Composable
private fun HomeTopBar() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(65.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 15.dp),
        shape = RoundedCornerShape(0.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 15.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "ClassEase",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Cursive,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun HomePreview() {
    ClassEaseTheme(darkTheme = false) {
        // HomeScreen()
    }
}
