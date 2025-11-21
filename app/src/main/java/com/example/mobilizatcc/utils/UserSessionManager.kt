package com.example.mobilizatcc.utils

import android.content.Context
import android.content.SharedPreferences

class UserSessionManager(context: Context) {
    
    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    
    companion object {
        private const val PREFS_NAME = "user_session"
        private const val KEY_USER_ID = "user_id"
        private const val KEY_USER_NAME = "user_name"
        private const val KEY_USER_EMAIL = "user_email"
        private const val KEY_USER_USERNAME = "user_username"
        private const val KEY_USER_PHOTO = "user_photo"
        private const val KEY_IS_LOGGED_IN = "is_logged_in"
        private const val KEY_TOKEN = "token"
        
        @Volatile
        private var INSTANCE: UserSessionManager? = null
        
        fun getInstance(context: Context): UserSessionManager {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: UserSessionManager(context.applicationContext).also { INSTANCE = it }
            }
        }
    }
    
    /**
     * Salva os dados do usuário após login bem-sucedido
     */
    fun saveUserSession(
        userId: Int,
        name: String?,
        email: String?,
        username: String?,
        photoUrl: String? = null,
        token: String? = null
    ) {
        prefs.edit().apply {
            putInt(KEY_USER_ID, userId)
            putString(KEY_USER_NAME, name ?: "Usuário")
            putString(KEY_USER_EMAIL, email ?: "email@exemplo.com")
            putString(KEY_USER_USERNAME, username ?: "user")
            putString(KEY_USER_PHOTO, photoUrl)
            putString(KEY_TOKEN, token)
            putBoolean(KEY_IS_LOGGED_IN, true)
            apply()
        }
    }
    
    /**
     * Retorna o ID do usuário logado
     */
    fun getUserId(): Int {
        return prefs.getInt(KEY_USER_ID, -1)
    }
    
    /**
     * Retorna o nome do usuário logado
     */
    fun getUserName(): String? {
        return prefs.getString(KEY_USER_NAME, null)
    }
    
    /**
     * Retorna o email do usuário logado
     */
    fun getUserEmail(): String? {
        return prefs.getString(KEY_USER_EMAIL, null)
    }
    
    /**
     * Retorna o username do usuário logado
     */
    fun getUserUsername(): String? {
        return prefs.getString(KEY_USER_USERNAME, null)
    }
    
    /**
     * Retorna a URL da foto do usuário logado
     */
    fun getUserPhoto(): String? {
        return prefs.getString(KEY_USER_PHOTO, null)
    }
    
    /**
     * Retorna o token do usuário logado
     */
    fun getToken(): String? {
        return prefs.getString(KEY_TOKEN, null)
    }
    
    /**
     * Verifica se o usuário está logado
     */
    fun isLoggedIn(): Boolean {
        return prefs.getBoolean(KEY_IS_LOGGED_IN, false) && getUserId() != -1
    }
    
    /**
     * Limpa a sessão do usuário (logout)
     */
    fun clearSession() {
        prefs.edit().clear().apply()
    }
    
    /**
     * Atualiza apenas o nome do usuário
     */
    fun updateUserName(name: String) {
        prefs.edit().putString(KEY_USER_NAME, name).apply()
    }
    
    /**
     * Atualiza apenas o email do usuário
     */
    fun updateUserEmail(email: String) {
        prefs.edit().putString(KEY_USER_EMAIL, email).apply()
    }
}
