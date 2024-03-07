package com.evaniewares.classease.utils

import android.content.Context
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
            modifier = Modifier.fillMaxHeight(),
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
){
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
