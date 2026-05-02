package com.bbeniful.domain.model

val weeklyExercises = listOf(
    // Monday - Chest
    Exercise(id = 1, name = "Bench Press", isActive = true, circle = 4, rep = 8, day = Day.Monday.raw, type = BodyPart.Chest.raw),
    Exercise(id = 2, name = "Incline Dumbbell Press", isActive = true, circle = 3, rep = 10, day = Day.Monday.raw, type = BodyPart.Chest.raw),
    Exercise(id = 3, name = "Cable Flyes", isActive = true, circle = 3, rep = 12, day = Day.Monday.raw, type = BodyPart.Chest.raw),

    // Friday - Biceps
    Exercise(id = 14, name = "Barbell Curls", isActive = true, circle = 4, rep = 8, day = Day.Monday.raw, type = BodyPart.Biceps.raw),
    Exercise(id = 15, name = "Dumbbell Curls", isActive = true, circle = 3, rep = 10, day = Day.Monday.raw, type = BodyPart.Biceps.raw),
    Exercise(id = 16, name = "Hammer Curls", isActive = true, circle = 3, rep = 12, day = Day.Monday.raw, type = BodyPart.Biceps.raw),

    // Tuesday - Back
    Exercise(id = 4, name = "Deadlifts", isActive = true, circle = 4, rep = 6, day = Day.Tuesday.raw, type = BodyPart.Back.raw),
    Exercise(id = 5, name = "Barbell Rows", isActive = true, circle = 4, rep = 8, day = Day.Tuesday.raw, type = BodyPart.Back.raw),
    Exercise(id = 6, name = "Pull-ups", isActive = true, circle = 3, rep = 10, day = Day.Tuesday.raw, type = BodyPart.Back.raw),


    // Wednesday - Shoulder
    Exercise(id = 7, name = "Overhead Press", isActive = true, circle = 4, rep = 8, day = Day.Wednesday.raw, type = BodyPart.Shoulder.raw),
    Exercise(id = 8, name = "Lateral Raises", isActive = true, circle = 3, rep = 12, day = Day.Wednesday.raw, type = BodyPart.Shoulder.raw),
    Exercise(id = 9, name = "Reverse Pec Deck", isActive = true, circle = 3, rep = 12, day = Day.Wednesday.raw, type = BodyPart.Shoulder.raw),

    // Saturday - Triceps
    Exercise(id = 17, name = "Close-Grip Bench Press", isActive = true, circle = 4, rep = 8, day = Day.Wednesday.raw, type = BodyPart.Triceps.raw),
    Exercise(id = 18, name = "Tricep Dips", isActive = true, circle = 3, rep = 10, day = Day.Wednesday.raw, type = BodyPart.Triceps.raw),
    Exercise(id = 19, name = "Rope Pushdowns", isActive = true, circle = 3, rep = 12, day = Day.Wednesday.raw, type = BodyPart.Triceps.raw),

    // Thursday - Leg
    Exercise(id = 10, name = "Squats", isActive = true, circle = 4, rep = 8, day = Day.Thursday.raw, type = BodyPart.Leg.raw),
    Exercise(id = 11, name = "Leg Press", isActive = true, circle = 3, rep = 10, day = Day.Thursday.raw, type = BodyPart.Leg.raw),
    Exercise(id = 12, name = "Leg Curls", isActive = true, circle = 3, rep = 12, day = Day.Thursday.raw, type = BodyPart.Leg.raw),
    Exercise(id = 13, name = "Calf Raises", isActive = true, circle = 3, rep = 15, day = Day.Thursday.raw, type = BodyPart.Leg.raw),



    // Sunday - Rest (no exercises)
)