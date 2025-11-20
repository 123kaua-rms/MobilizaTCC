package com.example.mobilizatcc.ui.theme.screens

import com.example.mobilizatcc.ui.theme.components.RouteBadge
import android.graphics.Color as AndroidColor
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SentimentNeutral
import androidx.compose.material.icons.filled.SentimentSatisfiedAlt
import androidx.compose.material.icons.filled.SentimentVeryDissatisfied
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.mobilizatcc.R
import com.example.mobilizatcc.model.FeedbackResponse
import com.example.mobilizatcc.service.RetrofitFactory
import coil.compose.AsyncImage
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

    suspend fun requestFeedbacks(query: String? = null) {
        val normalizedQuery = query?.trim()?.takeIf { it.isNotEmpty() }
        isLoading = true
        try {
            val response = if (normalizedQuery == null) {
                feedbackService.getAllFeedbacks()
            } else {
                feedbackService.getFeedbacksByLinha(normalizedQuery)
            }

            feedbacks = response.feedbacks
            errorMessage = when {
                response.feedbacks.isEmpty() && normalizedQuery != null ->
                    "Nenhum feedback encontrado para a linha $normalizedQuery."
                response.feedbacks.isEmpty() ->
                    "Nenhum feedback disponível no momento."
                else -> null
            }
        } catch (e: Exception) {
            feedbacks = emptyList()
            errorMessage = "Não foi possível carregar os feedbacks. Tente novamente."
            Log.e("FeedbacksScreen", "Erro ao buscar feedbacks", e)
        } finally {
            isLoading = false
        }
    }

    // 🔹 Buscar todos os feedbacks ao abrir a tela
    LaunchedEffect(Unit) {
        requestFeedbacks()
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
                            requestFeedbacks(searchQuery)
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
    val username = feedback.nome_usuario?.takeIf { it.isNotBlank() }
        ?: feedback.username?.takeIf { it.isNotBlank() }
        ?: "Usuário ${feedback.id_usuario}"

    val ratingUi = when (feedback.avaliacao) {
        1 -> FeedbackRatingUi(
            label = "Bom",
            backgroundColor = Color(0xFFDCFCE7),
            contentColor = Color(0xFF16A34A),
            icon = Icons.Filled.SentimentSatisfiedAlt
        )

        2 -> FeedbackRatingUi(
            label = "Mediano",
            backgroundColor = Color(0xFFFEF9C3),
            contentColor = Color(0xFFCA8A04),
            icon = Icons.Filled.SentimentNeutral
        )

        3 -> FeedbackRatingUi(
            label = "Ruim",
            backgroundColor = Color(0xFFFEE2E2),
            contentColor = Color(0xFFDC2626),
            icon = Icons.Filled.SentimentVeryDissatisfied
        )

        else -> FeedbackRatingUi(
            label = "--",
            backgroundColor = Color(0xFFE2E8F0),
            contentColor = Color(0xFF94A3B8),
            icon = Icons.Filled.SentimentNeutral
        )
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Column(
            modifier = Modifier
                .background(Color.White, RoundedCornerShape(16.dp))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF16A34A))
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val avatarModel = feedback.foto_usuario?.takeIf { it.isNotBlank() }
                AsyncImage(
                    model = avatarModel ?: R.drawable.perfilcinza,
                    contentDescription = "Foto de $username",
                    placeholder = painterResource(id = R.drawable.perfilcinza),
                    error = painterResource(id = R.drawable.perfilcinza),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                )

                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = username,
                        color = Color.White,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                RouteBadge(
                    routeCode = feedback.route_short_name ?: feedback.id_linha,
                    routeColorHex = feedback.route_color
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF7F7F7))
                    .padding(horizontal = 16.dp, vertical = 18.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = feedback.conteudo,
                    color = Color(0xFF111827),
                    fontSize = 14.sp,
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(16.dp))

                RatingBadge(ratingUi = ratingUi)
            }
        }
    }
}

@Composable
private fun RatingBadge(ratingUi: FeedbackRatingUi) {
    val borderColor = Color(0xFFD1D5DB)
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(52.dp)
                .clip(CircleShape)
                .background(ratingUi.backgroundColor)
                .border(2.dp, borderColor, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = ratingUi.icon,
                contentDescription = ratingUi.label,
                tint = ratingUi.contentColor,
                modifier = Modifier.size(26.dp)
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = ratingUi.label,
            color = ratingUi.contentColor,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}


private data class FeedbackRatingUi(
    val label: String,
    val backgroundColor: Color,
    val contentColor: Color,
    val icon: ImageVector
)

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
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(12.dp))
            .background(Color(0xFFF7F7F7))
            .height(52.dp),
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
            textStyle = LocalTextStyle.current.copy(fontSize = 14.sp),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = { onSearchClick() }),
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
                .width(52.dp)
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
                modifier = Modifier.size(22.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FeedbacksScreenPreview() {
    FeedbacksScreen(navegacao = rememberNavController())
}
