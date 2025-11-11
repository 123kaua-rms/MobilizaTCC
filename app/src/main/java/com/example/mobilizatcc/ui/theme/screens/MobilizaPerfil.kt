// kotlin
package com.example.mobilizatcc.ui.theme.screens

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Star
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
import com.example.mobilizatcc.model.Usuario
import com.google.gson.Gson

@Composable
fun PerfilScreen(navegacao: NavHostController?) {
    val context = LocalContext.current
    val prefs = remember { context.getSharedPreferences("mobiliza_prefs", Context.MODE_PRIVATE) }
    val usuarioJson = prefs.getString("usuario_json", null)
    val usuario: Usuario? = remember(usuarioJson) {
        try { Gson().fromJson(usuarioJson, Usuario::class.java) } catch (_: Exception) { null }
    }
    val nome = usuario?.nome ?: usuario?.email ?: "Username"
    val green = Color(0xFF16A34A)
    val lightGreenBg = Color(0xFFEFF8F1)

    Surface(modifier = Modifier.fillMaxSize(), color = Color.White) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Top area
            Box(modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Avatar circular with camera icon
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .clip(CircleShape)
                            .background(green),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.CameraAlt,
                            contentDescription = "Avatar",
                            tint = Color.White,
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Bem-vindo!",
                            color = Color.Gray,
                            fontSize = 14.sp
                        )
                        Text(
                            text = nome,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF22343A)
                        )
                    }

                    // Logout icon (vermelho)
                    IconButton(onClick = {
                        prefs.edit().clear().apply()
                        navegacao?.navigate("loguin") {
                            popUpTo("home") { inclusive = true }
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Person,
                            contentDescription = "Logout",
                            tint = Color(0xFFDD2C2C)
                        )
                    }
                }
            }

            // Card com opções
            Card(
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(Color.Transparent)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(lightGreenBg)
                        .padding(vertical = 28.dp)
                ) {
                    ProfileRow(
                        icon = { Icon(Icons.Outlined.Star, contentDescription = null, tint = green) },
                        text = "Linhas favoritas",
                        onClick = { navegacao?.navigate("linhas_favoritas") }
                    )
                    ProfileRow(
                        icon = { Icon(Icons.Filled.LocationOn, contentDescription = null, tint = green) },
                        text = "Feedbacks realizados",
                        onClick = { navegacao?.navigate("feedbacks_realizados") }
                    )
                    ProfileRow(
                        icon = { Icon(Icons.Filled.Security, contentDescription = null, tint = green) },
                        text = "Segurança",
                        onClick = { navegacao?.navigate("seguranca") }
                    )
                    ProfileRow(
                        icon = { Icon(Icons.Filled.Settings, contentDescription = null, tint = green) },
                        text = "Configurações",
                        onClick = { navegacao?.navigate("configuracoes") }
                    )
                }
            }

            // Bottom navigation (simples, fixa)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                BottomNavItemCustom(
                    icon = Icons.Filled.Home,
                    label = "Início",
                    selected = false,
                    onClick = {
                        navegacao?.navigate("home") {
                            popUpTo("home") { inclusive = false }
                        }
                    }
                )
                BottomNavItemCustom(
                    icon = Icons.Filled.LocationOn,
                    label = "Linhas",
                    selected = false,
                    onClick = { navegacao?.navigate("linhas") }
                )
                BottomNavItemCustom(
                    icon = Icons.Filled.Person,
                    label = "Feedback",
                    selected = false,
                    onClick = { navegacao?.navigate("feedback") }
                )
                BottomNavItemCustom(
                    icon = Icons.Filled.Person,
                    label = "Perfil",
                    selected = true,
                    onClick = { /* já está em perfil */ },
                    tint = green
                )
            }
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

@Composable
private fun BottomNavItemCustom(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    tint: Color = Color.Gray
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable { onClick() }
            .padding(horizontal = 6.dp)
    ) {
        Icon(icon, contentDescription = label, tint = if (selected) tint else Color.Gray, modifier = Modifier.size(22.dp))
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = label, fontSize = 12.sp, color = if (selected) tint else Color.Gray)
    }
}
