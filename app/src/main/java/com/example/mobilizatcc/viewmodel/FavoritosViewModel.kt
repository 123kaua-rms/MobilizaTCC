package com.example.mobilizatcc.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobilizatcc.model.Favorito
import com.example.mobilizatcc.model.FavoritoRequest
import com.example.mobilizatcc.service.RetrofitFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FavoritosViewModel : ViewModel() {

    private val service = RetrofitFactory().getUsuarioService()

    private val _favoritos = MutableStateFlow<List<Favorito>>(emptyList())
    val favoritos: StateFlow<List<Favorito>> = _favoritos

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    // Set de IDs de linhas favoritas para verificação rápida
    private val _favoritosIds = MutableStateFlow<Set<String>>(emptySet())
    val favoritosIds: StateFlow<Set<String>> = _favoritosIds

    fun carregarFavoritos(usuarioId: Int) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                val response = service.getFavoritosByUsuario(usuarioId)
                _favoritos.value = response.favoritos
                _favoritosIds.value = response.favoritos.map { it.linhaId }.toSet()
            } catch (e: Exception) {
                _error.value = "Erro ao carregar favoritos"
                _favoritos.value = emptyList()
                _favoritosIds.value = emptySet()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun adicionarFavorito(usuarioId: Int, linhaId: String, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            try {
                val request = FavoritoRequest(usuarioId = usuarioId, linhaId = linhaId)
                val response = service.adicionarFavorito(request)

                if (response.status) {
                    carregarFavoritos(usuarioId) // Recarrega a lista
                    onSuccess()
                } else {
                    _error.value = response.message
                }
            } catch (e: Exception) {
                _error.value = "Erro ao adicionar favorito"
            }
        }
    }

    fun removerFavorito(usuarioId: Int, linhaId: String, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            try {
                val response = service.removerFavorito(usuarioId, linhaId)

                if (response.status) {
                    carregarFavoritos(usuarioId) // Recarrega a lista
                    onSuccess()
                } else {
                    _error.value = response.message
                }
            } catch (e: Exception) {
                _error.value = "Erro ao remover favorito"
            }
        }
    }

    fun isFavorito(linhaId: String): Boolean {
        return _favoritosIds.value.contains(linhaId)
    }

    fun toggleFavorito(usuarioId: Int, linhaId: String) {
        if (isFavorito(linhaId)) {
            removerFavorito(usuarioId, linhaId)
        } else {
            adicionarFavorito(usuarioId, linhaId)
        }
    }
}

