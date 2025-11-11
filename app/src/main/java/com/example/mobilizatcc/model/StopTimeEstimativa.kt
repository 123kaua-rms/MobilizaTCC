package com.example.mobilizatcc.model

import com.google.gson.annotations.SerializedName

// Modelo antigo mantido para compatibilidade
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

// Novos modelos para o endpoint de estimativa por route_id
data class ParadaInfo(
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

data class HorarioParadaEstimativa(
    @SerializedName("stop_sequence")
    val stopSequence: Int,

    @SerializedName("estimativa")
    val estimativa: String,

    @SerializedName("parada")
    val parada: ParadaInfo
)

data class SentidoEstimativa(
    @SerializedName("trip_id")
    val tripId: String,

    @SerializedName("horario_paradas")
    val horarioParadas: List<HorarioParadaEstimativa>
)

data class SentidosEstimativa(
    @SerializedName("ida")
    val ida: SentidoEstimativa?,

    @SerializedName("volta")
    val volta: SentidoEstimativa?
)

data class RouteEstimativaResponse(
    @SerializedName("status")
    val status: Boolean,

    @SerializedName("status_code")
    val statusCode: Int,

    @SerializedName("route_id")
    val routeId: String,

    @SerializedName("sentidos")
    val sentidos: SentidosEstimativa
)

