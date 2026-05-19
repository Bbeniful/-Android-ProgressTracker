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

    @Query("SELECT * FROM exercise_table WHERE id = :id")
    fun getById(id: Int): Flow<ExerciseDto?>

    @Upsert
    fun add(exerciseDto: ExerciseDto)

    @Query("DELETE FROM exercise_table WHERE id = :id")
    suspend fun deleteById(id: Int)

    @Query("SELECT * FROM exercise_table WHERE day = :day AND orderOnDay = :order AND orderOnDay > 0 LIMIT 1")
    suspend fun getByDayAndOrder(day: String, order: Int): ExerciseDto?

    @Query("UPDATE exercise_table SET completedDate = :date WHERE id = :id")
    suspend fun updateCompletedDate(id: Int, date: String?)
}