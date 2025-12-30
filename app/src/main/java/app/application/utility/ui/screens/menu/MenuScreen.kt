package app.application.utility.ui.screens.menu

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Numbers
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import app.application.utility.ui.components.BaseScreen
import app.application.utility.ui.components.MenuCard
import app.application.utility.ui.navigation.Routes

/**
 * 🏠 Menú principal de la aplicación
 */
@Composable
fun MenuScreen(navController: NavController) {

    // 🧩 Pantalla base reutilizable
    BaseScreen(title = "Menú Principal") {

        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // 👤 Verificador de edad
            MenuCard(
                title = "Verificador de Edad",
                description = "Comprueba si eres mayor o menor",
                icon = {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                },
                onClick = {
                    navController.navigate(Routes.Edad.route)
                }
            )

            // 🔢 Número primo
            MenuCard(
                title = "Número Primo",
                description = "Determina si un número es primo",
                icon = {
                    Icon(
                        imageVector = Icons.Default.Numbers,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                },
                onClick = {
                    navController.navigate(Routes.Primo.route)
                }
            )

            // 🔼 Mayor y menor
            MenuCard(
                title = "Mayor y Menor",
                description = "Compara dos números",
                icon = {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                },
                onClick = {
                    navController.navigate(Routes.MayorMenor.route)
                }
            )

            // ➕ Calculadora
            MenuCard(
                title = "Calculadora",
                description = "Operaciones básicas",
                icon = {
                    Icon(
                        imageVector = Icons.Default.Calculate,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                },
                onClick = {
                    navController.navigate(Routes.Calculadora.route)
                }
            )

            // 👋 Saludo (NUEVO)
            MenuCard(
                title = "Saludo",
                description = "Mensaje personalizado",
                icon = {
                    Icon(
                        imageVector = Icons.Default.Face,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                },
                onClick = {
                    navController.navigate(Routes.Saludo.route)
                }
            )
        }
    }
}
