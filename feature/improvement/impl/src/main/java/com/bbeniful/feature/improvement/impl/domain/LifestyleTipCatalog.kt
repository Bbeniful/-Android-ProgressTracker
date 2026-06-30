package com.bbeniful.feature.improvement.impl.domain

import com.bbeniful.feature.improvement.impl.domain.model.Recommendation
import com.bbeniful.feature.improvement.impl.domain.model.RecommendationType

internal object LifestyleTipCatalog {

    fun protein(isEmphasized: Boolean = false) = Recommendation(
        type = RecommendationType.Protein,
        title = "Prioritize protein",
        message = "Aim for roughly 1.6-2.2g of protein per kg of bodyweight to support muscle recovery and growth.",
        isEmphasized = isEmphasized
    )

    fun hydration(isEmphasized: Boolean = false) = Recommendation(
        type = RecommendationType.Hydration,
        title = "Stay hydrated",
        message = "Drink water consistently through the day, not just around your workout, to support performance and recovery.",
        isEmphasized = isEmphasized
    )

    fun creatine() = Recommendation(
        type = RecommendationType.Creatine,
        title = "Consider creatine",
        message = "3-5g of creatine monohydrate daily is one of the most well-studied ways to support strength and muscle gains over time."
    )

    fun sleep() = Recommendation(
        type = RecommendationType.Sleep,
        title = "Protect your sleep",
        message = "Most muscle repair happens during sleep — aim for 7-9 hours a night, especially after heavy training."
    )

    fun logMoreData() = Recommendation(
        type = RecommendationType.LogMoreData,
        title = "Log your sets",
        message = "Log your weight after each session for this muscle group to start getting personalized tips here."
    )
}
