package com.evaniewares.classease.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.evaniewares.classease.data.local.StudentDao
import com.evaniewares.classease.domain.model.StudentEntity

@Database(
    entities = [StudentEntity::class],
    version = 1
)
abstract class ClassEaseDatabase : RoomDatabase() {
    abstract fun studentDao(): StudentDao
}