package com.example.mobilizatcc.model

import com.google.gson.annotations.SerializedName

data class ParadaDetalhada(
    @SerializedName("stop_id")
    val stopId: String,
    
    @SerializedName("stop_name")
    val stopName: String,
    
    @SerializedName("stop_desc")
    val stopDesc: String?,
    
    @SerializedName("stop_lat")
    val stopLat: String,
    
    @SerializedName("stop_lon")
    val stopLon: String
)

data class HorarioParada(
    @SerializedName("trip_id")
    val tripId: String,
    
    @SerializedName("stop_sequence")
    val stopSequence: Int,
    
    @SerializedName("arrival_time")
    val arrivalTime: String,
    
    @SerializedName("departure_time")
    val departureTime: String,
    
    @SerializedName("paradas")
    val paradas: List<ParadaDetalhada>
)

data class StopTimesDetalhadoResponse(
    @SerializedName("status")
    val status: Boolean,
    
    @SerializedName("status_code")
    val statusCode: Int,
    
    @SerializedName("horario_paradas")
    val horarioParadas: List<HorarioParada>
)
