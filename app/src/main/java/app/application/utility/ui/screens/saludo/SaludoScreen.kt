package app.application.utility.ui.screens.saludo

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.text.input.KeyboardType   // ✅ IMPORTANTE
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import app.application.utility.ui.components.BaseScreen
import app.application.utility.ui.components.CardContainer
import app.application.utility.ui.components.FuturisticButton
import app.application.utility.ui.components.FuturisticTextField

/**
 * 👋 Pantalla Saludo integrada a Utility App
 */
@Composable
fun SaludoScreen(
    viewModel: SaludoViewModel = viewModel()
) {

    // 📡 Estados desde el ViewModel
    val nombre by viewModel.nombre.collectAsState()
    val edad by viewModel.edad.collectAsState()
    val saludo by viewModel.saludo.collectAsState()

    // 🧱 Usa la BaseScreen existente
    BaseScreen(title = "Saludo") {

        AnimatedVisibility(
            visible = true,
            enter = fadeIn() + slideInVertically()
        ) {

            CardContainer {

                Column(
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {

                    // ✨ Nombre → TECLADO TEXTO
                    FuturisticTextField(
                        value = nombre,
                        onValueChange = {
                            if (it.all { c -> c.isLetter() || c.isWhitespace() })
                                viewModel.onNombreChange(it)
                        },
                        label = "Nombre",
                        keyboardType = KeyboardType.Text // ✅ CLAVE
                    )

                    // 🔢 Edad → TECLADO NUMÉRICO
                    FuturisticTextField(
                        value = edad,
                        onValueChange = {
                            if (it.all { c -> c.isDigit() })
                                viewModel.onEdadChange(it)
                        },
                        label = "Edad",
                        keyboardType = KeyboardType.Number
                    )

                    Spacer(modifier = androidx.compose.ui.Modifier.height(8.dp))

                    // 🚀 Botón
                    FuturisticButton(
                        text = "Continuar",
                        onClick = {
                            viewModel.onContinuar()
                        }
                    )

                    // 📢 Resultado
                    if (saludo.isNotEmpty()) {
                        androidx.compose.material3.Text(
                            text = saludo,
                            style = androidx.compose.material3.MaterialTheme.typography.titleMedium
                        )
                    }
                }
            }
        }
    }
}
