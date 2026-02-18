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
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import app.application.utility.ui.components.BaseScreen
import app.application.utility.ui.components.PerfumeriaDrawer
import app.application.utility.ui.components.ProductCard
import app.application.utility.ui.navigation.Routes
import app.application.utility.ui.screens.auth.AuthViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TiendaHomeScreen(navController: NavController) {
    val viewModel: TiendaHomeViewModel = viewModel()
    val authViewModel: AuthViewModel = viewModel()
    val products by viewModel.products.collectAsState()
    val user = FirebaseAuth.getInstance().currentUser
    val isAdmin = user?.email?.lowercase() == "diegoruperti1987@hotmail.com"
    val context = LocalContext.current

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val categorias = listOf("🧴 Perfumería", "💄 Maquillaje", "🛁 Accesorios")
    val pagerState = rememberPagerState(pageCount = { categorias.size })
    var searchQuery by remember { mutableStateOf("") }
    val isLoading = products.isEmpty()
    val NeonCyan = Color(0xFF00E5FF)

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            PerfumeriaDrawer(
                authViewModel = authViewModel, // ¡CORREGIDO!
                navController = navController,
                onCloseDrawer = { scope.launch { drawerState.close() } }
            )
        }
    ) {
        BaseScreen(title = "", isDark = false) {
            Box(modifier = Modifier.fillMaxSize().background(Color.White)) {
                Scaffold(containerColor = Color(0xFFF8FAFC)) { innerPadding ->
                    Column(modifier = Modifier.padding(innerPadding).fillMaxSize()) {

                        // Header
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 20.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                Icon(Icons.Default.Menu, "Menu", tint = Color(0xFF2D3436))
                            }
                            Column(modifier = Modifier.weight(1f).padding(horizontal = 12.dp)) {
                                Text("DEPARTAMENTOS", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = NeonCyan)
                                Text("Perfumería Integral", fontSize = 24.sp, fontWeight = FontWeight.Black, color = Color(0xFF2D3436), maxLines = 1)
                            }
                            IconButton(
                                onClick = { navController.navigate(Routes.Cart.route) },
                                modifier = Modifier.size(48.dp).background(Color(0xFFFFF9E6), RoundedCornerShape(14.dp))
                            ) {
                                Icon(Icons.Default.ShoppingCart, null, tint = Color(0xFFFFAB00), modifier = Modifier.size(24.dp))
                            }
                        }

                        // Search
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp).shadow(4.dp, RoundedCornerShape(22.dp)),
                            placeholder = { Text("¿Qué buscas hoy?", color = Color.Gray.copy(alpha = 0.6f)) },
                            leadingIcon = { Icon(Icons.Default.Search, null, tint = NeonCyan) },
                            shape = RoundedCornerShape(22.dp),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = NeonCyan, unfocusedBorderColor = Color.Transparent, focusedContainerColor = Color.White, unfocusedContainerColor = Color.White)
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        ScrollableTabRow(
                            selectedTabIndex = pagerState.currentPage,
                            edgePadding = 24.dp,
                            containerColor = Color.Transparent,
                            divider = {},
                            indicator = { tabPositions ->
                                TabRowDefaults.SecondaryIndicator(Modifier.tabIndicatorOffset(tabPositions[pagerState.currentPage]), height = 3.dp, color = NeonCyan)
                            }
                        ) {
                            categorias.forEachIndexed { index, cat ->
                                Tab(
                                    selected = pagerState.currentPage == index,
                                    onClick = { scope.launch { pagerState.animateScrollToPage(index) } },
                                    text = { Text(cat.uppercase(), color = if (pagerState.currentPage == index) Color(0xFF2D3436) else Color.Gray, fontWeight = FontWeight.Bold, fontSize = 12.sp) }
                                )
                            }
                        }

                        HorizontalPager(state = pagerState, modifier = Modifier.fillMaxSize()) { pageIndex ->
                            val catActual = categorias[pageIndex]
                            val filteredList = products.filter { it.categoria == catActual && (searchQuery.isEmpty() || it.nombre.contains(searchQuery, ignoreCase = true)) }

                            if (isLoading) {
                                LazyColumn(contentPadding = PaddingValues(24.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                                    items(5) { SkeletonProductCard() }
                                }
                            } else {
                                LazyColumn(
                                    modifier = Modifier.fillMaxSize(),
                                    verticalArrangement = Arrangement.spacedBy(14.dp),
                                    contentPadding = PaddingValues(bottom = 120.dp, start = 20.dp, end = 20.dp, top = 10.dp)
                                ) {
                                    items(filteredList) { product ->
                                        ProductCard(product = product, onClick = { navController.navigate(Routes.ProductDetail.createRoute(product.id)) })
                                    }
                                }
                            }
                        }
                    }
                }

                if (isAdmin) {
                    Box(modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 24.dp, start = 20.dp, end = 20.dp)) {
                        Surface(modifier = Modifier.fillMaxWidth(), color = Color.White, shape = RoundedCornerShape(28.dp), shadowElevation = 15.dp) {
                            Row(modifier = Modifier.padding(10.dp).fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround, verticalAlignment = Alignment.CenterVertically) {
                                HomeCompactButton(Icons.Default.Home, "Inicio", NeonCyan) { scope.launch { pagerState.animateScrollToPage(0) } }
                                Box(modifier = Modifier.size(56.dp).clip(CircleShape).background(Color(0xFF7C4DFF)).clickable { navController.navigate(Routes.AdminProducts.route) }, contentAlignment = Alignment.Center) {
                                    Icon(Icons.Default.Settings, null, tint = Color.White, modifier = Modifier.size(28.dp))
                                }
                                HomeCompactButton(Icons.Default.Person, "Perfil", Color.Gray) { Toast.makeText(context, user?.email, Toast.LENGTH_SHORT).show() }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HomeCompactButton(icon: ImageVector, label: String, color: Color, onClick: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.clickable { onClick() }.padding(10.dp)) {
        Icon(icon, null, tint = color, modifier = Modifier.size(26.dp))
        Text(label, fontSize = 9.sp, fontWeight = FontWeight.Bold, color = color)
    }
}

@Composable
fun SkeletonProductCard() {
    val alpha by rememberInfiniteTransition().animateFloat(0.3f, 0.6f, infiniteRepeatable(tween(1000), RepeatMode.Reverse))
    Row(modifier = Modifier.fillMaxWidth().height(115.dp).background(Color.White, RoundedCornerShape(26.dp)).padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.size(85.dp).clip(RoundedCornerShape(20.dp)).background(Color.LightGray.copy(alpha = alpha)))
        Spacer(Modifier.width(16.dp))
        Column {
            Box(modifier = Modifier.width(150.dp).height(18.dp).background(Color.LightGray.copy(alpha = alpha)))
            Spacer(Modifier.height(10.dp))
            Box(modifier = Modifier.width(80.dp).height(14.dp).background(Color.LightGray.copy(alpha = alpha)))
        }
    }
}