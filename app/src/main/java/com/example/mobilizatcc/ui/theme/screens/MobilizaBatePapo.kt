package com.example.mobilizatcc.ui.theme.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.mobilizatcc.R

data class ChatMessage(
    val username: String,
    val message: String,
    val time: String,
    val isCurrentUser: Boolean
)


@Composable
fun ChatScreen(navegacao: NavHostController?) {
    var messageText by remember { mutableStateOf("") }

    Scaffold(
        bottomBar = { BottomNavigationBar(navegacao = navegacao, selectedRoute = "linhas") }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color.White)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navegacao?.popBackStack() }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Voltar",
                        tint = Color.Black
                    )
                }

                Icon(
                    imageVector = Icons.Default.DirectionsBus,
                    contentDescription = "Ônibus",
                    tint = Color(0xFF26A65B),
                    modifier = Modifier
                        .size(20.dp)
                        .padding(end = 4.dp)
                )

                Text(
                    text = "106",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "Terminal Central / Vila Nova - Estação KM 21",
                    fontSize = 13.sp,
                    color = Color.DarkGray,
                    maxLines = 1
                )
            }

            Text(
                text = "Bate-papo",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Color.Black
            )

            // Lista de mensagens simulada
            val messages = listOf(
                ChatMessage(
                    username = "Username",
                    message = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt",
                    time = "00:00",
                    isCurrentUser = false
                ),
                ChatMessage(
                    username = "Username",
                    message = "Lorem ipsum dolor sit amet",
                    time = "00:00",
                    isCurrentUser = false
                ),
                ChatMessage(
                    username = "Você",
                    message = "Lorem ipsum dolor sit amet",
                    time = "00:00",
                    isCurrentUser = true
                )
            )

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(messages.size) { index ->
                    val msg = messages[index]
                    ChatMessageItem(
                        username = msg.username,
                        message = msg.message,
                        time = msg.time,
                        isCurrentUser = msg.isCurrentUser
                    )
                }
            }



            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Min)
                        .clip(RoundedCornerShape(5.dp))
                        .background(Color(0xFFF7F7F7))
                        .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(5.dp)),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextField(
                        value = messageText,
                        onValueChange = { messageText = it },
                        placeholder = {
                            Text(
                                text = "O que está acontecendo na linha?",
                                color = Color.Gray,
                                fontSize = 12.sp,
                            )
                        },
                        modifier = Modifier
                            .fillMaxHeight()
                            .height(35.dp)
                            .padding(start = 12.dp)
                            .defaultMinSize(minHeight = 35.dp),
                        singleLine = true,
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            disabledContainerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            cursorColor = Color(0xFF26A65B)
                        )
                    )

                    Box(
                        modifier = Modifier
                            .height(50.dp)
                            .width(50.dp)
                            .background(Color(0xFF26A65B)),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.enviar),
                            contentDescription = "Botão Enviar",
                            modifier = Modifier
                                .size(28.dp)
                                .clickable {  }
                        )
                    }
                }
            }
        }

    }
}


@Composable
fun ChatMessageItem(username: String, message: String, time: String, isCurrentUser: Boolean) {
    val alignment = if (isCurrentUser) Alignment.End else Alignment.Start
    val backgroundColor = if (isCurrentUser) Color(0xFFDFFFD6) else Color.White
    val textColor = if (isCurrentUser) Color.Black else Color.Black

    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = alignment
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = 280.dp)
                .background(backgroundColor, RoundedCornerShape(8.dp))
                .border(0.dp, Color.Transparent, RoundedCornerShape(8.dp))
        ) {
            // Cabeçalho verde com usuário e hora
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
                    text = username,
                    color = Color.White,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Normal
                )

                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = time,
                    color = Color.White,
                    fontSize = 11.sp
                )
            }

            // Corpo da mensagem
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF8F8F8))
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = message,
                    color = textColor,
                    fontSize = 13.sp
                )
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun ChatScreenPreview() {
    ChatScreen(navegacao = rememberNavController())
}