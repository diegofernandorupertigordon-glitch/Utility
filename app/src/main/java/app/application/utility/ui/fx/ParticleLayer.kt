package app.application.utility.ui.fx

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import kotlin.random.Random

@Composable
fun ParticleLayer() {
    Canvas(modifier = Modifier.fillMaxSize()) {
        repeat(60) {
            val x = Random.nextFloat() * size.width
            val y = Random.nextFloat() * size.height
            val radius = Random.nextFloat() * 3f + 1f

            drawCircle(
                color = Color(0xFF38BDF8).copy(alpha = 0.3f),
                radius = radius,
                center = androidx.compose.ui.geometry.Offset(x, y)
            )
        }
    }
}
