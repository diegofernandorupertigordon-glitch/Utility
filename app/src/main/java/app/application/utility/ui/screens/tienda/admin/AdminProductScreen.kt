package app.application.utility.ui.screens.tienda.admin

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
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
import app.application.utility.ui.screens.tienda.home.TiendaHomeViewModel
import coil.compose.AsyncImage
import com.google.firebase.firestore.FirebaseFirestore

// IMPORTS ESPECÍFICOS PARA SUPABASE
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.storage.Storage
import io.github.jan.supabase.storage.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminProductScreen(navController: NavController, screenType: String) {
    val viewModel: TiendaHomeViewModel = viewModel()
    val products by viewModel.products.collectAsState()
    val db = FirebaseFirestore.getInstance()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var mostrandoRegistro by remember { mutableStateOf(screenType == "registro") }
    var productoSeleccionado by remember { mutableStateOf<Product?>(null) }
    var editingProductId by remember { mutableStateOf<String?>(null) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var productToDelete by remember { mutableStateOf<Product?>(null) }

    var nombre by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var precioForm by remember { mutableStateOf("") }
    var stockForm by remember { mutableStateOf("") }
    var cantidadUnidad by remember { mutableStateOf("") }
    var unidadMedida by remember { mutableStateOf("ml") }
    var expandedMenuUnidad by remember { mutableStateOf(false) }
    var imageUrlForm by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    val categorias = listOf("🧴 Perfumería", "💄 Maquillaje", "🛁 Accesorios")
    var categoriaSeleccionada by remember { mutableStateOf(categorias[0]) }

    // --- LÓGICA DE SELECCIÓN E CARGA DE IMAGEN ---
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            isLoading = true
            scope.launch {
                subirImagenASupabase(context, it) { url ->
                    imageUrlForm = url
                    isLoading = false
                }
            }
        }
    }

    fun resetForm() {
        nombre = ""; descripcion = ""; precioForm = ""; stockForm = ""; cantidadUnidad = ""; imageUrlForm = ""
        unidadMedida = "ml"
        editingProductId = null; productoSeleccionado = null
    }

    BackHandler {
        if (mostrandoRegistro && screenType != "registro") {
            mostrandoRegistro = false
            resetForm()
        } else {
            navController.popBackStack()
        }
    }

    if (showDeleteDialog && productToDelete != null) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("¿Eliminar registro?", fontWeight = FontWeight.Bold) },
            text = { Text("¿Estás seguro de que deseas eliminar ${productToDelete?.nombre}?") },
            confirmButton = {
                TextButton(onClick = {
                    productToDelete?.id?.let { id ->
                        db.collection("products").document(id).delete().addOnSuccessListener {
                            Toast.makeText(context, "Eliminado", Toast.LENGTH_SHORT).show()
                            showDeleteDialog = false; productToDelete = null; productoSeleccionado = null
                        }
                    }
                }) { Text("ELIMINAR", color = Color.Red, fontWeight = FontWeight.Bold) }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) { Text("CANCELAR", color = Color.Gray) }
            }
        )
    }

    BaseScreen(title = "", isDark = false) {
        Box(modifier = Modifier.fillMaxSize().background(Color.White)) {
            Scaffold(
                containerColor = Color(0xFFF8FAFC)
            ) { padding ->
                AnimatedContent(
                    targetState = mostrandoRegistro,
                    transitionSpec = {
                        if (targetState) {
                            (slideInHorizontally { it } + fadeIn()).togetherWith(slideOutHorizontally { -it } + fadeOut())
                        } else {
                            (slideInHorizontally { -it } + fadeIn()).togetherWith(slideOutHorizontally { it } + fadeOut())
                        }
                    }, label = ""
                ) { targetMostrandoRegistro ->
                    Box(modifier = Modifier.fillMaxSize().padding(top = 40.dp)) {
                        Column(modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp)) {
                            if (!targetMostrandoRegistro) {
                                // --- VISTA INVENTARIO (ORIGINAL COMPLETA) ---
                                Text("ADMINISTRACIÓN", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFF00E5FF))
                                Text("Inventario Actualizado", fontSize = 28.sp, fontWeight = FontWeight.Black, color = Color(0xFF2D3436))
                                Spacer(modifier = Modifier.height(20.dp))

                                LazyColumn(
                                    modifier = Modifier.weight(1f),
                                    contentPadding = PaddingValues(bottom = 110.dp)
                                ) {
                                    items(products) { product ->
                                        val isSelected = productoSeleccionado?.id == product.id

                                        val isUrl = product.imageUrl.startsWith("http")
                                        val imageResId = if (!isUrl && product.imageUrl.isNotEmpty()) {
                                            context.resources.getIdentifier(product.imageUrl, "drawable", context.packageName)
                                        } else 0

                                        ListItem(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(12.dp))
                                                .clickable { productoSeleccionado = if (isSelected) null else product }
                                                .background(if (isSelected) Color(0xFF00E5FF).copy(alpha = 0.1f) else Color.Transparent),
                                            colors = ListItemDefaults.colors(containerColor = Color.Transparent),
                                            leadingContent = {
                                                Box(modifier = Modifier.size(65.dp).clip(RoundedCornerShape(12.dp)).background(Color.White)) {
                                                    if (isUrl) {
                                                        AsyncImage(
                                                            model = if(product.imageUrl.isNotEmpty()) product.imageUrl else "https://via.placeholder.com/150",
                                                            contentDescription = null, modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop,
                                                            error = painterResource(id = android.R.drawable.ic_menu_report_image)
                                                        )
                                                    } else if (imageResId != 0) {
                                                        Image(painter = painterResource(id = imageResId), contentDescription = null, modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
                                                    } else {
                                                        Image(painter = painterResource(id = android.R.drawable.ic_menu_gallery), contentDescription = null, modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Inside)
                                                    }
                                                }
                                            },
                                            headlineContent = { Text(product.nombre, fontWeight = FontWeight.Bold, fontSize = 16.sp) },
                                            supportingContent = {
                                                Column {
                                                    Text("${product.categoria} | Stock: ${product.stock}", color = if(product.stock < 5) Color.Red else Color.Gray, fontSize = 12.sp)
                                                    Text("Precio: $${product.precio}", fontWeight = FontWeight.Bold, color = Color(0xFF2D3436))
                                                }
                                            }
                                        )
                                        HorizontalDivider(color = Color.LightGray.copy(alpha = 0.3f), modifier = Modifier.padding(vertical = 4.dp))
                                    }
                                }
                            } else {
                                // --- VISTA REGISTRO (ORIGINAL COMPLETA) ---
                                Text("ADMINISTRACIÓN", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = if(editingProductId == null) Color(0xFF7C4DFF) else Color(0xFF00E5FF))
                                Text(if(editingProductId == null) "Ingreso de Artículos" else "Modificar Datos", fontSize = 28.sp, fontWeight = FontWeight.Black, color = Color(0xFF2D3436))
                                Spacer(modifier = Modifier.height(25.dp))

                                Column(modifier = Modifier.weight(1f).verticalScroll(rememberScrollState())) {
                                    CardContainer {
                                        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {

                                            Text("SELECCIONAR CATEGORÍA", fontSize = 11.sp, fontWeight = FontWeight.ExtraBold, color = Color.Gray)
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                                            ) {
                                                categorias.forEach { cat ->
                                                    val isSelected = categoriaSeleccionada == cat
                                                    Box(
                                                        modifier = Modifier
                                                            .weight(1f)
                                                            .height(48.dp)
                                                            .clip(RoundedCornerShape(14.dp))
                                                            .background(if (isSelected) Color(0xFF7C4DFF) else Color(0xFFF1F2F6))
                                                            .clickable { categoriaSeleccionada = cat },
                                                        contentAlignment = Alignment.Center
                                                    ) {
                                                        Text(
                                                            text = cat,
                                                            fontSize = 11.sp,
                                                            fontWeight = FontWeight.Bold,
                                                            color = if (isSelected) Color.White else Color.Gray
                                                        )
                                                    }
                                                }
                                            }

                                            HorizontalDivider(thickness = 0.5.dp, color = Color.LightGray.copy(alpha = 0.5f))

                                            FuturisticTextField(value = nombre, onValueChange = { nombre = it }, label = "Nombre del Artículo", keyboardType = KeyboardType.Text)
                                            FuturisticTextField(value = descripcion, onValueChange = { descripcion = it }, label = "Descripción Breve", keyboardType = KeyboardType.Text)

                                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                                Box(modifier = Modifier.weight(1f)) {
                                                    FuturisticTextField(value = precioForm, onValueChange = { precioForm = it }, label = "Precio ($)", keyboardType = KeyboardType.Number)
                                                }
                                                Box(modifier = Modifier.weight(1f)) {
                                                    FuturisticTextField(value = stockForm, onValueChange = { stockForm = it }, label = "Stock", keyboardType = KeyboardType.Number)
                                                }
                                            }

                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Box(modifier = Modifier.weight(1.5f)) {
                                                    FuturisticTextField(value = cantidadUnidad, onValueChange = { cantidadUnidad = it }, label = "Cantidad", keyboardType = KeyboardType.Number)
                                                }

                                                Box(modifier = Modifier.weight(1f).padding(top = 8.dp)) {
                                                    ExposedDropdownMenuBox(
                                                        expanded = expandedMenuUnidad,
                                                        onExpandedChange = { expandedMenuUnidad = !expandedMenuUnidad }
                                                    ) {
                                                        OutlinedTextField(
                                                            value = unidadMedida,
                                                            onValueChange = {},
                                                            readOnly = true,
                                                            label = { Text("Unidad", fontSize = 12.sp) },
                                                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedMenuUnidad) },
                                                            colors = OutlinedTextFieldDefaults.colors(
                                                                focusedBorderColor = Color(0xFF7C4DFF),
                                                                unfocusedBorderColor = Color.LightGray,
                                                                focusedLabelColor = Color(0xFF7C4DFF)
                                                            ),
                                                            shape = RoundedCornerShape(12.dp),
                                                            modifier = Modifier.menuAnchor().fillMaxWidth()
                                                        )
                                                        ExposedDropdownMenu(
                                                            expanded = expandedMenuUnidad,
                                                            onDismissRequest = { expandedMenuUnidad = false },
                                                            modifier = Modifier.background(Color.White)
                                                        ) {
                                                            listOf("ml", "gr", "oz", "und").forEach { unit ->
                                                                DropdownMenuItem(
                                                                    text = { Text(unit) },
                                                                    onClick = {
                                                                        unidadMedida = unit
                                                                        expandedMenuUnidad = false
                                                                    }
                                                                )
                                                            }
                                                        }
                                                    }
                                                }
                                            }

                                            // CAMPO DE IMAGEN CON BOTÓN DE CARGA INTEGRADO
                                            Box(modifier = Modifier.fillMaxWidth()) {
                                                FuturisticTextField(
                                                    value = imageUrlForm,
                                                    onValueChange = { imageUrlForm = it },
                                                    label = "Imagen (URL o Drawable)",
                                                    keyboardType = KeyboardType.Text
                                                )

                                                IconButton(
                                                    onClick = { launcher.launch("image/*") },
                                                    modifier = Modifier
                                                        .align(Alignment.CenterEnd)
                                                        .padding(end = 8.dp, top = 8.dp)
                                                ) {
                                                    Icon(
                                                        painter = painterResource(id = android.R.drawable.ic_menu_camera),
                                                        contentDescription = "Subir a Supabase",
                                                        tint = Color(0xFF7C4DFF)
                                                    )
                                                }
                                            }

                                            Spacer(modifier = Modifier.height(10.dp))

                                            FuturisticButton(
                                                text = if (isLoading) "PROCESANDO..." else if (editingProductId == null) "CONFIRMAR REGISTRO" else "ACTUALIZAR CAMBIOS",
                                                onClick = {
                                                    if (nombre.isNotBlank() && precioForm.isNotBlank()) {
                                                        isLoading = true
                                                        val data = hashMapOf(
                                                            "name" to nombre,
                                                            "description" to descripcion,
                                                            "price" to (precioForm.toDoubleOrNull() ?: 0.0),
                                                            "stock" to (stockForm.toIntOrNull() ?: 0),
                                                            "presentacionMl" to (cantidadUnidad.toIntOrNull() ?: 0),
                                                            "imageUrl" to imageUrlForm,
                                                            "categoria" to categoriaSeleccionada,
                                                            "unidad" to unidadMedida
                                                        )
                                                        val docRef = if (editingProductId == null) db.collection("products").document() else db.collection("products").document(editingProductId!!)
                                                        docRef.set(data).addOnSuccessListener {
                                                            Toast.makeText(context, "Éxito", Toast.LENGTH_SHORT).show()
                                                            resetForm(); mostrandoRegistro = false; isLoading = false
                                                        }.addOnFailureListener { isLoading = false }
                                                    }
                                                }
                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(120.dp))
                                }
                            }
                        }
                    }
                }
            }

            // BARRA FLOTANTE (ORIGINAL CON DOBLE SOMBRA Y LÓGICA)
            AnimatedVisibility(
                visible = !mostrandoRegistro,
                modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 24.dp, start = 20.dp, end = 20.dp),
                enter = fadeIn() + slideInVertically { it },
                exit = fadeOut() + slideOutVertically { it }
            ) {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = Color.White.copy(alpha = 0.98f),
                    shape = RoundedCornerShape(24.dp),
                    shadowElevation = 10.dp,
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.2f))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp).fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AdminCompactButton(Icons.Default.Edit, "Editar", if (productoSeleccionado != null) Color(0xFF00E5FF) else Color.LightGray) {
                            productoSeleccionado?.let {
                                nombre = it.nombre; descripcion = it.descripcion; precioForm = it.precio.toString()
                                stockForm = it.stock.toString(); cantidadUnidad = it.presentacionMl.toString(); imageUrlForm = it.imageUrl
                                categoriaSeleccionada = it.categoria; unidadMedida = it.unidad; editingProductId = it.id; mostrandoRegistro = true
                            }
                        }
                        AdminCompactButton(Icons.Default.Delete, "Borrar", if (productoSeleccionado != null) Color.Red else Color.LightGray) {
                            productoSeleccionado?.let { productToDelete = it; showDeleteDialog = true }
                        }
                        Box(
                            modifier = Modifier.size(52.dp).shadow(4.dp, CircleShape).shadow(4.dp, CircleShape).clip(CircleShape).background(Color(0xFF7C4DFF))
                                .clickable { resetForm(); mostrandoRegistro = true },
                            contentAlignment = Alignment.Center
                        ) { Icon(Icons.Default.Add, null, tint = Color.White, modifier = Modifier.size(28.dp)) }

                        AdminCompactButton(Icons.Default.Share, "WhatsApp", Color(0xFF25D366)) {
                            if (products.isNotEmpty()) {
                                compartirInventarioWA(context, products)
                            } else {
                                Toast.makeText(context, "No hay productos para compartir", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AdminCompactButton(icon: ImageVector, label: String, color: Color, onClick: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.clip(RoundedCornerShape(12.dp)).clickable { onClick() }.padding(8.dp)) {
        Icon(icon, null, tint = color, modifier = Modifier.size(26.dp))
        Text(label, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = color)
    }
}

// --- FUNCIÓN DE SUBIDA A SUPABASE ---
suspend fun subirImagenASupabase(context: Context, uri: Uri, onResult: (String) -> Unit) {
    val supabaseUrl = "https://dazhrsgqecvyivnmexai.supabase.co"
    val supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImRhemhyc2dxZWN2eWl2bm1leGFpIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NzAyNDg0OTQsImV4cCI6MjA4NTgyNDQ5NH0.kWWmZrSct-15EJCRiFwTUlZPCe34V0ApV5M6uP6iOzk"
    val client = createSupabaseClient(supabaseUrl, supabaseKey) {
        install(Storage)
    }

    try {
        val bytes = withContext(Dispatchers.IO) {
            context.contentResolver.openInputStream(uri)?.use { it.readBytes() }
        }
        if (bytes != null) {
            val fileName = "prod_${System.currentTimeMillis()}.jpg"
            val bucket = client.storage.from("Utility")
            withContext(Dispatchers.IO) {
                // Sintaxis de subida corregida para evitar errores de tipo
                bucket.upload(path = fileName, data = bytes, upsert = true)
            }
            val publicUrl = "$supabaseUrl/storage/v1/object/public/Utility/$fileName"
            withContext(Dispatchers.Main) {
                onResult(publicUrl)
                Toast.makeText(context, "Imagen cargada!", Toast.LENGTH_SHORT).show()
            }
        }
    } catch (e: Exception) {
        withContext(Dispatchers.Main) {
            Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }
}

// --- LÓGICA DE COMPARTIR ORIGINAL ---
fun compartirInventarioWA(context: Context, productos: List<Product>) {
    val fecha = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
    val reporte = StringBuilder()
    reporte.append("📦 *REPORTE DE INVENTARIO*\n")
    reporte.append("📅 Fecha: $fecha\n")
    reporte.append("------------------------------------------\n\n")

    productos.groupBy { it.categoria }.forEach { (categoria, lista) ->
        reporte.append("📌 *${categoria.uppercase()}*\n")
        lista.forEach { p ->
            val alerta = if(p.stock < 5) "⚠️" else "✅"
            reporte.append("$alerta *${p.nombre}* (${p.presentacionMl}${p.unidad})\n")
            reporte.append("   Stock: ${p.stock} | Precio: $${p.precio}\n")
        }
        reporte.append("\n")
    }
    reporte.append("------------------------------------------\n")
    reporte.append("_Generado desde Admin App_")

    val sendIntent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, reporte.toString())
    }
    val whatsappIntent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, reporte.toString())
        setPackage("com.whatsapp")
    }
    try { context.startActivity(whatsappIntent) } catch (e: Exception) {
        context.startActivity(Intent.createChooser(sendIntent, "Compartir vía:"))
    }
}