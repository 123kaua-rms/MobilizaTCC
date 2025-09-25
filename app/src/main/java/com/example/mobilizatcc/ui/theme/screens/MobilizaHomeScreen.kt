
package com.example.mobilizatcc.ui.theme.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mobilizatcc.R

@Composable
fun MobilizaHomeScreen(
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
                    .background(greenColor)
                    .padding(16.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {

                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Para onde vamos?",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Campos de busca
                    // Campos de busca
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White // fundo branco
                        ),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 4.dp // leve sombra
                        ),
                        modifier = Modifier.fillMaxWidth(0.9f)
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(16.dp)
                        ) {
                            OutlinedTextField(
                                value = partida,
                                onValueChange = { partida = it },
                                label = { Text("Partida") },
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            OutlinedTextField(
                                value = destino,
                                onValueChange = { destino = it },
                                label = { Text("Destino") },
                                placeholder = { Text("Selecione um destino") },
                                trailingIcon = {
                                    IconButton(
                                        onClick = {  },
                                        modifier = Modifier.size(32.dp)
                                    ) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.lupa),
                                            contentDescription = "Pesquisar",
                                            tint = Color.Gray
                                        )
                                    }
                                },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }

                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Bem vindo ao Mobiliza, $username!",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = greenColor,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

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
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White // cor de fundo branca
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 5.dp // leve sombra
        ),
        modifier = Modifier.fillMaxWidth(),
        onClick = { onClick(line.codigo) }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(12.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.onibus), // Ícone de ônibus
                contentDescription = "Linha",
                tint = Color(0xFF3AAA35),
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = line.codigo,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = line.descricao,
                fontSize = 14.sp,
                color = Color.DarkGray
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun MobilizaHomeScreenPreview() {
    MobilizaHomeScreen()
}

