package br.senai.sp.jandira.projeto_travello

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mobilizatcc.ui.theme.MobilizaTCCTheme
import com.example.mobilizatcc.ui.theme.screens.FinalRegisterScreen
import com.example.mobilizatcc.ui.theme.screens.LoginScreen
import com.example.mobilizatcc.ui.theme.screens.RegisterScreen


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MobilizaTCCTheme {
                val navegacao = rememberNavController()
                NavHost(
                    navController = navegacao,
                    startDestination = "loguin"
                ) {
                    composable(route = "cadastro") {
                        RegisterScreen(navegacao)
                    }
                    composable(route = "cadastro-google") {
                        FinalRegisterScreen(navegacao)
                    }
                    composable(route = "loguin") {
                        LoginScreen(navegacao)
                    }

                }
            }
        }
    }
}