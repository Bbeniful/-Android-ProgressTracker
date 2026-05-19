package com.bbeniful.add.impl.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bbeniful.design.darkButton
import com.bbeniful.design.darkTextColor
import com.bbeniful.design.dayBorder
import com.bbeniful.design.dayTextColor
import com.bbeniful.design.exerciseListItem
import com.bbeniful.design.lightButton
import com.bbeniful.design.normalTextColor
import com.bbeniful.design.pastDay
import com.bbeniful.domain.model.Day
import com.bbeniful.domain.model.MuscleGroup
import org.koin.androidx.compose.koinViewModel

@Composable
fun AddScreen(
    exerciseId: Int? = null,
    onBack: () -> Unit = {},
    onDeleted: () -> Unit = {},
    viewModel: AddViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(exerciseId) {
        exerciseId?.let { viewModel.setEvent(AddExerciseIntent.LoadExercise(it)) }
    }

    LaunchedEffect(state.isDeleted) {
        if (state.isDeleted) onDeleted()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(15.dp, alignment = Alignment.Top)
    ) {
        IconButton(onClick = onBack) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = normalTextColor
            )
        }
        AddComponent(
            state = state,
            onEvent = viewModel::setEvent
        )
    }
}

@Composable
internal fun AddComponent(state: AddExerciseState, onEvent: (AddExerciseIntent) -> Unit) {

    if (state.showConflictDialog) {
        AlertDialog(
            onDismissRequest = { onEvent(AddExerciseIntent.DismissConflict) },
            title = { Text("Position taken") },
            text = {
                Text(
                    "\"${state.conflictExerciseName}\" is already #${state.orderOnDay} on this day. " +
                    "Override? It will lose its position."
                )
            },
            confirmButton = {
                TextButton(onClick = { onEvent(AddExerciseIntent.ConfirmOverride) }) {
                    Text("Override", color = Color(0xFFD32F2F))
                }
            },
            dismissButton = {
                TextButton(onClick = { onEvent(AddExerciseIntent.DismissConflict) }) {
                    Text("Cancel")
                }
            }
        )
    }

    OutlinedTextField(
        value = state.name,
        onValueChange = { onEvent(AddExerciseIntent.SetExerciseName(name = it)) },
        label = { Text("Exercise Name") },
        placeholder = { Text("") },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        maxLines = 2,
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = Color(0xFFE1D5E7),
            unfocusedLabelColor = Color.White,
            unfocusedPlaceholderColor = Color.Gray,
            unfocusedTextColor = normalTextColor.copy(alpha = 0.8f),
            focusedTextColor = normalTextColor
        )
    )

    MuscleGroup(
        state = state,
        muscle = MuscleGroup.Triceps,
        onItemSelected = { onEvent(AddExerciseIntent.SetMuscleGroup(muscleGroup = it)) }
    )

    SelectDay(selectedDay = state.day) { onEvent(AddExerciseIntent.SetDay(day = it)) }

    SetRepsAndSets(state = state, onEvent = onEvent)

    IsActiveCheckbox(
        isActive = state.isActive,
        onCheckedChange = { onEvent(AddExerciseIntent.SetIsActive(it)) }
    )

    SetNumbers(
        title = "Position on day (0 = none)",
        number = state.orderOnDay
    ) { onEvent(AddExerciseIntent.SetOrderOnDay(it)) }

    SaveExercise { onEvent(AddExerciseIntent.Save) }

    if (state.editingId != 0) {
        DeleteExercise { onEvent(AddExerciseIntent.DeleteExercise) }
    }
}

@Composable
internal fun IsActiveCheckbox(
    isActive: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Checkbox(
            checked = isActive,
            onCheckedChange = onCheckedChange,
            colors = CheckboxDefaults.colors(
                checkedColor = darkButton,
                checkmarkColor = dayTextColor
            )
        )
        Text(
            text = "Active exercise",
            color = normalTextColor,
            fontSize = 16.sp
        )
    }
}

