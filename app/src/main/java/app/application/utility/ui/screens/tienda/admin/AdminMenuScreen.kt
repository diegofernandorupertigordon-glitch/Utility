package app.application.utility.ui.screens.tienda.admin

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import app.application.utility.ui.components.BaseScreen
import app.application.utility.ui.navigation.Routes
import coil.compose.AsyncImage

@Composable
fun AdminMenuScreen(navController: NavController) {
    BaseScreen(title = "Panel de Control", isDark = false) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp).verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            Text("ADMINISTRACIÓN", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF00E5FF))
            Text("CENTRAL DE CONTROL", fontSize = 28.sp, fontWeight = FontWeight.Black, color = Color(0xFF2D3436))
            Spacer(modifier = Modifier.height(30.dp))

            // 1. INVENTARIO ACTUAL
            AdminGlowCard(
                title = "INVENTARIO ACTUAL",
                subtitle = "Ver y Editar Stock",
                imageUrl = "https://images.unsplash.com/photo-1592945403244-b3fbafd7f539?q=80&w=500",
                neonColor = Color(0xFF00E5FF),
                onClick = { navController.navigate("admin_inventory?screenType=inventario") }
            )

            Spacer(modifier = Modifier.height(20.dp))

            // 2. REGISTRAR ARTÍCULO
            AdminGlowCard(
                title = "REGISTRAR ARTÍCULO",
                subtitle = "Añadir nuevas fragancias",
                imageUrl = "https://images.unsplash.com/photo-1541643600914-78b084683601?q=80&w=500",
                neonColor = Color(0xFF7C4DFF),
                onClick = { navController.navigate("admin_inventory?screenType=registro") }
            )

            Spacer(modifier = Modifier.height(20.dp))

            // 3. HISTORIAL DE VENTAS
            AdminGlowCard(
                title = "HISTORIAL DE VENTAS",
                subtitle = "Revisar ingresos",
                imageUrl = "https://images.unsplash.com/photo-1556742044-3c52d6e88c62?q=80&w=500",
                neonColor = Color(0xFFFFAB00),
                onClick = { navController.navigate(Routes.SalesHistory.route) }
            )
        }
    }
}

@Composable
fun AdminGlowCard(title: String, subtitle: String, imageUrl: String, neonColor: Color, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().height(140.dp).clickable { onClick() },
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            AsyncImage(model = imageUrl, contentDescription = null, modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
            Box(modifier = Modifier.fillMaxSize().background(Brush.verticalGradient(colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.9f)))))
            Column(modifier = Modifier.fillMaxSize().padding(20.dp), verticalArrangement = Arrangement.Bottom) {
                Box(modifier = Modifier.width(40.dp).height(4.dp).clip(RoundedCornerShape(2.dp)).background(neonColor))
                Spacer(modifier = Modifier.height(8.dp))
                Text(title, color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Black)
                Text(subtitle, color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp)
            }
        }
    }
}