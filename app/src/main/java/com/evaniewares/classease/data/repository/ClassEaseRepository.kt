package com.evaniewares.classease.data.repository

import com.evaniewares.classease.data.local.StudentDao
import com.evaniewares.classease.domain.model.StudentEntity
import kotlinx.coroutines.flow.Flow

class ClassEaseRepository(
    private val studentDao: StudentDao
) {
    suspend fun insertStudent(studentEntity: StudentEntity): Long = studentDao.insertStudent(studentEntity)

    suspend fun updateStudent(studentEntity: StudentEntity) = studentDao.updateStudent(studentEntity)

    suspend fun deleteStudent(studentEntity: StudentEntity): Int = studentDao.deleteStudent(studentEntity)

    suspend fun getStudentById(studentId: Long): StudentEntity? = studentDao.getStudentById(studentId)

    fun getStudentsSortByGrade(): Flow<List<StudentEntity>> = studentDao.getStudentsSortByGrade()

    fun getStudentsSortById(): Flow<List<StudentEntity>> = studentDao.getStudentsSortById()

    fun getStudentsSortByScore(): Flow<List<StudentEntity>> = studentDao.getStudentsSortByScore()
}