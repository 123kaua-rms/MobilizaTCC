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
import com.example.mobilizatcc.utils.UserSessionManager

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
                    // ... rotas existentes (mantidas)

                    // Registro / Login
                    composable("cadastro") { RegisterScreen(navegacao) }
                    composable("cadastro-google") { FinalRegisterScreen(navegacao) }
                    composable("loguin") { LoginScreen(navegacao) }

                    // Home
                    composable("home") { MobilizaHomeScreen(navegacao) }

                    // Perfil (nova rota)
                    composable("perfil") { PerfilScreen(navegacao) }

                    // Recuperar senha, linhas, etc. (mantidos)
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

                    // Linhas
                    composable("linhas") { LinesScreen(navegacao) }

                    // Linha Tracado
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

                    // Chat por linha
                    composable(
                        route = "chat/{routeId}/{routeShortName}/{routeDescription}",
                        arguments = listOf(
                            navArgument("routeId") { type = NavType.StringType },
                            navArgument("routeShortName") { type = NavType.StringType },
                            navArgument("routeDescription") { type = NavType.StringType }
                        )
                    ) { backStackEntry ->
                        val routeId = backStackEntry.arguments?.getString("routeId") ?: ""
                        val routeShortName = backStackEntry.arguments?.getString("routeShortName") ?: ""
                        val routeDescription = backStackEntry.arguments?.getString("routeDescription") ?: ""
                        ChatScreen(
                            navegacao = navegacao,
                            routeId = routeId,
                            routeShortName = routeShortName,
                            routeDescription = routeDescription
                        )
                    }

                    // Feedback
                    composable("feedback") { FeedbacksScreen(navegacao) }
                    composable("feedback-form") { FeedbackFormScreen(navegacao) }

                    // Linha Detalhes
                    composable(
                        route = "gradehoraria/{routeId}/{linhaCodigo}",
                        arguments = listOf(
                            navArgument("routeId") { type = NavType.StringType },
                            navArgument("linhaCodigo") { type = NavType.StringType }
                        )
                    ) { backStackEntry ->
                        val routeId = backStackEntry.arguments?.getString("routeId") ?: ""
                        val linhaCodigo = backStackEntry.arguments?.getString("linhaCodigo") ?: ""
                        LinhaDetalhesScreen(navegacao, routeId, linhaCodigo)
                    }
                }
            }
        }
    }
}
