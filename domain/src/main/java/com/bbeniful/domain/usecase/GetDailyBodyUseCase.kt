package com.bbeniful.domain.usecase

import com.bbeniful.domain.model.Day
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Provided

@Factory
class GetDailyBodyUseCase(
     @Provided private val createWeekWorkoutPlaneUseCase: CreateWeekWorkoutPlaneUseCase
) {

    operator fun invoke(day: Day) = createWeekWorkoutPlaneUseCase().map { workout ->
        workout.workoutForWeek[day.raw]?.groupBy { it.type }?.keys?.joinToString(", ")
    }
}
