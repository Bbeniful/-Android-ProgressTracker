package com.bbeniful.feature.statistic.impl

import app.cash.turbine.test
import com.bbeniful.domain.model.Day
import com.bbeniful.domain.model.Exercise
import com.bbeniful.domain.model.MuscleGroup
import com.bbeniful.domain.model.Progress
import com.bbeniful.domain.repository.ExerciseRepository
import com.bbeniful.domain.repository.ProgressRepository
import com.bbeniful.domain.usecase.CreateWeekWorkoutPlaneUseCase
import com.bbeniful.domain.usecase.GetProgressForExerciseUseCase
import com.bbeniful.feature.statistic.impl.ui.StatisticIntent
import com.bbeniful.feature.statistic.impl.ui.StatisticViewModel
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain

private val fakeExercises = listOf(
    Exercise(id = 1, name = "Bench Press", sets = 3, rep = 10, day = Day.Monday.raw, muscleGroup = MuscleGroup.Chest.raw),
    Exercise(id = 2, name = "Squat", sets = 4, rep = 8, day = Day.Tuesday.raw, muscleGroup = MuscleGroup.Leg.raw)
)

private val fakeProgresses = listOf(
    Progress(id = 1, exerciseId = 1, timestamp = "2026-05-01", min = 60, max = 80),
    Progress(id = 2, exerciseId = 1, timestamp = "2026-05-05", min = 65, max = 85)
)

private val fakeExerciseRepository = object : ExerciseRepository {
    override fun getAll(): Flow<List<Exercise>> = flowOf(fakeExercises)
    override suspend fun add(exercise: Exercise) {}
}

private val fakeProgressRepository = object : ProgressRepository {
    override fun getProgressForExercise(exerciseId: Int): Flow<List<Progress>> =
        flowOf(fakeProgresses.filter { it.exerciseId == exerciseId })
    override suspend fun add(progress: Progress) {}
    override suspend fun remove(progress: Progress) {}
}

@OptIn(ExperimentalCoroutinesApi::class)
class StatisticViewModelTest : FunSpec({

    val testDispatcher = StandardTestDispatcher()

    beforeEach { Dispatchers.setMain(testDispatcher) }
    afterEach { Dispatchers.resetMain() }

    fun createViewModel() = StatisticViewModel(
        createWeekWorkoutPlaneUseCase = CreateWeekWorkoutPlaneUseCase(fakeExerciseRepository),
        getProgressForExerciseUseCase = GetProgressForExerciseUseCase(fakeProgressRepository)
    )

    test("initial load populates all exercises") {
        val viewModel = createViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.state.test {
            val loaded = expectMostRecentItem()
            loaded.exercises shouldHaveSize fakeExercises.size
            loaded.isLoading shouldBe false
            cancelAndIgnoreRemainingEvents()
        }
    }

    test("selecting an exercise updates selectedExercise and loads its progress") {
        val viewModel = createViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.setEvent(StatisticIntent.SelectExercise(fakeExercises[0]))
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.state.test {
            val state = expectMostRecentItem()
            state.selectedExercise shouldBe fakeExercises[0]
            state.progresses shouldHaveSize 2
            cancelAndIgnoreRemainingEvents()
        }
    }

    test("clearing selection resets selectedExercise and progresses") {
        val viewModel = createViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.setEvent(StatisticIntent.SelectExercise(fakeExercises[0]))
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.setEvent(StatisticIntent.ClearSelection)
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.state.test {
            val state = expectMostRecentItem()
            state.selectedExercise shouldBe null
            state.progresses.shouldBeEmpty()
            cancelAndIgnoreRemainingEvents()
        }
    }

    test("selecting exercise with no progress returns empty progresses") {
        val viewModel = createViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.setEvent(StatisticIntent.SelectExercise(fakeExercises[1]))
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.state.test {
            val state = expectMostRecentItem()
            state.selectedExercise shouldNotBe null
            state.progresses.shouldBeEmpty()
            cancelAndIgnoreRemainingEvents()
        }
    }
})
