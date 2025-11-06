package com.example.mobilizatcc.ui.theme.screens

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobilizatcc.model.Stop
import com.example.mobilizatcc.model.SentidoResponse
import com.example.mobilizatcc.model.Frequencia
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

    private val _frequencias = MutableStateFlow<List<Frequencia>>(emptyList())
    val frequencias: StateFlow<List<Frequencia>> = _frequencias

    private val _tempoEstimado = MutableStateFlow<String?>(null)
    val tempoEstimado: StateFlow<String?> = _tempoEstimado

    private var tripIdAtual: String? = null

    fun carregarParadas(tripId: String, sentido: String = "ida") {
        viewModelScope.launch {
            try {
                Log.d("LinhaTracadoVM", "TripId enviado: $tripId - sentido: $sentido")
                _isLoading.value = true
                _error.value = null

                tripIdAtual = tripId
                _sentido.value = sentido

                // Chama o endpoint correto conforme seu UsuarioService
                val response: SentidoResponse = if (sentido == "ida") {
                    service.getParadasIda(tripId)
                } else {
                    service.getParadasVolta(tripId)
                }

                // >>> AQUI: use o nome correto do campo retornado (paradas)
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

    fun carregarFrequencias(linhaCodigo: String) {
        viewModelScope.launch {
            try {
                Log.d("LinhaTracadoVM", "Carregando frequências para: $linhaCodigo")
                val response = service.getFrequencias(linhaCodigo)
                _frequencias.value = response.frequencia
                calcularTempoEstimado()
            } catch (e: Exception) {
                Log.e("LinhaTracadoVM", "Erro ao carregar frequências", e)
                _frequencias.value = emptyList()
            }
        }
    }

    private fun calcularTempoEstimado() {
        val frequencias = _frequencias.value
        if (frequencias.isEmpty()) {
            _tempoEstimado.value = null
            return
        }

        try {
            val agora = Calendar.getInstance()
            val horaAtual = agora.get(Calendar.HOUR_OF_DAY) * 60 + agora.get(Calendar.MINUTE)

            val sdf = SimpleDateFormat("HH:mm:ss", Locale.getDefault())

            for (freq in frequencias) {
                val inicio = sdf.parse(freq.start_time)
                val fim = sdf.parse(freq.end_time)

                if (inicio != null && fim != null) {
                    val calInicio = Calendar.getInstance().apply { time = inicio }
                    val calFim = Calendar.getInstance().apply { time = fim }

                    val minInicio = calInicio.get(Calendar.HOUR_OF_DAY) * 60 + calInicio.get(Calendar.MINUTE)
                    val minFim = calFim.get(Calendar.HOUR_OF_DAY) * 60 + calFim.get(Calendar.MINUTE)

                    if (horaAtual >= minInicio && horaAtual <= minFim) {
                        // Estamos dentro do intervalo desta frequência
                        val intervaloMinutos = freq.headway_secs / 60
                        val minutos = intervaloMinutos % 60
                        val horas = intervaloMinutos / 60

                        _tempoEstimado.value = if (horas > 0) {
                            "${horas}h ${minutos}min"
                        } else {
                            "${minutos} min"
                        }
                        return
                    }
                }
            }

            // Se não encontrou frequência ativa, mostra a próxima
            _tempoEstimado.value = "20 min"

        } catch (e: Exception) {
            Log.e("LinhaTracadoVM", "Erro ao calcular tempo estimado", e)
            _tempoEstimado.value = null
        }
    }

    fun mudarSentido() {
        val novo = if (_sentido.value == "ida") "volta" else "ida"
        tripIdAtual?.let {
            carregarParadas(it, novo)
        } ?: run {
            Log.w("LinhaTracadoVM", "Não há tripIdAtual definido ao mudar sentido")
        }
    }
}
