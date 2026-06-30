package com.bbeniful.feature.improvement.impl.domain.model

data class Recommendation(
    val type: RecommendationType,
    val title: String,
    val message: String,
    val exerciseName: String? = null,
    val isEmphasized: Boolean = false
)
