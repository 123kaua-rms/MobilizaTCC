package com.example.mobilizatcc.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobilizatcc.model.BusLineResponse
import com.example.mobilizatcc.model.Favorito
import com.example.mobilizatcc.model.FavoritoRequest
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
    
    private val _favoritoLoading = MutableStateFlow<Set<String>>(emptySet())
    val favoritoLoading: StateFlow<Set<String>> = _favoritoLoading

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
    
    fun isLinhaFavorita(linhaId: String): Boolean {
        return _favoritos.value.any { it.linhaId == linhaId }
    }
    
    fun toggleFavorito(usuarioId: Int, linhaId: String) {
        viewModelScope.launch {
            try {
                // Adicionar ao loading
                _favoritoLoading.value = _favoritoLoading.value + linhaId
                
                val isFavorito = isLinhaFavorita(linhaId)
                
                if (isFavorito) {
                    // Remover favorito
                    Log.d("LinesVM", "Removendo favorito: usuário $usuarioId, linha $linhaId")
                    val response = service.removerFavorito(usuarioId, linhaId)
                    if (response.status) {
                        // Atualizar lista local
                        _favoritos.value = _favoritos.value.filter { it.linhaId != linhaId }
                        Log.d("LinesVM", "Favorito removido com sucesso: ${response.message}")
                    } else {
                        Log.e("LinesVM", "Erro ao remover favorito: ${response.message}")
                    }
                } else {
                    // Adicionar favorito
                    Log.d("LinesVM", "Adicionando favorito: usuário $usuarioId, linha $linhaId")
                    val favoritoRequest = FavoritoRequest(usuarioId = usuarioId, linhaId = linhaId)
                    val response = service.adicionarFavorito(favoritoRequest)
                    if (response.status) {
                        // Atualizar lista local - criar objeto Favorito para a lista
                        val novoFavorito = Favorito(usuarioId = usuarioId, linhaId = linhaId)
                        _favoritos.value = _favoritos.value + novoFavorito
                        Log.d("LinesVM", "Favorito adicionado com sucesso: ${response.message}")
                    } else {
                        Log.e("LinesVM", "Erro ao adicionar favorito: ${response.message}")
                    }
                }
            } catch (e: Exception) {
                Log.e("LinesVM", "Erro ao toggle favorito", e)
            } finally {
                // Remover do loading
                _favoritoLoading.value = _favoritoLoading.value - linhaId
            }
        }
    }
    
    fun isLinhaLoading(linhaId: String): Boolean {
        return _favoritoLoading.value.contains(linhaId)
    }
}
