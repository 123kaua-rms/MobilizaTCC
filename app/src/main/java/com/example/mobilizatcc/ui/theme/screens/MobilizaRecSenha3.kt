package com.example.mobilizatcc.ui.theme.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mobilizatcc.R

@Composable
fun NovaSenhaScreen(
    username: String = "Username",
    onSendClick: () -> Unit = {}
) {
    val greenColor = Color(0xFF3AAA35)

    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .fillMaxWidth(),
        color = Color.White
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Canto verde topo ESQUERDO
            Box(
                modifier = Modifier
                    .size(width = 105.dp, height = 25.dp)
                    .background(greenColor, shape = RoundedCornerShape(bottomEnd = 16.dp))
                    .align(Alignment.TopStart)
            )

            // Canto verde inferior DIREITO
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
                verticalArrangement = Arrangement.Top
            ) {

                Spacer(modifier = Modifier.height(60.dp))

                // Logo Mobiliza
                Image(
                    painter = painterResource(id = R.drawable.logo_claro), // coloque sua logo
                    contentDescription = "Logo Mobiliza",
                    modifier = Modifier.size(140.dp)
                )


                Spacer(modifier = Modifier.height(24.dp))

                // Texto de boas-vindas
                Text(
                    text = "Olá, $username.",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Digite sua nova senha para prosseguir",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Label
                Text(
                    text = "Nova senha",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 4.dp)
                )

                // Campo senha
                OutlinedTextField(
                    value = newPassword,
                    onValueChange = { newPassword = it },
                    placeholder = { Text("Digite sua nova senha") },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.lock3),
                            contentDescription = "Senha",
                            tint = greenColor,
                            modifier = Modifier.size(20.dp) // Ícone maior
                        )
                    },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Password
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Campo confirmar senha
                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    placeholder = { Text("Confirme sua nova senha") },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.lock3),
                            contentDescription = "Confirmar Senha",
                            tint = greenColor,
                            modifier = Modifier.size(20.dp) // Ícone maior
                        )
                    },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Password
                    )
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Botão enviar
                Button(
                    onClick = onSendClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = greenColor),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Enviar", color = Color.White)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NovaSenhaScreenPreview() {
    NovaSenhaScreen()
}
