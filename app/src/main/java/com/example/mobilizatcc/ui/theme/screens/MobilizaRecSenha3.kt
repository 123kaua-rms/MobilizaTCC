package com.example.mobilizatcc.ui.theme.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.mobilizatcc.R
import com.example.mobilizatcc.model.ResetSenhaRequest
import com.example.mobilizatcc.service.RetrofitFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun MobilizaRecSenha3(
    navegacao: NavHostController?,
    email: String,
    codigo: Int
) {
    val greenColor = Color(0xFF3AAA35)
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }
    var showConfirmPassword by remember { mutableStateOf(false) }
    var passwordStrength by remember { mutableStateOf("") }
    var passwordStrengthColor by remember { mutableStateOf(Color.Gray) }
    var loading by remember { mutableStateOf(false) }
    val context = LocalContext.current

    val senhasJaUsadas = listOf("mobiliza123", "senha123", "12345678")

    fun avaliarForcaSenha(senha: String) {
        when {
            senha.length < 6 -> {
                passwordStrength = "Senha fraca"
                passwordStrengthColor = Color.Red
            }
            senha.matches(Regex(".*[A-Z].*")) && senha.matches(Regex(".*[0-9].*")) && senha.length >= 8 -> {
                passwordStrength = "Senha forte"
                passwordStrengthColor = greenColor
            }
            senha.length >= 6 -> {
                passwordStrength = "Senha média"
                passwordStrengthColor = Color(0xFFFFA500)
            }
            else -> {
                passwordStrength = ""
                passwordStrengthColor = Color.Gray
            }
        }
    }

    Surface(modifier = Modifier.fillMaxSize(), color = Color.White) {
        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            val maxWidth = maxWidth
            val paddingHorizontal = if (maxWidth < 360.dp) 16.dp else 24.dp
            val iconSize = if (maxWidth < 360.dp) 100.dp else 140.dp
            val fontSizeTitle = if (maxWidth < 360.dp) 18.sp else 20.sp
            val fontSizeStrength = if (maxWidth < 360.dp) 12.sp else 14.sp
            val fieldHeight = if (maxWidth < 360.dp) 48.dp else 56.dp
            val buttonHeight = if (maxWidth < 360.dp) 45.dp else 50.dp
            val spacingSmall = if (maxWidth < 360.dp) 12.dp else 16.dp
            val spacingMedium = if (maxWidth < 360.dp) 24.dp else 32.dp
            val spacingLarge = if (maxWidth < 360.dp) 48.dp else 60.dp

            // Cantos verdes decorativos
            Box(
                modifier = Modifier
                    .size(width = 125.dp.coerceAtMost(maxWidth / 3), height = 45.dp)
                    .background(greenColor, shape = RoundedCornerShape(bottomEnd = 16.dp))
                    .align(Alignment.TopStart)
            )
            Box(
                modifier = Modifier
                    .size(width = 125.dp.coerceAtMost(maxWidth / 3), height = 45.dp)
                    .background(greenColor, shape = RoundedCornerShape(topStart = 16.dp))
                    .align(Alignment.BottomEnd)
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = paddingHorizontal),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Spacer(modifier = Modifier.height(spacingLarge))

                Image(
                    painter = painterResource(id = R.drawable.logo_claro),
                    contentDescription = "Logo Mobiliza",
                    modifier = Modifier.size(iconSize)
                )

                Spacer(modifier = Modifier.height(spacingMedium))

                Text(
                    text = "Digite sua nova senha",
                    fontSize = fontSizeTitle,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(spacingMedium))

                OutlinedTextField(
                    value = newPassword,
                    onValueChange = {
                        newPassword = it
                        avaliarForcaSenha(it)
                    },
                    placeholder = { Text("Nova senha") },
                    singleLine = true,
                    visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Password),
                    trailingIcon = {
                        val icon = if (showPassword) Icons.Default.VisibilityOff else Icons.Default.Visibility
                        IconButton(onClick = { showPassword = !showPassword }) {
                            Icon(icon, contentDescription = "Mostrar senha")
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(fieldHeight)
                )

                if (passwordStrength.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = passwordStrength,
                        color = passwordStrengthColor,
                        fontSize = fontSizeStrength,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Spacer(modifier = Modifier.height(spacingSmall))

                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    placeholder = { Text("Confirme a nova senha") },
                    singleLine = true,
                    visualTransformation = if (showConfirmPassword) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Password),
                    trailingIcon = {
                        val icon = if (showConfirmPassword) Icons.Default.VisibilityOff else Icons.Default.Visibility
                        IconButton(onClick = { showConfirmPassword = !showConfirmPassword }) {
                            Icon(icon, contentDescription = "Mostrar confirmação")
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(fieldHeight)
                )

                Spacer(modifier = Modifier.height(spacingMedium))

                Button(
                    onClick = {
                        when {
                            newPassword != confirmPassword -> {
                                Toast.makeText(context, "As senhas não coincidem", Toast.LENGTH_SHORT).show()
                                return@Button
                            }
                            newPassword in senhasJaUsadas -> {
                                Toast.makeText(context, "Esta senha já foi utilizada. Escolha outra.", Toast.LENGTH_SHORT).show()
                                return@Button
                            }
                            passwordStrength == "Senha fraca" -> {
                                Toast.makeText(context, "Escolha uma senha mais forte", Toast.LENGTH_SHORT).show()
                                return@Button
                            }
                        }

                        loading = true
                        val service = RetrofitFactory().getUsuarioService()
                        val request = ResetSenhaRequest(email, codigo, newPassword)

                        service.resetarSenha(request).enqueue(object : Callback<Void> {
                            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                                loading = false
                                if (response.isSuccessful) {
                                    Toast.makeText(context, "Senha alterada com sucesso!", Toast.LENGTH_SHORT).show()
                                    navegacao?.navigate("loguin")
                                } else {
                                    Toast.makeText(context, "Erro ao atualizar senha", Toast.LENGTH_SHORT).show()
                                }
                            }

                            override fun onFailure(call: Call<Void>, t: Throwable) {
                                loading = false
                                Toast.makeText(context, "Falha na conexão: ${t.message}", Toast.LENGTH_SHORT).show()
                            }
                        })
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(buttonHeight),
                    colors = ButtonDefaults.buttonColors(containerColor = greenColor),
                    shape = RoundedCornerShape(10.dp),
                    enabled = !loading
                ) {
                    Text(if (loading) "Enviando..." else "Enviar", color = Color.White)
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun MobilizaRecSenha3Preview() {
    MobilizaRecSenha3(navegacao = null, email = "teste@email.com", codigo = 123456)
}
