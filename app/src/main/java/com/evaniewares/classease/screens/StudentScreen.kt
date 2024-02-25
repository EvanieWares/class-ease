package com.evaniewares.classease.screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.evaniewares.classease.domain.model.CustomTopBar
import com.evaniewares.classease.domain.model.GenderType
import com.evaniewares.classease.domain.model.StudentEntity
import com.evaniewares.classease.domain.model.toastMsg
import com.evaniewares.classease.presentation.EditStudentViewModel
import com.evaniewares.classease.presentation.StudentViewModel
import com.evaniewares.classease.ui.theme.DangerColor

@Composable
fun StudentScreen(
    studentViewModel: StudentViewModel,
    navController: NavHostController
) {
    studentViewModel.getStudentsSortById()
    val context = LocalContext.current
    val editStudentViewModel = remember {
        EditStudentViewModel()
    }
    val state = editStudentViewModel.editState.collectAsStateWithLifecycle().value

    val studentList =
        studentViewModel.studentList.collectAsStateWithLifecycle(initialValue = emptyList())

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
                 CustomTopBar(
                     activity = "Students",
                     onBackClick = { navController.popBackStack() }
                 )
        },
        bottomBar = {
            ElevatedButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                onClick = {
                    editStudentViewModel.onAction(EditStudentViewModel.UserAction.AddButtonClicked)
                },
                shape = RoundedCornerShape(0.dp),
                colors = ButtonDefaults.buttonColors(),
                elevation = ButtonDefaults.buttonElevation(5.dp)
            ) {
                Text(text = "Add")
            }
        }
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                StudentHeader()
                LazyColumn {
                    items(studentList.value) { student ->
                        StudentRow(
                            student = student,
                            onDeleteButtonClick = { studentToDelete ->
                                editStudentViewModel.onAction(
                                    EditStudentViewModel.UserAction.DeleteButtonClicked(
                                        studentToDelete
                                    )
                                )
                            },
                            onEditButtonClick = { studentToEdit ->
                                editStudentViewModel.onAction(
                                    EditStudentViewModel.UserAction.EditButtonClicked(
                                        studentToEdit
                                    )
                                )
                            }
                        )
                    }
                }
                if (state.isDeleting && state.selectedStudent != null) {
                    DeleteStudentDialog(
                        student = state.selectedStudent,
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
                                editStudentViewModel.onAction(EditStudentViewModel.UserAction.DeleteStudentDialogDismiss)
                            }
                        },
                        onDismiss = {
                            editStudentViewModel.onAction(EditStudentViewModel.UserAction.DeleteStudentDialogDismiss)
                        }
                    )
                }
                if (state.isEditing || state.isAdding) {
                    EditStudentDialog(
                        state = state,
                        onIdChanged = { studentId ->
                            editStudentViewModel.onAction(
                                EditStudentViewModel.UserAction.OnIDChanged(
                                    studentId
                                )
                            )
                        },
                        onNameChanged = { studentName ->
                            editStudentViewModel.onAction(
                                EditStudentViewModel.UserAction.OnNameChanged(
                                    studentName.uppercase()
                                )
                            )
                        },
                        onGenderChanged = { gender ->
                            editStudentViewModel.onAction(
                                EditStudentViewModel.UserAction.OnGenderChanged(
                                    gender
                                )
                            )
                        },
                        onDismissRequest = {
                            editStudentViewModel.onAction(EditStudentViewModel.UserAction.EditStudentDialogDismiss)
                        },
                        onSaveStudent = { student ->
                            if (state.isEditing) {
                                studentViewModel.updateStudent(student) { success ->
                                    if (success) {
                                        editStudentViewModel.onAction(EditStudentViewModel.UserAction.OnSaveStudent)
                                        editStudentViewModel.onAction(EditStudentViewModel.UserAction.EditStudentDialogDismiss)
                                        toastMsg(context, "Updated!")
                                    } else {
                                        toastMsg(context, "Unable to update. Try again!")
                                    }
                                }
                            } else {
                                studentViewModel.insertStudent(student) { success ->
                                    if (success) {
                                        editStudentViewModel.onAction(EditStudentViewModel.UserAction.OnSaveStudent)
                                        toastMsg(context, "Saved!")
                                    } else {
                                        toastMsg(context, "Unable to save. Try again!",)
                                    }
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun StudentRow(
    student: StudentEntity,
    onEditButtonClick: (StudentEntity) -> Unit,
    onDeleteButtonClick: (StudentEntity) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        border = BorderStroke(1.dp, Color.Black)
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
                    .weight(1f)
            )
            Text(
                text = student.studentName,
                modifier = Modifier
                    .padding(2.dp)
                    .weight(8f)
            )
            Text(
                text = student.gender,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(2.dp)
                    .weight(2f)
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
}

@Composable
private fun StudentHeader() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        border = BorderStroke(1.dp, Color.Black)
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
                    .weight(1f)
            )
            Text(
                text = "STUDENT NAME",
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(2.dp)
                    .weight(8f)
            )
            Text(
                text = "SEX",
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(2.dp)
                    .weight(2f)
            )
            Spacer(modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.weight(1f))
        }
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

@Composable
private fun EditStudentDialog(
    state: EditStudentViewModel.EditState,
    onIdChanged: (String) -> Unit,
    onNameChanged: (String) -> Unit,
    onGenderChanged: (String) -> Unit,
    onDismissRequest: () -> Unit,
    onSaveStudent: (StudentEntity) -> Unit
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        Card {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(text = "Enter student ID, name and select gender")
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.Center
                ) {
                    if (state.isEditing) {
                        EditStudentTextField(
                            value = state.studentId,
                            placeHolder = "STUDENT ID",
                            onValueChange = onIdChanged,
                            keyboardType = KeyboardType.Decimal,
                            enabled = false
                        )
                    } else {
                        EditStudentTextField(
                            value = state.studentId,
                            placeHolder = "STUDENT ID",
                            onValueChange = onIdChanged,
                            keyboardType = KeyboardType.Decimal
                        )
                    }
                    EditStudentTextField(
                        value = state.studentName,
                        placeHolder = "STUDENT NAME",
                        onValueChange = onNameChanged
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        GenderSelection(
                            text = "MALE",
                            selected = state.gender == GenderType.M.name,
                            onClick = { onGenderChanged("M") }
                        )
                        GenderSelection(
                            text = "FEMALE",
                            selected = state.gender == GenderType.F.name,
                            onClick = { onGenderChanged("F") }
                        )
                        GenderSelection(
                            text = "OTHER",
                            selected = state.gender == GenderType.O.name,
                            onClick = { onGenderChanged("O") }
                        )
                    }
                    Button(
                        onClick = {
                            if (state.selectedStudent != null) {
                                onSaveStudent(
                                    state.selectedStudent.copy(
                                        studentName = state.studentName.trim(),
                                        gender = state.gender.trim()
                                    )
                                )
                            } else {
                                onSaveStudent(
                                    StudentEntity(
                                        studentId = state.studentId.toLong(),
                                        studentName = state.studentName.trim(),
                                        gender = state.gender.trim()
                                    )
                                )
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 30.dp, end = 30.dp),
                        enabled = validateInputs(
                            name = state.studentName,
                            gender = state.gender,
                            studentId = state.studentId
                        )
                    ) {
                        Text(text = "Save")
                    }
                }
            }
        }
    }
}

private fun validateInputs(name: String, studentId: String, gender: String): Boolean {
    if (name.isBlank() || studentId.isBlank() || gender.isBlank()) {
        return false
    }
    if (!"MFO".contains(gender.trim())) {
        return false
    }
    return studentId.isDigitsOnly()
}

@Composable
private fun GenderSelection(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Box {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = selected,
                onClick = { onClick() }
            )
            Text(text = text)
        }
    }
}

@Composable
private fun EditStudentTextField(
    value: String,
    placeHolder: String,
    onValueChange: (String) -> Unit,
    keyboardType: KeyboardType = KeyboardType.Text,
    enabled: Boolean = true
) {
    TextField(
        value = value,
        onValueChange = { onValueChange(it) },
        modifier = Modifier.padding(10.dp),
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
            capitalization = KeyboardCapitalization.Characters
        ),
        placeholder = {
            Text(text = placeHolder)
        },
        enabled = enabled
    )
}

/*@Preview(showSystemUi = true)
@Composable
fun StudentPreview() {
    EditStudentDialog(
        state = EditStudentViewModel.EditState(),
        onIdChanged = {},
        onNameChanged = {},
        onDismissRequest = {},
        onGenderChanged = {},
        onSaveStudent = {}
    )
}*/
