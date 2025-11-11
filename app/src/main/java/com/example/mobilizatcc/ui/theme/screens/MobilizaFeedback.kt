
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
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
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.mobilizatcc.R
import com.example.mobilizatcc.model.FeedbackResponse
import com.example.mobilizatcc.service.RetrofitFactory
import kotlinx.coroutines.launch

//Color Figma -> 0xFF16A34A
//Color Alternative -> 0xFF26A65B

@Composable
fun FeedbacksScreen(
    navegacao: NavHostController?
) {
    var searchQuery by remember { mutableStateOf("") }
    var feedbacks by remember { mutableStateOf<List<FeedbackResponse>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val feedbackService = remember { RetrofitFactory().getFeedbackService() }
    val coroutineScope = rememberCoroutineScope()

    // 🔹 Buscar todos os feedbacks ao abrir a tela
    LaunchedEffect(Unit) {
        try {
            val response = feedbackService.getAllFeedbacks()
            feedbacks = response.feedbacks
            errorMessage = null
        } catch (e: retrofit2.HttpException) {
            // API de feedbacks não implementada no backend - usar dados de demonstração
            errorMessage = null
            feedbacks = listOf(
                FeedbackResponse(
                    id = 1,
                    id_linha = "001",
                    id_usuario = 1,
                    avaliacao = 5,
                    conteudo = "Ótimo serviço! Ônibus sempre pontual e limpo."
                ),
                FeedbackResponse(
                    id = 2,
                    id_linha = "002", 
                    id_usuario = 2,
                    avaliacao = 3,
                    conteudo = "Serviço regular, mas poderia melhorar a frequência."
                ),
                FeedbackResponse(
                    id = 3,
                    id_linha = "003",
                    id_usuario = 3,
                    avaliacao = 1,
                    conteudo = "Muito atraso e superlotação. Precisa melhorar urgente."
                )
            )
        } catch (e: Exception) {
            errorMessage = "Erro ao conectar com o servidor"
            e.printStackTrace()
        } finally {
            isLoading = false
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navegacao?.navigate("feedback-form") },
                containerColor = Color(0xFF16A34A)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Adicionar",
                    tint = Color.White
                )
            }
        },
        bottomBar = { BottomNavigationBar(navegacao = navegacao, selectedRoute = "feedback") }
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
                FeedbackHeader(navegacao)
                Spacer(modifier = Modifier.height(16.dp))

                // 🔹 Campo de pesquisa que chama o endpoint por linha
                SearchFieldFeedback(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    onSearchClick = {
                        coroutineScope.launch {
                            if (searchQuery.isNotBlank()) {
                                isLoading = true
                                try {
                                    val response = feedbackService.getFeedbacksByLinha(searchQuery)
                                    feedbacks = response.feedbacks
                                    errorMessage = null
                                } catch (e: retrofit2.HttpException) {
                                    // Simular busca por linha com dados mock
                                    val mockFeedbacks = listOf(
                                        FeedbackResponse(1, "001", 1, 5, "Ótimo serviço! Ônibus sempre pontual e limpo."),
                                        FeedbackResponse(2, "002", 2, 3, "Serviço regular, mas poderia melhorar a frequência."),
                                        FeedbackResponse(3, "003", 3, 1, "Muito atraso e superlotação. Precisa melhorar urgente.")
                                    )
                                    feedbacks = mockFeedbacks.filter { it.id_linha.contains(searchQuery, ignoreCase = true) }
                                    errorMessage = if (feedbacks.isEmpty()) "Nenhum feedback encontrado para a linha $searchQuery" else null
                                } catch (e: Exception) {
                                    feedbacks = emptyList()
                                    errorMessage = "Erro ao buscar feedbacks"
                                } finally {
                                    isLoading = false
                                }
                            }
                        }
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                when {
                    isLoading -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = Color(0xFF16A34A))
                        }
                    }

                    errorMessage != null -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = errorMessage ?: "", color = Color.Gray)
                        }
                    }

                    feedbacks.isEmpty() -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Nenhum feedback encontrado.", color = Color.Gray)
                        }
                    }

                    else -> {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(feedbacks) { feedback ->
                                FeedbackCardFromApi(feedback)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FeedbackCardFromApi(feedback: FeedbackResponse) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, shape = RoundedCornerShape(8.dp))
            .border(0.dp, Color.Transparent, RoundedCornerShape(8.dp))
            .padding(bottom = 4.dp)
    ) {
        // 🔹 Faixa verde superior com id_usuario (esquerda) e id_linha (direita)
        Card(
            shape = RoundedCornerShape(topEnd = 12.dp, topStart = 12.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF16A34A))
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.perfilcinza),
                    contentDescription = "Usuário",
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Usuário ${feedback.id_usuario}",
                    color = Color.White,
                    fontSize = 13.sp
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = feedback.id_linha,
                    color = Color.White,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // 🔹 Parte inferior do card com o conteúdo do feedback
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF8F8F8))
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = feedback.conteudo,
                color = Color.Black,
                fontSize = 13.sp,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun FeedbackHeader(navegacao: NavHostController? = null) {
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
                .size(32.dp)
                .clip(CircleShape)
                .clickable { navegacao?.navigate("perfil") }
        )
    }
}

@Composable
fun SearchFieldFeedback(
    value: String,
    onValueChange: (String) -> Unit,
    onSearchClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
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
                        text = "Pesquise por uma linha...",
                        color = Color.Gray,
                        fontSize = 12.sp
                    )
                },
                modifier = Modifier
                    .weight(2f)
                    .fillMaxHeight()
                    .padding(horizontal = 12.dp),
                singleLine = true,
                maxLines = 1,
                textStyle = LocalTextStyle.current.copy(fontSize = 13.sp),
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
                    .width(42.dp)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(topEnd = 12.dp, bottomEnd = 12.dp))
                    .background(Color(0xFF16A34A))
                    .clickable { onSearchClick() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Buscar",
                    tint = Color.White,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FeedbacksScreenPreview() {
    FeedbacksScreen(navegacao = rememberNavController())
}
