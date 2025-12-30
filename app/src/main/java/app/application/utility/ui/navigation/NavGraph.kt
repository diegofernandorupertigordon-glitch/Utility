package app.application.utility.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import app.application.utility.ui.screens.auth.LoginScreen
import app.application.utility.ui.screens.auth.RegisterScreen
import app.application.utility.ui.screens.calculadora.CalculadoraScreen
import app.application.utility.ui.screens.edad.EdadScreen
import app.application.utility.ui.screens.main.MainScreen
import app.application.utility.ui.screens.mayormenor.MayorMenorScreen
import app.application.utility.ui.screens.primo.PrimoScreen
import app.application.utility.ui.screens.saludo.SaludoScreen
import app.application.utility.ui.screens.splash.SplashScreen

/**
 * 🗺️ Gráfico de navegación principal
 */
@Composable
fun NavGraph(navController: NavHostController) {

    NavHost(
        navController = navController,
        startDestination = Routes.Splash.route
    ) {

        // 🌌 Splash
        composable(Routes.Splash.route) {
            SplashScreen(navController)
        }

        // 🔐 Login
        composable(Routes.Login.route) {
            LoginScreen(navController)
        }

        // 📝 Registro
        composable(Routes.Register.route) {
            RegisterScreen(navController)
        }

        // 🏠 Menú principal
        composable(Routes.Main.route) {
            MainScreen(navController)
        }

        // 📟 Calculadora
        composable(Routes.Calculadora.route) {
            CalculadoraScreen()
        }

        // 👤 Edad
        composable(Routes.Edad.route) {
            EdadScreen()
        }

        // ⭐ Primo
        composable(Routes.Primo.route) {
            PrimoScreen()
        }

        // 🔼 Mayor o menor
        composable(Routes.MayorMenor.route) {
            MayorMenorScreen()
        }

        // 👋 Saludo
        composable(Routes.Saludo.route) {
            SaludoScreen()
        }
    }
}
