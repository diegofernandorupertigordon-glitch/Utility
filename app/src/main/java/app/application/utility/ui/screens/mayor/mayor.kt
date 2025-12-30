package app.application.utility.ui.screens.mayormenor

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
fun MayorMenorScreen() {

    var num1 by remember { mutableStateOf("") }
    var num2 by remember { mutableStateOf("") }
    var resultado by remember { mutableStateOf("") }

    BaseScreen(title = "Mayor y Menor") {

        AnimatedVisibility(
            visible = true,
            enter = fadeIn() + slideInVertically()
        ) {

            CardContainer {

                Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {

                    FuturisticTextField(
                        value = num1,
                        onValueChange = { if (it.all { c -> c.isDigit() }) num1 = it },
                        label = "Número 1"
                    )

                    FuturisticTextField(
                        value = num2,
                        onValueChange = { if (it.all { c -> c.isDigit() }) num2 = it },
                        label = "Número 2"
                    )

                    Spacer(modifier = androidx.compose.ui.Modifier.height(8.dp))

                    FuturisticButton(
                        text = "Calcular",
                        onClick = {
                            val a = num1.toIntOrNull() ?: 0
                            val b = num2.toIntOrNull() ?: 0
                            resultado =
                                if (a > b) "Mayor: $a | Menor: $b"
                                else "Mayor: $b | Menor: $a"
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
