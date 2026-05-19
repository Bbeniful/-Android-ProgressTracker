package com.bbeniful.feature.statistic.impl.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bbeniful.domain.model.Exercise
import com.bbeniful.domain.usecase.CreateWeekWorkoutPlaneUseCase
import com.bbeniful.domain.usecase.GetProgressForExerciseUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
class StatisticViewModel(
    private val createWeekWorkoutPlaneUseCase: CreateWeekWorkoutPlaneUseCase,
    private val getProgressForExerciseUseCase: GetProgressForExerciseUseCase
) : ViewModel() {

    val state: StateFlow<StatisticState>
        field = MutableStateFlow(StatisticState())

    val intent = MutableSharedFlow<StatisticIntent>()

    init {
        loadExercises()
        subscribeToIntent()
    }

    private fun subscribeToIntent() {
        viewModelScope.launch {
            intent.collect { handleIntent(it) }
        }
    }

    private fun handleIntent(event: StatisticIntent) {
        when (event) {
            is StatisticIntent.SelectExercise -> selectExercise(event.exercise)
            StatisticIntent.ClearSelection -> clearSelection()
        }
    }

    fun setEvent(event: StatisticIntent) {
        viewModelScope.launch {
            intent.emit(event)
        }
    }

    private fun loadExercises() {
        viewModelScope.launch {
            state.update { it.copy(isLoading = true) }
            createWeekWorkoutPlaneUseCase().collect { workout ->
                val allExercises = workout.workoutForWeek.values.flatten()
                state.update { it.copy(isLoading = false, exercises = allExercises) }
            }
        }
    }

    private fun selectExercise(exercise: Exercise) {
        state.update { it.copy(selectedExercise = exercise, progresses = emptyList()) }
        viewModelScope.launch {
            getProgressForExerciseUseCase(exercise.id).collect { progresses ->
                state.update {
                    it.copy(
                        progresses = progresses.sortedByDescending { p -> p.timestamp }
                    )
                }
            }
        }
    }

    private fun clearSelection() {
        state.update { it.copy(selectedExercise = null, progresses = emptyList()) }
    }
}
