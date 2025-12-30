package app.application.utility.ui.screens.auth

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import app.application.utility.ui.components.BaseScreen
import app.application.utility.ui.components.CardContainer
import app.application.utility.ui.components.FuturisticButton
import app.application.utility.ui.components.FuturisticTextField
import app.application.utility.ui.navigation.Routes

@Composable
fun RegisterScreen(navController: NavController) {

    // 📌 Estados del formulario
    var nombre by remember { mutableStateOf("") }
    var edad by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // 🔴 Estados visuales (glow rojo)
    var nombreError by remember { mutableStateOf(false) }
    var edadError by remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf(false) }
    var passwordError by remember { mutableStateOf(false) }

    // 🔥 ViewModel Firebase (YA EXISTE)
    val authViewModel: AuthViewModel = viewModel()
    val firebaseError by authViewModel.error.collectAsState()

    BaseScreen(title = "Registro") {

        AnimatedVisibility(
            visible = true,
            enter = fadeIn() + slideInVertically()
        ) {

            CardContainer {

                Column(
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {

                    // 👤 Nombre
                    FuturisticTextField(
                        value = nombre,
                        onValueChange = {
                            nombre = it
                            nombreError = false
                        },
                        label = "Nombre",
                        keyboardType = KeyboardType.Text,
                        isError = nombreError
                    )

                    // 🔢 Edad
                    FuturisticTextField(
                        value = edad,
                        onValueChange = {
                            if (it.all { c -> c.isDigit() }) {
                                edad = it
                                edadError = false
                            }
                        },
                        label = "Edad",
                        keyboardType = KeyboardType.Number,
                        isError = edadError
                    )

                    // 📧 Email
                    FuturisticTextField(
                        value = email,
                        onValueChange = {
                            email = it
                            emailError = false
                        },
                        label = "Email",
                        keyboardType = KeyboardType.Email,
                        isError = emailError
                    )

                    // 🔒 Password
                    FuturisticTextField(
                        value = password,
                        onValueChange = {
                            password = it
                            passwordError = false
                        },
                        label = "Contraseña",
                        keyboardType = KeyboardType.Password,
                        isError = passwordError
                    )

                    // ⚠️ Error Firebase
                    if (firebaseError.isNotEmpty()) {
                        androidx.compose.material3.Text(
                            text = firebaseError,
                            color = androidx.compose.material3.MaterialTheme.colorScheme.error
                        )
                    }

                    Spacer(modifier = androidx.compose.ui.Modifier.height(8.dp))

                    // 🚀 Crear cuenta (Firebase REAL)
                    FuturisticButton(
                        text = "Crear Cuenta",
                        onClick = {

                            // 🧠 Validaciones visuales
                            nombreError = nombre.isBlank()
                            edadError = edad.isBlank()
                            emailError = email.isBlank() || !email.contains("@")
                            passwordError = password.length < 6

                            if (
                                !nombreError &&
                                !edadError &&
                                !emailError &&
                                !passwordError
                            ) {
                                authViewModel.register(
                                    email = email,
                                    password = password
                                ) {
                                    // ✅ Registro exitoso → Main
                                    navController.navigate(Routes.Main.route) {
                                        popUpTo(Routes.Register.route) { inclusive = true }
                                    }
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}
