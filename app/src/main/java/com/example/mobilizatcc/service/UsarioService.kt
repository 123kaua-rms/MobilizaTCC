package com.example.mobilizatcc.service

import LoguinResponse
import com.example.mobilizatcc.model.*
import retrofit2.Call
import retrofit2.http.*

interface UsuarioService {

    // ========================= USUÁRIO =========================

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

    // ========================= LINHAS / SENTIDOS =========================

    @GET("linhas")
    suspend fun getAllLines(): LinesApiResponse

    @GET("sentidos/{trip_id}/ida")
    suspend fun getParadasIda(@Path("trip_id") tripId: String): SentidoResponse

    @GET("sentidos/{trip_id}/volta")
    suspend fun getParadasVolta(@Path("trip_id") tripId: String): SentidoResponse

    // ========================= FREQUÊNCIAS =========================

    @GET("frequencias/{linha}-0")
    suspend fun getFrequencias(
        @Path("linha") linha: String
    ): FrequenciaResponse
}
