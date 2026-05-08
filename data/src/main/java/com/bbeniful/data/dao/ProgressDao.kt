package com.bbeniful.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.bbeniful.data.entities.ProgressDto
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Singleton

@Singleton
@Dao
interface ProgressDao {

    @Query("SELECT * FROM progress_table WHERE exerciseId = :exerciseId")
    fun getProgressForExercise(exerciseId: Int): Flow<List<ProgressDto>>?

    @Upsert
    fun add(progressDto: ProgressDto)

    @Delete
    fun remove(progressDto: ProgressDto)

    @Query("DELETE FROM progress_table WHERE timestamp < :cutoffDate")
    suspend fun deleteOlderThan(cutoffDate: String)
}

