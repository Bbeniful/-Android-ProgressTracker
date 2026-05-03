package com.bbeniful.home.impl.ui.component


import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.isDigitsOnly
import com.bbeniful.design.addNewLogSmallBg
import com.bbeniful.design.darkTextColor
import com.bbeniful.design.dayBorder
import com.bbeniful.design.highlightedIconElement
import com.bbeniful.design.highlightedListElement
import com.bbeniful.design.lightButton
import com.bbeniful.design.normalTextColor
import com.bbeniful.design.progressIconTint
import com.bbeniful.domain.model.Progress
import com.bbeniful.feature.home.impl.R

@Composable
fun ProgressHalfSheet(
    exerciseName: String,
    progresses: List<Progress>,
    onClose: () -> Unit,
    onSave: (Int, Int) -> Unit
) {

    val fakeProgresses = listOf(
        Progress(id = 1, exerciseId = 101, timestamp = "2026-04-01", min = 40, max = 60),
        Progress(id = 2, exerciseId = 101, timestamp = "2026-04-15", min = 45, max = 65),
        Progress(id = 3, exerciseId = 101, timestamp = "2026-05-01", min = 50, max = 70)
    ).sortedByDescending { it.timestamp }

    val halfSheetAnimatedSize = animateFloatAsState(
        targetValue = if (progresses.isEmpty()) 0.5f else 0.7f
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(halfSheetAnimatedSize.value)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(0.8f)) {
                Text(
                    text = exerciseName,
                    fontSize = 28.sp,
                    color = normalTextColor
                )

                Text(
                    text = "Track your progress and sets",
                    fontSize = 14.sp,
                    color = normalTextColor.copy(alpha = 0.8f)
                )
            }

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.2f)
                    .clickable {
                        onClose()
                    },
                textAlign = TextAlign.End,
                text = "X",
                fontSize = 25.sp,
                color = normalTextColor
            )
        }

        AddNewLog(onSave = onSave)
        Spacer(modifier = Modifier.height(20.dp))
        ProgressList(progresses = progresses) { }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun AddNewLog(
    onSave: (Int, Int) -> Unit
) {
    var minWeight by remember { mutableStateOf("") }
    var maxWeight by remember { mutableStateOf("") }

    val localeKeyboard = LocalSoftwareKeyboardController.current
    val isKeyboardVisible = WindowInsets.isImeVisible

    Column(
        modifier = Modifier
            .background(
                color = addNewLogSmallBg,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(horizontal = 16.dp, vertical = 15.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "Add New Log",
            fontSize = 16.sp,
            color = normalTextColor
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            OutlinedTextField(
                value = minWeight,
                onValueChange = { minWeight = it },
                label = { Text("Start Weight (kg)") },
                placeholder = { Text("0.0") },
                modifier = Modifier.weight(1f),
                isError = minWeight.isEmpty(),
                shape = RoundedCornerShape(16.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color(0xFFE1D5E7),
                    unfocusedLabelColor = Color.White,
                    unfocusedPlaceholderColor = Color.Gray
                )
            )

            OutlinedTextField(
                value = maxWeight,
                onValueChange = { maxWeight = it },
                label = { Text("Max Weight (kg)") },
                placeholder = { Text("0.0") },
                modifier = Modifier.weight(1f),
                isError = maxWeight.isEmpty(),
                shape = RoundedCornerShape(16.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color(0xFFE1D5E7),
                    unfocusedLabelColor = Color.White,
                    unfocusedPlaceholderColor = Color.Gray
                )
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .padding(horizontal = 20.dp),
            onClick = {
                if (isKeyboardVisible) {
                    localeKeyboard?.hide()
                }
                if (!validateWeight(
                        min = minWeight,
                        max = maxWeight
                    )
                ) {
                    return@Button
                }

                onSave(minWeight.toInt(), maxWeight.toInt())
                minWeight = ""
                maxWeight = ""
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = lightButton
            )
        ) {
            Text(
                text = "Save",
                color = darkTextColor,
                fontSize = 16.sp
            )
        }
    }
}

@Composable
internal fun ProgressList(
    progresses: List<Progress>,
    onViewAllClicked: () -> Unit
) {

    if (progresses.isEmpty()) {
        return
    }

    Column() {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Previous Logs", fontSize = 16.sp, color = normalTextColor)
            Text(modifier = Modifier.clickable {
                onViewAllClicked()
            }, text = "View All", fontSize = 14.sp, color = normalTextColor)
        }
        Spacer(modifier = Modifier.height(20.dp))

        LazyColumn() {
            items(progresses) { progress ->
                ProgressItem(isFirst = progress.id == progresses[0].id, progress = progress)
            }
        }
    }
}

@Composable
internal fun ProgressItem(isFirst: Boolean, progress: Progress) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(
                    color = if (isFirst) highlightedListElement else dayBorder,
                    shape = RoundedCornerShape(16.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                modifier = Modifier.size(16.dp),
                painter = painterResource(R.drawable.progress_time_icon),
                contentDescription = null,
                tint = if (isFirst) highlightedIconElement else progressIconTint
            )
        }

        Spacer(modifier = Modifier.width(10.dp))
        Column() {
            Text(text = progress.timestamp, fontSize = 11.sp, color = normalTextColor)
            Text(text = progress.createMinMax(), fontSize = 14.sp, color = normalTextColor)
        }
    }
}


fun Progress.createMinMax() = "Start: ${this.min}kg | Max: ${this.max}kg"

fun validateWeight(min: String, max: String): Boolean {
    if (min.isEmpty() || max.isEmpty()) return false
    if (!min.isDigitsOnly() || !max.isDigitsOnly()) return false

    return true
}