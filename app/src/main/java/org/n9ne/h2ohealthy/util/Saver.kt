package org.n9ne.h2ohealthy.util

import android.app.Activity
import android.content.Context

object Saver {

    private var FIRST_TIME = "FIRST_TIME"

    fun Activity.setFirstTime(isFirstTime: Boolean) {
        val sharedPreferences = getSharedPreferences("APP", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean(FIRST_TIME, isFirstTime)
        editor.apply()
    }

    fun Activity.isFirstTime(): Boolean {
        val sharedPreferences = getSharedPreferences("APP", Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean(FIRST_TIME, true)
    }
}