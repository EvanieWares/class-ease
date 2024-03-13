package com.evaniewares.classease.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.evaniewares.classease.domain.model.StudentEntity
import com.evaniewares.classease.presentation.StudentViewModel
import com.evaniewares.classease.tests.studentListTest
import com.evaniewares.classease.utils.StudentSortType
import com.evaniewares.classease.utils.getTotalScore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ProgressScreen(
    studentViewModel: StudentViewModel
) {
    studentViewModel.getStudentsSortByScore()
    val state = studentViewModel.studentState.collectAsStateWithLifecycle().value
    val studentList = studentViewModel.studentList.collectAsStateWithLifecycle().value
    val isSortDialogShow = rememberSaveable {
        mutableStateOf(false)
    }

    LaunchedEffect(state.progressSortType) {
        when (state.progressSortType) {
            StudentSortType.SCORE -> {
                studentViewModel.getStudentsSortByScore()
            }

            StudentSortType.ID -> {
                studentViewModel.getStudentsSortById()
            }

            StudentSortType.GRADE -> {
                studentViewModel.getStudentsSortByGrade()
            }
        }
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 80.dp)
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize()
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                CompactProgressScreen(studentList = studentList)
                /*when (navigationType) {
                    ClassEaseNavigationType.BOTTOM_NAVIGATION -> {
                        CompactProgressScreen(studentList = studentList)
                    }

                    ClassEaseNavigationType.NAVIGATION_RAIL -> {
                        MediumProgressScreen(studentList = studentList)
                    }

                    else -> {
                        ExpandedProgressScreen(studentList = studentList)
                    }
                }*/
            }
        }
        AnimatedVisibility(isSortDialogShow.value) {
            SortTypeDialog(
                onDismiss = {
                    isSortDialogShow.value = false
                },
                onGradeSelect = {
                    studentViewModel.onSortTypeChange(StudentSortType.GRADE)
                    isSortDialogShow.value = false
                },
                onIdSelect = {
                    studentViewModel.onSortTypeChange(StudentSortType.ID)
                    isSortDialogShow.value = false
                },
                onScoreSelect = {
                    studentViewModel.onSortTypeChange(StudentSortType.SCORE)
                    isSortDialogShow.value = false
                },
                state = state
            )
        }
    }
}

