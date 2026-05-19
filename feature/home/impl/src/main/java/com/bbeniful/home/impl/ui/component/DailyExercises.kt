package com.bbeniful.home.impl.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bbeniful.design.dayBorder
import com.bbeniful.design.exerciseListItem
import com.bbeniful.design.normalTextColor
import com.bbeniful.design.progressIconTint
import com.bbeniful.domain.model.Exercise
import com.bbeniful.feature.home.impl.R

private val doneGreen = Color(0xFF4CAF50)

@Composable
fun DailyExercises(
    dailyBodyParts: String,
    exercises: List<Exercise>,
    doneExerciseIds: Set<Int>,
    onExerciseClick: (Int) -> Unit,
    onToggleDone: (Exercise) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Text(text = "Today: $dailyBodyParts", color = normalTextColor, fontSize = 16.sp)
        }

        items(exercises, key = { it.id }) { exercise ->
            ExerciseItem(
                exercise = exercise,
                isDone = exercise.id in doneExerciseIds,
                onExerciseClick = onExerciseClick,
                onToggleDone = onToggleDone
            )
        }
    }
}

@Composable
internal fun ExerciseItem(
    exercise: Exercise,
    isDone: Boolean,
    onExerciseClick: (Int) -> Unit,
    onToggleDone: (Exercise) -> Unit
) {
    val currentOnExerciseClick by rememberUpdatedState(onExerciseClick)
    val currentOnToggleDone by rememberUpdatedState(onToggleDone)
    val currentExercise by rememberUpdatedState(exercise)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .background(color = exerciseListItem, shape = RoundedCornerShape(16.dp))
            .padding(horizontal = 16.dp)
            .pointerInput(exercise.id) {
                detectTapGestures(
                    onLongPress = { currentOnExerciseClick(exercise.id) }
                )
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(color = dayBorder, shape = RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                modifier = Modifier.size(20.dp),
                painter = painterResource(R.drawable.gym),
                contentDescription = null,
                tint = progressIconTint
            )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = exercise.name, color = normalTextColor, fontSize = 16.sp)
            Text(
                text = exercise.createSets(),
                color = normalTextColor.copy(alpha = 0.8f),
                fontSize = 14.sp
            )
        }

        Box(
            modifier = Modifier
                .size(28.dp)
                .then(
                    if (isDone) Modifier.background(doneGreen, CircleShape)
                    else Modifier.border(2.dp, Color.White, CircleShape)
                )
                .pointerInput(exercise.id) {
                    detectTapGestures(onTap = { currentOnToggleDone(currentExercise) })
                }
        )
    }
}

fun Exercise.createSets() = "${this.sets} sets x ${this.rep} reps"