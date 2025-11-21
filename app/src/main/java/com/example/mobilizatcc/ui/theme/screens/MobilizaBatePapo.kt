package com.example.mobilizatcc.ui.theme.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.mobilizatcc.R
import com.example.mobilizatcc.model.ChatMessageDto
import com.example.mobilizatcc.model.ChatMessageRequest
import com.example.mobilizatcc.service.RetrofitFactory
import com.example.mobilizatcc.ui.theme.components.RouteBadge
import com.example.mobilizatcc.utils.UserSessionManager

import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import coil.compose.AsyncImage
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow

@Composable
fun ChatScreen(
    navegacao: NavHostController?,
    routeId: String,
    routeShortName: String,
    routeDescription: String
) {
    val context = LocalContext.current
    val usuarioService = remember { RetrofitFactory().getUsuarioService() }
    val userSession = remember { UserSessionManager.getInstance(context) }
    val coroutineScope = rememberCoroutineScope()

    val currentUserId = userSession.getUserId()
    val currentUsername = userSession.getUserUsername() ?: "Você"
    val accentGreen = Color(0xFF209C4E)

    var messageText by remember { mutableStateOf("") }
    var messages by remember { mutableStateOf<List<ChatMessageDto>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var isSending by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var routeLongName by remember { mutableStateOf<String?>(null) }

    suspend fun loadMessages() {
        try {
            isLoading = true
            val response = usuarioService.getChatMessages(routeId)
            messages = response.messages.sortedBy { it.dataEnvio }
            errorMessage = null
        } catch (e: Exception) {
            errorMessage = "Não foi possível carregar as mensagens."
        } finally {
            isLoading = false
        }
    }

    suspend fun loadRouteLongName() {
        try {
            val response = usuarioService.getAllLines()
            routeLongName = response.linhas.firstOrNull { it.routeId == routeId }?.routeLongName
        } catch (_: Exception) {
            routeLongName = null
        }
    }

    LaunchedEffect(routeId) {
        loadRouteLongName()
        loadMessages()
    }

    val lazyListState = rememberLazyListState()

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            lazyListState.animateScrollToItem(messages.lastIndex)
        }
    }

    val uiMessages = remember(messages, currentUserId, currentUsername) {
        messages.map { dto ->
            val user = dto.usuarios.firstOrNull()
            ChatUiMessage(
                id = dto.idMensagem,
                username = user?.username?.takeUnless { it.isBlank() }
                    ?: user?.nome?.takeUnless { it.isBlank() }
                    ?: currentUsername,
                avatarUrl = user?.foto,
                content = dto.conteudo,
                time = dto.dataEnvio.formatChatTime(),
                isCurrentUser = user?.id == currentUserId
            )
        }
    }

    fun sendMessage() {
        val content = messageText.trim()
        if (content.isEmpty() || isSending || currentUserId == -1) return

        coroutineScope.launch {
            try {
                isSending = true
                usuarioService.sendChatMessage(
                    ChatMessageRequest(routeId = routeId, idUsuario = currentUserId, conteudo = content)
                )
                messageText = ""
                loadMessages()
            } catch (e: Exception) {
                errorMessage = "Erro ao enviar mensagem."
            } finally {
                isSending = false
            }
        }
    }

    Scaffold(
        bottomBar = { BottomNavigationBar(navegacao = navegacao, selectedRoute = "linhas") }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color.White)
        ) {
            ChatHeader(
                navegacao = navegacao,
                routeShortName = routeShortName,
                routeName = routeLongName ?: routeDescription
            )

            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                color = Color(0xFFE5E7EB)
            )

            Text(
                text = "Bate-papo",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color(0xFF111827)
            )

            when {
                isLoading -> {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = accentGreen)
                    }
                }

                errorMessage != null && uiMessages.isEmpty() -> {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Não tem mensagens aqui, diga um \"Olá\"",
                            color = Color(0xFF9CA3AF),
                            textAlign = TextAlign.Center
                        )
                    }
                }

                errorMessage != null -> {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = errorMessage ?: "", color = Color.Gray)
                            Spacer(modifier = Modifier.height(12.dp))
                            OutlinedButton(onClick = { coroutineScope.launch { loadMessages() } }) {
                                Text("Tentar novamente")
                            }
                        }
                    }
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        state = lazyListState,
                        verticalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        if (uiMessages.isEmpty()) {
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 32.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "Não tem mensagens aqui, diga um \"Olá\"",
                                        color = Color(0xFF9CA3AF),
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        } else {
                            items(uiMessages.size) { index ->
                                ChatMessageItem(uiMessages[index])
                            }
                        }
                    }
                }
            }

            ChatInputField(
                messageText = messageText,
                onValueChange = { messageText = it },
                onSend = { sendMessage() },
                isSending = isSending,
                accentGreen = accentGreen
            )
        }
    }
}

