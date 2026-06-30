package com.bbeniful.feature.improvement.impl.domain

import com.bbeniful.domain.model.Exercise
import com.bbeniful.domain.model.MuscleGroup
import com.bbeniful.domain.model.Progress
import java.time.LocalDate

data class ImprovementContext(
    val selectedMuscleGroup: MuscleGroup?,
    val relevantExercises: List<Exercise>,
    val progressByExerciseId: Map<Int, List<Progress>>,
    val today: LocalDate
)
