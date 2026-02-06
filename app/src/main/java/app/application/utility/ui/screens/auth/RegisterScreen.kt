package app.application.utility.ui.screens.auth

import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Brush
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
    val NeonCyan = Color(0xFF00E5FF)

    BaseScreen(title = "", isDark = false) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.verticalGradient(listOf(Color(0xFFF8FAFC), Color.White)))
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(60.dp))

            Text("NUEVA CUENTA", fontSize = 12.sp, fontWeight = FontWeight.Black, color = NeonCyan, letterSpacing = 2.sp)
            Text("Únete a Nosotros", fontSize = 36.sp, fontWeight = FontWeight.Black, color = Color(0xFF2D3436))
            Text("Accede a un catálogo de fragancias exclusivas", fontSize = 14.sp, color = Color.Gray, modifier = Modifier.padding(top = 8.dp))

            Spacer(modifier = Modifier.height(48.dp))

            CardContainer {
                Column(verticalArrangement = Arrangement.spacedBy(18.dp)) {
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
                                navController.navigate(Routes.Selector.route) {
                                    popUpTo(Routes.Register.route) { inclusive = true }
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            TextButton(onClick = { navController.popBackStack() }) {
                Text("¿YA TIENES CUENTA? INICIA SESIÓN", color = Color.Gray, fontWeight = FontWeight.Black, fontSize = 13.sp)
            }
        }
    }
}