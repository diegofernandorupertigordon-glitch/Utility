package app.application.utility.ui.screens.tienda.detail

import androidx.lifecycle.ViewModel
import app.application.utility.ui.model.Product
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ProductDetailViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()

    private val _product = MutableStateFlow<Product?>(null)
    val product: StateFlow<Product?> = _product

    /**
     * Carga los detalles de un producto específico desde Firestore.
     * Al usar toObject(Product::class.java), se mapean automáticamente
     * todos los campos, incluyendo los nuevos (unidad, categoría, etc.)
     */
    fun loadProduct(productId: String) {
        db.collection("products")
            .document(productId)
            .get()
            .addOnSuccessListener { doc ->
                if (doc.exists()) {
                    // Mantenemos la lógica de copiar el ID del documento para asegurar la navegación
                    _product.value = doc.toObject(Product::class.java)?.copy(id = doc.id)
                }
            }
            .addOnFailureListener {
                // Se mantiene el espacio para manejo de errores
            }
    }
}