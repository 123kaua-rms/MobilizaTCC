//package br.senai.sp.jandira.projeto_travello
//
//import android.os.Bundle
//import androidx.activity.ComponentActivity
//import androidx.activity.compose.setContent
//import androidx.navigation.compose.NavHost
//import androidx.navigation.compose.composable
//import androidx.navigation.compose.rememberNavController
//import com.example.mobilizatcc.ui.theme.MobilizaTCCTheme
//import com.example.mobilizatcc.ui.theme.screens.FinalRegisterScreen
//import com.example.mobilizatcc.ui.theme.screens.LoginScreen
//import com.example.mobilizatcc.ui.theme.screens.MobilizaCadastroGoogleScreen
//import com.example.mobilizatcc.ui.theme.screens.RegisterScreen
//import com.example.mobilizatcc.ui.theme.screens.mobilizaCadastroScreen
//
//class MainActivity : ComponentActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContent {
//            MobilizaTCCTheme {
//                val navegacao = rememberNavController()
//
//                NavHost(
//                    navController = navegacao,
//                    startDestination = "cadastro"
//                ) {
//                    // Tela de cadastro normal
//                    composable(route = "cadastro") {
//                        RegisterScreen(
//                            navegacao = navegacao,
//                            onRegisterClick = {
//                                // depois do cadastro, manda para login
//                                navegacao.navigate("login")
//                            },
//                            onLoginClick = {
//                                // se clicar em "Entrar", vai direto para login
//                                navegacao.navigate("login")
//                            }
//                        )
//                    }
//
//                    // Tela de login
//                    composable(route = "login") {
//                        LoginScreen(navegacao)
//                    }
//
//                    // Tela de cadastro com Google
//                    composable(route = "cadastro_google") {
//                        FinalRegisterScreen(
//                            navegacao = navegacao,
//                            onRegisterClick = {
//                                // depois do cadastro com Google, manda para login
//                                navegacao.navigate("login")
//                            },
//                            onGoogleClick = {
//                                // lógica de login com Google
//                            },
//                            onLoginClick = {
//                                navegacao.navigate("login")
//                            }
//                        )
//                    }
//                }
//            }
//        }
//    }
//}
