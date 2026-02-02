package app.application.utility.ui.screens.tienda.home

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import app.application.utility.ui.components.BaseScreen
import app.application.utility.ui.components.ProductCard
import app.application.utility.ui.navigation.Routes
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TiendaHomeScreen(navController: NavController) {
    val viewModel: TiendaHomeViewModel = viewModel()
    val products by viewModel.products.collectAsState()
    val user = FirebaseAuth.getInstance().currentUser
    val isAdmin = user?.email?.lowercase() == "diegoruperti1987@hotmail.com"

    var searchQuery by remember { mutableStateOf("") }

    // Simulación de estado de carga si la lista está vacía inicialmente
    val isLoading = products.isEmpty()

    val displayProducts = remember(searchQuery, products) {
        if (searchQuery.isEmpty()) products
        else products.filter { it.nombre.contains(searchQuery, ignoreCase = true) }
    }

    BaseScreen(title = "Aromas UIO", isDark = false) {
        Scaffold(
            containerColor = Color.Transparent,
            floatingActionButton = {
                if (isAdmin) {
                    FloatingActionButton(
                        onClick = { navController.navigate(Routes.AdminProducts.route) },
                        containerColor = Color(0xFF00E5FF),
                        contentColor = Color.Black,
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Icon(Icons.Default.Settings, contentDescription = "Admin")
                    }
                }
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            ) {
                // --- HEADER ---
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("EXPLORAR", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFF00E5FF))
                        Text("Fragancias", fontSize = 32.sp, fontWeight = FontWeight.Black, color = Color(0xFF2D3436))
                    }

                    IconButton(
                        onClick = { navController.navigate(Routes.Cart.route) },
                        modifier = Modifier.size(48.dp).background(Color(0xFFFFF9E6), RoundedCornerShape(14.dp))
                    ) {
                        Icon(Icons.Default.ShoppingCart, null, tint = Color(0xFFFFAB00), modifier = Modifier.size(24.dp))
                    }
                }

                // --- BUSCADOR ---
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
                    placeholder = { Text("¿Qué aroma buscas hoy?", color = Color.Gray) },
                    leadingIcon = { Icon(Icons.Default.Search, null, tint = Color(0xFF00E5FF)) },
                    shape = RoundedCornerShape(20.dp),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF00E5FF),
                        unfocusedBorderColor = Color.LightGray.copy(0.3f),
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    )
                )

                Spacer(modifier = Modifier.height(20.dp))

                // --- LISTA CON LÓGICA DE CARGA (SKELETON) ---
                if (isLoading && searchQuery.isEmpty()) {
                    LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(24.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        items(5) { SkeletonProductCard() }
                    }
                } else if (displayProducts.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("No hay resultados", fontWeight = FontWeight.Bold, color = Color.LightGray)
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(bottom = 100.dp, start = 20.dp, end = 20.dp)
                    ) {
                        items(displayProducts) { product ->
                            ProductCard(
                                product = product,
                                onClick = { navController.navigate(Routes.ProductDetail.createRoute(product.id)) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SkeletonProductCard() {
    val transition = rememberInfiniteTransition(label = "")
    val alpha by transition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.7f,
        animationSpec = infiniteRepeatable(animation = tween(800), repeatMode = RepeatMode.Reverse),
        label = ""
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(110.dp)
            .background(Color.White, RoundedCornerShape(22.dp))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.size(85.dp).clip(RoundedCornerShape(18.dp)).background(Color.LightGray.copy(alpha = alpha)))
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Box(modifier = Modifier.fillMaxWidth(0.6f).height(15.dp).background(Color.LightGray.copy(alpha = alpha)))
            Spacer(modifier = Modifier.height(8.dp))
            Box(modifier = Modifier.fillMaxWidth(0.3f).height(15.dp).background(Color.LightGray.copy(alpha = alpha)))
        }
    }
}