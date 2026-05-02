package com.bbeniful.data

import com.bbeniful.domain.model.Day
import com.bbeniful.domain.provider.DateProvider
import org.koin.core.annotation.Single
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

@Single(binds = [DateProvider::class])
class DateProviderImpl : DateProvider {

    override fun getCurrentDay(): Day {
        val dayOfWeek = LocalDate.now().dayOfWeek
        val dayName = dayOfWeek.getDisplayName(TextStyle.FULL, Locale.ENGLISH)
        return Day.from(dayName)
    }
}