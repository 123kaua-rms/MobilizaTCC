package com.example.mobilizatcc.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobilizatcc.model.BusLineResponse
import com.example.mobilizatcc.service.RetrofitFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LinesViewModel : ViewModel() {

    private val _lines = MutableStateFlow<List<BusLineResponse>>(emptyList())
    val lines: StateFlow<List<  BusLineResponse>> get() = _lines

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    private val usuarioService = RetrofitFactory().getUsuarioService()

    init {
        fetchLines()
    }

    fun fetchLines() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = usuarioService.getAllLines()
                _lines.value = response
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun searchLines(query: String) {
        viewModelScope.launch {
            val allLines = usuarioService.getAllLines()
            _lines.value = if (query.isBlank()) allLines
            else allLines.filter {
                it.routeShortName.contains(query, ignoreCase = true) ||
                        it.routeLongName.contains(query, ignoreCase = true)
            }
        }
    }
}
