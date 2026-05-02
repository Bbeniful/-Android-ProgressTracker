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
import androidx.compose.runtime.getValue
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
        selectedDay = state.userSelectedDay ?: state.currentDay,
        onEvent = viewModel::setEvent
    )
}

@Composable
internal fun HomeContent(
    dailyBodyPart: String,
    exercises: List<Exercise>,
    currentDay: Day,
    selectedDay: Day,
    onEvent: (HomeIntent) -> Unit
) {

    // Tmp exercise nam,e
    var exerciseName by remember { mutableStateOf("") }

    var showBottomSheet by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

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
                exerciseName = exercises.find { it.id == exerciseId }?.name ?: "Not found"

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
            ProgressHalfSheet(exerciseName) {
                scope.launch { sheetState.hide() }.invokeOnCompletion {
                    if (!sheetState.isVisible) showBottomSheet = false
                }
            }
        }
    }
}