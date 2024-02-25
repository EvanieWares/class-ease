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

    @Delete
    suspend fun deleteStudent(studentEntity: StudentEntity): Int

    @Query(
        "SELECT * FROM students " +
                "ORDER BY " +
                "CASE " +
                "WHEN arts >= 80 AND chichewa >= 80 AND english >= 80 AND maths >= 80 AND science >= 80 AND social >= 80 THEN 1 " +
                "WHEN (arts >= 66 AND arts < 80) AND (chichewa >= 66 AND chichewa < 80) AND (english >= 66 AND english < 80) AND (maths >= 66 AND maths < 80) AND (science >= 66 AND science < 80) AND (social >= 66 AND social < 80) THEN 2 " +
                "WHEN (arts >= 56 AND arts < 66) AND (chichewa >= 56 AND chichewa < 66) AND (english >= 56 AND english < 66) AND (maths >= 56 AND maths < 66) AND (science >= 56 AND science < 66) AND (social >= 56 AND social < 66) THEN 3 " +
                "WHEN (arts >= 40 AND arts < 56) AND (chichewa >= 40 AND chichewa < 56) AND (english >= 40 AND english < 56) AND (maths >= 40 AND maths < 56) AND (science >= 40 AND science < 56) AND (social >= 40 AND social < 56) THEN 4 " +
                "ELSE 5 " +
                "END"
    )
    fun getStudentsSortByGrade(): Flow<List<StudentEntity>>

    @Query("SELECT * FROM students ORDER BY student_id")
    fun getStudentsSortById(): Flow<List<StudentEntity>>

    @Query("SELECT * FROM students ORDER BY arts + chichewa + english + maths + science + social")
    fun getStudentsSortByScore(): Flow<List<StudentEntity>>
}