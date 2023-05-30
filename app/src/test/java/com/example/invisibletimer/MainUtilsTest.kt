package com.example.invisibletimer

import org.junit.Test
import org.junit.Assert.*

class MainUtilsTest {

    @Test
    fun testSecondsToReadableFormat() {
        // Create an instance of your class or object
        val mainUtils = MainUtils()

        // Test case 1: UI format
        val seconds1 = "3600"
        val expected1 = "1h0m0s"
        val result1 = mainUtils.secondsToReadableFormat(seconds1, true)
        assertEquals(expected1, result1)

        // Test case 2: Non-UI format
        val seconds2 = "3665"
        val expected2 = "1:1:5"
        val result2 = mainUtils.secondsToReadableFormat(seconds2, false)
        assertEquals(expected2, result2)
    }
}