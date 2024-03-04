package com.evaniewares.classease.data

import android.app.Activity
import android.content.Context
import com.evaniewares.classease.utils.StudentSortType

class SharedPrefs(activity: Activity) {
    private val sortTypeKey = "sort_type"
    private val sharedPrefs = activity.getPreferences(Context.MODE_PRIVATE)

    fun getStudentSortType(): StudentSortType {
        val sortTypeText =
            sharedPrefs.getString(sortTypeKey, StudentSortType.SCORE.name.lowercase()) ?: ""
        return when (sortTypeText) {
            StudentSortType.GRADE.name.lowercase() -> {
                StudentSortType.GRADE
            }

            StudentSortType.ID.name.lowercase() -> {
                StudentSortType.ID
            }

            else -> {
                StudentSortType.SCORE
            }
        }
    }

    fun setStudentSortType(sortType: StudentSortType) {
        val editor = sharedPrefs.edit()
        editor.putString(sortTypeKey, sortType.name.lowercase())
        editor.apply()
    }
}