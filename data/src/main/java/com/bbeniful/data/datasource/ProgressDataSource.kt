package com.bbeniful.data.datasource

import com.bbeniful.data.dao.ProgressDao
import com.bbeniful.data.entities.ProgressDto
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Single

interface ProgressDataSource {


    fun getProgressForExercise(exerciseId: Int): Flow<List<ProgressDto>>?

    suspend fun add(progressDto: ProgressDto)

    suspend fun remove(progressDto: ProgressDto)

    suspend fun deleteOlderThan(cutoffDate: String)
}

@Single(binds = [ProgressDataSource::class])
class ProgressDataSourceImpl(
    private val progressDao: ProgressDao
) : ProgressDataSource {
    override fun getProgressForExercise(exerciseId: Int): Flow<List<ProgressDto>>? = progressDao.getProgressForExercise(exerciseId = exerciseId)

    override suspend fun add(progressDto: ProgressDto) = progressDao.add(progressDto = progressDto)

    override suspend fun remove(progressDto: ProgressDto) = progressDao.remove(progressDto = progressDto)

    override suspend fun deleteOlderThan(cutoffDate: String) = progressDao.deleteOlderThan(cutoffDate)
}