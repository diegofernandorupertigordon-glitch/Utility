package app.application.utility.ui.model

import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.PropertyName

data class Product(
    @get:Exclude @set:Exclude
    var id: String = "",

    @get:PropertyName("name")
    @set:PropertyName("name")
    var nombre: String = "",

    @get:PropertyName("description")
    @set:PropertyName("description")
    var descripcion: String = "",

    @get:PropertyName("price")
    var precio: Double = 0.0,

    var presentacionMl: Int = 0,
    var stock: Int = 0,
    var vendidos: Int = 0,
    var imageUrl: String = "",

    // ✨ Nuevo campo para departamentos
    var categoria: String = "🧴 Perfumería",

    // 🏷️ Campo para unidad de medida (ml, gr, oz, und)
    var unidad: String = "ml"
) {
    @PropertyName("price")
    fun setPrecio(value: Any?) {
        this.precio = when (value) {
            is Double -> value
            is Long -> value.toDouble()
            is Float -> value.toDouble()
            else -> 0.0
        }
    }
}