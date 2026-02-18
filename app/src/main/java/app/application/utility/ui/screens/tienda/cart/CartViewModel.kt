package app.application.utility.ui.screens.tienda.cart

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModel
import app.application.utility.R
import app.application.utility.ui.model.Product
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class CartItem(
    val product: Product,
    val quantity: Int
)

class CartViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems

    private fun generarTicketImagen(context: Context, total: Double, items: List<CartItem>): Uri? {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.ticket_layout, null)

        val cliente = auth.currentUser?.email?.split("@")?.get(0) ?: "Cliente Premium"
        val fecha = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date())

        view.findViewById<TextView>(R.id.txtInfoPedido).text = "CLIENTE: ${cliente.uppercase()}\nFECHA: $fecha"

        val table = view.findViewById<TableLayout>(R.id.tableProducts)

        items.forEach { item ->
            val row = TableRow(context).apply {
                setPadding(0, 16, 0, 8)
            }

            val tvCant = TextView(context).apply {
                text = "${item.quantity} "
                textSize = 13f
                setTextColor(Color.BLACK)
                setTypeface(null, android.graphics.Typeface.BOLD)
            }

            val tvProd = TextView(context).apply {
                // Se mantienen tus variables originales sin 'marca' para evitar errores de compilación
                val detalles = "${item.product.nombre.uppercase()}\n" +
                        "${item.product.presentacionMl} ${item.product.unidad}"
                text = detalles
                textSize = 12f
                setTextColor(Color.DKGRAY)
                setPadding(10, 0, 0, 0)
            }

            val tvSub = TextView(context).apply {
                text = "$${String.format(Locale.US, "%.2f", item.product.precio * item.quantity)}"
                gravity = android.view.Gravity.END
                textSize = 13f
                setTextColor(Color.BLACK)
                setTypeface(null, android.graphics.Typeface.BOLD)
            }

            row.addView(tvCant)
            row.addView(tvProd)
            row.addView(tvSub)
            table.addView(row)

            val divider = View(context).apply {
                layoutParams = TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, 1)
                setBackgroundColor(Color.parseColor("#EEEEEE"))
            }
            table.addView(divider)
        }

        val subtotalSinIva = total / 1.15
        val iva = total - subtotalSinIva
        view.findViewById<TextView>(R.id.txtSubtotal).text = "Subtotal: $${String.format(Locale.US, "%.2f", subtotalSinIva)}"
        view.findViewById<TextView>(R.id.txtIva).text = "IVA 15%: $${String.format(Locale.US, "%.2f", iva)}"
        view.findViewById<TextView>(R.id.txtTotal).text = "TOTAL: $${String.format(Locale.US, "%.2f", total)}"

        view.measure(
            View.MeasureSpec.makeMeasureSpec(1000, View.MeasureSpec.EXACTLY),
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        )
        view.layout(0, 0, view.measuredWidth, view.measuredHeight)

        val bitmap = Bitmap.createBitmap(view.measuredWidth, view.measuredHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)

        return try {
            val file = File(context.cacheDir, "ticket_pedido.jpg")
            FileOutputStream(file).use { bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it) }
            FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun enviarTicketWhatsApp(context: Context, total: Double, items: List<CartItem>) {
        val ticketUri = generarTicketImagen(context, total, items)

        if (ticketUri != null) {
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "image/jpeg"
                putExtra(Intent.EXTRA_STREAM, ticketUri)
                putExtra("jid", "593987413450@s.whatsapp.net")
                putExtra(Intent.EXTRA_TEXT, "✅ *NUEVO PEDIDO CONFIRMADO*\n\nHola Perfumería Integral, adjunto mi ticket de compra para agendar el despacho de mis productos. ¡Muchas gracias!")
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(Intent.createChooser(intent, "Abriendo WhatsApp..."))
        }
    }

    fun addProduct(product: Product): Boolean {
        val currentItems = _cartItems.value.toMutableList()
        val index = currentItems.indexOfFirst { it.product.id == product.id }
        if (index >= 0) {
            val item = currentItems[index]
            if (item.quantity < product.stock) {
                currentItems[index] = item.copy(quantity = item.quantity + 1)
                _cartItems.value = currentItems
                return true
            } else return false
        } else {
            if (product.stock > 0) {
                currentItems.add(CartItem(product, 1))
                _cartItems.value = currentItems
                return true
            } else return false
        }
    }

    fun removeProduct(productId: String) {
        val currentItems = _cartItems.value.toMutableList()
        val index = currentItems.indexOfFirst { it.product.id == productId }
        if (index >= 0) {
            val item = currentItems[index]
            if (item.quantity > 1) currentItems[index] = item.copy(quantity = item.quantity - 1)
            else currentItems.removeAt(index)
            _cartItems.value = currentItems
        }
    }

    fun deleteItemCompletely(productId: String) {
        _cartItems.value = _cartItems.value.filter { it.product.id != productId }
    }

    fun clearCart() { _cartItems.value = emptyList() }

    fun total(): Double = _cartItems.value.sumOf { it.product.precio * it.quantity.toDouble() }

    // --- FUNCIÓN CHECKOUT CORREGIDA ---
    fun checkout(context: Context, onSuccess: () -> Unit, onError: (String) -> Unit) {
        val itemsToProcess = _cartItems.value
        val userEmail = auth.currentUser?.email ?: "Anónimo"
        val totalVenta = total()

        if (itemsToProcess.isEmpty()) return

        db.runTransaction { transaction ->
            // 1. LECTURAS: Se obtienen todos los documentos primero
            val snapshots = itemsToProcess.map { item ->
                val ref = db.collection("products").document(item.product.id)
                ref to transaction.get(ref)
            }

            // 2. ESCRITURAS: Se aplican los cambios después de las lecturas
            snapshots.forEachIndexed { index, pair ->
                val item = itemsToProcess[index]
                val productRef = pair.first
                val snapshot = pair.second

                val currentStock = snapshot.getLong("stock") ?: 0L
                val currentVendidos = snapshot.getLong("vendidos") ?: 0L

                if (currentStock >= item.quantity.toLong()) {
                    transaction.update(productRef, "stock", currentStock - item.quantity)
                    transaction.update(productRef, "vendidos", currentVendidos + item.quantity)
                } else {
                    throw Exception("Stock insuficiente para: ${item.product.nombre}")
                }
            }

            val saleRef = db.collection("sales").document()
            transaction.set(saleRef, hashMapOf(
                "id" to saleRef.id,
                "cliente" to userEmail,
                "total" to totalVenta,
                "fecha" to System.currentTimeMillis()
            ))
        }.addOnSuccessListener {
            enviarTicketWhatsApp(context, totalVenta, itemsToProcess)
            clearCart()
            onSuccess()
        }.addOnFailureListener { e -> onError(e.message ?: "Error") }
    }
}