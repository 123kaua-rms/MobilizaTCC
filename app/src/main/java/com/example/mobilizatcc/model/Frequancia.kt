package com.example.mobilizatcc.model

data class FrequenciaResponse(
    val status: Boolean,
    val length: Int,
    val status_code: Int,
    val frequencia: List<Frequencia>
)

data class Frequencia(
    val trip_id: String,
    val start_time: String,
    val end_time: String,
    val headway_secs: Int
)
