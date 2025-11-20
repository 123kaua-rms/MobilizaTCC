package com.example.mobilizatcc.ui.theme.screens

import android.util.Patterns
import android.widget.Toast
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
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
    val greenColor = Color(0xFF16A34A)
    val context = LocalContext.current

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp

    // Margens e espaçamentos adaptáveis
    val horizontalPadding = (screenWidth * 0.06f).coerceAtLeast(16.dp)
    val topSpacing = (screenHeight * 0.12f).coerceAtLeast(60.dp)
    val fieldSpacing = (screenHeight * 0.02f).coerceAtLeast(12.dp)

    var nome by remember { mutableStateOf("") }
    var usuario by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }

    var nomeError by remember { mutableStateOf<String?>(null) }
    var usuarioError by remember { mutableStateOf<String?>(null) }
    var emailError by remember { mutableStateOf<String?>(null) }
    var senhaError by remember { mutableStateOf<String?>(null) }
    var showPassword by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }

    // --- Funções de validação ---

    fun isValidGmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches() &&
                email.lowercase().endsWith("@gmail.com") &&
                email.length in 6..150 &&
                !email.matches(Regex("^\\d+$"))
    }

    fun validateUsuario(username: String): String? {
        val regex = Regex("^[A-Za-z0-9_-]{3,60}$")
        return when {
            username.isBlank() -> "O nome de usuário é obrigatório."
            username.length < 3 -> "O nome de usuário deve ter pelo menos 3 caracteres."
            username.length > 60 -> "O nome de usuário deve ter no máximo 60 caracteres."
            username.matches(Regex("^\\d+$")) -> "O nome de usuário não pode ser apenas numérico."
            !regex.matches(username) -> "Use apenas letras, números, underscore (_) ou hífen (-)."
            else -> null
        }
    }

    fun validateSenha(password: String): String? {
        return when {
            password.isBlank() -> "A senha é obrigatória."
            password.contains(" ") -> "A senha não deve conter espaços."
            password.length < 8 -> "A senha deve ter no mínimo 8 caracteres."
            password.length > 100 -> "A senha deve ter no máximo 100 caracteres."
            !password.matches(Regex(".*[!@#\$%^&*(),.?\":{}|<>_+=-].*")) ->
                "A senha deve conter pelo menos um caractere especial."
            password.any { it.isLetter() } && !password.matches(Regex(".*[A-Z].*")) ->
                "A senha deve conter pelo menos uma letra maiúscula."
            else -> null
        }
    }

    // --- Layout principal ---
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        Box(modifier = Modifier.fillMaxSize()) {

            // Top bar decorativa
            Box(
                modifier = Modifier
                    .width(screenWidth * 0.33f)
                    .height(screenHeight * 0.06f)
                    .background(greenColor, shape = RoundedCornerShape(bottomEnd = 16.dp))
                    .align(Alignment.TopStart)
            )

            // Bottom bar decorativa
            Box(
                modifier = Modifier
                    .width(screenWidth * 0.33f)
                    .height(screenHeight * 0.06f)
                    .background(greenColor, shape = RoundedCornerShape(topStart = 16.dp))
                    .align(Alignment.BottomEnd)
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = horizontalPadding),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Spacer(modifier = Modifier.height(topSpacing))

                Text(
                    text = buildAnnotatedString {
                        append("Bem-vindo ao ")
                        withStyle(style = SpanStyle(color = greenColor, fontWeight = FontWeight.Bold)) {
                            append("Mobiliza!")
                        }
                    },
                    fontSize = (screenWidth.value * 0.06f).sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Preencha seus dados para criar sua conta",
                    fontSize = (screenWidth.value * 0.035f).sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Nome
                OutlinedTextField(
                    value = nome,
                    onValueChange = {
                        nome = it
                        nomeError = when {
                            it.isBlank() -> "O nome é obrigatório."
                            it.length < 2 -> "O nome deve ter pelo menos 2 caracteres."
                            it.length > 120 -> "O nome deve ter no máximo 120 caracteres."
                            !it.matches(Regex("^[A-Za-zÀ-ÖØ-öø-ÿ ]+$")) -> "Use apenas letras e espaços."
                            else -> null
                        }
                    },
                    isError = nomeError != null,
                    label = { Text("Nome completo") },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.userr),
                            contentDescription = "Nome",
                            tint = Color.Unspecified,
                            modifier = Modifier.size(20.dp)
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                if (nomeError != null) {
                    Text(text = nomeError!!, color = Color.Red, fontSize = 12.sp)
                }

                Spacer(modifier = Modifier.height(fieldSpacing))

                // Usuário
                OutlinedTextField(
                    value = usuario,
                    onValueChange = {
                        usuario = it
                        usuarioError = validateUsuario(it)
                    },
                    isError = usuarioError != null,
                    label = { Text("Nome de usuário") },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.userr),
                            contentDescription = "Usuário",
                            tint = Color.Unspecified,
                            modifier = Modifier.size(20.dp)
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                if (usuarioError != null) {
                    Text(text = usuarioError!!, color = Color.Red, fontSize = 12.sp)
                }

                Spacer(modifier = Modifier.height(fieldSpacing))

                // Email
                OutlinedTextField(
                    value = email,
                    onValueChange = {
                        email = it
                        emailError = if (!isValidGmail(it) && it.isNotEmpty()) {
                            "Digite um e-mail Gmail válido."
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
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                if (emailError != null) {
                    Text(text = emailError!!, color = Color.Red, fontSize = 12.sp)
                }

                Spacer(modifier = Modifier.height(fieldSpacing))

                // Senha
                OutlinedTextField(
                    value = senha,
                    onValueChange = {
                        senha = it
                        senhaError = validateSenha(it)
                    },
                    isError = senhaError != null,
                    label = { Text("Senha") },
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
                            contentDescription = "Toggle password visibility",
                            modifier = Modifier
                                .size(24.dp)
                                .clickable { showPassword = !showPassword }
                        )
                    },
                    visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                if (senhaError != null) {
                    Text(text = senhaError!!, color = Color.Red, fontSize = 12.sp)
                }

                Spacer(modifier = Modifier.height(28.dp))

                // Botão cadastrar
                val allValid = nomeError == null && usuarioError == null && emailError == null && senhaError == null &&
                        nome.isNotBlank() && usuario.isNotBlank() && email.isNotBlank() && senha.isNotBlank()

                Button(
                    onClick = {
                        if (!allValid) {
                            Toast.makeText(context, "Preencha todos os campos corretamente.", Toast.LENGTH_SHORT).show()
                        } else {
                            isLoading = true
                            val usuarioService = RetrofitFactory().getUsuarioService()
                            val usuarioRequest = UsuarioRequest(
                                fotoUsuario = "foto_legal.png",
                                nomeUsuario = nome.trim(),
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
                                        if (response.isSuccessful) {
                                            Toast.makeText(context, "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT).show()
                                            navegacao?.navigate("loguin") {
                                                popUpTo("register") { inclusive = true }
                                            }
                                        } else {
                                            Toast.makeText(context, "Erro ao cadastrar. Tente novamente.", Toast.LENGTH_SHORT).show()
                                        }
                                    }

                                    override fun onFailure(call: Call<UsuarioResponse>, t: Throwable) {
                                        isLoading = false
                                        Toast.makeText(context, "Falha de conexão: ${t.message}", Toast.LENGTH_SHORT).show()
                                    }
                                })
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height((screenHeight * 0.065f).coerceAtLeast(45.dp)),
                    colors = ButtonDefaults.buttonColors(containerColor = greenColor),
                    shape = RoundedCornerShape(10.dp),
                    enabled = allValid && !isLoading
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
                        .height((screenHeight * 0.06f).coerceAtLeast(45.dp)),
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
