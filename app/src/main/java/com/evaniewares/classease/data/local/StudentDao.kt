package com.evaniewares.classease.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.evaniewares.classease.domain.model.StudentEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StudentDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertStudent(studentEntity: StudentEntity): Long

    @Update
    suspend fun updateStudent(studentEntity: StudentEntity)

    @Update
    suspend fun updateAllStudents(students: List<StudentEntity>)

    @Delete
    suspend fun deleteStudent(studentEntity: StudentEntity): Int

    @Delete
    suspend fun deleteAllStudents(students: List<StudentEntity>): Int

    @Query("SELECT * from students WHERE student_id = :studentId")
    suspend fun getStudentById(studentId: Long): StudentEntity?

    @Query("SELECT * FROM students ORDER BY grade_group DESC, english DESC, maths DESC, science DESC")
    fun getStudentsSortByGrade(): Flow<List<StudentEntity>>

    @Query("SELECT * FROM students ORDER BY student_id")
    fun getStudentsSortById(): Flow<List<StudentEntity>>

    @Query("SELECT * FROM students ORDER BY arts + chichewa + english + maths + science + social DESC, english DESC, maths DESC, science DESC")
    fun getStudentsSortByScore(): Flow<List<StudentEntity>>
}