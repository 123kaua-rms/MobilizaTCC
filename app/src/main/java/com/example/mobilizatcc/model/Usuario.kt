package com.example.mobilizatcc.model

data class UsuarioRequest(
    val foto: String = "foto_legal.png", // default
    val nome: String,
    val username: String,
    val email: String,
    val senha: String
)

data class UsuarioResponse(
    val message: String,
    val usuario: UsuarioRequest
)
