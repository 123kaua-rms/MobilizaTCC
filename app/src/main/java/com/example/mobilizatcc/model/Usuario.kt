package com.example.mobilizatcc.model

data class UsuarioRequest(
    val nome: String,
    val usuario: String,
    val email: String,
    val senha: String
)

data class UsuarioResponse(
    val message: String,
    val usuario: UsuarioRequest
)
