package app.application.utility.ui.screens.tienda.cart

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import app.application.utility.ui.components.BaseScreen
import app.application.utility.ui.components.FuturisticButton
import app.application.utility.ui.navigation.Routes
import coil.compose.AsyncImage
import java.util.Locale

@Composable
fun CartScreen(navController: NavController, viewModel: CartViewModel) {
    val items by viewModel.cartItems.collectAsState()
    val context = LocalContext.current
    val NeonCyan = Color(0xFF00E5FF)
    val SoftGray = Color(0xFFF8FAFC)

    BaseScreen(title = "", isDark = false) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.verticalGradient(listOf(SoftGray, Color.White)))
        ) {
            if (items.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.ShoppingCart,
                        contentDescription = null,
                        modifier = Modifier.size(100.dp),
                        tint = Color.LightGray.copy(alpha = 0.5f)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "CARRITO VACÍO",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.LightGray
                    )
                    Spacer(modifier = Modifier.height(32.dp))
                    FuturisticButton(
                        text = "VOLVER A LA TIENDA",
                        onClick = { navController.popBackStack() }
                    )
                }
            } else {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 24.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Column {
                        Text("LISTA DE COMPRA", fontSize = 10.sp, color = NeonCyan, fontWeight = FontWeight.Bold)
                        Text("Mi Carrito", fontWeight = FontWeight.Black, fontSize = 32.sp, color = Color(0xFF2D3436))
                    }
                    TextButton(onClick = { navController.popBackStack() }) {
                        Text("+ AÑADIR", color = NeonCyan, fontWeight = FontWeight.ExtraBold)
                    }
                }

                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(bottom = 20.dp)
                ) {
                    items(items) { item ->
                        val imageResId = remember(item.product.imageUrl) {
                            val id = context.resources.getIdentifier(item.product.imageUrl, "drawable", context.packageName)
                            if (id != 0) id else null
                        }

                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            color = Color.White,
                            shape = RoundedCornerShape(24.dp),
                            shadowElevation = 4.dp
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(85.dp)
                                        .clip(RoundedCornerShape(18.dp))
                                        .background(SoftGray)
                                ) {
                                    if (imageResId != null) {
                                        Image(
                                            painter = painterResource(id = imageResId),
                                            contentDescription = null,
                                            modifier = Modifier.fillMaxSize(),
                                            contentScale = ContentScale.Crop
                                        )
                                    } else {
                                        AsyncImage(
                                            model = item.product.imageUrl,
                                            contentDescription = null,
                                            modifier = Modifier.fillMaxSize(),
                                            contentScale = ContentScale.Crop
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.width(16.dp))

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = item.product.nombre.uppercase(),
                                        fontWeight = FontWeight.Black,
                                        color = Color(0xFF2D3436),
                                        fontSize = 14.sp,
                                        maxLines = 1
                                    )
                                    Text(
                                        text = "${item.product.presentacionMl} ${item.product.unidad} • $${item.product.precio} c/u",
                                        color = Color.Gray,
                                        fontSize = 11.sp
                                    )

                                    Spacer(modifier = Modifier.height(10.dp))

                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                                    ) {
                                        Surface(
                                            shape = CircleShape,
                                            color = SoftGray,
                                            modifier = Modifier.size(30.dp).clickable {
                                                viewModel.removeProduct(item.product.id)
                                            }
                                        ) {
                                            Icon(Icons.Default.Remove, null, modifier = Modifier.padding(6.dp), tint = Color(0xFF2D3436))
                                        }

                                        Text(
                                            text = "${item.quantity}",
                                            fontWeight = FontWeight.Black,
                                            fontSize = 16.sp,
                                            color = Color(0xFF2D3436)
                                        )

                                        Surface(
                                            shape = CircleShape,
                                            color = NeonCyan.copy(alpha = 0.15f),
                                            modifier = Modifier.size(30.dp).clickable {
                                                val added = viewModel.addProduct(item.product)
                                                if (!added) {
                                                    Toast.makeText(context, "Stock máximo alcanzado", Toast.LENGTH_SHORT).show()
                                                }
                                            }
                                        ) {
                                            Icon(Icons.Default.Add, null, modifier = Modifier.padding(6.dp), tint = NeonCyan)
                                        }
                                    }
                                }

                                Column(horizontalAlignment = Alignment.End) {
                                    Text(
                                        text = "$${String.format(Locale.US, "%.2f", item.product.precio * item.quantity)}",
                                        fontWeight = FontWeight.Black,
                                        color = Color(0xFF2D3436),
                                        fontSize = 17.sp
                                    )

                                    IconButton(onClick = {
                                        viewModel.deleteItemCompletely(item.product.id)
                                    }) {
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = "Eliminar",
                                            tint = Color.Red.copy(0.3f),
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = Color(0xFF1A1F26),
                    shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
                ) {
                    Column(modifier = Modifier.padding(28.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text("TOTAL A PAGAR", color = Color.Gray, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                Text(
                                    text = "$${String.format(Locale.US, "%.2f", viewModel.total())}",
                                    color = Color.White,
                                    fontSize = 32.sp,
                                    fontWeight = FontWeight.Black
                                )
                            }

                            Surface(
                                color = NeonCyan.copy(alpha = 0.2f),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = "USD",
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    color = NeonCyan,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        FuturisticButton(
                            text = "FINALIZAR Y ENVIAR TICKET", // Texto más claro para el flujo
                            modifier = Modifier.fillMaxWidth(),
                            onClick = {
                                viewModel.checkout(
                                    context = context,
                                    onSuccess = {
                                        // Quitamos el Toast pequeño y ponemos uno más descriptivo
                                        Toast.makeText(context, "Generando ticket profesional...", Toast.LENGTH_LONG).show()

                                        // Navegación de retorno
                                        navController.navigate(Routes.TiendaHome.route) {
                                            popUpTo(Routes.TiendaHome.route) { inclusive = true }
                                        }
                                    },
                                    onError = { error ->
                                        Toast.makeText(context, error, Toast.LENGTH_LONG).show()
                                    }
                                )
                            }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}