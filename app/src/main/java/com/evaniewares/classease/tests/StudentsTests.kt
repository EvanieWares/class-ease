package com.evaniewares.classease.tests

import com.evaniewares.classease.domain.model.StudentEntity
import com.evaniewares.classease.utils.calculateGradeGroup
import kotlin.random.Random

private val genders = listOf("M", "F")
private val names = listOf(
    "John", "Emma", "Michael", "Olivia", "William", "Ava", "James", "Isabella", "Oliver", "Sophia",
    "Benjamin", "Charlotte", "Elijah", "Amelia", "Lucas", "Mia", "Mason", "Harper", "Logan", "Evelyn",
    "Alexander", "Abigail", "Ethan", "Emily", "Daniel", "Ella", "Matthew", "Madison", "Henry", "Scarlett"
)

private fun generateRandomStudent(): StudentEntity {
    val name = (names.random() + " " + names.random()).uppercase()
    val gender = genders.random()
    val arts = Random.nextInt(0, 101)
    val chichewa = Random.nextInt(0, 101)
    val english = Random.nextInt(0, 101)
    val maths = Random.nextInt(0, 101)
    val science = Random.nextInt(0, 101)
    val social = Random.nextInt(0, 101)

    return StudentEntity(
        studentName = name,
        gender = gender,
        arts = arts,
        chichewa = chichewa,
        english = english,
        maths = maths,
        science = science,
        social = social,
        gradeGroup = calculateGradeGroup(arts, chichewa, english, maths, science, social)
    )
}

private val students = List(109) { generateRandomStudent().copy(studentId = it.toLong() + 1) }

val studentListTest = students + StudentEntity(110, "CHISOMO PSYELERA", "M", 100,100,100,100,100,100, 24)
