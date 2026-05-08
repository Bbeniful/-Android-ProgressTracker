package com.bbeniful.domain.provider

import com.bbeniful.domain.model.Day

interface DateProvider {

    fun getCurrentDay(): Day

    fun getCurrentDateAsFormattedString(): String

    fun getCurrentDateOnly(): String
}