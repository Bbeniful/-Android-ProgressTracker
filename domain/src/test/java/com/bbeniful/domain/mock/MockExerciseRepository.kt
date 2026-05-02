package com.bbeniful.domain.mock

import com.bbeniful.domain.model.Exercise
import com.bbeniful.domain.model.weeklyExercises
import com.bbeniful.domain.repository.ExerciseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

val mockExerciseRepo = object : ExerciseRepository {
    override suspend fun add(exercise: Exercise) {
    }

    override fun getAll(): Flow<List<Exercise>> = flowOf(
        weeklyExercises
    )

}