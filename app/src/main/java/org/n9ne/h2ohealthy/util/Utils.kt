package org.n9ne.h2ohealthy.util

import org.n9ne.h2ohealthy.data.model.Activity
import org.n9ne.h2ohealthy.data.model.Progress
import org.n9ne.h2ohealthy.data.repo.local.WaterEntity
import java.text.SimpleDateFormat
import java.util.Calendar
import kotlin.math.roundToInt

object Utils {
    fun calculateDayProgress(list: List<WaterEntity>): Int {
        val today = DateUtils.getDate()
        var amount = 0.0
        list.forEach {
            if (it.date == today) {
                amount += it.amount.toDouble()
            }
        }

        return amount.roundToInt()
    }

    fun calculateActivities(list: List<WaterEntity>): List<Activity> {
        val result = arrayListOf<Activity>()

        val today = DateUtils.getDate()
        list.forEach {
            if (it.date == today) {

                val displayTime = DateUtils.getCurrentTimeDiff(it.time)
                val amount = (it.amount.toDouble() * 1000).roundToInt()
                val item = Activity(amount.toString(), it.date, displayTime)
                result.add(item)
            }
        }

        return result.reversed()
    }

    fun calculateWeekProgress(list: List<WaterEntity>): List<Progress> {
        val time = Calendar.getInstance()

        val result = arrayListOf<Progress>()
        for (i in 1..7) {
            time.add(Calendar.DAY_OF_YEAR, -1)
            val formatter = SimpleDateFormat("yyyy/MM/dd")
            val day = formatter.format(time.time)
            var amount = 0.0
            list.forEach {
                if (it.date == day) {
                    amount += it.amount.toDouble()
                }
            }
            val displayDay = DateUtils.getNameOfDay(time[Calendar.YEAR], time[Calendar.DAY_OF_YEAR])
            result.add(Progress(amount.toInt(), displayDay))
        }
        return result.reversed()
    }
}