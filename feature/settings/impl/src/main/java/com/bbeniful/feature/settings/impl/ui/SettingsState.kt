package com.bbeniful.feature.settings.impl.ui

import androidx.compose.runtime.Immutable
import com.bbeniful.domain.model.Exercise
import com.bbeniful.domain.model.MuscleGroup
import com.bbeniful.domain.model.UserProfile

@Immutable
data class SettingsState(
    val isLoading: Boolean = false,
    val userProfile: UserProfile = UserProfile(),
    val exercises: List<Exercise> = emptyList(),
    val showExerciseList: Boolean = false,
    val showUserProfile: Boolean = false,
    val selectedMuscleGroup: MuscleGroup? = null,
    val isProfileSaved: Boolean = false,
    val error: String? = null
) {
    val filteredExercises: List<Exercise>
        get() = if (selectedMuscleGroup == null) exercises
                else exercises.filter { it.muscleGroup == selectedMuscleGroup.raw }
}

sealed interface SettingsIntent {
    data class UpdateFirstName(val firstName: String) : SettingsIntent
    data class UpdateLastName(val lastName: String) : SettingsIntent
    data class UpdateNickname(val nickname: String) : SettingsIntent
    data object SaveUserProfile : SettingsIntent
    data object ProfileSavedHandled : SettingsIntent
    data object ShowExerciseList : SettingsIntent
    data object HideExerciseList : SettingsIntent
    data object ShowUserProfile : SettingsIntent
    data object HideUserProfile : SettingsIntent
    data class FilterByMuscleGroup(val muscleGroup: MuscleGroup?) : SettingsIntent
}
