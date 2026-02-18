package app.application.utility.ui.components

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import app.application.utility.ui.navigation.Routes
import app.application.utility.ui.screens.auth.AuthViewModel
import coil.compose.AsyncImage
import com.google.firebase.auth.FirebaseAuth
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.header
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfumeriaDrawer(
    authViewModel: AuthViewModel,
    navController: NavController,
    onCloseDrawer: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val user = FirebaseAuth.getInstance().currentUser
    val userEmail = user?.email ?: "usuario@perfumeria.com"
    val userName = user?.displayName ?: userEmail.split("@")[0].replaceFirstChar { it.uppercase() }

    val supabaseUrl = "https://dazhrsgqecvyivnmexai.supabase.co"
    val supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImRhemhyc2dxZWN2eWl2bm1leGFpIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NzAyNDg0OTQsImV4cCI6MjA4NTgyNDQ5NH0.kWWmZrSct-15EJCRiFwTUlZPCe34V0ApV5M6uP6iOzk"
    val bucketName = "avatars"
    val fileName = "${userEmail}.jpg"
    val publicUrl = "$supabaseUrl/storage/v1/object/public/$bucketName/$fileName"

    var profileUrl by remember { mutableStateOf<Any?>(publicUrl) }
    var isUploading by remember { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let { selectedUri ->
            isUploading = true
            scope.launch {
                try {
                    val bytes = withContext(Dispatchers.IO) {
                        context.contentResolver.openInputStream(selectedUri)?.use { it.readBytes() }
                    }
                    if (bytes != null) {
                        val client = HttpClient(CIO)
                        val response: HttpResponse = client.put("$supabaseUrl/storage/v1/object/$bucketName/$fileName") {
                            header("Authorization", "Bearer $supabaseKey")
                            header("apikey", supabaseKey)
                            setBody(bytes)
                        }
                        if (response.status == HttpStatusCode.OK || response.status == HttpStatusCode.Created) {
                            profileUrl = null
                            profileUrl = selectedUri
                            Toast.makeText(context, "Perfil actualizado", Toast.LENGTH_SHORT).show()
                        }
                        client.close()
                    }
                } catch (e: Exception) {
                    Toast.makeText(context, "Error: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
                } finally {
                    isUploading = false
                }
            }
        }
    }

    ModalDrawerSheet(
        drawerContainerColor = Color(0xFFF8FAFC),
        modifier = Modifier.fillMaxHeight().width(310.dp)
    ) {
        // --- HEADER DEL DRAWER ---
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(210.dp)
                .background(Brush.verticalGradient(listOf(Color(0xFF0012FF), Color(0xFF000A8B))))
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(90.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.2f))
                        .clickable { if (!isUploading) launcher.launch("image/*") },
                    contentAlignment = Alignment.Center
                ) {
                    if (isUploading) {
                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(30.dp))
                    } else {
                        AsyncImage(
                            model = profileUrl,
                            contentDescription = "Foto",
                            modifier = Modifier.fillMaxSize().clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                        Box(
                            modifier = Modifier.align(Alignment.BottomEnd).size(24.dp).background(Color.White, CircleShape).padding(4.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.CameraAlt, null, tint = Color(0xFF0012FF), modifier = Modifier.size(14.dp))
                        }
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(userName, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text(userEmail, color = Color.White.copy(alpha = 0.7f), fontSize = 13.sp)
            }
        }

        // --- OPCIONES DEL DRAWER (Navegación Corregida) ---
        Column(modifier = Modifier.padding(12.dp)) {
            DrawerItemCustom("Sobre Nosotros", Icons.Default.Info) {
                navController.navigate("${Routes.Manual.route}?seccion=NOSOTROS")
            }
            DrawerItemCustom("Garantía de Autenticidad", Icons.Default.Verified) {
                navController.navigate("${Routes.Manual.route}?seccion=GARANTIA")
            }
            DrawerItemCustom("Manual de Usuario", Icons.AutoMirrored.Filled.MenuBook) {
                navController.navigate("${Routes.Manual.route}?seccion=MANUAL")
            }
            DrawerItemCustom("Ubicación Matriz", Icons.Default.LocationOn) {
                navController.navigate("${Routes.Manual.route}?seccion=MATRIZ")
            }

            Spacer(modifier = Modifier.weight(1f))

            DrawerItemCustom("Cerrar Sesión", Icons.AutoMirrored.Filled.ExitToApp) {
                authViewModel.logout(context) {
                    navController.navigate(Routes.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            }
        }
    }
}

@Composable
fun DrawerItemCustom(label: String, icon: ImageVector, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, null, tint = Color(0xFF455A64), modifier = Modifier.size(22.dp))
        Spacer(modifier = Modifier.width(18.dp))
        Text(label, color = Color(0xFF263238), fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
    }
}