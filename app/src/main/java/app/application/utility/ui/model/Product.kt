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

    // Precio excluido del mapeo directo para usar los métodos manuales abajo
    @get:Exclude
    @set:Exclude
    var precio: Double = 0.0,

    var presentacionMl: Int = 0,
    var stock: Int = 0,
    var vendidos: Int = 0,
    var imageUrl: String = "",

    // ✨ Nuevo campo para departamentos
    var categoria: String = "🧴 Perfumería",

    // 🏷️ Campo para unidad de medida
    var unidad: String = "ml"
) {
    /**
     * CRÍTICO: Maneja la entrada de datos desde Firebase (Long, Int, Double)
     * vinculándolo a la etiqueta "price".
     */
    @PropertyName("price")
    fun setPrecioFirebase(value: Any?) {
        this.precio = when (value) {
            is Double -> value
            is Long -> value.toDouble()
            is Float -> value.toDouble()
            is Int -> value.toDouble()
            else -> 0.0
        }
    }

    /**
     * Getter manual para que Firebase siempre busque y escriba la etiqueta "price"
     */
    @PropertyName("price")
    fun getPrecioFirebase(): Double = precio
}