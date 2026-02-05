package app.application.utility.ui.screens.tienda.home

import android.widget.Toast
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import app.application.utility.ui.components.BaseScreen
import app.application.utility.ui.components.ProductCard
import app.application.utility.ui.navigation.Routes
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TiendaHomeScreen(navController: NavController) {
    val viewModel: TiendaHomeViewModel = viewModel()
    val products by viewModel.products.collectAsState()
    val user = FirebaseAuth.getInstance().currentUser
    val isAdmin = user?.email?.lowercase() == "diegoruperti1987@hotmail.com"
    val context = LocalContext.current

    val categorias = listOf("🧴 Perfumería", "💄 Maquillaje", "🛁 Accesorios")

    val pagerState = rememberPagerState(pageCount = { categorias.size })
    val scope = rememberCoroutineScope()
    var searchQuery by remember { mutableStateOf("") }

    val isLoading = products.isEmpty()

    BaseScreen(title = "Perfumería Integral", isDark = false) {
        Box(modifier = Modifier.fillMaxSize().background(Color.White)) {
            Scaffold(
                containerColor = Color(0xFFF8FAFC)
            ) { innerPadding ->
                Column(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize()
                ) {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp, vertical = 20.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("DEPARTAMENTOS", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFF00E5FF))
                            Text("Perfumería Integral", fontSize = 28.sp, fontWeight = FontWeight.Black, color = Color(0xFF2D3436))
                        }
                        IconButton(
                            onClick = { navController.navigate(Routes.Cart.route) },
                            modifier = Modifier
                                .size(48.dp)
                                .background(Color(0xFFFFF9E6), RoundedCornerShape(14.dp))
                        ) {
                            Icon(Icons.Default.ShoppingCart, null, tint = Color(0xFFFFAB00), modifier = Modifier.size(24.dp))
                        }
                    }

                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp),
                        placeholder = { Text("¿Qué buscas hoy?", color = Color.Gray) },
                        leadingIcon = { Icon(Icons.Default.Search, null, tint = Color(0xFF00E5FF)) },
                        shape = RoundedCornerShape(20.dp),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF00E5FF),
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    ScrollableTabRow(
                        selectedTabIndex = pagerState.currentPage,
                        edgePadding = 24.dp,
                        containerColor = Color.Transparent,
                        divider = {},
                        indicator = { tabPositions ->
                            if (pagerState.currentPage < tabPositions.size) {
                                TabRowDefaults.Indicator(
                                    modifier = Modifier.tabIndicatorOffset(tabPositions[pagerState.currentPage]),
                                    height = 3.dp,
                                    color = Color(0xFF00E5FF)
                                )
                            }
                        }
                    ) {
                        categorias.forEachIndexed { index, cat ->
                            Tab(
                                selected = pagerState.currentPage == index,
                                onClick = { scope.launch { pagerState.animateScrollToPage(index) } },
                                text = {
                                    Text(
                                        text = cat,
                                        color = if (pagerState.currentPage == index) Color(0xFF00E5FF) else Color.Gray,
                                        fontWeight = if (pagerState.currentPage == index) FontWeight.Black else FontWeight.Medium,
                                        fontSize = 14.sp
                                    )
                                }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier.fillMaxSize()
                    ) { pageIndex ->

                        val catActual = categorias[pageIndex]
                        val filteredList = products.filter {
                            it.categoria == catActual &&
                                    (searchQuery.isEmpty() || it.nombre.contains(searchQuery, ignoreCase = true))
                        }

                        if (isLoading && searchQuery.isEmpty()) {
                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                contentPadding = PaddingValues(24.dp),
                                verticalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                items(5) { SkeletonProductCard() }
                            }
                        } else if (filteredList.isEmpty()) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    "No hay artículos en ${catActual.substring(2)}",
                                    fontWeight = FontWeight.Bold,
                                    color = Color.LightGray
                                )
                            }
                        } else {
                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.spacedBy(12.dp),
                                contentPadding = PaddingValues(
                                    bottom = 16.dp,
                                    start = 20.dp,
                                    end = 20.dp
                                )
                            ) {
                                items(filteredList) { product ->
                                    ProductCard(
                                        product = product,
                                        onClick = {
                                            navController.navigate(
                                                Routes.ProductDetail.createRoute(product.id)
                                            )
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }

            if (isAdmin) {
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .background(Color.White)
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
                                .padding(
                                    top = 6.dp,
                                    bottom = 6.dp,
                                    start = 24.dp,
                                    end = 24.dp
                                )
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .clickable {
                                        searchQuery = ""
                                        scope.launch { pagerState.animateScrollToPage(0) }
                                    }
                                    .padding(4.dp)
                            ) {
                                Icon(Icons.Default.Home, null, tint = Color(0xFF00E5FF), modifier = Modifier.size(26.dp))
                                Text("Inicio", fontSize = 10.sp, color = Color(0xFF00E5FF), fontWeight = FontWeight.Bold)
                            }

                            Box(
                                modifier = Modifier
                                    .size(56.dp)
                                    .shadow(12.dp, CircleShape)
                                    .clip(CircleShape)
                                    .background(Color(0xFF00E5FF))
                                    .clickable {
                                        navController.navigate(Routes.AdminProducts.route)
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Settings,
                                    contentDescription = "Admin",
                                    tint = Color.Black,
                                    modifier = Modifier.size(32.dp)
                                )
                            }

                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .clickable {
                                        Toast.makeText(context, "Admin: ${user?.email}", Toast.LENGTH_SHORT).show()
                                    }
                                    .padding(4.dp)
                            ) {
                                Icon(Icons.Default.Person, null, tint = Color.Gray, modifier = Modifier.size(26.dp))
                                Text("Perfil", fontSize = 10.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
                            }
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
        animationSpec = infiniteRepeatable(
            animation = tween(800),
            repeatMode = RepeatMode.Reverse
        ),
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
        Box(
            modifier = Modifier
                .size(85.dp)
                .clip(RoundedCornerShape(18.dp))
                .background(Color.LightGray.copy(alpha = alpha))
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .height(15.dp)
                    .background(Color.LightGray.copy(alpha = alpha))
            )
            Spacer(modifier = Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.3f)
                    .height(15.dp)
                    .background(Color.LightGray.copy(alpha = alpha))
            )
        }
    }
}
