package com.example.mobilizatcc.ui.theme.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.mobilizatcc.R

@Composable
fun MobilizaWelcome3(
    navegacao: NavHostController?,
    onNextClick: () -> Unit = {},
    onSkipClick: () -> Unit = {}
) {
    val greenColor = Color(0xFF16A34A)

    // 📐 Pega dimensões da tela
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp

    // 📊 Tamanhos responsivos
    val horizontalPadding = screenWidth * 0.06f
    val imageHeight = screenHeight * 0.35f
    val nextButtonSize = screenWidth * 0.12f

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = horizontalPadding, vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // 🖼️ Imagem central responsiva
            Image(
                painter = painterResource(id = R.drawable.relogio),
                contentDescription = "Imagem de relógio",
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .height(imageHeight)
            )

            // 📝 Textos
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Saiba quando seu transporte chega",
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color(0xFF202020),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Confira os horários do seu transporte.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )
            }

            // 🟢 Rodapé com indicadores e botões
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(onClick = { navegacao?.navigate("cadastro") }) {
                    Text(text = "Pular", color = Color.Gray)
                }

                // 🔘 Indicadores (3 passos — este é o segundo ativo)
                Row(horizontalArrangement = Arrangement.Center) {
                    // 1ª bolinha (inativa)
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(Color.LightGray)
                    )
                    Spacer(modifier = Modifier.width(6.dp))

                    // 2ª bolinha (ativa)
                    Box(
                        modifier = Modifier
                            .size(width = 20.dp, height = 6.dp)
                            .clip(RoundedCornerShape(50))
                            .background(greenColor)
                    )
                    Spacer(modifier = Modifier.width(6.dp))

                    // 3ª bolinha (inativa)
                    repeat(2) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(Color.LightGray)
                        )
                        if (it != 1) Spacer(modifier = Modifier.width(6.dp))
                    }
                }

                // ➡️ Botão circular responsivo
                IconButton(
                    onClick = { navegacao?.navigate("welcome-4") },
                    modifier = Modifier
                        .size(nextButtonSize)
                        .background(color = greenColor, shape = CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = "Próximo",
                        tint = Color.White
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MobilizaWelcome3Preview() {
    MobilizaWelcome3(navegacao = null)
}
