package com.example.mobilizatcc.ui.theme.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.mobilizatcc.R
import com.example.mobilizatcc.model.BusLineResponse
import com.example.mobilizatcc.utils.UserSessionManager
import com.example.mobilizatcc.viewmodel.LinesViewModel
import kotlin.random.Random

@Composable
fun LinesScreen(
    navegacao: NavHostController?,
    viewModel: LinesViewModel = viewModel()
) {
    val context = LocalContext.current
    val userSessionManager = remember { UserSessionManager.getInstance(context) }
    
    var searchQuery by remember { mutableStateOf("") }
    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabs = listOf("Ônibus", "Metrô", "Trem")

    val lines by viewModel.lines.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val favoritos by viewModel.favoritos.collectAsState()
    val showingFavoritos by viewModel.showingFavoritos.collectAsState()
    val greenColor = Color(0xFF16A34A)
    
    // Obter ID do usuário logado
    val usuarioId = userSessionManager.getUserId()
    
    // Log para debug
    Log.d("LinesScreen", "Usuário ID para favoritos: $usuarioId")

    LaunchedEffect(Unit) {
        viewModel.fetchLines()
        if (usuarioId != -1) {
            Log.d("LinesScreen", "Buscando favoritos para usuário ID: $usuarioId")
            viewModel.fetchFavoritos(usuarioId)
        } else {
            Log.w("LinesScreen", "Usuário não está logado, não buscando favoritos")
        }
    }

    // 🔹 Mapeamento: índice da tab -> routeType
    // 0 (Ônibus) -> routeType 3
    // 1 (Metrô) -> routeType 1
    // 2 (Trem) -> routeType 2
    val routeTypeMap = mapOf(0 to 3, 1 to 1, 2 to 2)

    // 🔹 Linhas filtradas por tipo de transporte, busca e favoritos
    val filteredLines = remember(lines, searchQuery, selectedTabIndex, showingFavoritos, favoritos) {
        val selectedRouteType = routeTypeMap[selectedTabIndex] ?: 3

        // Se estiver mostrando favoritos
        if (showingFavoritos) {
            val linhasFavoritas = viewModel.getLinhasFavoritas()
            // Aplica filtro de tipo e busca nas linhas favoritas
            val favoritasByType = linhasFavoritas.filter { it.routeType == selectedRouteType }
            
            if (searchQuery.isBlank()) {
                favoritasByType
            } else {
                favoritasByType.filter {
                    it.routeShortName.contains(searchQuery, ignoreCase = true) ||
                            (it.routeLongName?.contains(searchQuery, ignoreCase = true) ?: false)
                }
            }
        } else {
            // Comportamento normal quando não está mostrando favoritos
            val linesByType = lines.filter { it.routeType == selectedRouteType }
            
            if (searchQuery.isBlank()) {
                linesByType.shuffled(Random(System.currentTimeMillis())).take(8)
            } else {
                linesByType.filter {
                    it.routeShortName.contains(searchQuery, ignoreCase = true) ||
                            (it.routeLongName?.contains(searchQuery, ignoreCase = true) ?: false)
                }
            }
        }
    }

    Scaffold(
        bottomBar = { BottomNavigationBar(navegacao = navegacao, selectedRoute = "linhas") }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color.White)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp)
            ) {
                Header()
                Spacer(modifier = Modifier.height(16.dp))
                SearchField(value = searchQuery, onValueChange = { searchQuery = it })
                Spacer(modifier = Modifier.height(16.dp))
                TransportTabs(
                    selectedTabIndex = selectedTabIndex,
                    onTabSelected = { selectedTabIndex = it },
                    tabs = tabs,
                    showingFavoritos = showingFavoritos,
                    onFavoritosClick = { 
                        viewModel.toggleShowFavoritos()
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))

                if (isLoading) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = greenColor,)

                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp)
                    ) {
                        items(filteredLines) { line ->
                            BusLineItem(line, navegacao)
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BusLineItem(line: BusLineResponse, navegacao: NavHostController?) {
    val longName = line.routeLongName ?: ""
    val parts = longName.split("-").map { it.trim() }.filter { it.isNotEmpty() }
    val origem = parts.getOrNull(0) ?: "Origem desconhecida"
    val destino = parts.getOrNull(1) ?: "Destino desconhecida"

    val lineColor = try {
        if (!line.routeColor.isNullOrBlank()) {
            Color(android.graphics.Color.parseColor("#${line.routeColor}"))
        } else Color(0xFF16A34A)
    } catch (e: Exception) {
        Color(0xFF16A34A)
    }

    // Determinar o ícone baseado no routeType
    // 1 = Metrô, 2 = Trem, 3 = Ônibus
    val transportIcon = when (line.routeType) {
        1 -> R.drawable.onibus // Metrô (substitua pelo ícone correto se tiver)
        2 -> R.drawable.onibus // Trem (substitua pelo ícone correto se tiver)
        3 -> R.drawable.onibus // Ônibus
        else -> R.drawable.onibus
    }

    // Determinar largura baseada no tipo (Metrô e Trem precisam de mais espaço)
    val boxWidth = if (line.routeType == 1 || line.routeType == 2) 95.dp else 65.dp

    // Fonte menor para metrô e trem
    val fontSize = if (line.routeType == 1 || line.routeType == 2) 10.sp else 11.sp

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                navegacao?.navigate("linha-tracado/${line.routeId}/${line.routeShortName}")
            }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .width(boxWidth)
                    .clip(RoundedCornerShape(6.dp))
                    .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(6.dp))
                    .background(Color.White),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier
                        .padding(vertical = 4.dp, horizontal = 3.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        painter = painterResource(id = transportIcon),
                        contentDescription = "Ícone de transporte",
                        tint = Color.DarkGray,
                        modifier = Modifier.size(13.dp)
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        text = line.routeShortName,
                        fontWeight = FontWeight.Bold,
                        fontSize = fontSize,
                        color = Color.Black,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        softWrap = false
                    )
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(3.dp)
                        .background(lineColor)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(
                modifier = Modifier.weight(1f)
            ) {
                // Agora origem e destino aparecem na mesma linha em cinza
                Text(
                    text = "$origem - $destino",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Gray,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 12.dp, end = 12.dp),
            color = Color(0xFFE0E0E0),
            thickness = 1.dp
        )
    }
}
@Composable
fun Header() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp),
        contentAlignment = Alignment.TopStart
    ) {
        Image(
            painter = painterResource(id = R.drawable.perfilcinza),
            contentDescription = "Usuário",
            modifier = Modifier
                .padding(start = 12.dp, top = 11.dp)
                .size(40.dp)
                .clip(CircleShape)
        )
    }
}

