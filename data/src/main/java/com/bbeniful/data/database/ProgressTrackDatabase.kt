package com.bbeniful.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.bbeniful.data.dao.ExerciseDao
import com.bbeniful.data.dao.ProgressDao
import com.bbeniful.data.entities.ExerciseDto
import com.bbeniful.data.entities.ProgressDto

@Database(
    version = 1,
    entities = [ExerciseDto::class, ProgressDto::class],
    exportSchema = false
)
abstract class ProgressTrackDatabase: RoomDatabase() {

    abstract fun exerciseDao(): ExerciseDao

    abstract fun progressDao(): ProgressDao
}