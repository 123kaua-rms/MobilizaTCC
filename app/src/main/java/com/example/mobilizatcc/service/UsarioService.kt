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


    @GET("linhas")
    suspend fun getAllLines(): LinesApiResponse

    @GET("sentidos/{trip_id}/ida")
    suspend fun getParadasIda(@Path("trip_id") tripId: String): SentidoResponse

    @GET("sentidos/{trip_id}/volta")
    suspend fun getParadasVolta(@Path("trip_id") tripId: String): SentidoResponse


    @GET("frequencias/{linha}-0")
    suspend fun getFrequencias(
        @Path("linha") linha: String
    ): FrequenciaResponse


    @GET("stop_times/{trip_id}/estimativa")
    suspend fun getStopTimesEstimativa(
        @Path("trip_id") tripId: String
    ): StopTimeEstimativaResponse

    @GET("stop_times/{trip_id}")
    suspend fun getStopTimesDetalhado(
        @Path("trip_id") tripId: String
    ): StopTimesDetalhadoResponse

    // ========================= FAVORITOS =========================

    @GET("favoritos/usuario/{usuario_id}")
    suspend fun getFavoritosByUsuario(
        @Path("usuario_id") usuarioId: Int
    ): FavoritosResponse

    @Headers("Content-Type: application/json")
    @POST("favoritos")
    suspend fun adicionarFavorito(
        @Body favorito: FavoritoRequest
    ): FavoritoActionResponse

    @DELETE("favoritos/usuario/{usuario_id}/linha/{linha_id}")
    suspend fun removerFavorito(
        @Path("usuario_id") usuarioId: Int,
        @Path("linha_id") linhaId: String
    ): FavoritoActionResponse
}
