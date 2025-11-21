package com.example.mobilizatcc.ui.theme.components

import android.graphics.Color as AndroidColor
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.Dp

@Composable
fun RouteBadge(
    routeCode: String,
    routeColorHex: String? = null,
    modifier: Modifier = Modifier,
    maxWidth: Dp = 120.dp
) {
    val accentColor = parseRouteColor(routeColorHex)

    Column(
        modifier = modifier
            .defaultMinSize(minWidth = 68.dp)
            .widthIn(max = maxWidth)
            .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(8.dp))
            .background(Color.White, RoundedCornerShape(8.dp))
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 10.dp, vertical = 6.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.DirectionsBus,
                contentDescription = "Linha",
                tint = Color(0xFF1F2937),
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.size(6.dp))
            Text(
                text = routeCode,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = Color(0xFF1F2937),
                maxLines = 1
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .background(accentColor, RoundedCornerShape(bottomStart = 8.dp, bottomEnd = 8.dp))
        )
    }
}

private fun parseRouteColor(routeColorHex: String?): Color {
    val defaultColor = Color(0xFFFACC15)
    return runCatching {
        routeColorHex?.takeIf { it.isNotBlank() }?.let { hex ->
            val normalized = if (hex.startsWith("#")) hex else "#$hex"
            Color(AndroidColor.parseColor(normalized))
        } ?: defaultColor
    }.getOrElse { defaultColor }
}
