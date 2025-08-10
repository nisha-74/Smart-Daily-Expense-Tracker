package com.example.smartdailyexpensetracker.domen.utils



import java.util.Calendar

object DateUtils {

    /**
     * Returns the start and end timestamps (in millis) for the current day.
     * start = 00:00:00
     * end   = 23:59:59
     */
    fun todayRange(): Pair<Long, Long> {
        val calendar = Calendar.getInstance()

        // Start of day
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val startOfDay = calendar.timeInMillis

        // End of day
        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 59)
        calendar.set(Calendar.MILLISECOND, 999)
        val endOfDay = calendar.timeInMillis

        return Pair(startOfDay, endOfDay)
    }
    /**
     * Returns start and end timestamps for the given date (in millis).
     * Example: If date = 10 Aug 2025, returns timestamps from
     *          10 Aug 2025 00:00:00 to 10 Aug 2025 23:59:59
     */
    fun dateRange(date: Long): Pair<Long, Long> {
        val cal = Calendar.getInstance()
        cal.timeInMillis = date

        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        val start = cal.timeInMillis

        cal.set(Calendar.HOUR_OF_DAY, 23)
        cal.set(Calendar.MINUTE, 59)
        cal.set(Calendar.SECOND, 59)
        cal.set(Calendar.MILLISECOND, 999)
        val end = cal.timeInMillis

        return Pair(start, end)
    }
}
