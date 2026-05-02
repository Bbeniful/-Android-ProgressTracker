package com.bbeniful.home.impl.ui.component

import android.R
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bbeniful.design.addNewLogSmallBg
import com.bbeniful.design.darkTextColor
import com.bbeniful.design.lightButton
import com.bbeniful.design.normalTextColor

@Composable
fun ProgressHalfSheet(exerciseName: String, onClose: () -> Unit) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.7f)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column() {
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
                    .clickable {
                        onClose()
                    },
                text = "X",
                fontSize = 25.sp,
                color = normalTextColor
            )
        }

        AddNewLog()
    }
}

@Composable
internal fun AddNewLog() {
    var startWeight by remember { mutableStateOf("") }
    var maxWeight by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .background(
                color = addNewLogSmallBg,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(horizontal = 16.dp, vertical = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Add New Log",
            fontSize = 16.sp,
            color = normalTextColor
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = startWeight,
                onValueChange = { startWeight = it },
                label = { Text("Start Weight (kg)") },
                placeholder = { Text("0.0") },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(16.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color(0xFFE1D5E7), // Adjust to match your light purple exactly
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

        Button(
            modifier = Modifier.fillMaxWidth()
                .padding(20.dp),
            onClick = {},
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