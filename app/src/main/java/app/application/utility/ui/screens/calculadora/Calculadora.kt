package app.application.utility.ui.screens.calculadora

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.application.utility.ui.components.BaseScreen
import app.application.utility.ui.components.CardContainer
import app.application.utility.ui.components.FuturisticButton
import app.application.utility.ui.components.FuturisticTextField

@Composable
fun CalculadoraScreen() {
    var num1 by remember { mutableStateOf("") }
    var num2 by remember { mutableStateOf("") }
    var resultado by remember { mutableStateOf("") }

    BaseScreen(title = "Calculadora", isDark = false) {
        AnimatedVisibility(visible = true, enter = fadeIn() + slideInVertically()) {
            Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                Text("OPERACIONES", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFF00E5FF))
                Text("Cálculo Rápido", fontSize = 28.sp, fontWeight = FontWeight.Black, color = Color(0xFF2D3436))

                Spacer(modifier = Modifier.height(20.dp))

                CardContainer {
                    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                        FuturisticTextField(value = num1, onValueChange = { if (it.all { c -> c.isDigit() || c == '.' }) num1 = it }, label = "Primer Número")
                        FuturisticTextField(value = num2, onValueChange = { if (it.all { c -> c.isDigit() || c == '.' }) num2 = it }, label = "Segundo Número")

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Box(modifier = Modifier.weight(1f)) { FuturisticButton("➕", onClick = { resultado = ((num1.toDoubleOrNull() ?: 0.0) + (num2.toDoubleOrNull() ?: 0.0)).toString() }) }
                            Box(modifier = Modifier.weight(1f)) { FuturisticButton("➖", onClick = { resultado = ((num1.toDoubleOrNull() ?: 0.0) - (num2.toDoubleOrNull() ?: 0.0)).toString() }) }
                        }
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Box(modifier = Modifier.weight(1f)) { FuturisticButton("✖️", onClick = { resultado = ((num1.toDoubleOrNull() ?: 0.0) * (num2.toDoubleOrNull() ?: 0.0)).toString() }) }
                            Box(modifier = Modifier.weight(1f)) { FuturisticButton("➗", onClick = {
                                val n2 = num2.toDoubleOrNull() ?: 0.0
                                resultado = if(n2 != 0.0) (num1.toDoubleOrNull()!! / n2).toString() else "Error"
                            }) }
                        }

                        if (resultado.isNotEmpty()) {
                            Text(text = "Resultado: $resultado", fontSize = 22.sp, fontWeight = FontWeight.Black, color = Color(0xFF00E5FF), modifier = Modifier.padding(top = 10.dp))
                        }
                    }
                }
            }
        }
    }
}