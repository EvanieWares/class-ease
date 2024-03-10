package com.evaniewares.classease.utils

/*
@Composable
fun StudentScreenBackup(
    studentViewModel: StudentViewModel,
    navController: NavHostController
) {
    studentViewModel.getStudentsSortById()
    val context = LocalContext.current
    val studentState = studentViewModel.studentState.collectAsStateWithLifecycle().value
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
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    studentViewModel.onAction(StudentViewModel.UserAction.AddButtonClicked)
                }
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add student"
                    )
                    Text(text = "Add new")
                }
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
                if (studentState.isEditing || studentState.isAdding) {
                    EditStudentDialog(
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
                        },
                        onDismissRequest = {
                            studentViewModel.onAction(StudentViewModel.UserAction.EditStudentDialogDismiss)
                        }
                    ) { student ->
                        if (studentState.isEditing) {
                            studentViewModel.updateStudent(student) { success ->
                                if (success) {
                                    studentViewModel.onAction(StudentViewModel.UserAction.OnSaveStudent)
                                    studentViewModel.onAction(StudentViewModel.UserAction.EditStudentDialogDismiss)
                                    toastMsg(context, "Updated!")
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
                }
            }
        }
    }
}
 */

/*
@Composable
fun ProgressScreenBackup(
    navController: NavHostController,
    windowSize: WindowWidthSizeClass,
    studentViewModel: StudentViewModel
) {
    studentViewModel.getStudentsSortByScore()
    val state = studentViewModel.studentState.collectAsStateWithLifecycle().value
    val studentList = studentViewModel.studentList.collectAsStateWithLifecycle().value
    val navigationType: ClassEaseNavigationType
    val isSortDialogShow = rememberSaveable {
        mutableStateOf(false)
    }

    when (windowSize) {
        WindowWidthSizeClass.Compact -> {
            navigationType = ClassEaseNavigationType.BOTTOM_NAVIGATION
        }

        WindowWidthSizeClass.Medium -> {
            navigationType = ClassEaseNavigationType.NAVIGATION_RAIL
        }

        WindowWidthSizeClass.Expanded -> {
            navigationType = ClassEaseNavigationType.PERMANENT_NAVIGATION_DRAWER
        }

        else -> {
            navigationType = ClassEaseNavigationType.BOTTOM_NAVIGATION
        }
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

    Surface {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                CustomTopBar(
                    activity = "Progress",
                    onBackClick = { navController.popBackStack() }
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                when (navigationType) {
                    ClassEaseNavigationType.BOTTOM_NAVIGATION -> {
                        CompactProgressScreen(studentList = studentList)
                    }

                    ClassEaseNavigationType.NAVIGATION_RAIL -> {
                        MediumProgressScreen(studentList = studentList)
                    }

                    else -> {
                        ExpandedProgressScreen(studentList = studentList)
                    }
                }
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
 */

/*
/**
 * Displays a bottom [NavigationBar]
 *
 * @param onSettingsClick this is called when user clicks on Settings.
 * @param onSortClick this is called when user clicks Sort.
 */
@Composable
private fun ProgressBottomBar(
    onSettingsClick: () -> Unit,
    onSortClick: () -> Unit
) {
    NavigationBar {
        NavigationBarItem(
            selected = false,
            onClick = onSettingsClick,
            icon = {
                Icon(
                    imageVector = Icons.Outlined.Settings,
                    contentDescription = "Settings"
                )
            },
            label = {
                Text(text = "Settings")
            }
        )
        NavigationBarItem(
            selected = false,
            onClick = onSortClick,
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_sort),
                    contentDescription = "Sort"
                )
            },
            label = {
                Text(text = "Sort")
            }
        )
    }
}
 */

/*
@Composable
private fun EditStudentDialog(
    state: StudentViewModel.StudentState,
    onIdChanged: (String) -> Unit,
    onNameChanged: (String) -> Unit,
    onGenderChanged: (String) -> Unit,
    onDismissRequest: () -> Unit,
    onSaveStudent: (StudentEntity) -> Unit
) {
    val scrollState = rememberScrollState()
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        Surface(
            modifier = Modifier
                .padding(14.dp)
                .widthIn(min = 280.dp, max = 560.dp),
            shape = RoundedCornerShape(28.dp)
        ) {
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
                        keyboardType = KeyboardType.Decimal
                    )
                }
                EditStudentTextField(
                    value = state.studentName,
                    placeHolder = "STUDENT NAME",
                    onValueChange = onNameChanged
                )
                BoxWithConstraints {
                    if (maxWidth < 300.dp) {
                        ColumnGenderSection(
                            state = state,
                            onGenderChanged = onGenderChanged
                        )
                    } else {
                        RowGenderSection(
                            state = state,
                            onGenderChanged = onGenderChanged
                        )
                    }
                }
                Spacer(modifier = Modifier.padding(5.dp))
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
                    Text(
                        text = "Save",
                        style = MaterialTheme.typography.labelMedium
                    )
                }
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

@Composable
private fun ColumnGenderSection(
    state: StudentViewModel.StudentState,
    onGenderChanged: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
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
        maxLines = 1
    )
}
 */
