package com.bbeniful.domain.model

data class Workout(
    val workoutForWeek: Map<String, List<Exercise>>
)