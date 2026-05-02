package com.bbeniful.data.repository

import com.bbeniful.data.datasource.ExerciseDataSource
import com.bbeniful.data.mapper.toData
import com.bbeniful.data.mapper.toExerciseDomain
import com.bbeniful.domain.model.Exercise
import com.bbeniful.domain.repository.ExerciseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Single

@Single
class ExerciseRepositoryImpl(
    private val exerciseDataSource: ExerciseDataSource
) : ExerciseRepository {
    override fun getAll(): Flow<List<Exercise>> = exerciseDataSource.getAll().map { it.toExerciseDomain }

    override suspend fun add(exercise: Exercise) = exerciseDataSource.add(exerciseDto = exercise.toData)
}