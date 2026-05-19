package com.bbeniful.data.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.bbeniful.domain.model.MuscleGroup
import com.bbeniful.domain.model.Day

@Entity(
    tableName = "exercise_table",
    indices = [
        // covers getByDayAndOrder conflict check and the day-based workout plan queries
        Index(value = ["day", "orderOnDay"])
    ]
)
data class ExerciseDto(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val name: String,
    val isActive: Boolean,
    val circle: Int,
    val rep: Int,
    val day: String = Day.Unknown.raw,
    val type: String = MuscleGroup.Unknown.raw,
    val orderOnDay: Int = 0,
    val completedDate: String? = null
)