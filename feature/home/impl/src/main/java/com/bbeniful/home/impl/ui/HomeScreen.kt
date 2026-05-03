@file:OptIn(ExperimentalMaterial3Api::class)

package com.bbeniful.home.impl.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bbeniful.design.halfSheetBg
import com.bbeniful.domain.model.Day
import com.bbeniful.domain.model.Exercise
import com.bbeniful.domain.model.Progress
import com.bbeniful.home.impl.ui.component.DailyExercises
import com.bbeniful.home.impl.ui.component.Days
import com.bbeniful.home.impl.ui.component.ProgressHalfSheet
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(viewModel: HomeViewModel = koinViewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()


    HomeContent(
        dailyBodyPart = state.dailyBodyPart,
        exercises = state.exercises,
        currentDay = state.currentDay,
        progresses = state.progresses,
        selectedDay = state.userSelectedDay ?: state.currentDay,
        onEvent = viewModel::setEvent,
        progressSheetOpen = { id ->
            viewModel.updateProgresses(exerciseId = id)
        }
    )
}

@Composable
internal fun HomeContent(
    dailyBodyPart: String,
    exercises: List<Exercise>,
    currentDay: Day,
    selectedDay: Day,
    progresses: List<Progress>,
    progressSheetOpen: (Int) -> Unit,
    onEvent: (HomeIntent) -> Unit
) {

    // Tmp exercise nam,e
    var exerciseIdSaved by remember { mutableIntStateOf(-1) }

    var showBottomSheet by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    LaunchedEffect(showBottomSheet) {
        if (showBottomSheet) {
            progressSheetOpen(exerciseIdSaved)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Days(
            currentDay = currentDay,
            restDay = Day.Wednesday,
            selectedDay = selectedDay,
            onDayClick = { day ->
                onEvent(HomeIntent.DaySelected(selectedDay = day))
            },
        )
        Spacer(modifier = Modifier.height(50.dp))

        DailyExercises(
            dailyBodyParts = dailyBodyPart,
            exercises = exercises,
            onExerciseClick = { exerciseId ->
                showBottomSheet = true
                //onExerciseClick(exerciseId)
                exerciseIdSaved = exerciseId

            }
        )
    }
    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false },
            sheetState = sheetState,
            dragHandle = { BottomSheetDefaults.DragHandle() },
            containerColor = halfSheetBg,
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
        ) {
            ProgressHalfSheet(exerciseName = exercises.find { it.id == exerciseIdSaved }?.name ?: "Exercise doesn't exist", progresses = progresses, onClose = {
                scope.launch { sheetState.hide() }.invokeOnCompletion {
                    if (!sheetState.isVisible) showBottomSheet = false
                }
            }, onSave = { min, max ->
                onEvent(
                    HomeIntent.SaveProgress(
                        id = exerciseIdSaved,
                        min = min,
                        max = max
                    )
                )
            })
        }
    }
}