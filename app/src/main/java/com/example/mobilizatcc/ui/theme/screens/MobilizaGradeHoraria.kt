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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.asPaddingValues
import com.example.mobilizatcc.R
import com.example.mobilizatcc.viewmodel.LinhaDetalhesViewModel

@Composable
fun LinhaDetalhesScreen(
    navegacao: NavHostController? = null,
    linhaCodigo: String = "1012"
) {
    val viewModel: LinhaDetalhesViewModel = viewModel()
    val frequencias by viewModel.frequencias.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val greenColor = Color(0xFF3AAA35)

    LaunchedEffect(linhaCodigo) {
        viewModel.carregarFrequencias(linhaCodigo)
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

            // ---------- ÍCONE DE USUÁRIO ----------
            Image(
                painter = painterResource(id = R.drawable.perfilcinza),
                contentDescription = "Usuário",
                modifier = Modifier
                    .padding(start = 35.dp, top = 20.dp)
                    .size(40.dp)
                    .clip(CircleShape)
            )

            // ---------- LINHA SUPERIOR ----------
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.seta),
                    contentDescription = "Voltar",
                    tint = Color.Black,
                    modifier = Modifier
                        .size(20.dp)
                        .clickable { navegacao?.popBackStack() }
                )

                Box(
                    modifier = Modifier
                        .background(Color.White, RoundedCornerShape(8.dp))
                        .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(8.dp))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(id = R.drawable.onibus),
                            contentDescription = "Ônibus",
                            tint = Color.Black,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = linhaCodigo,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            fontSize = 14.sp
                        )
                    }
                }

                Icon(
                    painter = painterResource(id = R.drawable.star),
                    contentDescription = "Favoritar",
                    tint = Color(0xFFFFD600),
                    modifier = Modifier.size(24.dp)
                )
            }

            // ---------- BLOCO VERDE ----------
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(greenColor)
                    .padding(vertical = 16.dp, horizontal = 20.dp)
            ) {
                Column {
                    Text(
                        text = "Ponto de Partida",
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(id = R.drawable.onibus),
                            contentDescription = "Terminal",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Estação KM 21",
                            color = Color.White,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

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
                            columns = GridCells.Adaptive(minSize = 140.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(min = 0.dp, max = 400.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            userScrollEnabled = false
                        ) {
                            items(frequencias) { freq ->
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(Color.White, RoundedCornerShape(12.dp))
                                        .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(12.dp))
                                        .padding(horizontal = 12.dp, vertical = 10.dp)
                                ) {
                                    Text(
                                        text = "${freq.start_time} - ${freq.end_time}",
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
