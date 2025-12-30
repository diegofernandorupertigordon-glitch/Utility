package app.application.utility.ui.screens.calculadora

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
fun CalculadoraScreen() {

    var num1 by remember { mutableStateOf("") }
    var num2 by remember { mutableStateOf("") }
    var resultado by remember { mutableStateOf("") }

    BaseScreen(title = "Calculadora") {

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
                        text = "Sumar",
                        onClick = {
                            resultado =
                                ((num1.toDoubleOrNull() ?: 0.0) +
                                        (num2.toDoubleOrNull() ?: 0.0)).toString()
                        }
                    )

                    FuturisticButton(
                        text = "Restar",
                        onClick = {
                            resultado =
                                ((num1.toDoubleOrNull() ?: 0.0) -
                                        (num2.toDoubleOrNull() ?: 0.0)).toString()
                        }
                    )

                    FuturisticButton(
                        text = "Multiplicar",
                        onClick = {
                            resultado =
                                ((num1.toDoubleOrNull() ?: 0.0) *
                                        (num2.toDoubleOrNull() ?: 0.0)).toString()
                        }
                    )

                    if (resultado.isNotEmpty()) {
                        androidx.compose.material3.Text(
                            text = "Resultado: $resultado",
                            style = androidx.compose.material3.MaterialTheme.typography.titleLarge
                        )
                    }
                }
            }
        }
    }
}
