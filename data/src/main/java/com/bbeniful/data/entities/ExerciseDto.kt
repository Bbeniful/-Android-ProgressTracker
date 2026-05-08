package com.bbeniful.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bbeniful.domain.model.MuscleGroup
import com.bbeniful.domain.model.Day

@Entity("exercise_table")
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