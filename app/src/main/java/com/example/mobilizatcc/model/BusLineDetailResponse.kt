package com.example.mobilizatcc.model

import com.google.gson.annotations.SerializedName



data class Stop(
    @SerializedName("stop_id")
    val stopId: String? = null,

    @SerializedName("stop_name")
    val stopName: String? = null,

    @SerializedName("stop_lat")
    val stopLat: String? = null,

    @SerializedName("stop_lon")
    val stopLon: String? = null
)

data class BusLineDetailResponse(
    @SerializedName("status") val status: Boolean,
    @SerializedName("status_code") val statusCode: Int,
    @SerializedName("parada") val parada: List<Stop>
)
