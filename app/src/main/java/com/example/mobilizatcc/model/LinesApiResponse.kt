package com.example.mobilizatcc.model

import com.google.gson.annotations.SerializedName

data class LinesApiResponse(
    val status: Boolean,
    val length: Int,
    @SerializedName("status_code") val statusCode: Int,
    val linhas: List<BusLineResponse>
)

