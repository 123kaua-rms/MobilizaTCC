package com.example.mobilizatcc.ui.theme.screens

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobilizatcc.model.Stop
import com.example.mobilizatcc.model.SentidoResponse
import com.example.mobilizatcc.model.StopTimeEstimativa
import com.example.mobilizatcc.service.RetrofitFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class LinhaTracadoViewModel : ViewModel() {

    private val service = RetrofitFactory().getUsuarioService()

    private val _paradas = MutableStateFlow<List<Stop>>(emptyList())
    val paradas: StateFlow<List<Stop>> = _paradas

    private val _sentido = MutableStateFlow("ida") // "ida" ou "volta"
    val sentido: StateFlow<String> = _sentido

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _stopTimesEstimativas = MutableStateFlow<List<StopTimeEstimativa>>(emptyList())
    val stopTimesEstimativas: StateFlow<List<StopTimeEstimativa>> = _stopTimesEstimativas

    // Mapa de tempo estimado por stop_id
    private val _estimativasPorStopId = MutableStateFlow<Map<String, String>>(emptyMap())
    val estimativasPorStopId: StateFlow<Map<String, String>> = _estimativasPorStopId

    private var tripIdAtual: String? = null

    fun carregarParadas(tripId: String, sentido: String = "ida") {
        viewModelScope.launch {
            try {
                Log.d("LinhaTracadoVM", "TripId enviado: $tripId - sentido: $sentido")
                _isLoading.value = true
                _error.value = null

                tripIdAtual = tripId
                _sentido.value = sentido

                val response: SentidoResponse = if (sentido == "ida") {
                    service.getParadasIda(tripId)
                } else {
                    service.getParadasVolta(tripId)
                }

                _paradas.value = response.paradas ?: emptyList()
                Log.d("LinhaTracadoVM", "Paradas carregadas: ${_paradas.value.size}")

            } catch (e: retrofit2.HttpException) {
                Log.e("LinhaTracadoVM", "HTTP error: ${e.code()} - ${e.message()}", e)
                _error.value = "Erro HTTP: ${e.code()}"
                _paradas.value = listOf(Stop("0", "Erro HTTP ${e.code()}", "0.0", "0.0"))
            } catch (e: Exception) {
                Log.e("LinhaTracadoVM", "Erro ao carregar paradas", e)
                _error.value = "Erro ao carregar paradas"
                _paradas.value = listOf(Stop("0", "Falha ao carregar paradas", "0.0", "0.0"))
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun carregarEstimativas(tripId: String) {
        viewModelScope.launch {
            try {
                Log.d("LinhaTracadoVM", "Carregando estimativas para: $tripId")
                val response = service.getStopTimesEstimativa(tripId)

                // Protege contra null
                _stopTimesEstimativas.value = response.stopTimes ?: emptyList()

                calcularEstimativasPorParada()
            } catch (e: Exception) {
                Log.e("LinhaTracadoVM", "Erro ao carregar estimativas", e)
                _stopTimesEstimativas.value = emptyList()
                _estimativasPorStopId.value = emptyMap()
            }
        }
    }

    private fun calcularEstimativasPorParada() {
        val stopTimes = _stopTimesEstimativas.value

        if (stopTimes.isNullOrEmpty()) {
            Log.w("LinhaTracadoVM", "Nenhuma estimativa recebida para cálculo.")
            _estimativasPorStopId.value = emptyMap()
            return
        }

        try {
            val estimativasMap = mutableMapOf<String, String>()

            for (stopTime in stopTimes) {
                val minutos = stopTime.estimativaMinutos
                    ?: calcularMinutosAteChegada(stopTime.arrivalTime)
                estimativasMap[stopTime.stopId] = formatarTempo(minutos)
            }

            _estimativasPorStopId.value = estimativasMap
            Log.d("LinhaTracadoVM", "Estimativas calculadas: ${estimativasMap.size} paradas")

        } catch (e: Exception) {
            Log.e("LinhaTracadoVM", "Erro ao calcular estimativas", e)
            _estimativasPorStopId.value = emptyMap()
        }
    }

    private fun calcularMinutosAteChegada(arrivalTime: String): Int {
        try {
            val sdf = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
            val agora = Calendar.getInstance()
            val horaAtual = agora.get(Calendar.HOUR_OF_DAY) * 60 + agora.get(Calendar.MINUTE)

            val chegada = sdf.parse(arrivalTime)
            if (chegada != null) {
                val calChegada = Calendar.getInstance().apply { time = chegada }
                val minChegada = calChegada.get(Calendar.HOUR_OF_DAY) * 60 + calChegada.get(Calendar.MINUTE)

                var diferenca = minChegada - horaAtual

                if (diferenca < 0) {
                    diferenca += 24 * 60 // próximo dia
                }

                return diferenca
            }
        } catch (e: Exception) {
            Log.e("LinhaTracadoVM", "Erro ao calcular minutos até chegada", e)
        }
        return 0
    }

    private fun formatarTempo(minutos: Int): String {
        return if (minutos >= 60) {
            val horas = minutos / 60
            val mins = minutos % 60
            if (mins > 0) "${horas}h ${mins}min" else "${horas}h"
        } else {
            "$minutos min"
        }
    }

    fun mudarSentido() {
        val novo = if (_sentido.value == "ida") "volta" else "ida"
        tripIdAtual?.let {
            carregarParadas(it, novo)
            carregarEstimativas(it)
        } ?: Log.w("LinhaTracadoVM", "Não há tripIdAtual definido ao mudar sentido")
    }
}
