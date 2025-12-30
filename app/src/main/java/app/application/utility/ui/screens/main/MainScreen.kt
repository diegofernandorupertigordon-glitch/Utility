// 📦 Paquete donde vive esta pantalla
package app.application.utility.ui.screens.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import app.application.saludo.ui.theme.SaludoTheme
import app.application.utility.ui.navigation.Routes
import app.application.utility.ui.screens.auth.AuthViewModel

// ============================
// 🔥 PANTALLA PRINCIPAL
// ============================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavController) {

    // 🔥 Auth ViewModel
    val authViewModel: AuthViewModel = viewModel()

    // 🌌 Fondo futurista degradado
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFF0A0F1F),
                        Color(0xFF101A2F),
                        Color(0xFF0A0F1F)
                    )
                )
            )
    ) {

        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = "App de Utilidades",
                            color = Color(0xFF64FFDA),
                            style = MaterialTheme.typography.headlineMedium
                        )
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = Color(0xFF0F172A)
                    )
                )
            }
        ) { padding ->

            AnimatedVisibility(
                visible = true,
                enter = fadeIn() + slideInVertically()
            ) {

                Column(
                    modifier = Modifier
                        .padding(padding)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(18.dp)
                ) {

                    MenuCard("Calculadora", Icons.Filled.Calculate) {
                        navController.navigate(Routes.Calculadora.route)
                    }

                    MenuCard("Verificar Edad", Icons.Filled.Person) {
                        navController.navigate(Routes.Edad.route)
                    }

                    MenuCard("Número Primo", Icons.Filled.Star) {
                        navController.navigate(Routes.Primo.route)
                    }

                    MenuCard("Mayor o Menor", Icons.Filled.SwapVert) {
                        navController.navigate(Routes.MayorMenor.route)
                    }

                    MenuCard("Saludo", Icons.Filled.Face) {
                        navController.navigate(Routes.Saludo.route)
                    }

                    // 🚪 LOGOUT (SEGURO)
                    MenuCard("Cerrar Sesión", Icons.Filled.Logout) {
                        authViewModel.logout {
                            navController.navigate(Routes.Login.route) {
                                popUpTo(Routes.Main.route) { inclusive = true }
                            }
                        }
                    }
                }
            }
        }
    }
}

// ============================
// 💎 CARD FUTURISTA PREMIUM
// ============================
@Composable
fun MenuCard(
    text: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.96f else 1f,
        animationSpec = tween(120),
        label = ""
    )

    val glowBrush = Brush.horizontalGradient(
        listOf(Color(0xFF64FFDA), Color(0xFF38BDF8))
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .scale(scale)
            .border(1.5.dp, glowBrush, RoundedCornerShape(22.dp))
            .background(Color(0xFF111827), RoundedCornerShape(22.dp))
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
            .padding(18.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = text,
                tint = Color(0xFF64FFDA),
                modifier = Modifier.size(34.dp)
            )

            Text(
                text = text,
                color = Color.White,
                style = MaterialTheme.typography.titleLarge
            )
        }
    }
}

// ============================
// 👁️ Preview
// ============================
@Preview(showBackground = true)
@Composable
fun MainPreview() {
    SaludoTheme {
        MainScreen(rememberNavController())
    }
}
