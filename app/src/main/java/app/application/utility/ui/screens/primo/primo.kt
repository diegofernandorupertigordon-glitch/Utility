package app.application.utility.ui.screens.primo

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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.application.utility.ui.components.BaseScreen
import app.application.utility.ui.components.CardContainer
import app.application.utility.ui.components.FuturisticButton
import app.application.utility.ui.components.FuturisticTextField

@Composable
fun PrimoScreen() {
    var numero by remember { mutableStateOf("") }
    var resultado by remember { mutableStateOf("") }

    BaseScreen(title = "Número Primo", isDark = false) {
        AnimatedVisibility(visible = true, enter = fadeIn() + slideInVertically()) {
            Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                Text("MATEMÁTICAS", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFF00E5FF))
                Text("Analizador Primo", fontSize = 28.sp, fontWeight = FontWeight.Black, color = Color(0xFF2D3436))

                Spacer(modifier = Modifier.height(20.dp))

                CardContainer {
                    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                        FuturisticTextField(value = numero, onValueChange = { if (it.all { c -> c.isDigit() }) numero = it }, label = "Ingresa un número")
                        FuturisticButton(text = "ANALIZAR", onClick = {
                            val n = numero.toIntOrNull() ?: 0
                            resultado = if (esPrimo(n)) "✨ $n es un número PRIMO" else "❌ $n no es un número primo"
                        })

                        if (resultado.isNotEmpty()) {
                            Text(text = resultado, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2D3436))
                        }
                    }
                }
            }
        }
    }
}

fun esPrimo(n: Int): Boolean {
    if (n < 2) return false
    for (i in 2 until n) { if (n % i == 0) return false }
    return true
}