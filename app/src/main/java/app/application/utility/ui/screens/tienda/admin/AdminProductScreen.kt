package app.application.utility.ui.screens.tienda.admin

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
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
import app.application.utility.ui.model.Product
import app.application.utility.ui.navigation.Routes
import app.application.utility.ui.screens.tienda.home.TiendaHomeViewModel
import coil.compose.AsyncImage
import com.google.firebase.firestore.FirebaseFirestore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminProductScreen(navController: NavController, screenType: String) {
    val viewModel: TiendaHomeViewModel = viewModel()
    val products by viewModel.products.collectAsState()
    val db = FirebaseFirestore.getInstance()
    val context = LocalContext.current

    var mostrandoRegistro by remember { mutableStateOf(screenType == "registro") }

    var productoSeleccionado by remember { mutableStateOf<Product?>(null) }
    var editingProductId by remember { mutableStateOf<String?>(null) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var productToDelete by remember { mutableStateOf<Product?>(null) }

    var nombre by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }
    var stock by remember { mutableStateOf("") }
    var ml by remember { mutableStateOf("") }
    var imageUrl by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    val categorias = listOf("🧴 Perfumería", "💄 Maquillaje", "🛁 Accesorios")
    var categoriaSeleccionada by remember { mutableStateOf(categorias[0]) }

    fun resetForm() {
        nombre = ""; descripcion = ""; precio = ""; stock = ""; ml = ""; imageUrl = ""
        editingProductId = null; productoSeleccionado = null
    }

    BackHandler {
        if (mostrandoRegistro) {
            resetForm()
            navController.popBackStack()
        } else {
            navController.popBackStack()
        }
    }

    if (showDeleteDialog && productToDelete != null) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("¿Eliminar registro?", fontWeight = FontWeight.Bold) },
            text = { Text("¿Estás seguro de que deseas eliminar ${productToDelete?.nombre}? Esta acción es permanente.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        productToDelete?.id?.let { id ->
                            db.collection("products").document(id).delete().addOnSuccessListener {
                                Toast.makeText(context, "Eliminado correctamente", Toast.LENGTH_SHORT).show()
                                showDeleteDialog = false
                                productToDelete = null
                                productoSeleccionado = null
                            }
                        }
                    }
                ) {
                    Text("ELIMINAR", color = Color.Red, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false; productToDelete = null }) {
                    Text("CANCELAR", color = Color.Gray)
                }
            }
        )
    }

    BaseScreen(title = if (mostrandoRegistro) "Gestión de Producto" else "Inventario Actual", isDark = false) {
        Box(modifier = Modifier.fillMaxSize().background(Color.White)) {
            Scaffold(
                containerColor = Color(0xFFF8FAFC),
                topBar = {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = {
                            if (mostrandoRegistro) {
                                resetForm()
                                mostrandoRegistro = false
                            } else {
                                navController.popBackStack()
                            }
                        }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Regresar", tint = Color.Black)
                        }

                        Spacer(modifier = Modifier.weight(1f))

                        IconButton(onClick = {
                            navController.navigate(Routes.TiendaHome.route) {
                                popUpTo(Routes.TiendaHome.route) { inclusive = true }
                            }
                        }) {
                            Icon(Icons.Default.Home, contentDescription = "Inicio", tint = Color(0xFF00E5FF))
                        }
                    }
                }
            ) { padding ->
                AnimatedContent(
                    targetState = mostrandoRegistro,
                    transitionSpec = {
                        if (targetState) {
                            slideInHorizontally { it } + fadeIn() togetherWith slideOutHorizontally { -it } + fadeOut()
                        } else {
                            slideInHorizontally { -it } + fadeIn() togetherWith slideOutHorizontally { it } + fadeOut()
                        }
                    },
                    label = "ScreenTransition"
                ) { targetMostrandoRegistro ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(padding)
                    ) {

                        Column(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)) {
                            if (!targetMostrandoRegistro) {
                                Text("ADMINISTRACIÓN", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFF00E5FF))
                                Text("Inventario Actual", fontSize = 28.sp, fontWeight = FontWeight.Black, color = Color(0xFF2D3436))
                                Spacer(modifier = Modifier.height(20.dp))

                                LazyColumn(
                                    modifier = Modifier.weight(1f),
                                    contentPadding = PaddingValues(bottom = 24.dp)
                                ) {
                                    items(products) { product ->
                                        val isSelected = productoSeleccionado?.id == product.id
                                        val isUrl = product.imageUrl.startsWith("http")
                                        val imageResId = if (!isUrl) {
                                            val id = context.resources.getIdentifier(product.imageUrl, "drawable", context.packageName)
                                            if (id != 0) id else null
                                        } else null

                                        ListItem(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(12.dp))
                                                .clickable { productoSeleccionado = if (isSelected) null else product }
                                                .background(if (isSelected) Color(0xFF00E5FF).copy(alpha = 0.1f) else Color.Transparent),
                                            colors = ListItemDefaults.colors(containerColor = Color.Transparent),
                                            leadingContent = {
                                                Box(modifier = Modifier.size(60.dp).clip(RoundedCornerShape(12.dp))) {
                                                    if (imageResId != null) {
                                                        Image(
                                                            painter = painterResource(id = imageResId),
                                                            contentDescription = null,
                                                            modifier = Modifier.fillMaxSize(),
                                                            contentScale = ContentScale.Crop
                                                        )
                                                    } else {
                                                        AsyncImage(
                                                            model = if (product.imageUrl.isNotEmpty()) product.imageUrl else "https://via.placeholder.com/150",
                                                            contentDescription = null,
                                                            modifier = Modifier.fillMaxSize(),
                                                            contentScale = ContentScale.Crop,
                                                            error = painterResource(id = android.R.drawable.ic_menu_report_image),
                                                            placeholder = painterResource(id = android.R.drawable.ic_menu_gallery)
                                                        )
                                                    }
                                                }
                                            },
                                            headlineContent = { Text(product.nombre, fontWeight = FontWeight.Bold) },
                                            supportingContent = {
                                                Column {
                                                    Text("${product.categoria} | Stock: ${product.stock}", color = if (product.stock < 5) Color.Red else Color.Gray)
                                                    Text("Precio: $${product.precio}")
                                                }
                                            }
                                        )
                                        HorizontalDivider(color = Color.LightGray.copy(alpha = 0.3f))
                                    }
                                }
                            } else {
                                Text("ADMINISTRACIÓN", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = if (editingProductId == null) Color(0xFF7C4DFF) else Color(0xFF00E5FF))
                                Text(if (editingProductId == null) "Ingreso de Datos" else "Modificar Datos", fontSize = 28.sp, fontWeight = FontWeight.Black, color = Color(0xFF2D3436))
                                Spacer(modifier = Modifier.height(20.dp))

                                Column(modifier = Modifier.weight(1f).verticalScroll(rememberScrollState())) {
                                    CardContainer {
                                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                            FuturisticTextField(nombre, { nombre = it }, "Nombre del Artículo", KeyboardType.Text)
                                            Text("Seleccionar Departamento:", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                                                categorias.forEach { cat ->
                                                    FilterChip(
                                                        selected = categoriaSeleccionada == cat,
                                                        onClick = { categoriaSeleccionada = cat },
                                                        label = { Text(cat, fontSize = 10.sp, fontWeight = FontWeight.Bold) }
                                                    )
                                                }
                                            }
                                            FuturisticTextField(descripcion, { descripcion = it }, "Descripción", KeyboardType.Text)
                                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                                Box(modifier = Modifier.weight(1f)) { FuturisticTextField(precio, { precio = it }, "Precio ($)", KeyboardType.Number) }
                                                Box(modifier = Modifier.weight(1f)) { FuturisticTextField(ml, { ml = it }, "ML", KeyboardType.Number) }
                                            }
                                            FuturisticTextField(stock, { stock = it }, "Stock Inicial", KeyboardType.Number)
                                            FuturisticTextField(imageUrl, { imageUrl = it }, "Nombre en Drawable o URL", KeyboardType.Text)

                                            Spacer(modifier = Modifier.height(10.dp))
                                            FuturisticButton(
                                                text = if (isLoading) "PROCESANDO..." else if (editingProductId == null) "CONFIRMAR REGISTRO" else "ACTUALIZAR CAMBIOS",
                                                onClick = {
                                                    if (nombre.isNotBlank() && precio.isNotBlank()) {
                                                        isLoading = true
                                                        val data = hashMapOf(
                                                            "name" to nombre,
                                                            "description" to descripcion,
                                                            "price" to (precio.toDoubleOrNull() ?: 0.0),
                                                            "stock" to (stock.toIntOrNull() ?: 0),
                                                            "presentacionMl" to (ml.toIntOrNull() ?: 0),
                                                            "imageUrl" to imageUrl,
                                                            "categoria" to categoriaSeleccionada
                                                        )
                                                        val docRef =
                                                            if (editingProductId == null) db.collection("products").document()
                                                            else db.collection("products").document(editingProductId!!)
                                                        docRef.set(data).addOnSuccessListener {
                                                            Toast.makeText(context, "Operación Exitosa", Toast.LENGTH_SHORT).show()
                                                            resetForm()
                                                            mostrandoRegistro = false
                                                            isLoading = false
                                                        }.addOnFailureListener {
                                                            isLoading = false
                                                            Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
                                                        }
                                                    }
                                                }
                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(16.dp)) // ✅ CAMBIO 3
                                }
                            }
                        }
                    }
                }
            }

            AnimatedVisibility(
                visible = !mostrandoRegistro,
                modifier = Modifier.align(Alignment.BottomCenter),
                enter = slideInVertically { it } + fadeIn(),
                exit = slideOutVertically { it } + fadeOut()
            ) {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    tonalElevation = 12.dp,
                    color = Color.White,
                    shadowElevation = 25.dp,
                    shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .navigationBarsPadding()
                            .padding(vertical = 6.dp, horizontal = 16.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AdminIconButton(Icons.Default.Edit, "Editar", if (productoSeleccionado != null) Color(0xFF00E5FF) else Color.LightGray) {
                            productoSeleccionado?.let { product ->
                                nombre = product.nombre
                                descripcion = product.descripcion
                                precio = product.precio.toString()
                                stock = product.stock.toString()
                                ml = product.presentacionMl.toString()
                                imageUrl = product.imageUrl
                                categoriaSeleccionada = product.categoria
                                editingProductId = product.id
                                mostrandoRegistro = true
                            }
                        }

                        AdminIconButton(Icons.Default.Delete, "Borrar", if (productoSeleccionado != null) Color.Red else Color.LightGray) {
                            productoSeleccionado?.let {
                                productToDelete = it
                                showDeleteDialog = true
                            }
                        }

                        AdminIconButton(Icons.Default.Add, "Nuevo", Color(0xFF7C4DFF)) {
                            resetForm()
                            mostrandoRegistro = true
                        }

                        AdminIconButton(Icons.Default.Share, "Reporte", Color(0xFF25D366)) {
                            compartirInventarioWA(context, products)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AdminIconButton(icon: ImageVector, label: String, color: Color, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable { onClick() }
            .padding(8.dp)
    ) {
        Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(26.dp))
        Text(label, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = color)
    }
}

fun compartirInventarioWA(context: Context, productos: List<Product>) {
    val fecha = java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault()).format(java.util.Date())
    val reporte = StringBuilder("*📋 REPORTE DE INVENTARIO - $fecha*\n\n")
    productos.groupBy { it.categoria }.forEach { (categoria, lista) ->
        reporte.append("⭐ *${categoria.uppercase()}*\n")
        lista.forEach { p ->
            reporte.append("• ${p.nombre} | Stock: ${p.stock} | $${p.precio}\n")
        }
        reporte.append("\n")
    }
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, reporte.toString())
        setPackage("com.whatsapp")
    }
    try {
        context.startActivity(intent)
    } catch (e: Exception) {
        context.startActivity(Intent.createChooser(intent, "Compartir reporte"))
    }
}
