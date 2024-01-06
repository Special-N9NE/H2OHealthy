package org.n9ne.common.util

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.widget.FrameLayout
import org.n9ne.common.model.Progress
import org.n9ne.common.util.Mapper.toMilliLiter
import org.n9ne.common.util.Saver.isAppEnglish
import java.security.MessageDigest
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlin.math.roundToInt

object Utils {
    fun encrypt(input: String): String {
        val md = MessageDigest.getInstance("SHA-256")
        val messageDigest = md.digest(input.toByteArray())
        return messageDigest.fold("") { str, it -> str + "%02x".format(it) }
    }

    fun Dialog.setDialog() {
        var width = FrameLayout.LayoutParams.MATCH_PARENT
        if (context.isDeviceTablet())
            width = context.dpToPx(500)
        window?.setLayout(
            width, FrameLayout.LayoutParams.WRAP_CONTENT
        )
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setCanceledOnTouchOutside(false)
    }

    fun getLocal(): Locale {
        val isEn = isAppEnglish()
        return if (isEn)
            Locale.ENGLISH
        else
            Locale("fa", "IR")
    }

    fun isLocalPersian(): Boolean {
        return (getLocal().country == "IR")
    }

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
        val today = DateUtils.getDate()
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

        val today = DateUtils.getDate()
        list.forEach {
            if (it.date == today) {

                val displayTime = DateUtils.getCurrentTimeDiff(it.time)
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
            val day = formatter.format(time.time).toEnglishNumbers()
            var amount = 0.0
            list.forEach {
                if (it.date == day) {
                    amount += (it.amount.toDouble().toMilliLiter())
                }
            }
            val displayDay = DateUtils.getNameOfDay(
                time[Calendar.YEAR],
                time[Calendar.DAY_OF_YEAR]
            )
            result.add(Progress(amount.toInt(), displayDay))
        }
        return result.reversed()
    }


    fun String.toEnglishNumbers(): String {
        var result = ""
        for (ch in this) {
            result += when (ch) {
                '۰' -> '0'
                '۱' -> '1'
                '۲' -> '2'
                '۳' -> '3'
                '۴' -> '4'
                '۵' -> '5'
                '۶' -> '6'
                '۷' -> '7'
                '۸' -> '8'
                '۹' -> '9'
                else -> ch
            }
        }
        return result
    }
}