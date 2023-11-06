package org.n9ne.h2ohealthy.util

import java.text.SimpleDateFormat
import java.util.Calendar

object DateUtils {
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