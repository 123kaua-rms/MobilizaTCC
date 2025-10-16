package com.example.mobilizatcc.ui.theme.screens

import LoguinResponse
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.compose.animation.AnimatedVisibility
import com.example.mobilizatcc.R
import com.example.mobilizatcc.model.LoginRequest
import com.example.mobilizatcc.service.RetrofitFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.util.Patterns

@Composable
fun LoginScreen(
    navegacao: NavHostController?,
    onGoogleClick: () -> Unit = {}
) {
    val greenColor = Color(0xFF3AAA35)

    var email by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }

    // Flags de validação
    val isEmailValid = email.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    val isSenhaValid = senha.isNotBlank()
    val isFormValid = isEmailValid && isSenhaValid

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        Box(modifier = Modifier.fillMaxSize()) {

            // Cantos verdes decorativos
            Box(
                modifier = Modifier
                    .size(width = 125.dp, height = 45.dp)
                    .background(greenColor, shape = RoundedCornerShape(bottomEnd = 16.dp))
                    .align(Alignment.TopStart)
            )
            Box(
                modifier = Modifier
                    .size(width = 125.dp, height = 45.dp)
                    .background(greenColor, shape = RoundedCornerShape(topStart = 16.dp))
                    .align(Alignment.BottomEnd)
            )

            // Conteúdo principal
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Logo
                Image(
                    painter = painterResource(id = R.drawable.logo_claro),
                    contentDescription = "Logo Mobiliza",
                    modifier = Modifier.height(150.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

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

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Entre com seu email para acessar sua conta",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(28.dp))

                // Campo Email / Nome de Usuário
                OutlinedTextField(
                    value = email,
                    onValueChange = {
                        email = it
                        if (it.isBlank()) errorMessage = "O campo E-mail/Nome de Usuário é obrigatório."
                        else if (!Patterns.EMAIL_ADDRESS.matcher(it).matches()) errorMessage = "Digite um e-mail válido."
                        else errorMessage = null
                    },
                    label = { Text("E-mail ou Nome de Usuário") },
                    placeholder = { Text("Digite seu e-mail ou nome de usuário") },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.fontisto_email),
                            contentDescription = "Email",
                            tint = Color.Unspecified,
                            modifier = Modifier.size(20.dp)
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    isError = email.isNotEmpty() && !Patterns.EMAIL_ADDRESS.matcher(email).matches()
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Campo Senha com alternância de visibilidade
                OutlinedTextField(
                    value = senha,
                    onValueChange = {
                        senha = it
                        if (it.isBlank()) errorMessage = "O campo Senha é obrigatório."
                        else errorMessage = null
                    },
                    label = { Text("Senha") },
                    placeholder = { Text("Digite sua senha") },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.lock),
                            contentDescription = "Senha",
                            tint = Color.Unspecified,
                            modifier = Modifier.size(20.dp)
                        )
                    },
                    trailingIcon = {
                        val icon = if (showPassword) Icons.Filled.VisibilityOff else Icons.Filled.Visibility
                        Icon(
                            imageVector = icon,
                            contentDescription = "Mostrar/Ocultar senha",
                            modifier = Modifier
                                .size(24.dp)
                                .clickable { showPassword = !showPassword }
                        )
                    },
                    visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(4.dp))

                TextButton(
                    onClick = { navegacao?.navigate("recsenha1") },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text(
                        text = "Esqueceu sua senha?",
                        color = greenColor,
                        fontSize = 13.sp,
                        textDecoration = TextDecoration.Underline
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Botão ENTRAR (só habilita se form for válido)
                Button(
                    onClick = {
                        if (!isFormValid) {
                            errorMessage = "Preencha corretamente os campos obrigatórios."
                            return@Button
                        }

                        isLoading = true
                        errorMessage = null

                        val usuarioService = RetrofitFactory().getUsuarioService()
                        val call = usuarioService.loguinUser(LoginRequest(email = email, senha = senha))

                        call.enqueue(object : Callback<LoguinResponse> {
                            override fun onResponse(
                                call: Call<LoguinResponse>,
                                response: Response<LoguinResponse>
                            ) {
                                isLoading = false
                                if (response.isSuccessful) {
                                    val body = response.body()
                                    if (body != null && body.status && body.usuario != null) {
                                        navegacao?.navigate("home") {
                                            popUpTo("login") { inclusive = true }
                                        }
                                    } else {
                                        errorMessage = "E-mail/Nome de usuário ou senha incorretos."
                                    }
                                } else {
                                    errorMessage = "Falha no login. Verifique seus dados."
                                }
                            }

                            override fun onFailure(call: Call<LoguinResponse>, t: Throwable) {
                                isLoading = false
                                errorMessage = "Erro de conexão. Tente novamente."
                            }
                        })
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = greenColor),
                    shape = RoundedCornerShape(8.dp),
                    enabled = !isLoading && isFormValid
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            color = Color.White,
                            strokeWidth = 2.dp,
                            modifier = Modifier.size(22.dp)
                        )
                    } else {
                        Text("Entrar", color = Color.White)
                    }
                }

                // Mensagem de erro
                AnimatedVisibility(visible = errorMessage != null) {
                    Box(
                        modifier = Modifier
                            .padding(top = 12.dp)
                            .fillMaxWidth()
                            .background(Color(0xFFFFEBEE), shape = RoundedCornerShape(8.dp))
                            .padding(12.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                painter = painterResource(id = R.drawable.lock),
                                contentDescription = null,
                                tint = Color.Red,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(
                                text = errorMessage ?: "",
                                color = Color.Red,
                                fontSize = 14.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // OU
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Divider(modifier = Modifier.weight(1f), color = Color.LightGray)
                    Text(text = " OU ", color = Color.Gray, fontSize = 14.sp)
                    Divider(modifier = Modifier.weight(1f), color = Color.LightGray)
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Botão Google
                OutlinedButton(
                    onClick = onGoogleClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(8.dp),
                    enabled = !isLoading
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

                // Link cadastro
                TextButton(
                    onClick = { navegacao?.navigate("cadastro") },
                    enabled = !isLoading
                ) {
                    Text("Não possui uma conta?", color = Color.Gray)
                    Text(" Cadastrar", color = greenColor, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen(navegacao = null)
}
