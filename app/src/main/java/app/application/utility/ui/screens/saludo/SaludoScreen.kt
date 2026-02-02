package app.application.utility.ui.screens.saludo

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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import app.application.utility.ui.components.BaseScreen
import app.application.utility.ui.components.CardContainer
import app.application.utility.ui.components.FuturisticButton
import app.application.utility.ui.components.FuturisticTextField

@Composable
fun SaludoScreen(viewModel: SaludoViewModel = viewModel()) {
    val nombre by viewModel.nombre.collectAsState()
    val edad by viewModel.edad.collectAsState()
    val saludo by viewModel.saludo.collectAsState()

    BaseScreen(title = "Saludo Personalizado", isDark = false) {
        AnimatedVisibility(visible = true, enter = fadeIn() + slideInVertically()) {
            Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                Text("SOCIAL", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFF00E5FF))
                Text("Generador Saludo", fontSize = 28.sp, fontWeight = FontWeight.Black, color = Color(0xFF2D3436))

                Spacer(modifier = Modifier.height(20.dp))

                CardContainer {
                    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                        FuturisticTextField(value = nombre, onValueChange = { viewModel.onNombreChange(it) }, label = "Nombre", keyboardType = KeyboardType.Text)
                        FuturisticTextField(value = edad, onValueChange = { viewModel.onEdadChange(it) }, label = "Edad", keyboardType = KeyboardType.Number)

                        FuturisticButton(text = "GENERAR MENSAJE", onClick = { viewModel.onContinuar() })

                        if (saludo.isNotEmpty()) {
                            Text(text = saludo, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF00E5FF))
                        }
                    }
                }
            }
        }
    }
}