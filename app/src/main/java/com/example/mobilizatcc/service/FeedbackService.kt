package com.example.mobilizatcc.service

import com.example.mobilizatcc.model.*
import retrofit2.http.*

interface FeedbackService {

    // Lista todos os feedbacks
    @GET("feedbacks")
    suspend fun getAllFeedbacks(): FeedbackListResponse

    // Busca feedbacks por linha específica
    @GET("feedbacks/linha/{id_linha}")
    suspend fun getFeedbacksByLinha(@Path("id_linha") idLinha: String): FeedbackListResponse

    // Adiciona um novo feedback
    @Headers("Content-Type: application/json")
    @POST("feedbacks")
    suspend fun adicionarFeedback(@Body feedback: FeedbackRequest): FeedbackActionResponse
}