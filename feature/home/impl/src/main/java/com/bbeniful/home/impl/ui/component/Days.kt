package com.bbeniful.home.impl.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bbeniful.design.darkButton
import com.bbeniful.design.dayBorder
import com.bbeniful.design.dayTextColor
import com.bbeniful.design.pastDay
import com.bbeniful.domain.model.Day
import com.bbeniful.domain.model.isAfter

@Composable
fun Days(
    currentDay: Day,
    restDay: Day,
    onDayClick: (Day) -> Unit
) {
    val workoutDays = Day.entries.filter {
        it != Day.Saturday && it != Day.Sunday && it != Day.Unknown
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        workoutDays.forEach {
            DayItem(
                isPastDay = currentDay.isAfter(it),
                isCurrentDay = it == currentDay,
                isRestDay = it == restDay,
                day = it,
                onDayClick = onDayClick
            )
        }
    }
}

@Composable
private fun DayItem(
    isCurrentDay: Boolean,
    isRestDay: Boolean,
    isPastDay: Boolean,
    day: Day,
    onDayClick: (Day) -> Unit
) {
    val backgroundColor = when {
        isCurrentDay -> darkButton
        isPastDay -> pastDay
        isRestDay -> Color.Transparent
        else -> Color.Transparent
    }

    val textColor = if (isCurrentDay) {
        dayTextColor
    } else {
        Color.White
    }

    val borderWeight = if (isCurrentDay) 0.dp else 1.dp

    Column(
        modifier = Modifier
            .size(
                width = 64.dp, height = 80.dp
            )
            .background(
                color = backgroundColor, shape = RoundedCornerShape(16.dp)
            )
            .border(width = borderWeight, color = dayBorder, shape = RoundedCornerShape(16.dp))
            .clickable {
                onDayClick(day)
            },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = day.raw.take(3), fontSize = 16.sp, color = textColor)
    }
}

@Preview
@Composable
fun PreviewOfDays() {
    Box(modifier = Modifier.fillMaxSize()) {
        Days(Day.Monday, Day.Wednesday) {}
    }
}