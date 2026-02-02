package app.application.utility.ui.navigation

sealed class Routes(val route: String) {
    // 🌌 Sistema
    object Splash : Routes("splash")
    object Selector : Routes("selector")

    // 🔐 Autenticación
    object Login : Routes("login")
    object Register : Routes("register")

    // 🏠 Herramientas (Utility)
    object Main : Routes("main")
    object Calculadora : Routes("calculadora")
    object Edad : Routes("edad")
    object Primo : Routes("primo")
    object MayorMenor : Routes("mayor_menor")
    object Saludo : Routes("saludo")

    // 🧴 Tienda
    object TiendaHome : Routes("tienda_home")
    object Cart : Routes("cart")
    object ProductDetail : Routes("product_detail/{productId}") {
        fun createRoute(productId: String): String = "product_detail/$productId"
    }

    // 📊 Administración
    object AdminProducts : Routes("admin_products") // Panel Central
    object AdminInventory : Routes("admin_inventory") // Inventario + Registro
    object SalesHistory : Routes("sales_history") // Historial
}