@Composable
internal fun MuscleGroup(
    state: AddExerciseState,
    muscle: MuscleGroup? = null,
    onItemSelected: (MuscleGroup) -> Unit
) {
    val muscles = MuscleGroup.entries.filter { it != MuscleGroup.Unknown }
    var preSelectedMuscle by remember { mutableStateOf(muscle) }

    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        muscles.forEach { item ->
            val isSelected = if (preSelectedMuscle != null) {
                item == preSelectedMuscle
            } else {
                item == state.muscleGroup
            }

            FilterChip(
                selected = isSelected,
                onClick = {
                    preSelectedMuscle = null
                    onItemSelected(muscles.find { item.raw == it.raw } ?: MuscleGroup.Unknown)
                },
                label = { Text(item.raw) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = darkButton,
                    selectedLabelColor = dayTextColor
                )
            )
        }
    }
}

@Composable
fun SelectDay(
    selectedDay: Day? = null,
    onDayClick: (Day) -> Unit
) {
    val workoutDays = Day.entries.filter {
        it != Day.Saturday && it != Day.Sunday && it != Day.Unknown
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        workoutDays.forEach { day ->
            DayItem(
                isSelectedDay = day == selectedDay,
                day = day,
                onDayClick = { onDayClick(day) }
            )
        }
    }
}

@Composable
private fun DayItem(
    isSelectedDay: Boolean,
    day: Day,
    onDayClick: (Day) -> Unit
) {
    val backgroundColor = when {
        isSelectedDay -> darkButton
        else -> Color.Transparent
    }

    val textColor = if (isSelectedDay) dayTextColor else Color.White

    Column(
        modifier = Modifier
            .size(width = 64.dp, height = 80.dp)
            .background(color = backgroundColor, shape = RoundedCornerShape(16.dp))
            .border(width = 1.dp, color = dayBorder, shape = RoundedCornerShape(16.dp))
            .clickable { onDayClick(day) },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = day.raw.take(3), fontSize = 16.sp, color = textColor)
    }
}

@Composable
internal fun SetRepsAndSets(
    state: AddExerciseState,
    onEvent: (AddExerciseIntent) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        SetNumbers(
            modifier = Modifier.weight(0.5f),
            title = "Number of Sets",
            number = state.sets
        ) { onEvent(AddExerciseIntent.SetSets(sets = it)) }
        Spacer(modifier = Modifier.width(20.dp))
        SetNumbers(
            modifier = Modifier.weight(0.5f),
            title = "Reps per Set",
            number = state.reps
        ) { onEvent(AddExerciseIntent.SetReps(reps = it)) }
    }
}

@Composable
internal fun SetNumbers(
    modifier: Modifier = Modifier,
    title: String,
    number: Int,
    onNumberChanged: (Int) -> Unit
) {
    var localNumber by remember { mutableIntStateOf(number) }

    Column(
        modifier = modifier
            .background(color = exerciseListItem, shape = RoundedCornerShape(16.dp))
            .padding(all = 25.dp),
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        Text(text = title, color = normalTextColor, fontSize = 16.sp)
        Box(
            modifier = Modifier
                .size(width = 150.dp, height = 50.dp)
                .border(width = 1.dp, color = Color.White)
                .padding(start = 12.dp),
        ) {
            Text(
                modifier = Modifier.align(Alignment.CenterStart),
                text = "$number",
                color = normalTextColor,
                fontSize = 18.sp
            )
        }
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(color = pastDay, shape = CircleShape)
                    .border(width = 1.dp, color = Color.White, shape = CircleShape)
                    .clickable {
                        if (localNumber > 0) {
                            localNumber--
                            onNumberChanged(localNumber)
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(text = "-", color = normalTextColor, fontSize = 18.sp)
            }
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(color = pastDay, shape = CircleShape)
                    .border(width = 1.dp, color = Color.White, shape = CircleShape)
                    .clickable {
                        if (localNumber < 100) {
                            localNumber++
                            onNumberChanged(localNumber)
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(text = "+", color = normalTextColor, fontSize = 18.sp)
            }
        }
    }
}

@Composable
internal fun SaveExercise(onClick: () -> Unit) {
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .padding(horizontal = 20.dp),
        colors = ButtonDefaults.buttonColors(containerColor = lightButton),
        onClick = onClick
    ) {
        Text(text = "Save", fontSize = 16.sp, color = darkTextColor)
    }
}

@Composable
internal fun DeleteExercise(onClick: () -> Unit) {
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .padding(horizontal = 20.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F)),
        onClick = onClick
    ) {
        Text(text = "Delete", fontSize = 16.sp, color = Color.White)
    }
}
