package com.bbeniful.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.bbeniful.data.dao.ExerciseDao
import com.bbeniful.data.dao.ProgressDao
import com.bbeniful.data.entities.ExerciseDto
import com.bbeniful.data.entities.ProgressDto

@Database(
    version = 2,
    entities = [ExerciseDto::class, ProgressDto::class],
    exportSchema = false
)
abstract class ProgressTrackDatabase : RoomDatabase() {

    abstract fun exerciseDao(): ExerciseDao

    abstract fun progressDao(): ProgressDao

    companion object {
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // new columns
                database.execSQL("ALTER TABLE exercise_table ADD COLUMN orderOnDay INTEGER NOT NULL DEFAULT 0")
                database.execSQL("ALTER TABLE exercise_table ADD COLUMN completedDate TEXT")
                // indexes
                database.execSQL("CREATE INDEX IF NOT EXISTS index_progress_table_exerciseId_timestamp ON progress_table (exerciseId, timestamp)")
                database.execSQL("CREATE INDEX IF NOT EXISTS index_progress_table_timestamp ON progress_table (timestamp)")
                database.execSQL("CREATE INDEX IF NOT EXISTS index_exercise_table_day_orderOnDay ON exercise_table (day, orderOnDay)")
            }
        }
    }
}