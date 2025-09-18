package com.example.mobilizatcc.ui.theme.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import com.example.mobilizatcc.R
import com.example.mobilizatcc.model.Cliente
import com.example.mobilizatcc.service.RetrofitFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun mobilizaCadastroScreen(navegacao: NavHostController?,
                           onRegisterClick: () -> Unit = {},
                           onGoogleClick: () -> Unit = {},
                           onLoginClick: () -> Unit = {}
) {
    val greenColor = Color(0xFF3AAA35)
    val context = LocalContext.current

    var nome by remember { mutableStateOf("") }
    var usuario by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.height(2.dp))

            // Logo
            Image(
                painter = painterResource(id = R.drawable.logo_claro),
                contentDescription = "Logo Mobiliza",
                modifier = Modifier
                    .height(190.dp)
                    .fillMaxWidth(),
                alignment = Alignment.Center
            )

            Row {
                Text(
                    text = "Bem-vindo ao ",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Text(
                    text = "Mobiliza!",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = greenColor
                )
            }

            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Faça seu cadastro para ter acesso à sua conta",
                fontSize = 14.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Nome
            OutlinedTextField(
                value = nome,
                onValueChange = { nome = it },
                label = { Text("Nome completo") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Usuário
            OutlinedTextField(
                value = usuario,
                onValueChange = { usuario = it },
                label = { Text("Nome de usuário") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Email
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Senha
            OutlinedTextField(
                value = senha,
                onValueChange = { senha = it },
                label = { Text("Senha") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Botão cadastrar (chama API)
            Button(
                onClick = {
                    val cliente = Cliente(
                        nome = nome,
                        usuario = usuario,
                        email = email,
                        senha = senha
                    )

                    val call = RetrofitFactory().getClienteService().cadastrar(cliente)

                    call.enqueue(object : Callback<Cliente> {
                        override fun onResponse(call: Call<Cliente>, response: Response<Cliente>) {
                            if (response.isSuccessful) {
                                Toast.makeText(context, "Cadastro realizado!", Toast.LENGTH_SHORT).show()
                                onRegisterClick()
                            } else {
                                Toast.makeText(context, "Erro: ${response.code()}", Toast.LENGTH_SHORT).show()
                            }
                        }

                        override fun onFailure(call: Call<Cliente>, t: Throwable) {
                            Toast.makeText(context, "Falha: ${t.message}", Toast.LENGTH_SHORT).show()
                        }
                    })
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = greenColor),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("Cadastrar", color = Color.White)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "OU",
                color = Color.Gray,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Botão Google (placeholder)
            OutlinedButton(
                onClick = onGoogleClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(10.dp)
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

            Spacer(modifier = Modifier.height(20.dp))

            TextButton(onClick = onLoginClick) {
                Text("Já possui uma conta?", color = Color.Gray)
                Text("Entrar", color = greenColor)
            }
        }
    }
}
@Preview(showSystemUi = true, showBackground = true)
@Composable
fun mobilizaCadastroScreenPreview() {
    mobilizaCadastroScreen(navegacao = null)
}

