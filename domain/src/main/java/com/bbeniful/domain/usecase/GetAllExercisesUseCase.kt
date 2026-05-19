package com.bbeniful.domain.usecase

import com.bbeniful.domain.repository.ExerciseRepository
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Provided

@Factory
class GetAllExercisesUseCase(
    @Provided private val exerciseRepository: ExerciseRepository
) {
    operator fun invoke() = exerciseRepository.getAll()
}
