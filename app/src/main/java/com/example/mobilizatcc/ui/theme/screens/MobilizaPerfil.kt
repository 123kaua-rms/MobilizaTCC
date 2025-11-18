// kotlin
package com.example.mobilizatcc.ui.theme.screens

import android.content.Context
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Feedback
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.mobilizatcc.utils.UserSessionManager

@Composable
fun PerfilScreen(navegacao: NavHostController?) {
    val context = LocalContext.current
    val userSessionManager = remember { UserSessionManager.getInstance(context) }
    
    // Obter dados do usuário logado
    val userId = userSessionManager.getUserId()
    val userName = userSessionManager.getUserName() ?: "Usuário"
    val userEmail = userSessionManager.getUserEmail() ?: "email@exemplo.com"
    val userUsername = userSessionManager.getUserUsername() ?: "user"
    val isLoggedIn = userSessionManager.isLoggedIn()
    
    // Logs para debug
    Log.d("PerfilScreen", "=== DADOS DO USUÁRIO ===")
    Log.d("PerfilScreen", "ID: $userId")
    Log.d("PerfilScreen", "Nome: $userName")
    Log.d("PerfilScreen", "Email: $userEmail")
    Log.d("PerfilScreen", "Username: $userUsername")
    Log.d("PerfilScreen", "Está logado: $isLoggedIn")
    Log.d("PerfilScreen", "========================")
    
    val green = Color(0xFF16A34A)
    val lightGreenBg = Color(0xFFEFF8F1)

    Surface(modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState()), 
        color = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(top = 32.dp, bottom = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Profile section with photo and text side by side
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Avatar circular with camera icon on the left
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(green),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.CameraAlt,
                        contentDescription = "Avatar",
                        tint = Color.White,
                        modifier = Modifier.size(32.dp)
                    )
                }

                Spacer(modifier = Modifier.width(24.dp))

                // User info on the right
                Column {
                    Text(
                        text = "Bem-vindo!",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                    
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    Text(
                        text = userName,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF22343A)
                    )
                    
                    Spacer(modifier = Modifier.height(2.dp))
                    
                    Text(
                        text = "@$userUsername",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                    
                    Spacer(modifier = Modifier.height(2.dp))
                    
                    Text(
                        text = userEmail,
                        color = Color.Gray,
                        fontSize = 12.sp
                    )
                }
            }

            // Card com opções
            Spacer(modifier = Modifier.height(16.dp))
            Card(
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f, fill = false)
                    .background(Color.Transparent)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(lightGreenBg)
                        .padding(vertical = 20.dp, horizontal = 16.dp)
                ) {
                    ProfileRow(
                        icon = { Icon(Icons.Filled.Star, contentDescription = "Favoritos", tint = green) },
                        text = "Favoritos",
                        onClick = { 
                            navegacao?.navigate("linhas") {
                                // Limpa a pilha de navegação para evitar voltar para o perfil ao voltar
                                popUpTo("perfil") { inclusive = true }
                            }
                        }
                    )
                    ProfileRow(
                        icon = { Icon(Icons.Filled.Settings, contentDescription = "Configurações", tint = green) },
                        text = "Configurações",
                        onClick = { 
                            // Navegar para tela de configurações
                        }
                    )
                    ProfileRow(
                        icon = { Icon(Icons.Filled.Feedback, contentDescription = "Feedbacks", tint = green) },
                        text = "Feedbacks feitos",
                        onClick = { 
                            navegacao?.navigate("feedback") {
                                // Limpa a pilha de navegação para evitar voltar para o perfil ao voltar
                                popUpTo("perfil") { inclusive = true }
                            }
                        }
                    )
                    ProfileRow(
                        icon = { Icon(Icons.Filled.ExitToApp, contentDescription = null, tint = Color(0xFFDD2C2C)) },
                        text = "Sair da conta",
                        onClick = { 
                            userSessionManager.clearSession()
                            navegacao?.navigate("loguin") {
                                popUpTo("home") { inclusive = true }
                            }
                        }
                    )
                }
            }

            // Bottom navigation padrão
            Spacer(modifier = Modifier.height(16.dp))
            BottomNavigationBar(navegacao = navegacao, selectedRoute = "perfil")
        }
    }
}

@Composable
private fun ProfileRow(
    icon: @Composable () -> Unit,
    text: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 20.dp, vertical = 18.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.size(36.dp), contentAlignment = Alignment.Center) {
            icon()
        }
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = text,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodyLarge,
            color = Color(0xFF39494A),
            fontSize = 16.sp
        )
        Icon(
            imageVector = Icons.Filled.ArrowForwardIos,
            contentDescription = null,
            tint = Color.Gray,
            modifier = Modifier.size(18.dp)
        )
    }
}

