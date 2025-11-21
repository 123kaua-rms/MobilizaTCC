package com.example.mobilizatcc.ui.theme.screens

import android.graphics.Color as AndroidColor
import android.util.Log
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.mobilizatcc.R
import com.example.mobilizatcc.model.FeedbackResponse
import com.example.mobilizatcc.service.RetrofitFactory
import com.example.mobilizatcc.ui.theme.components.RouteBadge
import com.example.mobilizatcc.utils.UserSessionManager
import coil.compose.AsyncImage
import kotlinx.coroutines.launch

//Color Figma -> 0xFF16A34A
//Color Alternative -> 0xFF26A65B

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FeedbacksScreen(
    navegacao: NavHostController?
) {
    var searchQuery by remember { mutableStateOf("") }
    var feedbacks by remember { mutableStateOf<List<FeedbackResponse>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val context = LocalContext.current
    val userSessionManager = remember { UserSessionManager.getInstance(context) }
    val currentUserId = remember { userSessionManager.getUserId() }

    val feedbackService = remember { RetrofitFactory().getFeedbackService() }
    val usuarioService = remember { RetrofitFactory().getUsuarioService() }
    val coroutineScope = rememberCoroutineScope()
    var usernamesCache by remember { mutableStateOf<Map<Int, String>>(emptyMap()) }
    var isRefreshing by remember { mutableStateOf(false) }

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

    LaunchedEffect(Unit) {
        requestFeedbacks()
    }

    LaunchedEffect(feedbacks) {
        var cacheSnapshot = usernamesCache

        val providedUsernames = feedbacks
            .mapNotNull { feedback ->
                feedback.username?.takeIf { it.isNotBlank() }?.let { name ->
                    feedback.id_usuario to name
                }
            }
            .filter { (userId, name) -> cacheSnapshot[userId] != name }

        if (providedUsernames.isNotEmpty()) {
            cacheSnapshot = cacheSnapshot + providedUsernames
            usernamesCache = cacheSnapshot
        }

        val missingUsers = feedbacks
            .map { it.id_usuario }
            .distinct()
            .filter { !cacheSnapshot.containsKey(it) }

        missingUsers.forEach { userId ->
            launch {
                runCatching {
                    val response = usuarioService.getUsuarioById(userId)
                    response.username?.takeIf { it.isNotBlank() }
                }.onSuccess { apiUsername ->
                    if (!apiUsername.isNullOrBlank()) {
                        usernamesCache = usernamesCache + (userId to apiUsername)
                    }
                }.onFailure {
                    Log.w("FeedbacksScreen", "Não foi possível buscar username do usuário $userId", it)
                }
            }
        }

        val missingInPayload = feedbacks.filter { it.username.isNullOrBlank() }
        if (missingInPayload.isNotEmpty()) {
            Log.w(
                "FeedbacksScreen",
                "Feedbacks sem username na payload: ${missingInPayload.joinToString { "(id=${it.id ?: "-"}, user=${it.id_usuario})" }}"
            )
        }
    }

    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = {
            coroutineScope.launch {
                isRefreshing = true
                try {
                    requestFeedbacks(searchQuery)
                } finally {
                    isRefreshing = false
                }
            }
        }
    )

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
                .pullRefresh(pullRefreshState)
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
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            contentPadding = PaddingValues(bottom = 96.dp)
                        ) {
                            items(feedbacks) { feedback ->
                                val cachedUsername = usernamesCache[feedback.id_usuario]
                                val payloadUsername = feedback.username?.takeIf { it.isNotBlank() }
                                val nomeUsuario = feedback.nome_usuario?.takeIf { it.isNotBlank() }
                                val effectiveUsername = cachedUsername ?: payloadUsername
                                val composedDisplay = when {
                                    nomeUsuario != null && !effectiveUsername.isNullOrBlank() ->
                                        "$nomeUsuario (${effectiveUsername})"
                                    !effectiveUsername.isNullOrBlank() -> effectiveUsername
                                    nomeUsuario != null -> nomeUsuario
                                    else -> "Usuário ${feedback.id_usuario}"
                                }
                                val displayUsername = if (currentUserId != -1 && feedback.id_usuario == currentUserId) {
                                    "Você"
                                } else {
                                    composedDisplay
                                }
                                FeedbackCardFromApi(feedback, displayUsername)
                            }
                        }
                    }
                }
            }

            PullRefreshIndicator(
                refreshing = isRefreshing,
                state = pullRefreshState,
                modifier = Modifier.align(Alignment.TopCenter),
                backgroundColor = Color.White,
                contentColor = Color(0xFF16A34A)
            )
        }
    }
}

@Composable
fun FeedbackCardFromApi(
    feedback: FeedbackResponse,
    username: String
) {
    val contentScrollState = rememberScrollState()

    val ratingUi = when (feedback.avaliacao) {
        1 -> FeedbackRatingUi(
            label = "Bom",
            backgroundColor = Color(0xFFDCFCE7),
            accentColor = Color(0xFF16A34A),
            iconRes = R.drawable.emoji_feliz
        )

        2 -> FeedbackRatingUi(
            label = "Mediano",
            backgroundColor = Color(0xFFFEF9C3),
            accentColor = Color(0xFFCA8A04),
            iconRes = R.drawable.emoji_neutro
        )

        3 -> FeedbackRatingUi(
            label = "Ruim",
            backgroundColor = Color(0xFFFEE2E2),
            accentColor = Color(0xFFDC2626),
            iconRes = R.drawable.emoji_triste
        )

        else -> FeedbackRatingUi(
            label = "--",
            backgroundColor = Color(0xFFE2E8F0),
            accentColor = Color(0xFF94A3B8),
            iconRes = R.drawable.emoji_neutro
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
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .heightIn(min = 48.dp, max = 200.dp)
                        .verticalScroll(contentScrollState)
                ) {
                    Text(
                        text = feedback.conteudo,
                        color = Color(0xFF111827),
                        fontSize = 16.sp,
                        lineHeight = 22.sp
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                RatingBadge(ratingUi = ratingUi)
            }
        }
    }
}

@Composable
private fun RatingBadge(ratingUi: FeedbackRatingUi) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            painter = painterResource(id = ratingUi.iconRes),
            contentDescription = ratingUi.label,
            modifier = Modifier.size(54.dp)
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = ratingUi.label,
            color = ratingUi.accentColor,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}


private data class FeedbackRatingUi(
    val label: String,
    val backgroundColor: Color,
    val accentColor: Color,
    @DrawableRes val iconRes: Int
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
