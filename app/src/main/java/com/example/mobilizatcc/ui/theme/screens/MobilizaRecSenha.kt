package com.example.mobilizatcc.ui.theme.screens

import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.Image
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.mobilizatcc.R
import com.example.mobilizatcc.model.RecSenhaRequest
import com.example.mobilizatcc.service.RetrofitFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun RecSenhaScreen(
    navegacao: NavHostController?
) {
    val greenColor = Color(0xFF3AAA35)
    var email by remember { mutableStateOf("") }
    val context = LocalContext.current
    var loading by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        Box(modifier = Modifier.fillMaxSize()) {

            // Cantos verdes
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

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                Image(
                    painter = painterResource(id = R.drawable.lock2),
                    contentDescription = "Ícone de segurança",
                    modifier = Modifier.size(120.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Problemas para entrar?",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Insira o e-mail da conta que deseja recuperar",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "E-mail",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 4.dp)
                )

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    placeholder = { Text("Digite seu e-mail") },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.fontisto_email),
                            contentDescription = "E-mail",
                            tint = greenColor,
                            modifier = Modifier.size(24.dp)
                        )
                    },
                    visualTransformation = VisualTransformation.None,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        if (email.isBlank() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                            Toast.makeText(context, "Informe um e-mail válido", Toast.LENGTH_SHORT).show()
                            return@Button
                        }

                        loading = true
                        val service = RetrofitFactory().getUsuarioService()
                        val request = RecSenhaRequest(email)

                        service.recuperarSenha(request).enqueue(object : Callback<Void> {
                            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                                loading = false
                                if (response.isSuccessful) {
                                    Toast.makeText(context, "Código enviado para seu email", Toast.LENGTH_SHORT).show()
                                    navegacao?.navigate("recsenha2/${email}")
                                } else {
                                    Toast.makeText(context, "Erro ao enviar email: ${response.code()}", Toast.LENGTH_SHORT).show()
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
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = greenColor),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(if (loading) "Enviando..." else "Enviar", color = Color.White)
                }

                Spacer(modifier = Modifier.height(24.dp))

                TextButton(onClick = { navegacao?.navigate("loguin") }) {
                    Text("Voltar ao ", color = Color.Gray)
                    Text("Login", color = greenColor)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RecSenhaScreenPreview() {
    RecSenhaScreen(navegacao = null)
}
