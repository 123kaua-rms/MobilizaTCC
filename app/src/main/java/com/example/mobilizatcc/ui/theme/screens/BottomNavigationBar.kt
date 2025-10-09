package com.example.mobilizatcc.ui.theme.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.mobilizatcc.R

@Composable
fun BottomNavigationBar(
    navegacao: NavHostController?,
    selectedRoute: String = "home"
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        BottomMenuItem(
            iconId = R.drawable.home,
            label = "Início",
            selected = selectedRoute == "home",
            onClick = { navegacao?.navigate("home") }
        )
        BottomMenuItem(
            iconId = R.drawable.linhas,
            label = "Linhas",
            selected = selectedRoute == "linhas",
            onClick = { navegacao?.navigate("linhas") }
        )
        BottomMenuItem(
            iconId = R.drawable.feedback,
            label = "Feedback",
            selected = selectedRoute == "feedback",
            onClick = { navegacao?.navigate("feedback") }
        )
        BottomMenuItem(
            iconId = R.drawable.telaperfil,
            label = "Perfil",
            selected = selectedRoute == "perfil",
            onClick = { navegacao?.navigate("perfil") }
        )
    }
}

@Composable
fun BottomMenuItem(iconId: Int, label: String, selected: Boolean, onClick: () -> Unit) {
    val greenColor = Color(0xFF3AAA35)
    val grayColor = Color(0xFF9E9E9E)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {
        Icon(
            painter = painterResource(id = iconId),
            contentDescription = label,
            tint = if (selected) greenColor else grayColor,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            fontSize = 12.sp,
            color = if (selected) greenColor else grayColor
        )
    }
}
