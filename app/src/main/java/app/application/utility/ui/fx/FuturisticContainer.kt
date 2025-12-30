
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import app.application.utility.ui.fx.AnimatedBackground
import app.application.utility.ui.fx.GlowOverlay
import app.application.utility.ui.fx.ParticleLayer

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun FuturisticContainer(
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFF020617),
                        Color(0xFF0F172A),
                        Color(0xFF020617)
                    )
                )
            )
    ) {

        AnimatedBackground()
        ParticleLayer()
        GlowOverlay()

        // 🧩 Contenido principal (NavGraph)
        content()
    }
}
