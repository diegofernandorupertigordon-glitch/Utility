package app.application.utility.ui.model

import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.PropertyName

data class Product(
    // 🚫 Excluimos el ID de los campos internos del documento de Firebase
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
    var imageUrl: String = ""
) {
    // 🛠️ Mantiene compatibilidad con precios enteros o decimales en Firebase
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