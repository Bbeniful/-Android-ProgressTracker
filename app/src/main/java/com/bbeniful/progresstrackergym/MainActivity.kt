package com.bbeniful.progresstrackergym

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.bbeniful.data.datasource.ExerciseDataSource
import com.bbeniful.design.mainBg
import com.bbeniful.domain.model.weeklyExercises
import com.bbeniful.domain.usecase.AddExerciseUseCase
import com.bbeniful.nav.AddNavKey
import com.bbeniful.nav.HomeNavKey
import com.bbeniful.progresstrackergym.ui.theme.ProgressTrackerGYMTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ProgressTrackerGYMTheme {

                val backstack = remember { mutableStateListOf<Any>(HomeNavKey) }

                val exerciseDS: ExerciseDataSource = koinInject()
                val addExerciseUseCase: AddExerciseUseCase = koinInject()

                LaunchedEffect(Unit) {
                    launch(Dispatchers.IO) {
                        val existing = exerciseDS.getAll().first()
                        if (existing.isEmpty()) {
                            weeklyExercises.forEach { exercise ->
                                addExerciseUseCase(exercise)
                            }
                        }
                    }

                }

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(mainBg)
                            .padding(innerPadding)
                    ) {
                        Navigation(backstack = backstack)
                    }
                }
            }
        }
    }
}
