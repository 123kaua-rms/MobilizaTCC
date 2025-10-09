package br.senai.sp.jandira.mobilizatcc

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.mobilizatcc.ui.theme.MobilizaTCCTheme
import com.example.mobilizatcc.ui.theme.SplashScreen
import com.example.mobilizatcc.ui.theme.screens.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MobilizaTCCTheme {
                val navegacao = rememberNavController()
                NavHost(
                    navController = navegacao,
                    startDestination = "welcome-1"
                ) {

                    // Onboarding / Welcome screens
                    composable("welcome-1") { SplashScreen(navegacao) }
                    composable("welcome-2") { OnboardingScreen1(navegacao) }
                    composable("welcome-3") { MobilizaWelcome3(navegacao) }
                    composable("welcome-4") { MobilizaWelcome4(navegacao) }
                    composable("welcome-5") { OnboardingScreen5(navegacao) }

                    // Registro / Login
                    composable("cadastro") { RegisterScreen(navegacao) }
                    composable("cadastro-google") { FinalRegisterScreen(navegacao) }
                    composable("loguin") { LoginScreen(navegacao) }

                    // Home
                    composable("home") { MobilizaHomeScreen(navegacao) }

                    // Recuperar senha - Tela 1 (início)
                    composable("recsenha1") { RecSenhaScreen(navegacao) }

                    // Recuperar senha - Tela 2 (verificação de código)
                    composable(
                        route = "recsenha2/{email}",
                        arguments = listOf(navArgument("email") { type = NavType.StringType })
                    ) { backStackEntry ->
                        val email = backStackEntry.arguments?.getString("email") ?: ""
                        CodeVerificationScreen(navegacao, email)
                    }

                    // Recuperar senha - Tela 3 (resetar senha)
                    composable(
                        route = "recsenha3/{email}/{codigo}",
                        arguments = listOf(
                            navArgument("email") { type = NavType.StringType },
                            navArgument("codigo") { type = NavType.IntType }
                        )
                    ) { backStackEntry ->
                        val email = backStackEntry.arguments?.getString("email") ?: ""
                        val codigo = backStackEntry.arguments?.getInt("codigo") ?: 0
                        MobilizaRecSenha3(navegacao, email, codigo)
                    }





                    composable("linhas") { LinesScreen(navegacao) }

                }
            }
        }
    }
}
