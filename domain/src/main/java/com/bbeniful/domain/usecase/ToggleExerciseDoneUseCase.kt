package com.bbeniful.domain.usecase

import com.bbeniful.domain.model.Exercise
import com.bbeniful.domain.provider.DateProvider
import com.bbeniful.domain.repository.ExerciseRepository
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Provided

@Factory
class ToggleExerciseDoneUseCase(
    @Provided private val exerciseRepository: ExerciseRepository,
    @Provided private val dateProvider: DateProvider
) {
    suspend operator fun invoke(exercise: Exercise) {
        val today = dateProvider.getCurrentDateOnly()
        val newDate = if (exercise.completedDate == today) null else today
        exerciseRepository.updateCompletedDate(exercise.id, newDate)
    }
}
