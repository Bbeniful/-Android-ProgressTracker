package com.bbeniful.domain.usecase

import app.cash.turbine.test
import com.bbeniful.domain.mock.mockExerciseRepo
import com.bbeniful.domain.model.Day
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4


@RunWith(JUnit4::class)
class CreateWeekWorkoutPlaneUseCaseTest {

    val useCase = CreateWeekWorkoutPlaneUseCase(exerciseRepository = mockExerciseRepo)

    @Test
    fun `contains all day we have exercises for`() = runTest {
        val workout = useCase()
        val daysForWorkout = listOf(
            Day.Monday,
            Day.Tuesday,
            Day.Wednesday,
            Day.Thursday,

            ).map { it.raw }

        workout.test {
            val item = awaitItem()
            val days = item.workoutForWeek.keys.toList()
            for (day in days) {
                Assert.assertTrue(daysForWorkout.contains(day))
            }
            cancelAndIgnoreRemainingEvents()

        }
    }
}