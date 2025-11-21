package com.example.mobilizatcc.ui.theme.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.mobilizatcc.R
import com.example.mobilizatcc.ui.theme.components.RouteBadge
import com.example.mobilizatcc.utils.UserSessionManager
import com.example.mobilizatcc.viewmodel.LinhaDetalhesViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest

@Composable
fun LinhaDetalhesScreen(
    navegacao: NavHostController? = null,
    routeId: String = "",
    linhaCodigo: String = "1012"
) {
    val context = LocalContext.current
    val userSessionManager = remember { UserSessionManager.getInstance(context) }
    
    val viewModel: LinhaDetalhesViewModel = viewModel()
    val frequencias by viewModel.frequencias.collectAsState()
    val paradas by viewModel.paradas.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val isFavorito by viewModel.isFavorito.collectAsState()
    val favoritoLoading by viewModel.favoritoLoading.collectAsState()
    val userPhotoUrl = userSessionManager.getUserPhoto()
    val greenColor = Color(0xFF209C4E)
    
    // Obter ID do usuário logado
    val usuarioId = userSessionManager.getUserId()

    // 🔹 Pegar a última parada (estação final)
    val estacaoFinal = remember(paradas) {
        paradas.lastOrNull()?.stopName ?: "Estação final"
    }

    LaunchedEffect(linhaCodigo) {
        viewModel.carregarFrequencias(linhaCodigo)
    }

    LaunchedEffect(routeId) {
        if (routeId.isNotEmpty()) {
            viewModel.carregarParadas(routeId)
            viewModel.verificarFavorito(usuarioId, routeId)
        }
    }

    Scaffold(
        bottomBar = {
            Divider(color = Color(0xFFE0E0E0), thickness = 1.dp)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                BottomNavItem(icon = R.drawable.home, label = "Início", selected = false)
                BottomNavItem(icon = R.drawable.linhas, label = "Linhas", selected = true)
                BottomNavItem(icon = R.drawable.feedback, label = "Feedback", selected = false)
                BottomNavItem(icon = R.drawable.telaperfil, label = "Perfil", selected = false)
            }
        }
    ) { innerPadding ->

        // Adiciona o espaçamento superior da status bar
        val topPadding = 4.dp
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(innerPadding)
                .padding(top = topPadding)
                .verticalScroll(rememberScrollState())
        ) {

            // ---------- LINHA SUPERIOR ----------
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 18.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Spacer(modifier = Modifier.width(12.dp))

                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clickable { navegacao?.popBackStack() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.seta),
                            contentDescription = "Voltar",
                            tint = Color.Black,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Box(
                        modifier = Modifier.weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        RouteBadge(routeCode = linhaCodigo, maxWidth = 110.dp)
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    IconButton(
                        onClick = {
                            if (!favoritoLoading) {
                                viewModel.toggleFavorito(usuarioId, routeId)
                            }
                        },
                        modifier = Modifier.size(42.dp)
                    ) {
                        if (favoritoLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(22.dp),
                                color = greenColor,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Icon(
                                painter = painterResource(id = R.drawable.star_filled),
                                contentDescription = if (isFavorito) "Remover favorito" else "Adicionar favorito",
                                tint = if (isFavorito) Color(0xFFFACC15) else Color(0xFFD4D4D4),
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // ---------- BLOCO VERDE ----------
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(greenColor)
                    .padding(horizontal = 24.dp, vertical = 22.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.sentido),
                        contentDescription = "Sentido",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = estacaoFinal,
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ---------- GRADE HORÁRIA ----------
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Text(
                    text = "Grade horária",
                    color = greenColor,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                when {
                    isLoading -> {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = greenColor)
                        }
                    }
                    frequencias.isEmpty() -> {
                        Text(
                            text = "Nenhum horário disponível.",
                            color = Color.Gray,
                            fontSize = 14.sp
                        )
                    }
                    else -> {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(3),
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(min = 0.dp, max = 600.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            userScrollEnabled = false
                        ) {
                            items(frequencias) { freq ->
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(Color.White, RoundedCornerShape(8.dp))
                                        .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(8.dp))
                                        .padding(horizontal = 16.dp, vertical = 12.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = freq.start_time.substring(0, 5),
                                        color = Color.Black,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun BottomNavItem(icon: Int, label: String, selected: Boolean) {
    val color = if (selected) Color(0xFF3AAA35) else Color(0xFF9E9E9E)
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = label,
            tint = color,
            modifier = Modifier.size(22.dp)
        )
        Text(
            text = label,
            color = color,
            fontSize = 12.sp,
            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LinhaDetalhesPreview() {
    LinhaDetalhesScreen()
}
