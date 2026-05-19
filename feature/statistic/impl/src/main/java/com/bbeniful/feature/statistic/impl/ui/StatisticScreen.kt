package com.bbeniful.feature.statistic.impl.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bbeniful.design.darkButton
import com.bbeniful.design.exerciseListItem
import com.bbeniful.design.normalTextColor
import com.bbeniful.domain.model.Exercise
import com.bbeniful.domain.model.Progress
import org.koin.androidx.compose.koinViewModel

@Composable
fun StatisticScreen(viewModel: StatisticViewModel = koinViewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    StatisticContent(
        state = state,
        onEvent = viewModel::setEvent
    )
}

@Composable
internal fun StatisticContent(
    state: StatisticState,
    onEvent: (StatisticIntent) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Statistics",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = normalTextColor,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        if (state.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
            return
        }

        if (state.error != null) {
            Text(text = state.error, color = Color.Red, fontSize = 14.sp)
            return
        }

        if (state.selectedExercise != null) {
            ExerciseProgressDetail(
                exercise = state.selectedExercise,
                progresses = state.progresses,
                onBack = { onEvent(StatisticIntent.ClearSelection) }
            )
        } else {
            ExerciseList(
                exercises = state.exercises,
                onExerciseClick = { onEvent(StatisticIntent.SelectExercise(it)) }
            )
        }
    }
}

@Composable
internal fun ExerciseList(
    exercises: List<Exercise>,
    onExerciseClick: (Exercise) -> Unit
) {
    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        items(exercises, key = { it.id }) { exercise ->
            ExerciseListItem(exercise = exercise, onClick = { onExerciseClick(exercise) })
        }
    }
}

@Composable
internal fun ExerciseListItem(exercise: Exercise, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = exerciseListItem, shape = RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = exercise.name, color = normalTextColor, fontSize = 16.sp, fontWeight = FontWeight.Medium)
            Text(text = exercise.muscleGroup, color = Color.Gray, fontSize = 13.sp)
        }
        Text(
            text = "${exercise.sets}x${exercise.rep}",
            color = normalTextColor,
            fontSize = 14.sp
        )
    }
}

@Composable
internal fun ExerciseProgressDetail(
    exercise: Exercise,
    progresses: List<Progress>,
    onBack: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            text = "< Back",
            color = normalTextColor,
            fontSize = 14.sp,
            modifier = Modifier.clickable(onClick = onBack)
        )

        Text(text = exercise.name, color = normalTextColor, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Text(text = exercise.muscleGroup, color = Color.Gray, fontSize = 14.sp)

        Spacer(modifier = Modifier.height(4.dp))

        Text(text = "Progress History", color = normalTextColor, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)

        if (progresses.isEmpty()) {
            Text(text = "No progress recorded yet.", color = Color.Gray, fontSize = 14.sp)
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(progresses, key = { it.id }) { progress ->
                    ProgressItem(progress = progress)
                }
            }
        }
    }
}

@Composable
internal fun ProgressItem(progress: Progress) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = exerciseListItem, shape = RoundedCornerShape(12.dp))
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = progress.timestamp, color = Color.Gray, fontSize = 13.sp)
        Text(
            text = "Min: ${progress.min}  Max: ${progress.max}",
            color = normalTextColor,
            fontSize = 14.sp
        )
    }
}
