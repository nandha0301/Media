package com.example.media.pref

import android.content.Context
import android.content.SharedPreferences



class AppPreference(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("MediaPref", Context.MODE_PRIVATE)

    fun setLoginBoolean(isLoggedIn: Boolean) {
        sharedPreferences.edit().putBoolean("LoginBoolean", isLoggedIn).apply()
    }

    fun getLoginBoolean(): Boolean {
        return sharedPreferences.getBoolean("LoginBoolean", false)
    }
}