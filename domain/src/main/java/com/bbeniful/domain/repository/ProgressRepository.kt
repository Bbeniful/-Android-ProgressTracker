package com.bbeniful.domain.repository

import com.bbeniful.domain.model.Progress
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Single


interface ProgressRepository {

    fun getProgressForExercise(exerciseId: Int): Flow<List<Progress>>

    suspend fun add(progress: Progress)

    suspend fun remove(progress: Progress)
}