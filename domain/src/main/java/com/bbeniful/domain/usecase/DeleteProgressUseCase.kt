package com.bbeniful.domain.usecase

import com.bbeniful.domain.model.Progress
import com.bbeniful.domain.repository.ProgressRepository
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Provided

@Factory
class DeleteProgressUseCase(
    @Provided private val progressRepository: ProgressRepository
) {
    suspend operator fun invoke(progress: Progress) = progressRepository.remove(progress)
}
