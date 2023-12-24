package org.n9ne.h2ohealthy.util

import android.app.Activity
import android.content.Context

object Saver {

    private var FIRST_TIME = "FIRST_TIME"
    private var TOKEN = "TOKEN"
    private var EMAIL = "EMAIL"

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

    fun Activity.saveToken(token: String?) {
        val sharedPreferences = getSharedPreferences("APP", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(TOKEN, token)
        editor.apply()
    }

    fun Activity.getToken(): String? {
        val sharedPreferences = getSharedPreferences("APP", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString(TOKEN, null)
        return if (!token.isNullOrEmpty()) {
            "Bearer ${token}}"
        } else {
            token
        }
    }

    fun Activity.saveEmail(email: String?) {
        val sharedPreferences = getSharedPreferences("APP", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(EMAIL, email)
        editor.apply()
    }

    fun Activity.getEmail(): String? {
        val sharedPreferences = getSharedPreferences("APP", Context.MODE_PRIVATE)
        return sharedPreferences.getString(EMAIL, null)
    }
}