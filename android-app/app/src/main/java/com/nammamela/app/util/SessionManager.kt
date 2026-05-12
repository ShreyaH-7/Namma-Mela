package com.nammamela.app.util

import android.content.Context
import androidx.core.content.edit

class SessionManager(context: Context) {

    private val preferences = context.getSharedPreferences("namma_mela_session", Context.MODE_PRIVATE)

    fun saveSession(token: String, userId: String, name: String, email: String, role: String) {
        preferences.edit {
            putString(KEY_TOKEN, token)
            putString(KEY_USER_ID, userId)
            putString(KEY_NAME, name)
            putString(KEY_EMAIL, email)
            putString(KEY_ROLE, role)
        }
    }

    fun clearSession() {
        preferences.edit { clear() }
    }

    fun getToken(): String? = preferences.getString(KEY_TOKEN, null)
    fun getUserId(): String = preferences.getString(KEY_USER_ID, "").orEmpty()
    fun getName(): String = preferences.getString(KEY_NAME, "").orEmpty()
    fun getEmail(): String = preferences.getString(KEY_EMAIL, "").orEmpty()
    fun getRole(): String = preferences.getString(KEY_ROLE, "").orEmpty()
    fun isLoggedIn(): Boolean = !getToken().isNullOrBlank()
    fun isAdmin(): Boolean = getRole() == "admin"

    private companion object {
        const val KEY_TOKEN = "token"
        const val KEY_USER_ID = "user_id"
        const val KEY_NAME = "name"
        const val KEY_EMAIL = "email"
        const val KEY_ROLE = "role"
    }
}
