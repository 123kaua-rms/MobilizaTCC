package com.example.mobilizatcc.model

import com.google.gson.annotations.SerializedName

data class FeedbackListResponse(
    @SerializedName("status")
    val status: Boolean,
    @SerializedName("length")
    val length: Int,
    @SerializedName("status_code")
    val status_code: Int,
    @SerializedName("feedbacks")
    val feedbacks: List<FeedbackResponse>
)

data class FeedbackResponse(
    @SerializedName("id")
    val id: Int? = null,
    @SerializedName("id_linha")
    val id_linha: String,
    @SerializedName("id_usuario")
    val id_usuario: Int,
    @SerializedName("avaliacao")
    val avaliacao: Int,
    @SerializedName("conteudo")
    val conteudo: String,
    @SerializedName("nome_usuario")
    val nome_usuario: String? = null,
    @SerializedName("username")
    val username: String? = null,
    @SerializedName("route_short_name")
    val route_short_name: String? = null,
    @SerializedName("route_long_name")
    val route_long_name: String? = null,
    @SerializedName("foto_usuario")
    val foto_usuario: String? = null,
    @SerializedName("route_color")
    val route_color: String? = null
)

data class FeedbackRequest(
    @SerializedName("id_linha")
    val id_linha: String,
    @SerializedName("id_usuario")
    val id_usuario: Int,
    @SerializedName("avaliacao")
    val avaliacao: Int,
    @SerializedName("conteudo")
    val conteudo: String
)

data class FeedbackActionResponse(
    @SerializedName("status")
    val status: Boolean,
    @SerializedName("message")
    val message: String,
    @SerializedName("status_code")
    val status_code: Int
)