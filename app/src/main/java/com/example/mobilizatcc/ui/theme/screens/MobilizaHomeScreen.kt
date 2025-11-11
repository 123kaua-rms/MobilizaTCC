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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.mobilizatcc.R
import com.example.mobilizatcc.model.BusLineResponse
import com.example.mobilizatcc.utils.UserSessionManager
import com.example.mobilizatcc.viewmodel.LinesViewModel
import kotlin.random.Random

@Composable
fun MobilizaHomeScreen(
    navegacao: NavHostController?,
    viewModel: LinesViewModel = viewModel()
) {
    val context = LocalContext.current
    val userSessionManager = remember { UserSessionManager.getInstance(context) }
    
    var partida by remember { mutableStateOf("") }
    var destino by remember { mutableStateOf("") }

    val lines by viewModel.lines.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val greenColor = Color(0xFF16A34A)
    
    // Obter nome real do usuário
    val userName = userSessionManager.getUserName()?.takeIf { it.isNotBlank() } ?: "Usuário"

    LaunchedEffect(Unit) {
        viewModel.fetchLines()
    }

    // Filtrar apenas linhas de ônibus (routeType == 3) e pegar 8 aleatórias
    val linhasRecentes = remember(lines) {
        lines.filter { it.routeType == 3 }
            .shuffled(Random(System.currentTimeMillis()))
            .take(8)
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding()
        ) {

            // 🔹 Cabeçalho verde superior
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Color(0xFF16A34A),
                        shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)
                    )
                    .padding(16.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {

                    // Linha superior com ícone de perfil e botão de emergência
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.perfilcinza),
                            contentDescription = "Usuário",
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .clickable {
                                    navegacao?.navigate("perfil")
                                }
                        )

                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Para onde vamos?",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // 🔹 Card de partida e destino
                    Card(
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                        modifier = Modifier.fillMaxWidth(0.9f)
                    ) {
                        Box(modifier = Modifier.padding(16.dp)) {

                            Column {
                                // Partida
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(12.dp)
                                            .clip(CircleShape)
                                            .background(Color(0xFF673AB7))
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = "Partida",
                                            fontSize = 12.sp,
                                            color = Color.Gray
                                        )
                                        TextField(
                                            value = partida,
                                            onValueChange = { partida = it },
                                            placeholder = { Text("Digite o local de partida", fontSize = 14.sp, color = Color.Gray) },
                                            singleLine = true,
                                            colors = TextFieldDefaults.colors(
                                                focusedContainerColor = Color.Transparent,
                                                unfocusedContainerColor = Color.Transparent,
                                                focusedIndicatorColor = Color.Transparent,
                                                unfocusedIndicatorColor = Color.Transparent,
                                                cursorColor = Color(0xFF16A34A)
                                            ),
                                            textStyle = LocalTextStyle.current.copy(fontSize = 14.sp, color = Color.Black),
                                            modifier = Modifier.fillMaxWidth()
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(12.dp))
                                HorizontalDivider(color = Color(0xFFE0E0E0), thickness = 1.dp)
                                Spacer(modifier = Modifier.height(5.dp))

                                // Destino
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(12.dp)
                                            .clip(CircleShape)
                                            .background(Color(0xFFFF9800))
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = "Destino",
                                            fontSize = 12.sp,
                                            color = Color.Gray
                                        )
                                        TextField(
                                            value = destino,
                                            onValueChange = { destino = it },
                                            placeholder = { Text("Digite o destino", fontSize = 14.sp, color = Color.Gray) },
                                            singleLine = true,
                                            colors = TextFieldDefaults.colors(
                                                focusedContainerColor = Color.Transparent,
                                                unfocusedContainerColor = Color.Transparent,
                                                focusedIndicatorColor = Color.Transparent,
                                                unfocusedIndicatorColor = Color.Transparent,
                                                cursorColor = Color(0xFF16A34A)
                                            ),
                                            textStyle = LocalTextStyle.current.copy(fontSize = 14.sp, color = Color.Black),
                                            modifier = Modifier.fillMaxWidth()
                                        )
                                    }

                                    IconButton(onClick = { 
                                        if (partida.isNotBlank() && destino.isNotBlank()) {
                                            navegacao?.navigate("linhas")
                                        }
                                    }) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.lupa),
                                            contentDescription = "Pesquisar",
                                            tint = Color(0xFF16A34A),
                                            modifier = Modifier.size(24.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            // 🔹 Mensagem de boas-vindas
            Text(
                text = "Bem vindo ao Mobiliza, $userName!",
                fontSize = 23.sp,
                fontWeight = FontWeight.Bold,
                style = androidx.compose.ui.text.TextStyle(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color(0xFF16A34A),
                            Color(0xFF2E7D32)
                        )
                    )
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(30.dp))

            // 🔹 Linhas recentes
            Text(
                text = "Linhas recentes",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(start = 16.dp)
            )

            if (isLoading) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = greenColor)
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .weight(1f)
                ) {
                    items(linhasRecentes) { linha ->
                        BusLineItemHome(linha, navegacao)
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }

            // 🔹 Menu inferior (importado de outro arquivo)
            BottomNavigationBar(navegacao = navegacao, selectedRoute = "home")
        }
    }
}

@Composable
fun BusLineItemHome(line: BusLineResponse, navegacao: NavHostController?) {
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

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                navegacao?.navigate("linha-tracado/${line.routeId}/${line.routeShortName}")
            }
            .padding(vertical = 8.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
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
                            color = lineColor,
                            topLeft = androidx.compose.ui.geometry.Offset(0f, size.height - 4.dp.toPx()),
                            size = androidx.compose.ui.geometry.Size(size.width, 4.dp.toPx())
                        )
                    }
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.onibus),
                        contentDescription = "Linha",
                        tint = Color(0xFF363D38),
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = line.routeShortName,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = "$origem - $destino",
                fontSize = 14.sp,
                color = Color.DarkGray,
                modifier = Modifier.weight(1f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Spacer(modifier = Modifier.height(8.dp))
        HorizontalDivider(color = Color(0xFFE0E0E0), thickness = 1.dp)
    }
}
@Preview(showBackground = true)
@Composable
fun MobilizaHomeScreenPreview() {
    MobilizaHomeScreen(null)
}
