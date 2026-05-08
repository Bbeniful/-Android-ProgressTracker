package com.bbeniful.data.repository

import androidx.datastore.core.DataStore
import com.bbeniful.data.UserProfileProto
import com.bbeniful.domain.model.UserProfile
import com.bbeniful.domain.repository.UserProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Single

@Single(binds = [UserProfileRepository::class])
class UserProfileRepositoryImpl(
    private val dataStore: DataStore<UserProfileProto>
) : UserProfileRepository {

    override fun getUserProfile(): Flow<UserProfile> = dataStore.data.map { proto ->
        UserProfile(
            firstName = proto.firstName,
            lastName = proto.lastName,
            nickname = proto.nickname
        )
    }

    override suspend fun saveUserProfile(userProfile: UserProfile) {
        dataStore.updateData { current ->
            current.toBuilder()
                .setFirstName(userProfile.firstName)
                .setLastName(userProfile.lastName)
                .setNickname(userProfile.nickname)
                .build()
        }
    }
}
