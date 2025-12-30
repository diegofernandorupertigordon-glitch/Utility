package app.application.utility.ui.screens.auth

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AuthViewModel : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    // 🔴 Error observable para la UI
    private val _error = MutableStateFlow("")
    val error: StateFlow<String> = _error

    // 🔐 LOGIN con Email y Password
    fun login(
        email: String,
        password: String,
        onSuccess: () -> Unit
    ) {
        if (email.isBlank() || password.length < 6) {
            _error.value = "Email o contraseña inválidos"
            return
        }

        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                _error.value = ""
                onSuccess()
            }
            .addOnFailureListener { exception ->
                _error.value = exception.message ?: "Error al iniciar sesión"
            }
    }

    // 📝 REGISTRO con Email y Password
    fun register(
        email: String,
        password: String,
        onSuccess: () -> Unit
    ) {
        if (email.isBlank() || password.length < 6) {
            _error.value = "Datos inválidos"
            return
        }

        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                _error.value = ""
                onSuccess()
            }
            .addOnFailureListener { exception ->
                _error.value = exception.message ?: "Error al registrar"
            }
    }

    // 🌐 LOGIN con Google (Firebase real)
    fun signInWithGoogle(
        idToken: String,
        onSuccess: () -> Unit
    ) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)

        auth.signInWithCredential(credential)
            .addOnSuccessListener {
                _error.value = ""
                onSuccess()
            }
            .addOnFailureListener { exception ->
                _error.value = exception.message ?: "Error con Google"
            }
    }
    // 🚪 LOGOUT
    fun logout(onSuccess: () -> Unit) {
        auth.signOut()
        _error.value = ""
        onSuccess()
    }

}
