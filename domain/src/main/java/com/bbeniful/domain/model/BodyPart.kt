package com.bbeniful.domain.model

import com.bbeniful.domain.model.BodyPart.entries

enum class BodyPart(val raw: String) {
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

val BodyPart.names: List<String>
    get() = entries.map { it.raw }