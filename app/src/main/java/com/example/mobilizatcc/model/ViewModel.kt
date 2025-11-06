package com.example.mobilizatcc.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobilizatcc.model.BusLineResponse
import com.example.mobilizatcc.model.Favorito
import com.example.mobilizatcc.service.RetrofitFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LinesViewModel : ViewModel() {

    private val service = RetrofitFactory().getUsuarioService()

    private val _lines = MutableStateFlow<List<BusLineResponse>>(emptyList())
    val lines: StateFlow<List<BusLineResponse>> = _lines

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading
    
    // Estados para favoritos
    private val _favoritos = MutableStateFlow<List<Favorito>>(emptyList())
    val favoritos: StateFlow<List<Favorito>> = _favoritos
    
    private val _showingFavoritos = MutableStateFlow(false)
    val showingFavoritos: StateFlow<Boolean> = _showingFavoritos

    fun fetchLines() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val response = service.getAllLines()
                if (response.status) {
                    _lines.value = response.linhas
                } else {
                    _lines.value = emptyList()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _lines.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun fetchFavoritos(usuarioId: Int) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val response = service.getFavoritosByUsuario(usuarioId)
                _favoritos.value = response.favoritos
                Log.d("LinesVM", "Favoritos carregados: ${response.favoritos.size}")
            } catch (e: Exception) {
                Log.e("LinesVM", "Erro ao carregar favoritos", e)
                _favoritos.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun toggleShowFavoritos() {
        _showingFavoritos.value = !_showingFavoritos.value
    }
    
    fun getLinhasFavoritas(): List<BusLineResponse> {
        val favoritosIds = _favoritos.value.map { it.linhaId }.toSet()
        return _lines.value.filter { it.routeId in favoritosIds }
    }
}
