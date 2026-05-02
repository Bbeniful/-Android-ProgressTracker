package com.bbeniful.domain.model

data class Progress(
    val id: Int,
    val exerciseId: Int,
    val timestamp: String,
    val min: Int,
    val max: Int
)