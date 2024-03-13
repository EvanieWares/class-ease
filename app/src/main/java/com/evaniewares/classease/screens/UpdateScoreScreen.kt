package com.evaniewares.classease.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import com.evaniewares.classease.domain.model.StudentEntity
import com.evaniewares.classease.presentation.StudentViewModel
import com.evaniewares.classease.utils.SubjectType
import com.evaniewares.classease.utils.calculateGradeGroup
import com.evaniewares.classease.utils.toastMsg
import java.util.Locale

/**
 * Screen that enables users to update student scores.
 *
 * Users selects the subject, enters student ID and score.
 * The save [Button] will be enabled when the user input is valid.
 *
 * @param studentViewModel the [StudentViewModel] instance that will help
 * to retrieve and update data from and to the database. It will also help data
 * to survive recompositions
 */
@Composable
fun ScoringScreen(
    studentViewModel: StudentViewModel
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val context = LocalContext.current
    val studentToUpdate = remember {
        mutableStateOf<StudentEntity?>(null)
    }
    var studentId by rememberSaveable {
        mutableStateOf("")
    }
    var score by rememberSaveable {
        mutableStateOf("")
    }
    var subject by rememberSaveable {
        mutableStateOf(SubjectType.ENGLISH)
    }
    var newEntry by rememberSaveable {
        mutableStateOf(true)
    }

    LaunchedEffect(studentId, newEntry) {
        if (studentId.isNumber()) {
            studentToUpdate.value = studentViewModel.getStudentById(studentId.toLong())
        } else {
            studentToUpdate.value = null
        }
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(80.dp)
    ) {
        LazyColumn(
            modifier = Modifier.padding(5.dp)
        ) {
            item {
                Column(
                    verticalArrangement = Arrangement.spacedBy(5.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Display the subject drop down list for users to select from
                    LearningAreas(
                        subject = subject,
                        onSubjectChange = { subjectType ->
                            subject = subjectType
                        }
                    )

                    // Display the score updating form
                    ScoringTextField(
                        value = studentId,
                        onValueChange = { value ->
                            studentId = value
                            newEntry = false
                        },
                        label = studentToUpdate.value?.studentName ?: "ID",
                        onFocusChanged = { focusState ->
                            if (focusState.hasFocus && newEntry) {
                                studentId = ""
                            }
                        },
                        onDone = {
                            keyboardController?.hide()
                        }
                    )
                    ScoringTextField(
                        value = score,
                        onValueChange = { value ->
                            score = value
                            newEntry = false
                        },
                        label = "SCORE",
                        onFocusChanged = { focusState ->
                            if (focusState.hasFocus && newEntry) {
                                score = ""
                            }
                        },
                        onDone = {
                            keyboardController?.hide()
                        }
                    )
                    Button(
                        onClick = {
                            studentToUpdate.value?.let { oldStudent ->
                                val newStudent = updatedStudent(
                                    student = oldStudent,
                                    score = score.toInt(),
                                    subject = subject
                                )
                                studentViewModel.updateStudent(
                                    newStudent.copy(
                                        gradeGroup = calculateGradeGroup(
                                            newStudent.arts,
                                            newStudent.chichewa,
                                            newStudent.english,
                                            newStudent.maths,
                                            newStudent.science,
                                            newStudent.social
                                        )
                                    )
                                ) { success ->
                                    if (success) {
                                        toastMsg(context, "Saved!")
                                        newEntry = true
                                        score = ""
                                    } else {
                                        toastMsg(
                                            context,
                                            "Unable to update. Check your input and try again!"
                                        )
                                    }
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 30.dp, end = 30.dp),
                        enabled = isValidInputs(
                            studentId = studentId,
                            score = score,
                            student = studentToUpdate.value
                        )
                    ) {
                        Text(text = "Save")
                    }
                }
            }
        }
    }
}

/**
 * Displays a [ExposedDropdownMenuBox] with a list of subject for user to select from.
 *
 * This is an alternative to using radio buttons and is implemented in this app
 * to save space on user's screen.
 *
 * @param subject the [SubjectType] to update.
 * @param onSubjectChange the callback function to update the selected subject.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LearningAreas(
    subject: SubjectType,
    onSubjectChange: (SubjectType) -> Unit
) {
    var isExpanded by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ExposedDropdownMenuBox(
            expanded = isExpanded,
            onExpandedChange = {
                isExpanded = it
            }
        ) {
            TextField(
                value = subject.name.lowercase().replaceFirstChar {
                    it.titlecase(
                        Locale.getDefault()
                    )
                },
                onValueChange = {},
                readOnly = true,
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(
                        expanded = isExpanded
                    )
                },
                colors = ExposedDropdownMenuDefaults.textFieldColors(),
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor()
                    .padding(start = 5.dp, end = 5.dp)
            )
            ExposedDropdownMenu(
                expanded = isExpanded,
                onDismissRequest = {
                    isExpanded = false
                }
            ) {
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    DropdownMenuItem(
                        text = {
                            Text(text = "English")
                        }, onClick = {
                            onSubjectChange(SubjectType.ENGLISH)
                            isExpanded = false
                        }
                    )
                    DropdownMenuItem(
                        text = {
                            Text(text = "Chichewa")
                        }, onClick = {
                            onSubjectChange(SubjectType.CHICHEWA)
                            isExpanded = false
                        }
                    )
                    DropdownMenuItem(
                        text = {
                            Text(text = "Mathematics")
                        }, onClick = {
                            onSubjectChange(SubjectType.MATHEMATICS)
                            isExpanded = false
                        }
                    )
                    DropdownMenuItem(
                        text = {
                            Text(text = "Social & BK/RE")
                        }, onClick = {
                            onSubjectChange(SubjectType.SOCIAL)
                            isExpanded = false
                        }
                    )
                    DropdownMenuItem(
                        text = {
                            Text(text = "Primary Science")
                        }, onClick = {
                            onSubjectChange(SubjectType.SCIENCE)
                            isExpanded = false
                        }
                    )
                    DropdownMenuItem(
                        text = {
                            Text(text = "Arts & Life Skills")
                        }, onClick = {
                            onSubjectChange(SubjectType.ARTS)
                            isExpanded = false
                        }
                    )
                }
            }
        }
    }
}

/**
 * Customized [TextField] to simplify it's usage in the program
 *
 * @param value the input text to be shown in the text field.
 * @param label the text to be displayed inside the [TextField] container.
 * @param onValueChange the callback that is triggered when the input service updates the text. An
 * updated text comes as a parameter of the callback.
 */
@Composable
private fun ScoringTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    onFocusChanged: (FocusState) -> Unit,
    onDone: () -> Unit
) {
    TextField(
        value = value,
        onValueChange = { onValueChange(it) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .onFocusChanged { focusState ->
                onFocusChanged(focusState)
            },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = { onDone() }
        ),
        shape = RoundedCornerShape(5.dp),
        label = {
            Text(text = label)
        },
        maxLines = 1
    )
}

/**
 * Returns whether the form was filled correctly.
 *
 * It checks if the [student] is a valid [StudentEntity]. Then it checks if [studentId] and
 * [score] contains digits only. Lastly, it checks if the [studentId] matches [student]'s studentId.
 */
private fun isValidInputs(
    studentId: String,
    score: String,
    student: StudentEntity? = null
): Boolean {
    if (studentId.isBlank() || score.isBlank()) {
        return false
    }
    if (student != null && studentId.isDigitsOnly() && score.isDigitsOnly()) {
        if (student.studentId == studentId.toLong()) {
            return true
        }
    }
    return false
}

private fun CharSequence.isNumber(): Boolean {
    if (this.isBlank()) {
        return false
    }
    return this.isDigitsOnly()
}

/**
 * Copy [student] and update the [score] for the a particular [subject]
 *
 * This will simplify the process of updating student scores
 */
private fun updatedStudent(
    student: StudentEntity,
    score: Int,
    subject: SubjectType
): StudentEntity {
    return when (subject) {
        SubjectType.ARTS -> student.copy(arts = score)
        SubjectType.CHICHEWA -> student.copy(chichewa = score)
        SubjectType.ENGLISH -> student.copy(english = score)
        SubjectType.MATHEMATICS -> student.copy(maths = score)
        SubjectType.SCIENCE -> student.copy(science = score)
        SubjectType.SOCIAL -> student.copy(social = score)
    }
}
