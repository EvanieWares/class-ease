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
)
