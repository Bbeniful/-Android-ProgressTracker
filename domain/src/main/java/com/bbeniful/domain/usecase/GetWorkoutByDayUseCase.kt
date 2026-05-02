package com.bbeniful.domain.usecase

import com.bbeniful.domain.model.Day
import com.bbeniful.domain.repository.ExerciseRepository
import kotlinx.coroutines.flow.map

class GetWorkoutByDayUseCase(
    private val repo: ExerciseRepository
) {

    operator fun invoke(day: Day) = repo.getAll().map { exercises ->
        exercises.filter { exercise ->
            exercise.day == day.raw
        }
    }
}