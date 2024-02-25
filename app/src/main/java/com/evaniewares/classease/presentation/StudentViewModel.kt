package com.evaniewares.classease.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.evaniewares.classease.ROOM_DATABASE
import com.evaniewares.classease.data.repository.ClassEaseRepository
import com.evaniewares.classease.domain.model.StudentEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StudentViewModel @Inject constructor(
    private val repository: ClassEaseRepository
) : ViewModel() {
    private val _studentList = MutableStateFlow<List<StudentEntity>>(emptyList())
    val studentList: StateFlow<List<StudentEntity>> = _studentList.asStateFlow()

    init {
        getStudentsSortByScore()
    }

    fun getStudentsSortByScore() {
        viewModelScope.launch {
            repository.getStudentsSortByScore().collect { sortedStudents ->
                _studentList.value = sortedStudents
            }
        }
    }

    fun getStudentsSortByGrade() {
        viewModelScope.launch {
            repository.getStudentsSortByGrade().collect { sortedStudents ->
                _studentList.value = sortedStudents
            }
        }
    }

    fun getStudentsSortById() {
        viewModelScope.launch {
            repository.getStudentsSortById().collect { sortedStudents ->
                _studentList.value = sortedStudents
            }
        }
    }

    fun insertStudent(student: StudentEntity, success: (Boolean) -> Unit) {
        viewModelScope.launch {
            val result = repository.insertStudent(student)
            if (result != -1L) {
                repository.insertStudent(student)
                Log.e(ROOM_DATABASE, "Insert ${student.studentName} success!")
                success(true)
            } else {
                Log.e(ROOM_DATABASE, "Insert ${student.studentName} error!")
                success(false)
            }
        }
    }

    fun updateStudent(student: StudentEntity, success: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                repository.updateStudent(student)
                Log.e(ROOM_DATABASE, "Update ${student.studentName} success!")
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
}