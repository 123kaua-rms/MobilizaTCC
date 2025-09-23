package com.example.mobilizatcc.ui.theme.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mobilizatcc.R

@Composable
fun CodeVerificationScreen(
    onSendClick: () -> Unit = {}
) {
    val greenColor = Color(0xFF3AAA35)
    val codeLength = 5
    var code by remember { mutableStateOf(List(codeLength) { "" }) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Canto verde topo esquerdo
            Box(
                modifier = Modifier
                    .size(width = 105.dp, height = 25.dp)
                    .background(greenColor, shape = RoundedCornerShape(bottomEnd = 16.dp))
                    .align(Alignment.TopStart)
            )

            // Canto verde inferior direito
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
                verticalArrangement = Arrangement.Center
            ) {

                // Ícone cadeado
                Icon(
                    painter = painterResource(id = R.drawable.lock2),
                    contentDescription = "Cadeado",
                    tint = Color.Unspecified,
                    modifier = Modifier.size(80.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Título
                Text(
                    text = "Um código foi enviado ao seu e-mail!",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Subtítulo
                Text(
                    text = "Digite o código que enviamos para seu e-mail",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Campos do código
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    for (i in 0 until codeLength) {
                        OutlinedTextField(
                            value = code[i],
                            onValueChange = {
                                if (it.length <= 1) {
                                    val newList = code.toMutableList()
                                    newList[i] = it
                                    code = newList
                                }
                            },
                            singleLine = true,
                            textStyle = LocalTextStyle.current.copy(
                                fontSize = 20.sp,
                                textAlign = TextAlign.Center
                            ),
                            modifier = Modifier.size(50.dp), // sem border e sem cores customizadas
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number
                            ),
                            visualTransformation = VisualTransformation.None
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Botão enviar
                Button(
                    onClick = onSendClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = greenColor),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Enviar", color = Color.White)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CodeVerificationScreenPreview() {
    CodeVerificationScreen()
}

