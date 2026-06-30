package com.bbeniful.progresstrackergym

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.platform.LocalContext
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.bbeniful.add.impl.ui.AddScreen
import com.bbeniful.feature.improvement.impl.ui.ImprovementScreen
import com.bbeniful.feature.improvement.nav.ImprovementNavKey
import com.bbeniful.feature.settings.impl.ui.SettingsScreen
import com.bbeniful.feature.settings.api.nav.SettingsNavKey
import com.bbeniful.feature.statistic.impl.ui.StatisticScreen
import com.bbeniful.feature.statistic.nav.StatisticNavKey
import com.bbeniful.home.impl.ui.HomeScreen
import com.bbeniful.home.impl.ui.ProgressHistoryScreen
import com.bbeniful.nav.AddNavKey
import com.bbeniful.nav.HomeNavKey
import com.bbeniful.nav.ProgressHistoryNavKey

@Composable
fun Navigation(backstack: SnapshotStateList<Any>) {

    ShakeHandler {
        if (backstack.lastOrNull() !is AddNavKey) {
            backstack.add(AddNavKey())
        }
    }

    NavDisplay(
        backStack = backstack,
        onBack = {
            backstack.removeLastOrNull()
        },
        entryProvider = entryProvider {
            entry<HomeNavKey> {
                HomeScreen(
                    toSettings = { backstack.add(SettingsNavKey) },
                    toProgressHistory = { id, name ->
                        backstack.add(ProgressHistoryNavKey(exerciseId = id, exerciseName = name))
                    }
                )
            }
            entry<ProgressHistoryNavKey> { key ->
                ProgressHistoryScreen(
                    exerciseId = key.exerciseId,
                    exerciseName = key.exerciseName,
                    onBack = { backstack.removeLastOrNull() }
                )
            }
            entry<AddNavKey> { key ->
                AddScreen(
                    exerciseId = key.exerciseId,
                    onBack = { backstack.removeLastOrNull() },
                    onDeleted = { backstack.removeLastOrNull() }
                )
            }
            entry<SettingsNavKey> {
                SettingsScreen(
                    onBack = { backstack.removeLastOrNull() },
                    onAddExercise = { backstack.add(AddNavKey()) },
                    onEditExercise = { id -> backstack.add(AddNavKey(exerciseId = id)) },
                    onOpenImprovements = { backstack.add(ImprovementNavKey) }
                )
            }
            entry<StatisticNavKey> {
                StatisticScreen()
            }
            entry<ImprovementNavKey> {
                ImprovementScreen(onBack = { backstack.removeLastOrNull() })
            }
        }
    )
}

@Composable
fun ShakeHandler(onShake: () -> Unit) {
    val context = LocalContext.current
    val currentOnShake by rememberUpdatedState(onShake)

    DisposableEffect(Unit) {
        val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        val detector = ShakeDetector { currentOnShake() }

        sensorManager.registerListener(detector, accelerometer, SensorManager.SENSOR_DELAY_UI)

        onDispose {
            sensorManager.unregisterListener(detector)
        }
    }
}
