// 📦 Paquete principal de la aplicación
package app.application.utility

// 📱 Manejo del ciclo de vida de Android

// 🧩 Activity base para apps con Jetpack Compose

// 🎨 Permite definir la UI con Compose

// 🖥️ Permite usar la pantalla completa (edge-to-edge)

// 🔁 Permite usar funciones @Composable

// 🧭 Controlador de navegación para Compose

// 🎨 Tema personalizado de la app (Material 3)

// ✨ Contenedor visual futurista (fondo + efectos)

// 🗺️ Gráfico de navegación principal
import FuturisticContainer
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import app.application.saludo.ui.theme.SaludoTheme
import app.application.utility.ui.navigation.NavGraph


// 🚀 Activity principal (punto de entrada de la aplicación)
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 🖥️ Habilita diseño de pantalla completa (sin barras molestas)
        enableEdgeToEdge()

        // 🎨 Define la interfaz usando Jetpack Compose
        setContent {
            UtilityApp()
        }
    }
}


// 🧩 Composable raíz de TODA la aplicación
@Composable
fun UtilityApp() {

    // 🎨 Aplica el tema Material 3 a toda la app
    SaludoTheme {

        // 🧭 Controlador de navegación (maneja pantallas)
        val navController = rememberNavController()

        // 🌌 Contenedor visual global
        // - Fondo degradado
        // - Partículas
        // - Glow
        // - Contiene TODAS las pantallas
        FuturisticContainer {

            // 🗺️ Ejecuta el sistema de navegación
            // (Splash, Main, Calculadora, etc.)
            NavGraph(navController)
        }
    }
}
