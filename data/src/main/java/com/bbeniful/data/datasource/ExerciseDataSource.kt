package com.bbeniful.data.datasource

import com.bbeniful.data.dao.ExerciseDao
import com.bbeniful.data.entities.ExerciseDto
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Single

interface ExerciseDataSource {

    fun getAll(): Flow<List<ExerciseDto>>

    fun getById(id: Int): Flow<ExerciseDto?>

    suspend fun add(exerciseDto: ExerciseDto)

    suspend fun deleteById(id: Int)

    suspend fun getByDayAndOrder(day: String, order: Int): ExerciseDto?

    suspend fun updateCompletedDate(id: Int, date: String?)
}

@Single(binds = [ExerciseDataSource::class])
class ExerciseDataSourceImpl(
    private val exerciseDao: ExerciseDao
) : ExerciseDataSource {

    override fun getAll(): Flow<List<ExerciseDto>> = exerciseDao.getAllExercise()

    override fun getById(id: Int): Flow<ExerciseDto?> = exerciseDao.getById(id)

    override suspend fun add(exerciseDto: ExerciseDto) = exerciseDao.add(exerciseDto = exerciseDto)

    override suspend fun deleteById(id: Int) = exerciseDao.deleteById(id)

    override suspend fun getByDayAndOrder(day: String, order: Int): ExerciseDto? =
        exerciseDao.getByDayAndOrder(day, order)

    override suspend fun updateCompletedDate(id: Int, date: String?) =
        exerciseDao.updateCompletedDate(id, date)
}
