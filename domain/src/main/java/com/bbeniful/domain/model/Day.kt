package com.bbeniful.domain.model

import com.bbeniful.domain.model.Day.Unknown

enum class Day(val raw: String, val index: Int) {
    Monday("Monday", 1),
    Tuesday("Tuesday", 2),
    Wednesday("Wednesday", 3),
    Thursday("Thursday", 4),
    Friday("Friday", 5),
    Saturday("Saturday", 6),
    Sunday("Sunday", 7),
    Unknown("Unknown", 8);

    companion object {
        fun from(dayName: String): Day =
            entries.find { it.raw.equals(dayName, ignoreCase = true) } ?: Unknown


    }

}

fun Day.isAfter(other: Day): Boolean {
    if (this == Unknown || other == Unknown) return false
    return this.index > other.index
}