package com.evaniewares.classease.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ClearAll
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.evaniewares.classease.ui.theme.AppColor
import com.evaniewares.classease.ui.theme.ClassEaseTheme
import com.evaniewares.classease.ui.theme.DangerColor
import com.evaniewares.classease.utils.CustomTopBar

@Composable
fun SettingScreen(
    navController: NavHostController,
    onClearScores: () -> Unit,
    onClearAll: () -> Unit
) {
    var showClearScoreDialog by rememberSaveable {
        mutableStateOf(false)
    }
    var showClearDataDialog by rememberSaveable {
        mutableStateOf(false)
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            CustomTopBar(
                activity = "Settings",
                onBackClick = { navController.popBackStack() }
            )
        }
    ) { paddingValues ->
        Surface(
            modifier = Modifier.padding(paddingValues)
        ) {
            Column {
                SettingRow(
                    title = "Clear scores",
                    imageVector = Icons.Default.ClearAll,
                    onClick = { showClearScoreDialog = true },
                    description = "This clears all student scores"
                )
                HorizontalDivider(modifier = Modifier.padding(start = 10.dp, end = 10.dp))
                SettingRow(
                    title = "Clear all data",
                    imageVector = Icons.Default.DeleteForever,
                    onClick = { showClearDataDialog = true },
                    tint = DangerColor,
                    description = "Clear all students, this can not be undone"
                )
            }
            if (showClearDataDialog) {
                ClearDataDialog(
                    text = "Clear all data?",
                    onDismiss = { showClearDataDialog = false },
                    onConfirm = {
                        onClearAll()
                        showClearDataDialog = false
                    }
                )
            }
            if (showClearScoreDialog) {
                ClearDataDialog(
                    text = "Clear all scores?",
                    onDismiss = { showClearScoreDialog = false },
                    onConfirm = {
                        onClearScores()
                        showClearScoreDialog = false
                    }
                )
            }
        }
    }
}

@Composable
fun SettingRow(
    title: String,
    imageVector: ImageVector,
    onClick: () -> Unit,
    description: String = "",
    tint: Color = AppColor
) {
    Row(
        modifier = Modifier
            .padding(10.dp)
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = title,
            tint = tint
        )
        Spacer(modifier = Modifier.width(10.dp))
        Column(
            verticalArrangement = Arrangement.spacedBy(5.dp),
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge
            )
            if (description.isNotBlank()) {
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Composable
private fun ClearDataDialog(
    text: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = onConfirm,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(DangerColor)
            ) {
                Text(text = "Confirm")
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onDismiss,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Cancel")
            }
        },
        text = {
            Text(text = text)
        }
    )
}

@Preview(showSystemUi = true)
@Composable
private fun SettingsPreview() {
    ClassEaseTheme {
        Surface {
            SettingScreen(
                navController = rememberNavController(),
                onClearAll = {},
                onClearScores = {}
            )
        }
    }
}
