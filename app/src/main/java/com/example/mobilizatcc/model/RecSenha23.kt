package com.example.mobilizatcc.model

data class ResetSenhaRequest(
    val email: String,
    val codigo: Int,
    val senha: String
)
