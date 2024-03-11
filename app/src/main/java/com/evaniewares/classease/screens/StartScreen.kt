package com.evaniewares.classease.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.evaniewares.classease.R
import com.evaniewares.classease.utils.Graph
import kotlinx.coroutines.delay

@Composable
fun StartScreen(
    navController: NavHostController
) {
    LaunchedEffect(Unit) {
        delay(3000)
        navController.popBackStack()
        navController.navigate(Graph.HOME)
    }

    Surface {
        Column(
            modifier = Modifier.padding(top = 100.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(40.dp)
        ) {
            Image(
                modifier = Modifier.size(100.dp),
                painter = painterResource(id = R.mipmap.ic_classease_round),
                contentDescription = "App logo",
                contentScale = ContentScale.Crop
            )
            Text(
                text = "Streamlining teacher assessment process",
                style = MaterialTheme.typography.bodyLarge,
                fontFamily = FontFamily.Cursive
            )
        }
    }
}

@Preview
@Composable
fun StartPreview() {
    StartScreen(navController = rememberNavController())
}
