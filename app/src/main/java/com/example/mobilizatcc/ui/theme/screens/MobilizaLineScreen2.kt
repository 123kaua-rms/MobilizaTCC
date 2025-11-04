package com.example.mobilizatcc.ui.theme.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.mobilizatcc.R

@Composable
fun LinhaTracadoScreen(
    navegacao: NavHostController,
    routeId: String = "",
    routeShortName: String = "",
    viewModel: LinhaTracadoViewModel = viewModel()
) {
    val greenColor = Color(0xFF16A34A)
    val orangeColor = Color(0xFFF5A623)
    val blueColor = Color(0xFF1976D2)
    val grayLine = Color(0xFFE0E0E0)

    val paradas by viewModel.paradas.collectAsState()
    val sentido by viewModel.sentido.collectAsState()

    LaunchedEffect(routeId) {
        if (routeId.isNotEmpty()) {
            viewModel.carregarParadas(routeId, "ida")
        }
    }

    Scaffold(
        bottomBar = { BottomNavigationBar(navegacao = navegacao, selectedRoute = "linhas") }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(paddingValues)
        ) {

            // 🔹 Barra superior com imagem do usuário
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 🧍 Ícone de usuário personalizado
                Image(
                    painter = painterResource(id = R.drawable.perfilcinza), // 🔸 substitua pelo nome da sua imagem
                    contentDescription = "Usuário",
                    modifier = Modifier
                        .size(34.dp)
                        .clip(CircleShape)
                        .clickable { navegacao.navigate("perfil") }
                )
                Spacer(modifier = Modifier.weight(1f))
            }

            // 🔹 Linha com voltar / número do ônibus / favorito (acima do verde)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 🔙 Seta de voltar (agora cinza)
                Icon(
                    painter = painterResource(id = R.drawable.seta),
                    contentDescription = "Voltar",
                    tint = Color.Gray, // 🔸 alterado para cinza
                    modifier = Modifier
                        .size(26.dp)
                        .clickable { navegacao.navigateUp() }
                )

                Box(
                    modifier = Modifier
                        .background(Color.White, RoundedCornerShape(8.dp))
                        .border(1.dp, grayLine, RoundedCornerShape(8.dp))
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
                            text = routeShortName,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            fontSize = 14.sp
                        )
                    }
                }

                // ⭐ Ícone de favorito personalizado
                Image(
                    painter = painterResource(id = R.drawable.star), // 🔸 substitua pelo nome da sua imagem
                    contentDescription = "Favoritar",
                    modifier = Modifier
                        .size(30.dp)
                        .clickable { /* ação de favoritar se desejar */ }
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 🔹 Área verde com apenas o sentido
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = greenColor,
                    )
                    .padding(horizontal = 16.dp, vertical = 16.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(id = R.drawable.sentido),
                            contentDescription = "Sentido",
                            tint = Color.White,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Sentido ${sentido.uppercase()}",
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Mudar sentido (${if (sentido == "ida") "volta" else "ida"})",
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 14.sp,
                        modifier = Modifier.clickable { viewModel.mudarSentido() }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 🔹 Botões abaixo da área verde
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = { navegacao.navigate("chat") },
                    colors = ButtonDefaults.buttonColors(containerColor = orangeColor),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(44.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.chat),
                        contentDescription = "Chat",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Fale com outros passageiros",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        maxLines = 1
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    onClick = { navegacao.navigate("gradehoraria") },
                    colors = ButtonDefaults.buttonColors(containerColor = blueColor),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .weight(0.7f)
                        .height(44.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.relogio),
                        contentDescription = "Grade horária",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Grade horária",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 🔹 Lista de paradas com bolinhas e linha
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp)
            ) {
                itemsIndexed(paradas) { index, parada ->
                    Row(
                        verticalAlignment = Alignment.Top,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.width(24.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(12.dp)
                                    .background(
                                        color = if (index == 0) greenColor else Color.LightGray,
                                        shape = CircleShape
                                    )
                            )
                            if (index < paradas.lastIndex) {
                                Box(
                                    modifier = Modifier
                                        .width(2.dp)
                                        .height(40.dp)
                                        .background(
                                            color = if (index == 0) greenColor else Color.LightGray
                                        )
                                )
                            }
                        }

                        Column(modifier = Modifier.padding(start = 8.dp)) {
                            Text(
                                text = parada.stopName ?: "Parada sem nome",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color.Black
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LinhaTracadoScreenPreview() {
}
