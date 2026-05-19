package com.bbeniful.domain.model

data class Progress(
    val id: Int = 0,
    val exerciseId: Int,
    val timestamp: String,
    val min: Int,
    val max: Int
)