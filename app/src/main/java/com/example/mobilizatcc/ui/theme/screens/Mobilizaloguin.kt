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
import androidx.compose.ui.platform.LocalContext
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
import com.example.mobilizatcc.utils.UserSessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.util.Patterns
import android.util.Log
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun LoginScreen(
    navegacao: NavHostController?,
    onGoogleClick: () -> Unit = {}
) {
    val greenColor = Color(0xFF16A34A)
    val context = LocalContext.current
    val userSessionManager = remember { UserSessionManager.getInstance(context) }

    var email by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }

    // Verificar se usuário já está logado
    LaunchedEffect(Unit) {
        if (userSessionManager.isLoggedIn()) {
            Log.d("LoginScreen", "Usuário já está logado, redirecionando para home")
            navegacao?.navigate("home") {
                popUpTo("loguin") { inclusive = true }
            }
        }
    }

    // Flags de validação - aceitar email ou username
    val isEmailValid = email.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    val isUsernameValid = email.isNotBlank() && email.length >= 3 // Username mínimo 3 caracteres
    val isEmailOrUsernameValid = isEmailValid || isUsernameValid
    val isSenhaValid = senha.isNotBlank()
    val isFormValid = isEmailOrUsernameValid && isSenhaValid

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        BoxWithConstraints(
            modifier = Modifier.fillMaxSize()
        ) {
            val maxWidth = maxWidth

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
                    .fillMaxWidth()
                    .padding(horizontal = if (maxWidth < 360.dp) 12.dp else 24.dp)
                    .align(Alignment.Center)
                    .defaultMinSize(minWidth = 280.dp)
                    .wrapContentHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Logo
                Image(
                    painter = painterResource(id = R.drawable.logo_claro),
                    contentDescription = "Logo Mobiliza",
                    modifier = Modifier
                        .height(if (maxWidth < 360.dp) 120.dp else 150.dp)
                        .fillMaxWidth(0.5f)
                )

                Spacer(modifier = Modifier.height(if (maxWidth < 360.dp) 12.dp else 16.dp))

                Text(
                    text = buildAnnotatedString {
                        append("Bem-vindo ao ")
                        withStyle(style = SpanStyle(color = greenColor, fontWeight = FontWeight.Bold)) {
                            append("Mobiliza!")
                        }
                    },
                    fontSize = if (maxWidth < 360.dp) 18.sp else 22.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(if (maxWidth < 360.dp) 2.dp else 4.dp))

                Text(
                    text = "Entre com seu email para acessar sua conta",
                    fontSize = if (maxWidth < 360.dp) 12.sp else 14.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(if (maxWidth < 360.dp) 20.dp else 28.dp))

                // Campo Email / Nome de Usuário
                OutlinedTextField(
                    value = email,
                    onValueChange = {
                        email = it
                        if (it.isBlank()) {
                            errorMessage = "O campo E-mail/Nome de Usuário é obrigatório."
                        } else if (it.length < 3) {
                            errorMessage = "Digite pelo menos 3 caracteres."
                        } else {
                            errorMessage = null
                        }
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
                    isError = email.isNotEmpty() && !isEmailOrUsernameValid
                )

                Spacer(modifier = Modifier.height(if (maxWidth < 360.dp) 8.dp else 12.dp))

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

                Spacer(modifier = Modifier.height(if (maxWidth < 360.dp) 16.dp else 24.dp))

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
                        
                        // Criar LoginRequest sem encoding especial - deixar o Retrofit/OkHttp lidar com isso
                        val loginRequest = LoginRequest(email = email.trim(), senha = senha)

                        Log.d("LoginScreen", "Tentando login com email: ${email.trim()}")
                        Log.d("LoginScreen", "Senha tem ${senha.length} caracteres")
                        Log.d("LoginScreen", "LoginRequest: email=${email.trim()}, senha=[HIDDEN]")

                        val call = usuarioService.loguinUser(loginRequest)

                        call.enqueue(object : Callback<LoguinResponse> {
                            override fun onResponse(
                                call: Call<LoguinResponse>,
                                response: Response<LoguinResponse>
                            ) {
                                isLoading = false
                                Log.d("LoginScreen", "Response code: ${response.code()}")

                                if (response.isSuccessful) {
                                    val body = response.body()
                                    Log.d("LoginScreen", "Body status: ${body?.status}, usuario: ${body?.usuario}")

                                    if (body != null && body.status && body.usuario != null) {
                                        val usuario = body.usuario!!
                                        
                                        Log.d("LoginScreen", "Login bem sucedido! Dados do usuário:")
                                        Log.d("LoginScreen", "- ID: ${usuario.id}")
                                        Log.d("LoginScreen", "- Nome: ${usuario.nome ?: "null"}")
                                        Log.d("LoginScreen", "- Email: ${usuario.email ?: "null"}")
                                        Log.d("LoginScreen", "- Username: ${usuario.username ?: "null"}")
                                        Log.d("LoginScreen", "- Token: ${body.token ?: "null"}")
                                        
                                        // Salvar sessão do usuário
                                        try {
                                            userSessionManager.saveUserSession(
                                                userId = usuario.id,
                                                name = usuario.nome,
                                                email = usuario.email,
                                                username = usuario.username,
                                                token = body.token
                                            )
                                            
                                            Log.d("LoginScreen", "Sessão salva com sucesso!")
                                            Log.d("LoginScreen", "Verificando se está logado: ${userSessionManager.isLoggedIn()}")
                                            
                                            navegacao?.navigate("home") {
                                                popUpTo("loguin") { inclusive = true }
                                            }
                                        } catch (e: Exception) {
                                            Log.e("LoginScreen", "Erro ao salvar sessão", e)
                                            errorMessage = "Erro ao salvar dados do usuário. Tente novamente."
                                        }
                                    } else {
                                        errorMessage = "E-mail/Nome de usuário ou senha incorretos."
                                        Log.e("LoginScreen", "Login falhou: body null ou status false")
                                    }
                                } else {
                                    // Tentar parsear a mensagem de erro do backend
                                    val errorBody = response.errorBody()?.string()
                                    Log.e("LoginScreen", "Response error: $errorBody")

                                    errorMessage = when (response.code()) {
                                        401 -> "E-mail ou senha incorretos."
                                        404 -> "Usuário não encontrado."
                                        400 -> "Dados inválidos. Verifique os campos."
                                        500 -> "Erro no servidor. Tente novamente mais tarde."
                                        else -> try {
                                            // Extrair mensagem do JSON de erro
                                            val pattern = "\"message\":\"([^\"]+)\"".toRegex()
                                            val match = pattern.find(errorBody ?: "")
                                            match?.groupValues?.get(1) ?: "Falha no login. Verifique seus dados."
                                        } catch (e: Exception) {
                                            "Falha no login. Verifique seus dados."
                                        }
                                    }
                                }
                            }

                            override fun onFailure(call: Call<LoguinResponse>, t: Throwable) {
                                isLoading = false
                                errorMessage = "Erro de conexão. Tente novamente."
                                Log.e("LoginScreen", "Falha na requisição", t)
                            }
                        })
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(if (maxWidth < 360.dp) 44.dp else 48.dp),
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

                Spacer(modifier = Modifier.height(if (maxWidth < 360.dp) 16.dp else 24.dp))

                // OU
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Divider(modifier = Modifier.weight(1f), color = Color.LightGray)
                    Text(text = " OU ", color = Color.Gray, fontSize = 14.sp)
                    Divider(modifier = Modifier.weight(1f), color = Color.LightGray)
                }

                Spacer(modifier = Modifier.height(if (maxWidth < 360.dp) 12.dp else 16.dp))

                // Botão Google
                OutlinedButton(
                    onClick = onGoogleClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(if (maxWidth < 360.dp) 44.dp else 48.dp),
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

                Spacer(modifier = Modifier.height(if (maxWidth < 360.dp) 16.dp else 20.dp))

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
