package com.evaniewares.classease.screens

import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.evaniewares.classease.domain.model.StudentEntity
import com.evaniewares.classease.presentation.StudentViewModel
import com.evaniewares.classease.utils.GenderType
import com.evaniewares.classease.utils.toastMsg

@Composable
fun AddEditStudentScreen(
    navController: NavHostController,
    studentViewModel: StudentViewModel
) {
    val context = LocalContext.current
    val studentState = studentViewModel.studentState.collectAsStateWithLifecycle().value

    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Column {
            CustomAppBar(
                state = studentState,
                onBackClick = {
                    navController.popBackStack()
                },
                onSaveStudent = { student ->
                    if (studentState.isEditing) {
                        studentViewModel.updateStudent(student) { success ->
                            if (success) {
                                studentViewModel.onAction(StudentViewModel.UserAction.OnSaveStudent)
                                studentViewModel.onAction(StudentViewModel.UserAction.EditStudentDialogDismiss)
                                toastMsg(context, "Updated!")
                                navController.popBackStack()
                            } else {
                                toastMsg(context, "Unable to update. Try again!")
                            }
                        }
                    } else {
                        studentViewModel.insertStudent(student) { success ->
                            if (success) {
                                studentViewModel.onAction(StudentViewModel.UserAction.OnSaveStudent)
                                toastMsg(context, "Saved!")
                            } else {
                                toastMsg(context, "Unable to save. Try again!")
                            }
                        }
                    }
                }
            )
            AddEdit(
                state = studentState,
                onIdChanged = { studentId ->
                    studentViewModel.onAction(
                        StudentViewModel.UserAction.OnIDChanged(
                            studentId
                        )
                    )
                },
                onNameChanged = { studentName ->
                    studentViewModel.onAction(
                        StudentViewModel.UserAction.OnNameChanged(
                            studentName.uppercase()
                        )
                    )
                },
                onGenderChanged = { gender ->
                    studentViewModel.onAction(
                        StudentViewModel.UserAction.OnGenderChanged(
                            gender
                        )
                    )
                }
            )
        }
        BackHandler {
            studentViewModel.onAction(StudentViewModel.UserAction.EditStudentDialogDismiss)
        }
    }
}

@Composable
private fun AddEdit(
    state: StudentViewModel.StudentState,
    onIdChanged: (String) -> Unit,
    onNameChanged: (String) -> Unit,
    onGenderChanged: (String) -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .padding(10.dp)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = if (state.isEditing) "Edit student" else "Add student",
            style = MaterialTheme.typography.headlineSmall
        )
        Row(
            modifier = Modifier.height(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            HorizontalDivider(
                thickness = 1.dp,
                color = Color.Black
            )
        }
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
                keyboardType = KeyboardType.Decimal,
                supportingText = "If you leave empty or enter 0, the student ID will be auto-generated"
            )
        }
        EditStudentTextField(
            value = state.studentName,
            placeHolder = "STUDENT NAME",
            onValueChange = onNameChanged
        )
        RowGenderSection(
            state = state,
            onGenderChanged = onGenderChanged
        )
    }
}

/**
 * Shows the back icon and Save button to help user navigate to the previous screen
 * and to add or edit student respectively
 *
 * @param onBackClick handles user click events on the [ArrowBack] icon
 * @param onSaveStudent handles user click events on the Save [TextButton]
 */
@Composable
private fun CustomAppBar(
    onBackClick: () -> Unit,
    onSaveStudent: (StudentEntity) -> Unit,
    state: StudentViewModel.StudentState
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
            .padding(5.dp)
    ) {
        IconButton(
            onClick = onBackClick,
            modifier = Modifier
                .size(24.dp)
                .align(Alignment.CenterStart)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Default.ArrowBack,
                contentDescription = "Back"
            )
        }
        if (validateInputs(
                name = state.studentName,
                gender = state.gender,
                studentId = state.studentId
            )
        ) {
            Surface(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .clickable {
                        if (state.selectedStudent != null) {
                            onSaveStudent(
                                state.selectedStudent.copy(
                                    studentName = state.studentName.trim(),
                                    gender = state.gender.trim()
                                )
                            )
                        } else {
                            val studentId = if (state.studentId.isBlank()){
                                0L
                            } else {
                                state.studentId.toLong()
                            }
                            onSaveStudent(
                                StudentEntity(
                                    studentId = studentId,
                                    studentName = state.studentName.trim(),
                                    gender = state.gender.trim()
                                )
                            )
                        }
                    },
                tonalElevation = 10.dp
            ) {
                Text(
                    text = "SAVE",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.padding(5.dp)
                )
            }
        }
    }
}

@Composable
private fun RowGenderSection(
    state: StudentViewModel.StudentState,
    onGenderChanged: (String) -> Unit
) {
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
}

private fun validateInputs(name: String, studentId: String, gender: String): Boolean {
    if (name.isBlank() || gender.isBlank()) {
        return false
    }
    if (!"MFO".contains(gender.trim())) {
        return false
    }
    return studentId.isBlank() || studentId.isDigitsOnly()
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
    enabled: Boolean = true,
    supportingText: String = ""
) {
    TextField(
        value = value,
        onValueChange = { onValueChange(it) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
            capitalization = KeyboardCapitalization.Characters
        ),
        placeholder = {
            Text(text = placeHolder)
        },
        enabled = enabled,
        maxLines = 1,
        supportingText = {
            if (supportingText.isNotBlank()) {
                Text(
                    text = supportingText,
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }
    )
}
