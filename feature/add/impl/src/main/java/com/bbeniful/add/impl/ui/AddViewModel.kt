package com.bbeniful.add.impl.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bbeniful.domain.model.Day
import com.bbeniful.domain.model.Exercise
import com.bbeniful.domain.model.MuscleGroup
import com.bbeniful.domain.usecase.AddExerciseUseCase
import com.bbeniful.domain.usecase.DeleteExerciseUseCase
import com.bbeniful.domain.usecase.GetExerciseByDayAndOrderUseCase
import com.bbeniful.domain.usecase.GetExerciseByIdUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
class AddViewModel(
    private val addExerciseUseCase: AddExerciseUseCase,
    private val getExerciseByIdUseCase: GetExerciseByIdUseCase,
    private val deleteExerciseUseCase: DeleteExerciseUseCase,
    private val getExerciseByDayAndOrderUseCase: GetExerciseByDayAndOrderUseCase
) : ViewModel() {

    val state: StateFlow<AddExerciseState>
        field = MutableStateFlow(AddExerciseState())

    val intent = MutableSharedFlow<AddExerciseIntent>()

    init {
        subscribeToIntent()
    }

    private fun subscribeToIntent() {
        viewModelScope.launch {
            intent.collect { handleIntent(event = it) }
        }
    }

    private fun handleIntent(event: AddExerciseIntent) {
        when (event) {
            AddExerciseIntent.Save -> saveExercise()
            is AddExerciseIntent.SetDay -> state.update { it.copy(day = event.day) }
            is AddExerciseIntent.SetExerciseName -> state.update { it.copy(name = event.name) }
            is AddExerciseIntent.SetMuscleGroup -> state.update { it.copy(muscleGroup = event.muscleGroup) }
            is AddExerciseIntent.SetReps -> state.update { it.copy(reps = event.reps) }
            is AddExerciseIntent.SetSets -> state.update { it.copy(sets = event.sets) }
            is AddExerciseIntent.SetIsActive -> state.update { it.copy(isActive = event.isActive) }
            is AddExerciseIntent.SetOrderOnDay -> state.update { it.copy(orderOnDay = event.order) }
            is AddExerciseIntent.LoadExercise -> loadExercise(event.id)
            AddExerciseIntent.DeleteExercise -> deleteExercise()
            AddExerciseIntent.ConfirmOverride -> confirmOverride()
            AddExerciseIntent.DismissConflict -> state.update { it.copy(showConflictDialog = false) }
        }
    }

    fun setEvent(event: AddExerciseIntent) {
        viewModelScope.launch {
            intent.emit(event)
        }
    }

    private fun loadExercise(id: Int) {
        viewModelScope.launch {
            getExerciseByIdUseCase(id).collect { exercise ->
                exercise?.let {
                    state.update { _ ->
                        AddExerciseState(
                            name = exercise.name,
                            muscleGroup = MuscleGroup.fromName(exercise.muscleGroup),
                            day = Day.from(exercise.day),
                            sets = exercise.sets,
                            reps = exercise.rep,
                            isActive = exercise.isActive,
                            editingId = exercise.id,
                            orderOnDay = exercise.orderOnDay
                        )
                    }
                }
            }
        }
    }

    private fun deleteExercise() {
        viewModelScope.launch(Dispatchers.IO) {
            deleteExerciseUseCase(state.value.editingId)
            state.update { it.copy(isDeleted = true) }
        }
    }

    private fun saveExercise() {
        viewModelScope.launch(Dispatchers.IO) {
            val current = state.value
            if (current.orderOnDay > 0) {
                val conflict = getExerciseByDayAndOrderUseCase(current.day.raw, current.orderOnDay)
                if (conflict != null && conflict.id != current.editingId) {
                    state.update { it.copy(showConflictDialog = true, conflictExerciseName = conflict.name) }
                    return@launch
                }
            }
            doSave()
        }
    }

    private fun confirmOverride() {
        state.update { it.copy(showConflictDialog = false) }
        viewModelScope.launch(Dispatchers.IO) {
            val current = state.value
            if (current.orderOnDay > 0) {
                val conflict = getExerciseByDayAndOrderUseCase(current.day.raw, current.orderOnDay)
                conflict?.let { addExerciseUseCase(it.copy(orderOnDay = 0)) }
            }
            doSave()
        }
    }

    private suspend fun doSave() {
        val current = state.value
        val exercise = Exercise(
            id = current.editingId,
            name = current.name,
            sets = current.sets,
            rep = current.reps,
            muscleGroup = current.muscleGroup.raw,
            day = current.day.raw,
            isActive = current.isActive,
            orderOnDay = current.orderOnDay
        )
        addExerciseUseCase(exercise = exercise)
    }
}
