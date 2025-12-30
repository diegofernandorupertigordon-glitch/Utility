// 📦 Paquete de componentes reutilizables de la UI
package app.application.utility.ui.components

// 🧱 Layouts básicos
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding

// 🎨 Componentes Material 3
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults

// 🔁 Composición
import androidx.compose.runtime.Composable

// 🎨 Utilidades visuales
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


/**
 * 🧩 BaseScreen
 *
 * Plantilla visual reutilizable para TODAS las pantallas de la app.
 * Proporciona:
 *  - Fondo futurista con degradado
 *  - TopBar centrado con título
 *  - Soporte para contenido dinámico
 *
 * @param title Título que se muestra en la barra superior
 * @param content Contenido específico de cada pantalla
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BaseScreen(
    title: String,
    content: @Composable (PaddingValues) -> Unit
) {

    // 🌌 Contenedor raíz que ocupa toda la pantalla
    Box(
        modifier = Modifier
            .fillMaxSize()

            // 🎨 Fondo futurista común para toda la app
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFF0A0F1F), // Azul oscuro superior
                        Color(0xFF101A2F), // Azul medio
                        Color(0xFF0A0F1F)  // Azul oscuro inferior
                    )
                )
            )
    ) {

        // 🧱 Estructura base Material (TopBar + contenido)
        Scaffold(

            // El fondo del Scaffold es transparente
            // porque el fondo real ya lo pinta el Box
            containerColor = Color.Transparent,

            // 🔝 Barra superior centrada
            topBar = {
                CenterAlignedTopAppBar(
                    title = {

                        // 📝 Título de la pantalla
                        Text(
                            text = title,
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    },

                    // 🎨 Color del TopAppBar
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = Color(0xFF0F172A)
                    )
                )
            }
        ) { paddingValues ->

            // 📦 Contenedor del contenido específico de la pantalla
            Column(
                modifier = Modifier
                    // Padding automático del Scaffold (TopBar)
                    .padding(paddingValues)

                    // Padding interno común para todas las pantallas
                    .padding(16.dp)
            ) {

                // 🧩 Renderiza el contenido que envía cada pantalla
                content(paddingValues)
            }
        }
    }
}