@Composable
private fun ChatHeader(
    navegacao: NavHostController?,
    routeShortName: String,
    routeName: String
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
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

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navegacao?.popBackStack() }) {
                Icon(
                    painter = painterResource(id = R.drawable.seta),
                    contentDescription = "Voltar",
                    tint = Color(0xFF606C63),
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            RouteBadge(routeCode = routeShortName, maxWidth = 92.dp)

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = routeName,
                color = Color(0xFF111827),
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )

            Spacer(
                modifier = Modifier
                    .width(16.dp)
                    .height(1.dp)
            )
        }
    }
}

@Composable
private fun ChatInputField(
    messageText: String,
    onValueChange: (String) -> Unit,
    onSend: () -> Unit,
    isSending: Boolean,
    accentGreen: Color
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xFFF3F3F3))
                .border(1.dp, Color(0xFFE2E2E2), RoundedCornerShape(8.dp)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = messageText,
                onValueChange = onValueChange,
                placeholder = {
                    Text(
                        text = "O que está acontecendo na linha?",
                        color = Color(0xFF9CA3AF),
                        fontSize = 13.sp
                    )
                },
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 12.dp),
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = accentGreen
                )
            )

            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(RoundedCornerShape(topEnd = 8.dp, bottomEnd = 8.dp))
                    .background(accentGreen)
                    .clickable(enabled = messageText.isNotBlank() && !isSending) { onSend() },
                contentAlignment = Alignment.Center
            ) {
                if (isSending) {
                    CircularProgressIndicator(color = Color.White, strokeWidth = 2.dp, modifier = Modifier.size(22.dp))
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.enviar),
                        contentDescription = "Enviar",
                        modifier = Modifier.size(26.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun ChatMessageItem(message: ChatUiMessage) {
    val alignment = if (message.isCurrentUser) Alignment.End else Alignment.Start
    val bubbleShape = RoundedCornerShape(16.dp)
    val accentGreen = Color(0xFF209C4E)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp),
        horizontalAlignment = alignment
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = 320.dp)
                .clip(bubbleShape)
                .border(1.dp, Color(0xFFE5E7EB), bubbleShape)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(accentGreen)
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ChatUserAvatar(message.avatarUrl, message.username)
                Text(
                    text = message.username,
                    color = Color.White,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF3F3F3))
                    .padding(horizontal = 12.dp, vertical = 12.dp)
            ) {
                Text(text = message.content, color = Color(0xFF111827), fontSize = 14.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = message.time,
                    color = Color(0xFF9CA3AF),
                    fontSize = 11.sp,
                    modifier = Modifier.align(Alignment.End)
                )
            }
        }
    }
}

@Composable
private fun ChatUserAvatar(avatarUrl: String?, username: String) {
    val placeholder = painterResource(id = R.drawable.perfilcinza)
    if (!avatarUrl.isNullOrBlank()) {
        AsyncImage(
            model = avatarUrl,
            contentDescription = "Avatar de $username",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(22.dp)
                .clip(CircleShape),
            placeholder = placeholder,
            error = placeholder
        )
    } else {
        Image(
            painter = placeholder,
            contentDescription = "Avatar",
            modifier = Modifier
                .size(22.dp)
                .clip(CircleShape)
        )
    }
}

private data class ChatUiMessage(
    val id: Int,
    val username: String,
    val avatarUrl: String?,
    val content: String,
    val time: String,
    val isCurrentUser: Boolean
)

private fun String.formatChatTime(): String {
    return try {
        val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val outputFormatter = DateTimeFormatter.ofPattern("HH:mm")
        LocalDateTime.parse(this, inputFormatter).format(outputFormatter)
    } catch (_: DateTimeParseException) {
        "--:--"
    }
}

@Preview(showBackground = true)
@Composable
fun ChatScreenPreview() {
    ChatScreen(
        navegacao = rememberNavController(),
        routeId = "1012-10",
        routeShortName = "106",
        routeDescription = "Terminal Central / Vila Nova - Estação KM 21"
    )
}