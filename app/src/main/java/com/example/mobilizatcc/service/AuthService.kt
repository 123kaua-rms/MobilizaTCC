package com.example.mobilizatcc.service

import com.example.mobilizatcc.model.LoginRequest
import com.example.mobilizatcc.model.LoginResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
    @POST("login") // ajuste se sua rota for diferente, tipo /api/login
    suspend fun login(@Body request: LoginRequest): LoginResponse
}
