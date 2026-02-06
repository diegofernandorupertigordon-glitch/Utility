package app.application.utility.ui.screens.tienda.admin

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import app.application.utility.ui.components.BaseScreen
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun SalesHistoryScreen(navController: NavController) {
    val viewModel: SalesViewModel = viewModel()
    val sales by viewModel.sales.collectAsState()
    val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    val NeonCyan = Color(0xFF00E5FF)

    val topSelling = remember(sales) {
        sales.flatMap { it.items }
            .groupBy { it["nombre"] as? String ?: "Desconocido" }
            .mapValues { entry ->
                entry.value.sumOf { (it["cantidad"]?.toString()?.toIntOrNull() ?: 0) }
            }
            .toList()
            .sortedByDescending { it.second }
            .take(3)
    }

    BaseScreen(title = "", isDark = false) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.verticalGradient(listOf(Color(0xFFF8FAFC), Color.White)))
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            // --- CABECERA DE ADMINISTRACIÓN ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("ADMINISTRACIÓN", fontSize = 10.sp, fontWeight = FontWeight.Black, color = NeonCyan, letterSpacing = 1.sp)
                    Text("Movimientos", fontSize = 32.sp, fontWeight = FontWeight.Black, color = Color(0xFF2D3436))
                }
                Surface(
                    shape = CircleShape,
                    color = NeonCyan.copy(alpha = 0.1f),
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(Icons.Default.History, null, tint = NeonCyan, modifier = Modifier.padding(12.dp))
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // --- TARJETA DE RESUMEN DE CAJA (DISEÑO PREMIUM) ---
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Color(0xFF1A1F26),
                shape = RoundedCornerShape(24.dp),
                shadowElevation = 8.dp
            ) {
                Row(
                    modifier = Modifier.padding(24.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .background(NeonCyan.copy(alpha = 0.2f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.AttachMoney, null, tint = NeonCyan, modifier = Modifier.size(30.dp))
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text("TOTAL RECAUDADO", color = Color.Gray, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        val total = viewModel.getTotalIngresos()
                        Text("$${String.format(Locale.US, "%.2f", total)}", color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Black)
                    }
                }
            }

            // --- SECCIÓN FAVORITOS (TENDENCIAS) ---
            if (topSelling.isNotEmpty() && topSelling.any { it.second > 0 }) {
                Spacer(modifier = Modifier.height(24.dp))
                Text("TOP VENTAS", fontSize = 12.sp, fontWeight = FontWeight.Black, color = Color.Gray, letterSpacing = 1.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = Color.White,
                    shape = RoundedCornerShape(20.dp),
                    shadowElevation = 2.dp
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        topSelling.forEachIndexed { index, item ->
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text("#${index + 1}", fontWeight = FontWeight.Black, color = NeonCyan, fontSize = 14.sp)
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Text(item.first.uppercase(), fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2D3436))
                                }
                                Surface(color = Color(0xFFF1F5F9), shape = RoundedCornerShape(8.dp)) {
                                    Text("${item.second} uds", modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp), fontSize = 11.sp, fontWeight = FontWeight.Black)
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            Text("HISTORIAL RECIENTE", fontSize = 12.sp, fontWeight = FontWeight.Black, color = Color.Gray, letterSpacing = 1.sp)
            Spacer(modifier = Modifier.height(12.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(14.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(sales) { sale ->
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        color = Color.White,
                        shape = RoundedCornerShape(20.dp),
                        shadowElevation = 1.dp
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text(sdf.format(Date(sale.fecha)), fontSize = 11.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
                                Text("+$${String.format(Locale.US, "%.2f", sale.total)}", fontWeight = FontWeight.Black, color = Color(0xFF00C853), fontSize = 15.sp)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(sale.cliente, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2D3436))

                            HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp), thickness = 0.5.dp, color = Color(0xFFF1F5F9))

                            sale.items.forEach { item ->
                                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 2.dp)) {
                                    Box(modifier = Modifier.size(6.dp).background(NeonCyan, CircleShape))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("${item["nombre"]} (x${item["cantidad"]})", fontSize = 12.sp, color = Color.Gray)
                                }
                            }
                        }
                    }
                }
                item { Spacer(modifier = Modifier.height(20.dp)) }
            }
        }
    }
}