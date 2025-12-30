package app.application.utility.ui.screens.saludo

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * 🧠 ViewModel del Saludo
 * Maneja nombre, edad y mensaje
 */
class SaludoViewModel : ViewModel() {

    // 🔒 Estados privados
    private val _nombre = MutableStateFlow("")
    private val _edad = MutableStateFlow("")
    private val _saludo = MutableStateFlow("")

    // 🔓 Estados públicos
    val nombre: StateFlow<String> = _nombre
    val edad: StateFlow<String> = _edad
    val saludo: StateFlow<String> = _saludo

    // ✏️ Actualiza nombre
    fun onNombreChange(valor: String) {
        _nombre.value = valor
    }

    // ✏️ Actualiza edad
    fun onEdadChange(valor: String) {
        _edad.value = valor
    }

    // 🚀 Acción del botón
    fun onContinuar() {
        val nombreTexto = _nombre.value
        val edadNumero = _edad.value.toIntOrNull()

        _saludo.value =
            if (nombreTexto.isBlank() || edadNumero == null) {
                "Por favor ingresa un nombre y una edad válida"
            } else {
                val categoria = when {
                    edadNumero <= 12 -> "un niño"
                    edadNumero <= 17 -> "un joven"
                    edadNumero <= 59 -> "un adulto"
                    else -> "un adulto mayor"
                }
                "Hola $nombreTexto, tienes $edadNumero años, eres $categoria"
            }
    }
}
