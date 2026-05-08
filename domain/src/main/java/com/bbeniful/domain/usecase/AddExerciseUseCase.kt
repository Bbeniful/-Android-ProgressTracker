package com.bbeniful.domain.usecase

import com.bbeniful.domain.model.Exercise
import com.bbeniful.domain.repository.ExerciseRepository
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Provided

@Factory
class AddExerciseUseCase(
    @Provided private val exerciseRepository: ExerciseRepository
) {

    suspend operator fun invoke(exercise: Exercise) = exerciseRepository.add(exercise = exercise)
}