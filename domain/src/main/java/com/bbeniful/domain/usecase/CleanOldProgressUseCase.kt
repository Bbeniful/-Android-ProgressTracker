package com.bbeniful.domain.usecase

import com.bbeniful.domain.repository.ProgressRepository
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Provided
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Factory
class CleanOldProgressUseCase(
    @Provided private val progressRepository: ProgressRepository
) {
    private val formatter = DateTimeFormatter.ofPattern("yyyy. MM. dd, HH:mm:ss")

    suspend operator fun invoke() {
        val cutoff = LocalDate.now().minusMonths(2).atStartOfDay().format(formatter)
        progressRepository.deleteOlderThan(cutoff)
    }
}
