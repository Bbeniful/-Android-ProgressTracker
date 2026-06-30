package com.bbeniful.feature.improvement.impl.domain

import com.bbeniful.domain.model.Progress
import com.bbeniful.feature.improvement.impl.domain.model.Recommendation
import com.bbeniful.feature.improvement.impl.domain.model.RecommendationType
import org.koin.core.annotation.Factory
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

private val TIMESTAMP_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy. MM. dd, HH:mm:ss")
private const val TREND_SESSION_WINDOW = 3
private const val HIGH_VOLUME_WEEKLY_LOG_THRESHOLD = 4
private const val RECENT_WINDOW_DAYS = 7L

@Factory
class GenerateImprovementRecommendationsUseCase {

    operator fun invoke(context: ImprovementContext): List<Recommendation> {
        if (context.selectedMuscleGroup == null) return genericTips()

        val weightRecommendations = context.relevantExercises.flatMap { exercise ->
            weightRecommendationsFor(
                exerciseName = exercise.name,
                progress = context.progressByExerciseId[exercise.id].orEmpty()
            )
        }

        return weightRecommendations + lifestyleTips(context)
    }

    private fun weightRecommendationsFor(exerciseName: String, progress: List<Progress>): List<Recommendation> {
        val recent = progress.sortedBy { it.timestamp }.takeLast(TREND_SESSION_WINDOW)
        if (recent.size < 2) return emptyList()

        val recommendations = mutableListOf<Recommendation>()

        val hasPlateaued = recent.size >= TREND_SESSION_WINDOW && recent.last().max <= recent.first().max
        if (hasPlateaued) {
            recommendations += Recommendation(
                type = RecommendationType.IncreaseWeightPlateau,
                title = "Time to add weight",
                message = "$exerciseName hasn't moved up from ${recent.last().max}kg across your last ${recent.size} sessions. Try a heavier weight next time.",
                exerciseName = exerciseName
            )
        }

        val isRisingStreak = (1 until recent.size).all { index -> recent[index].max > recent[index - 1].max }
        if (isRisingStreak) {
            recommendations += Recommendation(
                type = RecommendationType.IncreaseWeightMomentum,
                title = "Great progress",
                message = "$exerciseName has gone up every session recently, now at ${recent.last().max}kg. Keep pushing!",
                exerciseName = exerciseName
            )
        }

        return recommendations
    }

    private fun lifestyleTips(context: ImprovementContext): List<Recommendation> {
        val hasAnyData = context.relevantExercises.any { context.progressByExerciseId[it.id].orEmpty().isNotEmpty() }
        if (!hasAnyData) {
            return listOf(
                LifestyleTipCatalog.logMoreData(),
                LifestyleTipCatalog.protein(),
                LifestyleTipCatalog.hydration(),
                LifestyleTipCatalog.creatine()
            )
        }

        val weeklyLogCount = context.relevantExercises.sumOf { exercise ->
            context.progressByExerciseId[exercise.id].orEmpty()
                .count { it.loggedWithin(days = RECENT_WINDOW_DAYS, today = context.today) }
        }
        val isHighVolume = weeklyLogCount >= HIGH_VOLUME_WEEKLY_LOG_THRESHOLD

        return listOf(
            LifestyleTipCatalog.protein(isEmphasized = isHighVolume),
            LifestyleTipCatalog.hydration(isEmphasized = isHighVolume),
            LifestyleTipCatalog.creatine(),
            LifestyleTipCatalog.sleep()
        )
    }

    private fun genericTips(): List<Recommendation> = listOf(
        LifestyleTipCatalog.protein(),
        LifestyleTipCatalog.hydration(),
        LifestyleTipCatalog.creatine(),
        LifestyleTipCatalog.sleep()
    )
}

private fun Progress.loggedWithin(days: Long, today: LocalDate): Boolean {
    val loggedDate = runCatching { LocalDateTime.parse(timestamp, TIMESTAMP_FORMATTER).toLocalDate() }.getOrNull() ?: return false
    return !loggedDate.isBefore(today.minusDays(days))
}
