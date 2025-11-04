package com.example.mobilizatcc.model


import com.google.gson.annotations.SerializedName

data class SentidoResponse(
    val status: Boolean?,
    @SerializedName("status_code")
    val statusCode: Int? = null,
    @SerializedName("route_id")
    val routeId: String? = null,
    @SerializedName("trip_id")
    val tripId: String? = null,
    @SerializedName("trip_headsign")
    val tripHeadsign: String? = null,
    @SerializedName("shape_id")
    val shapeId: String? = null,
    val sentido: Int? = null,
    val direcao: String? = null,
    @SerializedName("paradas")
    val paradas: List<Stop>? = null
)
