package com.example.mobilizatcc.service

import LoguinResponse
import com.example.mobilizatcc.model.*
import retrofit2.Call
import retrofit2.http.*

interface UsuarioService {

    @Headers("Content-Type: application/json")
    @POST("usuario")
    fun registerUser(@Body usuario: UsuarioRequest): Call<UsuarioResponse>

    @Headers("Content-Type: application/json")
    @POST("usuario/login")
    fun loguinUser(@Body request: LoginRequest): Call<LoguinResponse>

    @Headers("Content-Type: application/json")
    @POST("usuario/recuperar-senha")
    fun recuperarSenha(@Body body: RecSenhaRequest): Call<Void>

    @Headers("Content-Type: application/json")
    @POST("usuario/verificar-codigo")
    fun verificarCodigo(@Body body: VerifyCodeRequest): Call<Void>

    @Headers("Content-Type: application/json")
    @POST("usuario/resetar-senha")
    fun resetarSenha(@Body body: ResetSenhaRequest): Call<Void>

    @GET("linhas")
    suspend fun getAllLines(): LinesApiResponse

    @GET("linhas/{id}")
    suspend fun getLineById(@Path("id") id: String): BusLineResponse
}
