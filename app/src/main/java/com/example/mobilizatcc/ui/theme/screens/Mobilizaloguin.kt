//package com.example.mobilizatcc.ui.theme.screens
//
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.text.SpanStyle
//import androidx.compose.ui.text.buildAnnotatedString
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.input.PasswordVisualTransformation
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.text.style.TextDecoration
//import androidx.compose.ui.text.withStyle
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.navigation.NavHostController
//import com.example.mobilizatcc.R
//import com.example.mobilizatcc.model.LoginRequest
//import com.example.mobilizatcc.service.RetrofitFactory
//import kotlinx.coroutines.launch
//
//@Composable
//fun LoginScreen(
//    navegacao: NavHostController?,
//    onForgotPasswordClick: () -> Unit = {},
//    onGoogleClick: () -> Unit = {},
//    onRegisterClick: () -> Unit = {}
//) {
//    val greenColor = Color(0xFF3AAA35)
//
//    var email by remember { mutableStateOf("") }
//    var senha by remember { mutableStateOf("") }
//    var errorMessage by remember { mutableStateOf<String?>(null) }
//    val scope = rememberCoroutineScope()
//
//    Surface(
//        modifier = Modifier.fillMaxSize(),
//        color = Color.White
//    ) {
//        Box(modifier = Modifier.fillMaxSize()) {
//            // Cantos verdes
//            Box(
//                modifier = Modifier
//                    .size(width = 105.dp, height = 25.dp)
//                    .background(greenColor, shape = RoundedCornerShape(bottomEnd = 16.dp))
//                    .align(Alignment.TopStart)
//            )
//            Box(
//                modifier = Modifier
//                    .size(width = 105.dp, height = 25.dp)
//                    .background(greenColor, shape = RoundedCornerShape(topStart = 16.dp))
//                    .align(Alignment.BottomEnd)
//            )
//
//            // Conteúdo principal
//            Column(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .padding(horizontal = 24.dp),
//                horizontalAlignment = Alignment.CenterHorizontally,
//                verticalArrangement = Arrangement.Center
//            ) {
//                // Logo
//                Image(
//                    painter = painterResource(id = R.drawable.logo_claro),
//                    contentDescription = "Logo Mobiliza",
//                    modifier = Modifier.height(150.dp)
//                )
//
//                Spacer(modifier = Modifier.height(16.dp))
//
//                Text(
//                    text = buildAnnotatedString {
//                        append("Bem-vindo ao ")
//                        withStyle(style = SpanStyle(color = greenColor, fontWeight = FontWeight.Bold)) {
//                            append("Mobiliza!")
//                        }
//                    },
//                    fontSize = 22.sp,
//                    fontWeight = FontWeight.Bold
//                )
//
//                Spacer(modifier = Modifier.height(4.dp))
//
//                Text(
//                    text = "Entre com seu email para acessar sua conta",
//                    fontSize = 14.sp,
//                    color = Color.Gray,
//                    textAlign = TextAlign.Center
//                )
//
//                Spacer(modifier = Modifier.height(24.dp))
//
//                // Campo Email
//                OutlinedTextField(
//                    value = email,
//                    onValueChange = { email = it },
//                    label = { Text("Email") },
//                    placeholder = { Text("Digite seu email") },
//                    leadingIcon = {
//                        Icon(
//                            painter = painterResource(id = R.drawable.fontisto_email),
//                            contentDescription = "Email",
//                            tint = Color.Unspecified,
//                            modifier = Modifier.size(20.dp)
//                        )
//                    },
//                    modifier = Modifier.fillMaxWidth()
//                )
//
//                Spacer(modifier = Modifier.height(12.dp))
//
//                // Campo Senha
//                Column(modifier = Modifier.fillMaxWidth()) {
//                    OutlinedTextField(
//                        value = senha,
//                        onValueChange = { senha = it },
//                        label = { Text("Senha") },
//                        placeholder = { Text("Digite sua senha") },
//                        visualTransformation = PasswordVisualTransformation(),
//                        leadingIcon = {
//                            Icon(
//                                painter = painterResource(id = R.drawable.lock),
//                                contentDescription = "Senha",
//                                tint = Color.Unspecified,
//                                modifier = Modifier.size(20.dp)
//                            )
//                        },
//                        modifier = Modifier.fillMaxWidth()
//                    )
//
//                    Spacer(modifier = Modifier.height(4.dp))
//
//                    TextButton(
//                        onClick = onForgotPasswordClick,
//                        modifier = Modifier.align(Alignment.End)
//                    ) {
//                        Text(
//                            text = "Esqueceu sua senha?",
//                            color = greenColor,
//                            fontSize = 13.sp,
//                            textDecoration = TextDecoration.Underline
//                        )
//                    }
//                }
//
//                Spacer(modifier = Modifier.height(20.dp))
//
//                // 🔹 BOTÃO LOGIN (com RetrofitFactory)
//                Button(
//                    onClick = {
//                        scope.launch {
//                            try {
//                                val authService = RetrofitFactory().getAuthService()
//                                val response = authService.login(LoginRequest(email, senha))
//
//                                if (response.token.isNotEmpty()) {
//                                    // Login OK → navega para Home
//                                    navegacao?.navigate("home") {
//                                        popUpTo("login") { inclusive = true }
//                                    }
//                                } else {
//                                    errorMessage = "Erro no login"
//                                }
//                            } catch (e: Exception) {
//                                errorMessage = "Usuário ou senha inválidos"
//                            }
//                        }
//                    },
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .height(48.dp),
//                    colors = ButtonDefaults.buttonColors(containerColor = greenColor),
//                    shape = RoundedCornerShape(8.dp)
//                ) {
//                    Text("Login", color = Color.White)
//                }
//
//                // Mensagem de erro
//                errorMessage?.let {
//                    Spacer(Modifier.height(8.dp))
//                    Text(it, color = Color.Red, fontSize = 14.sp)
//                }
//
//                Spacer(modifier = Modifier.height(16.dp))
//
//                // OU
//                Row(
//                    verticalAlignment = Alignment.CenterVertically,
//                    modifier = Modifier.fillMaxWidth()
//                ) {
//                    Divider(modifier = Modifier.weight(1f), color = Color.LightGray)
//                    Text(text = " OU ", color = Color.Gray, fontSize = 14.sp)
//                    Divider(modifier = Modifier.weight(1f), color = Color.LightGray)
//                }
//
//                Spacer(modifier = Modifier.height(16.dp))
//
//                // Botão Google
//                OutlinedButton(
//                    onClick = onGoogleClick,
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .height(48.dp),
//                    shape = RoundedCornerShape(8.dp)
//                ) {
//                    Icon(
//                        painter = painterResource(id = R.drawable.google),
//                        contentDescription = "Google",
//                        tint = Color.Unspecified,
//                        modifier = Modifier.size(24.dp)
//                    )
//                    Spacer(modifier = Modifier.width(8.dp))
//                    Text("Logue com Google", color = Color.Black)
//                }
//
//                Spacer(modifier = Modifier.height(20.dp))
//
//                // Link cadastro
//                TextButton(onClick = onRegisterClick) {
//                    Text("Não possui uma conta?", color = Color.Gray)
//                    Text("Cadastrar", color = greenColor)
//                }
//            }
//        }
//    }
//}
