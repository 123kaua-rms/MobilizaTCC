package com.example.mobilizatcc.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobilizatcc.model.BusLineResponse
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
}
