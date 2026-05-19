package com.bbeniful.domain.repository

import com.bbeniful.domain.model.UserProfile
import kotlinx.coroutines.flow.Flow

interface UserProfileRepository {

    fun getUserProfile(): Flow<UserProfile>

    suspend fun saveUserProfile(userProfile: UserProfile)
}
