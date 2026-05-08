package com.bbeniful.progresstrackergym

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import kotlin.math.sqrt

class ShakeDetector(private val onShake: () -> Unit) : SensorEventListener {
    private val threshold = 12.0f
    private var lastUpdate: Long = 0

    override fun onSensorChanged(event: SensorEvent) {
        val curTime = System.currentTimeMillis()
        // Only check every 100ms to save battery
        if ((curTime - lastUpdate) > 100) {
            val x = event.values[0]
            val y = event.values[1]
            val z = event.values[2]

            val gForce = sqrt((x * x + y * y + z * z).toDouble()).toFloat() - SensorManager.GRAVITY_EARTH

            if (gForce > threshold) {
                onShake()
            }
            lastUpdate = curTime
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
}