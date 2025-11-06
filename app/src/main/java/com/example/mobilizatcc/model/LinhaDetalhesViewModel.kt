package com.example.mobilizatcc.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobilizatcc.model.Frequencia
import com.example.mobilizatcc.model.Stop
import com.example.mobilizatcc.model.FavoritoRequest
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

    // Estados para favoritos
    private val _isFavorito = MutableStateFlow(false)
    val isFavorito: StateFlow<Boolean> = _isFavorito
    
    private val _favoritoLoading = MutableStateFlow(false)
    val favoritoLoading: StateFlow<Boolean> = _favoritoLoading

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

    fun verificarFavorito(usuarioId: Int, linhaId: String) {
        viewModelScope.launch {
            try {
                val response = service.getFavoritosByUsuario(usuarioId)
                _isFavorito.value = response.favoritos.any { it.linhaId == linhaId }
                Log.d("LinhaDetalhesVM", "Favorito verificado: ${_isFavorito.value}")
            } catch (e: Exception) {
                Log.e("LinhaDetalhesVM", "Erro ao verificar favorito", e)
                _isFavorito.value = false
            }
        }
    }

    fun toggleFavorito(usuarioId: Int, linhaId: String) {
        viewModelScope.launch {
            try {
                _favoritoLoading.value = true
                
                if (_isFavorito.value) {
                    // Remover favorito
                    val response = service.removerFavorito(usuarioId, linhaId)
                    if (response.status) {
                        _isFavorito.value = false
                        Log.d("LinhaDetalhesVM", "Favorito removido")
                    }
                } else {
                    // Adicionar favorito
                    val request = FavoritoRequest(usuarioId = usuarioId, linhaId = linhaId)
                    val response = service.adicionarFavorito(request)
                    if (response.status) {
                        _isFavorito.value = true
                        Log.d("LinhaDetalhesVM", "Favorito adicionado")
                    }
                }
            } catch (e: Exception) {
                Log.e("LinhaDetalhesVM", "Erro ao alterar favorito", e)
            } finally {
                _favoritoLoading.value = false
            }
        }
    }
}
