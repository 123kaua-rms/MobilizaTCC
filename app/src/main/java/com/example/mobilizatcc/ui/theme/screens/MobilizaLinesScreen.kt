package com.example.mobilizatcc.ui.theme.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.mobilizatcc.R
import com.example.mobilizatcc.model.BusLineResponse

@Composable
fun LinesScreen(navegacao: NavHostController?) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabs = listOf("Ônibus", "Metrô", "Trem")

    // 🔹 Linhas mockadas (iguais às da SPTrans)
    val mockLines = remember {
        listOf(
            BusLineResponse("1001", "SPTRANS", "701A", "Terminal Pirituba - Praça Ramos", 3, "FF5733", "FFFFFF"),
            BusLineResponse("1002", "SPTRANS", "809P", "Terminal Lapa - Pq. São Domingos", 3, "33AFFF", "000000"),
            BusLineResponse("1003", "SPTRANS", "177Y", "Metrô Santana - USP", 3, "4CAF50", "FFFFFF"),
            BusLineResponse("1004", "SPTRANS", "875P", "Terminal Pinheiros - Lapa", 3, "F4B400", "000000"),
            BusLineResponse("1005", "SPTRANS", "5109", "Term. Mercado - Jabaquara", 3, "9C27B0", "FFFFFF")
        )
    }

    val filteredLines = remember(searchQuery) {
        if (searchQuery.isEmpty()) mockLines
        else mockLines.filter {
            it.routeShortName.contains(searchQuery, ignoreCase = true) ||
                    it.routeLongName.contains(searchQuery, ignoreCase = true)
        }
    }

    Scaffold(
        bottomBar = { BottomNavigationBar(navegacao = navegacao, selectedRoute = "linhas") }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color.White)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp)
            ) {
                Header()
                Spacer(modifier = Modifier.height(20.dp))
                SearchField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it }
                )
                Spacer(modifier = Modifier.height(24.dp))
                TransportTabs(
                    selectedTabIndex = selectedTabIndex,
                    onTabSelected = { selectedTabIndex = it },
                    tabs = tabs
                )
                Spacer(modifier = Modifier.height(24.dp))

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp)
                ) {
                    items(filteredLines) { line ->
                        BusLineItem(line)
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun Header() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp),
        contentAlignment = Alignment.TopStart
    ) {
        Image(
            painter = painterResource(id = R.drawable.perfilcinza),
            contentDescription = "Usuário",
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
        )
    }
}

@Composable
fun SearchField(value: String, onValueChange: (String) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth(0.95f)
            .height(54.dp)
            .clip(RoundedCornerShape(14.dp))
            .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(14.dp))
            .background(Color(0xFFF7F7F7)),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = value,
                onValueChange = onValueChange,
                placeholder = { Text("Pesquise por uma linha", color = Color.Gray) },
                modifier = Modifier
                    .weight(1f)
                    .background(Color.Transparent)
                    .padding(start = 4.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = Color(0xFF4CAF50)
                ),
                textStyle = LocalTextStyle.current.copy(fontSize = 15.sp)
            )
            Box(
                modifier = Modifier
                    .size(54.dp)
                    .clip(RoundedCornerShape(topEnd = 14.dp, bottomEnd = 14.dp))
                    .background(Color(0xFF4CAF50)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Buscar",
                    tint = Color.White,
                    modifier = Modifier.size(26.dp)
                )
            }
        }
    }
}

@Composable
fun TransportTabs(selectedTabIndex: Int, onTabSelected: (Int) -> Unit, tabs: List<String>) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Icon(
            imageVector = Icons.Default.Star,
            contentDescription = "Favoritos",
            tint = Color.Gray,
            modifier = Modifier.size(22.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        TabRow(
            selectedTabIndex = selectedTabIndex,
            containerColor = Color.Transparent,
            contentColor = Color(0xFF4CAF50),
            modifier = Modifier.fillMaxWidth(),
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                    height = 3.dp,
                    color = Color(0xFF4CAF50)
                )
            }
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { onTabSelected(index) },
                    text = {
                        Text(
                            text = title,
                            fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 16.sp
                        )
                    },
                    selectedContentColor = Color(0xFF4CAF50),
                    unselectedContentColor = Color.Gray
                )
            }
        }
    }
}

@Composable
fun BusLineItem(line: BusLineResponse) {
    Row(
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
            .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(12.dp))
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .width(80.dp)
                .clip(RoundedCornerShape(8.dp))
                .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(8.dp))
                .background(Color.White),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.padding(vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.DirectionsBus,
                    contentDescription = "Ícone de ônibus",
                    tint = Color.DarkGray,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = line.routeShortName,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = Color.Black
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(5.dp)
                    .background(Color(android.graphics.Color.parseColor("#${line.routeColor}")))
            )
        }
        Spacer(modifier = Modifier.width(20.dp))
        Text(
            text = line.routeLongName,
            fontSize = 14.sp,
            color = Color.DarkGray,
            lineHeight = 20.sp,
            modifier = Modifier.weight(1f)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LinesScreenPreview() {
    LinesScreen(navegacao = rememberNavController())
}
