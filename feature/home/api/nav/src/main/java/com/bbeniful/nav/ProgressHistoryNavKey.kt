package com.bbeniful.nav

import kotlinx.serialization.Serializable

@Serializable
data class ProgressHistoryNavKey(
    val exerciseId: Int,
    val exerciseName: String
)
