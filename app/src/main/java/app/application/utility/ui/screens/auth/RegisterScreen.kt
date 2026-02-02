package app.application.utility.ui.screens.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import app.application.utility.ui.components.BaseScreen
import app.application.utility.ui.components.CardContainer
import app.application.utility.ui.components.FuturisticButton
import app.application.utility.ui.components.FuturisticTextField
import app.application.utility.ui.navigation.Routes

@Composable
fun RegisterScreen(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val authViewModel: AuthViewModel = viewModel()
    val firebaseError by authViewModel.error.collectAsState()

    BaseScreen(title = "Registro", isDark = false) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            Text("NUEVA CUENTA", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF00E5FF))
            Text("Únete a Nosotros", fontSize = 32.sp, fontWeight = FontWeight.Black, color = Color(0xFF2D3436))
            Text("Crea tu cuenta para acceder a todos los servicios", fontSize = 14.sp, color = Color.Gray)

            Spacer(modifier = Modifier.height(32.dp))

            CardContainer {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    FuturisticTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = "Correo Electrónico",
                        keyboardType = KeyboardType.Email
                    )

                    FuturisticTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = "Contraseña (6+ caracteres)",
                        keyboardType = KeyboardType.Password
                    )

                    if (firebaseError.isNotEmpty()) {
                        Text(text = firebaseError, color = MaterialTheme.colorScheme.error, fontSize = 12.sp)
                    }

                    FuturisticButton(
                        text = "CREAR CUENTA",
                        onClick = {
                            authViewModel.register(email, password) {
                                // 🔄 REDIRECCIÓN AL SELECTOR (Opción A)
                                navController.navigate(Routes.Selector.route) {
                                    popUpTo(Routes.Register.route) { inclusive = true }
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            TextButton(onClick = { navController.popBackStack() }) {
                Text("¿Ya tienes cuenta? Inicia sesión", color = Color(0xFF00E5FF), fontWeight = FontWeight.Bold)
            }
        }
    }
}