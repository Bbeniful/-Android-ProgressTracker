package com.bbeniful.domain

import com.bbeniful.domain.model.Day
import org.junit.Assert
import org.junit.Test

class DayTest {


    @Test
    fun `check if the happened or no`() {
        val target = Day.Monday
        val isHappened = Day.isAfter(target)
        Assert.assertTrue(isHappened)
    }
}