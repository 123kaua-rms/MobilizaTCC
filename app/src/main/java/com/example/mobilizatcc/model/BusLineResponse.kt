package com.example.mobilizatcc.model

import com.google.gson.annotations.SerializedName

data class BusLineResponse(
    @SerializedName("route_id") val routeId: String,
    @SerializedName("agency_id") val agencyId: String,
    @SerializedName("route_short_name") val routeShortName: String,
    @SerializedName("route_long_name") val routeLongName: String?,
    @SerializedName("route_type") val routeType: Int,
    @SerializedName("route_color") val routeColor: String?,
    @SerializedName("route_text_color") val routeTextColor: String?
)

