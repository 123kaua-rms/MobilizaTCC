package com.example.mobilizatcc.ui.theme.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.DirectionsWalk
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.mobilizatcc.R
import com.example.mobilizatcc.model.BusLineResponse
import com.example.mobilizatcc.model.RotaInteligenteOpcao
import com.example.mobilizatcc.model.RotaInteligentePasso
import com.example.mobilizatcc.ui.theme.components.RouteBadge
import com.example.mobilizatcc.viewmodel.RotasViewModel

@Composable
fun RotasScreen(
    navegacao: NavHostController?,
    origemInicial: String,
    destinoInicial: String,
    viewModel: RotasViewModel = viewModel()
) {
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val origem by viewModel.origemTexto.collectAsState()
    val destino by viewModel.destinoTexto.collectAsState()
    val opcoes by viewModel.opcoes.collectAsState()
    val linhasMap by viewModel.linhasMap.collectAsState()
    val rotaSelecionada by viewModel.rotaSelecionada.collectAsState()
    val origemResultado by viewModel.origemResultado.collectAsState()
    val destinoResultado by viewModel.destinoResultado.collectAsState()
    val buscaRealizada by viewModel.buscaRealizada.collectAsState()
    val focusManager = LocalFocusManager.current

    val tentarBuscar = {
        val origemTrim = origem.trim()
        val destinoTrim = destino.trim()
        if (origemTrim.isNotEmpty() && destinoTrim.isNotEmpty()) {
            focusManager.clearFocus(force = true)
            viewModel.buscarRotas(origemTrim, destinoTrim)
        }
    }

    androidx.compose.runtime.LaunchedEffect(Unit) {
        viewModel.carregarLinhasSeNecessario()
        val origemDecoded = java.net.URLDecoder.decode(origemInicial, Charsets.UTF_8.name())
        val destinoDecoded = java.net.URLDecoder.decode(destinoInicial, Charsets.UTF_8.name())
        viewModel.configurarBuscaInicial(origemDecoded, destinoDecoded)
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        Column(modifier = Modifier.fillMaxSize()) {

            val headerCompacto = buscaRealizada || isLoading

            HeaderRotas(
                navegacao = navegacao,
                origem = origem,
                destino = destino,
                onOrigemChange = viewModel::atualizarOrigem,
                onDestinoChange = viewModel::atualizarDestino,
                onBuscar = tentarBuscar,
                onCampoFocus = { viewModel.selecionarOpcao(null) },
                compacto = headerCompacto,
                estaCarregando = isLoading
            )

            Spacer(modifier = Modifier.height(24.dp))

            Box(modifier = Modifier.weight(1f)) {
                if (rotaSelecionada == null) {
                    RotasListaSection(
                        opcoes = opcoes,
                        linhasMap = linhasMap,
                        isLoading = isLoading,
                        error = error,
                        onDetalhesClick = { opcao -> viewModel.selecionarOpcao(opcao) }
                    )
                } else {
                    RotaDetalhadaSection(
                        origem = origemResultado.ifBlank { origem },
                        destino = destinoResultado.ifBlank { destino },
                        opcao = rotaSelecionada!!,
                        linhasMap = linhasMap,
                        onVoltarLista = { viewModel.selecionarOpcao(null) }
                    )
                }
            }

            BottomNavigationBar(
                navegacao = navegacao,
                selectedRoute = "home"
            )
        }
    }
}

@Composable
private fun HeaderRotas(
    navegacao: NavHostController?,
    origem: String,
    destino: String,
    onOrigemChange: (String) -> Unit,
    onDestinoChange: (String) -> Unit,
    onBuscar: () -> Unit,
    onCampoFocus: () -> Unit,
    compacto: Boolean,
    estaCarregando: Boolean
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Color(0xFF16A34A),
                shape = RoundedCornerShape(bottomStart = if (compacto) 24.dp else 32.dp, bottomEnd = if (compacto) 24.dp else 32.dp)
            )
            .padding(horizontal = 18.dp, vertical = if (compacto) 12.dp else 20.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (!compacto) {
                Text(
                    text = if (estaCarregando) "Buscando rotas..." else "Para onde vamos?",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(if (estaCarregando) 12.dp else 18.dp))
            } else {
                Spacer(modifier = Modifier.height(8.dp))
            }

            Card(
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .padding(horizontal = if (compacto) 14.dp else 20.dp, vertical = if (compacto) 12.dp else 16.dp)
                ) {
                    CampoEndereco(
                        titulo = "Partida",
                        marcadorColor = Color(0xFF6366F1),
                        texto = origem,
                        placeholder = "Digite a origem",
                        onValueChange = onOrigemChange,
                        onImeAction = onBuscar,
                        onCampoFocus = onCampoFocus
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                    Divider(color = Color(0xFFE5E7EB))
                    Spacer(modifier = Modifier.height(12.dp))

                    CampoEndereco(
                        titulo = "Destino",
                        marcadorColor = Color(0xFFF59E0B),
                        texto = destino,
                        placeholder = "Digite o destino",
                        onValueChange = onDestinoChange,
                        trailingContent = {
                            IconButton(onClick = onBuscar) {
                                Icon(
                                    painter = painterResource(id = R.drawable.lupa),
                                    contentDescription = "Buscar rota",
                                    tint = Color(0xFF16A34A),
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        },
                        onImeAction = onBuscar,
                        onCampoFocus = onCampoFocus
                    )
                }
            }
        }
    }
}

