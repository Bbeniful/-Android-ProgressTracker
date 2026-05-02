package com.bbeniful.domain.usecase

import com.bbeniful.domain.repository.ProgressRepository
import org.koin.core.annotation.Factory

//@Factory
class GetProgressForExerciseUseCase(
    private val progressRepository: ProgressRepository
) {

    operator fun invoke(exerciseId: Int) =
        progressRepository.getProgressForExercise(exerciseId = exerciseId)
}