package app.application.utility.ui.screens.primo

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
fun PrimoScreen() {

    var numero by remember { mutableStateOf("") }
    var resultado by remember { mutableStateOf("") }

    BaseScreen(title = "Número Primo") {

        AnimatedVisibility(
            visible = true,
            enter = fadeIn() + slideInVertically()
        ) {

            CardContainer {

                Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {

                    FuturisticTextField(
                        value = numero,
                        onValueChange = { if (it.all { c -> c.isDigit() }) numero = it },
                        label = "Número"
                    )

                    Spacer(modifier = androidx.compose.ui.Modifier.height(8.dp))

                    FuturisticButton(
                        text = "Verificar",
                        onClick = {
                            val n = numero.toIntOrNull() ?: 0
                            resultado =
                                if (esPrimo(n)) "Es un número primo"
                                else "No es un número primo"
                        }
                    )

                    if (resultado.isNotEmpty()) {
                        androidx.compose.material3.Text(
                            text = resultado,
                            style = androidx.compose.material3.MaterialTheme.typography.titleMedium
                        )
                    }
                }
            }
        }
    }
}

fun esPrimo(n: Int): Boolean {
    if (n < 2) return false
    for (i in 2 until n) {
        if (n % i == 0) return false
    }
    return true
}
