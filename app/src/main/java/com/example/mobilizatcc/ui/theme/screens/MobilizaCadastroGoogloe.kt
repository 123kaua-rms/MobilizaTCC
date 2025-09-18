package com.example.mobilizatcc.ui.theme.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.mobilizatcc.R

@Composable
fun MobilizaCadastroGoogleScreen(navegacao: NavHostController?,
                                 onRegisterClick: () -> Unit = {},
                                 onGoogleClick: () -> Unit = {},
                                 onLoginClick: () -> Unit = {}
) {
    val greenColor = Color(0xFF3AAA35)

    var usuario by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Top Rounded Green Box
            Box(
                modifier = Modifier
                    .size(width = 60.dp, height = 24.dp)
                    .background(color = greenColor, shape = RoundedCornerShape(bottomEnd = 16.dp))
                    .align(Alignment.Start)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Logo
            Image(
                painter = painterResource(id = R.drawable.logo_claro),
                contentDescription = "Logo Mobiliza",
                modifier = Modifier
                    .height(80.dp)
                    .padding(top = 16.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Títulos
            Text(
                text = "Falta pouco!",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Finalize seu cadastro para ter acesso sua conta",
                fontSize = 14.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Campo Nome de usuário
            OutlinedTextField(
                value = usuario,
                onValueChange = { usuario = it },
                label = { Text("Nome de usuário") },
                placeholder = { Text("Digite seu nome de usuário") },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Person, contentDescription = "Usuário", tint = Color(0xFF16A34A))
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Campo Senha
            OutlinedTextField(
                value = senha,
                onValueChange = { senha = it },
                label = { Text("Senha") },
                placeholder = { Text("Digite sua senha") },
                visualTransformation = PasswordVisualTransformation(),
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Lock, contentDescription = "Senha", tint = Color(0xFF16A34A))
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Botão cadastrar
            Button(
                onClick = onRegisterClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = greenColor),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Cadastrar", color = Color.White)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // OU
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Divider(modifier = Modifier.weight(1f), color = Color.LightGray)
                Text(
                    text = " OU ",
                    color = Color.Gray,
                    fontSize = 14.sp
                )
                Divider(modifier = Modifier.weight(1f), color = Color.LightGray)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Google login button
            OutlinedButton(
                onClick = onGoogleClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.google),
                    contentDescription = "Google",
                    tint = Color.Unspecified,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Logue com Google", color = Color.Black)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Link para login
            Row {
                TextButton(onClick = onLoginClick) {
                    Text("Já possui uma conta?", color = Color.Gray)
                    Text("Entrar", color = greenColor)

                }
            }

            // Bottom Rounded Green Box
            Box(
                modifier = Modifier
                    .size(width = 60.dp, height = 24.dp)
                    .background(color = greenColor, shape = RoundedCornerShape(topStart = 16.dp))
                    .align(Alignment.End)
            )
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun MobilizaCadastroGoogleScreenPreview() {
    MobilizaCadastroGoogleScreen(navegacao = null)
}
