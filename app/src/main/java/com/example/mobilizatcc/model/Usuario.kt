package com.example.mobilizatcc.model

import com.google.gson.annotations.SerializedName

data class UsuarioRequest(
    @SerializedName("foto_usuario") val fotoUsuario: String = "foto_legal.png",
    @SerializedName("nome_usuario") val nomeUsuario: String,
    @SerializedName("username") val username: String,
    @SerializedName("email") val email: String,
    @SerializedName("senha") val senha: String
)

data class UsuarioResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("foto_usuario") val fotoUsuario: String?,
    @SerializedName("nome_usuario") val nomeUsuario: String?,
    @SerializedName("username") val username: String?,
    @SerializedName("email") val email: String?,
    @SerializedName("senha") val senha: String?
)


data class Usuario(
    val nome: String? = null,
    val email: String? = null
)
