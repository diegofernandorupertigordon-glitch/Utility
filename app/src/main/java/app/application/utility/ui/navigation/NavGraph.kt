package app.application.utility.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import app.application.utility.ui.screens.auth.LoginScreen
import app.application.utility.ui.screens.auth.RegisterScreen
import app.application.utility.ui.screens.calculadora.CalculadoraScreen
import app.application.utility.ui.screens.edad.EdadScreen
import app.application.utility.ui.screens.main.MainScreen
import app.application.utility.ui.screens.mayormenor.MayorMenorScreen
import app.application.utility.ui.screens.primo.PrimoScreen
import app.application.utility.ui.screens.saludo.SaludoScreen
import app.application.utility.ui.screens.selector.SelectorScreen
import app.application.utility.ui.screens.splash.SplashScreen
import app.application.utility.ui.screens.tienda.ManualScreen
import app.application.utility.ui.screens.tienda.admin.AdminMenuScreen
import app.application.utility.ui.screens.tienda.admin.AdminProductScreen
import app.application.utility.ui.screens.tienda.admin.SalesHistoryScreen
import app.application.utility.ui.screens.tienda.cart.CartScreen
import app.application.utility.ui.screens.tienda.cart.CartViewModel
import app.application.utility.ui.screens.tienda.detail.ProductDetailScreen
import app.application.utility.ui.screens.tienda.home.TiendaHomeScreen

@Composable
fun NavGraph(navController: NavHostController) {
    val cartViewModel: CartViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = Routes.Splash.route
    ) {
        // --- FLUJO INICIAL ---
        composable(Routes.Splash.route) { SplashScreen(navController) }
        composable(Routes.Selector.route) { SelectorScreen(navController) }

        // --- AUTH ---
        composable(Routes.Login.route) { LoginScreen(navController) }
        composable(Routes.Register.route) { RegisterScreen(navController) }

        // --- UTILITY ---
        composable(Routes.Main.route) { MainScreen(navController) }
        composable(Routes.Calculadora.route) { CalculadoraScreen() }
        composable(Routes.Edad.route) { EdadScreen() }
        composable(Routes.Primo.route) { PrimoScreen() }
        composable(Routes.MayorMenor.route) { MayorMenorScreen() }
        composable(Routes.Saludo.route) { SaludoScreen() }

        // --- TIENDA ---
        composable(Routes.TiendaHome.route) { TiendaHomeScreen(navController) }
        composable(Routes.Cart.route) { CartScreen(navController, cartViewModel) }

        // --- RUTA: MANUAL DE USUARIO ---
        composable(
            route = "${Routes.Manual.route}?seccion={seccion}",
            arguments = listOf(navArgument("seccion") { defaultValue = "MANUAL" })
        ) { backStackEntry ->
            val seccion = backStackEntry.arguments?.getString("seccion") ?: "MANUAL"
            ManualScreen(navController, seccion)
        }

        // --- SECCIÓN ADMIN ---
        // 1. Panel Central
        composable(Routes.AdminProducts.route) { AdminMenuScreen(navController) }

        // 2. Inventario y Registro Unificado (Ruta Blindada con ?)
        composable(
            route = "admin_inventory?screenType={screenType}",
            arguments = listOf(
                navArgument("screenType") {
                    type = NavType.StringType
                    defaultValue = "inventario"
                }
            )
        ) { backStackEntry ->
            val screenType = backStackEntry.arguments?.getString("screenType") ?: "inventario"
            AdminProductScreen(navController, screenType)
        }

        // 3. Historial de Ventas
        composable(Routes.SalesHistory.route) { SalesHistoryScreen(navController) }

        // --- DETALLE DE PRODUCTO ---
        composable(
            route = Routes.ProductDetail.route,
            arguments = listOf(navArgument("productId") { type = NavType.StringType })
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId") ?: ""
            ProductDetailScreen(navController, productId, cartViewModel)
        }
    }
}