@Composable
private fun CampoEndereco(
    titulo: String,
    marcadorColor: Color,
    texto: String,
    placeholder: String,
    onValueChange: (String) -> Unit,
    trailingContent: (@Composable () -> Unit)? = null,
    onImeAction: () -> Unit,
    onCampoFocus: () -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(14.dp)
                .clip(CircleShape)
                .background(marcadorColor)
        )
        Spacer(modifier = Modifier.width(8.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(text = titulo, fontSize = 12.sp, color = Color.Gray)
            OutlinedTextField(
                value = texto,
                onValueChange = onValueChange,
                placeholder = {
                    Text(text = placeholder, color = Color(0xFF9CA3AF))
                },
                textStyle = LocalTextStyle.current.copy(fontSize = 14.sp, color = Color.Black),
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusChanged { if (it.isFocused) onCampoFocus() },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    errorContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = Color(0xFF16A34A)
                ),
                trailingIcon = trailingContent,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Search
                ),
                keyboardActions = KeyboardActions(
                    onSearch = { onImeAction() },
                    onDone = { onImeAction() },
                    onGo = { onImeAction() },
                    onNext = { onImeAction() },
                    onSend = { onImeAction() }
                )
            )
        }
    }
}

@Composable
private fun RotasListaSection(
    opcoes: List<RotaInteligenteOpcao>,
    linhasMap: Map<String, BusLineResponse>,
    isLoading: Boolean,
    error: String?,
    onDetalhesClick: (RotaInteligenteOpcao) -> Unit
) {
    Spacer(modifier = Modifier.height(24.dp))

    Text(
        text = "Rotas",
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        color = Color.Black,
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth()
    )

    Spacer(modifier = Modifier.height(16.dp))

    when {
        isLoading -> {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color(0xFF16A34A))
            }
        }

        error != null -> {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = error, color = Color.Gray)
            }
        }

        else -> {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                items(opcoes) { opcao ->
                    RotaItem(
                        opcao = opcao,
                        linhasMap = linhasMap,
                        onClickDetalhes = { onDetalhesClick(opcao) }
                    )
                }
                item { Spacer(modifier = Modifier.height(16.dp)) }
            }
        }
    }
}

@Composable
private fun RotaItem(
    opcao: RotaInteligenteOpcao,
    linhasMap: Map<String, BusLineResponse>,
    onClickDetalhes: () -> Unit
) {
    val linhas = opcao.roteiro.mapNotNull { it.linha }.distinct()

    Card(
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClickDetalhes() }
    ) {
        Column(modifier = Modifier.padding(horizontal = 18.dp, vertical = 16.dp)) {
            Text(
                text = opcao.tipoRota,
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF111827)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Spacer(modifier = Modifier.height(8.dp))

            PassosResumo(passos = opcao.roteiro, linhasMap = linhasMap)
        }
    }
}

