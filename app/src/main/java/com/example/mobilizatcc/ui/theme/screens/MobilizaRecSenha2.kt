package com.example.mobilizatcc.ui.theme.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
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
fun CodeVerificationScreen(
    navegacao: NavHostController?,
    email: String
) {
    val greenColor = Color(0xFF3AAA35)
    val codeLength = 6
    var code by remember { mutableStateOf(List(codeLength) { "" }) }
    var loading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    val context = LocalContext.current

    val focusRequesters = List(codeLength) { FocusRequester() }

    Surface(modifier = Modifier.fillMaxSize(), color = Color.White) {
        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            val maxWidth = maxWidth

            // Cantos verdes decorativos com tamanho adaptativo
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
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .padding(vertical = 16.dp)
                    .align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                Icon(
                    painter = painterResource(id = R.drawable.lock2),
                    contentDescription = "Cadeado",
                    tint = Color.Unspecified,
                    modifier = Modifier.size(if (maxWidth < 360.dp) 60.dp else 80.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Um código foi enviado ao seu e-mail!",
                    fontSize = if (maxWidth < 360.dp) 16.sp else 18.sp,
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Digite o código que enviamos para seu e-mail",
                    fontSize = if (maxWidth < 360.dp) 12.sp else 14.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(29.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    // Para centralizar os campos de código horizontalmente
                    Spacer(modifier = Modifier.weight(1f))
                    for (i in 0 until codeLength) {
                        OutlinedTextField(
                            value = code[i],
                            onValueChange = {
                                if (it.length <= 1 && it.all { c -> c.isDigit() }) {
                                    val newList = code.toMutableList()
                                    newList[i] = it
                                    code = newList
                                    if (it.isNotEmpty() && i < codeLength - 1) {
                                        focusRequesters[i + 1].requestFocus()
                                    }
                                }
                            },
                            singleLine = true,
                            textStyle = LocalTextStyle.current.copy(
                                fontSize = if (maxWidth < 360.dp) 16.sp else 20.sp,
                                textAlign = TextAlign.Center
                            ),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            visualTransformation = VisualTransformation.None,
                            modifier = Modifier
                                .size(if (maxWidth < 360.dp) 40.dp else 50.dp)
                                .focusRequester(focusRequesters[i]),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color.Gray,
                                unfocusedBorderColor = Color.LightGray,
                                focusedContainerColor = Color(0xFFC7C7C7),
                                unfocusedContainerColor = Color(0xFFC7C7C7),
                                cursorColor = Color.DarkGray,
                                focusedTextColor = Color.Black,
                                unfocusedTextColor = Color.Black
                            )
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (errorMessage.isNotEmpty()) {
                    Text(
                        errorMessage,
                        color = Color.Red,
                        fontSize = if (maxWidth < 360.dp) 12.sp else 14.sp,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        val typedCode = code.joinToString("").trim()
                        val codigoInt = typedCode.toIntOrNull()

                        if (typedCode.length != codeLength || codigoInt == null) {
                            errorMessage = "Código inválido."
                            return@Button
                        }

                        loading = true
                        errorMessage = ""

                        val service = RetrofitFactory().getUsuarioService()

                        val request = ResetSenhaRequest(
                            email = email,
                            codigo = codigoInt,
                            senha = "senhaTemporaria123"
                        )

                        service.resetarSenha(request).enqueue(object : Callback<Void> {
                            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                                loading = false
                                if (response.isSuccessful) {
                                    navegacao?.navigate("recsenha3/$email/$codigoInt")
                                } else {
                                    errorMessage = "Código incorreto. Tente novamente."
                                }
                            }

                            override fun onFailure(call: Call<Void>, t: Throwable) {
                                loading = false
                                Toast.makeText(
                                    context,
                                    "Falha na conexão: ${t.message}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        })
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(if (maxWidth < 360.dp) 45.dp else 50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = greenColor),
                    shape = RoundedCornerShape(8.dp),
                    enabled = !loading
                ) {
                    Text(if (loading) "Verificando..." else "Enviar", color = Color.White)
                }
            }
        }
    }
}
@Preview(showBackground = true)
@Composable
fun CodeVerificationScreenPreview() {
    CodeVerificationScreen(navegacao = null, email = "teste@email.com")
}
