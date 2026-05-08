package com.bbeniful.domain.model

data class Exercise (
    val id: Int = 0,
    val name: String,
    val isActive: Boolean = false,
    val sets: Int,
    val rep: Int,
    val day: String = Day.Unknown.raw,
    val muscleGroup: String = MuscleGroup.Unknown.raw,
    val orderOnDay: Int = 0,
    val completedDate: String? = null
)
