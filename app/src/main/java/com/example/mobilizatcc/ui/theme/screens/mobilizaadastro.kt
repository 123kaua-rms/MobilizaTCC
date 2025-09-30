package com.example.mobilizatcc.ui.theme.screens

import android.util.Log
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

    var toastShown by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier.fillMaxSize(),
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

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {

                Spacer(modifier = Modifier.height(129.dp))

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

                Spacer(modifier = Modifier.height(9.dp))

                Text(
                    text = "Faça seu cadastro para ter acesso sua conta",
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
                    leadingIcon = { Icon(painter = painterResource(id = R.drawable.user), contentDescription = "Nome", tint = Color.Unspecified, modifier = Modifier.size(20.dp)) },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(18.dp))

                OutlinedTextField(
                    value = usuario,
                    onValueChange = { usuario = it },
                    label = { Text("Nome de usuário") },
                    leadingIcon = { Icon(painter = painterResource(id = R.drawable.user), contentDescription = "Usuário", tint = Color.Unspecified, modifier = Modifier.size(20.dp)) },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(18.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    leadingIcon = { Icon(painter = painterResource(id = R.drawable.fontisto_email), contentDescription = "Email", tint = Color.Unspecified, modifier = Modifier.size(20.dp)) },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(18.dp))

                OutlinedTextField(
                    value = senha,
                    onValueChange = { senha = it },
                    label = { Text("Senha") },
                    leadingIcon = { Icon(painter = painterResource(id = R.drawable.lock), contentDescription = "Senha", tint = Color.Unspecified, modifier = Modifier.size(20.dp)) },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(25.dp))

                // Botão Cadastrar
                Button(
                    onClick = {
                        if (!toastShown) {
                            toastShown = true
                            val usuarioService = RetrofitFactory().getUsuarioService()
                            val usuarioRequest = UsuarioRequest(
                                foto = "foto_legal.png",
                                nome = nome,
                                username = usuario,
                                email = email,
                                senha = senha
                            )

                            // Log do JSON que será enviado
                            Log.d("RegisterScreen", "JSON enviado: $usuarioRequest")

                            usuarioService.registerUser(usuarioRequest)
                                .enqueue(object : Callback<UsuarioResponse> {
                                    override fun onResponse(call: Call<UsuarioResponse>, response: Response<UsuarioResponse>) {
                                        Log.d("RegisterScreen", "Código HTTP: ${response.code()}")
                                        Log.d("RegisterScreen", "Body da resposta: ${response.body()}")

                                        if (!response.isSuccessful) {
                                            val errorBody = response.errorBody()?.string()
                                            Log.e("RegisterScreen", "Erro do servidor: $errorBody")
                                            Toast.makeText(context, "Erro: ${response.code()}", Toast.LENGTH_SHORT).show()
                                        } else {
                                            Toast.makeText(context, "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT).show()
                                            onRegisterClick()
                                        }
                                        toastShown = false
                                    }

                                    override fun onFailure(call: Call<UsuarioResponse>, t: Throwable) {
                                        Log.e("RegisterScreen", "Falha na requisição", t)
                                        Toast.makeText(context, "Falha: ${t.message}", Toast.LENGTH_SHORT).show()
                                        toastShown = false
                                    }
                                })
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = greenColor),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Cadastrar", color = Color.White)
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

                // Google login
                OutlinedButton(
                    onClick = onGoogleClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(painter = painterResource(id = R.drawable.google), contentDescription = "Google", tint = Color.Unspecified, modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Logue com Google", color = Color.Black)
                }

                Spacer(modifier = Modifier.height(10.dp))

                TextButton(onClick = onLoginClick) {
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
