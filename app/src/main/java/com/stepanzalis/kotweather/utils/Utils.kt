package com.stepanzalis.kotweather.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import java.text.SimpleDateFormat
import java.util.*

object Utils {

    fun savePreference(name: String, value: String, context: Context) {
        val prefs = context.getSharedPreferences("pref", Context.MODE_PRIVATE)
        prefs.edit().putString(name, value).apply()
    }

    fun getPreferenceString(name: String, context: Context): String {
        val prefs = context.getSharedPreferences("pref", Context.MODE_PRIVATE)
        return prefs.getString(name, "")
    }

    fun getActualTime(): String {
        val formattedTime = SimpleDateFormat(" HH:mm", Locale.ENGLISH)
        val time: Date = Calendar.getInstance().time
        return formattedTime.format(time)
    }

    fun isConnected(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }
}