package com.bbeniful.feature.statistic.impl.ui

import androidx.compose.runtime.Immutable
import com.bbeniful.domain.model.Exercise
import com.bbeniful.domain.model.Progress

@Immutable
data class StatisticState(
    val isLoading: Boolean = false,
    val exercises: List<Exercise> = emptyList(),
    val selectedExercise: Exercise? = null,
    val progresses: List<Progress> = emptyList(),
    val error: String? = null
)

sealed interface StatisticIntent {
    data class SelectExercise(val exercise: Exercise) : StatisticIntent
    data object ClearSelection : StatisticIntent
}
