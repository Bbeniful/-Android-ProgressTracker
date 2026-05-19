package com.bbeniful.domain.usecase

import com.bbeniful.domain.repository.ProgressRepository
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Provided

@Factory
class GetProgressForExerciseUseCase(
     @Provided private val progressRepository: ProgressRepository
) {

    operator fun invoke(exerciseId: Int) =
        progressRepository.getProgressForExercise(exerciseId = exerciseId)
}