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

    fun loadProduct(productId: String) {
        db.collection("products")
            .document(productId)
            .get()
            .addOnSuccessListener { doc ->
                if (doc.exists()) {
                    _product.value = doc.toObject(Product::class.java)?.copy(id = doc.id)
                }
            }
            .addOnFailureListener {
                // Aquí podrías manejar el error si lo deseas
            }
    }
}