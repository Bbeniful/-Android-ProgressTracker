package com.bbeniful.feature.settings.impl.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bbeniful.design.darkButton
import com.bbeniful.design.dayTextColor
import com.bbeniful.design.exerciseListItem
import com.bbeniful.design.normalTextColor
import com.bbeniful.domain.model.Exercise
import com.bbeniful.domain.model.MuscleGroup
import org.koin.androidx.compose.koinViewModel

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = koinViewModel(),
    onBack: () -> Unit = {},
    onAddExercise: () -> Unit,
    onEditExercise: (Int) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    when {
        state.showExerciseList -> ExerciseListContent(
            exercises = state.filteredExercises,
            selectedMuscleGroup = state.selectedMuscleGroup,
            onMuscleGroupSelected = { viewModel.setEvent(SettingsIntent.FilterByMuscleGroup(it)) },
            onBack = { viewModel.setEvent(SettingsIntent.HideExerciseList) },
            onExerciseClick = onEditExercise
        )
        state.showUserProfile -> UserProfileScreen(
            userProfile = state.userProfile,
            onEvent = viewModel::setEvent,
            onBack = { viewModel.setEvent(SettingsIntent.HideUserProfile) }
        )
        else -> SettingsContent(
            state = state,
            onEvent = viewModel::setEvent,
            onBack = onBack,
            onAddExercise = onAddExercise
        )
    }
}

@Composable
internal fun SettingsContent(
    state: SettingsState,
    onEvent: (SettingsIntent) -> Unit,
    onBack: () -> Unit,
    onAddExercise: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = normalTextColor
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "Settings",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = normalTextColor
                )
                Spacer(modifier = Modifier.weight(1f))
            }
        }

        item {
            ProfileSection(onEditProfile = { onEvent(SettingsIntent.ShowUserProfile) })
        }

        item {
            HorizontalDivider(color = Color.DarkGray)
        }

        item {
            ExercisesSection(
                onAddExercise = onAddExercise,
                onShowAllExercises = { onEvent(SettingsIntent.ShowExerciseList) }
            )
        }
    }
}

@Composable
internal fun ProfileSection(onEditProfile: () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = "Profile",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = normalTextColor
        )
        SettingsMenuItem(
            title = "Edit Profile",
            subtitle = "Update your name and nickname",
            onClick = onEditProfile
        )
    }
}

@Composable
internal fun ProfileTextField(
    value: String,
    label: String,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = Color(0xFFE1D5E7),
            unfocusedLabelColor = Color.White,
            focusedLabelColor = Color.White,
            unfocusedTextColor = normalTextColor.copy(alpha = 0.8f),
            focusedTextColor = normalTextColor
        )
    )
}

@Composable
internal fun ExercisesSection(
    onAddExercise: () -> Unit,
    onShowAllExercises: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = "Exercises",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = normalTextColor
        )

        SettingsMenuItem(
            title = "Add New Exercise",
            subtitle = "Create a new exercise",
            onClick = onAddExercise
        )

        SettingsMenuItem(
            title = "All Exercises",
            subtitle = "View and edit existing exercises",
            onClick = onShowAllExercises
        )
    }
}

@Composable
internal fun SettingsMenuItem(
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = exerciseListItem, shape = RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(text = title, color = normalTextColor, fontSize = 16.sp, fontWeight = FontWeight.Medium)
            Text(text = subtitle, color = Color.Gray, fontSize = 13.sp)
        }
    }
}

@Composable
internal fun ExerciseListContent(
    exercises: List<Exercise>,
    selectedMuscleGroup: MuscleGroup?,
    onMuscleGroupSelected: (MuscleGroup?) -> Unit,
    onBack: () -> Unit,
    onExerciseClick: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = normalTextColor
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "All Exercises",
                color = normalTextColor,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.weight(1f))
        }

        MuscleGroupFilter(
            selectedMuscleGroup = selectedMuscleGroup,
            onMuscleGroupSelected = onMuscleGroupSelected
        )

        Spacer(modifier = Modifier.height(12.dp))

        if (exercises.isEmpty()) {
            Text(
                text = "No exercises found.",
                color = Color.Gray,
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 8.dp)
            )
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(exercises, key = { it.id }) { exercise ->
                    ExerciseItem(
                        exercise = exercise,
                        onClick = { onExerciseClick(exercise.id) }
                    )
                }
            }
        }
    }
}

@Composable
internal fun MuscleGroupFilter(
    selectedMuscleGroup: MuscleGroup?,
    onMuscleGroupSelected: (MuscleGroup?) -> Unit
) {
    val muscles = MuscleGroup.entries.filter { it != MuscleGroup.Unknown }

    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        FilterChip(
            selected = selectedMuscleGroup == null,
            onClick = { onMuscleGroupSelected(null) },
            label = { Text("All") },
            colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = darkButton,
                selectedLabelColor = dayTextColor
            )
        )
        muscles.forEach { muscle ->
            FilterChip(
                selected = muscle == selectedMuscleGroup,
                onClick = {
                    onMuscleGroupSelected(if (muscle == selectedMuscleGroup) null else muscle)
                },
                label = { Text(muscle.raw) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = darkButton,
                    selectedLabelColor = dayTextColor
                )
            )
        }
    }
}

@Composable
internal fun ExerciseItem(exercise: Exercise, onClick: () -> Unit) {
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
            Text(text = "${exercise.muscleGroup} · ${exercise.day}", color = Color.Gray, fontSize = 13.sp)
        }
        Text(
            text = if (exercise.isActive) "Active" else "Inactive",
            color = if (exercise.isActive) Color(0xFF4CAF50) else Color.Gray,
            fontSize = 12.sp
        )
    }
}
