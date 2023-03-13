package com.robosoft.foursquare

import android.content.Context
import android.content.SharedPreferences

class SharedPreferenceManager(context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("sharedPreference" , Context.MODE_PRIVATE)
    val editor: SharedPreferences.Editor = sharedPreferences.edit()

    fun getAccessToken(): String {
        return "Bearer " + sharedPreferences.getString("accessToken", "")
    }

}
