package com.bbeniful.domain.model

import com.bbeniful.domain.model.MuscleGroup.entries

enum class MuscleGroup(val raw: String) {
        Chest("Chest"),
        Back("Back"),
        Shoulder("Shoulder"),
        Leg("Leg"),
        Biceps("Biceps"),
        Triceps("Triceps"),
        Unknown("Unknown");

    companion object {
        fun fromName(name: String) = entries.find { it.raw == name } ?: Unknown
    }
}

val MuscleGroup.names: List<String>
    get() = entries.map { it.raw }