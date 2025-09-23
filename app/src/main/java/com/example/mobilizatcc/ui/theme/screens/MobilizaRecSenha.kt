package com.example.mobilizatcc.ui.theme.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mobilizatcc.R

@Composable
fun RecSenhaScreen(
    onSendClick: () -> Unit = {},
    onLoginClick: () -> Unit = {}
) {
    val greenColor = Color(0xFF3AAA35)
    var email by remember { mutableStateOf("") }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Canto verde topo esquerdo
            Box(
                modifier = Modifier
                    .size(width = 105.dp, height = 25.dp)
                    .background(greenColor, shape = RoundedCornerShape(bottomEnd = 16.dp))
                    .align(Alignment.TopStart)
            )

            // Canto verde inferior direito
            Box(
                modifier = Modifier
                    .size(width = 105.dp, height = 25.dp)
                    .background(greenColor, shape = RoundedCornerShape(topStart = 16.dp))
                    .align(Alignment.BottomEnd)
            )

            // Conteúdo principal
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Ícone cadeado
                Image(
                    painter = painterResource(id = R.drawable.lock2),
                    contentDescription = "Ícone de segurança",
                    modifier = Modifier.size(120.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Título
                Text(
                    text = "Problemas para entrar?",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Subtítulo
                Text(
                    text = "Insira o e-mail da conta que deseja recuperar",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Texto "E-mail"
                Text(
                    text = "E-mail",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 4.dp)
                )

                // Campo do e-mail
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    placeholder = { Text("Digite seu e-mail") },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.fontisto_email),
                            contentDescription = "E-mail",
                            tint = greenColor,
                            modifier = Modifier.size(24.dp) // ajustado o tamanho do ícone
                        )
                    },
                    visualTransformation = VisualTransformation.None,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Botão enviar
                Button(
                    onClick = onSendClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = greenColor),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text("Enviar", color = Color.White)
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Separador "OU"
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Divider(modifier = Modifier.weight(1f), color = Color(0xFFE0E0E0))
                    Text(
                        text = "OU",
                        color = Color.Gray,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                    Divider(modifier = Modifier.weight(1f), color = Color(0xFFE0E0E0))
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Link para registro
                TextButton(onClick = onLoginClick) {
                    Text("Já possui uma conta?", color = Color.Gray)
                    Text("Entrar", color = greenColor)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RecSenhaScreenPreview() {
    RecSenhaScreen()
}

