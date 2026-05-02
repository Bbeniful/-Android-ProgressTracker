package com.bbeniful.domain.repository

import com.bbeniful.domain.model.Exercise
import kotlinx.coroutines.flow.Flow

interface ExerciseRepository {

    fun getAll(): Flow<List<Exercise>>

    suspend fun add(exercise: Exercise)
}