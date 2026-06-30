package com.bbeniful.domain

import com.bbeniful.domain.model.Day
import com.bbeniful.domain.model.isAfter
import org.junit.Assert
import org.junit.Test

class DayTest {


    @Test
    fun `check if the happened or no`() {
        val target = Day.Monday
        val isHappened = Day.Friday.isAfter(target)
        Assert.assertTrue(isHappened)
    }
}