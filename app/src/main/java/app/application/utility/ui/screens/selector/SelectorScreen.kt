package app.application.utility.ui.screens.selector

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import app.application.utility.ui.components.BaseScreen
import app.application.utility.ui.navigation.Routes
import app.application.utility.ui.screens.auth.AuthViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.firebase.auth.FirebaseAuth

@Composable
fun SelectorScreen(navController: NavController) {
    val auth = FirebaseAuth.getInstance()
    val user = auth.currentUser
    val authViewModel: AuthViewModel = viewModel()
    val context = LocalContext.current

    val userName = remember(user) {
        user?.email?.substringBefore("@")?.uppercase() ?: "INVITADO"
    }

    // Título vacío para eliminar la barra superior y la flecha
    BaseScreen(title = "", isDark = false) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            Text("BIENVENIDO,", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
            Text(userName, fontSize = 32.sp, fontWeight = FontWeight.Black, color = Color(0xFF2D3436))

            Spacer(modifier = Modifier.height(40.dp))

            // Tarjeta Utility - Aumentada a 180.dp
            LocalGlowImageCard(
                title = "HERRAMIENTAS UTILITY",
                subtitle = "Gestión y Cálculo",
                imageUrl = "https://images.unsplash.com/photo-1581092160562-40aa08e78837?q=80&w=800",
                neonColor = Color(0xFF00E5FF),
                onClick = { navController.navigate(Routes.Main.route) }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Tarjeta Tienda - Aumentada a 180.dp
            LocalGlowImageCard(
                title = "PERFUMERÍA INTEGRAL",
                subtitle = "Catálogo Exclusivo",
                imageUrl = "https://images.unsplash.com/photo-1541643600914-78b084683601?q=80&w=800",
                neonColor = Color(0xFFFFAB00),
                onClick = {
                    if (user == null) {
                        navController.navigate(Routes.Login.route)
                    } else {
                        navController.navigate(Routes.TiendaHome.route)
                    }
                }
            )

            Spacer(modifier = Modifier.weight(1f))

            if (user != null) {
                Button(
                    onClick = {
                        authViewModel.logout(context) {
                            navController.navigate(Routes.Selector.route) {
                                popUpTo(0) { inclusive = true }
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF5252).copy(alpha = 0.1f)),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp)
                ) {
                    Text("CERRAR SESIÓN", color = Color(0xFFFF5252), fontWeight = FontWeight.Bold)
                }
            } else {
                TextButton(onClick = { navController.navigate(Routes.Login.route) }) {
                    Text("INICIAR SESIÓN PARA COMPRAR", color = Color(0xFF00E5FF), fontWeight = FontWeight.Black)
                }
            }
        }
    }
}

@Composable
fun LocalGlowImageCard(title: String, subtitle: String, imageUrl: String, neonColor: Color, onClick: () -> Unit) {
    val context = LocalContext.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp) // Tamaño aumentado
            .clickable { onClick() },
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Optimización de carga: crossfade y caché
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(imageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            Box(modifier = Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(Color.Transparent, Color.Black.copy(0.75f)))))

            Column(modifier = Modifier.fillMaxSize().padding(20.dp), verticalArrangement = Arrangement.Bottom) {
                Box(modifier = Modifier.width(45.dp).height(4.dp).clip(RoundedCornerShape(2.dp)).background(neonColor))
                Spacer(modifier = Modifier.height(10.dp))
                Text(title, color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Black)
                Text(subtitle, color = Color.White.copy(alpha = 0.8f), fontSize = 13.sp)
            }
        }
    }
}