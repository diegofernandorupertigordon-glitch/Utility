package app.application.utility.ui.navigation

/**
 * 🧭 Rutas de navegación de la app
 */
sealed class Routes(val route: String) {

    // 🌌 Splash
    object Splash : Routes("splash")

    object Login : Routes("login")
    object Register : Routes("register")

    // 🏠 Menú principal
    object Main : Routes("main")

    // 📟 Calculadora
    object Calculadora : Routes("calculadora")

    // 👤 Verificador de edad
    object Edad : Routes("edad")

    // ⭐ Número primo
    object Primo : Routes("primo")

    // 🔼 Mayor y menor
    object MayorMenor : Routes("mayor_menor")

    // 👋 Saludo (NUEVO)
    object Saludo : Routes("saludo")
}
