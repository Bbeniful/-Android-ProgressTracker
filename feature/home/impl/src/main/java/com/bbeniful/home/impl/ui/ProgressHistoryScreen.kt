package com.bbeniful.home.impl.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import com.bbeniful.design.addNewLogSmallBg
import com.bbeniful.design.darkButton
import com.bbeniful.design.darkTextColor
import com.bbeniful.design.highlightedListElement
import com.bbeniful.design.lightButton
import com.bbeniful.design.normalTextColor
import com.bbeniful.design.progressIconTint
import com.bbeniful.domain.model.Progress
import com.bbeniful.domain.usecase.GetProgressForExerciseUseCase
import com.bbeniful.home.impl.ui.component.createMinMax
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.core.annotation.KoinViewModel

// ── State & Intent ────────────────────────────────────────────────────────────

data class ProgressHistoryState(
    val isLoading: Boolean = true,
    val progresses: List<Progress> = emptyList(),
    val showChart: Boolean = false
)

// ── ViewModel ─────────────────────────────────────────────────────────────────

@KoinViewModel
class ProgressHistoryViewModel(
    private val getProgressForExerciseUseCase: GetProgressForExerciseUseCase
) : ViewModel() {

    val state: StateFlow<ProgressHistoryState>
        field = MutableStateFlow(ProgressHistoryState())

    fun load(exerciseId: Int) {
        viewModelScope.launch {
            getProgressForExerciseUseCase(exerciseId).collect { list ->
                state.update {
                    it.copy(
                        isLoading = false,
                        progresses = list.sortedByDescending { p -> p.timestamp }
                    )
                }
            }
        }
    }

    fun toggleChart() {
        state.update { it.copy(showChart = !it.showChart) }
    }
}

// ── Screen ────────────────────────────────────────────────────────────────────

@Composable
fun ProgressHistoryScreen(
    exerciseId: Int,
    exerciseName: String,
    onBack: () -> Unit,
    viewModel: ProgressHistoryViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(exerciseId) { viewModel.load(exerciseId) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
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
                text = exerciseName,
                color = normalTextColor,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (state.progresses.isNotEmpty()) {
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = if (state.showChart) darkButton else lightButton),
                onClick = viewModel::toggleChart
            ) {
                Text(
                    text = if (state.showChart) "Hide Chart" else "Show Progress Chart",
                    color = if (state.showChart) normalTextColor else darkTextColor,
                    fontSize = 15.sp
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            AnimatedVisibility(
                visible = state.showChart,
                enter = expandVertically(),
                exit = shrinkVertically()
            ) {
                ProgressChart(
                    progresses = state.progresses.asReversed(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                        .background(addNewLogSmallBg, shape = RoundedCornerShape(16.dp))
                        .padding(16.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))
        }

        if (state.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Loading…", color = normalTextColor)
            }
        } else if (state.progresses.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No logs yet.", color = Color.Gray)
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                itemsIndexed(state.progresses, key = { _, p -> p.id }) { index, progress ->
                    val prev = state.progresses.getOrNull(index + 1)
                    ProgressHistoryItem(
                        progress = progress,
                        minDiff = prev?.let { progress.min - it.min },
                        maxDiff = prev?.let { progress.max - it.max }
                    )
                }
            }
        }
    }
}

// ── List item ─────────────────────────────────────────────────────────────────

