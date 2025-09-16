package com.example.mobilizatcc.ui.theme

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import com.example.mobilizatcc.R
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun SplashScreen(
    navController: NavController? = null,  // opcional para preview
    onTimeout: () -> Unit = {}
) {
    val logoSizeDp = 300.dp
    val density = LocalDensity.current
    val logoSizePx = with(density) { logoSizeDp.toPx() }

    // Simula loading de 3 segundos antes de ir para a próxima tela
    LaunchedEffect(key1 = true) {
        delay(3000)
        onTimeout()
    }

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
            Canvas(modifier = Modifier.size(logoSizeDp * 1.6f)) {
                val centerX = size.width / 2
                val centerY = size.height / 2

                val baseRadius = logoSizePx / 2
                val radii = listOf(
                    baseRadius * 0.9f,
                    baseRadius * 1.1f,
                    baseRadius * 1.3f
                )

                radii.forEach { radius ->
                    drawCircle(
                        color = Color.LightGray,
                        radius = radius,
                        style = Stroke(width = 3f),
                        center = Offset(centerX, centerY)
                    )
                }
            }

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
    SplashScreen(onTimeout = {})
}
