package com.bbeniful.feature.improvement.impl.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bbeniful.design.darkButton
import com.bbeniful.design.dayTextColor
import com.bbeniful.design.exerciseListItem
import com.bbeniful.design.highlightedIconElement
import com.bbeniful.design.highlightedListElement
import com.bbeniful.design.normalTextColor
import com.bbeniful.domain.model.MuscleGroup
import com.bbeniful.feature.improvement.impl.domain.model.Recommendation
import org.koin.androidx.compose.koinViewModel

@Composable
fun ImprovementScreen(
    viewModel: ImprovementViewModel = koinViewModel(),
    onBack: () -> Unit = {}
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ImprovementContent(
        state = state,
        onMuscleGroupSelected = { viewModel.setEvent(ImprovementIntent.SelectMuscleGroup(it)) },
        onBack = onBack
    )
}

@Composable
internal fun ImprovementContent(
    state: ImprovementState,
    onMuscleGroupSelected: (MuscleGroup?) -> Unit,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
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
                text = "Improvements",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = normalTextColor
            )
            Spacer(modifier = Modifier.weight(1f))
        }

        MuscleGroupSelector(
            selectedMuscleGroup = state.selectedMuscleGroup,
            onMuscleGroupSelected = onMuscleGroupSelected
        )

        if (state.isLoading) {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = darkButton)
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(state.recommendations, key = { it.type.name + it.exerciseName.orEmpty() }) { recommendation ->
                    RecommendationCard(recommendation)
                }
            }
        }
    }
}

@Composable
internal fun MuscleGroupSelector(
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
            label = { Text("Generic") },
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
internal fun RecommendationCard(recommendation: Recommendation) {
    val backgroundColor = if (recommendation.isEmphasized) highlightedListElement else exerciseListItem
    val titleColor = if (recommendation.isEmphasized) highlightedIconElement else normalTextColor

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = backgroundColor, shape = RoundedCornerShape(12.dp))
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(text = recommendation.title, color = titleColor, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
        recommendation.exerciseName?.let { exerciseName ->
            Text(text = exerciseName, color = titleColor.copy(alpha = 0.8f), fontSize = 12.sp, fontWeight = FontWeight.Medium)
        }
        Text(text = recommendation.message, color = normalTextColor.copy(alpha = 0.85f), fontSize = 14.sp)
    }
}
