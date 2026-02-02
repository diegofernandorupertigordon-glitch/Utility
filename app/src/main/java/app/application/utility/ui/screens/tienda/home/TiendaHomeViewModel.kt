package app.application.utility.ui.screens.tienda.home

import androidx.lifecycle.ViewModel
import app.application.utility.ui.model.Product
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class TiendaHomeViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()

    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products

    private var listener: ListenerRegistration? = null

    init {
        listenProducts()
    }

    private fun listenProducts() {
        listener = db.collection("products")
            .addSnapshotListener { snapshot, error ->

                if (error != null || snapshot == null) {
                    _products.value = emptyList()
                    return@addSnapshotListener
                }

                val list = snapshot.documents.mapNotNull { doc ->
                    doc.toObject(Product::class.java)?.copy(id = doc.id)
                }

                _products.value = list
            }
    }

    override fun onCleared() {
        super.onCleared()
        listener?.remove()
    }
}
