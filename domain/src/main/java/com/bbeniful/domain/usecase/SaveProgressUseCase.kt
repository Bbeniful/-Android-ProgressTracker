package com.bbeniful.domain.usecase

import com.bbeniful.domain.model.Progress
import com.bbeniful.domain.provider.DateProvider
import com.bbeniful.domain.repository.ProgressRepository
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Provided

@Factory
class SaveProgressUseCase(
    @Provided private val progressRepository: ProgressRepository,
    @Provided private val dateProvider: DateProvider
) {

    suspend operator fun invoke(
        exerciseId: Int,
        min: Int,
        max: Int
    ) {
        progressRepository.add(
            Progress(
                exerciseId = exerciseId,
                min = min,
                max = max,
                timestamp = dateProvider.getCurrentDateAsFormattedString()
            )
        )
    }
}