package com.bbeniful.domain.model

data class Exercise (
    val id: Int,
    val name: String,
    val isActive: Boolean,
    val circle: Int,
    val rep: Int,
    val day: String = Day.Unknown.raw,
    val type: String = BodyPart.Unknown.raw
)
