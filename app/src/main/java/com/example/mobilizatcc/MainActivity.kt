package com.example.mobilizatcc

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
                    startDestination = "linhas"
                ) {

                    // ---------- Onboarding / Welcome ----------
                    composable("welcome-1") { SplashScreen(navegacao) }
                    composable("welcome-2") { OnboardingScreen1(navegacao) }
                    composable("welcome-3") { MobilizaWelcome3(navegacao) }
                    composable("welcome-4") { MobilizaWelcome4(navegacao) }
                    composable("welcome-5") { OnboardingScreen5(navegacao) }

                    // ---------- Registro / Login ----------
                    composable("cadastro") { RegisterScreen(navegacao) }
                    composable("cadastro-google") { FinalRegisterScreen(navegacao) }
                    composable("loguin") { LoginScreen(navegacao) }

                    // ---------- Home ----------
                    composable("home") { MobilizaHomeScreen(navegacao) }

                    // ---------- Recuperar senha ----------
                    composable("recsenha1") { RecSenhaScreen(navegacao) }
                    composable(
                        route = "recsenha2/{email}",
                        arguments = listOf(navArgument("email") { type = NavType.StringType })
                    ) { backStackEntry ->
                        val email = backStackEntry.arguments?.getString("email") ?: ""
                        CodeVerificationScreen(navegacao, email)
                    }
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

                    // ---------- Linhas ----------
                    composable("linhas") { LinesScreen(navegacao) }

                    // ---------- Linha Tracado ----------
                    composable(
                        route = "linha-tracado/{routeId}/{routeShortName}",
                        arguments = listOf(
                            navArgument("routeId") { type = NavType.StringType },
                            navArgument("routeShortName") { type = NavType.StringType }
                        )
                    ) { backStackEntry ->
                        val routeId = backStackEntry.arguments?.getString("routeId") ?: ""
                        val routeShortName = backStackEntry.arguments?.getString("routeShortName") ?: ""
                        LinhaTracadoScreen(navegacao, routeId, routeShortName)
                    }

                    // ---------- Chat ----------
                    composable("chat") { ChatScreen(navegacao) }

                    // ---------- Linha Detalhes ----------
                    composable(
                        route = "gradehoraria/{linhaCodigo}",
                        arguments = listOf(
                            navArgument("linhaCodigo") { type = NavType.StringType }
                        )
                    ) { backStackEntry ->
                        val linhaCodigo = backStackEntry.arguments?.getString("linhaCodigo") ?: ""
                        LinhaDetalhesScreen(navegacao, linhaCodigo)
                    }

//                    // ---------- Feedback ----------
//                    composable("feedback") { FeedbackScreen(navegacao) }
//
//                    // ---------- Perfil ----------
//                    composable("perfil") { PerfilScreen(navegacao) }
                }
            }
        }
    }
}
