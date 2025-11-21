package com.example.mobilizatcc.model

import com.google.gson.annotations.SerializedName

data class RotaInteligenteRequest(
    @SerializedName("origem") val origem: String,
    @SerializedName("destino") val destino: String
)

data class RotaInteligenteCoordenada(
    @SerializedName("texto") val texto: String,
    @SerializedName("endereco") val endereco: String,
    @SerializedName("lat") val lat: Double,
    @SerializedName("lon") val lon: Double
)

data class RotaInteligentePasso(
    @SerializedName("passo") val passo: Int,
    @SerializedName("tipo") val tipo: String,
    @SerializedName("descricao") val descricao: String,
    // Somente presente em passos de caminhada
    @SerializedName("distancia_metros") val distanciaMetros: Double? = null,
    @SerializedName("duracao_segundos") val duracaoSegundos: Double? = null,
    // Somente presente em passos de transporte
    @SerializedName("linha") val linha: String? = null,
    @SerializedName("de") val de: String? = null,
    @SerializedName("para") val para: String? = null
)

data class RotaInteligenteOpcao(
    @SerializedName("opcao") val opcao: Int,
    @SerializedName("tipo_rota") val tipoRota: String,
    @SerializedName("roteiro") val roteiro: List<RotaInteligentePasso>
)

data class RotaInteligenteResponse(
    @SerializedName("status_code") val statusCode: Int,
    @SerializedName("origem") val origem: RotaInteligenteCoordenada,
    @SerializedName("destino") val destino: RotaInteligenteCoordenada,
    @SerializedName("total_opcoes") val totalOpcoes: Int,
    @SerializedName("opcoes_rota") val opcoesRota: List<RotaInteligenteOpcao>
)