@Composable
private fun ProgressHistoryItem(
    progress: Progress,
    minDiff: Int? = null,
    maxDiff: Int? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(addNewLogSmallBg, shape = RoundedCornerShape(12.dp))
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = progress.timestamp, color = progressIconTint, fontSize = 12.sp)
        Text(
            text = progress.createMinMax(minDiff, maxDiff),
            color = normalTextColor,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

// ── Smooth canvas chart ───────────────────────────────────────────────────────

@Composable
private fun ProgressChart(
    progresses: List<Progress>,
    modifier: Modifier = Modifier
) {
    if (progresses.size < 2) {
        Box(modifier = modifier, contentAlignment = Alignment.Center) {
            Text("Need at least 2 logs for a chart.", color = Color.Gray, fontSize = 13.sp)
        }
        return
    }

    val maxLine = Color(0xFF7C5CBF)
    val minLine = Color(0xFF4FC3F7)
    val gridColor = Color.White.copy(alpha = 0.08f)
    val labelColor = android.graphics.Color.argb(180, 255, 255, 255)

    val maxValues = progresses.map { it.max.toFloat() }
    val minValues = progresses.map { it.min.toFloat() }
    val allValues = maxValues + minValues
    val globalMin = allValues.min()
    val globalMax = allValues.max()
    val valueRange = (globalMax - globalMin).coerceAtLeast(1f)

    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val topPad = 16f
        val bottomPad = 32f
        val leftPad = 8f
        val chartH = h - topPad - bottomPad
        val chartW = w - leftPad

        fun xFor(index: Int) = leftPad + index * (chartW / (progresses.size - 1))
        fun yFor(value: Float) = topPad + chartH * (1f - (value - globalMin) / valueRange)

        // grid lines
        val gridSteps = 4
        repeat(gridSteps + 1) { i ->
            val y = topPad + chartH * i / gridSteps
            drawLine(gridColor, Offset(leftPad, y), Offset(w, y), strokeWidth = 1f)
        }

        // x-axis date labels (show first, middle, last)
        val labelIndices = listOf(0, progresses.size / 2, progresses.size - 1).distinct()
        labelIndices.forEach { idx ->
            val raw = progresses[idx].timestamp
            // extract "MM. dd" from "yyyy. MM. dd, HH:mm:ss"
            val label = runCatching { raw.substring(6, 12) }.getOrDefault(raw.take(6))
            drawContext.canvas.nativeCanvas.drawText(
                label,
                xFor(idx),
                h,
                android.graphics.Paint().apply {
                    color = labelColor
                    textSize = 28f
                    textAlign = android.graphics.Paint.Align.CENTER
                    isAntiAlias = true
                }
            )
        }

        fun smoothPath(values: List<Float>): Path {
            val path = Path()
            val pts = values.indices.map { i -> Offset(xFor(i), yFor(values[i])) }
            path.moveTo(pts[0].x, pts[0].y)
            for (i in 0 until pts.size - 1) {
                val p0 = if (i == 0) pts[0] else pts[i - 1]
                val p1 = pts[i]
                val p2 = pts[i + 1]
                val p3 = if (i + 2 < pts.size) pts[i + 2] else pts[i + 1]
                // Catmull-Rom → cubic Bezier
                val cp1x = p1.x + (p2.x - p0.x) / 6f
                val cp1y = p1.y + (p2.y - p0.y) / 6f
                val cp2x = p2.x - (p3.x - p1.x) / 6f
                val cp2y = p2.y - (p3.y - p1.y) / 6f
                path.cubicTo(cp1x, cp1y, cp2x, cp2y, p2.x, p2.y)
            }
            return path
        }

        // min line
        drawPath(
            path = smoothPath(minValues),
            color = minLine,
            style = Stroke(width = 3f, cap = StrokeCap.Round)
        )
        // max line
        drawPath(
            path = smoothPath(maxValues),
            color = maxLine,
            style = Stroke(width = 3.5f, cap = StrokeCap.Round)
        )

        // dots
        progresses.indices.forEach { i ->
            drawCircle(maxLine, radius = 5f, center = Offset(xFor(i), yFor(maxValues[i])))
            drawCircle(minLine, radius = 4f, center = Offset(xFor(i), yFor(minValues[i])))
        }
    }

    // legend
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.End
    ) {
        Box(modifier = Modifier.size(10.dp).background(Color(0xFF7C5CBF), RoundedCornerShape(2.dp)))
        Spacer(modifier = Modifier.width(4.dp))
        Text("Max", color = normalTextColor, fontSize = 11.sp)
        Spacer(modifier = Modifier.width(12.dp))
        Box(modifier = Modifier.size(10.dp).background(Color(0xFF4FC3F7), RoundedCornerShape(2.dp)))
        Spacer(modifier = Modifier.width(4.dp))
        Text("Min", color = normalTextColor, fontSize = 11.sp)
    }
}
