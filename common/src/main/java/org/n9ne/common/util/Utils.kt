package org.n9ne.common.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import org.n9ne.common.model.Progress
import org.n9ne.common.util.Mapper.toMilliLiter
import java.text.SimpleDateFormat
import java.util.Calendar
import kotlin.math.roundToInt

object Utils {
    fun Context.isOnline(): Boolean {
        val connectivityManager =
            this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                    return true
                }

                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                    return true
                }

                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                    return true
                }
            }
        }
        return false
    }

    fun calculateDayProgress(list: List<org.n9ne.common.model.Activity>): Int {
        val today = org.n9ne.common.util.DateUtils.getDate()
        var amount = 0.0
        list.forEach {
            if (it.date == today) {
                amount += (it.amount.toDouble().toMilliLiter())
            }
        }

        return amount.roundToInt()
    }

    fun calculateActivities(list: List<org.n9ne.common.model.Activity>): List<org.n9ne.common.model.Activity> {
        val result = arrayListOf<org.n9ne.common.model.Activity>()

        val today = org.n9ne.common.util.DateUtils.getDate()
        list.forEach {
            if (it.date == today) {

                val displayTime = org.n9ne.common.util.DateUtils.getCurrentTimeDiff(it.time)
                val amount = (it.amount.toDouble().toMilliLiter()).roundToInt()
                val item = org.n9ne.common.model.Activity(
                    it.id,
                    it.idUser,
                    amount.toString(),
                    it.date,
                    displayTime
                )
                result.add(item)
            }
        }

        return result.reversed()
    }

    fun calculateWeekProgress(list: List<org.n9ne.common.model.Activity>): List<Progress> {
        val time = Calendar.getInstance()

        val result = arrayListOf<Progress>()
        for (i in 1..7) {
            time.add(Calendar.DAY_OF_YEAR, -1)
            val formatter = SimpleDateFormat("yyyy/MM/dd")
            val day = formatter.format(time.time)
            var amount = 0.0
            list.forEach {
                if (it.date == day) {
                    amount += (it.amount.toDouble().toMilliLiter())
                }
            }
            val displayDay = org.n9ne.common.util.DateUtils.getNameOfDay(time[Calendar.YEAR], time[Calendar.DAY_OF_YEAR])
            result.add(Progress(amount.toInt(), displayDay))
        }
        return result.reversed()
    }
}