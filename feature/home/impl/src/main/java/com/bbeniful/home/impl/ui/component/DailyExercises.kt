package com.bbeniful.home.impl.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bbeniful.design.dayBorder
import com.bbeniful.design.exerciseListItem
import com.bbeniful.design.normalTextColor
import com.bbeniful.domain.model.Exercise

@Composable
fun DailyExercises(
    dailyBodyParts: String,
    exercises: List<Exercise>,
    onExerciseClick: (Int) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Text(text = "Today: $dailyBodyParts", color = normalTextColor, fontSize = 16.sp)
        }

        items(exercises) { exercise ->
            ExerciseItem(
                exercise = exercise,
                onExerciseClick = onExerciseClick
            )
        }
    }
}


@Composable
internal fun ExerciseItem(
    exercise: Exercise,
    onExerciseClick: (Int) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .background(color = exerciseListItem, shape = RoundedCornerShape(16.dp))
            .padding(horizontal = 16.dp)
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = {
                        onExerciseClick(exercise.id)
                    }
                )
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(color = dayBorder, shape = RoundedCornerShape(16.dp))
        )
        Spacer(modifier = Modifier.width(10.dp))
        Column {
            Text(
                text = exercise.name,
                color = normalTextColor, fontSize = 16.sp
            )
            Text(
                text = exercise.createSets(),
                color = normalTextColor.copy(alpha = 0.8f),
                fontSize = 16.sp
            )
        }
    }
}


fun Exercise.createSets() = "${this.circle} sets x ${this.rep} reps"