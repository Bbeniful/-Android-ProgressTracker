package com.bbeniful.domain.usecase

import com.bbeniful.domain.model.Exercise
import com.bbeniful.domain.repository.ExerciseRepository
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Provided

@Factory
class GetExerciseByDayAndOrderUseCase(
    @Provided private val exerciseRepository: ExerciseRepository
) {
    suspend operator fun invoke(day: String, order: Int): Exercise? =
        exerciseRepository.getByDayAndOrder(day, order)
}
