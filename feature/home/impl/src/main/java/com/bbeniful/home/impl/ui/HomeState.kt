package com.bbeniful.home.impl.ui

import androidx.compose.runtime.Immutable
import com.bbeniful.domain.model.Day
import com.bbeniful.domain.model.Exercise
import com.bbeniful.domain.model.Progress

@Immutable
data class HomeState(
    val dailyBodyPart: String = "",
    val exercises: List<Exercise> = emptyList(),
    val currentDay: Day = Day.Unknown,
    val userSelectedDay: Day? = null,
    val progresses: List<Progress> = emptyList()
)

sealed interface HomeIntent {
    data object SelectCurrentDay: HomeIntent
    data class DaySelected(val selectedDay: Day): HomeIntent
    data object ResetSelectedDay: HomeIntent

    data class SaveProgress(val id: Int, val min: Int, val max: Int): HomeIntent
}