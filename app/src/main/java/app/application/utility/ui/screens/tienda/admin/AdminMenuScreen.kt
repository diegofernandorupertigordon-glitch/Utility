package app.application.utility.ui.screens.tienda.admin

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.navigation.NavController
import app.application.utility.ui.components.BaseScreen
import app.application.utility.ui.navigation.Routes
import coil.compose.AsyncImage
import coil.request.ImageRequest

@Composable
fun AdminMenuScreen(navController: NavController) {
    val NeonCyan = Color(0xFF00E5FF)

    // Se mantiene el título vacío para evitar la flecha y barra superior
    BaseScreen(title = "", isDark = false) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.verticalGradient(listOf(Color(0xFFF8FAFC), Color.White)))
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(48.dp))

            // --- HEADER ADMINISTRATIVO ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("ADMINISTRACIÓN", fontSize = 11.sp, fontWeight = FontWeight.Black, color = NeonCyan, letterSpacing = 2.sp)
                    Text("Central de Control", fontSize = 32.sp, fontWeight = FontWeight.Black, color = Color(0xFF2D3436))
                }
                Surface(
                    shape = CircleShape,
                    color = NeonCyan.copy(alpha = 0.1f),
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(Icons.Default.Shield, null, tint = NeonCyan, modifier = Modifier.padding(12.dp))
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // 1. INVENTARIO ACTUAL
            AdminGlowCard(
                title = "INVENTARIO ACTUAL",
                subtitle = "Gestión de Stock en Tiempo Real",
                imageUrl = "https://images.unsplash.com/photo-1592945403244-b3fbafd7f539?q=80&w=800",
                neonColor = NeonCyan,
                onClick = { navController.navigate("admin_inventory?screenType=inventario") }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // 2. REGISTRAR ARTÍCULO
            AdminGlowCard(
                title = "REGISTRAR ARTÍCULO",
                subtitle = "Ingreso de Nuevas Fragancias",
                imageUrl = "https://images.unsplash.com/photo-1541643600914-78b084683601?q=80&w=800",
                neonColor = Color(0xFF7C4DFF),
                onClick = { navController.navigate("admin_inventory?screenType=registro") }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // 3. HISTORIAL DE VENTAS
            AdminGlowCard(
                title = "HISTORIAL DE VENTAS",
                subtitle = "Análisis de Ingresos y Reportes",
                imageUrl = "https://images.unsplash.com/photo-1556742044-3c52d6e88c62?q=80&w=800",
                neonColor = Color(0xFFFFAB00),
                onClick = { navController.navigate(Routes.SalesHistory.route) }
            )

            Spacer(modifier = Modifier.height(48.dp))
        }
    }
}

@Composable
fun AdminGlowCard(title: String, subtitle: String, imageUrl: String, neonColor: Color, onClick: () -> Unit) {
    val context = LocalContext.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(185.dp) // Optimizado para un impacto visual superior
            .shadow(
                elevation = 20.dp,
                shape = RoundedCornerShape(32.dp),
                spotColor = neonColor.copy(alpha = 0.4f)
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

            // Gradiente oscuro profundo para legibilidad premium
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.85f)),
                            startY = 150f
                        )
                    )
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.Bottom
            ) {
                // Barra indicadora Neon
                Surface(
                    color = neonColor,
                    shape = CircleShape,
                    modifier = Modifier.width(42.dp).height(4.dp)
                ) {}

                Spacer(modifier = Modifier.height(12.dp))

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