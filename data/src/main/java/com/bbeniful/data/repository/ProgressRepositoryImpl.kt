package com.bbeniful.data.repository

import com.bbeniful.data.datasource.ProgressDataSource
import com.bbeniful.data.mapper.toData
import com.bbeniful.data.mapper.toProgressDomain
import com.bbeniful.domain.model.Progress
import com.bbeniful.domain.repository.ExerciseRepository
import com.bbeniful.domain.repository.ProgressRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Single

@Single(binds = [ProgressRepository::class])
class ProgressRepositoryImpl(
    private val progressDataSource: ProgressDataSource
) : ProgressRepository {
    override fun getProgressForExercise(exerciseId: Int): Flow<List<Progress>> =
        progressDataSource.getProgressForExercise(exerciseId = exerciseId)?.map { it.toProgressDomain }?: flowOf(emptyList())

    override suspend fun add(progress: Progress) =
        progressDataSource.add(progressDto = progress.toData)

    override suspend fun remove(progress: Progress) =
        progressDataSource.remove(progressDto = progress.toData)
}