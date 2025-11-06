package com.example.mobilizatcc.model

import com.google.gson.annotations.SerializedName

data class Favorito(
    @SerializedName("id")
    val id: Int? = null,

    @SerializedName("usuario_id")
    val usuarioId: Int,

    @SerializedName("linha_id")
    val linhaId: String,

    @SerializedName("route_short_name")
    val routeShortName: String? = null,

    @SerializedName("route_long_name")
    val routeLongName: String? = null,

    @SerializedName("route_color")
    val routeColor: String? = null
)

data class FavoritoRequest(
    @SerializedName("usuario_id")
    val usuarioId: Int,

    @SerializedName("linha_id")
    val linhaId: String
)

data class FavoritosResponse(
    @SerializedName("status")
    val status: Boolean,

    @SerializedName("status_code")
    val statusCode: Int,

    @SerializedName("favoritos")
    val favoritos: List<Favorito>
)

data class FavoritoActionResponse(
    @SerializedName("status")
    val status: Boolean,

    @SerializedName("message")
    val message: String,

    @SerializedName("status_code")
    val statusCode: Int
)

