package com.bbeniful.domain.usecase

import com.bbeniful.domain.repository.ExerciseRepository
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Provided

@Factory
class DeleteExerciseUseCase(
    @Provided private val exerciseRepository: ExerciseRepository
) {
    suspend operator fun invoke(id: Int) = exerciseRepository.deleteById(id)
}
