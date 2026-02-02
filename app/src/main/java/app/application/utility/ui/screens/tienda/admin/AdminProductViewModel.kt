package app.application.utility.ui.screens.tienda.admin

import androidx.lifecycle.ViewModel
import app.application.utility.ui.model.Product
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AdminProductViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products

    private val _isAdmin = MutableStateFlow<Boolean?>(null)
    val isAdmin: StateFlow<Boolean?> = _isAdmin

    private var listener: ListenerRegistration? = null

    init {
        checkAdmin()
        listenProducts()
    }

    private fun checkAdmin() {
        val user = auth.currentUser
        val adminEmail = "diegoruperti1987@hotmail.com"

        if (user == null) {
            _isAdmin.value = false
            return
        }

        // Primero verificamos por email (Acceso inmediato para ti)
        if (user.email?.lowercase() == adminEmail) {
            _isAdmin.value = true
        } else {
            // Verificación secundaria en la colección de usuarios si fuera necesario
            db.collection("users").document(user.uid).get()
                .addOnSuccessListener { doc ->
                    _isAdmin.value = doc.getBoolean("isAdmin") == true
                }
                .addOnFailureListener {
                    _isAdmin.value = false
                }
        }
    }

    private fun listenProducts() {
        listener = db.collection("products")
            .addSnapshotListener { snapshot, error ->
                if (error != null || snapshot == null) return@addSnapshotListener

                val list = snapshot.documents.mapNotNull { doc ->
                    // 🔑 Convertimos el documento a objeto y asignamos manualmente el ID del documento
                    val p = doc.toObject(Product::class.java)
                    p?.id = doc.id
                    p
                }
                _products.value = list
            }
    }

    fun addProduct(product: Product) {
        // Firebase ignorará el campo 'id' gracias a @get:Exclude
        db.collection("products").add(product)
    }

    fun updateProduct(product: Product) {
        if (product.id.isBlank()) return
        // Actualiza el documento usando su ID sin crear un campo 'id' adentro
        db.collection("products").document(product.id).set(product)
    }

    fun deleteProduct(productId: String) {
        if (productId.isBlank()) return
        db.collection("products").document(productId).delete()
    }

    // ✅ FUNCIONES DE REPORTES (Dashboard)
    fun calcularGananciasTotales(): Double {
        return _products.value.sumOf { it.precio * it.vendidos }
    }

    fun obtenerTotalProductosVendidos(): Int {
        return _products.value.sumOf { it.vendidos }
    }

    override fun onCleared() {
        super.onCleared()
        listener?.remove()
    }
}