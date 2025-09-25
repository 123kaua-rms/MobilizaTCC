package com.example.mobilizatcc.service

import com.example.mobilizatcc.model.UsuarioRequest
import com.example.mobilizatcc.model.UsuarioResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface UsuarioService {
    @POST("usuarios")
    fun cadastrarUsuario(@Body usuario: UsuarioRequest): Call<UsuarioResponse>
}
