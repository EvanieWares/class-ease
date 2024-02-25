package com.evaniewares.classease.presentation

import androidx.lifecycle.ViewModel
import com.evaniewares.classease.domain.model.StudentEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class EditStudentViewModel : ViewModel() {
    private val _editState = MutableStateFlow(EditState())
    var editState: StateFlow<EditState> = _editState.asStateFlow()

    // Handle user actions
    fun onAction(userAction: UserAction){
        when(userAction){
            UserAction.AddButtonClicked -> {
                _editState.value = _editState.value.copy(
                    selectedStudent = null,
                    isAdding = true
                )
            }

            is UserAction.DeleteButtonClicked -> {
                _editState.value = _editState.value.copy(
                    selectedStudent = userAction.studentEntity,
                    isDeleting = true
                )
            }
            is UserAction.EditButtonClicked -> {
                _editState.value = _editState.value.copy(
                    selectedStudent = userAction.studentEntity,
                    isEditing = true,
                    studentName = userAction.studentEntity.studentName,
                    gender = userAction.studentEntity.gender,
                    studentId = userAction.studentEntity.studentId.toString(),
                )
            }

            UserAction.EditStudentDialogDismiss -> {
                _editState.value = _editState.value.copy(
                    isEditing = false,
                    isAdding = false,
                    studentName = "",
                    gender = "",
                    studentId = "",
                    selectedStudent = null
                )
            }

            UserAction.DeleteStudentDialogDismiss -> {
                _editState.value = _editState.value.copy(
                    isDeleting = false,
                    selectedStudent = null
                )
            }

            is UserAction.OnGenderChanged -> {
                _editState.value = _editState.value.copy(
                    gender = userAction.gender
                )
            }
            is UserAction.OnIDChanged -> {
                _editState.value = _editState.value.copy(
                    studentId = userAction.studentId
                )
            }
            is UserAction.OnNameChanged -> {
                _editState.value = _editState.value.copy(
                    studentName = userAction.studentName
                )
            }

            is UserAction.OnSaveStudent -> {
                _editState.value = _editState.value.copy(
                    studentName = "",
                    gender = "",
                    studentId = ""
                )
            }
        }
    }

    data class EditState(
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