package app.application.saludo.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// 🌙 Esquema oscuro (el que ya tenías)
private val NeonDarkScheme = darkColorScheme(
    background = DeepSpace,
    surface = Nebula,
    primary = NeonBlue,
    secondary = NeonCyan,
    tertiary = NeonPurple,
    onBackground = SoftWhite,
    onSurface = SoftWhite,
    onPrimary = Color.Black
)

// ☀️ Esquema claro (derivado, sin romper colores)
private val NeonLightScheme = lightColorScheme(
    background = Color(0xFFF4F6FF),
    surface = Color.White,
    primary = NeonBlue,
    secondary = NeonCyan,
    tertiary = NeonPurple,
    onBackground = Color.Black,
    onSurface = Color.Black,
    onPrimary = Color.Black
)

/**
 * 🎨 Tema principal de la app
 * - Detecta automáticamente modo oscuro del sistema
 * - No rompe diseño existente
 */
@Composable
fun SaludoTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {

    val colors = if (darkTheme) {
        NeonDarkScheme
    } else {
        NeonLightScheme
    }

    MaterialTheme(
        colorScheme = colors,
        typography = NeonTypography,
        content = content
    )
}
