@file:OptIn(ExperimentalMaterial3Api::class)

package com.bbeniful.home.impl.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bbeniful.design.halfSheetBg
import com.bbeniful.domain.model.Day
import com.bbeniful.domain.model.Exercise
import com.bbeniful.home.impl.ui.component.DailyExercises
import com.bbeniful.home.impl.ui.component.Days
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(viewModel: HomeViewModel = koinViewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()


    HomeContent(
        dailyBodyPart = state.dailyBodyPart,
        exercise = state.exercises,
        onDayClick = { _ -> },
        onExerciseClick = { _ -> }
    )
}

@Composable
internal fun HomeContent(
    dailyBodyPart: String,
    exercise: List<Exercise>,
    onDayClick: (Day) -> Unit,
    onExerciseClick: (Int) -> Unit
) {

    var showBottomSheet by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    // 1. Create the state. skipPartiallyExpanded = false allows the "half-way" stop.
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Days(
            currentDay = Day.Wednesday,
            restDay = Day.Wednesday,
            onDayClick = onDayClick
        )
        Spacer(modifier = Modifier.height(50.dp))

        DailyExercises(
            dailyBodyParts = dailyBodyPart,
            exercises = exercise,
            onExerciseClick = { exerciseId ->
                showBottomSheet = true
                onExerciseClick(exerciseId)

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
            // 3. Sheet Content
            SheetContent(onClose = {
                scope.launch { sheetState.hide() }.invokeOnCompletion {
                    if (!sheetState.isVisible) showBottomSheet = false
                }
            })
        }
    }
}

@Composable
fun SheetContent(onClose: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.7f)
            .padding(bottom = 32.dp, start = 16.dp, end = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Half Sheet Dialog", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))
        Text("This sheet stops at 50% height first.")
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = onClose) {
            Text("Close Sheet")
        }
    }
}