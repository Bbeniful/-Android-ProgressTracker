package com.bbeniful.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("progress_table")
data class ProgressDto(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val exerciseId: Int,
    val timestamp: String,
    val min: Int,
    val max: Int
)