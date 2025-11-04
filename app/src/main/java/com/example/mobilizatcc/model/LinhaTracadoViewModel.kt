package com.example.mobilizatcc.ui.theme.screens

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobilizatcc.model.Stop
import com.example.mobilizatcc.model.SentidoResponse
import com.example.mobilizatcc.service.RetrofitFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

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

    fun mudarSentido() {
        val novo = if (_sentido.value == "ida") "volta" else "ida"
        tripIdAtual?.let {
            carregarParadas(it, novo)
        } ?: run {
            Log.w("LinhaTracadoVM", "Não há tripIdAtual definido ao mudar sentido")
        }
    }
}
