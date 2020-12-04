package com.ydh.chatyok

import android.content.Context
import androidx.core.content.edit

class LocalSession(private val context: Context) {
    private val name = "com.ydh.chatyok"
    private val sharedPreferences by lazy {
        context.getSharedPreferences(name, Context.MODE_PRIVATE)
    }
    private val tokenKey = "TOKEN_KEY"
    private val uidKey = "UID_KEY"

    var uid
        get() = sharedPreferences.getString(uidKey, "") ?: ""
        set(value) {
            sharedPreferences.edit { putString(uidKey, value) }
        }

    var token
        get() = sharedPreferences.getString(tokenKey, "") ?: ""
        set(value) {
            sharedPreferences.edit { putString(tokenKey, value) }
        }

    fun onClear() {
        sharedPreferences.edit().clear().apply()
    }
}