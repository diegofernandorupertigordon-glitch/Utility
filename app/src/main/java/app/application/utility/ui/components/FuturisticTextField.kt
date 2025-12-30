package app.application.utility.ui.components

// 📐 Layout

// ⌨️ Teclado

// 🎨 Material 3

// 🔁 Compose

// 📏 Dimensiones
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

/**
 * ✨ FuturisticTextField
 *
 * Campo de texto reutilizable y futurista
 * ✔ Compatible con el código actual
 * ✔ Glow neón normal
 * ✔ Glow rojo cuando hay error
 */
@Composable
fun FuturisticTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    keyboardType: KeyboardType = KeyboardType.Number,

    // 🔴 NUEVO (opcional)
    isError: Boolean = false
) {

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        isError = isError,

        // 🏷️ Label
        label = {
            Text(
                text = label,
                color = if (isError)
                    MaterialTheme.colorScheme.error
                else
                    MaterialTheme.colorScheme.primary
            )
        },

        // ⌨️ Teclado
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType
        ),

        // 🔷 Forma futurista
        shape = RoundedCornerShape(16.dp),

        // 🎨 Colores dinámicos
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = if (isError)
                MaterialTheme.colorScheme.error
            else
                MaterialTheme.colorScheme.primary,

            unfocusedBorderColor = if (isError)
                MaterialTheme.colorScheme.error.copy(alpha = 0.6f)
            else
                MaterialTheme.colorScheme.secondary,

            focusedLabelColor = if (isError)
                MaterialTheme.colorScheme.error
            else
                MaterialTheme.colorScheme.primary,

            cursorColor = if (isError)
                MaterialTheme.colorScheme.error
            else
                MaterialTheme.colorScheme.primary
        )
    )
}
