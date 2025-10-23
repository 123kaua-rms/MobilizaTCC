package com.example.mobilizatcc.ui.theme.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.mobilizatcc.R

@Composable
fun LinhaTracadoScreen(
    navegacao: NavHostController? = null,
    routeId: String = "",
    routeShortName: String = ""
) {
    val greenColor = Color(0xFF3AAA35)
    val orangeColor = Color(0xFFF5A623)
    val blueColor = Color(0xFF2962FF)
    val grayLine = Color(0xFFE0E0E0)

    val paradas = listOf(
        "Terminal Metropolitano Sul / KM 21",
        "Av. Integração 302",
        "Av. SENAI - Professor Vicente Amato",
        "Rua XV de Novembro",
        "Travessa das Palmeiras",
        "Avenida Brasil"
    )

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        Column(modifier = Modifier.fillMaxSize()) {

            // ---------- CABEÇALHO ----------
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(horizontal = 16.dp, vertical = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Ícone perfil
                Image(
                    painter = painterResource(id = R.drawable.perfilcinza),
                    contentDescription = "Usuário",
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                )
                Spacer(modifier = Modifier.height(12.dp))

                // Linha com ícone de voltar e estrela
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Ícone de voltar
                    Icon(
                        painter = painterResource(id = R.drawable.ss),
                        contentDescription = "Voltar",
                        tint = greenColor,
                        modifier = Modifier
                            .size(32.dp)
                            .clickable { navegacao?.navigate("linhas") }
                    )

                    // Número da linha
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

                    // Ícone estrela
                    Icon(
                        painter = painterResource(id = R.drawable.star),
                        contentDescription = "Favoritar",
                        tint = Color(0xFF9E9E9E),
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            // ---------- BLOCO VERDE ----------
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(greenColor)
                    .padding(vertical = 14.dp, horizontal = 20.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.sentido),
                        contentDescription = "Sentido",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Estação KM 21",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Mudar sentido",
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 14.sp,
                    modifier = Modifier.clickable { /* ação de mudar sentido */ }
                )

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = { /* ação do chat */ },
                    colors = ButtonDefaults.buttonColors(containerColor = orangeColor),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.height(42.dp)
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
                        fontSize = 14.sp
                    )
                }
            }

            // ---------- LISTA DE PARADAS ----------
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp)
                    .weight(1f)
            ) {
                items(paradas.size) { index ->
                    val parada = paradas[index]
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(end = 12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(12.dp)
                                    .background(
                                        if (index == 0) greenColor else Color.White,
                                        CircleShape
                                    )
                                    .border(2.dp, greenColor, CircleShape)
                            )
                            if (index != paradas.size - 1) {
                                Box(
                                    modifier = Modifier
                                        .width(2.dp)
                                        .height(32.dp)
                                        .background(greenColor)
                                )
                            }
                        }

                        Column(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                text = parada,
                                fontWeight = if (index == 0) FontWeight.Bold else FontWeight.Normal,
                                color = if (index == 0) Color.Black else Color(0xFF424242),
                                fontSize = 15.sp
                            )

                            if (index == 0) {
                                Spacer(modifier = Modifier.height(6.dp))
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Button(
                                        onClick = { /* ação grade horária */ },
                                        colors = ButtonDefaults.buttonColors(containerColor = blueColor),
                                        shape = RoundedCornerShape(6.dp),
                                        modifier = Modifier.height(30.dp)
                                    ) {
                                        Text(
                                            text = "Grade horária",
                                            color = Color.White,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Box(
                                        modifier = Modifier
                                            .background(Color(0xFFF5F5F5), RoundedCornerShape(6.dp))
                                            .padding(horizontal = 8.dp, vertical = 6.dp)
                                    ) {
                                        Text(
                                            text = "🕓 20 min",
                                            color = Color.Black,
                                            fontSize = 12.sp
                                        )
                                    }
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            // ---------- MENU INFERIOR ----------
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                BottomMenuItem(R.drawable.home, "Início", false)
                BottomMenuItem(R.drawable.linhas, "Linhas", true)
                BottomMenuItem(R.drawable.feedback, "Feedback", false)
                BottomMenuItem(R.drawable.telaperfil, "Perfil", false)
            }
        }
    }
}

@Composable
fun BottomMenuItem(iconId: Int, label: String, selected: Boolean) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            painter = painterResource(id = iconId),
            contentDescription = label,
            tint = if (selected) Color(0xFF3AAA35) else Color(0xFF9E9E9E),
            modifier = Modifier.size(24.dp)
        )
        Text(
            text = label,
            fontSize = 12.sp,
            color = if (selected) Color(0xFF3AAA35) else Color(0xFF9E9E9E)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LinhaTracadoScreenPreview() {
    LinhaTracadoScreen()
}
