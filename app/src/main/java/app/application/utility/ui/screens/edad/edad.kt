package app.application.utility.ui.screens.edad

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

    BaseScreen(title = "Verificar Edad", isDark = false) {
        AnimatedVisibility(visible = true, enter = fadeIn() + slideInVertically()) {
            Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                Text("SISTEMA", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFF00E5FF))
                Text("Validar Identidad", fontSize = 28.sp, fontWeight = FontWeight.Black, color = Color(0xFF2D3436))

                Spacer(modifier = Modifier.height(20.dp))

                CardContainer {
                    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                        // ✅ Corregido: Permite texto y espacios, teclado de texto
                        FuturisticTextField(
                            value = nombre,
                            onValueChange = { if (it.all { c -> c.isLetter() || c.isWhitespace() }) nombre = it },
                            label = "Tu Nombre",
                            keyboardType = KeyboardType.Text
                        )

                        // ✅ Teclado numérico para edad
                        FuturisticTextField(
                            value = edad,
                            onValueChange = { if (it.all { c -> c.isDigit() }) edad = it },
                            label = "Tu Edad",
                            keyboardType = KeyboardType.Number
                        )

                        FuturisticButton(text = "VERIFICAR AHORA", onClick = {
                            if (nombre.isBlank() || edad.isBlank()) {
                                mensaje = "Por favor, completa todos los campos."
                            } else {
                                val e = edad.toIntOrNull() ?: 0
                                mensaje = if (e >= 18) "Acceso concedido, $nombre. Eres mayor de edad."
                                else "Acceso restringido, $nombre. Eres menor de edad."
                            }
                            mostrar = true
                        })
                    }
                }
            }
        }
    }

    if (mostrar) {
        AlertDialog(
            onDismissRequest = { mostrar = false },
            confirmButton = {
                FuturisticButton(text = "ENTENDIDO", onClick = { mostrar = false })
            },
            title = { Text("Resultado", fontWeight = FontWeight.Black) },
            text = { Text(mensaje) },
            containerColor = Color.White,
            shape = RoundedCornerShape(24.dp)
        )
    }
}