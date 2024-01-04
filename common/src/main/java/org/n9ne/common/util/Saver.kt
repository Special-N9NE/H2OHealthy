package org.n9ne.common.util

import android.content.Context
import android.content.SharedPreferences

object Saver {

    private var FIRST_TIME = "FIRST_TIME"
    private var LANGUAGE = "LANGUAGE"
    private var TOKEN = "TOKEN"
    private var EMAIL = "EMAIL"

    private var sharedPreferences: SharedPreferences? = null
    fun setup(context: Context) {
        sharedPreferences = context.getSharedPreferences("APP", Context.MODE_PRIVATE)
    }


    fun setFirstTime(isFirstTime: Boolean) {
        val editor = sharedPreferences!!.edit()
        editor.putBoolean(FIRST_TIME, isFirstTime)
        editor.apply()
    }

    fun isFirstTime(): Boolean {
        return sharedPreferences!!.getBoolean(FIRST_TIME, true)
    }

    fun setLanguage(isEn: Boolean) {
        val editor = sharedPreferences!!.edit()
        editor.putBoolean(LANGUAGE, isEn)
        editor.apply()
    }

    fun isAppEnglish(): Boolean {
        return if (sharedPreferences == null)
            true
        else
            sharedPreferences!!.getBoolean(LANGUAGE, true)
    }

    fun saveToken(token: String?) {
        val editor = sharedPreferences!!.edit()
        editor.putString(TOKEN, token)
        editor.apply()
    }

    fun getToken(): String? {
        val token = sharedPreferences!!.getString(TOKEN, null)
        return if (!token.isNullOrEmpty()) {
            "Bearer ${token}}"
        } else {
            token
        }
    }

    fun saveEmail(email: String?) {
        val editor = sharedPreferences!!.edit()
        editor.putString(EMAIL, email)
        editor.apply()
    }

    fun getEmail(): String? {
        return sharedPreferences!!.getString(EMAIL, null)
    }

}