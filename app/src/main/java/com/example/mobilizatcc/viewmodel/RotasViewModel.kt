package com.example.mobilizatcc.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobilizatcc.model.BusLineResponse
import com.example.mobilizatcc.model.LinesApiResponse
import com.example.mobilizatcc.model.RotaInteligenteOpcao
import com.example.mobilizatcc.model.RotaInteligenteRequest
import com.example.mobilizatcc.model.RotaInteligenteResponse
import com.example.mobilizatcc.service.RetrofitFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RotasViewModel : ViewModel() {

    private val service = RetrofitFactory().getUsuarioService()
    private var carregamentoInicialFeito = false

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _origemTexto = MutableStateFlow("")
    val origemTexto: StateFlow<String> = _origemTexto

    private val _destinoTexto = MutableStateFlow("")
    val destinoTexto: StateFlow<String> = _destinoTexto

    private val _opcoes = MutableStateFlow<List<RotaInteligenteOpcao>>(emptyList())
    val opcoes: StateFlow<List<RotaInteligenteOpcao>> = _opcoes

    private val _linhasMap = MutableStateFlow<Map<String, BusLineResponse>>(emptyMap())
    val linhasMap: StateFlow<Map<String, BusLineResponse>> = _linhasMap

    private val _rotaSelecionada = MutableStateFlow<RotaInteligenteOpcao?>(null)
    val rotaSelecionada: StateFlow<RotaInteligenteOpcao?> = _rotaSelecionada

    private val _origemResultado = MutableStateFlow("")
    val origemResultado: StateFlow<String> = _origemResultado

    private val _destinoResultado = MutableStateFlow("")
    val destinoResultado: StateFlow<String> = _destinoResultado

    private val _buscaRealizada = MutableStateFlow(false)
    val buscaRealizada: StateFlow<Boolean> = _buscaRealizada

    fun carregarLinhasSeNecessario() {
        if (_linhasMap.value.isNotEmpty()) return

        viewModelScope.launch {
            try {
                val response: LinesApiResponse = service.getAllLines()
                if (response.status) {
                    _linhasMap.value = response.linhas.associateBy { it.routeShortName }
                }
            } catch (_: Exception) {
            }
        }
    }

    fun configurarBuscaInicial(origem: String, destino: String) {
        if (carregamentoInicialFeito) return
        carregamentoInicialFeito = true
        atualizarOrigem(origem)
        atualizarDestino(destino)
        buscarRotas(origem, destino)
    }

    fun atualizarOrigem(texto: String) {
        _origemTexto.value = texto
    }

    fun atualizarDestino(texto: String) {
        _destinoTexto.value = texto
    }

    fun buscarRotas(origem: String, destino: String) {
        _buscaRealizada.value = true
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                _rotaSelecionada.value = null

                _origemTexto.value = origem
                _destinoTexto.value = destino

                val body = RotaInteligenteRequest(origem = origem, destino = destino)
                val response: RotaInteligenteResponse = service.getRotaInteligente(body)

                if (response.statusCode == 200) {
                    _opcoes.value = response.opcoesRota
                    _origemResultado.value = response.origem.texto.ifBlank { response.origem.endereco }
                    _destinoResultado.value = response.destino.texto.ifBlank { response.destino.endereco }
                } else {
                    _opcoes.value = emptyList()
                    _error.value = "Nenhuma rota encontrada."
                    _origemResultado.value = ""
                    _destinoResultado.value = ""
                }
            } catch (e: Exception) {
                _opcoes.value = emptyList()
                _error.value = "Erro ao buscar rotas."
                _origemResultado.value = ""
                _destinoResultado.value = ""
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun selecionarOpcao(opcao: RotaInteligenteOpcao?) {
        _rotaSelecionada.value = opcao
    }
}
