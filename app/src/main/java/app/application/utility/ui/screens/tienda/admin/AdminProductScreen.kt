package app.application.utility.ui.screens.tienda.admin

import android.widget.Toast
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import app.application.utility.ui.components.BaseScreen
import app.application.utility.ui.components.CardContainer
import app.application.utility.ui.components.FuturisticButton
import app.application.utility.ui.components.FuturisticTextField
import app.application.utility.ui.screens.tienda.home.TiendaHomeViewModel
import coil.compose.AsyncImage
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun AdminProductScreen(navController: NavController, screenType: String) {
    val viewModel: TiendaHomeViewModel = viewModel()
    val products by viewModel.products.collectAsState()
    val db = FirebaseFirestore.getInstance()
    val context = LocalContext.current

    // Detectar si venimos del botón "Registrar" o "Inventario"
    var mostrandoRegistro by remember { mutableStateOf(screenType == "registro") }

    // Estados para el formulario de registro
    var nombre by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }
    var stock by remember { mutableStateOf("") }
    var ml by remember { mutableStateOf("") }
    var imageUrl by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    BaseScreen(title = if (mostrandoRegistro) "Nuevo Artículo" else "Inventario Actual", isDark = false) {
        Column(modifier = Modifier.fillMaxSize().padding(20.dp)) {

            if (!mostrandoRegistro) {
                // --- VISTA 1: INVENTARIO ACTUAL ---
                Text("ADMINISTRACIÓN", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFF00E5FF))
                Text("Inventario Actual", fontSize = 28.sp, fontWeight = FontWeight.Black, color = Color(0xFF2D3436))

                Spacer(modifier = Modifier.height(20.dp))

                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(products) { product ->
                        // LÓGICA DE DETECCIÓN DE RECURSOS LOCALES (res/drawable)
                        val imageResId = remember(product.imageUrl) {
                            val id = context.resources.getIdentifier(product.imageUrl, "drawable", context.packageName)
                            if (id != 0) id else null
                        }

                        ListItem(
                            leadingContent = {
                                Box(
                                    modifier = Modifier
                                        .size(60.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                ) {
                                    if (imageResId != null) {
                                        // Muestra imagen local si existe en res/drawable
                                        Image(
                                            painter = painterResource(id = imageResId),
                                            contentDescription = null,
                                            modifier = Modifier.fillMaxSize(),
                                            contentScale = ContentScale.Crop
                                        )
                                    } else {
                                        // Si no es local, intenta cargar URL o placeholder
                                        AsyncImage(
                                            model = if(product.imageUrl.isNotEmpty()) product.imageUrl else "https://via.placeholder.com/150",
                                            contentDescription = null,
                                            modifier = Modifier.fillMaxSize(),
                                            contentScale = ContentScale.Crop
                                        )
                                    }
                                }
                            },
                            headlineContent = { Text(product.nombre, fontWeight = FontWeight.Bold, fontSize = 16.sp) },
                            supportingContent = {
                                Column {
                                    Text("Stock: ${product.stock} unidades", color = if(product.stock < 5) Color.Red else Color.Gray)
                                    Text("Precio: $${product.precio}", fontWeight = FontWeight.Medium)
                                }
                            },
                            trailingContent = {
                                if(product.stock < 5) {
                                    Text("STOCK BAJO", color = Color.Red, fontWeight = FontWeight.Black, fontSize = 10.sp)
                                }
                            }
                        )
                        HorizontalDivider(color = Color.LightGray.copy(alpha = 0.3f), modifier = Modifier.padding(vertical = 4.dp))
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                TextButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("VOLVER AL PANEL CENTRAL", color = Color.Gray, fontWeight = FontWeight.Bold)
                }

            } else {
                // --- VISTA 2: REGISTRO DE NUEVO ARTÍCULO ---
                Text("ADMINISTRACIÓN", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFF7C4DFF))
                Text("Ingreso de Datos", fontSize = 28.sp, fontWeight = FontWeight.Black, color = Color(0xFF2D3436))

                Spacer(modifier = Modifier.height(20.dp))

                Column(modifier = Modifier.weight(1f).verticalScroll(rememberScrollState())) {
                    CardContainer {
                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            FuturisticTextField(value = nombre, onValueChange = { nombre = it }, label = "Nombre del Aroma")
                            FuturisticTextField(value = descripcion, onValueChange = { descripcion = it }, label = "Descripción")

                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                Box(modifier = Modifier.weight(1f)) {
                                    FuturisticTextField(value = precio, onValueChange = { precio = it }, label = "Precio ($)", keyboardType = KeyboardType.Number)
                                }
                                Box(modifier = Modifier.weight(1f)) {
                                    FuturisticTextField(value = ml, onValueChange = { ml = it }, label = "ML", keyboardType = KeyboardType.Number)
                                }
                            }

                            FuturisticTextField(value = stock, onValueChange = { stock = it }, label = "Stock Inicial", keyboardType = KeyboardType.Number)
                            FuturisticTextField(value = imageUrl, onValueChange = { imageUrl = it }, label = "URL o Nombre de Recurso")

                            Spacer(modifier = Modifier.height(10.dp))

                            FuturisticButton(
                                text = if (isLoading) "GUARDANDO..." else "CONFIRMAR REGISTRO",
                                onClick = {
                                    if (nombre.isNotBlank() && precio.isNotBlank()) {
                                        isLoading = true
                                        val data = hashMapOf(
                                            "nombre" to nombre,
                                            "descripcion" to descripcion,
                                            "precio" to (precio.toDoubleOrNull() ?: 0.0),
                                            "stock" to (stock.toIntOrNull() ?: 0),
                                            "presentacionMl" to (ml.toIntOrNull() ?: 0),
                                            "imageUrl" to imageUrl
                                        )
                                        db.collection("products").add(data)
                                            .addOnSuccessListener {
                                                Toast.makeText(context, "Producto Guardado", Toast.LENGTH_SHORT).show()
                                                navController.popBackStack()
                                                isLoading = false
                                            }
                                            .addOnFailureListener {
                                                isLoading = false
                                                Toast.makeText(context, "Error al guardar", Toast.LENGTH_SHORT).show()
                                            }
                                    } else {
                                        Toast.makeText(context, "Nombre y Precio son obligatorios", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            )
                        }
                    }
                }

                TextButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("CANCELAR", color = Color.Red, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}