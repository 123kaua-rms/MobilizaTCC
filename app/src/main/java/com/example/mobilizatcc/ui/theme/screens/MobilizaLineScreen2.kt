package com.example.mobilizatcc.ui.theme.screens

import android.net.Uri
import android.util.Log
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.mobilizatcc.R
import com.example.mobilizatcc.ui.theme.components.RouteBadge
import com.example.mobilizatcc.utils.UserSessionManager

@Composable
fun LinhaTracadoScreen(
    navegacao: NavHostController,
    routeId: String = "",
    routeShortName: String = "",
    viewModel: LinhaTracadoViewModel = viewModel()
) {
    val context = LocalContext.current
    val userSessionManager = remember { UserSessionManager.getInstance(context) }
    
    val greenColor = Color(0xFF209C4E)
    val orangeColor = Color(0xFFF5A623)
    val blueColor = Color(0xFF1976D2)
    val grayLine = Color(0xFFDDDDDD)

    val paradas by viewModel.paradas.collectAsState()
    val estimativasPorStopId by viewModel.estimativasPorStopId.collectAsState()
    val isFavorito by viewModel.isFavorito.collectAsState()
    val favoritoLoading by viewModel.favoritoLoading.collectAsState()

    var paradaSelecionada by remember { mutableStateOf<String?>(null) }
    
    // Obter ID do usuário logado
    val usuarioId = userSessionManager.getUserId()

    // 🔹 Pegar a última parada (estação final)
    val estacaoFinal = remember(paradas) {
        paradas.lastOrNull()?.stopName ?: "Estação final"
    }

    LaunchedEffect(routeId) {
        if (routeId.isNotEmpty()) {
            Log.d("LineScreen2", "Carregando dados para linha $routeId, usuário $usuarioId")
            viewModel.carregarParadas(routeId, "ida")
            viewModel.carregarEstimativas(routeId)
            if (usuarioId != -1) {
                viewModel.verificarFavorito(usuarioId, routeId)
            } else {
                Log.w("LineScreen2", "Usuário não logado, não verificando favoritos")
            }
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

            // 🔹 Barra superior com imagem do usuário / seta / badge / estrela
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF7F7F7))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 18.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Image(
                                painter = painterResource(id = R.drawable.perfilcinza),
                                contentDescription = "Usuário",
                                modifier = Modifier
                                    .size(34.dp)
                                    .clip(CircleShape)
                                    .clickable { navegacao?.navigate("perfil") }
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
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
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Box(
                            modifier = Modifier
                                .weight(1f),
                            contentAlignment = Alignment.Center
                        ) {
                            RouteBadge(routeCode = routeShortName, maxWidth = 100.dp)
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        IconButton(
                            onClick = {
                                if (!favoritoLoading && usuarioId != -1) {
                                    Log.d("LineScreen2", "Clicou na estrela: usuário $usuarioId, linha $routeId, isFavorito: $isFavorito")
                                    viewModel.toggleFavorito(usuarioId, routeId)
                                } else if (usuarioId == -1) {
                                    Log.w("LineScreen2", "Usuário não logado, não é possível favoritar")
                                }
                            },
                            modifier = Modifier.size(40.dp)
                        ) {
                            if (favoritoLoading) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(18.dp),
                                    color = greenColor,
                                    strokeWidth = 2.dp
                                )
                            } else {
                                val starColor by animateColorAsState(
                                    targetValue = if (isFavorito) Color(0xFFFACC15) else Color(0xFFD4D4D4),
                                    animationSpec = tween(durationMillis = 300),
                                    label = "star_color"
                                )

                                Icon(
                                    painter = painterResource(id = R.drawable.star_filled),
                                    contentDescription = if (isFavorito) "Remover favorito" else "Adicionar favorito",
                                    tint = starColor,
                                    modifier = Modifier.width(30.dp)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 🔹 Área verde com o sentido
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = greenColor)
                    .padding(horizontal = 24.dp, vertical = 22.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.Start
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

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Mudar sentido",
                        color = Color(0xFFC5CAD3),
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        textDecoration = TextDecoration.Underline,
                        modifier = Modifier
                            .padding(start = 16.dp)
                            .clickable { viewModel.mudarSentido() }
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 🔹 Botões abaixo da área verde
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = {
                        val encodedShortName = Uri.encode(routeShortName)
                        val encodedDescription = Uri.encode(estacaoFinal)
                        navegacao.navigate("chat/$routeId/$encodedShortName/$encodedDescription")
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = orangeColor),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(44.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.chat),
                            contentDescription = "Chat",
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Fale com outros passageiros",
                            color = Color.White,
                            fontWeight = FontWeight.Medium,
                            fontSize = 11.sp,
                            maxLines = 1
                        )
                    }
                }

                Button(
                    onClick = { navegacao.navigate("gradehoraria/$routeId/$routeShortName") },
                    colors = ButtonDefaults.buttonColors(containerColor = blueColor),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
                    modifier = Modifier
                        .weight(0.6f)
                        .height(44.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.horario),
                            contentDescription = "Grade horária",
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Grade horária",
                            color = Color.White,
                            fontWeight = FontWeight.Medium,
                            fontSize = 11.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 🔹 Lista de paradas
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 28.dp)
            ) {
                itemsIndexed(paradas) { index, parada ->
                    val stopId = parada.stopId ?: ""
                    val isSelecionada = paradaSelecionada == stopId

                    Row(
                        verticalAlignment = Alignment.Top,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                paradaSelecionada = if (isSelecionada) null else stopId
                            }
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.width(24.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(12.dp)
                                    .background(
                                        color = if (isSelecionada) greenColor else grayLine,
                                        shape = CircleShape
                                    )
                            )
                            if (index < paradas.lastIndex) {
                                Box(
                                    modifier = Modifier
                                        .width(2.dp)
                                        .height(if (isSelecionada) 80.dp else 58.dp)
                                        .background(
                                            color = if (isSelecionada) greenColor else grayLine
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

                            // 🔹 Mostrar estimativa se esta parada estiver selecionada
                            if (isSelecionada && estimativasPorStopId.containsKey(stopId)) {
                                Spacer(modifier = Modifier.height(6.dp))
                                Box(
                                    modifier = Modifier
                                        .background(
                                            color = Color(0xFFE8E8E8),
                                            shape = RoundedCornerShape(6.dp)
                                        )
                                        .padding(horizontal = 10.dp, vertical = 6.dp)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.Center
                                    ) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.horario),
                                            contentDescription = "Tempo estimado",
                                            tint = Color.DarkGray,
                                            modifier = Modifier.size(14.dp)
                                        )
                                        Spacer(modifier = Modifier.width(5.dp))
                                        Text(
                                            text = estimativasPorStopId[stopId] ?: "",
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = Color.DarkGray
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LinhaTracadoScreenPreview() {}
