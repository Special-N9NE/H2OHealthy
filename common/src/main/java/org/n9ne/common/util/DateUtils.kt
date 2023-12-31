package org.n9ne.common.util

import org.n9ne.common.util.Utils.isLocalPersian
import org.n9ne.common.util.Utils.toEnglishNumbers
import saman.zamani.persiandate.PersianDate
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.Period
import java.util.Calendar
import java.util.concurrent.TimeUnit

object DateUtils {

    fun getCurrentTimeDiff(time: String): String {
        val timeFormat = SimpleDateFormat("HH:mm")
        val current = timeFormat.parse(getTime())!!
        val formatTime = timeFormat.parse(time)!!

        val diff = current.time - formatTime.time

        var resultTime = TimeUnit.MILLISECONDS.toMinutes(diff)

        val texts =
            if (isLocalPersian())
                arrayOf("لحظاتی پیش", " ساعت پیش", " دقیقه پیش")
            else
                arrayOf("few seconds ago", "hours ago", "minutes ago")

        return if (resultTime < 1L) {
            texts[0]
        } else if (resultTime >= 60L) {
            resultTime = TimeUnit.MILLISECONDS.toHours(diff)
            "$resultTime ${texts[1]}"
        } else {
            "$resultTime ${texts[2]}"
        }
    }

    fun getNameOfDay(year: Int, dayOfYear: Int): String {
        val calendar = Calendar.getInstance()
        calendar[Calendar.YEAR] = year
        calendar[Calendar.DAY_OF_YEAR] = dayOfYear
        val days = if (isLocalPersian())
            arrayOf("ی", "د", "س", "چ", "پ", "ج", "ش")
        else
            arrayOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")
        val dayIndex = calendar[Calendar.DAY_OF_WEEK]
        return days[dayIndex - 1]
    }

    fun getDate(): String {
        val time = Calendar.getInstance().time
        val formatter = SimpleDateFormat("yyyy/MM/dd")
        return formatter.format(time).toEnglishNumbers()
    }

    fun getYear(): Int {
        val time = Calendar.getInstance().time
        val formatter = SimpleDateFormat("yyyy")
        return formatter.format(time).toEnglishNumbers().toInt()
    }

    fun getMonth(): Int {
        val time = Calendar.getInstance().time
        val formatter = SimpleDateFormat("MM")
        return formatter.format(time).toEnglishNumbers().toInt()
    }

    fun getDayOfMonth(): Int {
        val time = Calendar.getInstance().time
        val formatter = SimpleDateFormat("dd")
        return formatter.format(time).toEnglishNumbers().toInt()
    }

    fun String.persianToGeorgian(): String {
        val dates = this.split("/")
        val pDate = PersianDate()
        val result = pDate.jalali_to_gregorian(dates[0].toInt(), dates[1].toInt(), dates[2].toInt())

        val builder = StringBuilder()
        result.forEach {
            builder.append("$it/")
        }
        return result.dropLast(1).toString()
    }

    fun getTime(): String {
        val time = Calendar.getInstance().time
        val formatter = SimpleDateFormat("HH:mm")
        val result = formatter.format(time)
        return result.toEnglishNumbers()
    }

    fun calculateAge(birthdate: String): Int {
        val values = birthdate.split("/")
        return Period.between(
            LocalDate.of(values[0].toInt(), values[1].toInt(), values[2].toInt()),
            LocalDate.now()
        ).years
    }
}