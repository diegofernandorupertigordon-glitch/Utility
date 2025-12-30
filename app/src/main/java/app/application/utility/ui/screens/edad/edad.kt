package app.application.utility.ui.screens.edad

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import app.application.utility.ui.components.BaseScreen
import app.application.utility.ui.components.CardContainer
import app.application.utility.ui.components.FuturisticButton
import app.application.utility.ui.components.FuturisticTextField

@Composable
fun EdadScreen() {

    var nombre by remember { mutableStateOf("") }
    var edad by remember { mutableStateOf("") }
    var mensaje by remember { mutableStateOf("") }
    var mostrar by remember { mutableStateOf(false) }

    BaseScreen(title = "Verificar Edad") {

        AnimatedVisibility(
            visible = true,
            enter = fadeIn() + slideInVertically()
        ) {

            // 🔥 CARD FUTURISTA (NO Card normal)
            CardContainer {

                Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {

                    // ✨ INPUT FUTURISTA
                    FuturisticTextField(
                        value = nombre,
                        onValueChange = {
                            if (it.all { c -> c.isLetter() || c.isWhitespace() }) {
                                nombre = it
                            }
                        },
                        label = "Nombre"
                    )

                    FuturisticTextField(
                        value = edad,
                        onValueChange = {
                            if (it.all { c -> c.isDigit() }) {
                                edad = it
                            }
                        },
                        label = "Edad"
                    )

                    Spacer(modifier = androidx.compose.ui.Modifier.height(8.dp))

                    // 🔘 BOTÓN FUTURISTA
                    FuturisticButton(
                        text = "Verificar",
                        onClick = {
                            val e = edad.toIntOrNull() ?: 0
                            mensaje =
                                if (e >= 18) "$nombre, eres mayor de edad"
                                else "$nombre, eres menor de edad"
                            mostrar = true
                        }
                    )
                }
            }
        }
    }

    if (mostrar) {
        androidx.compose.material3.AlertDialog(
            onDismissRequest = { mostrar = false },
            confirmButton = {
                FuturisticButton(
                    text = "Aceptar",
                    onClick = { mostrar = false }
                )
            },
            text = { androidx.compose.material3.Text(mensaje) }
        )
    }
}
