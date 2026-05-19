package com.bbeniful.data.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "progress_table",
    indices = [
        // covers getProgressForExercise (filter + sort by timestamp in one index)
        Index(value = ["exerciseId", "timestamp"]),
        // covers deleteOlderThan (timestamp-only predicate can't use the composite above)
        Index(value = ["timestamp"])
    ]
)
data class ProgressDto(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val exerciseId: Int,
    val timestamp: String,
    val min: Int,
    val max: Int
)