package com.bbeniful.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.bbeniful.data.entities.ExerciseDto
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Single
import org.koin.core.annotation.Singleton

@Singleton
@Dao
interface ExerciseDao {

    @Query("SELECT * FROM exercise_table")
    fun getAllExercise(): Flow<List<ExerciseDto>>

    @Upsert
    fun add(exerciseDto: ExerciseDto)
}