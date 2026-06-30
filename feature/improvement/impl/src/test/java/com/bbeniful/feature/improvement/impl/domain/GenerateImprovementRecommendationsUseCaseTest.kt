package com.bbeniful.feature.improvement.impl.domain

import com.bbeniful.domain.model.Day
import com.bbeniful.domain.model.Exercise
import com.bbeniful.domain.model.MuscleGroup
import com.bbeniful.domain.model.Progress
import com.bbeniful.feature.improvement.impl.domain.model.RecommendationType
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import java.time.LocalDate

private val today = LocalDate.of(2026, 6, 30)

private fun exercise(id: Int, name: String, muscleGroup: MuscleGroup = MuscleGroup.Chest) = Exercise(
    id = id,
    name = name,
    isActive = true,
    sets = 3,
    rep = 10,
    day = Day.Monday.raw,
    muscleGroup = muscleGroup.raw
)

private fun progress(exerciseId: Int, timestamp: String, max: Int, min: Int = max - 5) =
    Progress(exerciseId = exerciseId, timestamp = timestamp, min = min, max = max)

class GenerateImprovementRecommendationsUseCaseTest : FunSpec({

    val useCase = GenerateImprovementRecommendationsUseCase()

    test("no muscle group selected returns only the generic lifestyle tips") {
        val result = useCase(
            ImprovementContext(
                selectedMuscleGroup = null,
                relevantExercises = listOf(exercise(1, "Bench Press")),
                progressByExerciseId = emptyMap(),
                today = today
            )
        )

        result shouldHaveSize 4
        result.map { it.type } shouldBe listOf(
            RecommendationType.Protein,
            RecommendationType.Hydration,
            RecommendationType.Creatine,
            RecommendationType.Sleep
        )
        result.none { it.isEmphasized } shouldBe true
    }

    test("muscle group selected with no logged progress nudges to start logging") {
        val bench = exercise(1, "Bench Press")
        val result = useCase(
            ImprovementContext(
                selectedMuscleGroup = MuscleGroup.Chest,
                relevantExercises = listOf(bench),
                progressByExerciseId = emptyMap(),
                today = today
            )
        )

        result.map { it.type } shouldBe listOf(
            RecommendationType.LogMoreData,
            RecommendationType.Protein,
            RecommendationType.Hydration,
            RecommendationType.Creatine
        )
    }

    test("weight flat across the last 3 sessions triggers a plateau recommendation") {
        val bench = exercise(1, "Bench Press")
        val history = listOf(
            progress(1, "2026. 06. 10, 10:00:00", max = 50),
            progress(1, "2026. 06. 20, 10:00:00", max = 55),
            progress(1, "2026. 06. 30, 10:00:00", max = 50)
        )

        val result = useCase(
            ImprovementContext(
                selectedMuscleGroup = MuscleGroup.Chest,
                relevantExercises = listOf(bench),
                progressByExerciseId = mapOf(1 to history),
                today = today
            )
        )

        val plateauTip = result.first { it.type == RecommendationType.IncreaseWeightPlateau }
        plateauTip.exerciseName shouldBe "Bench Press"
    }

    test("weight rising every session triggers a momentum recommendation, not a plateau one") {
        val bench = exercise(1, "Bench Press")
        val history = listOf(
            progress(1, "2026. 06. 10, 10:00:00", max = 50),
            progress(1, "2026. 06. 20, 10:00:00", max = 55),
            progress(1, "2026. 06. 30, 10:00:00", max = 60)
        )

        val result = useCase(
            ImprovementContext(
                selectedMuscleGroup = MuscleGroup.Chest,
                relevantExercises = listOf(bench),
                progressByExerciseId = mapOf(1 to history),
                today = today
            )
        )

        result.none { it.type == RecommendationType.IncreaseWeightPlateau } shouldBe true
        val momentumTip = result.first { it.type == RecommendationType.IncreaseWeightMomentum }
        momentumTip.exerciseName shouldBe "Bench Press"
    }

    test("a single logged session is not enough data to judge a trend") {
        val bench = exercise(1, "Bench Press")
        val history = listOf(progress(1, "2026. 06. 30, 10:00:00", max = 50))

        val result = useCase(
            ImprovementContext(
                selectedMuscleGroup = MuscleGroup.Chest,
                relevantExercises = listOf(bench),
                progressByExerciseId = mapOf(1 to history),
                today = today
            )
        )

        result.none { it.type == RecommendationType.IncreaseWeightPlateau || it.type == RecommendationType.IncreaseWeightMomentum } shouldBe true
    }

    test("4 or more sessions logged this week emphasizes protein and hydration tips") {
        val exercises = (1..4).map { exercise(it, "Exercise $it") }
        val progressByExerciseId = exercises.associate { exercise ->
            exercise.id to listOf(progress(exercise.id, "2026. 06. 28, 09:00:00", max = 40))
        }

        val result = useCase(
            ImprovementContext(
                selectedMuscleGroup = MuscleGroup.Chest,
                relevantExercises = exercises,
                progressByExerciseId = progressByExerciseId,
                today = today
            )
        )

        result.first { it.type == RecommendationType.Protein }.isEmphasized shouldBe true
        result.first { it.type == RecommendationType.Hydration }.isEmphasized shouldBe true
        result.first { it.type == RecommendationType.Creatine }.isEmphasized shouldBe false
    }

    test("fewer than 4 sessions logged this week keeps lifestyle tips at default emphasis") {
        val bench = exercise(1, "Bench Press")
        val history = listOf(progress(1, "2026. 06. 28, 09:00:00", max = 40))

        val result = useCase(
            ImprovementContext(
                selectedMuscleGroup = MuscleGroup.Chest,
                relevantExercises = listOf(bench),
                progressByExerciseId = mapOf(1 to history),
                today = today
            )
        )

        result.none { it.isEmphasized } shouldBe true
    }
})
