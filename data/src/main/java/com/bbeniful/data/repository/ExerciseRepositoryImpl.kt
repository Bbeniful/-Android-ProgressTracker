package com.bbeniful.data.repository

import com.bbeniful.data.datasource.ExerciseDataSource
import com.bbeniful.data.mapper.toData
import com.bbeniful.data.mapper.toExerciseDomain
import com.bbeniful.domain.model.BodyPart
import com.bbeniful.domain.model.Day
import com.bbeniful.domain.model.Exercise
import com.bbeniful.domain.repository.ExerciseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Single

@Single
class ExerciseRepositoryImpl(
    private val exerciseDataSource: ExerciseDataSource
) : ExerciseRepository {
    override fun getAll(): Flow<List<Exercise>> =
        flowOf(weeklyExercises) /*exerciseDataSource.getAll().map { it.toExerciseDomain }*/

    override suspend fun add(exercise: Exercise) =
        exerciseDataSource.add(exerciseDto = exercise.toData)
}


val weeklyExercises = listOf(

    Exercise(
        id = 1,
        name = "Szeles fogasu evezes, alulrol (sarga)",
        isActive = true,
        circle = 3,
        rep = 7,
        day = Day.Monday.raw,
        type = BodyPart.Back.raw
    ),
    Exercise(
        id = 2,
        name = "Szuk evezes alulrol, szeles hatra",
        isActive = true,
        circle = 3,
        rep = 8,
        day = Day.Monday.raw,
        type = BodyPart.Back.raw
    ),
    Exercise(
        id = 3,
        name = "Evezes 45 fok, csigan/barna gep",
        isActive = true,
        circle = 3,
        rep = 8,
        day = Day.Monday.raw,
        type = BodyPart.Back.raw
    ),
    Exercise(
        id = 4,
        name = "Bicepsz, szcot pad",
        isActive = true,
        circle = 2,
        rep = 10,
        day = Day.Monday.raw,
        type = BodyPart.Biceps.raw
    ),
    Exercise(
        id = 5,
        name = "Bicepsz, fuggetlen karos gepen",
        isActive = true,
        circle = 2,
        rep = 10,
        day = Day.Monday.raw,
        type = BodyPart.Biceps.raw
    ),
    Exercise(
        id = 6,
        name = "Oldalemeles ulve, egykezessel 20 fok",
        isActive = true,
        circle = 2,
        rep = 10,
        day = Day.Monday.raw,
        type = BodyPart.Shoulder.raw
    ),
    Exercise(
        id = 7,
        name = "Oldalemeles csigan, lapockacsikban",
        isActive = true,
        circle = 2,
        rep = 10,
        day = Day.Monday.raw,
        type = BodyPart.Shoulder.raw
    ),

    Exercise(
        id = 8,
        name = "Labtolo",
        isActive = true,
        circle = 3,
        rep = 8,
        day = Day.Tuesday.raw,
        type = BodyPart.Leg.raw
    ),
    Exercise(
        id = 9,
        name = "Hajlito",
        isActive = true,
        circle = 3,
        rep = 8,
        day = Day.Tuesday.raw,
        type = BodyPart.Leg.raw
    ),
    Exercise(
        id = 10,
        name = "Bolgar guggolas/kitores",
        isActive = true,
        circle = 2,
        rep = 8,
        day = Day.Tuesday.raw,
        type = BodyPart.Leg.raw
    ),
    Exercise(
        id = 11,
        name = "Tarogatas (szurke)",
        isActive = true,
        circle = 3,
        rep = 9,
        day = Day.Tuesday.raw,
        type = BodyPart.Chest.raw
    ),
    Exercise(
        id = 12,
        name = "Nyomas mogotte (also mell)",
        isActive = true,
        circle = 3,
        rep = 8,
        day = Day.Tuesday.raw,
        type = BodyPart.Chest.raw
    ),
    Exercise(
        id = 13,
        name = "Vallbol nyomas DROP",
        isActive = true,
        circle = 1,
        rep = 10,
        day = Day.Tuesday.raw,
        type = BodyPart.Shoulder.raw
    ),
    Exercise(
        id = 14,
        name = "Tolodzkodo gepen letolas",
        isActive = true,
        circle = 2,
        rep = 10,
        day = Day.Tuesday.raw,
        type = BodyPart.Triceps.raw
    ),
    Exercise(
        id = 15,
        name = "Szeles fogasu letolas",
        isActive = true,
        circle = 2,
        rep = 10,
        day = Day.Tuesday.raw,
        type = BodyPart.Triceps.raw
    ),

    Exercise(
        id = 16,
        name = "Felulrol szeles, dontot torzs",
        isActive = true,
        circle = 4,
        rep = 7,
        day = Day.Thursday.raw,
        type = BodyPart.Back.raw
    ),
    Exercise(
        id = 17,
        name = "Felulrol szuk lehuzas",
        isActive = true,
        circle = 3,
        rep = 8,
        day = Day.Thursday.raw,
        type = BodyPart.Back.raw
    ),
    Exercise(
        id = 18,
        name = "Szeles fogasu evezes alulrol (csigan)",
        isActive = true,
        circle = 2,
        rep = 8,
        day = Day.Thursday.raw,
        type = BodyPart.Back.raw
    ),
    Exercise(
        id = 19,
        name = "Egykezes bicepsz, valtott kar",
        isActive = true,
        circle = 2,
        rep = 9,
        day = Day.Thursday.raw,
        type = BodyPart.Biceps.raw
    ),
    Exercise(
        id = 20,
        name = "Egykezes bicepsz,csigan valtott kar",
        isActive = true,
        circle = 2,
        rep = 10,
        day = Day.Thursday.raw,
        type = BodyPart.Biceps.raw
    ),
    Exercise(
        id = 21,
        name = "Oldalemeles melkas tamasz padon 40",
        isActive = true,
        circle = 2,
        rep = 12,
        day = Day.Thursday.raw,
        type = BodyPart.Shoulder.raw
    ),
    Exercise(
        id = 22,
        name = "Y-emeles keresztcsigan",
        isActive = true,
        circle = 2,
        rep = 10,
        day = Day.Thursday.raw,
        type = BodyPart.Shoulder.raw
    ),


    Exercise(
        id = 23,
        name = "Nyomas (szurke fugetlenkaros gep)",
        isActive = true,
        circle = 3,
        rep = 10,
        day = Day.Friday.raw,
        type = BodyPart.Chest.raw
    ),
    Exercise(
        id = 24,
        name = "Tarogatas barna gep",
        isActive = true,
        circle = 2,
        rep = 8,
        day = Day.Friday.raw,
        type = BodyPart.Chest.raw
    ),
    Exercise(
        id = 25,
        name = "Pendulum",
        isActive = true,
        circle = 3,
        rep = 8,
        day = Day.Friday.raw,
        type = BodyPart.Leg.raw
    ),
    Exercise(
        id = 26,
        name = "Labnyujtas",
        isActive = true,
        circle = 3,
        rep = 9,
        day = Day.Friday.raw,
        type = BodyPart.Leg.raw
    ),
    Exercise(
        id = 27,
        name = "Vallbol nyomas",
        isActive = true,
        circle = 3,
        rep = 8,
        day = Day.Friday.raw,
        type = BodyPart.Shoulder.raw
    ),
    Exercise(
        id = 28,
        name = "Egykezes tricepsz kotellel 45",
        isActive = true,
        circle = 3,
        rep = 10,
        day = Day.Friday.raw,
        type = BodyPart.Triceps.raw
    ),
    Exercise(
        id = 29,
        name = "Szuk letolas csigan",
        isActive = true,
        circle = 3,
        rep = 10,
        day = Day.Friday.raw,
        type = BodyPart.Triceps.raw
    ),

    )