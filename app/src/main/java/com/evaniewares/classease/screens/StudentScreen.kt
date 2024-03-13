package com.evaniewares.classease.screens

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.evaniewares.classease.domain.model.StudentEntity
import com.evaniewares.classease.navigation.HomeScreenRoutes
import com.evaniewares.classease.presentation.StudentViewModel
import com.evaniewares.classease.ui.theme.DangerColor

/**
 * Screen that enables users to add, edit, update and delete students.
 *
 * To add new students, users will click the [FloatingActionButton] that says "Add new".
 * Then a [Dialog] will appear to enable users enter student unique ID, name and choose
 * the student's gender.
 * The save [Button] will be enabled when the user input is valid.
 *
 * @param navController the [NavHostController] that will help to go
 * back to the previous page.
 * @param studentViewModel the [StudentViewModel] instance that will help
 * to retrieve and update data from and to the database. It will also help data
 * to survive recompositions
 */
@Composable
fun StudentScreen(
    navController: NavHostController,
    studentViewModel: StudentViewModel
) {
    val context = LocalContext.current
    val studentState = studentViewModel.studentState.collectAsStateWithLifecycle().value
    val studentList =
        studentViewModel.studentList.collectAsStateWithLifecycle(initialValue = emptyList())
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 80.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(5.dp)
        ) {
            CustomAppBar(
                onAddNewClick = {
                    studentViewModel.onAction(StudentViewModel.UserAction.AddButtonClicked)
                    navController.navigate(HomeScreenRoutes.AddEditStudent.route)
                }
            )
            StudentHeader()
            LazyColumn {
                items(studentList.value) { student ->
                    StudentRow(
                        student = student,
                        onDeleteButtonClick = { studentToDelete ->
                            studentViewModel.onAction(
                                StudentViewModel.UserAction.DeleteButtonClicked(
                                    studentToDelete
                                )
                            )
                        },
                        onEditButtonClick = { studentToEdit ->
                            studentViewModel.onAction(
                                StudentViewModel.UserAction.EditButtonClicked(
                                    studentToEdit
                                )
                            )
                            navController.navigate(HomeScreenRoutes.AddEditStudent.route)
                        }
                    )
                }
            }
            if (studentState.isDeleting && studentState.selectedStudent != null) {
                DeleteStudentDialog(
                    student = studentState.selectedStudent,
                    onConfirm = { student ->
                        studentViewModel.deleteStudent(student) { success ->
                            if (success) {
                                Toast.makeText(
                                    context,
                                    "Student was removed successfully",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                Toast.makeText(
                                    context,
                                    "Unable to remove student. Please try again!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            studentViewModel.onAction(StudentViewModel.UserAction.DeleteStudentDialogDismiss)
                        }
                    },
                    onDismiss = {
                        studentViewModel.onAction(StudentViewModel.UserAction.DeleteStudentDialogDismiss)
                    }
                )
            }
        }
    }
}

/**
 * Shows the back icon to help user navigate to the previous screen
 *
 * @param onAddNewClick displays a dialog that has a form for users to fill in
 * the details of the student to be added.
 */
@Composable
private fun CustomAppBar(
    onAddNewClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
    ) {
        Surface(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .clickable { onAddNewClick() },
            tonalElevation = 10.dp
        ) {
            Text(
                text = "ADD NEW",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(5.dp)
            )
        }
    }
}

@Composable
private fun StudentRow(
    student: StudentEntity,
    onEditButtonClick: (StudentEntity) -> Unit,
    onDeleteButtonClick: (StudentEntity) -> Unit,
    textStyle: TextStyle = MaterialTheme.typography.bodyMedium
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp, 2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "${student.studentId}.",
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(2.dp)
                .weight(1f),
            style = textStyle
        )
        Text(
            text = student.studentName,
            modifier = Modifier
                .padding(2.dp)
                .weight(8f),
            style = textStyle
        )
        Text(
            text = student.gender,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(2.dp)
                .weight(2f),
            style = textStyle
        )
        IconButton(
            onClick = { onEditButtonClick(student) },
            modifier = Modifier
                .size(18.dp)
                .weight(1f)
        ) {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "Edit ${student.studentName}"
            )
        }
        IconButton(
            onClick = { onDeleteButtonClick(student) },
            modifier = Modifier
                .size(18.dp)
                .weight(1f)
        ) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete ${student.studentName}",
                tint = DangerColor
            )
        }
    }
}

@Composable
private fun StudentHeader(
    textStyle: TextStyle = MaterialTheme.typography.titleSmall,
    primaryColor: Color = MaterialTheme.colorScheme.primary
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp, 5.dp)
    ) {
        Text(
            text = "ID",
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(2.dp)
                .weight(1f),
            style = textStyle,
            color = primaryColor
        )
        Text(
            text = "STUDENT NAME",
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(2.dp)
                .weight(8f),
            style = textStyle,
            color = primaryColor
        )
        Text(
            text = "SEX",
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(2.dp)
                .weight(2f),
            style = textStyle,
            color = primaryColor
        )
        Spacer(modifier = Modifier.weight(1f))
        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
private fun DeleteStudentDialog(
    student: StudentEntity,
    onDismiss: () -> Unit,
    onConfirm: (StudentEntity) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = { onConfirm(student) },
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
            Text(text = "Delete ${student.studentName}?")
        }
    )
}
