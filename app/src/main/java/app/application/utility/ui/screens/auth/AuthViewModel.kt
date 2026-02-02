package app.application.utility.ui.screens.auth

import android.content.Context
import androidx.lifecycle.ViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AuthViewModel : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    private val _error = MutableStateFlow("")
    val error: StateFlow<String> = _error

    private fun createUserInFirestore(uid: String, email: String) {
        val userMap = mapOf(
            "email" to email,
            "isAdmin" to (email.lowercase() == "diegoruperti1987@hotmail.com"),
            "createdAt" to System.currentTimeMillis()
        )
        db.collection("users").document(uid).set(userMap, SetOptions.merge())
    }

    fun login(email: String, password: String, onSuccess: () -> Unit) {
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

    fun register(email: String, password: String, onSuccess: () -> Unit) {
        if (email.isBlank() || password.length < 6) {
            _error.value = "Datos inválidos (mínimo 6 caracteres)"
            return
        }

        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { result ->
                _error.value = ""
                result.user?.let { firebaseUser ->
                    createUserInFirestore(firebaseUser.uid, email)
                }
                onSuccess()
            }
            .addOnFailureListener { exception ->
                _error.value = exception.message ?: "Error al registrar"
            }
    }

    fun signInWithGoogle(idToken: String, onSuccess: () -> Unit) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnSuccessListener { authResult ->
                _error.value = ""
                authResult.user?.let { firebaseUser ->
                    createUserInFirestore(firebaseUser.uid, firebaseUser.email ?: "")
                }
                onSuccess()
            }
            .addOnFailureListener { exception ->
                _error.value = exception.message ?: "Error con Google"
            }
    }

    fun logout(context: Context, onSuccess: () -> Unit) {
        auth.signOut()
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()
        val googleSignInClient = GoogleSignIn.getClient(context, gso)

        googleSignInClient.signOut().addOnCompleteListener {
            googleSignInClient.revokeAccess().addOnCompleteListener {
                _error.value = ""
                onSuccess()
            }
        }
    }
}