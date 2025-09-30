package com.example.mobilizatcc.service

import com.example.mobilizatcc.model.UsuarioRequest
import com.example.mobilizatcc.model.UsuarioResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface UserService {

    @Headers("Content-Type: application/json")
    @POST("usuario")
    fun registerUser(@Body user: UsuarioRequest): Call<UsuarioResponse>

}
