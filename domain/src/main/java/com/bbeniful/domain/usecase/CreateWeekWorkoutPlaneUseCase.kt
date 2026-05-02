package com.bbeniful.domain.usecase

import com.bbeniful.domain.model.Workout
import com.bbeniful.domain.repository.ExerciseRepository
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Factory

//@Factory
class CreateWeekWorkoutPlaneUseCase(
    private val exerciseRepository: ExerciseRepository
) {

    operator fun invoke() = groupExercisesByDay().map { Workout(workoutForWeek = it) }

    private fun groupExercisesByDay() = exerciseRepository.getAll().map { exercises ->
        exercises.groupBy { exercise -> exercise.day }
    }
}