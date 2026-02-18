package app.application.utility.ui.screens.tienda

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Directions
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import app.application.utility.R
import coil.compose.AsyncImage
import coil.request.ImageRequest

@Composable
fun ManualScreen(navController: NavController, seccion: String = "MANUAL") {
    val context = LocalContext.current

    val tituloHeader = when(seccion) {
        "NOSOTROS" -> "Sobre Nuestra Casa"
        "GARANTIA" -> "Excelencia y Respaldo"
        "MATRIZ" -> "Nuestra Matriz VIP"
        else -> "Guía y Detalles de Marca"
    }

    // Referencias a tus nuevas imágenes locales guardadas en drawable
    val imagenLocal = when(seccion) {
        "NOSOTROS" -> R.drawable.img_nosotros
        "GARANTIA" -> R.drawable.img_garantia
        "MATRIZ"   -> R.drawable.img_matriz
        else       -> R.drawable.img_manual
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // --- BANNER SUPERIOR CON TUS IMÁGENES LOCALES ---
        Box(modifier = Modifier
            .fillMaxWidth()
            .height(260.dp)
            .background(Color(0xFFF5F5F5))) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(imagenLocal) // Usando tus archivos .jpg locales
                    .crossfade(800)
                    .build(),
                contentDescription = "Banner $seccion",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            // Filtro sutil para coherencia visual
            Box(modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.05f)))
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "PERFUMERÍA INTEGRAL",
                color = Color(0xFF00B8D4),
                fontSize = 12.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 2.sp
            )

            Text(
                text = tituloHeader,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1A1A1A),
                modifier = Modifier.padding(vertical = 4.dp)
            )

            Divider(
                modifier = Modifier.padding(vertical = 16.dp).width(40.dp),
                thickness = 3.dp,
                color = Color(0xFF00B8D4)
            )

            Column(
                modifier = Modifier.verticalScroll(rememberScrollState())
            ) {
                when(seccion) {
                    "MANUAL" -> {
                        ManualSectionItem(
                            icon = Icons.Default.ShoppingCart,
                            title = "Proceso de Pedido VIP",
                            content = "Al confirmar tu selección, generamos un ticket digital de alta precisión. Envía este documento vía WhatsApp para que un asesor valide el stock y coordine tu entrega prioritaria."
                        )
                        ManualSectionItem(
                            icon = Icons.Default.Thermostat,
                            title = "Conservación de Notas",
                            content = "Las fragancias son organismos vivos. Mantener en ambientes entre 15°C y 20°C, lejos de la luz solar directa para preservar su pirámide olfativa intacta."
                        )
                    }
                    "NOSOTROS" -> {
                        ManualSectionItem(
                            icon = Icons.Default.History,
                            title = "Nuestra Trayectoria",
                            content = "Surgimos de la necesidad de exclusividad. Somos curadores de piezas maestras, seleccionando fragancias que narran historias de distinción y elegancia."
                        )
                        ManualSectionItem(
                            icon = Icons.Default.Public,
                            title = "Compromiso Internacional",
                            content = "Alianzas directas con las casas más prestigiosas de Francia e Italia, garantizando lanzamientos globales en tiempo récord en Ecuador."
                        )
                    }
                    "GARANTIA" -> {
                        ManualSectionItem(
                            icon = Icons.Default.Verified,
                            title = "Originalidad Certificada",
                            content = "Garantía total de autenticidad. Cada unidad incluye códigos de serie y grabados originales para trazabilidad desde el laboratorio de origen."
                        )
                        ManualSectionItem(
                            icon = Icons.Default.Security,
                            title = "Protección al Cliente",
                            content = "Nuestra política de satisfacción protege tu inversión ante cualquier defecto de fabricación, con atención personalizada de alto nivel."
                        )
                    }
                    "MATRIZ" -> {
                        Text("📍 Ubicación Estratégica", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color(0xFF2D3436))
                        Text("Av. de los Shyris y Naciones Unidas\nEdificio CitiPlaza, Local 15\nQuito, Ecuador", color = Color.Gray, fontSize = 15.sp, lineHeight = 22.sp)

                        Spacer(modifier = Modifier.height(20.dp))

                        Button(
                            onClick = {
                                val gmmIntentUri = Uri.parse("google.navigation:q=-0.175510,-78.480210")
                                context.startActivity(Intent(Intent.ACTION_VIEW, gmmIntentUri).apply {
                                    setPackage("com.google.android.apps.maps")
                                })
                            },
                            modifier = Modifier.fillMaxWidth().height(55.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1A1A1A)),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(Icons.Default.Directions, null, tint = Color.White)
                            Spacer(modifier = Modifier.width(10.dp))
                            Text("INICIAR RUTA GPS", fontWeight = FontWeight.Bold, color = Color.White, letterSpacing = 1.sp)
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        ManualSectionItem(
                            icon = Icons.Default.Star,
                            title = "Ambiente Sensorial",
                            content = "Visítanos para una cata olfativa privada, donde podrá experimentar las notas de fondo en un entorno diseñado para la alta perfumería."
                        )
                    }
                }
                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}

@Composable
fun ManualSectionItem(icon: ImageVector, title: String, content: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .size(45.dp)
                .background(Color(0xFFF0FDFF), RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, null, tint = Color(0xFF00B8D4), modifier = Modifier.size(24.dp))
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(title, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2D3436))
            Spacer(modifier = Modifier.height(4.dp))
            Text(content, fontSize = 14.sp, color = Color(0xFF636E72), lineHeight = 22.sp)
        }
    }
}