package app.application.utility.ui.screens.tienda.cart

import androidx.lifecycle.ViewModel
import app.application.utility.ui.model.Product
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class CartItem(
    val product: Product,
    val quantity: Int
)

class CartViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems

    fun addProduct(product: Product) {
        val currentItems = _cartItems.value.toMutableList()
        val index = currentItems.indexOfFirst { it.product.id == product.id }

        if (index >= 0) {
            val item = currentItems[index]
            currentItems[index] = item.copy(quantity = item.quantity + 1)
        } else {
            currentItems.add(CartItem(product, 1))
        }
        _cartItems.value = currentItems
    }

    fun removeProduct(productId: String) {
        _cartItems.value = _cartItems.value.filter { it.product.id != productId }
    }

    fun clearCart() {
        _cartItems.value = emptyList()
    }

    fun total(): Double {
        return _cartItems.value.sumOf { it.product.precio * it.quantity.toDouble() }
    }

    fun checkout(onSuccess: () -> Unit, onError: (String) -> Unit) {
        val itemsToProcess = _cartItems.value
        val userEmail = auth.currentUser?.email ?: "Anónimo"
        val totalVenta = total()

        // Evitar decimales infinitos
        val totalFormateado = String.format("%.2f", totalVenta).replace(",", ".").toDouble()

        if (itemsToProcess.isEmpty()) return

        db.runTransaction { transaction ->
            val productSnapshots = itemsToProcess.map { item ->
                val productRef = db.collection("products").document(item.product.id)
                val snapshot = transaction.get(productRef)
                Triple(productRef, snapshot, item)
            }

            for ((productRef, snapshot, item) in productSnapshots) {
                val currentStock = snapshot.getLong("stock") ?: 0L
                val currentVendidos = snapshot.getLong("vendidos") ?: 0L

                if (currentStock >= item.quantity) {
                    transaction.update(productRef, "stock", currentStock - item.quantity)
                    transaction.update(productRef, "vendidos", currentVendidos + item.quantity)
                } else {
                    throw Exception("Stock insuficiente para: ${item.product.nombre}")
                }
            }

            val saleRef = db.collection("sales").document()
            val saleData = hashMapOf(
                "id" to saleRef.id,
                "cliente" to userEmail,
                "total" to totalFormateado,
                "fecha" to System.currentTimeMillis(),
                "items" to itemsToProcess.map {
                    mapOf(
                        "nombre" to it.product.nombre,
                        "cantidad" to it.quantity,
                        "precioUnitario" to it.product.precio
                    )
                }
            )
            transaction.set(saleRef, saleData)
        }.addOnSuccessListener {
            clearCart()
            onSuccess()
        }.addOnFailureListener { e ->
            val errorMsg = e.message ?: "Error al procesar la compra"
            onError(errorMsg)
        }
    }
}