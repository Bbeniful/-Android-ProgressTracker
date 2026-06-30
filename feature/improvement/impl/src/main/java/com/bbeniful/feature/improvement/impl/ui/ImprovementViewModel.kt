package com.bbeniful.feature.improvement.impl.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bbeniful.domain.model.Exercise
import com.bbeniful.domain.provider.DateProvider
import com.bbeniful.domain.usecase.GetAllExercisesUseCase
import com.bbeniful.domain.usecase.GetProgressForExerciseUseCase
import com.bbeniful.feature.improvement.impl.domain.GenerateImprovementRecommendationsUseCase
import com.bbeniful.feature.improvement.impl.domain.ImprovementContext
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.annotation.KoinViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

private val DATE_ONLY_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy. MM. dd")

@KoinViewModel
class ImprovementViewModel(
    private val getAllExercisesUseCase: GetAllExercisesUseCase,
    private val getProgressForExerciseUseCase: GetProgressForExerciseUseCase,
    private val generateImprovementRecommendationsUseCase: GenerateImprovementRecommendationsUseCase,
    private val dateProvider: DateProvider
) : ViewModel() {

    val state: StateFlow<ImprovementState>
        field = MutableStateFlow(ImprovementState())

    val intent = MutableSharedFlow<ImprovementIntent>()

    private var allExercises: List<Exercise> = emptyList()
    private var progressJob: Job? = null

    init {
        subscribeToIntent()
        loadExercises()
    }

    private fun subscribeToIntent() {
        viewModelScope.launch {
            intent.collect { handleIntent(it) }
        }
    }

    private fun handleIntent(event: ImprovementIntent) {
        when (event) {
            is ImprovementIntent.SelectMuscleGroup -> {
                state.update { it.copy(selectedMuscleGroup = event.muscleGroup) }
                observeRecommendations()
            }
        }
    }

    fun setEvent(event: ImprovementIntent) {
        viewModelScope.launch {
            intent.emit(event)
        }
    }

    private fun loadExercises() {
        viewModelScope.launch {
            getAllExercisesUseCase().collect { exercises ->
                allExercises = exercises
                observeRecommendations()
            }
        }
    }

    private fun observeRecommendations() {
        val selected = state.value.selectedMuscleGroup
        val relevant = allExercises.filter { it.isActive && it.muscleGroup == selected?.raw }
        val today = runCatching { LocalDate.parse(dateProvider.getCurrentDateOnly(), DATE_ONLY_FORMATTER) }
            .getOrDefault(LocalDate.now())

        progressJob?.cancel()

        if (selected == null || relevant.isEmpty()) {
            state.update {
                it.copy(
                    isLoading = false,
                    recommendations = generateImprovementRecommendationsUseCase(
                        ImprovementContext(selected, relevant, emptyMap(), today)
                    )
                )
            }
            return
        }

        state.update { it.copy(isLoading = true) }
        progressJob = viewModelScope.launch {
            val progressFlows = relevant.map { exercise ->
                getProgressForExerciseUseCase(exercise.id).map { exercise.id to it }
            }
            combine(progressFlows) { pairs -> pairs.toMap() }.collect { progressByExerciseId ->
                state.update {
                    it.copy(
                        isLoading = false,
                        recommendations = generateImprovementRecommendationsUseCase(
                            ImprovementContext(selected, relevant, progressByExerciseId, today)
                        )
                    )
                }
            }
        }
    }
}
