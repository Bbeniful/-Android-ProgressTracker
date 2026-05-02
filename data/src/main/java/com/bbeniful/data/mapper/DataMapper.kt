package com.bbeniful.data.mapper

import com.bbeniful.data.entities.ExerciseDto
import com.bbeniful.data.entities.ProgressDto
import com.bbeniful.domain.model.Exercise
import com.bbeniful.domain.model.Progress

val ExerciseDto.toDomain: Exercise
    get() = Exercise(
        id = id,
        name = name,
        circle = circle,
        rep = rep,
        day = day,
        type = type,
        isActive = isActive
    )

val Exercise.toData: ExerciseDto
    get() = ExerciseDto(
        id = id,
        name = name,
        circle = circle,
        rep = rep,
        day = day,
        type = type,
        isActive = isActive
    )

val ProgressDto.toDomain: Progress
    get() = Progress(
        id = id,
        exerciseId = exerciseId,
        min = min,
        max = max,
        timestamp = timestamp
    )

val Progress.toData: ProgressDto
    get() = ProgressDto(
        id = id,
        exerciseId = exerciseId,
        min = min,
        max = max,
        timestamp = timestamp
    )

val List<ExerciseDto>.toExerciseDomain: List<Exercise>
    get() = this.map { it.toDomain }

val List<ProgressDto>.toProgressDomain: List<Progress>
    get() = this.map { it.toDomain }