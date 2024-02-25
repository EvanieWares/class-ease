package com.evaniewares.classease.di

import android.content.Context
import androidx.room.Room
import com.evaniewares.classease.data.ClassEaseDatabase
import com.evaniewares.classease.data.local.StudentDao
import com.evaniewares.classease.data.repository.ClassEaseRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideLocalDatabase(@ApplicationContext context: Context): ClassEaseDatabase {
        return Room.databaseBuilder(
            context,
            ClassEaseDatabase::class.java,
            "class_ease_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideClassDao(db: ClassEaseDatabase) = db.studentDao()

    @Provides
    @Singleton
    fun provideClassEaseRepository(
        studentDao: StudentDao
    ): ClassEaseRepository = ClassEaseRepository(
        studentDao = studentDao
    )
}