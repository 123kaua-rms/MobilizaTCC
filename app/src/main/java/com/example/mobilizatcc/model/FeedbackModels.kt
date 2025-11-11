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
    val conteudo: String
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