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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.evaniewares.classease.domain.model.CustomTopBar
import com.evaniewares.classease.domain.model.StudentEntity
import com.evaniewares.classease.domain.model.SubjectType
import com.evaniewares.classease.domain.model.toastMsg
import com.evaniewares.classease.presentation.ScoringViewModel
import com.evaniewares.classease.presentation.StudentViewModel

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
                        verticalArrangement = Arrangement.spacedBy(15.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = studentToUpdate?.studentName ?: "")
                        ScoringTextField(value = scoreState.studentId,
                            placeHolder = "ID",
                            onValueChange = { studentId ->
                                scoringViewModel.onIdChange(studentId)
                                if (scoreState.studentId.isDigitsOnly()) {
                                    studentViewModel.getStudentById(studentId = studentId.toLong())
                                }
                            }
                        )
                        ScoringTextField(value = scoreState.score,
                            placeHolder = "SCORE",
                            onValueChange = { score ->
                                scoringViewModel.onScoreChange(score)
                            }
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
 * Customized [TextField] to simplify it's usage in the program
 *
 * @param value the input text to be shown in the text field.
 * @param placeHolder the text to be displayed when the input text is empty.
 * @param onValueChange the callback that is triggered when the input service updates the text. An
 * updated text comes as a parameter of the callback.
 */
@Composable
private fun ScoringTextField(
    value: String,
    placeHolder: String,
    onValueChange: (String) -> Unit
) {
    TextField(
        value = value,
        onValueChange = { onValueChange(it) },
        modifier = Modifier.padding(10.dp),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number
        ),
        placeholder = {
            Text(text = placeHolder)
        },
        shape = RoundedCornerShape(5.dp),
        textStyle = TextStyle.Default
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
    if (student == null) {
        return false
    }
    if (studentId.isDigitsOnly() && score.isDigitsOnly()) {
        if (student.studentId == studentId.toLong()) {
            return true
        }
    }
    return false
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
        SubjectType.MATHS -> student.copy(maths = score)
        SubjectType.SCIENCE -> student.copy(science = score)
        SubjectType.SOCIAL -> student.copy(social = score)
    }
}
