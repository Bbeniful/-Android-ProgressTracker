@file:OptIn(ExperimentalMaterial3Api::class)

package com.bbeniful.home.impl.ui

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bbeniful.design.darkButton
import com.bbeniful.design.halfSheetBg
import com.bbeniful.design.normalTextColor
import com.bbeniful.design.progressIconTint
import com.bbeniful.domain.model.Day
import com.bbeniful.domain.model.Exercise
import com.bbeniful.domain.model.Progress
import com.bbeniful.domain.model.UserProfile
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import com.bbeniful.home.impl.ui.component.DailyExercises
import com.bbeniful.home.impl.ui.component.Days
import com.bbeniful.home.impl.ui.component.ProgressHalfSheet
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = koinViewModel(),
    toSettings: () -> Unit,
    toProgressHistory: (exerciseId: Int, exerciseName: String) -> Unit = { _, _ -> }
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    HomeContent(
        dailyBodyPart = state.dailyBodyPart,
        exercises = state.exercises,
        currentDay = state.currentDay,
        progresses = state.progresses,
        selectedDay = state.userSelectedDay ?: state.currentDay,
        userProfile = state.userProfile,
        onEvent = viewModel::setEvent,
        toSettings = toSettings,
        toProgressHistory = toProgressHistory,
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
    userProfile: UserProfile,
    toSettings: () -> Unit,
    toProgressHistory: (exerciseId: Int, exerciseName: String) -> Unit,
    progresses: List<Progress>,
    progressSheetOpen: (Int) -> Unit,
    onEvent: (HomeIntent) -> Unit
) {
    var exerciseIdSaved by remember { mutableIntStateOf(-1) }
    var showBottomSheet by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    LaunchedEffect(showBottomSheet) {
        if (showBottomSheet) progressSheetOpen(exerciseIdSaved)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        WelcomeHeader(
            userProfile = userProfile,
            onSettingsClick = toSettings
        )

        Spacer(modifier = Modifier.height(55.dp))

        Days(
            currentDay = currentDay,
            restDay = Day.Wednesday,
            selectedDay = selectedDay,
            onDayClick = { day ->
                onEvent(HomeIntent.DaySelected(selectedDay = day))
            },
        )
        Spacer(modifier = Modifier.height(50.dp))

        val today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy. MM. dd"))
        DailyExercises(
            dailyBodyParts = dailyBodyPart,
            exercises = exercises,
            today = today,
            onExerciseClick = { exerciseId ->
                showBottomSheet = true
                exerciseIdSaved = exerciseId
            },
            onToggleDone = { exercise -> onEvent(HomeIntent.ToggleExerciseDone(exercise)) }
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
            val exerciseName = exercises.find { it.id == exerciseIdSaved }?.name ?: ""
            ProgressHalfSheet(
                exerciseName = exerciseName.ifBlank { "Exercise doesn't exist" },
                progresses = progresses,
                onClose = {
                    scope.launch { sheetState.hide() }.invokeOnCompletion {
                        if (!sheetState.isVisible) showBottomSheet = false
                    }
                },
                onSave = { min, max ->
                    onEvent(HomeIntent.SaveProgress(id = exerciseIdSaved, min = min, max = max))
                },
                onDelete = { progress ->
                    onEvent(HomeIntent.DeleteProgress(progress))
                },
                onViewAll = {
                    scope.launch { sheetState.hide() }.invokeOnCompletion {
                        if (!sheetState.isVisible) {
                            showBottomSheet = false
                            toProgressHistory(exerciseIdSaved, exerciseName)
                        }
                    }
                }
            )
        }
    }
}

@Composable
internal fun WelcomeHeader(
    userProfile: UserProfile,
    onSettingsClick: () -> Unit
) {
    val displayName = when {
        userProfile.nickname.isNotBlank() -> userProfile.nickname
        userProfile.firstName.isNotBlank() -> userProfile.firstName
        else -> "there"
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(halfSheetBg, halfSheetBg.copy(alpha = 0.6f))
                ),
                shape = RoundedCornerShape(20.dp)
            )
            .padding(horizontal = 20.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(
                text = "Welcome",
                color = progressIconTint,
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal
            )
            Text(
                text = displayName,
                color = normalTextColor,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(darkButton.copy(alpha = 0.2f))
                .clickable(onClick = onSettingsClick),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = "Settings",
                tint = darkButton,
                modifier = Modifier.size(22.dp)
            )
        }
    }
}
