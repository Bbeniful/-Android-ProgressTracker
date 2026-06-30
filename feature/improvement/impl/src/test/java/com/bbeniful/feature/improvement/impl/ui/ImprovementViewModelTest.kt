package com.bbeniful.feature.improvement.impl.ui

import app.cash.turbine.test
import com.bbeniful.domain.model.Day
import com.bbeniful.domain.model.Exercise
import com.bbeniful.domain.model.MuscleGroup
import com.bbeniful.domain.model.Progress
import com.bbeniful.domain.provider.DateProvider
import com.bbeniful.domain.repository.ExerciseRepository
import com.bbeniful.domain.repository.ProgressRepository
import com.bbeniful.domain.usecase.GetAllExercisesUseCase
import com.bbeniful.domain.usecase.GetProgressForExerciseUseCase
import com.bbeniful.feature.improvement.impl.domain.GenerateImprovementRecommendationsUseCase
import com.bbeniful.feature.improvement.impl.domain.model.RecommendationType
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain

private val benchPress = Exercise(
    id = 1,
    name = "Bench Press",
    isActive = true,
    sets = 3,
    rep = 10,
    day = Day.Monday.raw,
    muscleGroup = MuscleGroup.Chest.raw
)

private val legPress = Exercise(
    id = 2,
    name = "Leg Press",
    isActive = true,
    sets = 3,
    rep = 10,
    day = Day.Tuesday.raw,
    muscleGroup = MuscleGroup.Leg.raw
)

private val benchHistory = listOf(
    Progress(exerciseId = 1, timestamp = "2026. 06. 10, 10:00:00", min = 45, max = 50),
    Progress(exerciseId = 1, timestamp = "2026. 06. 30, 10:00:00", min = 50, max = 60)
)

private val fakeExerciseRepository = object : ExerciseRepository {
    override fun getAll(): Flow<List<Exercise>> = flowOf(listOf(benchPress, legPress))
    override fun getById(id: Int): Flow<Exercise?> = flowOf(null)
    override suspend fun add(exercise: Exercise) {}
    override suspend fun deleteById(id: Int) {}
    override suspend fun getByDayAndOrder(day: String, order: Int): Exercise? = null
    override suspend fun updateCompletedDate(id: Int, date: String?) {}
}

private val fakeProgressRepository = object : ProgressRepository {
    override fun getProgressForExercise(exerciseId: Int): Flow<List<Progress>> =
        flowOf(if (exerciseId == benchPress.id) benchHistory else emptyList())
    override suspend fun add(progress: Progress) {}
    override suspend fun remove(progress: Progress) {}
    override suspend fun deleteOlderThan(cutoffDate: String) {}
}

private val fakeDateProvider = object : DateProvider {
    override fun getCurrentDay(): Day = Day.Monday
    override fun getCurrentDateAsFormattedString(): String = "2026. 06. 30, 10:00:00"
    override fun getCurrentDateOnly(): String = "2026. 06. 30"
}

@OptIn(ExperimentalCoroutinesApi::class)
class ImprovementViewModelTest : FunSpec({

    val testDispatcher = StandardTestDispatcher()

    beforeEach { Dispatchers.setMain(testDispatcher) }
    afterEach { Dispatchers.resetMain() }

    fun createViewModel() = ImprovementViewModel(
        getAllExercisesUseCase = GetAllExercisesUseCase(fakeExerciseRepository),
        getProgressForExerciseUseCase = GetProgressForExerciseUseCase(fakeProgressRepository),
        generateImprovementRecommendationsUseCase = GenerateImprovementRecommendationsUseCase(),
        dateProvider = fakeDateProvider
    )

    test("with no muscle group selected only generic tips are shown") {
        val viewModel = createViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.state.test {
            val state = expectMostRecentItem()
            state.selectedMuscleGroup shouldBe null
            state.isLoading shouldBe false
            state.recommendations.none { it.exerciseName != null } shouldBe true
            cancelAndIgnoreRemainingEvents()
        }
    }

    test("selecting a muscle group loads progress-based recommendations for its exercises") {
        val viewModel = createViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.setEvent(ImprovementIntent.SelectMuscleGroup(MuscleGroup.Chest))
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.state.test {
            val state = expectMostRecentItem()
            state.selectedMuscleGroup shouldBe MuscleGroup.Chest
            state.isLoading shouldBe false
            state.recommendations.any { it.type == RecommendationType.IncreaseWeightMomentum && it.exerciseName == "Bench Press" } shouldBe true
            cancelAndIgnoreRemainingEvents()
        }
    }

    test("selecting a muscle group with no matching exercises nudges to start logging") {
        val viewModel = createViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.setEvent(ImprovementIntent.SelectMuscleGroup(MuscleGroup.Back))
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.state.test {
            val state = expectMostRecentItem()
            state.recommendations.any { it.type == RecommendationType.LogMoreData } shouldBe true
            cancelAndIgnoreRemainingEvents()
        }
    }

    test("clearing the muscle group selection reverts to generic tips") {
        val viewModel = createViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.setEvent(ImprovementIntent.SelectMuscleGroup(MuscleGroup.Chest))
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.setEvent(ImprovementIntent.SelectMuscleGroup(null))
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.state.test {
            val state = expectMostRecentItem()
            state.selectedMuscleGroup shouldBe null
            state.recommendations.none { it.exerciseName != null } shouldBe true
            cancelAndIgnoreRemainingEvents()
        }
    }
})
