package com.example.mobilizatcc.ui.theme.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobilizatcc.model.BusLineDetailResponse
import com.example.mobilizatcc.model.Stop
import com.example.mobilizatcc.service.RetrofitFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException

class LinhaTracadoViewModel : ViewModel() {

    private val service = RetrofitFactory().getUsuarioService()

    private val _linha = MutableStateFlow<BusLineDetailResponse?>(null)
    val linha: StateFlow<BusLineDetailResponse?> = _linha

    fun carregarLinha(id: String) {
        viewModelScope.launch {
            try {
                println("Carregando linha com id: $id")
                val resposta = service.getLineById(id)

                val paradaList = resposta.parada ?: emptyList()
                val linhaComParadas = resposta.copy(parada = paradaList)

                println("Resposta recebida: $linhaComParadas")
                _linha.value = linhaComParadas

            } catch (e: HttpException) {
                println("Erro HTTP: ${e.code()} - ${e.message()}")
                when (e.code()) {
                    404 -> {
                        _linha.value = BusLineDetailResponse(
                            status = false,
                            statusCode = 404,
                            parada = listOf(
                                Stop(
                                    stopId = "0",
                                    stopName = "Linha não encontrada",
                                    stopDesc = "Nenhuma parada disponível para este código.",
                                    stopLat = "0.0",
                                    stopLon = "0.0"
                                )
                            )
                        )
                    }
                    else -> {
                        _linha.value = BusLineDetailResponse(
                            status = false,
                            statusCode = e.code(),
                            parada = listOf(
                                Stop(
                                    stopId = "0",
                                    stopName = "Erro ${e.code()}",
                                    stopDesc = "Não foi possível carregar esta linha.",
                                    stopLat = "0.0",
                                    stopLon = "0.0"
                                )
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _linha.value = BusLineDetailResponse(
                    status = false,
                    statusCode = 500,
                    parada = listOf(
                        Stop(
                            stopId = "0",
                            stopName = "Falha de conexão",
                            stopDesc = "Verifique sua internet ou tente novamente mais tarde.",
                            stopLat = "0.0",
                            stopLon = "0.0"
                        )
                    )
                )
            }
        }
    }
}
