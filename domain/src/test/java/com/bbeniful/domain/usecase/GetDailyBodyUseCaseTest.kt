package com.bbeniful.domain.usecase

import app.cash.turbine.test
import com.bbeniful.domain.mock.mockExerciseRepo
import com.bbeniful.domain.model.MuscleGroup
import com.bbeniful.domain.model.Day
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class GetDailyBodyUseCaseTest {

    val createWeekWorkoutPlaneUseCase =
        CreateWeekWorkoutPlaneUseCase(exerciseRepository = mockExerciseRepo)

    val useCase = GetDailyBodyUseCase(createWeekWorkoutPlaneUseCase = createWeekWorkoutPlaneUseCase)

    @Test
    fun `should contain chest`() = runTest {
        val muscleGroup = MuscleGroup.Chest.raw
        val day = Day.Tuesday
        val data = useCase(day = day)
        data.test {
            val bodyParts = awaitItem()
            Assert.assertTrue(bodyParts.orEmpty().contains(muscleGroup))
            cancelAndIgnoreRemainingEvents()
        }

    }


    @Test
    fun `should contain full format`() = runTest {
        val expected = "Back, Biceps, Shoulder"
        val day = Day.Monday
        val data = useCase(day = day)
        data.test {
            val bodyParts = awaitItem()
            Assert.assertEquals(expected, bodyParts)
            cancelAndIgnoreRemainingEvents()
        }
    }
}