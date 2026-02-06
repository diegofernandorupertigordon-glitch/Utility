package app.application.utility.ui.screens.tienda.admin

import androidx.lifecycle.ViewModel
import app.application.utility.ui.model.Sale
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * 📊 SalesViewModel
 * Optimizado para reportes rápidos y lectura segura de Firebase.
 */
class SalesViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val _sales = MutableStateFlow<List<Sale>>(emptyList())
    val sales: StateFlow<List<Sale>> = _sales

    init {
        fetchSales()
    }

    private fun fetchSales() {
        // Mantenemos la lógica original: Últimas 100 ventas ordenadas por fecha
        db.collection("sales")
            .orderBy("fecha", Query.Direction.DESCENDING)
            .limit(100)
            .addSnapshotListener { snapshot, error ->
                if (error != null) return@addSnapshotListener

                if (snapshot != null) {
                    val list = snapshot.documents.mapNotNull { doc ->
                        try {
                            val cliente = doc.getString("cliente") ?: "Anónimo"
                            val fecha = doc.getLong("fecha") ?: 0L

                            // 🛒 Manejo de items: Aseguramos que sea una lista
                            val items = doc.get("items") as? List<Map<String, Any>> ?: emptyList()

                            // 💰 PARSEO BLINDADO:
                            val totalRaw = doc.get("total")
                            val totalParsed = totalRaw?.toString()?.toDoubleOrNull() ?: 0.0

                            Sale(
                                id = doc.id,
                                cliente = cliente,
                                total = totalParsed,
                                fecha = fecha,
                                items = items
                            )
                        } catch (e: Exception) {
                            null
                        }
                    }
                    _sales.value = list
                }
            }
    }

    /**
     * Calcula ingresos totales sumando la lista actual en memoria.
     */
    fun getTotalIngresos(): Double = _sales.value.sumOf { it.total }

    /**
     * Filtra ventas por nombre de cliente.
     */
    fun getSalesByClient(query: String): List<Sale> {
        return if (query.isEmpty()) _sales.value
        else _sales.value.filter { it.cliente.contains(query, ignoreCase = true) }
    }
}