package com.bbeniful.domain.usecase

import app.cash.turbine.test
import com.bbeniful.domain.mock.mockExerciseRepo
import com.bbeniful.domain.model.BodyPart
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
        val bodyPart = BodyPart.Chest.raw
        val day = Day.Monday
        val data = useCase(day = day)
        data.test {
            val bodyParts = awaitItem()
            Assert.assertTrue(bodyParts.contains(bodyPart))
            cancelAndIgnoreRemainingEvents()
        }

    }


    @Test
    fun `should contain full format`() = runTest {
        val expected = "Chest, Biceps"
        val day = Day.Monday
        val data = useCase(day = day)
        data.test {
            val bodyParts = awaitItem()
            Assert.assertEquals(expected, bodyParts)
            cancelAndIgnoreRemainingEvents()
        }
    }
}