@Composable
private fun CustomSearchBar(
    active: Boolean,
    onQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    onActiveChange: (Boolean) -> Unit,
    query: String
) {
    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            AnimatedVisibility(
                visible = active,
                enter = fadeIn() + slideInHorizontally(),
                exit = fadeOut() + slideOutHorizontally()
            ) {
                IconButton(
                    onClick = {
                        onActiveChange(false)
                    }
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                        contentDescription = "Close search"
                    )
                }
            }
            AnimatedVisibility(
                visible = active,
                enter = fadeIn() + slideInHorizontally(initialOffsetX = { it }),
                exit = fadeOut() + slideOutHorizontally(targetOffsetX = { it }),
                modifier = Modifier.weight(1f)
            ) {
                TextField(
                    value = query,
                    onValueChange = onQueryChange,
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Search
                    ),
                    keyboardActions = KeyboardActions(
                        onSearch = { onSearch(query) }
                    ),
                    singleLine = true,
                    placeholder = {
                        Text(text = "Search")
                    },
                    shape = RoundedCornerShape(40),
                    colors = OutlinedTextFieldDefaults.colors(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 20.dp)
                )
            }
            AnimatedVisibility(
                visible = !active,
                enter = fadeIn() + slideInHorizontally(),
                exit = fadeOut() + slideOutHorizontally()
            ) {
                IconButton(
                    onClick = {
                        onActiveChange(true)
                    }
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Search,
                        contentDescription = "Open search"
                    )
                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun SearchPreview() {
    Column {
        CompactProgressScreen(studentList = studentListTest)
    }
}

@Composable
private fun CompactProgressScreen(
    studentList: List<StudentEntity>
) {
    val scope = rememberCoroutineScope()
    val keyboardController = LocalSoftwareKeyboardController.current
    val query = rememberSaveable {
        mutableStateOf("")
    }
    val active = rememberSaveable {
        mutableStateOf(false)
    }
    CustomSearchBar(
        query = query.value,
        active = active.value,
        onQueryChange = { query.value = it },
        onSearch = {
            keyboardController?.hide()
        },
        onActiveChange = {
            active.value = it
            if (!it) {
                keyboardController?.hide()
                scope.launch(Dispatchers.IO) {
                    delay(300)
                    query.value = ""
                }
            }
        }
    )
    StudentItemHeader()
    LazyColumn {
        items(
            studentList.filter { it.studentName.contains(query.value, true) }
        ) { student ->
            StudentItem(student = student)
        }
    }
}

@Composable
private fun MediumProgressScreen(
    studentList: List<StudentEntity>
) {
    StudentItemHeader()
    LazyColumn {
        items(studentList) { student ->
            StudentItem(student = student)
        }
    }
}

@Composable
private fun ExpandedProgressScreen(
    studentList: List<StudentEntity>
) {
    StudentItemExpandedHeader()
    LazyColumn {
        items(studentList) { student ->
            StudentItemExpandedItem(student = student)
        }
    }
}

/**
 * Table header for the student progress record. Helps to show data
 * in an organized manner.
 */
@Composable
private fun StudentItemHeader(
    textStyle: TextStyle = MaterialTheme.typography.titleSmall
) {
    val scoreWeight = 1f
    val primaryColor = MaterialTheme.colorScheme.primary
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
    ) {
        Text(
            text = "ID",
            modifier = Modifier
                .weight(1.3f)
                .padding(5.dp),
            style = textStyle,
            textAlign = TextAlign.Center,
            color = primaryColor,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "STUDENT NAME",
            modifier = Modifier
                .weight(6f)
                .padding(5.dp),
            style = textStyle,
            textAlign = TextAlign.Left,
            color = primaryColor,
            fontWeight = FontWeight.Bold
        )
        Column(
            modifier = Modifier
                .weight(0.5f)
                .padding(5.dp)
        ) {
            "SEX".forEach {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = it.toString(),
                    style = textStyle,
                    textAlign = TextAlign.Center,
                    color = primaryColor,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        Column(
            modifier = Modifier
                .weight(scoreWeight)
                .padding(5.dp)
        ) {
            "ART".forEach {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = it.toString(),
                    style = textStyle,
                    textAlign = TextAlign.Center,
                    color = primaryColor,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        Column(
            modifier = Modifier
                .weight(scoreWeight)
                .padding(5.dp)
        ) {
            "CHI".forEach {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = it.toString(),
                    style = textStyle,
                    textAlign = TextAlign.Center,
                    color = primaryColor,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        Column(
            modifier = Modifier
                .weight(scoreWeight)
                .padding(5.dp)
        ) {
            "ENG".forEach {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = it.toString(),
                    style = textStyle,
                    textAlign = TextAlign.Center,
                    color = primaryColor,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        Column(
            modifier = Modifier
                .weight(scoreWeight)
                .padding(5.dp)
        ) {
            "MAT".forEach {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = it.toString(),
                    style = textStyle,
                    textAlign = TextAlign.Center,
                    color = primaryColor,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        Column(
            modifier = Modifier
                .weight(scoreWeight)
                .padding(5.dp)
        ) {
            "P/S".forEach {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = it.toString(),
                    style = textStyle,
                    textAlign = TextAlign.Center,
                    color = primaryColor,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        Column(
            modifier = Modifier
                .weight(scoreWeight)
                .padding(5.dp)
        ) {
            "SES".forEach {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = it.toString(),
                    style = textStyle,
                    textAlign = TextAlign.Center,
                    color = primaryColor,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        Column(
            modifier = Modifier
                .weight(1.3f)
                .padding(5.dp)
        ) {
            "TOT".forEach {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = it.toString(),
                    style = textStyle,
                    textAlign = TextAlign.Center,
                    color = primaryColor,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
    HorizontalDivider(
        thickness = 1.dp,
        color = Color.Black
    )
}

/**
 * Row that displays student performance in each subject. It displays a detailed
 * summary of the learners' performance.
 *
 * @param student the [StudentEntity] instance to display in the row
 */
@Composable
private fun StudentItem(
    student: StudentEntity,
    textStyle: TextStyle = MaterialTheme.typography.bodyMedium
) {
    val modifier = Modifier.fillMaxWidth()
    val scoreWeight = 1f
    val totalScore = getTotalScore(student)
    Row(
        modifier = modifier
            .padding(5.dp)
    ) {
        Text(
            text = student.studentId.toString(),
            modifier = modifier
                .weight(1.2f),
            style = textStyle,
            textAlign = TextAlign.Center
        )
        Text(
            text = student.studentName,
            modifier = modifier
                .weight(6f),
            style = textStyle,
            textAlign = TextAlign.Start
        )
        Text(
            text = student.gender,
            modifier = modifier
                .weight(0.7f),
            style = textStyle,
            textAlign = TextAlign.Center
        )
        Text(
            text = student.arts.toString(),
            modifier = modifier
                .weight(scoreWeight),
            style = textStyle,
            textAlign = TextAlign.Center
        )
        Text(
            text = student.chichewa.toString(),
            modifier = modifier
                .weight(scoreWeight),
            style = textStyle,
            textAlign = TextAlign.Center
        )
        Text(
            text = student.english.toString(),
            modifier = modifier
                .weight(scoreWeight),
            style = textStyle,
            textAlign = TextAlign.Center
        )
        Text(
            text = student.maths.toString(),
            modifier = modifier
                .weight(scoreWeight),
            style = textStyle,
            textAlign = TextAlign.Center
        )
        Text(
            text = student.science.toString(),
            modifier = modifier
                .weight(scoreWeight),
            style = textStyle,
            textAlign = TextAlign.Center
        )
        Text(
            text = student.social.toString(),
            modifier = modifier
                .weight(scoreWeight),
            style = textStyle,
            textAlign = TextAlign.Center
        )
        Text(
            text = totalScore.toString(),
            modifier = modifier
                .weight(1.3f),
            style = textStyle,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold
        )
    }
}

/**
 * Table header for expanded phone screens. This aims at fitting the contents
 * well with the screen.
 *
 * @see [StudentHeader]
 */
@Composable
private fun StudentItemExpandedHeader(
    textStyle: TextStyle = MaterialTheme.typography.titleSmall
) {
    val primaryColor = MaterialTheme.colorScheme.primary
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "ID",
            modifier = Modifier
                .weight(0.5f)
                .padding(5.dp, 0.dp),
            style = textStyle,
            textAlign = TextAlign.Center,
            color = primaryColor,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "STUDENT NAME",
            modifier = Modifier
                .weight(2.4f),
            style = textStyle,
            textAlign = TextAlign.Start,
            color = primaryColor,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "SEX",
            modifier = Modifier
                .weight(0.5f),
            style = textStyle,
            textAlign = TextAlign.Center,
            color = primaryColor,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "ARTS",
            modifier = Modifier
                .weight(1f),
            style = textStyle,
            textAlign = TextAlign.Center,
            color = primaryColor,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "CHICHEWA",
            modifier = Modifier
                .weight(1f),
            style = textStyle,
            textAlign = TextAlign.Center,
            color = primaryColor,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "ENGLISH",
            modifier = Modifier
                .weight(1f),
            style = textStyle,
            textAlign = TextAlign.Center,
            color = primaryColor,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "MATHS",
            modifier = Modifier
                .weight(1f),
            style = textStyle,
            textAlign = TextAlign.Center,
            color = primaryColor,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "SCIENCE",
            modifier = Modifier
                .weight(1f),
            style = textStyle,
            textAlign = TextAlign.Center,
            color = primaryColor,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "SOCIAL",
            modifier = Modifier
                .weight(1f),
            style = textStyle,
            textAlign = TextAlign.Center,
            color = primaryColor,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "TOTAL",
            modifier = Modifier
                .weight(0.6f),
            style = textStyle,
            textAlign = TextAlign.Center,
            color = primaryColor,
            fontWeight = FontWeight.Bold
        )
    }
    HorizontalDivider(
        thickness = 1.dp,
        color = Color.Black
    )
}

/**
 * Table header for expanded phone screens. This aims at fitting the contents
 * well with the screen.
 *
 * @see [StudentHeader]
 */
@Composable
private fun StudentItemExpandedItem(
    student: StudentEntity,
    textStyle: TextStyle = MaterialTheme.typography.bodyMedium
) {
    val totalScore = getTotalScore(student)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = student.studentId.toString(),
            modifier = Modifier
                .weight(0.5f)
                .padding(5.dp, 0.dp),
            textAlign = TextAlign.Center,
            style = textStyle
        )
        Text(
            text = student.studentName,
            modifier = Modifier
                .weight(2.4f),
            textAlign = TextAlign.Start,
            style = textStyle
        )
        Text(
            text = student.gender,
            modifier = Modifier
                .weight(0.5f),
            style = textStyle,
            textAlign = TextAlign.Center
        )
        Text(
            text = student.arts.toString(),
            modifier = Modifier
                .weight(1f),
            style = textStyle,
            textAlign = TextAlign.Center

        )
        Text(
            text = student.chichewa.toString(),
            modifier = Modifier
                .weight(1f),
            style = textStyle,
            textAlign = TextAlign.Center
        )
        Text(
            text = student.english.toString(),
            modifier = Modifier
                .weight(1f),
            style = textStyle,
            textAlign = TextAlign.Center
        )
        Text(
            text = student.maths.toString(),
            modifier = Modifier
                .weight(1f),
            style = textStyle,
            textAlign = TextAlign.Center
        )
        Text(
            text = student.science.toString(),
            modifier = Modifier
                .weight(1f),
            style = textStyle,
            textAlign = TextAlign.Center
        )
        Text(
            text = student.social.toString(),
            modifier = Modifier
                .weight(1f),
            style = textStyle,
            textAlign = TextAlign.Center
        )
        Text(
            text = totalScore.toString(),
            modifier = Modifier
                .weight(0.6f),
            style = textStyle,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun SortTypeDialog(
    state: StudentViewModel.StudentState,
    onDismiss: () -> Unit,
    onGradeSelect: () -> Unit,
    onScoreSelect: () -> Unit,
    onIdSelect: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        Surface(
            modifier = Modifier
                .padding(28.dp)
                .widthIn(min = 280.dp, max = 560.dp),
            shape = RoundedCornerShape(28.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Sort by",
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
                SortSelection(
                    text = "Grade",
                    selected = state.progressSortType == StudentSortType.GRADE,
                    onClick = onGradeSelect
                )
                SortSelection(
                    text = "Total score",
                    selected = state.progressSortType == StudentSortType.SCORE,
                    onClick = onScoreSelect
                )
                SortSelection(
                    text = "Student ID",
                    selected = state.progressSortType == StudentSortType.ID,
                    onClick = onIdSelect
                )
            }
        }
    }
}

@Composable
private fun SortSelection(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    textStyle: TextStyle = MaterialTheme.typography.bodyLarge
) {
    Box {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clickable {
                    onClick()
                }
        ) {
            Box(modifier = Modifier.weight(1f)) {
                Text(
                    text = text,
                    style = textStyle
                )
            }
            RadioButton(
                selected = selected,
                onClick = onClick
            )
        }
    }
}

/*
@Preview(
    showSystemUi = true,
    device = Devices.PHONE
)
@Composable
fun ProgressPhonePreview() {
    ClassEaseTheme {

    }
}

@Preview(
    showSystemUi = true,
    device = MEDIUM
)
@Composable
fun ProgressMediumPreview() {
    ClassEaseTheme {

    }
}

@Preview(
    showSystemUi = true,
    device = EXPANDED
)
@Composable
fun ProgressExpandedPreview() {
    ClassEaseTheme {

    }
}
*/

const val EXPANDED = "spec:id=reference_tablet,shape=Normal,width=841,height=800,unit=dp,dpi=240"
const val MEDIUM = "spec:id=reference_phone,shape=Normal,width=601,height=800,unit=dp,dpi=420"
