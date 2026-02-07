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

    /**
     * Verifica permisos de administrador.
     * Prioriza tu correo personal para acceso garantizado.
     */
    private fun checkAdmin() {
        val user = auth.currentUser
        val adminEmail = "diegoruperti1987@hotmail.com"

        if (user == null) {
            _isAdmin.value = false
            return
        }

        // Verificación inmediata por Email
        if (user.email?.lowercase() == adminEmail) {
            _isAdmin.value = true
        } else {
            // Verificación de respaldo en base de datos
            db.collection("users").document(user.uid).get()
                .addOnSuccessListener { doc ->
                    _isAdmin.value = doc.getBoolean("isAdmin") == true
                }
                .addOnFailureListener {
                    _isAdmin.value = false
                }
        }
    }

    /**
     * Escucha cambios en la colección de productos en tiempo real.
     */
    private fun listenProducts() {
        listener = db.collection("products")
            .addSnapshotListener { snapshot, error ->
                if (error != null || snapshot == null) return@addSnapshotListener

                val list = snapshot.documents.mapNotNull { doc ->
                    // Mapeo automático de campos (incluyendo unidad, categoria, etc.)
                    val p = doc.toObject(Product::class.java)
                    p?.id = doc.id
                    p
                }
                _products.value = list
            }
    }

    /**
     * Agrega un nuevo producto.
     * Firestore genera el ID automáticamente.
     */
    fun addProduct(product: Product) {
        db.collection("products").add(product)
    }

    /**
     * Actualiza un producto existente.
     * SE CORRIGE: Usa update con llaves en inglés para no borrar "vendidos" ni otros campos.
     */
    fun updateProduct(product: Product) {
        if (product.id.isBlank()) return

        val updates = hashMapOf<String, Any>(
            "name" to product.nombre,
            "description" to product.descripcion,
            "price" to product.precio,
            "stock" to product.stock,
            "presentacionMl" to product.presentacionMl,
            "imageUrl" to product.imageUrl,
            "categoria" to product.categoria,
            "unidad" to product.unidad
        )

        db.collection("products").document(product.id).update(updates)
    }

    /**
     * Elimina un producto de la base de datos.
     */
    fun deleteProduct(productId: String) {
        if (productId.isBlank()) return
        db.collection("products").document(productId).delete()
    }

    // --- FUNCIONES DE ANALÍTICA PARA EL DASHBOARD ---

    /**
     * Calcula las ganancias brutas basadas en los productos vendidos.
     */
    fun calcularGananciasTotales(): Double {
        return _products.value.sumOf { it.precio * it.vendidos }
    }

    /**
     * Retorna el conteo total de unidades vendidas históricamente.
     */
    fun obtenerTotalProductosVendidos(): Int {
        return _products.value.sumOf { it.vendidos }
    }

    override fun onCleared() {
        super.onCleared()
        listener?.remove() // Limpieza del listener para evitar fugas de memoria
    }
}