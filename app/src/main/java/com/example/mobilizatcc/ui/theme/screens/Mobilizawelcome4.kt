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
fun MobilizaWelcome4(
    navegacao: NavHostController?,
) {
    val greenColor = Color(0xFF3AAA35)

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp

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

            Image(
                painter = painterResource(id = R.drawable.homens),
                contentDescription = "Imagem de passageiros conversando",
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .height(imageHeight)
            )

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Converse com outros passageiros",
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color(0xFF202020),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Compartilhe informações e receba dicas sobre sua linha.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(
                    onClick = {
                        navegacao?.navigate("cadastro")
                    }
                ) {
                    Text(text = "Pular", color = Color.Gray)
                }

                Row(horizontalArrangement = Arrangement.Center) {
                    repeat(2) {
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

                    Spacer(modifier = Modifier.width(6.dp))

                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(Color.LightGray)
                    )
                }

                IconButton(
                    onClick = {
                        navegacao?.navigate("welcome-5")
                    },
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
fun MobilizaWelcome4Preview() {
    MobilizaWelcome4(navegacao = null)
}
