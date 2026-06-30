package com.bbeniful.feature.improvement.impl.ui

import androidx.compose.runtime.Immutable
import com.bbeniful.domain.model.MuscleGroup
import com.bbeniful.feature.improvement.impl.domain.model.Recommendation

@Immutable
data class ImprovementState(
    val isLoading: Boolean = false,
    val selectedMuscleGroup: MuscleGroup? = null,
    val recommendations: List<Recommendation> = emptyList()
)

sealed interface ImprovementIntent {
    data class SelectMuscleGroup(val muscleGroup: MuscleGroup?) : ImprovementIntent
}
