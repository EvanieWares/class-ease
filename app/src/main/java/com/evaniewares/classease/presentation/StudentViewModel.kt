package com.evaniewares.classease.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.evaniewares.classease.ROOM_DATABASE
import com.evaniewares.classease.data.repository.ClassEaseRepository
import com.evaniewares.classease.domain.model.StudentEntity
import com.evaniewares.classease.utils.StudentSortType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StudentViewModel @Inject constructor(
    private val repository: ClassEaseRepository
) : ViewModel() {
    private val _studentState = MutableStateFlow(StudentState())
    private val _studentList = MutableStateFlow<List<StudentEntity>>(emptyList())
    val studentList: StateFlow<List<StudentEntity>> = _studentList.asStateFlow()
    val studentState: StateFlow<StudentState> = _studentState.asStateFlow()

    init {
        getStudentsSortByScore()
    }

    suspend fun getStudentById(studentId: Long): StudentEntity? {
        return repository.getStudentById(studentId)
    }

    fun getStudentsSortByScore() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getStudentsSortByScore().collect { sortedStudents ->
                _studentList.value = sortedStudents
            }
        }
    }

    fun getStudentsSortByGrade() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getStudentsSortByGrade().collect { sortedStudents ->
                _studentList.value = sortedStudents
            }
        }
    }

    fun getStudentsSortById() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getStudentsSortById().collect { sortedStudents ->
                _studentList.value = sortedStudents
            }
        }
    }

    fun insertStudent(student: StudentEntity, success: (Boolean, Boolean) -> Unit) {
        if (studentList.value.size >= 10) {
            success(false, true)
        } else {
            viewModelScope.launch {
                val result = repository.insertStudent(student)
                if (result != -1L) {
                    repository.insertStudent(student)
                    Log.d(ROOM_DATABASE, "Insert ${student.studentName} success!")
                    success(true, false)
                } else {
                    Log.e(ROOM_DATABASE, "Insert ${student.studentName} error!")
                    success(false, false)
                }
            }
        }
    }

    fun updateStudent(student: StudentEntity, success: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                repository.updateStudent(student)
                Log.d(ROOM_DATABASE, "Update ${student.studentName} success!")
                success(true)
            } catch (e: Exception) {
                Log.e(ROOM_DATABASE, "Update ${student.studentName} error!", e)
                success(false)
            }
        }
    }

    fun deleteStudent(student: StudentEntity, success: (Boolean) -> Unit) {
        viewModelScope.launch {
            val result = repository.deleteStudent(student)
            if (result < 0) {
                success(false)
            } else {
                success(true)
            }
        }
    }

    fun deleteAllStudents(success: (Boolean) -> Unit) {
        viewModelScope.launch {
            val result = repository.deleteAllStudents(studentList.value)
            if (result < 0) {
                success(false)
            } else {
                success(true)
            }
        }
    }

    fun clearAllScores(success: (Boolean) -> Unit) {
        val allStudents = emptyList<StudentEntity>().toMutableList()
        studentList.value.map {
            allStudents.add(it.copy(
                arts = 0,
                chichewa = 0,
                english = 0,
                maths = 0,
                science = 0,
                social = 0,
                gradeGroup = 0
            ))
        }
        viewModelScope.launch {
            try {
                repository.updateAllStudents(allStudents)
                Log.d(ROOM_DATABASE, "Update success!")
                success(true)
            } catch (e: Exception) {
                Log.e(ROOM_DATABASE, "Update error!", e)
                success(false)
            }
        }
    }

    fun onSortTypeChange(sortType: StudentSortType) {
        _studentState.update {
            it.copy(
                progressSortType = sortType
            )
        }
    }

    fun onAction(userAction: UserAction) {
        when (userAction) {
            UserAction.AddButtonClicked -> {
                _studentState.update {
                    it.copy(
                        selectedStudent = null,
                        isAdding = true,
                        isEditing = false
                    )
                }
            }

            is UserAction.DeleteButtonClicked -> {
                _studentState.update {
                    it.copy(
                        selectedStudent = userAction.studentEntity,
                        isDeleting = true
                    )
                }
            }

            is UserAction.EditButtonClicked -> {
                _studentState.update {
                    it.copy(
                        selectedStudent = userAction.studentEntity,
                        isEditing = true,
                        isAdding = false,
                        studentName = userAction.studentEntity.studentName,
                        gender = userAction.studentEntity.gender,
                        studentId = userAction.studentEntity.studentId.toString()
                    )
                }
            }

            UserAction.EditStudentDialogDismiss -> {
                _studentState.update {
                    it.copy(
                        isEditing = false,
                        isAdding = false,
                        studentName = "",
                        gender = "",
                        studentId = "",
                        selectedStudent = null
                    )
                }
            }

            UserAction.DeleteStudentDialogDismiss -> {
                _studentState.update {
                    it.copy(
                        isDeleting = false,
                        selectedStudent = null
                    )
                }
            }

            is UserAction.OnGenderChanged -> {
                _studentState.update {
                    it.copy(
                        gender = userAction.gender
                    )
                }
            }

            is UserAction.OnIDChanged -> {
                _studentState.update {
                    it.copy(
                        studentId = userAction.studentId
                    )
                }
            }

            is UserAction.OnNameChanged -> {
                _studentState.update {
                    it.copy(
                        studentName = userAction.studentName
                    )
                }
            }

            is UserAction.OnSaveStudent -> {
                _studentState.update {
                    it.copy(
                        studentName = "",
                        gender = "",
                        studentId = ""
                    )
                }
            }
        }
    }

    data class StudentState(
        val progressSortType: StudentSortType = StudentSortType.SCORE,
        val isEditing: Boolean = false,
        val isAdding: Boolean = false,
        val isDeleting: Boolean = false,
        val studentId: String = "",
        val studentName: String = "",
        val gender: String = "",
        val selectedStudent: StudentEntity? = null
    )

    sealed class UserAction {
        data object AddButtonClicked : UserAction()
        data object EditStudentDialogDismiss : UserAction()
        data object DeleteStudentDialogDismiss : UserAction()
        data class EditButtonClicked(val studentEntity: StudentEntity) : UserAction()
        data class DeleteButtonClicked(val studentEntity: StudentEntity) : UserAction()
        data class OnNameChanged(val studentName: String) : UserAction()
        data class OnIDChanged(val studentId: String) : UserAction()
        data class OnGenderChanged(val gender: String) : UserAction()
        data object OnSaveStudent : UserAction()
    }
}