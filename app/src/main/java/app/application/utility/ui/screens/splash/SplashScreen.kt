// 📦 Paquete donde vive esta pantalla
package app.application.utility.ui.screens.splash

// 🎬 Animaciones
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween

// 📐 Layout
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding

// 🎨 UI
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

// 🧭 Navegación
import androidx.navigation.NavController
import app.application.utility.ui.navigation.Routes

// 🔐 Firebase Auth
import com.google.firebase.auth.FirebaseAuth

// 🕒 Delay
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController) {

    // 🔵 Opacidad (fade in)
    val alpha = remember { Animatable(0f) }

    // 🔍 Escala suave (zoom)
    val scale = remember { Animatable(0.9f) }

    // 🔐 Firebase Auth
    val auth = FirebaseAuth.getInstance()

    LaunchedEffect(Unit) {

        // 🎬 Animación combinada (fade + zoom)
        alpha.animateTo(1f, tween(1200))
        scale.animateTo(1f, tween(1200))

        // ⏱️ Tiempo visible del splash
        delay(2500)

        // 🧠 Usuario actual
        val user = auth.currentUser

        // 🚦 Navegación inteligente
        if (user != null) {
            // ✅ Usuario logueado → Main
            navController.navigate(Routes.Main.route) {
                popUpTo(Routes.Splash.route) { inclusive = true }
            }
        } else {
            // 🚪 Usuario NO logueado → Login
            navController.navigate(Routes.Login.route) {
                popUpTo(Routes.Splash.route) { inclusive = true }
            }
        }
    }

    // 🌌 Fondo futurista
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFF0A0F1F),
                        Color(0xFF101A2F),
                        Color(0xFF0A0F1F)
                    )
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

            // 🚀 Nombre de la App
            Text(
                text = "Utility App",
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF64FFDA)
            )

            // ✨ Subtítulo futurista
            Text(
                text = "Tecnología que simplifica tu mundo",
                fontSize = 14.sp,
                color = Color(0xFF9AEFFF)
            )
        }
    }
}
