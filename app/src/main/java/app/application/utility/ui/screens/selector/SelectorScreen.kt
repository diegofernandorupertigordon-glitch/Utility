package app.application.utility.ui.screens.selector

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
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
    val NeonCyan = Color(0xFF00E5FF)

    // Lógica para obtener el nombre de usuario limpio
    val userName = remember(user) {
        user?.email?.substringBefore("@")?.uppercase() ?: "INVITADO"
    }

    BaseScreen(title = "", isDark = false) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.verticalGradient(listOf(Color(0xFFF8FAFC), Color.White)))
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(54.dp))

            // --- HEADER DE BIENVENIDA PREMIUM ---
            Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.Start) {
                Text(
                    "BIENVENIDO,",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Black,
                    color = NeonCyan,
                    letterSpacing = 2.sp
                )
                Text(
                    userName,
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Black,
                    color = Color(0xFF2D3436),
                    lineHeight = 40.sp
                )
            }

            Spacer(modifier = Modifier.height(44.dp))

            // 🛠️ Tarjeta Utility
            LocalGlowImageCard(
                title = "HERRAMIENTAS UTILITY",
                subtitle = "Gestión y Cálculo Profesional",
                imageUrl = "https://images.unsplash.com/photo-1581092160562-40aa08e78837?q=80&w=800",
                neonColor = NeonCyan,
                onClick = { navController.navigate(Routes.Main.route) }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // 🧴 Tarjeta Tienda (Redirige a Login si no está autenticado)
            LocalGlowImageCard(
                title = "PERFUMERÍA INTEGRAL",
                subtitle = "Catálogo de Lujo Exclusivo",
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

            // --- ACCIÓN DE SESIÓN INFERIOR ---
            if (user != null) {
                Surface(
                    onClick = {
                        authViewModel.logout(context) {
                            // Reiniciamos el estado volviendo al selector limpio
                            navController.navigate(Routes.Selector.route) {
                                popUpTo(0) { inclusive = true }
                            }
                        }
                    },
                    color = Color(0xFFFF5252).copy(alpha = 0.08f),
                    shape = RoundedCornerShape(22.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 40.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(18.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(Icons.Default.Logout, null, tint = Color(0xFFFF5252), modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            "CERRAR SESIÓN",
                            color = Color(0xFFFF5252),
                            fontWeight = FontWeight.Black,
                            fontSize = 13.sp,
                            letterSpacing = 1.sp
                        )
                    }
                }
            } else {
                TextButton(
                    onClick = { navController.navigate(Routes.Login.route) },
                    modifier = Modifier.padding(bottom = 40.dp)
                ) {
                    Text(
                        "INICIAR SESIÓN PARA COMPRAR",
                        color = NeonCyan,
                        fontWeight = FontWeight.Black,
                        fontSize = 14.sp,
                        letterSpacing = 1.sp
                    )
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
            .height(195.dp) // Altura optimizada para impacto visual
            .shadow(
                elevation = 20.dp,
                shape = RoundedCornerShape(32.dp),
                spotColor = neonColor.copy(alpha = 0.5f)
            )
            .clickable { onClick() },
        shape = RoundedCornerShape(32.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(imageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            // Superposición oscura para resaltar tipografía
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            listOf(Color.Transparent, Color.Black.copy(0.85f)),
                            startY = 120f
                        )
                    )
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.Bottom
            ) {
                // Barra indicadora con color neón dinámico
                Surface(
                    color = neonColor,
                    shape = CircleShape,
                    modifier = Modifier.width(42.dp).height(4.dp)
                ) {}

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    title,
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 0.5.sp
                )
                Text(
                    subtitle,
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}