@Composable
fun SearchField(value: String, onValueChange: (String) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp), // Aumentado para não cortar as letras
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(12.dp))
                .background(Color(0xFFF7F7F7)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = value,
                onValueChange = onValueChange,
                placeholder = {
                    Text(
                        text = "Pesquise por uma linha",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                },
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 12.dp, end = 8.dp),
                singleLine = true,
                maxLines = 1,
                textStyle = LocalTextStyle.current.copy(fontSize = 15.sp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = Color(0xFF16A34A)
                )
            )
            Box(
                modifier = Modifier
                    .width(48.dp)
                    .height(52.dp)
                    .clip(RoundedCornerShape(topEnd = 12.dp, bottomEnd = 12.dp))
                    .background(Color(0xFF16A34A)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Buscar",
                    tint = Color.White,
                    modifier = Modifier.size(22.dp)
                )
            }
        }
    }
}
@Composable
fun TransportTabs(
    selectedTabIndex: Int,
    onTabSelected: (Int) -> Unit,
    tabs: List<String>,
    showingFavoritos: Boolean,
    onFavoritosClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
    ) {
        IconButton(
            onClick = onFavoritosClick,
            modifier = Modifier.size(32.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = if (showingFavoritos) "Mostrar todas as linhas" else "Mostrar favoritos",
                tint = if (showingFavoritos) Color(0xFF16A34A) else Color.Gray,
                modifier = Modifier.size(22.dp)
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        TabRow(
            selectedTabIndex = selectedTabIndex,
            containerColor = Color.Transparent,
            contentColor = Color(0xFF26A65B),
            modifier = Modifier.fillMaxWidth(),
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                    height = 2.dp,
                    color = Color(0xFF26A65B)
                )
            },
            divider = {
                Divider(
                    color = Color(0xFFE0E0E0),
                    thickness = 1.dp,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { onTabSelected(index) },
                    text = {
                        Text(
                            text = title,
                            fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 14.sp,
                            color = if (selectedTabIndex == index) Color(0xFF26A65B) else Color.Gray
                        )
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LinesScreenPreview() {
    LinesScreen(navegacao = rememberNavController())
}
