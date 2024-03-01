package com.evaniewares.classease.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.evaniewares.classease.domain.model.StudentEntity
import com.evaniewares.classease.presentation.ScoringViewModel
import com.evaniewares.classease.presentation.StudentViewModel
import com.evaniewares.classease.utils.CustomTopBar
import com.evaniewares.classease.utils.SubjectType
import com.evaniewares.classease.utils.toastMsg
import java.util.Locale

/**
 * Screen that enables users to update student scores.
 *
 * Users selects the subject, enters student ID and score.
 * The save [Button] will be enabled when the user input is valid.
 *
 * @param navController the [NavHostController] that will help to go
 * back to the previous page.
 * @param studentViewModel the [StudentViewModel] instance that will help
 * to retrieve and update data from and to the database. It will also help data
 * to survive recompositions
 */
@Composable
fun ScoringScreen(
    navController: NavHostController,
    studentViewModel: StudentViewModel
) {
    val context = LocalContext.current
    val scoringViewModel = remember {
        ScoringViewModel()
    }
    val scoreState = scoringViewModel.scoreState.collectAsStateWithLifecycle().value
    val studentState = studentViewModel.studentState.collectAsStateWithLifecycle().value
    val studentToUpdate = studentState.scoringStudent
    /*var currentStudentName by remember {
        mutableStateOf("")
    }

    LaunchedEffect(scoreState.studentId) {
        val studentId = scoreState.studentId
        currentStudentName = if (studentId.isNotBlank() && studentId.isDigitsOnly()) {
            studentViewModel.getStudentById(studentId.toLong())
            studentToUpdate?.studentName ?: ""
        } else {
            ""
        }
    }*/

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            CustomTopBar(
                activity = "Scoring",
                onBackClick = { navController.popBackStack() }
            )
        }
    ) { paddingValues ->
        Surface(
            modifier = Modifier.padding(paddingValues)
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
                            state = scoreState,
                            scoringViewModel = scoringViewModel
                        )

                        // Display the name of student with the entered ID
                        /*Text(
                            text = studentToUpdate?.studentName ?: "",
                            style = MaterialTheme.typography.labelSmall
                        )*/

                        // Display the score updating form
                        ScoringTextField(
                            value = scoreState.studentId,
                            onValueChange = { studentId ->
                                scoringViewModel.onIdChange(studentId)
                                if (studentId.isNumber()) {
                                    studentViewModel.getStudentById(studentId.toLong())
                                } else {
                                    studentViewModel.getStudentById(0)
                                }
                            },
                            label = studentToUpdate?.studentName ?: "ID"
                        )
                        ScoringTextField(
                            value = scoreState.score,
                            onValueChange = { score ->
                                scoringViewModel.onScoreChange(score)
                            },
                            label = "SCORE"
                        )
                        Button(
                            onClick = {
                                studentToUpdate?.let { oldStudent ->
                                    val newStudent = updatedStudent(
                                        student = oldStudent,
                                        score = scoreState.score.toInt(),
                                        subject = scoreState.subject
                                    )
                                    studentViewModel.updateStudent(newStudent) { success ->
                                        if (success) {
                                            toastMsg(context, "Saved!")
                                            scoringViewModel.onScoreChange("")
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
                                studentId = scoreState.studentId,
                                score = scoreState.score,
                                student = studentToUpdate
                            )
                        ) {
                            Text(text = "Save")
                        }
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
 * @param state the [ScoringViewModel.ScoreState] to manage the state of the [ScoringScreen].
 * @param scoringViewModel the [ScoringViewModel] instance to help data survive recompositions.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LearningAreas(
    state: ScoringViewModel.ScoreState,
    scoringViewModel: ScoringViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ExposedDropdownMenuBox(
            expanded = state.isLearningAreaExpanded,
            onExpandedChange = { isExpanded ->
                scoringViewModel.onExpandStateChange(isExpanded)
            }
        ) {
            TextField(
                value = state.subject.name.lowercase().replaceFirstChar {
                    it.titlecase(
                        Locale.getDefault()
                    )
                },
                onValueChange = {},
                readOnly = true,
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(
                        expanded = state.isLearningAreaExpanded
                    )
                },
                colors = ExposedDropdownMenuDefaults.textFieldColors(),
                modifier = Modifier.menuAnchor()
            )
            ExposedDropdownMenu(
                expanded = state.isLearningAreaExpanded,
                onDismissRequest = {
                    scoringViewModel.onExpandStateChange(false)
                }
            ) {
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    DropdownMenuItem(
                        text = {
                            Text(text = "English")
                        }, onClick = {
                            scoringViewModel.onSubjectChange(SubjectType.ENGLISH)
                            scoringViewModel.onExpandStateChange(false)
                        }
                    )
                    DropdownMenuItem(
                        text = {
                            Text(text = "Chichewa")
                        }, onClick = {
                            scoringViewModel.onSubjectChange(SubjectType.CHICHEWA)
                            scoringViewModel.onExpandStateChange(false)
                        }
                    )
                    DropdownMenuItem(
                        text = {
                            Text(text = "Mathematics")
                        }, onClick = {
                            scoringViewModel.onSubjectChange(SubjectType.MATHEMATICS)
                            scoringViewModel.onExpandStateChange(false)
                        }
                    )
                    DropdownMenuItem(
                        text = {
                            Text(text = "Social & BK/RE")
                        }, onClick = {
                            scoringViewModel.onSubjectChange(SubjectType.SOCIAL)
                            scoringViewModel.onExpandStateChange(false)
                        }
                    )
                    DropdownMenuItem(
                        text = {
                            Text(text = "Primary Science")
                        }, onClick = {
                            scoringViewModel.onSubjectChange(SubjectType.SCIENCE)
                            scoringViewModel.onExpandStateChange(false)
                        }
                    )
                    DropdownMenuItem(
                        text = {
                            Text(text = "Arts & Life Skills")
                        }, onClick = {
                            scoringViewModel.onSubjectChange(SubjectType.ARTS)
                            scoringViewModel.onExpandStateChange(false)
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
    label: String
) {
    TextField(
        value = value,
        onValueChange = { onValueChange(it) },
        modifier = Modifier.padding(10.dp),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number
        ),
        shape = RoundedCornerShape(5.dp),
        label = {
            Text(text = label)
        }
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
