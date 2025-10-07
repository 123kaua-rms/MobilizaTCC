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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.mobilizatcc.R

@Composable
fun OnboardingScreen5(
    navegacao: NavHostController?,
) {
    val greenColor = Color(0xFF3AAA35)

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Imagem central
            Image(
                painter = painterResource(id = R.drawable.velocimentro),
                contentDescription = "Imagem de avaliação",
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .height(240.dp)
            )

            // Textos
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Dê sua opinião",
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color(0xFF202020),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Avalie e ajude a melhorar o transporte da sua cidade.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )
            }

            // Rodapé com indicadores e botões
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Botão "Pular" também leva para o cadastro
                TextButton(onClick = { navegacao?.navigate("cadastro") }) {
                    Text(text = "Pular", color = Color.Gray)
                }

                // Indicadores (quarta bolinha ativa)
                Row(horizontalArrangement = Arrangement.Center) {
                    repeat(3) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(Color.LightGray)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                    }

                    Box(
                        modifier = Modifier
                            .size(width = 20.dp, height = 6.dp)
                            .clip(RoundedCornerShape(50))
                            .background(greenColor)
                    )
                }

                // ✅ Botão verde que navega para a tela de cadastro
                IconButton(
                    onClick = {
                        navegacao?.navigate("cadastro")
                    },
                    modifier = Modifier
                        .size(40.dp)
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
fun OnboardingScreen5Preview() {
    OnboardingScreen5(navegacao = null)
}
