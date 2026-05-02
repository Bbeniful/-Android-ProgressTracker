package com.bbeniful.domain.usecase

import com.bbeniful.domain.model.Day
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Factory

//@Factory
class GetDailyBodyUseCase(
    private val createWeekWorkoutPlaneUseCase: CreateWeekWorkoutPlaneUseCase
) {

    operator fun invoke(day: Day) = createWeekWorkoutPlaneUseCase().map { workout ->
        workout.workoutForWeek.getValue(day.raw).groupBy { it.type }.keys.joinToString(", ")
    }
}
