package com.bbeniful.home.impl.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bbeniful.domain.model.Day
import com.bbeniful.domain.model.weeklyExercises
import com.bbeniful.domain.provider.DateProvider
import com.bbeniful.domain.usecase.CleanOldProgressUseCase
import com.bbeniful.domain.usecase.CreateWeekWorkoutPlaneUseCase
import com.bbeniful.domain.usecase.DeleteProgressUseCase
import com.bbeniful.domain.usecase.GetDailyBodyUseCase
import com.bbeniful.domain.usecase.ToggleExerciseDoneUseCase
import com.bbeniful.domain.usecase.GetProgressForExerciseUseCase
import com.bbeniful.domain.usecase.GetUserProfileUseCase
import com.bbeniful.domain.usecase.SaveProgressUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
class HomeViewModel(
    private val createWeekWorkoutPlaneUseCase: CreateWeekWorkoutPlaneUseCase,
    private val dailyBodyParts: GetDailyBodyUseCase,
    private val getProgressForExerciseUseCase: GetProgressForExerciseUseCase,
    private val saveProgressUseCase: SaveProgressUseCase,
    private val deleteProgressUseCase: DeleteProgressUseCase,
    private val cleanOldProgressUseCase: CleanOldProgressUseCase,
    private val toggleExerciseDoneUseCase: ToggleExerciseDoneUseCase,
    private val dateProvider: DateProvider,
    private val getUserProfileUseCase: GetUserProfileUseCase
) : ViewModel() {

    val state: StateFlow<HomeState>
        field = MutableStateFlow<HomeState>(HomeState())

    private var progressJob: Job? = null
    private var bodyPartJob: Job? = null
    private var listJob: Job? = null

    val intent = MutableSharedFlow<HomeIntent>()

    init {
        getCurrentDay()
        updateList()
        updateBodyPart()
        loadUserProfile()
        subscribeToIntent()
        viewModelScope.launch(Dispatchers.IO) { cleanOldProgressUseCase() }
    }

    private fun subscribeToIntent() {
        viewModelScope.launch {
            intent.collect {
                handleEvent(event = it)
            }
        }
    }

    private fun handleEvent(event: HomeIntent) {
        when (event) {
            is HomeIntent.DaySelected -> {
                state.update { it.copy(userSelectedDay = event.selectedDay) }
                updateBodyPart()
                getDay()
                updateList()
            }

            HomeIntent.ResetSelectedDay -> {}
            HomeIntent.SelectCurrentDay -> {

            }

            is HomeIntent.SaveProgress -> {
                saveProgress(
                    exerciseId = event.id,
                    min = event.min,
                    max = event.max
                )
            }

            is HomeIntent.DeleteProgress -> {
                viewModelScope.launch(Dispatchers.IO) {
                    deleteProgressUseCase(event.progress)
                }
            }

            is HomeIntent.ToggleExerciseDone -> {
                val id = event.exercise.id
                state.update {
                    val updated = if (id in it.doneExerciseIds) it.doneExerciseIds - id
                                  else it.doneExerciseIds + id
                    it.copy(doneExerciseIds = updated)
                }
            }
        }
    }

    fun setEvent(event: HomeIntent) {
        viewModelScope.launch {
            intent.emit(event)
        }
    }

    private fun getCurrentDay() {
        val currentDay = dateProvider.getCurrentDay()
        state.update {
            it.copy(currentDay = currentDay)
        }
    }

    private fun updateList() {
        listJob?.cancel()
        listJob = viewModelScope.launch {
            createWeekWorkoutPlaneUseCase().collect { workout ->
                val plan = workout.workoutForWeek
                val sorted = (plan[getDay().raw] ?: emptyList())
                    .sortedWith(compareBy { if (it.orderOnDay == 0) Int.MAX_VALUE else it.orderOnDay })
                state.update { it.copy(exercises = sorted) }
            }
        }
    }

    private fun getDay(): Day {
        val currentDay = state.value.currentDay
        val userSelectedDay = state.value.userSelectedDay
        return userSelectedDay ?: currentDay
    }

    private fun updateBodyPart() {
        bodyPartJob?.cancel()
        bodyPartJob = viewModelScope.launch {
            dailyBodyParts(getDay()).collect { bodyParts ->
                state.update {
                    it.copy(
                        dailyBodyPart = bodyParts ?: "Rest day Baby"
                    )
                }
            }
        }
    }


    fun saveProgress(
        exerciseId: Int,
        min: Int,
        max: Int
    ) {
        viewModelScope.launch(
            Dispatchers.IO
        ) {
            saveProgressUseCase(
                exerciseId = exerciseId,
                min = min, max = max
            )
        }
    }

    private fun loadUserProfile() {
        viewModelScope.launch {
            getUserProfileUseCase().collect { profile ->
                state.update { it.copy(userProfile = profile) }
            }
        }
    }

    fun updateProgresses(exerciseId: Int) {
        progressJob?.cancel()
        state.update { it.copy(progresses = emptyList()) }
        progressJob = viewModelScope.launch {
            getProgressForExerciseUseCase(
                exerciseId = exerciseId
            ).collect { progresses ->
                state.update {
                    it.copy(
                        progresses = progresses
                            .sortedByDescending { progress -> progress.timestamp }
                            .take(3)
                    )
                }
            }
        }
    }
}