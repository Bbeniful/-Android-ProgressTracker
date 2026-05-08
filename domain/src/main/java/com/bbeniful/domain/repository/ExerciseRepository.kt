package com.bbeniful.domain.repository

import com.bbeniful.domain.model.Exercise
import kotlinx.coroutines.flow.Flow

interface ExerciseRepository {

    fun getAll(): Flow<List<Exercise>>

    fun getById(id: Int): Flow<Exercise?>

    suspend fun add(exercise: Exercise)

    suspend fun deleteById(id: Int)

    suspend fun getByDayAndOrder(day: String, order: Int): Exercise?

    suspend fun updateCompletedDate(id: Int, date: String?)
}