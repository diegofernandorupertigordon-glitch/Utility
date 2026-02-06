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

    // Cambiamos el nombre interno para que no choque con los métodos manuales
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
     * sin que la aplicación se cierre.
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
     * Getter manual para que Firebase siempre busque la etiqueta "price"
     */
    @PropertyName("price")
    fun getPrecioFirebase(): Double = precio
}