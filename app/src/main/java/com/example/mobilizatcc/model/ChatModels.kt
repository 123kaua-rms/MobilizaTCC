package com.example.mobilizatcc.model

import com.google.gson.annotations.SerializedName

data class ChatMessagesResponse(
    @SerializedName("status") val status: Boolean,
    @SerializedName("length") val length: Int,
    @SerializedName("status_code") val statusCode: Int,
    @SerializedName("usuario") val messages: List<ChatMessageDto>
)

data class ChatMessageDto(
    @SerializedName("id_mensagem") val idMensagem: Int,
    @SerializedName("route_id") val routeId: String,
    @SerializedName("conteudo") val conteudo: String,
    @SerializedName("data_envio") val dataEnvio: String,
    @SerializedName("usuario") val usuarios: List<ChatUserDto>
)

data class ChatUserDto(
    @SerializedName("id") val id: Int,
    @SerializedName("foto") val foto: String?,
    @SerializedName("nome") val nome: String,
    @SerializedName("username") val username: String,
    @SerializedName("email") val email: String,
    @SerializedName("senha") val senha: String?
)

data class ChatMessageRequest(
    @SerializedName("route_id") val routeId: String,
    @SerializedName("id_usuario") val idUsuario: Int,
    @SerializedName("conteudo") val conteudo: String
)

data class ChatActionResponse(
    @SerializedName("status") val status: Boolean,
    @SerializedName("message") val message: String?,
    @SerializedName("status_code") val statusCode: Int
)
