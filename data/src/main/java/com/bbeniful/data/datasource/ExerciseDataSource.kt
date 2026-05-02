package com.bbeniful.data.datasource

import com.bbeniful.data.dao.ExerciseDao
import com.bbeniful.data.entities.ExerciseDto
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Single

interface ExerciseDataSource {

    fun getAll(): Flow<List<ExerciseDto>>

    suspend fun add(exerciseDto: ExerciseDto)
}

@Single(binds = [ExerciseDataSource::class])
class ExerciseDataSourceImpl(
    private val exerciseDao: ExerciseDao
) : ExerciseDataSource {
    override fun getAll(): Flow<List<ExerciseDto>> = exerciseDao.getAllExercise()

    override suspend fun add(exerciseDto: ExerciseDto) = exerciseDao.add(exerciseDto = exerciseDto)

}
