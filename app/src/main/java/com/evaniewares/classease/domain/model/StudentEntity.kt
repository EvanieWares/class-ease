package com.evaniewares.classease.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "students")
data class StudentEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "student_id")
    val studentId: Long = 0,
    @ColumnInfo(name = "student_name", index = true)
    val studentName: String,
    val gender: String,
    val arts: Int = 0,
    val chichewa: Int = 0,
    val english: Int = 0,
    val maths: Int = 0,
    val science: Int = 0,
    val social: Int = 0,
    @ColumnInfo(name = "grade_group", index = true)
    val gradeGroup: Int = 0
) {
    fun calculateGradeGroup(): Int {
        return (
                getGrade(arts) +
                getGrade(chichewa) +
                getGrade(english) +
                getGrade(maths) +
                getGrade(science) +
                getGrade(social)
                )
    }

    private fun getGrade(score: Int): Int {
        return when {
            score >= 80 -> 4
            score >= 66 -> 3
            score >= 56 -> 2
            score >= 40 -> 1
            else -> 0
        }
    }
}
