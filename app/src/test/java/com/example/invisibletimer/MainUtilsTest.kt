package com.example.invisibletimer

import org.junit.Test
import org.junit.Assert.*
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainUtilsTest {

    @Test
    fun testSecondsToReadableFormat() {
        // Create an instance of the class
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

    @Test
    fun testCalculateTotalTimer() {
        // Create an instance of the class
        val mainUtils = MainUtils()

        val format = SimpleDateFormat(STRINGS.FULL_DATE_FORMAT.value, Locale.getDefault())

        val startTimer = format.parse("24/03/2023 10:05:00") as Date
        val endTimer = format.parse("24/03/2023 12:05:00") as Date
        val expectedTimer = 7200.0
        val result = mainUtils.calculateTotalTimer(startTimer, endTimer)
        assertEquals(expectedTimer, result, 0.0)
    }

    @Test
    fun testSameDate() {
        // Create an instance of the class
        val mainUtils = MainUtils()

        val lastDate = "24/03/2023"
        val currentDate = "24/03/2023"
        val result1 = mainUtils.sameDate(lastDate, currentDate)
        assert(result1)

        val currentDateChange = "25/03/2023"
        val result2 = mainUtils.sameDate(lastDate, currentDateChange)
        assertFalse(result2)
    }

    @Test
    fun testCalculateTimeRemainingToMidnight() {
        // Create an instance of the class
        val mainUtils = MainUtils()

        val startTime = "24/03/2023 10:05:00"
        val expectedResult = 5835299.0
        val result = mainUtils.calculateTimeRemainingToMidnight(startTime)

        assertEquals(expectedResult, result, 0.0)
    }

}