package app.application.utility.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BaseScreen(
    title: String,
    isDark: Boolean = false, // Falso por defecto para usar el blanco premium
    content: @Composable (PaddingValues) -> Unit
) {
    // Selección de fondo dinámica
    val backgroundBrush = if (isDark) {
        Brush.verticalGradient(listOf(Color(0xFF0A0F1F), Color(0xFF101A2F), Color(0xFF0A0F1F)))
    } else {
        Brush.verticalGradient(listOf(Color(0xFFF8FAFC), Color(0xFFFFFFFF)))
    }

    val contentColor = if (isDark) Color(0xFF00E5FF) else Color(0xFF2D3436)
    val topBarColor = if (isDark) Color(0xFF0F172A) else Color.White.copy(alpha = 0.9f)

    Box(modifier = Modifier.fillMaxSize().background(backgroundBrush)) {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                if (title.isNotEmpty()) {
                    CenterAlignedTopAppBar(
                        title = {
                            Text(
                                text = title,
                                style = MaterialTheme.typography.headlineMedium,
                                color = contentColor
                            )
                        },
                        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                            containerColor = topBarColor
                        )
                    )
                }
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(16.dp)
                    .fillMaxSize()
            ) {
                content(paddingValues)
            }
        }
    }
}