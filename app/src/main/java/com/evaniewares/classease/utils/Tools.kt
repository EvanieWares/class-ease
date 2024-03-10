package com.evaniewares.classease.utils

import android.content.Context
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.evaniewares.classease.R
import com.evaniewares.classease.domain.model.NavItem
import com.evaniewares.classease.domain.model.StudentEntity

@Composable
fun CustomTopBar(
    activity: String,
    onBackClick: () -> Unit
) {
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
                IconButton(onClick = { onBackClick() }) {
                    Card(
                        shape = CircleShape
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back Icon"
                        )
                    }
                }
                Text(
                    text = activity,
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

fun toastMsg(context: Context, msg: String) {
    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
}

fun getTotalScore(student: StudentEntity): Int {
    return (
            student.arts +
                    student.chichewa +
                    student.english +
                    student.maths +
                    student.science +
                    student.social
            )
}

@Composable
fun BackButtonHandler(onBackPressed: () -> Unit) {
    val backCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            onBackPressed()
        }
    }

    val currentActivity = LocalContext.current as? ComponentActivity
    DisposableEffect(currentActivity) {
        currentActivity?.onBackPressedDispatcher?.addCallback(backCallback)
        onDispose {
            backCallback.remove()
        }
    }
}

@Composable
fun CustomNavigationRail(
    navItems: List<NavItem>,
    currentRoute: String,
    onClick: (String) -> Unit
) {
    val scrollState = rememberScrollState()
    NavigationRail(
        header = {
            Icon(
                painter = painterResource(id = R.mipmap.ic_classease_round),
                contentDescription = "App icon"
            )
        },
        modifier = Modifier
            .background(MaterialTheme.colorScheme.inverseOnSurface)
            .offset(x = (-1).dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()

                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            navItems.forEach { navItem ->
                NavigationRailItem(
                    selected = currentRoute == navItem.route,
                    onClick = {
                        onClick(navItem.route)
                    },
                    icon = {
                        Icon(
                            imageVector = if (currentRoute == navItem.route) {
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
}

/**
 * Uses [NavigationBar] to offer a persistent and convenient way to switch between primary
 * destinations in an app.
 *
 * @param navItems the list of navigation items eg home, students, scoring and progress record
 * @param currentRoute the current screen route
 * @param onClick handle the onClick behaviour on navigation items
 */
@Composable
fun BottomNavigationBar(
    navItems: List<NavItem>,
    currentRoute: String,
    onClick: (String) -> Unit
) {
    NavigationBar {
        navItems.forEach { navItem ->
            NavigationBarItem(
                selected = currentRoute == navItem.route,
                onClick = {
                    onClick(navItem.route)
                },
                icon = {
                    Icon(
                        imageVector = if (currentRoute == navItem.route) {
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

fun calculateGradeGroup(
    arts: Int,
    chichewa: Int,
    english: Int,
    maths: Int,
    science: Int,
    social: Int
): Int {
    return (
            getGrade(arts) +
                    getGrade(chichewa) +
                    getGrade(english) +
                    getGrade(maths) +
                    getGrade(science) +
                    getGrade(social)
            )
}

private fun getGrade(score: Int): Int {
    return when {
        score >= 80 -> 4
        score >= 66 -> 3
        score >= 56 -> 2
        score >= 40 -> 1
        else -> 0
    }
}
