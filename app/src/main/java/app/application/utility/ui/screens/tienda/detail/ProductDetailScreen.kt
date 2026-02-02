package app.application.utility.ui.screens.tienda.detail

import android.widget.Toast
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import app.application.utility.ui.components.BaseScreen
import app.application.utility.ui.components.FuturisticButton
import app.application.utility.ui.screens.tienda.cart.CartViewModel
import coil.compose.AsyncImage

@Composable
fun ProductDetailScreen(
    navController: NavController,
    productId: String,
    cartViewModel: CartViewModel
) {
    val viewModel: ProductDetailViewModel = viewModel()
    val product by viewModel.product.collectAsState()
    val context = LocalContext.current
    val NeonCyan = Color(0xFF00E5FF)

    LaunchedEffect(productId) { viewModel.loadProduct(productId) }

    BaseScreen(title = "Detalles", isDark = false) {
        if (product == null) {
            DetailSkeleton()
        } else {
            val imageResId = remember(product?.imageUrl) {
                val id = context.resources.getIdentifier(product!!.imageUrl, "drawable", context.packageName)
                if (id != 0) id else null
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // --- CONTENEDOR DE IMAGEN ---
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(320.dp)
                        .shadow(25.dp, RoundedCornerShape(32.dp), spotColor = NeonCyan.copy(alpha = 0.4f))
                        .clip(RoundedCornerShape(32.dp))
                        .background(Color.White)
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
                            model = product!!.imageUrl.ifEmpty { "https://via.placeholder.com/400" },
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // --- TARJETA DE INFORMACIÓN ---
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = Color.White,
                    shape = RoundedCornerShape(28.dp),
                    shadowElevation = 2.dp
                ) {
                    Column(modifier = Modifier.padding(24.dp)) {
                        Text(product!!.nombre, fontSize = 28.sp, fontWeight = FontWeight.Black, color = Color(0xFF2D3436))
                        Text("$${product!!.precio}", fontSize = 26.sp, fontWeight = FontWeight.ExtraBold, color = NeonCyan)

                        HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp), color = Color(0xFFF1F5F9))

                        Text(text = product!!.descripcion, fontSize = 15.sp, color = Color.Gray, lineHeight = 22.sp)

                        Spacer(modifier = Modifier.height(20.dp))

                        DetailRow(label = "Presentación", value = "${product!!.presentacionMl} ml")
                        DetailRow(
                            label = "Estado",
                            value = if (product!!.stock > 0) "En Stock (${product!!.stock})" else "Agotado"
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // --- BOTÓN DE ACCIÓN CORREGIDO ---
                // Se eliminó el parámetro 'enabled' que causaba error y se maneja con alpha y lógica interna
                Box(modifier = Modifier.alpha(if (product!!.stock > 0) 1f else 0.5f)) {
                    FuturisticButton(
                        text = if (product!!.stock > 0) "AÑADIR AL CARRITO" else "AGOTADO",
                        onClick = {
                            if (product!!.stock > 0) {
                                cartViewModel.addProduct(product!!)
                                Toast.makeText(context, "Añadido con éxito", Toast.LENGTH_SHORT).show()
                                navController.popBackStack()
                            } else {
                                Toast.makeText(context, "Producto agotado", Toast.LENGTH_SHORT).show()
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                TextButton(onClick = { navController.popBackStack() }) {
                    Text("VOLVER A LA TIENDA", color = Color.Gray, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun DetailSkeleton() {
    val transition = rememberInfiniteTransition(label = "skeleton")
    val alpha by transition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.7f,
        animationSpec = infiniteRepeatable(animation = tween(1000), repeatMode = RepeatMode.Reverse),
        label = "alpha"
    )

    Column(modifier = Modifier.fillMaxSize().padding(24.dp)) {
        Box(modifier = Modifier.fillMaxWidth().height(320.dp).clip(RoundedCornerShape(32.dp)).background(Color.LightGray.copy(alpha = alpha)))
        Spacer(modifier = Modifier.height(24.dp))
        Box(modifier = Modifier.fillMaxWidth(0.7f).height(30.dp).background(Color.LightGray.copy(alpha = alpha)))
        Spacer(modifier = Modifier.height(12.dp))
        Box(modifier = Modifier.fillMaxWidth(0.4f).height(25.dp).background(Color.LightGray.copy(alpha = alpha)))
        Spacer(modifier = Modifier.height(30.dp))
        Box(modifier = Modifier.fillMaxWidth().height(150.dp).clip(RoundedCornerShape(20.dp)).background(Color.LightGray.copy(alpha = alpha)))
    }
}

@Composable
fun DetailRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, color = Color.Gray, fontSize = 14.sp)
        Text(value, fontWeight = FontWeight.Bold, color = Color(0xFF2D3436), fontSize = 14.sp)
    }
}