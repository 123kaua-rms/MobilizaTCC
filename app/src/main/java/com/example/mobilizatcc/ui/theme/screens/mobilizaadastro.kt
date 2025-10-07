package com.example.mobilizatcc.ui.theme.screens

import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.mobilizatcc.R
import com.example.mobilizatcc.model.UsuarioRequest
import com.example.mobilizatcc.model.UsuarioResponse
import com.example.mobilizatcc.service.RetrofitFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun RegisterScreen(
    navegacao: NavHostController? = null,
    onGoogleClick: () -> Unit = {}
) {
    val greenColor = Color(0xFF3AAA35)
    val context = LocalContext.current

    var nome by remember { mutableStateOf("") }
    var usuario by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }

    var emailError by remember { mutableStateOf<String?>(null) }
    var senhaStrength by remember { mutableStateOf("") }
    var senhaStrengthColor by remember { mutableStateOf(Color.Gray) }

    var isLoading by remember { mutableStateOf(false) }

    fun isValidGmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches() &&
                email.lowercase().endsWith("@gmail.com")
    }

    fun checkPasswordStrength(password: String) {
        when {
            password.length < 6 -> {
                senhaStrength = "Senha muito fraca"
                senhaStrengthColor = Color.Red
            }
            password.matches(Regex("^(?=.*[a-zA-Z])(?=.*\\d).{6,}\$")) -> {
                senhaStrength = "Senha média — adicione símbolos para fortalecê-la"
                senhaStrengthColor = Color(0xFFFFA500) // Laranja
            }
            password.matches(Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#\$%^&+=!]).{8,}\$")) -> {
                senhaStrength = "Senha forte! Boa escolha 🔒"
                senhaStrengthColor = Color(0xFF3AAA35)
            }
            else -> {
                senhaStrength = "Senha fraca — use letras e números"
                senhaStrengthColor = Color.Red
            }
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        Box(modifier = Modifier.fillMaxSize()) {

            // Canto verde topo esquerdo
            Box(
                modifier = Modifier
                    .size(width = 125.dp, height = 45.dp)
                    .background(greenColor, shape = RoundedCornerShape(bottomEnd = 16.dp))
                    .align(Alignment.TopStart)
            )

            // Canto verde inferior direito
            Box(
                modifier = Modifier
                    .size(width = 125.dp, height = 45.dp)
                    .background(greenColor, shape = RoundedCornerShape(topStart = 16.dp))
                    .align(Alignment.BottomEnd)
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {

                Spacer(modifier = Modifier.height(120.dp))

                Text(
                    text = buildAnnotatedString {
                        append("Bem-vindo ao ")
                        withStyle(style = SpanStyle(color = greenColor, fontWeight = FontWeight.Bold)) {
                            append("Mobiliza!")
                        }
                    },
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Preencha seus dados para criar sua conta",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Campos de input
                OutlinedTextField(
                    value = nome,
                    onValueChange = { nome = it },
                    label = { Text("Nome completo") },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.user),
                            contentDescription = "Nome",
                            tint = Color.Unspecified,
                            modifier = Modifier.size(20.dp)
                        )
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = usuario,
                    onValueChange = { usuario = it },
                    label = { Text("Nome de usuário") },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.user),
                            contentDescription = "Usuário",
                            tint = Color.Unspecified,
                            modifier = Modifier.size(20.dp)
                        )
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = {
                        email = it
                        emailError = if (!isValidGmail(it) && it.isNotEmpty()) {
                            "Digite um e-mail Gmail válido"
                        } else null
                    },
                    isError = emailError != null,
                    label = { Text("E-mail") },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.fontisto_email),
                            contentDescription = "Email",
                            tint = Color.Unspecified,
                            modifier = Modifier.size(20.dp)
                        )
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                if (emailError != null) {
                    Text(
                        text = emailError ?: "",
                        color = Color.Red,
                        fontSize = 12.sp,
                        modifier = Modifier.align(Alignment.Start)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = senha,
                    onValueChange = {
                        senha = it
                        checkPasswordStrength(it)
                    },
                    label = { Text("Senha") },
                    visualTransformation = PasswordVisualTransformation(),
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.lock),
                            contentDescription = "Senha",
                            tint = Color.Unspecified,
                            modifier = Modifier.size(20.dp)
                        )
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                if (senha.isNotEmpty()) {
                    Text(
                        text = senhaStrength,
                        color = senhaStrengthColor,
                        fontSize = 13.sp,
                        modifier = Modifier
                            .align(Alignment.Start)
                            .padding(top = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(28.dp))

                Button(
                    onClick = {
                        when {
                            nome.isBlank() || usuario.isBlank() || email.isBlank() || senha.isBlank() -> {
                                Toast.makeText(context, "Preencha todos os campos.", Toast.LENGTH_SHORT).show()
                            }
                            !isValidGmail(email) -> {
                                Toast.makeText(context, "Digite um Gmail válido.", Toast.LENGTH_SHORT).show()
                            }
                            senhaStrength.contains("fraca", ignoreCase = true) -> {
                                Toast.makeText(context, "Escolha uma senha mais forte.", Toast.LENGTH_SHORT).show()
                            }
                            else -> {
                                isLoading = true
                                val usuarioService = RetrofitFactory().getUsuarioService()
                                val usuarioRequest = UsuarioRequest(
                                    foto = "foto_legal.png",
                                    nome = nome,
                                    username = usuario,
                                    email = email,
                                    senha = senha
                                )

                                usuarioService.registerUser(usuarioRequest)
                                    .enqueue(object : Callback<UsuarioResponse> {
                                        override fun onResponse(
                                            call: Call<UsuarioResponse>,
                                            response: Response<UsuarioResponse>
                                        ) {
                                            isLoading = false
                                            if (!response.isSuccessful) {
                                                Toast.makeText(context, "Erro ao cadastrar. Tente novamente.", Toast.LENGTH_SHORT).show()
                                            } else {
                                                Toast.makeText(context, "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT).show()
                                                navegacao?.navigate("loguin") {
                                                    popUpTo("register") { inclusive = true }
                                                }
                                            }
                                        }

                                        override fun onFailure(call: Call<UsuarioResponse>, t: Throwable) {
                                            isLoading = false
                                            Toast.makeText(context, "Falha de conexão: ${t.message}", Toast.LENGTH_SHORT).show()
                                        }
                                    })
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = greenColor),
                    shape = RoundedCornerShape(10.dp),
                    enabled = !isLoading
                ) {
                    Text(if (isLoading) "Cadastrando..." else "Cadastrar", color = Color.White)
                }

                Spacer(modifier = Modifier.height(26.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Divider(modifier = Modifier.weight(1f), color = Color.LightGray)
                    Text(" OU ", color = Color.Gray, fontSize = 14.sp)
                    Divider(modifier = Modifier.weight(1f), color = Color.LightGray)
                }

                Spacer(modifier = Modifier.height(22.dp))

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

                Spacer(modifier = Modifier.height(10.dp))

                TextButton(onClick = { navegacao?.navigate("loguin") }) {
                    Text("Já possui uma conta? ", color = Color.Gray)
                    Text("Entrar", color = greenColor)
                }
            }
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun RegisterScreenPreview() {
    RegisterScreen(navegacao = null)
}
