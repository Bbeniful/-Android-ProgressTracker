package com.bbeniful.home.impl.ui

import androidx.lifecycle.ViewModel
import com.bbeniful.domain.model.weeklyExercises
import com.bbeniful.domain.usecase.CreateWeekWorkoutPlaneUseCase
import com.bbeniful.domain.usecase.GetDailyBodyUseCase
import com.bbeniful.domain.usecase.GetProgressForExerciseUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
class HomeViewModel(
/*    private val createWeekWorkoutPlaneUseCase: CreateWeekWorkoutPlaneUseCase,

    private val createWeekWorkoutPlaneUseCase: CreateWeekWorkoutPlaneUseCase,
    private val dailyBodyParts: GetDailyBodyUseCase,
    private val getProgressForExerciseUseCase: GetProgressForExerciseUseCase*/

): ViewModel() {

    val state: StateFlow<HomeState>
        field = MutableStateFlow<HomeState>(HomeState())

    val intent = MutableSharedFlow<HomeIntent>()

    init {
        state.update {
            it.copy(exercises = weeklyExercises)
        }
    }


}