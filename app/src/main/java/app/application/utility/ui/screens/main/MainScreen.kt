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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import app.application.utility.ui.components.BaseScreen
import app.application.utility.ui.navigation.Routes
import app.application.utility.ui.screens.auth.AuthViewModel

@Composable
fun MainScreen(navController: NavController) {
    val authViewModel: AuthViewModel = viewModel()
    val context = LocalContext.current

    // 🎨 Cambiado a isDark = false para el fondo Blanco Humo Premium
    BaseScreen(title = "Herramientas", isDark = false) {
        AnimatedVisibility(
            visible = true,
            enter = fadeIn() + slideInVertically()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Cabecera del Menú
                Column(modifier = Modifier.padding(bottom = 8.dp)) {
                    Text(
                        text = "UTILITY BOX",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF00E5FF)
                    )
                    Text(
                        text = "Menú Principal",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFF2D3436)
                    )
                }

                // Opciones del Menú
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

                Spacer(modifier = Modifier.weight(1f))

                // 🚪 Logout que devuelve al Selector (Arquitectura Centralizada)
                MenuCard(
                    text = "Volver al Inicio",
                    icon = Icons.Filled.ArrowBack,
                    isLogout = false,
                    colorAccent = Color(0xFF2D3436)
                ) {
                    navController.navigate(Routes.Selector.route) {
                        popUpTo(Routes.Main.route) { inclusive = true }
                    }
                }
            }
        }
    }
}

@Composable
fun MenuCard(
    text: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isLogout: Boolean = false,
    colorAccent: Color = Color(0xFF00E5FF),
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.97f else 1f,
        animationSpec = tween(150),
        label = "scale"
    )

    // Estética coherente: Fondo blanco, borde suave y acentos en Cyan
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .scale(scale)
            .background(Color.White, RoundedCornerShape(22.dp))
            .border(
                width = 1.dp,
                color = Color.LightGray.copy(alpha = 0.3f),
                shape = RoundedCornerShape(22.dp)
            )
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
            .padding(20.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .background(colorAccent.copy(alpha = 0.1f), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = text,
                    tint = colorAccent,
                    modifier = Modifier.size(26.dp)
                )
            }

            Text(
                text = text,
                color = Color(0xFF2D3436),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.weight(1f))

            Icon(
                imageVector = Icons.Filled.ChevronRight,
                contentDescription = null,
                tint = Color.LightGray,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}