package com.example.mobilizatcc.model

import com.google.gson.annotations.SerializedName

data class StopTimeEstimativa(
    @SerializedName("stop_id")
    val stopId: String,

    @SerializedName("stop_name")
    val stopName: String,

    @SerializedName("arrival_time")
    val arrivalTime: String,

    @SerializedName("departure_time")
    val departureTime: String,

    @SerializedName("stop_sequence")
    val stopSequence: Int,

    @SerializedName("estimativa_minutos")
    val estimativaMinutos: Int? = null
)

data class StopTimeEstimativaResponse(
    @SerializedName("status")
    val status: Boolean,

    @SerializedName("status_code")
    val statusCode: Int,

    @SerializedName("length")
    val length: Int,

    @SerializedName("stop_times")
    val stopTimes: List<StopTimeEstimativa>
)

