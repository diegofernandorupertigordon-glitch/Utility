package app.application.utility.ui.screens.mayormenor

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
fun MayorMenorScreen() {
    var num1 by remember { mutableStateOf("") }
    var num2 by remember { mutableStateOf("") }
    var resultado by remember { mutableStateOf("") }

    BaseScreen(title = "Comparador", isDark = false) {
        AnimatedVisibility(visible = true, enter = fadeIn() + slideInVertically()) {
            Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                Text("LÓGICA", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFF00E5FF))
                Text("Mayor o Menor", fontSize = 28.sp, fontWeight = FontWeight.Black, color = Color(0xFF2D3436))

                Spacer(modifier = Modifier.height(20.dp))

                CardContainer {
                    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                        FuturisticTextField(
                            value = num1,
                            onValueChange = { if (it.all { c -> c.isDigit() || c == '-' }) num1 = it },
                            label = "Número A",
                            keyboardType = KeyboardType.Number
                        )
                        FuturisticTextField(
                            value = num2,
                            onValueChange = { if (it.all { c -> c.isDigit() || c == '-' }) num2 = it },
                            label = "Número B",
                            keyboardType = KeyboardType.Number
                        )

                        FuturisticButton(text = "COMPARAR VALORES", onClick = {
                            val a = num1.toIntOrNull()
                            val b = num2.toIntOrNull()

                            resultado = if (a != null && b != null) {
                                when {
                                    a > b -> "🏆 El número mayor es $a y el número menor es $b."
                                    b > a -> "🏆 El número mayor es $b y el número menor es $a."
                                    else -> "⚖️ Ambos números son iguales ($a)."
                                }
                            } else {
                                "⚠️ Ingresa números válidos en ambos campos."
                            }
                        })

                        if (resultado.isNotEmpty()) {
                            Text(
                                text = resultado,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFF2D3436),
                                lineHeight = 24.sp,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}