package app.application.utility.ui.screens.splash

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import app.application.utility.ui.navigation.Routes
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController) {

    // 🌀 Animaciones para un efecto de entrada cinemático
    val alpha = remember { Animatable(0f) }
    val scale = remember { Animatable(0.85f) }
    val NeonCyan = Color(0xFF00E5FF)

    LaunchedEffect(Unit) {
        // Pequeño delay inicial para estabilizar la vista
        delay(300)

        // Ejecución sincronizada de animaciones
        alpha.animateTo(1f, tween(1200))
        scale.animateTo(1f, tween(1200))

        // Tiempo de exposición de tu marca personal (Diego Ruperti)
        delay(1800)

        // Navegación fluida hacia el Selector Inteligente eliminando el Splash del historial
        navController.navigate(Routes.Selector.route) {
            popUpTo(Routes.Splash.route) { inclusive = true }
        }
    }

    // 🌌 Contenedor principal con Degradado Premium
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFFF8FAFC), Color(0xFFFFFFFF))
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(16.dp)
                .graphicsLayer(
                    alpha = alpha.value,
                    scaleX = scale.value,
                    scaleY = scale.value
                )
        ) {
            // 💎 Iniciales de Marca Personal: DR (Diego Ruperti)
            Text(
                text = "DR",
                fontSize = 86.sp, // Tamaño impactante y equilibrado
                fontWeight = FontWeight.Black,
                color = Color(0xFF2D3436),
                letterSpacing = 12.sp
            )

            // ✨ Subtítulo con acento Neón
            Text(
                text = "PREMIUM UTILITIES",
                fontSize = 14.sp,
                fontWeight = FontWeight.ExtraBold,
                color = NeonCyan,
                letterSpacing = 5.sp
            )

            Spacer(modifier = Modifier.height(60.dp))

            // 🔋 Indicador de carga minimalista
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "CARGANDO ECOSISTEMA",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray.copy(alpha = 0.4f),
                    letterSpacing = 3.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Barra de progreso estética estática (o sutil)
                Box(
                    modifier = Modifier
                        .height(2.dp)
                        .width(40.dp)
                        .background(NeonCyan.copy(alpha = 0.3f))
                )
            }
        }
    }
}