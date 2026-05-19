package com.bbeniful.domain.usecase

import com.bbeniful.domain.repository.UserProfileRepository
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Provided

@Factory
class GetUserProfileUseCase(
    @Provided private val userProfileRepository: UserProfileRepository
) {
    operator fun invoke() = userProfileRepository.getUserProfile()
}
