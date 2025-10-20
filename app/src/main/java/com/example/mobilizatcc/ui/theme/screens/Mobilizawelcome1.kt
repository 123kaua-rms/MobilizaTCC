package com.example.mobilizatcc.ui.theme

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import kotlinx.coroutines.delay
import com.example.mobilizatcc.R
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun SplashScreen(
    navegacao: NavHostController?,
    onTimeout: () -> Unit = {}
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp

    // ✅ Logo será proporcional à largura da tela (ex: 50% da largura)
    val logoSizeDp = screenWidth * 0.5f

    val density = LocalDensity.current
    val logoSizePx = with(density) { logoSizeDp.toPx() }

    // ⏳ Timer para ir para próxima tela
    LaunchedEffect(Unit) {
        delay(4000)
        navegacao?.navigate("welcome-2")
        onTimeout()
    }

    // 🔁 Animações infinitas dos círculos
    val infiniteTransition = rememberInfiniteTransition(label = "circleAnimation")

    val scale by infiniteTransition.animateFloat(
        initialValue = 0.9f,
        targetValue = 1.3f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scaleAnim"
    )

    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alphaAnim"
    )

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        color = MaterialTheme.colorScheme.background
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            // 🎞️ Círculos animados ao redor do logo (também responsivos)
            Canvas(modifier = Modifier.size(logoSizeDp * 1.8f)) {
                val center = Offset(size.width / 2, size.height / 2)
                val baseRadius = logoSizePx / 2

                val radii = listOf(
                    baseRadius * (scale * 0.9f),
                    baseRadius * (scale * 1.1f),
                    baseRadius * (scale * 1.3f)
                )

                radii.forEach { radius ->
                    drawCircle(
                        color = Color.LightGray.copy(alpha = alpha),
                        radius = radius,
                        style = Stroke(width = 3f),
                        center = center
                    )
                }
            }

            // 🟢 Logo central responsivo
            Image(
                painter = painterResource(id = R.drawable.logo_claro),
                contentDescription = "Mobiliza Logo",
                modifier = Modifier.size(logoSizeDp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    SplashScreen(navegacao = null)
}
