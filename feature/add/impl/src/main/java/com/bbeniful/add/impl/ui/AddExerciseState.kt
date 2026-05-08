package com.bbeniful.add.impl.ui

import androidx.compose.runtime.Immutable
import com.bbeniful.domain.model.Day
import com.bbeniful.domain.model.MuscleGroup

@Immutable
data class AddExerciseState(
    var name: String = "",
    var muscleGroup: MuscleGroup = MuscleGroup.Unknown,
    var day: Day = Day.Unknown,
    var sets: Int = 0,
    var reps: Int = 0,
    var isActive: Boolean = false,
    var editingId: Int = 0,
    var isDeleted: Boolean = false,
    var orderOnDay: Int = 0,
    var showConflictDialog: Boolean = false,
    var conflictExerciseName: String = ""
)

sealed interface AddExerciseIntent {
    data class SetExerciseName(val name: String) : AddExerciseIntent
    data class SetMuscleGroup(val muscleGroup: MuscleGroup) : AddExerciseIntent
    data class SetDay(val day: Day) : AddExerciseIntent
    data class SetSets(val sets: Int) : AddExerciseIntent
    data class SetReps(val reps: Int) : AddExerciseIntent
    data class SetIsActive(val isActive: Boolean) : AddExerciseIntent
    data class SetOrderOnDay(val order: Int) : AddExerciseIntent
    data class LoadExercise(val id: Int) : AddExerciseIntent
    data object Save : AddExerciseIntent
    data object DeleteExercise : AddExerciseIntent
    data object ConfirmOverride : AddExerciseIntent
    data object DismissConflict : AddExerciseIntent
}
