package com.example.mobilizatcc.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobilizatcc.model.Frequencia
import com.example.mobilizatcc.model.Stop
import com.example.mobilizatcc.service.RetrofitFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LinhaDetalhesViewModel : ViewModel() {

    private val service = RetrofitFactory().getUsuarioService()

    private val _frequencias = MutableStateFlow<List<Frequencia>>(emptyList())
    val frequencias: StateFlow<List<Frequencia>> = _frequencias

    private val _paradas = MutableStateFlow<List<Stop>>(emptyList())
    val paradas: StateFlow<List<Stop>> = _paradas

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun carregarFrequencias(linhaCodigo: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val response = service.getFrequencias(linhaCodigo)
                _frequencias.value = response.frequencia
            } catch (e: Exception) {
                e.printStackTrace()
                _frequencias.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun carregarParadas(routeId: String) {
        viewModelScope.launch {
            try {
                val response = service.getParadasIda(routeId)
                _paradas.value = response.paradas ?: emptyList()
            } catch (e: Exception) {
                e.printStackTrace()
                _paradas.value = emptyList()
            }
        }
    }
}
