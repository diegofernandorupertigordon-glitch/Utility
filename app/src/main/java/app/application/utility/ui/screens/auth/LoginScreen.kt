package app.application.utility.ui.screens.auth

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import app.application.utility.R
import app.application.utility.ui.components.BaseScreen
import app.application.utility.ui.components.CardContainer
import app.application.utility.ui.components.FuturisticButton
import app.application.utility.ui.components.FuturisticTextField
import app.application.utility.ui.navigation.Routes
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException

@Composable
fun LoginScreen(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val authViewModel: AuthViewModel = viewModel()
    val firebaseError by authViewModel.error.collectAsState()
    val context = LocalContext.current
    val NeonCyan = Color(0xFF00E5FF)

    val googleLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)
                account?.idToken?.let { token ->
                    authViewModel.signInWithGoogle(token) {
                        navController.navigate(Routes.Selector.route) {
                            popUpTo(Routes.Login.route) { inclusive = true }
                        }
                    }
                }
            } catch (e: ApiException) { }
        }
    }

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

            // Cabecera Premium
            Text("BIENVENIDO", fontSize = 12.sp, fontWeight = FontWeight.Black, color = NeonCyan, letterSpacing = 2.sp)
            Text("Iniciar Sesión", fontSize = 36.sp, fontWeight = FontWeight.Black, color = Color(0xFF2D3436))

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
                        label = "Contraseña",
                        keyboardType = KeyboardType.Password
                    )

                    if (firebaseError.isNotEmpty()) {
                        Text(text = firebaseError, color = Color.Red, fontSize = 12.sp, modifier = Modifier.padding(horizontal = 4.dp))
                    }

                    FuturisticButton(
                        text = "ENTRAR",
                        onClick = {
                            authViewModel.login(email, password) {
                                navController.navigate(Routes.Selector.route) {
                                    popUpTo(Routes.Login.route) { inclusive = true }
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 8.dp)) {
                        HorizontalDivider(modifier = Modifier.weight(1f), color = Color.LightGray.copy(alpha = 0.4f))
                        Text(" O ", color = Color.Gray, fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 12.dp))
                        HorizontalDivider(modifier = Modifier.weight(1f), color = Color.LightGray.copy(alpha = 0.4f))
                    }

                    // Botón de Google Estilizado
                    FuturisticButton(
                        text = "CONTINUA CON GOOGLE",
                        onClick = {
                            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                                .requestIdToken(context.getString(R.string.default_web_client_id))
                                .requestEmail()
                                .build()
                            val client = GoogleSignIn.getClient(context, gso)
                            client.signOut().addOnCompleteListener {
                                googleLauncher.launch(client.signInIntent)
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            TextButton(onClick = { navController.navigate(Routes.Register.route) }) {
                Text("¿No tienes cuenta? REGÍSTRATE", color = Color.Gray, fontWeight = FontWeight.Black, fontSize = 13.sp)
            }

            TextButton(onClick = {
                navController.navigate(Routes.Selector.route) {
                    popUpTo(Routes.Login.route) { inclusive = true }
                }
            }) {
                Text("VOLVER AL INICIO", color = NeonCyan, fontSize = 11.sp, fontWeight = FontWeight.Black)
            }
        }
    }
}