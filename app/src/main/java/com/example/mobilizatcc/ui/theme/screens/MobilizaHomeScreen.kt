package com.example.mobilizatcc.ui.theme.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.mobilizatcc.R

@Composable
fun MobilizaHomeScreen(
    navegacao: NavHostController?,
    username: String = "username",
    onLineClick: (String) -> Unit = {}
) {
    val greenColor = Color(0xFF3AAA35)

    var partida by remember { mutableStateOf("Sua localização") }
    var destino by remember { mutableStateOf("") }

    val linhasRecentes = listOf(
        LineInfo("106", "Terminal Central / Vila Nova - Estação Km 21"),
        LineInfo("21", "Jardim Paulista / Centro - Estação Km 21"),
        LineInfo("JA136", "Rodoviária / Bairro Industrial - Estação Centro")
    )

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Header verde
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        greenColor,
                        shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)
                    )
                    .padding(16.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Linha superior com foto e emergência
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Campo para imagem de usuário
                        Image(
                            painter = painterResource(id = R.drawable.perfil), // substitua pela imagem desejada
                            contentDescription = "Usuário",
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                        )

                        // Botão emergência
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(Color.Yellow)
                                .clickable { /* ação emergência */ },
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.emergencia),
                                contentDescription = "Emergência",
                                modifier = Modifier.size(29.dp)
                            )

                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Texto central
                    Text(
                        text = "Para onde vamos?",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Card de busca
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                        modifier = Modifier.fillMaxWidth(0.9f)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {

                            Spacer(modifier = Modifier.height(12.dp))

                            // Partida
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(12.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFF673AB7)) // Roxo
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = partida,
                                    fontSize = 14.sp,
                                    color = Color.Black
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Divider(color = Color(0xFFE0E0E0), thickness = 1.dp)

                            // Destino
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(12.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFFFF9800)) // Laranja
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = if (destino.isEmpty()) "Selecione um destino" else destino,
                                    fontSize = 14.sp,
                                    color = if (destino.isEmpty()) Color.Gray else Color.Black
                                )

                                Spacer(modifier = Modifier.weight(1f))

                                IconButton(onClick = { /* ação pesquisar */ }) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.lupa),
                                        contentDescription = "Pesquisar",
                                        tint = greenColor,
                                        modifier = Modifier.size(28.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            Text(
                text = "Bem vindo ao Mobiliza, $username!",
                fontSize = 23.sp,
                fontWeight = FontWeight.Bold,
                style = androidx.compose.ui.text.TextStyle(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color(0xFF3AAA35),
                            Color(0xFF2E7D32)
                        )
                    )
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(30.dp))

            // Linhas recentes
            Text(
                text = "Linhas recentes",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(start = 16.dp)
            )

            LazyColumn(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                items(linhasRecentes) { linha ->
                    LineItem(linha, onLineClick)
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}

data class LineInfo(val codigo: String, val descricao: String)

@Composable
fun LineItem(line: LineInfo, onClick: (String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(line.codigo) }
            .padding(vertical = 8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Caixa com ícone + código
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .border(
                        width = 1.dp,
                        color = Color(0xFFDDDDDD),
                        shape = RoundedCornerShape(6.dp)
                    )
                    .background(Color.White)
                    .drawBehind {
                        drawRect(
                            color = if (line.codigo.startsWith("JA")) Color(0xFFFFA500) else Color(0xFFFFD700),
                            topLeft = androidx.compose.ui.geometry.Offset(0f, size.height - 4.dp.toPx()),
                            size = androidx.compose.ui.geometry.Size(size.width, 4.dp.toPx())
                        )
                    }
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.onibus),
                        contentDescription = "Linha",
                        tint = Color(0xFF363D38),
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = line.codigo,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = line.descricao,
                fontSize = 14.sp,
                color = Color.DarkGray,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Divider(color = Color(0xFFE0E0E0), thickness = 1.dp)
    }
}

@Preview(showBackground = true)
@Composable
fun MobilizaHomeScreenPreview() {
    MobilizaHomeScreen(null)
}
