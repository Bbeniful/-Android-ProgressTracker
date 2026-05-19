package com.bbeniful.domain.usecase

import com.bbeniful.domain.model.UserProfile
import com.bbeniful.domain.repository.UserProfileRepository
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Provided

@Factory
class SaveUserProfileUseCase(
    @Provided private val userProfileRepository: UserProfileRepository
) {
    suspend operator fun invoke(userProfile: UserProfile) =
        userProfileRepository.saveUserProfile(userProfile)
}
