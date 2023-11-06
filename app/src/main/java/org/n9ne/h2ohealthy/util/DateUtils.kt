package org.n9ne.h2ohealthy.util

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.concurrent.TimeUnit

object DateUtils {

    fun getCurrentTimeDiff(time: String): String {
        val timeFormat = SimpleDateFormat("HH:mm")
        val current = timeFormat.parse(getTime())
        val formatTime = timeFormat.parse(time)

        val diff = current.time - formatTime.time

        var resultTime = TimeUnit.MILLISECONDS.toMinutes(diff)

        return if (resultTime < 1L) {
            "few seconds ago"
        } else if (resultTime >= 60L) {
            resultTime = TimeUnit.MILLISECONDS.toHours(diff)
            "$resultTime hours ago"
        } else {
            "$resultTime minutes ago"
        }
    }

    fun getNameOfDay(year: Int, dayOfYear: Int): String {
        val calendar = Calendar.getInstance()
        calendar[Calendar.YEAR] = year
        calendar[Calendar.DAY_OF_YEAR] = dayOfYear
        val days =
            arrayOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")
        val dayIndex = calendar[Calendar.DAY_OF_WEEK]
        return days[dayIndex - 1]
    }

    fun getDate(): String {
        val time = Calendar.getInstance().time
        val formatter = SimpleDateFormat("yyyy/MM/dd")
        return formatter.format(time)
    }

    fun getTime(): String {
        val time = Calendar.getInstance().time
        val formatter = SimpleDateFormat("hh:mm")
        return formatter.format(time)
    }
}