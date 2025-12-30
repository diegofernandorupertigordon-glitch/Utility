package app.application.utility.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp

/**
 * 🧊 CardContainer (Glass limpio – SIN blur)
 *
 * ✔ Texto nítido
 * ✔ Fondo translúcido
 * ✔ Glow neón
 * ✔ Sin desenfoque
 * ✔ Rendimiento óptimo
 * ✔ Compatible con TODO tu código
 */
@Composable
fun CardContainer(
    content: @Composable () -> Unit
) {

    // 🌈 Borde con efecto glow neón
    val glowBorder = Brush.horizontalGradient(
        listOf(
            MaterialTheme.colorScheme.primary,
            MaterialTheme.colorScheme.secondary
        )
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()

            // 🌈 Glow exterior
            .border(
                width = 1.5.dp,
                brush = glowBorder,
                shape = RoundedCornerShape(24.dp)
            )

            // 🪟 Fondo translúcido tipo vidrio (SIN blur)
            .background(
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.85f),
                shape = RoundedCornerShape(24.dp)
            )

            // 📦 Padding interno
            .padding(20.dp)
    ) {
        content()
    }
}
