package com.bbeniful.home.impl.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bbeniful.domain.model.Day
import com.bbeniful.domain.model.weeklyExercises
import com.bbeniful.domain.provider.DateProvider
import com.bbeniful.domain.usecase.CreateWeekWorkoutPlaneUseCase
import com.bbeniful.domain.usecase.GetDailyBodyUseCase
import com.bbeniful.domain.usecase.GetProgressForExerciseUseCase
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
    private val dateProvider: DateProvider

) : ViewModel() {

    val state: StateFlow<HomeState>
        field = MutableStateFlow<HomeState>(HomeState())

    val intent = MutableSharedFlow<HomeIntent>()

    init {
        getCurrentDay()
        updateList()
        updateBodyPart()
        subscribeToIntent()

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
        viewModelScope.launch {
            createWeekWorkoutPlaneUseCase().collect { workout ->
                val plan = workout.workoutForWeek
                state.update {
                    it.copy(
                        exercises = plan[getDay().raw] ?: emptyList()
                    )
                }
            }
        }
    }

    private fun getDay(): Day {
        val currentDay = state.value.currentDay
        val userSelectedDay = state.value.userSelectedDay
        return userSelectedDay ?: currentDay
    }

    private fun updateBodyPart() {
        viewModelScope.launch {
            dailyBodyParts(getDay()).collect { bodyParts ->
                state.update {
                    it.copy(
                        dailyBodyPart = bodyParts ?: "Rest day Baby"
                    )
                }
            }
        }
    }
}