package com.example.mobilizatcc.service

import LoguinResponse
import com.example.mobilizatcc.model.LoginRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface AuthService {
    @POST("usuario/login") // ajuste para a rota exata do seu backend
    @Headers("Content-Type: application/json")

    fun login(@Body request: LoginRequest): Call<LoguinResponse>
}
