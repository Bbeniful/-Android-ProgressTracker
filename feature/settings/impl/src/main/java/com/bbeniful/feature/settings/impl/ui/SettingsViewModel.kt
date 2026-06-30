package com.bbeniful.feature.settings.impl.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bbeniful.domain.model.UserProfile
import com.bbeniful.domain.usecase.GetAllExercisesUseCase
import com.bbeniful.domain.usecase.GetUserProfileUseCase
import com.bbeniful.domain.usecase.SaveUserProfileUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
class SettingsViewModel(
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val saveUserProfileUseCase: SaveUserProfileUseCase,
    private val getAllExercisesUseCase: GetAllExercisesUseCase
) : ViewModel() {

    val state: StateFlow<SettingsState>
        field = MutableStateFlow(SettingsState())

    val intent = MutableSharedFlow<SettingsIntent>()

    init {
        loadUserProfile()
        loadExercises()
        subscribeToIntent()
    }

    private fun subscribeToIntent() {
        viewModelScope.launch {
            intent.collect { handleIntent(it) }
        }
    }

    private fun handleIntent(event: SettingsIntent) {
        when (event) {
            is SettingsIntent.UpdateFirstName -> state.update { it.copy(userProfile = it.userProfile.copy(firstName = event.firstName)) }
            is SettingsIntent.UpdateLastName -> state.update { it.copy(userProfile = it.userProfile.copy(lastName = event.lastName)) }
            is SettingsIntent.UpdateNickname -> state.update { it.copy(userProfile = it.userProfile.copy(nickname = event.nickname)) }
            SettingsIntent.SaveUserProfile -> saveUserProfile()
            SettingsIntent.ProfileSavedHandled -> state.update { it.copy(isProfileSaved = false) }
            SettingsIntent.ShowExerciseList -> state.update { it.copy(showExerciseList = true) }
            SettingsIntent.HideExerciseList -> state.update { it.copy(showExerciseList = false, selectedMuscleGroup = null) }
            SettingsIntent.ShowUserProfile -> state.update { it.copy(showUserProfile = true) }
            SettingsIntent.HideUserProfile -> state.update { it.copy(showUserProfile = false) }
            is SettingsIntent.FilterByMuscleGroup -> state.update { it.copy(selectedMuscleGroup = event.muscleGroup) }
        }
    }

    fun setEvent(event: SettingsIntent) {
        viewModelScope.launch {
            intent.emit(event)
        }
    }

    private fun loadUserProfile() {
        viewModelScope.launch {
            getUserProfileUseCase().collect { profile ->
                state.update { it.copy(userProfile = profile) }
            }
        }
    }

    private fun loadExercises() {
        viewModelScope.launch {
            getAllExercisesUseCase().collect { exercises ->
                state.update { it.copy(exercises = exercises) }
            }
        }
    }

    private fun saveUserProfile() {
        viewModelScope.launch(Dispatchers.IO) {
            saveUserProfileUseCase(state.value.userProfile)
            state.update { it.copy(isProfileSaved = true) }
        }
    }
}