@Composable
private fun PassosResumo(passos: List<RotaInteligentePasso>, linhasMap: Map<String, BusLineResponse>) {
    val resumo = passos.filter { it.tipo == "transporte" || it.tipo == "caminhada" }
    val maxItensResumo = 3
    val exibidos = resumo.take(maxItensResumo)
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        exibidos.forEachIndexed { index, passo ->
            when (passo.tipo) {
                "caminhada" -> {
                    Icon(
                        imageVector = Icons.Filled.DirectionsWalk,
                        contentDescription = "Caminhar",
                        tint = Color(0xFF16A34A),
                        modifier = Modifier.size(20.dp)
                    )
                }

                "transporte" -> {
                    val codigo = passo.linha ?: "?"
                    val linha = linhasMap[codigo]
                    MiniLinhaBadge(routeCode = linha?.routeShortName ?: codigo, colorHex = linha?.routeColor)
                }
            }

            if (index != exibidos.lastIndex) {
                Icon(
                    imageVector = Icons.Filled.ChevronRight,
                    contentDescription = "Próximo passo",
                    tint = Color(0xFF94A3B8),
                    modifier = Modifier
                        .padding(horizontal = 6.dp)
                        .size(16.dp)
                )
            }
        }
    }
}

@Composable
private fun RotaDetalhadaSection(
    origem: String,
    destino: String,
    opcao: RotaInteligenteOpcao,
    linhasMap: Map<String, BusLineResponse>,
    onVoltarLista: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "Rota detalhada",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        val scrollState = rememberScrollState()

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
                .verticalScroll(scrollState)
        ) {
            val totalItens = opcao.roteiro.size + 2

            for (index in 0 until totalItens) {
                val isPrimeiro = index == 0
                val isUltimo = index == totalItens - 1
                val passoIndex = index - 1

                Row(
                    verticalAlignment = Alignment.Top,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Min)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .width(24.dp)
                            .fillMaxHeight()
                    ) {
                        if (!isPrimeiro) {
                            Box(
                                modifier = Modifier
                                    .width(2.dp)
                                    .weight(1f)
                                    .background(Color(0xFFE5E7EB))
                            )
                        } else {
                            Spacer(modifier = Modifier.weight(1f))
                        }

                        val circleColor = when {
                            isPrimeiro || isUltimo -> Color(0xFF9CA3AF)
                            else -> {
                                val passo = opcao.roteiro[passoIndex]
                                if (passo.tipo == "transporte") Color(0xFF2563EB) else Color(0xFF9CA3AF)
                            }
                        }

                        Box(
                            modifier = Modifier
                                .size(12.dp)
                                .clip(CircleShape)
                                .background(circleColor)
                        )

                        if (!isUltimo) {
                            Box(
                                modifier = Modifier
                                    .width(2.dp)
                                    .weight(1f)
                                    .background(Color(0xFFE5E7EB))
                            )
                        } else {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(vertical = 6.dp)
                    ) {
                        when {
                            isPrimeiro -> {
                                Text(
                                    text = origem,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 14.sp,
                                    color = Color(0xFF111827),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }

                            isUltimo -> {
                                Text(
                                    text = destino,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 14.sp,
                                    color = Color(0xFF111827),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }

                            else -> {
                                val passo = opcao.roteiro[passoIndex]
                                when (passo.tipo) {
                                    "caminhada" -> {
                                        Text("A pé", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                                        passo.distanciaMetros?.let { dist ->
                                            Text("${"%.0f".format(dist)} m", fontSize = 12.sp, color = Color.Gray)
                                        }
                                    }

                                    "transporte" -> {
                                        val codigo = passo.linha ?: "?"
                                        val line = linhasMap[codigo]
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            RouteBadge(
                                                routeCode = line?.routeShortName ?: codigo,
                                                routeColorHex = line?.routeColor,
                                                maxWidth = 120.dp
                                            )
                                            Column {
                                                Text(text = passo.descricao, fontSize = 13.sp, color = Color.Black)
                                                passo.de?.let { origemPasso ->
                                                    passo.para?.let { destinoPasso ->
                                                        Text(
                                                            text = "$origemPasso → $destinoPasso",
                                                            fontSize = 12.sp,
                                                            color = Color.Gray
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    }

                                    else -> {
                                        Text(passo.descricao, fontSize = 13.sp)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            Button(
                onClick = onVoltarLista,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF16A34A)),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Voltar para rotas", color = Color.White, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Composable
private fun MiniLinhaBadge(routeCode: String, colorHex: String?) {
    val color = colorHex?.let {
        try {
            Color(android.graphics.Color.parseColor(if (it.startsWith("#")) it else "#$it"))
        } catch (_: Exception) {
            Color(0xFF2563EB)
        }
    } ?: Color(0xFF2563EB)

    Surface(
        shape = RoundedCornerShape(8.dp),
        color = color
    ) {
        Text(
            text = routeCode,
            color = Color.White,
            fontWeight = FontWeight.SemiBold,
            fontSize = 13.sp,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
        )
    }
}
