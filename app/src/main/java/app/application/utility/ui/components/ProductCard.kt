package app.application.utility.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.application.utility.ui.model.Product
import coil.compose.AsyncImage

@Composable
fun ProductCard(
    product: Product,
    onClick: () -> Unit
) {
    val context = LocalContext.current
    val NeonCyan = Color(0xFF00E5FF)
    val SoftGray = Color(0xFFF8FAFC)
    val DarkText = Color(0xFF2D3436)
    val outOfStock = product.stock <= 0

    val imageResId = remember(product.imageUrl) {
        val id = context.resources.getIdentifier(product.imageUrl, "drawable", context.packageName)
        if (id != 0) id else null
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 6.dp)
            .shadow(
                elevation = if (outOfStock) 0.dp else 10.dp,
                shape = RoundedCornerShape(22.dp),
                spotColor = NeonCyan.copy(alpha = 0.5f)
            )
            .background(Color.White, RoundedCornerShape(22.dp))
            .border(1.dp, if (outOfStock) Color.LightGray.copy(0.3f) else SoftGray, RoundedCornerShape(22.dp))
            .clickable(enabled = !outOfStock) { onClick() } // Deshabilitar si no hay stock
            .padding(12.dp)
            .alpha(if (outOfStock) 0.6f else 1f) // Efecto visual de deshabilitado
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            // Imagen del producto
            Box(
                modifier = Modifier
                    .size(90.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(SoftGray)
            ) {
                if (imageResId == null) {
                    AsyncImage(
                        model = product.imageUrl.ifEmpty { "https://via.placeholder.com/150" },
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Image(
                        painter = painterResource(id = imageResId),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = product.nombre,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = DarkText,
                    maxLines = 1
                )
                Text(
                    text = "$${product.precio}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Black,
                    color = if (outOfStock) Color.Gray else NeonCyan
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Etiqueta de Stock con Feedback Visual
                Surface(
                    color = if (!outOfStock) NeonCyan.copy(alpha = 0.1f) else Color.Red.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = if (!outOfStock) "DISPONIBLE: ${product.stock}" else "AGOTADO",
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Black,
                        color = if (!outOfStock) NeonCyan else Color.Red
                    )
                }
            }

            // Icono de acción dinámico
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(if (outOfStock) Color.Transparent else SoftGray, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (outOfStock) "✕" else "→",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (outOfStock) Color.LightGray else NeonCyan
                )
            }
        }
    }
}