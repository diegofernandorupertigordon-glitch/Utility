package app.application.utility.ui.screens.tienda.admin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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

    BaseScreen(title = "Historial Aromas UIO") {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {

            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.Default.ArrowBack, contentDescription = null, tint = Color(0xFF00E5FF))
            }

            // --- RESUMEN DE CAJA ---
            Card(
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF2D3436)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier.padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.AttachMoney, null, tint = Color(0xFF00E5FF), modifier = Modifier.size(40.dp))
                    Column {
                        Text("TOTAL RECAUDADO", color = Color.Gray, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        val total = viewModel.getTotalIngresos()
                        Text("$${String.format("%.2f", total)}", color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Black)
                    }
                }
            }

            // --- SECCIÓN LOS FAVORITOS ---
            if (topSelling.isNotEmpty() && topSelling.any { it.second > 0 }) {
                Text("LOS FAVORITOS", fontSize = 14.sp, fontWeight = FontWeight.Black, modifier = Modifier.padding(vertical = 8.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F2F6)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        topSelling.forEach { item ->
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Star, null, tint = Color(0xFFFFAB00), modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(item.first, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                                }
                                Text("${item.second} unidades", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            Text("ÚLTIMOS MOVIMIENTOS", fontSize = 14.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(vertical = 12.dp))

            // --- LISTA DE VENTAS ---
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxSize()) {
                items(sales) { sale ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(2.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text(sdf.format(Date(sale.fecha)), fontSize = 12.sp, color = Color.Gray)
                                Text("+$${String.format("%.2f", sale.total)}", fontWeight = FontWeight.Black, color = Color(0xFF00C853))
                            }
                            Text("Cliente: ${sale.cliente}", fontSize = 14.sp, fontWeight = FontWeight.Medium)

                            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), thickness = 0.5.dp)

                            sale.items.forEach { item ->
                                Text("• ${item["nombre"]} (x${item["cantidad"]})", fontSize = 12.sp, color = Color.DarkGray)
                            }
                        }
                    }
                }
            }
        }
    }
}