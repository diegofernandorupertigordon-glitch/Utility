package app.application.utility.ui.screens.auth

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException


/**
 * 🔐 LoginScreen
 * - Login con Email/Password (Firebase)
 * - Glow rojo dinámico
 * - Navegación segura
 * - Google SOLO UI (sin dependencias aún)
 */
@Composable
fun LoginScreen(navController: NavController) {

    // 📌 Estados del formulario
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // 🔴 Estados visuales (glow rojo)
    var emailError by remember { mutableStateOf(false) }
    var passwordError by remember { mutableStateOf(false) }

    // 🔥 ViewModel Firebase
    val authViewModel: AuthViewModel = viewModel()
    val firebaseError by authViewModel.error.collectAsState()

    // 🌐 Configuración Google Sign-In
    val context = navController.context

    val googleSignInClient = remember {
        GoogleSignIn.getClient(
            context,
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(
                    context.getString(
                        context.resources.getIdentifier(
                            "default_web_client_id",
                            "string",
                            context.packageName
                        )
                    )
                )
                .requestEmail()
                .build()
        )
    }
    // 🚀 Launcher Google
    val googleLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->

        if (result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)

            try {
                val account = task.getResult(ApiException::class.java)
                val idToken = account.idToken

                if (idToken != null) {
                    authViewModel.signInWithGoogle(idToken) {
                        navController.navigate(Routes.Main.route) {
                            popUpTo(Routes.Login.route) { inclusive = true }
                        }
                    }
                }

            } catch (e: ApiException) {
                // Error controlado
            }
        }
    }

    BaseScreen(title = "Iniciar Sesión") {

        AnimatedVisibility(
            visible = true,
            enter = fadeIn() + slideInVertically()
        ) {

            CardContainer {

                Column(
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {

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

                    // 🔐 Sign In (Firebase real)
                    FuturisticButton(
                        text = "Sign In",
                        onClick = {

                            // 🧠 Validación visual
                            emailError = email.isBlank() || !email.contains("@")
                            passwordError = password.length < 6

                            // 🚀 Login Firebase
                            if (!emailError && !passwordError) {
                                authViewModel.login(
                                    email = email,
                                    password = password
                                ) {
                                    navController.navigate(Routes.Main.route) {
                                        popUpTo(Routes.Login.route) { inclusive = true }
                                    }
                                }
                            }
                        }
                    )

                    // 🌐 Google (Firebase real)
                    FuturisticButton(
                        text = "Continuar con Google",
                        onClick = {
                            googleSignInClient.signOut() // evita cuentas cacheadas
                            googleLauncher.launch(googleSignInClient.signInIntent)
                        }
                    )


                    // 📝 Registro
                    FuturisticButton(
                        text = "Crear cuenta",
                        onClick = {
                            navController.navigate(Routes.Register.route)
                        }
                    )
                }
            }
        }
    }
}
