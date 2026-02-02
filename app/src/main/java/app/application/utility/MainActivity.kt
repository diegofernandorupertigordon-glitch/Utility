// 📦 Paquete principal de la aplicación
package app.application.utility

import FuturisticContainer
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import app.application.saludo.ui.theme.SaludoTheme
import app.application.utility.ui.navigation.NavGraph
import com.google.firebase.FirebaseApp   // 👈 IMPORTANTE

// 🚀 Activity principal (punto de entrada de la aplicación)
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 🔥 INICIALIZA FIREBASE (CLAVE)
        FirebaseApp.initializeApp(this)

        // 🖥️ Habilita diseño de pantalla completa
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

        // 🧭 Controlador de navegación
        val navController = rememberNavController()

        // 🌌 Contenedor visual global
        FuturisticContainer {

            // 🗺️ Navegación completa
            NavGraph(navController)
        }
    }
}
